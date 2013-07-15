CREATE TABLE IF NOT EXISTS raster_columns (
  r_table_name TEXT NOT NULL,
  r_raster_column TEXT NOT NULL,
  compr_qual_factor INTEGER NOT NULL DEFAULT 100,
  georectification INTEGER NOT NULL DEFAULT 0,
  srid INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT pk_rc PRIMARY KEY (r_table_name, r_raster_column)
  ON CONFLICT ROLLBACK,
  CONSTRAINT fk_rc_r_srid FOREIGN KEY (srid)
  REFERENCES spatial_ref_sys(srid),
  CONSTRAINT fk_rc_r_gc FOREIGN KEY (r_table_name) REFERENCES geopackage_contents(table_name));