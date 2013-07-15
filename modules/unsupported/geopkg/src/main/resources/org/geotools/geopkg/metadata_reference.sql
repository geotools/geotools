CREATE TABLE IF NOT EXISTS "metadata_reference" (
  "reference_scope" text NOT NULL,
  "table_name" text,
  "column_name" text,
  "row_id_value" integer,
  "timestamp" text NOT NULL DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ', 'now')),
  "md_file_id" integer NOT NULL REFERENCES metadata(id),
  "md_parent_id" integer REFERENCES metadata(id));