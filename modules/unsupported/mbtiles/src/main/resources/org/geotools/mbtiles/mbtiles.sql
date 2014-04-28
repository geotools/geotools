CREATE TABLE IF NOT EXISTS metadata (name text, value text, CONSTRAINT pk_metadata PRIMARY KEY(name));
CREATE TABLE IF NOT EXISTS tiles (zoom_level integer, tile_column integer, tile_row integer, tile_data blob, CONSTRAINT pk_tiles PRIMARY KEY(zoom_level, tile_column,tile_row));
CREATE TABLE IF NOT EXISTS grids (zoom_level integer, tile_column integer, tile_row integer, grid blob, CONSTRAINT pk_grids PRIMARY KEY(zoom_level, tile_column,tile_row));
CREATE TABLE IF NOT EXISTS grid_data (zoom_level integer, tile_column integer, tile_row integer, key_name text, key_json text, CONSTRAINT pk_griddata PRIMARY KEY(zoom_level, tile_column,tile_row,key_name));

