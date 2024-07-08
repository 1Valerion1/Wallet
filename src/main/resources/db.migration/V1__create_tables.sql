-- Создание схемы wallet
CREATE SCHEMA IF NOT EXISTS wallet;

create table users (
    id bigserial not null,
    birthday date,
    enabled boolean not null,
    creation_date timestamp with time zone,
    update_date timestamp with time zone,
    wallet_id bigint unique,
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    patronymic varchar(255),
    phone varchar(255),
    primary key (id)
);

create table session (
    active boolean not null,
    expiration_time timestamp(6),
    user_id bigint,
    value varchar(255) not null,
    primary key (value)
);

create table wallet (
    balance integer,
    user_id bigint unique,
    wallet_id bigserial not null,
    primary key (wallet_id)
);

create table money_transfer (
    amount int,
    creating_translation timestamp with time zone,
    user_id bigint,
    transfer_id uuid not null,
    comment varchar(255),
    receiver_phone varchar(255),
    receiver_wallet varchar(255),
    status varchar(255) check (status in ('PAID','UNPAID','CANCELLED')),
    transfer_type varchar(255) check (transfer_type in ('INCOMING','OUTGOING')),
    primary key (transfer_id)
);

create table payment_invoice (
    amount int,
    completed_at timestamp(6),
    receiver_id bigint,
    sender_id bigint,
    account_number uuid not null,
    comment varchar(255),
    status varchar(255) check (status in ('PAID','UNPAID','CANCELLED')),
    primary key (account_number)
);

create table users_money_transfers (
    user_id bigint not null,
    money_transfers_transfer_id uuid not null,
    primary key (user_id, money_transfers_transfer_id),
    constraint FK_users_money_transfers_user_id foreign key (user_id) references wallet.users (id),
    constraint FK_users_money_transfers_transfer_id foreign key (money_transfers_transfer_id) references wallet.money_transfer (transfer_id)
);

create table users_payment_invoices (
    user_id bigint not null,
    payment_invoices_account_number uuid not null,
    primary key (user_id, payment_invoices_account_number),
    constraint FK_users_payment_invoices_user_id foreign key (user_id) references wallet.users (id),
    constraint FK_users_payment_invoices_account_number foreign key (payment_invoices_account_number) references wallet.payment_invoice (account_number)
);

create table users_sessions (
    user_id bigint not null,
    sessions_value varchar(255) not null,
    primary key (user_id, sessions_value),
    constraint FK_users_sessions_user_id foreign key (user_id) references wallet.users (id),
    constraint FK_users_sessions_sessions_value foreign key (sessions_value) references wallet.session (value)
);

create table wallet_money_transfers (
    wallet_wallet_id bigint not null,
    money_transfers_transfer_id uuid not null,
    primary key (wallet_wallet_id, money_transfers_transfer_id),
    constraint FK_wallet_money_transfers_wallet_wallet_id foreign key (wallet_wallet_id) references wallet.wallet (wallet_id),
    constraint FK_wallet_money_transfers_transfer_id foreign key (money_transfers_transfer_id) references wallet.money_transfer (transfer_id)
);