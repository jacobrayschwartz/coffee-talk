CREATE TABLE IF NOT EXISTS users(
    user_uuid text primary key,
    user_email text,
    provider text,
    display_name text,
    created_at timestamptz,
    modified_at timestamptz
);

CREATE TABLE IF NOT EXISTS logins(
    user_uuid text,
    attempted_at timestamptz,
    success boolean,
    foreign key (user_uuid) references users(user_uuid) on delete cascade on update cascade
);