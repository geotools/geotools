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
