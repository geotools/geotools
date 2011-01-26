!erase roadsexp*;
!erase roadsimp*;
connect to geotools;
drop table "Test"."Roads";
drop table "Test"."Roads0";

create table "Test"."Roads0"
   (
    "ID"       int not null primary key
  , "Name"     varchar(30)
  , "Length"   double
  , "Geom"     db2gse.st_linestring)
    ;

create table "Test"."Roads"
   (
    "ID"       int not null primary key
  , "Name"     varchar(30)
  , "Length"   double
  , "Geom"     db2gse.st_linestring)
    ;

-- create SRS for New York  state-plane zone 3101 data
!db2se create_srs geotools
  -srsName      NY3101
  -srsId        3101
  -xOffset      0
  -yOffset      0
  -xScale       100
  -coordsysName NAD_1983_STATEPLANE_NEW_YORK_EAST_FIPS_3101_FEET
 ;

!db2se import_shape geotools
  -fileName data\roads.shp
  -srsName NY3101
  -tableSchema \"Test\"
  -tableName   \"Roads0\"
  -spatialColumn \"Geom\"
  -typeSchema db2gse
  -typeName   st_linestring
  -client 1
  -createTableFlag 0
  -messagesFile roadsimp.msg
  -exceptionFile roadsimperr
  -commitScope 100
  ;

!db2se register_spatial_column geotools
  -srsName NY3101
  -tableSchema \"Test\"
  -tableName   \"Roads\"
  -columnName    \"Geom\"
  ;

!db2se register_spatial_column geotools
  -srsName NY3101
  -tableSchema \"Test\"
  -tableName   \"Roads0\"
  -columnName    \"Geom\"
  ;

select count(*) from "Test"."Roads0";
insert into "Test"."Roads" select * from  "Test"."Roads0";
select count(*) from "Test"."Roads";
describe table  "Test"."Roads";

