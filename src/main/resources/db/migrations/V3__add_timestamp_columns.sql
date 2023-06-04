ALTER TABLE coffee_board
ADD COLUMN IF NOT EXISTS created_at timestamptz;

ALTER TABLE coffee_board
ADD COLUMN IF NOT EXISTS created_by_user text;

ALTER TABLE coffee_board
ADD CONSTRAINT fk_coffee_board_user FOREIGN KEY (created_by_user) references users(user_uuid);

ALTER TABLE coffee_board_lanes
ADD COLUMN IF NOT EXISTS created_at timestamptz;

ALTER TABLE coffee_board_lanes
ADD COLUMN IF NOT EXISTS created_by_user text;

ALTER TABLE coffee_board_lanes
ADD CONSTRAINT fk_coffee_board_lane_user FOREIGN KEY (created_by_user) references users(user_uuid);