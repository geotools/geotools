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