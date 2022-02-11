drop table if exists post;
create table post
(
    id serial primary key,
    name varchar(255),
    link text unique,
    description text,
    created timestamp
);