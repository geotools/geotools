CREATE TABLE IF NOT EXISTS gpkg_data_columns (
  table_name TEXT NOT NULL,
  column_name TEXT NOT NULL,
  name TEXT UNIQUE,
  title TEXT,
  description TEXT,
  mime_type TEXT,
  constraint_name TEXT,
  CONSTRAINT pk_gdc PRIMARY KEY (table_name, column_name),
  CONSTRAINT fk_gdc_tn FOREIGN KEY (table_name) REFERENCES gpkg_contents(table_name)
);
INSERT into gpkg_extensions VALUES('gpkg_data_columns', null, 'gpkg_schema', 'http://www.geopackage.org/spec121/index.html#extension_schema', 'read-write');