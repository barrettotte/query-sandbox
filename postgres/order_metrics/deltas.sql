-- test queries related order metrics deltas


-- get latest state of each order
with latest_metrics as (
  select m.order_id, m.state_start, m.state_end, m.status,
    row_number() over (
      partition by m.order_id order by m.state_start desc
    ) as rn
  from order_metrics as m
)
select order_id, state_start, state_end, status
from latest_metrics
where rn=1
order by state_start desc, order_id;


-- calculate age and inactivity of each order of current time
with 
params as (
  select '2024-05-06 23:59:59'::timestamp without time zone as end_date
),
relevant_metrics as (
  select m.order_id, m.state_start, m.state_end, m.status,
    row_number() over (
      partition by m.order_id order by m.state_start desc
    ) as rn
  from order_metrics as m
  where ((m.state_start <= (select end_date from params)) or (m.state_end is null))
),
metric_calcs as (
  select o.id, o.created, r.state_start, r.state_end, o.status,
    coalesce(r.state_end, (select end_date from params)) - o.created as age,
    (select end_date from params) - r.state_start as inactivity
  from orders as o
  join relevant_metrics as r on o.id=r.order_id and r.rn = 1
)
select avg(age) as avg_age,
  max(age) as max_age,
  min(age) as min_age,
  avg(inactivity) as avg_inactivity,
  max(inactivity) as max_inactivity,
  min(inactivity) as min_inactivity,
  count(*) as total_count
from metric_calcs;
/*
avg_age        | 72 days 14:23:59
max_age        | 156 days 11:59:59
min_age        | 4 days 11:59:59
avg_inactivity | 72 days 14:23:40.555812
max_inactivity | 156 days 11:59:59
min_inactivity | 05:31:55.236581
total_count    | 100000

TODO: not quite right
*/


-- calculate deltas, age, inactivity
with
params as (
--   select least(now(), '2024-05-07 00:00:00'::timestamp without time zone) as end_date
--   select least(now(), '2024-05-06 00:00:00'::timestamp without time zone) as end_date
--   select least(now(), '2024-05-02 12:30:00'::timestamp without time zone) as end_date
  select least(now(), '2024-05-02 11:30:00'::timestamp without time zone) as end_date
),
metrics as (
  select order_id, state_start, state_end, status, category, priority, 
    row_number() over (
      partition by order_id 
      order by state_start
    ) as rn
  from order_metrics
  where state_start <= (select end_date from params)
),
deltas as (
  select a.order_id, 
    a.state_start as start_time, 
    b.state_start as end_time,
    ((a.status <> b.status) or (a.category <> b.category) or (a.priority <> b.priority)) as is_delta
  from metrics as a
  join metrics as b on a.order_id=b.order_id 
    and a.rn = b.rn - 1
),
latest_state as (
  select order_id, max(end_time) as last_state_start
  from deltas
  where is_delta=true
  group by order_id
),
order_age as (
  select id as order_id, (select end_date from params) - created as age
  from orders
),
data_combined as (
  select o.id as order_id, oa.age, d.is_delta,
    (select end_date from params) - coalesce(ls.last_state_start, o.created) as inactivity
  from orders as o
  left join order_age as oa on o.id = oa.order_id
  left join latest_state as ls on o.id = ls.order_id
  left join deltas as d on o.id = d.order_id
)
select min(age) as min_age,
  max(age) as max_age, 
  avg(age) as avg_age,
  min(inactivity) as min_inactivity,
  max(inactivity) as max_inactivity,
  avg(inactivity) as avg_inactivity,
  count(is_delta) as delta_count,
  (select count(distinct order_id) from metrics) as total_count
from data_combined;


