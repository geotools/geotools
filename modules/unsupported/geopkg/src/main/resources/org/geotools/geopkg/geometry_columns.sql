CREATE TABLE IF NOT EXISTS geometry_columns (
  f_table_name TEXT,
  f_geometry_column TEXT,
  geometry_type INTEGER NOT NULL,
  coord_dimension INTEGER NOT NULL,
  srid INTEGER NOT NULL,
  CONSTRAINT pk_geom_cols PRIMARY KEY (f_table_name, f_geometry_column),
  CONSTRAINT fk_gc_srs FOREIGN KEY (srid) REFERENCES spatial_ref_sys (srid));