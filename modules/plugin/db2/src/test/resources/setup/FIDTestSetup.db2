connect to geotools;
drop table "Test".fidautoinc;
drop table "Test".fidnoprikey;
drop table "Test".fidintprikey;
drop table "Test".fidcharprikey;
drop table "Test".fidvcharprikey;
drop table "Test".fidmcolprikey;


create table "Test".fidautoinc (
    idcol integer not null primary key generated always as identity
   ,geom db2gse.st_point)
;
create table "Test".fidintprikey (
    idcol integer not null primary key
   ,geom db2gse.st_point)
;
create table "Test".fidnoprikey (
    idcol integer
   ,geom db2gse.st_point)
;
create table "Test".fidcharprikey (
    idcol char(15) not null primary key
   ,geom db2gse.st_point)
;
create table "Test".fidvcharprikey (
    idcol varchar(17) not null primary key
   ,geom db2gse.st_point
   )
;
create table "Test".fidmcolprikey (
    idcol1 char(11) not null
   ,idcol2 int      not null
   ,geom db2gse.st_point
   ,constraint mcol_pk primary key(idcol1, idcol2)
   )
;


ECHO insert some test values for keys;
INSERT INTO "Test".fidautoinc(geom)
  VALUES
         (db2gse.st_point(-76.0, 42.5, 1)),
         (db2gse.st_point(-76.5, 42.0, 1))
  ;


INSERT INTO "Test".fidintprikey(idcol, geom)
  VALUES
         (1, db2gse.st_point(-76.0, 42.5, 1)),
         (2, db2gse.st_point(-76.5, 42.0, 1))
  ;

INSERT INTO "Test".fidnoprikey(idcol, geom)
  VALUES
         (1, db2gse.st_point(-76.0, 42.5, 1)),
         (2, db2gse.st_point(-76.5, 42.0, 1))
  ;

INSERT INTO "Test".fidcharprikey(idcol, geom)
  VALUES
         ('key1', db2gse.st_point(-76.0, 42.5, 1)),
         ('key2', db2gse.st_point(-76.5, 42.0, 1))
  ;

INSERT INTO "Test".fidvcharprikey(idcol, geom)
  VALUES
         ('key1', db2gse.st_point(-76.0, 42.5, 1)),
         ('key2', db2gse.st_point(-76.5, 42.0, 1))
  ;

INSERT INTO "Test".fidmcolprikey(idcol1, idcol2, geom)
  VALUES
         ('key1', 1, db2gse.st_point(-76.0, 42.5, 1)),
         ('key2', 2, db2gse.st_point(-76.5, 42.0, 1))
  ;
