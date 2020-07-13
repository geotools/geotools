CREATE TABLE IF NOT EXISTS gpkg_data_column_constraints (
  constraint_name TEXT NOT NULL,
  constraint_type TEXT NOT NULL, 
  value TEXT,
  min NUMERIC,
  min_is_inclusive BOOLEAN,
  max NUMERIC,
  max_is_inclusive BOOLEAN,
  description TEXT,
  CONSTRAINT gdcc_ntv UNIQUE (constraint_name, constraint_type, value)
);
INSERT into gpkg_extensions VALUES('gpkg_data_column_constraints', null, 'gpkg_schema', 'http://www.geopackage.org/spec121/index.html#extension_schema', 'read-write');
