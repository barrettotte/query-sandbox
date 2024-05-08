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


-- calculate deltas, age, inactivity
with
params as (
  select '2024-05-08 00:00:00'::timestamp without time zone as end_date
  -- select '2024-05-07 00:00:00'::timestamp without time zone as end_date
  -- select '2024-05-02 12:30:00'::timestamp without time zone as end_date
  -- select '2024-05-02 10:30:00'::timestamp without time zone as end_date
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
latest_deltas as (
  select order_id, max(end_time) as last_state_start
  from deltas
  where is_delta=true
  group by order_id
),
order_age as (
  select o.id as order_id, (select end_date from params) - o.created as age, o.created
  from orders as o
  join metrics as m on o.id=m.order_id and m.rn=1
),
data_combined as (
  select o.id as order_id, oa.age, d.is_delta,
    (select end_date from params) - coalesce(ld.last_state_start, o.created) as inactivity
  from orders as o
  join order_age as oa on o.id = oa.order_id
  left join latest_deltas as ld on o.id = ld.order_id
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


