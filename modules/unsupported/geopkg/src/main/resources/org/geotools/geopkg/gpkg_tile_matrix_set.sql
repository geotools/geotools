CREATE TABLE IF NOT EXISTS gpkg_tile_matrix_set (
  table_name TEXT NOT NULL PRIMARY KEY,
  srs_id INTEGER NOT NULL,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  CONSTRAINT fk_gtms_table_name FOREIGN KEY (table_name) REFERENCES gpkg_contents(table_name),
  CONSTRAINT fk_gtms_srs FOREIGN KEY (srs_id) REFERENCES gpkg_spatial_ref_sys (srs_id)
);
