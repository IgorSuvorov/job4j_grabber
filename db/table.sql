drop table if exists posts;
create table posts
(
    id serial primary key,
    name varchar(255),
    link text unique,
    description text,
    created timestamp
);