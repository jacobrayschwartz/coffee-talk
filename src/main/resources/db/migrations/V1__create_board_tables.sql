create table coffee_board(
    id bigserial primary key,
    name varchar not null,
    description varchar
);

create table coffee_board_lanes(
    coffee_board_id bigint,
    lane_order int not null,
    lane_name varchar not null,
    lane_description varchar not null,
    foreign key(coffee_board_id) references coffee_board(id) on delete cascade on update cascade
);