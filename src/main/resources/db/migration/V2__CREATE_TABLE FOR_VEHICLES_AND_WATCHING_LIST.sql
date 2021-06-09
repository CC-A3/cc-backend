create table vehicle(
    id serial primary key,
    title varchar(512) not null,
    price bigint not null,
    kilometers int not null,
    colour varchar(255) not null,
    body varchar(512) not null,
    engine varchar(255) not null,
    transmission varchar(255) not null,
    fuel_consumption varchar(255) not null,
    type varchar(255) not null,
    status varchar(255) not null,
    owner_id serial not null
);

create table watch_list(
    client_id serial not null references client (id),
    vehicle_id serial not null references  vehicle (id),
    unique (client_id,vehicle_id)
)