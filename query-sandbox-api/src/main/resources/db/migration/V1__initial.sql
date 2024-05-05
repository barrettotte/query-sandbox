-- initial table setup

create table orders (
    id text not null unique default gen_random_uuid(),
    created timestamp without time zone default now(),
    updated timestamp without time zone default now(),
    description text,
    status text not null,
    category text not null,
    priority text not null,
    type text not null,
    hidden boolean default false,
    assignee_ids text[] default array[]::text[],

    constraint orders_pk primary key (id)
);

create index idx_orders_type on orders (type);

-- metrics

create table order_metrics (
    order_id uuid not null,
    state_start timestamp without time zone default now(),
    state_end timestamp without time zone,
    status text not null,
    category text not null,
    priority text not null,
    type text not null,
    hidden boolean not null,
    assignee_ids text[] default array[]::text[],

    constraint order_metrics_pk primary key (order_id, state_start)
);

create index idx_order_metrics_order_id on order_metrics (order_id);
create index idx_order_metrics_state_start on order_metrics (state_start);
create index idx_order_metrics_type on order_metrics (type);
