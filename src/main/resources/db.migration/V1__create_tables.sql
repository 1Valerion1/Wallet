-- Создание схемы wallet
create schema if not exists wallet;

create table if not exists users (
    id bigserial not null,
    birthday date,
    enabled boolean not null,
    creation_date timestamp with time zone,
    update_date timestamp with time zone,
    email varchar(50),
    first_name varchar(50),
    last_name varchar(50),
    patronymic varchar(50),
    password varchar(300),
    phone varchar(11),
    primary key (id)
);

create table if not exists sessions (
    active boolean not null,
    expiration_time timestamp,
    user_id bigint,
    foreign key (user_id) references users (id),
    value varchar(255) not null,
    primary key (value)
);