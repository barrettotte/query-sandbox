-- initial table setup

create table flyway_test (
    id serial primary key,
    message text
);

insert into flyway_test (message) values ('Hello world');
