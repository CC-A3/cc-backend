CREATE SCHEMA if not exists "ccbackend";
DROP TABLE IF EXISTS client;
create table client (
   id serial primary key,
   email varchar(255) unique not null,
   full_name varchar(255) not null,
   password varchar(100) not null,
   is_verified boolean not null,
   phone_number varchar(100)
);