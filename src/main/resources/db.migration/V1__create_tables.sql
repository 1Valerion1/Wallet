create table users
(

    id            bigserial not null,
    phone         bigint    not null unique,
    birthday      date,
    enabled       boolean   not null,
    email         varchar   not null unique,
    password      varchar,
    first_name    varchar,
    last_name     varchar,
    creation_date timestamp,
    update_date   timestamp,
    primary key (id)
);

create table session
(
    value           varchar(255),
    user_id         bigint references users (id),
    active          boolean not null,
    expiration_time timestamp,
    primary key (value)
)