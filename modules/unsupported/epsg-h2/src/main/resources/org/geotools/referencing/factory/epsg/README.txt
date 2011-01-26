Updating the EPSG database
---------------------------

The database in the zip file is a HSQL version of the official EPSG database.

1) Download from www.epsg.org the "geodetic dataset" creation scripts for the PostgreSQL database
2) Unzip them in _this_ directory, you should get three files named EPSG_v<version>.mdb_Data_PostgreSQL.sql,
   EPSG_v<version>.mdb_FKeys_PostgreSQL.sql and EPSG_v<version>.mdb_Tables_PostgreSQL.sql, where
   <version> is the the version of the EPSG database ("7_1" at the time of writing)  
3) Remove the _v<version.mdb part from all names, resulting in names like EPSG_Data_PostgreSQL.sql
5) Hand modify the EPSG_Tables_PostgreSQL.sql file and replace all TEXT types with VARCHAR(4096)
6) Update ThreadedH2EpsgFactory.VERSION to the current version
7) Run the DatabaseCreationScript.java
8) Check the EPSG.zip file has been updated
9) Run the build with extensive tests in the epsg-h2 module:
   mvn clean install -Pextensive.tests
   
   This step might force you to amend tests or modify the referencing subsystem to handle new
   axis direction definitions, new unit of measure and the like
10) Remove all the sql files in this directory, keeping only EPSG_Indexes_H2.sql (which was already
    there when you started). Also remove an eventual EPSG.*.log.db file.
11) Commit the changes
12) Congratulations, you're done!


PostgreSQL notes
------------------------------

Sometimes it is handy to have the database loaded in PostgreSQL to run random queries
against it. In order to load it you'll have to change the psql encoding thought:
createdb epsg<version>
psql espg<version>
\encoding latin9
\i EPSG_v<version>.mdb_Tables_PostgreSQL.sql
\i EPSG_v<version>.mdb_Data_PostgreSQL.sql
\i EPSG_v<version>.mdb_FKeys_PostgreSQL.sql

     