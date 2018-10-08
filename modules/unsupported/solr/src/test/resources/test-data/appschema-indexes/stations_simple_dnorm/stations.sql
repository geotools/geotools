DROP SCHEMA meteo ;

CREATE SCHEMA meteo
    AUTHORIZATION sisapp;

-- DROP TABLE meteo.stations;

CREATE TABLE meteo.stations
(
    id integer NOT NULL,
    name character varying(100) COLLATE pg_catalog."default",
    location character varying(100) COLLATE pg_catalog."default",
    geom geometry(Point,4326),
    CONSTRAINT stations_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE meteo.stations
    OWNER to sisapp;
    
    
INSERT INTO meteo.stations(id, name, location, geom)
	VALUES (1, 'station1', 'Europe', ST_GeomFromText('POINT(-1 1)', 4326)),
           (2, 'station2', 'Asia', ST_GeomFromText('POINT(-2 1)', 4326))
           (3, 'station3', 'Australia', ST_GeomFromText('POINT(-3 1)', 4326)),
           (4, 'station4', 'Asia', ST_GeomFromText('POINT(-2 1)', 4326)),
           (5, 'station5', 'Latin America', ST_GeomFromText('POINT(0 1)', 4326)),
           (6, 'station6', 'Latin America', ST_GeomFromText('POINT(0 1)', 4326)),
           (7, 'station7', 'Europe', ST_GeomFromText('POINT(1 1)', 4326)),
           (8, 'station8', 'Antartida', ST_GeomFromText('POINT(1 4)', 4326)),
           (9, 'station9', 'Antartida', ST_GeomFromText('POINT(0 4)', 4326)),
           (10, 'station10', 'Asia', ST_GeomFromText('POINT(-2 1)', 4326)),
           (11, 'station12', 'Antartida', ST_GeomFromText('POINT(0 1)', 4326));
           
           

           