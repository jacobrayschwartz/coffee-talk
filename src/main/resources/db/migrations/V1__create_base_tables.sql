create table if not exists users
(
    id bigserial unique,
    oidc_sub text not null,
    oidc_iss text not null,
    email   text,
    provider     text,
    display_name text,
    created_at   timestamp with time zone,
    modified_at  timestamp with time zone,
    is_disabled  boolean,
    disabled_at  timestamp with time zone,
    primary key (oidc_iss, oidc_sub)
);


create table if not exists coffee_board
(
    id              bigserial primary key,
    name            text not null,
    description     text,
    created_at      timestamp with time zone,
    created_by_user_id bigint
        constraint fk_coffee_board_user
            references users(id)
);


create table if not exists coffee_board_lanes
(
    coffee_board_id  bigint
        references coffee_board
            on update cascade on delete cascade,
    lane_order       integer not null,
    lane_name        text    not null,
    lane_description text    not null,
    created_at       timestamp with time zone,
    created_by_user_id  bigint
        constraint fk_coffee_board_lane_user
            references users(id)
);


create table if not exists logins
(
    user_id         bigint
        references users(id)
            on update cascade on delete cascade,
    attempted_at timestamp with time zone,
    success      boolean
);


create table if not exists groups
(
    id                   bigserial
        primary key,
    name                 text,
    created_by_user_id bigint
        references users(id)
            on delete cascade,
    created_at           timestamp with time zone,
    modified_at           timestamp with time zone
);


create table if not exists roles
(
    id         bigserial primary key,
    user_id  bigint
        references users(id)
            on delete cascade,
    group_id   bigint
        references groups,
    role_type text,
    created_at timestamp with time zone,
    modified_at timestamp with time zone
);


