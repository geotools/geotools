CREATE TABLE IF NOT EXISTS geopackage_contents (
  table_name TEXT NOT NULL PRIMARY KEY,
  data_type TEXT NOT NULL,
  identifier TEXT NOT NULL UNIQUE,
  description TEXT NOT NULL DEFAULT 'none',
  last_change TEXT NOT NULL DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ',CURRENT_TIMESTAMP)),
  min_x DOUBLE NOT NULL DEFAULT -180.0,
  min_y DOUBLE NOT NULL DEFAULT -90.0,
  max_x DOUBLE NOT NULL DEFAULT 180.0,
  max_y DOUBLE NOT NULL DEFAULT 90.0,
  srid INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT fk_gc_r_srid FOREIGN KEY (srid) REFERENCES spatial_ref_sys(srid));
