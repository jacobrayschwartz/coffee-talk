create table coffee_board(
    id bigserial primary key,
    name varchar not null,
    description varchar
);

create table coffee_board_columns(
    coffee_board_id bigint,
    column_order int not null,
    column_name varchar not null,
    column_description varchar not null,
    foreign key(coffee_board_id) references coffee_board(id) on delete cascade on update cascade
);