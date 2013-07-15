CREATE TABLE IF NOT EXISTS spatial_ref_sys (
  srid INTEGER PRIMARY KEY,
  auth_name TEXT NOT NULL,
  auth_srid INTEGER NOT NULL,
  srtext TEXT NOT NULL);
CREATE UNIQUE INDEX IF NOT EXISTS idx_spatial_ref_sys ON spatial_ref_sys (srid, auth_name);