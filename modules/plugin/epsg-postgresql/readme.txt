This module is used to allow geotools to communicate with the EPSG database
hosted via PostgreSQL.

The instructions are included formally in the javadocs for this module
(see org/geotools/referencing/factory/espg/doc-files/PostgreSQL.html) reproduced
here for your convience.

-- Text of PostgreSQL.html ---

The scripts provided by EPSG can be run "as is" even if minor SQL language
differences raise some errors: the commit statements should have a begin
statement, and the SET with replace() in not understandable by postgreSQL.

The SQL scripts must be executed in the following order:

- EPSG_v67.mdb_Tables.sql 
- EPSG_v67.mdb_Data.sql 
- EPSG_v67.mdb_FKeys.sql 
 

Hints for creating the database locally
- hb_pga.conf file:
     ...
     host	EPSG    	127.0.0.1	255.255.255.255 password	web
 

- web file:

     ...
     Geotools
 

- Creating the database

$ su -l postgres -c 'createuser -d -A -P Geotools'
Enter password for user "Geotools": Geotools
Enter it again: Geotools
CREATE USER

$ createdb -U Geotools -h localhost -E LATIN9 -e EPSG "EPSG for GeoTools"
Password: Geotools
CREATE DATABASE "EPSG" WITH ENCODING = 'LATIN9'
CREATE DATABASE
Password: Geotools
COMMENT ON DATABASE "EPSG" IS 'EPSG sous postgreSQL'
COMMENT

$ psql -U Geotools -h localhost -f EPSG_v67.mdb_Tables.sql EPSG
Password: Geotools
psql:EPSG_v67.mdb_Tables.sql:8: NOTICE:  CREATE TABLE / PRIMARY KEY will create implicit index 'pk_alias' for table 'epsg_alias'
...

$ psql -U Geotools -h localhost -f EPSG_v67.mdb_Data.sql EPSG
Password: Geotools
...
UPDATE 0
psql:EPSG_v67.mdb_Data.sql:273608: ERROR:  parser: parse error at or near ","
...
psql:EPSG_v67.mdb_Data.sql:273806: ERROR:  parser: parse error at or near ","
psql:EPSG_v67.mdb_Data.sql:273807: NOTICE:  COMMIT: no transaction in progress
...
#
# These errors won't break the database. The SQL statements are :
# UPDATE epsg_table
# SET a_field = replace(a_field, CHAR(182), CHAR(10));
#
COMMIT

$ psql -U Geotools -h localhost -f EPSG_v67.mdb_FKeys.sql EPSG
Password: Geotools
psql:EPSG_v67.mdb_FKeys.sql:2: NOTICE:  ALTER TABLE will create implicit trigger(s) for FOREIGN KEY check(s)
ALTER
...

$ vacuumdb -U Geotools -h localhost -f -z EPSG
Password: Geotools
NOTICE:  Skipping "pg_group" --- only table or database owner can VACUUM it
NOTICE:  Skipping "pg_database" --- only table or database owner can VACUUM it
NOTICE:  Skipping "pg_shadow" --- only table or database owner can VACUUM it
VACUUM

#
# Security matters : Geotools now only selects !
#
$ psql -U Geotools -h localhost -c 'revoke all on epsg_alias, epsg_area, epsg_change, epsg_coordinateaxis, epsg_coordinateaxisname, epsg_coordinatereferencesystem, epsg_coordinatesystem, epsg_coordoperation, epsg_coordoperationmethod, epsg_coordoperationparam, epsg_coordoperationparamusage, epsg_coordoperationparamvalue, epsg_coordoperationpath, epsg_datum, epsg_deprecation, epsg_ellipsoid, epsg_namingsystem, epsg_primemeridian, epsg_supersession, epsg_unitofmeasure, epsg_versionhistory from "Geotools"' EPSG
Password: Geotools
REVOKE
$ psql -U Geotools -h localhost -c 'grant select on epsg_alias, epsg_area, epsg_change, epsg_coordinateaxis, epsg_coordinateaxisname, epsg_coordinatereferencesystem, epsg_coordinatesystem, epsg_coordoperation, epsg_coordoperationmethod, epsg_coordoperationparam, epsg_coordoperationparamusage, epsg_coordoperationparamvalue, epsg_coordoperationpath, epsg_datum, epsg_deprecation, epsg_ellipsoid, epsg_namingsystem, epsg_primemeridian, epsg_supersession, epsg_unitofmeasure, epsg_versionhistory to "Geotools"' EPSG
Password: Geotools
GRANT
$ su -l postgres -c "psql -c 'alter user \"Geotools\" with NOCREATEDB' EPSG"
ALTER USER
 
- Deleting the database

$ su -l postgres -c 'dropdb EPSG'
DROP DATABASE

$ su -l postgres -c 'dropuser Geotools'
DROP USER
 

Making the database remotely accessible for all

- hb_pga.conf file:

     ...
     host	EPSG    	0.0.0.0  	0.0.0.0		password	web
 
Author: Didier Richard
Created: 2005-05-29
Revised: 2005-05-31
