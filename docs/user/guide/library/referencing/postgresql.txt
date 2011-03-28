EPSG PostgreSQL Plugin
----------------------

GeoTools Coordinate Transformation Services can use the EPSG Geodesy Parameters database (http://www.epsg.org) to provide CoordinateReferenceSystem definitions for authority codes.

Note: GeoTools 2.5.1 does not support Postgresql version 8.3 with this class/jar. You must be using Postgresql 8.2 or less: Postgresql 8.3 will not work.

The following document describes how to set the version 6.5 database up on Linux. The original version of this document was written by Richard Didier and is attached at the end. The modified SQL scripts are also attached as EPSG_v65.mdb-sql.zip.

To use the gt-epsg-postgres:

1. Place the jar on your CLASSPATH
2. Supply global hints or system property

EPSG Install
^^^^^^^^^^^^

How to create an EPSG Geodesy Parameters database from the scripts provided by the EPSG.

These instructions assume the following:

* OS Linux (installed, working)
* Standard Mandrake installation of PostgreSQL.
* PostGIS installed in contrib pgsql (/usr/lib/pgsql).
* SQL scripts provided by the EPSG:
  
  1. EPSG_v65.mdb_DDL.sql (table definitions);
  2. EPSG_v65.mdb_DML.sql (data).

* Modified SQL scripts - attached as EPSG_v65.mdb-sql.zip
* Privileges needed:
  
  1. System administrator (SA);
  2. PostgreSQL administrator: usually launches the postgreSQL server (it
     is normally not able to log onto the system);
  3. a Unix user (epsg_reader), to administrate the epsg database (US).

These instructions use the following files:

* :download:`EPSG_v65.mdb-sql.zip </artifacts/EPSG_v65.mdb-sql.zip>`.

The EPSG SQL DDL and DML scripts have lots of Windows carriage returns, as well as spaces at the end of lines.

EPSG_v65.mdb_DDL.sql:

* #302, lacks a comma between the parameter_code and coord_op_method_code
* you should add a BEGIN; and a COMMIT; to the DDL (beginning and end)
* the (ALTER TABLE) constraints were moved to EPSG_v65.mdb_DDL2.sql

EPSG_v65.mdb_DML.sql:

* you should add a BEGIN; and a COMMIT; to the DML (beginning and end)
* the order of the tables does not conform with the integrity constraints, a better order is:
  
  * epsg_namingsystem
  * epsg_alias
  * epsg_area
  * epsg_change
  * epsg_deprecation
  * epsg_coordinateaxisname
  * epsg_coordinatesystem
  * epsg_unitofmeasure
  * epsg_coordinateaxis
  * epsg_ellipsoid
  * epsg_primemeridian
  * epsg_datum
  * epsg_coordinatereferencesystem
  * epsg_coordoperationmethod
  * epsg_coordoperation
  * epsg_coordoperationpath
  * epsg_coordoperationparam
  * epsg_coordoperationparamusage
  * epsg_coordoperationparamvalue
  * epsg_versionhistory

Notes about Richard's SQL scripts

* Because of the problems with the integrity constraints, modified SQL DDL and DML files were created (attached as EPSG_v65.mdb-sql.zip).
* These allow the integrity constraints to be added to the tables after the records are inserted.
* These new files are:
  
  * EPSG_v65.mdb_DDL.sql - table definitions
  * EPSG_v65.mdb_DDL2.sql - integrity constraints (ALTER TABLE)
  * EPSG_v65.mdb_DDL3.sql - drop table statements to DELETE all the epsg tables in the database
  * EPSG_v65.mdb_DML.sql - database records to insert

Here are the instructions:

1. Open access to the new database (SA):
   
   Geotools CTS will need to access the database through a JDBC driver.
   Modify the access configuration file
   var/lib/pgsql/data/hb_pga.conf/ (SA) add::
     
     ...
     host	epsg    127.0.0.1    255.255.255.255 password    web
     host	epsg    192.168.0.0  255.255.255.0   password    web
   
   
   This indicates that the database 'epsg' is accessible locally or via the machine
   with the IP address 192.168.0.0, requires a password, and is restricted to users
   whose name exists in the file /var/lib/pgsql/data/web::
   
     ...
     epsg_reader
   
   (SA) now restart the postgreSQL server::
     
     $ service postgresql restart
     Arrêt de postgresql                                             [  OK  ]
     Démarrage de postgresql                                         [  OK  ]

2. Create the administrator of the new epsg database (SA):
   
   Create the administrative user of the new epsg database. This can be done by the
   system administrator (SA) if the postgres user has no right to log onto linux
   system (his login shell does not belong to /etc/shells like /bin/false).
   
   The createuser can be carried out as the postgres user directly, if this user has
   permission to log onto the system.::
     
     $ su - postgres -c "createuser --createdb --adduser --pwprompt --echo epsg_reader"
     Enter password for user "epsg_reader":
     Enter it again:
     CREATE USER "epsg_reader" WITH  PASSWORD '#epsg' CREATEDB CREATEUSER
     CREATE USER

3. Create the database (US)
   
   The following steps describe how to create the epsg database and turn this into a
   PostGIS spatial database.
   
   Note:
   
   * why create a spatial database to manage of coordinate systems? The idea is to
     "verify" the spacial constraints associated with a system and, possibly, to represent
     it graphically!
   
   * Installing PostGIS (and some of the following steps) are not necessary to use the
     EPSG database with Geotools.
   
   The user (administrator) begins by creating a new postgreSQL database::
     
     $ createdb -U epsg_reader -h localhost -E LATIN9 -e epsg "EPSG sous postgreSQL"
     Password:
     CREATE DATABASE "epsg" WITH ENCODING = 'LATIN9'
     CREATE DATABASE
     Password:
     COMMENT ON DATABASE "epsg" IS 'EPSG sous postgreSQL'
     COMMENT
   
   Then the database administrator creates the PL/pgSQL language for PostGIS in
   the database::
     
     $ createlang -U epsg_reader -h localhost plpgsql epsg
     Password:
     Password:
     Password:
     Password:
   
   Next, import the functions and tables associated with PostGIS::
     
     $ psql -U epsg_reader -h localhost -f /usr/lib/pgsql/contrib/postgis/postgis.sql epsg
     Password:
     BEGIN
     ...
     COMMIT
     $ psql -U epsg_reader -h localhost -f /usr/lib/pgsql/contrib/postgis/spatial_ref_sys.sql epsg
     Password:
     BEGIN
     ...
     COMMIT
   
   Lastly, grant the epsg_reader user permission to read the PostGIS tables::
     
     $ psql -U epsg_reader -h localhost -c "grant select on geometry_columns, spatial_ref_sys to epsg_reader;" epsg
     Password:
     GRANT

4. Insert the EPSG data (US):
   
   Note:
   
   * If errors occur, the following SQL command must be issued to 
     clean the posgreSQL database before restarting the creation/import process::
       
       $ psql -U epsg_reader -h localhost -f /path/2/EPSG_v65.mdb_DDL3.sql epsg
   
   The administrator creates the EPSG tables::
     
     $ psql -U epsg_reader -h localhost -f /path/2/EPSG_v65.mdb_DDL.sql epsg
     Password:
     ...
   
   Then, insert the records::
     
     $ psql -U epsg_reader -h localhost -f /path/2/EPSG_v65.mdb_DML.sql epsg
     Password:
     ...
   
   Then, add the constraints::
     
     $ psql -U epsg_reader -h localhost -f /path/2/EPSG_v65.mdb_DDL2.sql epsg
     ...
   
   Finally, vacuume analyze the new database::
     
     $ vacuumdb -U epsg_reader -h localhost -z epsg
     ...

5. Check the database (US)::
     
     $ psql -U epsg_reader -h localhost epsg
     Password:
     Welcome to psql, the PostgreSQL interactive terminal.
     
     Type:  \copyright for distribution terms
            \h for help with SQL commands
            \? for help on internal slash commands
            \g or terminate with semicolon to execute query
            \q to quit
     
     epsg=# \dt
                       List of relations
                   Name              | Type  |    Owner
     --------------------------------+-------+-------------
      epsg_alias                     | table | epsg_reader
      epsg_area                      | table | epsg_reader
      epsg_change                    | table | epsg_reader
      epsg_coordinateaxis            | table | epsg_reader
      epsg_coordinateaxisname        | table | epsg_reader
      epsg_coordinatereferencesystem | table | epsg_reader
      epsg_coordinatesystem          | table | epsg_reader
      epsg_coordoperation            | table | epsg_reader
      epsg_coordoperationmethod      | table | epsg_reader
      epsg_coordoperationparam       | table | epsg_reader
      epsg_coordoperationparamusage  | table | epsg_reader
      epsg_coordoperationparamvalue  | table | epsg_reader
      epsg_coordoperationpath        | table | epsg_reader
      epsg_datum                     | table | epsg_reader
      epsg_deprecation               | table | epsg_reader
      epsg_ellipsoid                 | table | epsg_reader
      epsg_namingsystem              | table | epsg_reader
      epsg_primemeridian             | table | epsg_reader
      epsg_unitofmeasure             | table | epsg_reader
      epsg_versionhistory            | table | epsg_reader
      geometry_columns               | table | epsg_reader
      spatial_ref_sys                | table | epsg_reader
     (22 rows)
     
     epsg=# \q

6. Provides connection parameters
   
   Create a EPSG-DataSource.properties file in the user home directory with the following content:
   
   * serverName   = myserver.foo.com
   * databaseName = mydatabase
   * user         = ...
   * password     = ...
   
   If the Geotools libraries are installed, a better test will be::
     
     $ java -cp gt-epsg-postgresql-2.5.1.jar org.geotools.referencing.CRS EPSG:4326 EPSG:2154 EPSG:7412
     
     <=== EPSG 4326 ===>
     GEOGCS["WGS 84",
         DATUM["World Geodetic System 1984",
             SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]],
             AUTHORITY["EPSG","6326"]],
         PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
         UNIT["×0,017 rad",0.017453292519943278],
         AXIS["Geodetic latitude",NORTH],
         AXIS["Geodetic longitude",EAST],
         AUTHORITY["EPSG","4326"]]
     
     
     <=== EPSG 2154 ===>
     PROJCS["RGF93 / Lambert-93",
         GEOGCS["RGF93",
             DATUM["Reseau Geodesique Francais 1993",
                 SPHEROID["GRS 1980", 6378137.0, 298.257222101, AUTHORITY["EPSG","7019"]],
                 TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
                 AUTHORITY["EPSG","6171"]],
             PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
             UNIT["×0,017 rad",0.017453292519943278],
             AXIS["Geodetic latitude",NORTH],
             AXIS["Geodetic longitude",EAST],
             AUTHORITY["EPSG","4171"]],
         PROJECTION["Lambert_Conformal_Conic_2SP"],
         PARAMETER["semi_major", 6378137.0],
         PARAMETER["semi_minor", 6356752.314140356],
         PARAMETER["central_meridian", 3.0],
         PARAMETER["latitude_of_origin", 46.5],
         PARAMETER["false_easting", 700000.0],
         PARAMETER["false_northing", 6600000.0],
         PARAMETER["standard_parallel_1", 49.0],
         PARAMETER["standard_parallel_2", 44.0],
         UNIT["mètre",1.0],
         AXIS["Easting",EAST],
         AXIS["Northing",NORTH],
         AUTHORITY["EPSG","2154"]]
     
     
     <=== EPSG 7412 ===>
     COMPD_CS["NTF (Paris) / Lambert zone II + NGF IGN69",
         PROJCS["NTF (Paris) / Lambert zone II",
             GEOGCS["NTF (Paris)",
                 DATUM["Nouvelle Triangulation Francaise (Paris)",
                     SPHEROID["Clarke 1880 (IGN)", 6378249.2, 293.4660212936269, AUTHORITY["EPSG","7011"]],
                     AUTHORITY["EPSG","6807"]],
                 PRIMEM["Paris", 2.5969213, AUTHORITY["EPSG","8903"]],
                 UNIT["×0,016 rad",0.01570796326794895],
                 AXIS["Geodetic latitude",NORTH],
                 AXIS["Geodetic longitude",EAST],
                 AUTHORITY["EPSG","4807"]],
             PROJECTION["Lambert_Conic_Conformal_1SP"],
             PARAMETER["semi_major", 6378249.2],
             PARAMETER["semi_minor", 6356515.0],
             PARAMETER["central_meridian", 0.0],
             PARAMETER["latitude_of_origin", 46.79999999999995],
             PARAMETER["scale_factor", 0.99987742],
             PARAMETER["false_easting", 600000.0],
             PARAMETER["false_northing", 2200000.0],
             UNIT["mètre",1.0],
             AXIS["Easting",EAST],
             AXIS["Northing",NORTH],
             AUTHORITY["EPSG","27572"]],
         VERT_CS["NGF Lallemand",
             VERT_DATUM["Nivellement general de la France - Lalle", 2002, AUTHORITY["EPSG","5118"]],
             UNIT["mètre",1.0],
             AXIS["Gravity-related height",UP],
             AUTHORITY["EPSG","5719"]],
         AUTHORITY["EPSG","7412"]]
   
   For more information please see Referencing Configuration and Tool.