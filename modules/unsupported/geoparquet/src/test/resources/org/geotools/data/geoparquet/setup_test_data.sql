/*
 * WORLDGRID GEOPARQUET TEST DATA GENERATOR
 *
 * This script creates a comprehensive test dataset for GeoParquet testing containing:
 *
 * 1. Points:
 *    - 65,341 individual points covering the world grid (-180 to 180, -90 to 90)
 *    - 4 multipoints (one per quadrant: NE, NW, SE, SW)
 *    - Attributes include: id, geometry, bbox, names (multilingual), confidence, phones, addresses
 *
 * 2. Lines:
 *    - 1,084 lines (horizontal and vertical lines along lat/lon grid)
 *    - 4 multilines (one per quadrant)
 *    - Attributes include: id, geometry, bbox, names (multilingual)
 *
 * 3. Polygons:
 *    - 2,485 polygons (5-degree grid cells)
 *    - 4 multipolygons (one per quadrant)
 *    - Attributes include: id, geometry, bbox, names (multilingual)
 *
 * Exports to:
 * - test-data/worldgrid_partitioned/ (Hive partitioned by theme and type)
 * - test-data/worldgrid/ (Single files with mixed geometry types)
 *
 * GeoParquet metadata with the 'geo' key is automatically added by DuckDB 2.1+
 */

-- Install and load required extensions
INSTALL spatial;
LOAD spatial;
INSTALL parquet;
LOAD parquet;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS points;
DROP TABLE IF EXISTS lines;
DROP TABLE IF EXISTS polygons;

-- Create points table
CREATE TABLE points AS 
WITH 
    x_range AS (SELECT range AS x FROM range(-180, 181, 1)),
    y_range AS (SELECT range AS y FROM range(-90, 91, 1)),
    coords AS (
        SELECT 
            x, 
            y,
            ROW_NUMBER() OVER () AS id,
            CASE
                WHEN x < 0 AND y < 0 THEN 'sw'
                WHEN x >= 0 AND y < 0 THEN 'se'
                WHEN x < 0 AND y >= 0 THEN 'nw'
                ELSE 'ne'
            END AS quadrant,
            x + y AS confidence_base
        FROM x_range, y_range
    )
SELECT 
    'PT_' || LPAD(id::VARCHAR, 6, '0') AS id,
    ST_GeomFromText(printf('POINT(%g %g)', x::DOUBLE, y::DOUBLE)) AS geometry,
    {'xmin': x::FLOAT, 'xmax': x::FLOAT, 'ymin': y::FLOAT, 'ymax': y::FLOAT} AS bbox,
    {
        'primary': 'Point ' || id,
        'common': MAP(
            ['en', 'es', 'fr'],
            ['Point ' || id, 'Punto ' || id, 'Point ' || id]
        ),
        'rules': {'variant': 'standard'}
    } AS names,
    confidence_base / 270.0 AS confidence, -- Normalize to range between -1 and 1
    CASE 
        WHEN id % 3 = 0 THEN ['555-' || LPAD((id % 10000)::VARCHAR, 4, '0'), '555-' || LPAD(((id + 1) % 10000)::VARCHAR, 4, '0')]
        WHEN id % 5 = 0 THEN ['555-' || LPAD((id % 10000)::VARCHAR, 4, '0')]
        ELSE NULL
    END AS phones,
    CASE
        WHEN id % 4 = 0 THEN {
            'freeform': id || ' Longitude St, Latitude City',
            'locality': 'City ' || (id % 100),
            'postcode': LPAD((id % 100000)::VARCHAR, 5, '0'),
            'region': CASE quadrant
                WHEN 'ne' THEN 'Northeast Region'
                WHEN 'nw' THEN 'Northwest Region'
                WHEN 'se' THEN 'Southeast Region'
                WHEN 'sw' THEN 'Southwest Region'
            END,
            'country': 'Gridland'
        }
        ELSE NULL
    END AS addresses,
    'points' AS theme,
    'point' AS type
FROM coords
ORDER BY id;

-- Add 4 multipoint records, one for each quadrant
INSERT INTO points
WITH quadrant_points AS (
    SELECT 
        CASE
            WHEN x < 0 AND y < 0 THEN 'sw'
            WHEN x >= 0 AND y < 0 THEN 'se'
            WHEN x < 0 AND y >= 0 THEN 'nw'
            ELSE 'ne'
        END AS quadrant,
        ST_GeomFromText(printf('POINT(%g %g)', x::DOUBLE, y::DOUBLE)) AS point,
        x, y
    FROM 
        -- Use a step of 5 to reduce number of points in each multipoint
        (SELECT range AS x FROM range(-180, 181, 5)) x_range,
        (SELECT range AS y FROM range(-90, 91, 5)) y_range
)
SELECT
    'MP_' || quadrant AS id,
    -- Create a multipoint geometry from all points in each quadrant
    ST_GeomFromText('MULTIPOINT(' || 
        STRING_AGG(printf('(%g %g)', x::DOUBLE, y::DOUBLE), ',' ORDER BY x, y) ||
    ')') AS geometry,
    {
        'xmin': MIN(x)::FLOAT, 
        'xmax': MAX(x)::FLOAT, 
        'ymin': MIN(y)::FLOAT, 
        'ymax': MAX(y)::FLOAT
    } AS bbox,
    {
        'primary': 'MultiPoint ' || quadrant,
        'common': MAP(
            ['en', 'es', 'fr'],
            ['MultiPoint ' || quadrant, 'Multipunto ' || quadrant, 'MultiPoint ' || quadrant]
        ),
        'rules': {'variant': 'standard'}
    } AS names,
    0.5 AS confidence,
    ['555-MULTI-' || quadrant] AS phones,
    {
        'freeform': 'MultiPoint ' || quadrant || ' Address',
        'locality': 'MultiPoint City',
        'postcode': '00000',
        'region': CASE quadrant
            WHEN 'ne' THEN 'Northeast Region'
            WHEN 'nw' THEN 'Northwest Region'
            WHEN 'se' THEN 'Southeast Region'
            WHEN 'sw' THEN 'Southwest Region'
        END,
        'country': 'Gridland'
    } AS addresses,
    'points' AS theme,
    'multipoint' AS type
FROM 
    quadrant_points
GROUP BY
    quadrant;


-- Create the lines table with horizontal and vertical lines
CREATE TABLE lines AS
-- Horizontal lines (one per latitude)
WITH horizontal_lines AS (
    SELECT
        y,
        CASE
            WHEN quad_x < 0 AND y < 0 THEN 'sw'
            WHEN quad_x >= 0 AND y < 0 THEN 'se'
            WHEN quad_x < 0 AND y >= 0 THEN 'nw'
            ELSE 'ne'
        END AS quadrant,
        1000 + ROW_NUMBER() OVER (ORDER BY y) AS id,
        -- Create a horizontal line for each latitude value
        CASE
		  WHEN y < 0 AND quad_x < 0
		    THEN ST_MakeLine(ST_Point(-180::DOUBLE, y::DOUBLE), ST_Point(0::DOUBLE,   y::DOUBLE))  -- SW
		  WHEN y < 0 AND quad_x >= 0
		    THEN ST_MakeLine(ST_Point(  0::DOUBLE, y::DOUBLE), ST_Point(180::DOUBLE,  y::DOUBLE))  -- SE
		  WHEN y >= 0 AND quad_x < 0
		    THEN ST_MakeLine(ST_Point(-180::DOUBLE, y::DOUBLE), ST_Point(0::DOUBLE,   y::DOUBLE))  -- NW
		  ELSE
		    ST_MakeLine(ST_Point(  0::DOUBLE, y::DOUBLE), ST_Point(180::DOUBLE,  y::DOUBLE))       -- NE
		END AS geometry,
        CASE WHEN quad_x < 0 THEN -180 ELSE 0 END AS x_min,
        CASE WHEN quad_x < 0 THEN 0 ELSE 180 END AS x_max,
        y AS y_min,
        y AS y_max
    FROM
        (SELECT DISTINCT range AS y FROM range(-90, 91, 1)) y_vals,
        (SELECT -1 AS quad_x UNION SELECT 1) quads
),
-- Vertical lines (one per longitude)
vertical_lines AS (
    SELECT
        x,
        CASE
            WHEN x < 0 AND quad_y < 0 THEN 'sw'
            WHEN x >= 0 AND quad_y < 0 THEN 'se'
            WHEN x < 0 AND quad_y >= 0 THEN 'nw'
            ELSE 'ne'
        END AS quadrant,
        3000 + ROW_NUMBER() OVER (ORDER BY x) AS id,
        -- Create a vertical line for each longitude value
        CASE
		  WHEN x < 0 AND quad_y < 0
		    THEN ST_MakeLine(ST_Point(x::DOUBLE, -90), ST_Point(x::DOUBLE, 0))
		  WHEN x >= 0 AND quad_y < 0
		    THEN ST_MakeLine(ST_Point(x::DOUBLE, -90), ST_Point(x::DOUBLE, 0))
		  WHEN x < 0 AND quad_y >= 0
		    THEN ST_MakeLine(ST_Point(x::DOUBLE, 0),   ST_Point(x::DOUBLE, 90))
		  ELSE
		    ST_MakeLine(ST_Point(x::DOUBLE, 0),        ST_Point(x::DOUBLE, 90))
		END AS geometry,
        x AS x_min,
        x AS x_max,
        CASE WHEN quad_y < 0 THEN -90 ELSE 0 END AS y_min,
        CASE WHEN quad_y < 0 THEN 0 ELSE 90 END AS y_max
    FROM
        (SELECT DISTINCT range AS x FROM range(-180, 181, 1)) x_vals,
        (SELECT -1 AS quad_y UNION SELECT 1) quads
),
-- Combine all lines
all_lines AS (
    SELECT id, quadrant, geometry, x_min, x_max, y_min, y_max FROM horizontal_lines
    UNION ALL
    SELECT id, quadrant, geometry, x_min, x_max, y_min, y_max FROM vertical_lines
)
SELECT
    'LN_' || LPAD(id::VARCHAR, 6, '0') AS id,
    geometry,
    {'xmin': x_min::FLOAT, 'xmax': x_max::FLOAT, 'ymin': y_min::FLOAT, 'ymax': y_max::FLOAT} AS bbox,
    {
        'primary': 'Line ' || id,
        'common': MAP(
            ['en', 'es', 'fr'],
            ['Line ' || id, 'Línea ' || id, 'Ligne ' || id]
        ),
        'rules': {'variant': 'standard'}
    } AS names,
    'lines' AS theme,
    'line' AS type
FROM all_lines;

-- Add MultiLineString records (one for each quadrant)
INSERT INTO lines
WITH line_points AS (
  SELECT
    CASE
      WHEN x < 0 AND y < 0 THEN 'sw'
      WHEN x >= 0 AND y < 0 THEN 'se'
      WHEN x < 0 AND y >= 0 THEN 'nw'
      ELSE 'ne'
    END AS quadrant,
    x, y
  FROM
    (SELECT range * 5 AS x FROM range(-36, 37)) x_range,
    (SELECT range * 5 AS y FROM range(-18, 19)) y_range
  WHERE (x % 20 = 0 OR y % 20 = 0)
),
quad_lines AS (
  SELECT
    quadrant,
    ST_MakeLine(
      ST_Point(x::DOUBLE, y::DOUBLE),
      ST_Point((x + 5)::DOUBLE, (y + 5)::DOUBLE)
    ) AS geom
  FROM line_points
),
collected AS (
  SELECT
    quadrant,
    LIST(geom)                         AS geoms,
    MIN(ST_XMin(geom))                 AS xmin,
    MAX(ST_XMax(geom))                 AS xmax,
    MIN(ST_YMin(geom))                 AS ymin,
    MAX(ST_YMax(geom))                 AS ymax
  FROM quad_lines
  GROUP BY quadrant
)
SELECT
  'ML_' || quadrant AS id,
  ST_Multi(ST_Collect(geoms))          AS geometry,
  {
    'xmin': xmin::FLOAT,
    'xmax': xmax::FLOAT,
    'ymin': ymin::FLOAT,
    'ymax': ymax::FLOAT
  }                                     AS bbox,
  {
    'primary': 'MultiLine ' || quadrant,
    'common': MAP(
      ['en', 'es', 'fr'],
      ['MultiLine ' || quadrant, 'Multilínea ' || quadrant, 'MultiLigne ' || quadrant]
    ),
    'rules': {'variant': 'standard'}
  }                                     AS names,
  'lines'                               AS theme,
  'multiline'                           AS type
FROM collected;

-- Create the polygons table with square polygons
CREATE TABLE polygons AS
WITH
    -- Create grid cells as 1x1 degree squares
    grid_cells AS (
        SELECT
            x, y,
            CASE
                WHEN x < 0 AND y < 0 THEN 'sw'
                WHEN x >= 0 AND y < 0 THEN 'se'
                WHEN x < 0 AND y >= 0 THEN 'nw'
                ELSE 'ne'
            END AS quadrant,
            ROW_NUMBER() OVER (ORDER BY y, x) AS id,
            -- Create a polygon square for each grid cell
            ST_MakePolygon(
			  ST_MakeLine(
			    LIST_VALUE(
			      ST_Point(x::DOUBLE,       y::DOUBLE),
			      ST_Point((x+1)::DOUBLE,   y::DOUBLE),
			      ST_Point((x+1)::DOUBLE,  (y+1)::DOUBLE),
			      ST_Point(x::DOUBLE,      (y+1)::DOUBLE),
			      ST_Point(x::DOUBLE,       y::DOUBLE)
			    )
			  )
			) AS geometry,
            x AS x_min,
            (x+1) AS x_max,
            y AS y_min,
            (y+1) AS y_max
        FROM 
            -- Use a step of 5 to reduce the number of polygons (less dense grid)
            (SELECT range AS x FROM range(-180, 179, 5)) x_range,
            (SELECT range AS y FROM range(-90, 89, 5)) y_range
        WHERE 
            -- Skip cells across dateline and poles for simplicity
            x >= -179 AND x < 179 AND y >= -89 AND y < 89
    )
SELECT
    'PG_' || LPAD(id::VARCHAR, 6, '0') AS id,
    geometry,
    {'xmin': x_min::FLOAT, 'xmax': x_max::FLOAT, 'ymin': y_min::FLOAT, 'ymax': y_max::FLOAT} AS bbox,
    {
        'primary': 'Polygon ' || id,
        'common': MAP(
            ['en', 'es', 'fr'], 
            ['Square ' || id, 'Cuadrado ' || id, 'Carré ' || id]
        ),
        'rules': {'variant': 'standard'}
    } AS names,
    'polygons' AS theme,
    'polygon' AS type
FROM grid_cells;

-- Add MultiPolygon records (one for each quadrant)
INSERT INTO polygons
WITH poly_points AS (
  SELECT
    CASE
      WHEN x < 0 AND y < 0 THEN 'sw'
      WHEN x >= 0 AND y < 0 THEN 'se'
      WHEN x < 0 AND y >= 0 THEN 'nw'
      ELSE 'ne'
    END AS quadrant,
    x, y
  FROM
    (SELECT range * 10 AS x FROM range(-18, 19)) x_range,
    (SELECT range * 10 AS y FROM range(-9, 10))  y_range
  WHERE (x % 30 = 0 AND y % 30 = 0)
),
squares AS (
  SELECT
    quadrant,
    ST_MakePolygon(
      ST_MakeLine(LIST_VALUE(
        ST_Point(x::DOUBLE,       y::DOUBLE),
        ST_Point((x+5)::DOUBLE,   y::DOUBLE),
        ST_Point((x+5)::DOUBLE,  (y+5)::DOUBLE),
        ST_Point(x::DOUBLE,      (y+5)::DOUBLE),
        ST_Point(x::DOUBLE,       y::DOUBLE)
      ))
    ) AS poly
  FROM poly_points
),
agg AS (
  SELECT
    quadrant,
    LIST(poly)                          AS polys,
    MIN(ST_XMin(poly))                  AS xmin,
    MAX(ST_XMax(poly))                  AS xmax,
    MIN(ST_YMin(poly))                  AS ymin,
    MAX(ST_YMax(poly))                  AS ymax
  FROM squares
  GROUP BY quadrant
)
SELECT
  'MPG_' || quadrant AS id,
  ST_Multi(ST_Collect(polys))           AS geometry,
  {
    'xmin': xmin::FLOAT, 'xmax': xmax::FLOAT,
    'ymin': ymin::FLOAT, 'ymax': ymax::FLOAT
  }                                   AS bbox,
  {
    'primary': 'MultiPolygon ' || quadrant,
    'common': MAP(
      ['en','es','fr'],
      ['MultiPolygon '||quadrant, 'Multipolígono '||quadrant, 'MultiPolygone '||quadrant]
    ),
    'rules': {'variant':'standard'}
  }                                   AS names,
  'polygons'                           AS theme,
  'multipolygon'                       AS type
FROM agg;


-- Verify
SELECT theme, type, count(*) FROM points GROUP BY theme, type;
SELECT theme, type, count(*) FROM lines GROUP BY theme, type;
SELECT theme, type, count(*) FROM polygons GROUP BY theme, type;

-- Export data to GeoParquet
SELECT '--------------------' AS separator;
SELECT 'Exporting to GeoParquet files...' AS status;

.shell mkdir -p test-data/worldgrid
.shell mkdir -p test-data/worldgrid_partitioned


-- Export to GeoParquet with Hive partitioning by theme and type at test-data/worldgrid_partitioned
COPY (
    SELECT * FROM points
) 
TO 'test-data/worldgrid_partitioned'
(
    FORMAT 'parquet',
    ROW_GROUP_SIZE 2048,
    COMPRESSION 'zstd', 
    PARTITION_BY (theme, type), 
    OVERWRITE_OR_IGNORE, 
    FILENAME_PATTERN 'points-'
);

COPY (
    SELECT * FROM lines
)
TO 'test-data/worldgrid_partitioned'
(
    FORMAT 'parquet',
    ROW_GROUP_SIZE 2048,
    COMPRESSION 'zstd', 
    PARTITION_BY (theme, type), 
    OVERWRITE_OR_IGNORE, 
    FILENAME_PATTERN 'lines-'
);

COPY (
    SELECT * FROM polygons
)
TO 'test-data/worldgrid_partitioned'
(
    FORMAT 'parquet',
    ROW_GROUP_SIZE 2048,
    COMPRESSION 'zstd', 
    PARTITION_BY (theme, type), 
    OVERWRITE_OR_IGNORE, 
    FILENAME_PATTERN 'polygons-'
);

-- Export to GeoParquet with no partitioning and multiple geometry types test-data/worldgrid/
COPY (
    SELECT * FROM points
) TO 'test-data/worldgrid/points.parquet' (
    FORMAT 'parquet',
    COMPRESSION 'zstd',
    OVERWRITE_OR_IGNORE
);

COPY (
    SELECT * FROM lines
) TO 'test-data/worldgrid/lines.parquet' (
    FORMAT 'parquet',
    COMPRESSION 'zstd',
    OVERWRITE_OR_IGNORE
);

COPY (
    SELECT * FROM polygons
) TO 'test-data/worldgrid/polygons.parquet' (
    FORMAT 'parquet',
    COMPRESSION 'zstd',
    OVERWRITE_OR_IGNORE
);

SELECT 'Export complete.' AS status;
SELECT 'NOTE: DuckDB version 1.2.2 does not add CRS information to GeoParquet metadata and does not provide API to modify it.' AS note;
SELECT 'The GeoParquetDataStore code handles missing CRS information by defaulting to EPSG:4326 (WGS 84).' AS note;
