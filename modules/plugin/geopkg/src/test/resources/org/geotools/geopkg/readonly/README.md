# Read only test data

This directory contains `cities_srs_84-with-incomplete-transaction.gpkg` which is
a copy of `../cities_srs_84.gpkg` that has an incomplete transaction by:

- opening in QGIS
- going into edit mode
- killing QGIS
- deleting the wal/shm files

Tests will then make this geopackage and the directory readonly whilst they
run to emulate a read only filesystem.
