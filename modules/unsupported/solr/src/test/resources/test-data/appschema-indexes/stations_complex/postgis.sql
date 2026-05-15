CREATE EXTENSION IF NOT EXISTS postgis;

CREATE SCHEMA IF NOT EXISTS meteo;

CREATE TABLE IF NOT EXISTS meteo.meteo_parameters (
    id integer PRIMARY KEY,
    param_name varchar(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS meteo.meteo_stations (
    id integer PRIMARY KEY,
    common_name varchar(128) NOT NULL,
    comments text,
    position geometry(Point, 4326)
);

CREATE TABLE IF NOT EXISTS meteo.meteo_observations (
    id integer PRIMARY KEY,
    station_id integer NOT NULL REFERENCES meteo.meteo_stations(id),
    parameter_id integer NOT NULL REFERENCES meteo.meteo_parameters(id),
    value double precision,
    description text
);

INSERT INTO meteo.meteo_parameters (id, param_name) VALUES
    (1, 'temperature'),
    (2, 'wind')
ON CONFLICT (id) DO NOTHING;

INSERT INTO meteo.meteo_stations (id, common_name, comments, position) VALUES
    (7, 'Bologna', 'The probability of precipitation (POP), is defined as the likelihood of occurrence (expressed as a percent) of a measurable amount of liquid precipitation', ST_GeomFromText('POINT(11.34 44.5)', 4326)),
    (13, 'Alessandria', 'No risk of severe thunderstorms.', ST_GeomFromText('POINT(8.63 44.92)', 4326))
ON CONFLICT (id) DO NOTHING;

INSERT INTO meteo.meteo_observations (id, station_id, parameter_id, value, description) VALUES
    (1, 13, 1, 12.0, 'sky'),
    (2, 13, 2, 7.0, 'OneDrive for Business on Linux'),
    (3, 7, 1, 20.0, 'temperature, wind'),
    (4, 7, 1, 15.0, 'precipitation on a routine basis'),
    (5, 7, 2, 80.0, NULL)
ON CONFLICT (id) DO NOTHING;
