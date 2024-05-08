-- test queries related order metrics collection


-- orders that have not been recorded yet
with recently_created as (
  select now() as state_start, null as state_end, o.id, o.status, 
    o.category, o.priority, o.type, o.hidden, o.assignee_ids
  from orders as o
  where not exists (
    select m.order_id 
    from order_metrics as m 
    where m.order_id=o.id
  )
)
select count(*) from recently_created;


-- add orders that have not been recorded
insert into order_metrics (order_id, state_start, state_end, status, category, priority, type, hidden, assignee_ids)
select o.id, o.created as state_start, null as state_end, o.status, o.category, 
  o.priority, o.type, o.hidden, o.assignee_ids
from orders as o
where not exists (
  select m.order_id
  from order_metrics as m
  where m.order_id=o.id
);


-- update a couple orders, then check above changed query
select id, status from orders where status='OPEN' order by id limit 5000;

select order_id, state_start, state_end, status
from order_metrics where order_id in (
  select id from orders where status='OPEN' order by id limit 5000
);

update orders set status='TEST_STATUS' where id in (
  select id from orders where status='OPEN' order by id limit 5000
);

select id, status from orders where status='TEST_STATUS' order by id limit 5000;
-- 5 entries need updating


-- orders that have changed and have not been recorded yet
with changed as (
  select id, o.status, o.category, o.priority, o.type, o.hidden, o.assignee_ids
  from order_metrics as m
  join orders as o on m.order_id = o.id
  where m.state_end is null and (
    o.status <> m.status or
    o.category <> m.category or
    o.priority <> m.priority or
    -- note: type should never change once its created
    o.hidden <> m.hidden or
    o.assignee_ids <> m.assignee_ids
  )
)
select changed.id, changed.status from changed;


-- update order metrics that changed to record state end
with changed as (
  select id, o.status, o.category, o.priority, o.type, o.hidden, o.assignee_ids
  from order_metrics as m
  join orders as o on m.order_id = o.id
  where m.state_end is null and (
    o.status <> m.status or
    o.category <> m.category or
    o.priority <> m.priority or
    o.hidden <> m.hidden or
    o.assignee_ids <> m.assignee_ids
  )
)
update order_metrics
set state_end='2024-05-07 12:00:00'::timestamp without time zone
from changed
where changed.id=order_metrics.order_id
  and order_metrics.state_end is null;


-- orders that had their new state recorded and now need a new metrics row
-- with a blank state_end
with new_metrics as (
  select '2024-05-07 12:00:00'::timestamp without time zone as state_start, null as state_end, o.id, o.status,
    o.category, o.priority, o.type, o.hidden, o.assignee_ids
  from orders as o
  where not exists (
    select m.order_id
    from order_metrics as m
    where m.order_id=o.id and m.state_end is null
  )
)
select count(*) from new_metrics;


-- add a new state metric for order that just had its state change recorded
with new_metrics as (
  select '2024-05-07 12:00:00'::timestamp without time zone as state_start, null as state_end, o.id, o.status,
    o.category, o.priority, o.type, o.hidden, o.assignee_ids
  from orders as o
  where not exists (
    select m.order_id
    from order_metrics as m
    where m.order_id=o.id and m.state_end is null
  )
)
insert into order_metrics (order_id, state_start, state_end, status, category, priority, type, hidden, assignee_ids)
select o.id, '2024-05-07 12:00:00'::timestamp without time zone as state_start, null as state_end, o.status, 
  o.category, o.priority, o.type, o.hidden, o.assignee_ids
from orders as o
where not exists (
  select m.order_id
  from order_metrics as m
  where m.order_id=o.id and m.state_end is null
);


-- check metrics for test rows
select count(*), state_start, status
from order_metrics
where order_id in (
  select id from orders where status='TEST_STATUS' order by id limit 5000
)
group by state_start, status;
/*
 count |        state_start         |   status    
-------+----------------------------+-------------
  5000 | 2024-05-02 12:00:00        | OPEN
  5000 | 2024-05-07 18:51:14.363238 | TEST_STATUS
*/


-- check metrics per date
select count(*),state_start 
from order_metrics
where state_end is null
group by state_start
order by state_start;
