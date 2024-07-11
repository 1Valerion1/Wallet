create table if not exists wallet (
    wallet_id bigserial not null,
    user_id bigint unique,
    balance integer,
    foreign key (user_id) references users (id),
    primary key (wallet_id)
);

create table if not exists money_transfer (
    amount int,
    creating_translation timestamp with time zone,
    user_id bigint,
    foreign key (user_id) references users (id),
    wallet_id bigint,
    foreign key (wallet_id) references wallet (wallet_id),
    transfer_id uuid not null,
    comment varchar(50),
    status varchar(15) check (status in ('PAID','UNPAID','CANCELLED')),
    transfer_type varchar(15) check (transfer_type in ('INCOMING','OUTGOING')),
    primary key (transfer_id)
);

create table if not exists payment_invoice (
    amount int,
    completed_at timestamp,
    receiver_id bigint,
    account_number uuid not null,
    sender_id bigint,
    comment varchar(255),
    status varchar(255) check (status in ('PAID','UNPAID','CANCELLED')),
    primary key (account_number)
);
