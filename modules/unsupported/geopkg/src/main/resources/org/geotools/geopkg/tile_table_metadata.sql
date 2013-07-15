CREATE TABLE IF NOT EXISTS tile_table_metadata (
  t_table_name TEXT PRIMARY KEY,
  is_times_two_zoom INTEGER NOT NULL DEFAULT 1
);
