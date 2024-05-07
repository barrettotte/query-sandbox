-- test queries related order metrics trends

-- Check metrics
select count(*), state_start, state_end, status
from order_metrics
group by state_start, state_end, status
order by state_start;
/*
count |        state_start         |         state_end         |   status    
-------+----------------------------+---------------------------+-------------
 15000 | 2023-12-02 12:00:00        |                           | IN_PROGRESS
 15000 | 2024-01-02 12:00:00        |                           | IN_PROGRESS
 15000 | 2024-02-02 12:00:00        |                           | IN_PROGRESS
 15000 | 2024-03-02 12:00:00        |                           | IN_PROGRESS
 15000 | 2024-04-02 12:00:00        |                           | IN_PROGRESS
     5 | 2024-05-02 12:00:00        | 2024-05-06 18:27:56.25322 | OPEN
 24995 | 2024-05-02 12:00:00        |                           | OPEN
     5 | 2024-05-06 18:28:03.763419 |                           | TEST_STATUS
*/

-- status trends
with bucket_starts as (
  select generate_series(
    '2019-06-01'::timestamp without time zone,
    '2024-06-01'::timestamp without time zone,
    '1 month'
  ) as bucket_start
),
bucket_ranges as (
  select bucket_start, (bucket_start + interval '1 month') as bucket_end
  from bucket_starts
),
latest_order_metrics as (
  select m.order_id, b.bucket_start, max(m.state_start) as latest_start
  from order_metrics as m
  join bucket_ranges as b on m.state_start < b.bucket_end
    and (m.state_end is null or m.state_end >= b.bucket_start)
  group by m.order_id, b.bucket_start
),
bucket_contents as (
  select lm.order_id, lm.bucket_start, m.state_start, m.status
  from latest_order_metrics as lm
  join order_metrics as m on lm.order_id=m.order_id
    and lm.latest_start=m.state_start
)
select b.bucket_start, c.status, coalesce(count(c.status), 0) as bucket_count
from bucket_ranges as b
left join bucket_contents as c on b.bucket_start=c.bucket_start
where c.status is not null
  and c.status = 'OPEN'
group by b.bucket_start, c.status
order by b.bucket_start, c.status;


-- status trends (smaller and slightly faster)
with buckets as (
  select b.bucket_start, (b.bucket_start + interval '1 month') as bucket_end
  from (
    select generate_series(
      '2019-06-01'::timestamp without time zone,
      '2024-06-01'::timestamp without time zone,
      '1 month'
    ) as bucket_start
  ) as b
),
most_recent_in_bucket as (
  select m.order_id, b.bucket_start, m.state_start, m.state_end, m.status,
    rank() over (
      partition by m.order_id, b.bucket_start
      order by m.state_start desc
    ) as rank
  from order_metrics as m
  join buckets as b on m.state_start < b.bucket_end
    and (m.state_end is null or m.state_end >= b.bucket_start)
)
select extract(month from bucket_start) as bucket_month,
  extract(year from bucket_start) as bucket_year,
  status, count(*) as bucket_count, bucket_start
from most_recent_in_bucket
where rank = 1
group by bucket_start, status
order by bucket_start, status;
/*
 bucket_month | bucket_year |   status    | bucket_count |    bucket_start     
--------------+-------------+-------------+--------------+---------------------
           12 |        2023 | IN_PROGRESS |        15000 | 2023-12-01 00:00:00
            1 |        2024 | IN_PROGRESS |        30000 | 2024-01-01 00:00:00
            2 |        2024 | IN_PROGRESS |        45000 | 2024-02-01 00:00:00
            3 |        2024 | IN_PROGRESS |        60000 | 2024-03-01 00:00:00
            4 |        2024 | IN_PROGRESS |        75000 | 2024-04-01 00:00:00
            5 |        2024 | IN_PROGRESS |        75000 | 2024-05-01 00:00:00
            5 |        2024 | OPEN        |        24995 | 2024-05-01 00:00:00
            5 |        2024 | TEST_STATUS |            5 | 2024-05-01 00:00:00
            6 |        2024 | IN_PROGRESS |        75000 | 2024-06-01 00:00:00
            6 |        2024 | OPEN        |        24995 | 2024-06-01 00:00:00
            6 |        2024 | TEST_STATUS |            5 | 2024-06-01 00:00:00
*/

-- 5 years, 1 month buckets -> ~668ms
-- 5 years, 1 day buckets -> ~17s
-- 5 years, 1 hour buckets -> ~5mins
-- note: this many data points would probably never be used practically, just performance testing
