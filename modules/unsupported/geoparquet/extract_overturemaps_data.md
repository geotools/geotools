
## Create target directory

```shell
$ mkdir extract
$ duckdb
```

Now run the following script.

> Adjust the `bounds` variable to your desired extract region. In this example:
> ```sql
> SET VARIABLE boundary = (SELECT ST_AsText(ST_SimplifyPreserveTopology(ST_Buffer(geometry, 0.001), 0.001))
>                         FROM divisions_division_area
>                         WHERE country = 'AR' AND names.primary = 'Tierra del Fuego');
> ```

```sql
-- Load required extensions
INSTALL spatial; LOAD spatial;
INSTALL httpfs; LOAD httpfs;
INSTALL parquet; LOAD parquet;

-- Create views. If you haven't downloaded the full OvertureMaps data use S3 URI's instead (e.g. 's3://overturemaps-us-west-2/release/2023-07-26-alpha.0/theme=buildings/type=building/*')
-- CREATE OR REPLACE VIEW buildings_building AS SELECT * FROM read_parquet('s3://overturemaps-us-wes-2/release/2023-07-26-alpha.0/theme=buildings/type=building/*');

CREATE OR REPLACE VIEW buildings_building_part AS SELECT * FROM read_parquet('theme=buildings/type=building_part/*');
CREATE OR REPLACE VIEW buildings_building AS SELECT * FROM read_parquet('theme=buildings/type=building/*');

CREATE OR REPLACE VIEW addresses_address AS SELECT * FROM read_parquet('theme=addresses/type=address/*');

CREATE OR REPLACE VIEW places_place AS SELECT * FROM read_parquet('theme=places/type=place/*');

CREATE OR REPLACE VIEW base_bathymetry AS SELECT * FROM read_parquet('theme=base/type=bathymetry/*');
CREATE OR REPLACE VIEW base_land AS SELECT * FROM read_parquet('theme=base/type=land/*');
CREATE OR REPLACE VIEW base_land_cover AS SELECT * FROM read_parquet('theme=base/type=land_cover/*');
CREATE OR REPLACE VIEW base_water AS SELECT * FROM read_parquet('theme=base/type=water/*');
CREATE OR REPLACE VIEW base_infrastructure AS SELECT * FROM read_parquet('theme=base/type=infrastructure/*');

CREATE OR REPLACE VIEW divisions_division AS SELECT * FROM read_parquet('theme=divisions/type=division/*');
CREATE OR REPLACE VIEW divisions_division_area AS SELECT * FROM read_parquet('theme=divisions/type=division_area/*');
CREATE OR REPLACE VIEW divisions_division_boundary AS SELECT * FROM read_parquet('theme=divisions/type=division_boundary/*');

-- Define the boundary of the data to exrtact. This one belongs to Tierra del Fuego, Argentina.

SET VARIABLE boundary = (
  SELECT ST_AsText(ST_Union_Agg(ST_SimplifyPreserveTopology(ST_ReducePrecision(ST_Buffer(geometry, 0.0001), 0.001), 0.001)))
  FROM divisions_division_area
  WHERE country = 'AR' AND names.primary = 'Tierra del Fuego'
);

/*
SET VARIABLE boundary = (
  SELECT ST_AsText(ST_Union_Agg(ST_SimplifyPreserveTopology(ST_ReducePrecision(ST_Buffer(geometry, 0.0001), 0.001), 0.001)))
  FROM divisions_division_area
  WHERE country = 'AR' OR names.primary = 'Falkland Islands'
);
*/

SET VARIABLE bounds = ST_Extent(ST_GeomFromText(getvariable('boundary')));


-- Now create the extracts. Note we're using Hive-paritioning PARTITION_BY (theme, type) and FILENAME_PATTERN 'part-{i}-{uuid}.zstd'
-- in order to mimic how the 2023-07-26-alpha.0 release files are structured, and OVERWRITE_OR_IGNORE to avoid each command failing if the folder is not empty, or deleting the other files if used OVERWRIDE instead
-- However, we're forcing part-0 in the file names. For some reason ghte index placeholder {i} is not working. The docs say it's one or the other: https://duckdb.org/docs/stable/data/partitioning/partitioned_writes.html
-- So not sure how OvertureMaps is generating proper file names with both index and uuid

COPY (
    SELECT * FROM buildings_building_part
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM buildings_building
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM addresses_address
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM places_place
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM base_bathymetry
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM base_land
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM base_land_cover
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM base_water
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM base_infrastructure
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM divisions_division
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM divisions_division_area
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

COPY (
    SELECT * FROM divisions_division_boundary
    WHERE bbox.xmin <= ST_XMax(getvariable('bounds')) and bbox.xmax >= ST_XMin(getvariable('bounds')) and bbox.ymin <= ST_YMax(getvariable('bounds')) and bbox.ymax >= ST_YMin(getvariable('bounds'))
    AND ST_Intersects(ST_MakeEnvelope(bbox.xmin, bbox.ymin, bbox.xmax, bbox.ymax), ST_GeomFromText(getvariable('boundary')))
    ORDER BY ST_Hilbert(geometry, ST_Extent(getvariable('bounds')))
) TO 'extract' (FORMAT 'parquet', COMPRESSION 'zstd', PARTITION_BY (theme, type), OVERWRITE_OR_IGNORE, FILENAME_PATTERN 'part-0-{uuid}.zstd');

```