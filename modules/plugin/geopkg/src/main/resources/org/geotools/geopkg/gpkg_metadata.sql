CREATE TABLE IF NOT EXISTS gpkg_metadata (
  id INTEGER CONSTRAINT m_pk PRIMARY KEY ASC NOT NULL,
  md_scope TEXT NOT NULL DEFAULT 'dataset',
  md_standard_uri TEXT NOT NULL,
  mime_type TEXT NOT NULL DEFAULT 'text/xml',
  metadata TEXT NOT NULL DEFAULT ''
);
INSERT into gpkg_extensions VALUES('gpkg_metadata', null, 'gpkg_metadata', 'http://www.geopackage.org/spec121/#extension_metadata', 'read-write');