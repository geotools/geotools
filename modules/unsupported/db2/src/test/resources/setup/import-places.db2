!erase Placesexp*;
!erase Placesimp*;
connect to geotools;
drop table "Test"."Places";
drop table "Test"."Places0";

create table "Test"."Places0"
   (
    "Id"       int generated always as identity primary key
  , "Name"     varchar(30)
  , "Geom"     db2gse.st_polygon)
    ;
create table "Test"."Places"
   (
    "Id"       int generated always as identity primary key
  , "Name"     varchar(30)
  , "Geom"     db2gse.st_polygon)
    ;


!db2se import_shape geotools
  -fileName data\Places.shp
  -srsName NAD83_SRS_1
  -tableSchema \"Test\"
  -tableName   \"Places0\"
  -spatialColumn \"Geom\"
  -typeSchema db2gse
  -typeName   st_polygon
  -client 1
  -createTableFlag 0
---idColumn id
---idColumnIsIdentity 1
  -messagesFile Placesimp.msg
  -exceptionFile Placesimperr
  -commitScope 100
  ;

!db2se register_spatial_column geotools
  -srsName NAD83_SRS_1
  -tableSchema \"Test\"
  -tableName   \"Places0\"
  -columnName    \"Geom\"
  ;


!db2se register_spatial_column geotools
  -srsName NAD83_SRS_1
  -tableSchema \"Test\"
  -tableName   \"Places\"
  -columnName    \"Geom\"
  ;

select count(*) from "Test"."Places0";
insert into "Test"."Places" select * from "Test"."Places0";
select count(*) from "Test"."Places";
describe table  "Test"."Places0";

