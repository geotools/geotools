CREATE TABLE IF NOT EXISTS "metadata" (
  "id" integer,
  "md_scope" text NOT NULL DEFAULT 'dataset',
  "md_standard_uri" text NOT NULL DEFAULT 'http://schemas.opengis.net/iso/19139',
  "mime_type" text NOT NULL DEFAULT 'text/xml',
  "metadata" text NOT NULL DEFAULT '',
  PRIMARY KEY ("id"));