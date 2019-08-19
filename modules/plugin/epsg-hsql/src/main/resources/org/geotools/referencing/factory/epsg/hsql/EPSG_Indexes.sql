--
-- The EPSG database is shipped with no index except primary keys. This file creates
-- some index appropriate for the queries performed by Geotools DirectEpsgFactory.
--
-- Authors: Andrea Aimes
--          Martin Desruisseaux
--
-- Version: $Id$
--

--------------------------------------------------------------------------------
-- Index for queries used by DirectEpsgFactory.createFoo(epsgCode) methods.   --
-- Indexed fields are numeric values used mainly in equality comparaisons.    --
--------------------------------------------------------------------------------
CREATE INDEX ALIAS_OBJECT_CODE                ON EPSG_ALIAS                     (OBJECT_CODE);
CREATE INDEX CRS_DATUM_CODE                   ON EPSG_COORDINATEREFERENCESYSTEM (DATUM_CODE);
CREATE INDEX CRS_PROJECTION_CODE              ON EPSG_COORDINATEREFERENCESYSTEM (PROJECTION_CONV_CODE);
CREATE INDEX COORDINATE_AXIS_CODE             ON EPSG_COORDINATEAXIS            (COORD_AXIS_CODE);
CREATE INDEX COORDINATE_AXIS_SYS_CODE         ON EPSG_COORDINATEAXIS            (COORD_SYS_CODE);
CREATE INDEX COORDINATE_OPERATION_CRS         ON EPSG_COORDOPERATION            (SOURCE_CRS_CODE, TARGET_CRS_CODE);
CREATE INDEX COORDINATE_OPERATION_METHOD_CODE ON EPSG_COORDOPERATION            (COORD_OP_METHOD_CODE);
CREATE INDEX PARAMETER_USAGE_METHOD_CODE      ON EPSG_COORDOPERATIONPARAMUSAGE  (COORD_OP_METHOD_CODE);
CREATE INDEX PARAMETER_VALUES                 ON EPSG_COORDOPERATIONPARAMVALUE  (COORD_OP_CODE, COORD_OP_METHOD_CODE);
CREATE INDEX PARAMETER_VALUE_CODE             ON EPSG_COORDOPERATIONPARAMVALUE  (PARAMETER_CODE);
CREATE INDEX PATH_CONCAT_OPERATION_CODE       ON EPSG_COORDOPERATIONPATH        (CONCAT_OPERATION_CODE);
CREATE INDEX SUPERSESSION_OBJECT_CODE         ON EPSG_SUPERSESSION              (OBJECT_CODE);


--------------------------------------------------------------------------------
-- Index for queries used by DirectEpsgFactory.createFoo(epsgCode) methods.   --
-- Indexed fields are numeric values used in ORDER BY clauses.  I'm not sure  --
-- that their impact on HSQL database is significant, so it is probably safe  --
-- to ignore them.                                                            --
--------------------------------------------------------------------------------
CREATE INDEX COORDINATE_AXIS_ORDER            ON EPSG_COORDINATEAXIS           (COORD_AXIS_ORDER);
CREATE INDEX COORDINATE_OPERATION_ACCURACY    ON EPSG_COORDOPERATION           (COORD_OP_ACCURACY);
CREATE INDEX PARAMETER_ORDER                  ON EPSG_COORDOPERATIONPARAMUSAGE (SORT_ORDER);
CREATE INDEX PATH_CONCAT_OPERATION_STEP       ON EPSG_COORDOPERATIONPATH       (OP_PATH_STEP);
CREATE INDEX SUPERSESSION_OBJECT_YEAR         ON EPSG_SUPERSESSION             (SUPERSESSION_YEAR);
CREATE INDEX VERSION_HISTORY_DATE             ON EPSG_VERSIONHISTORY           (VERSION_DATE);


--------------------------------------------------------------------------------
-- Index on the object names, used in order to find an EPSG code from a name. --
--------------------------------------------------------------------------------
CREATE INDEX NAME_CRS            ON EPSG_COORDINATEREFERENCESYSTEM (COORD_REF_SYS_NAME);
CREATE INDEX NAME_CS             ON EPSG_COORDINATESYSTEM          (COORD_SYS_NAME);
CREATE INDEX NAME_AXIS           ON EPSG_COORDINATEAXISNAME        (COORD_AXIS_NAME);
CREATE INDEX NAME_DATUM          ON EPSG_DATUM                     (DATUM_NAME);
CREATE INDEX NAME_ELLIPSOID      ON EPSG_ELLIPSOID                 (ELLIPSOID_NAME);
CREATE INDEX NAME_PRIME_MERIDIAN ON EPSG_PRIMEMERIDIAN             (PRIME_MERIDIAN_NAME);
CREATE INDEX NAME_COORD_OP       ON EPSG_COORDOPERATION            (COORD_OP_NAME);
CREATE INDEX NAME_METHOD         ON EPSG_COORDOPERATIONMETHOD      (COORD_OP_METHOD_NAME);
CREATE INDEX NAME_PARAMETER      ON EPSG_COORDOPERATIONPARAM       (PARAMETER_NAME);
CREATE INDEX NAME_UNIT           ON EPSG_UNITOFMEASURE             (UNIT_OF_MEAS_NAME);
