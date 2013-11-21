CREATE TABLE IF NOT EXISTS gpkg_metadata (
  id INTEGER CONSTRAINT m_pk PRIMARY KEY ASC NOT NULL UNIQUE,
  md_scope TEXT NOT NULL DEFAULT 'dataset',
  metadata_standard_URI TEXT NOT NULL DEFAULT 'http://schemas.opengis.net/iso/19139/',
  mime_type TEXT NOT NULL DEFAULT 'text/xml',
  metadata TEXT NOT NULL DEFAULT ''
);
