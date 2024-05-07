-- bulk insert a bunch of orders so metrics has something to collect

-- insert 25000 basic identical orders
insert into orders (
  id, created, updated, description, status, 
  category, priority, type, hidden, assignee_ids
)
select gen_random_uuid() as id, 
  '2024-05-02 12:00:00'::timestamp without time zone as created, 
  '2024-05-02 12:00:00'::timestamp without time zone as updated,
  ('test basic order ' || x.i::text) as description, 
  'OPEN' as status, 
  'CAT_1' as category, 
  'LOW' as priority,
  'BASIC' as type, 
  false as hidden, 
  array[]::text[] as assignee_ids
from generate_series(1, 25000) as x(i);

-- insert 75000 random orders
insert into orders (
  id, created, updated, description, status, 
  category, priority, type, hidden, assignee_ids
)
select gen_random_uuid() as id, 
  '2024-05-02 12:00:00'::timestamp without time zone - (interval '1 month' * (((i - 1) % 5) + 1)) as created,
  '2024-05-02 12:00:00'::timestamp without time zone - (interval '1 month' * (((i - 1) % 5) + 1)) as updated,
  ('rand order ' || x.i::text) as description,
  'IN_PROGRESS' as status,
  'CAT_' || (floor(random() * 4)) as category,
  case floor(random() * 4)
    when 0 then 'LOW'
    when 1 then 'MEDIUM'
    when 2 then 'HIGH'
    when 3 then 'CRTICIAL'
  end as priority, 
  case floor(random() * 3)
    when 0 then 'BASIC'
    when 1 then 'HAZARDOUS'
    when 2 then 'TOP_SECRET'
  end as type,
  false as hidden, 
  array[]::text[] as assignee_ids
from generate_series(1, 75000) as x(i);
