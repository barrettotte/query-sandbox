-- test queries related order metrics distribution

-- Check metrics
select count(*), state_start, state_end, status
from order_metrics
group by state_start, state_end, status
order by state_start;
/*
 count |     state_start     |      state_end      |   status    
-------+---------------------+---------------------+-------------
 50000 | 2023-12-02 12:00:00 |                     | IN_PROGRESS
 50000 | 2024-01-02 12:00:00 |                     | IN_PROGRESS
 50000 | 2024-02-02 12:00:00 |                     | IN_PROGRESS
 50000 | 2024-03-02 12:00:00 |                     | IN_PROGRESS
 50000 | 2024-04-02 12:00:00 |                     | IN_PROGRESS
  5000 | 2024-05-02 12:00:00 | 2024-05-07 12:00:00 | OPEN
 20000 | 2024-05-02 12:00:00 |                     | OPEN
  5000 | 2024-05-07 12:00:00 |                     | TEST_STATUS
*/


-- status distribution in recent past
with dist as (
  select o.id, o.description, m.state_start, m.state_end, 
    m.status, m.category, m.priority, m.hidden, m.type, m.assignee_ids
  from orders as o
  join order_metrics as m on m.order_id=o.id
    and m.state_start <= '2024-05-02 23:59:59'::timestamp without time zone
    and (m.state_end is null or m.state_end > '2024-05-02 23:59:59'::timestamp without time zone)
)
select count(*), status from dist group by status;
/*
 count  |   status    
--------+-------------
 250000 | IN_PROGRESS
  25000 | OPEN
*/

-- status distribution of current time
with dist as (
  select o.id, o.description, m.state_start, m.state_end, 
    m.status, m.category, m.priority, m.hidden, m.type, m.assignee_ids
  from orders as o
  join order_metrics as m on m.order_id=o.id
    and m.state_start <= now()
    and (m.state_end is null or m.state_end > now())
)
select count(*), status from dist group by status;
/*
 250000 | IN_PROGRESS
  20000 | OPEN
   5000 | TEST_STATUS
*/


-- status distribution of long time ago
with dist as (
  select o.id, o.description, m.state_start, m.state_end, 
    m.status, m.category, m.priority, m.hidden, m.type, m.assignee_ids
  from orders as o
  join order_metrics as m on m.order_id=o.id
    and m.state_start <= '2024-01-31 12:00:00'
    and (m.state_end is null or m.state_end > '2024-01-31 12:00:00')
)
select count(*), status from dist group by status;
/*
 count  |   status    
--------+-------------
 100000 | IN_PROGRESS
*/
