CREATE TABLE IF NOT EXISTS tile_matrix_metadata (
  t_table_name TEXT,
  zoom_level INTEGER,
  matrix_width INTEGER NOT NULL,
  matrix_height INTEGER NOT NULL,
  tile_width INTEGER NOT NULL,
  tile_height INTEGER NOT NULL,
  pixel_x_size DOUBLE NOT NULL,
  pixel_y_size DOUBLE NOT NULL,
  CONSTRAINT pk_ttm PRIMARY KEY (t_table_name, zoom_level) ON CONFLICT ROLLBACK,
  CONSTRAINT fk_ttm_t_table_name FOREIGN KEY (t_table_name) REFERENCES tile_table_metadata(t_table_name));
