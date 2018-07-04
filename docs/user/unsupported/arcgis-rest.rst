ArcGIS ReST API DataStore
=========================


Overview
--------

This datastore implements a very limited portion of the ArcGIS ReST API 
(http://resources.arcgis.com/en/help/arcgis-rest-api/), which is supported by both ArcGIS Server 
and ArcGIS Online. 

Specifically, only the FeatureServer services on either ArcGIS Online (Open Data) or 
ArcGIS Server FeatureServers are covered so far.

FeatureServers, non entire services, are used as endpoints for ArcGIS
Servers, beacuse there may be hundreds of different layers, and it seems ESRI throttlea back 
requests, leading to very long times for building feature types.


The main parmater is the API URL, which can take the form of:
* ArcGIS Online (Open Data): http://data.<data provider name>.opendata.arcgis.com/data.json  
* ArcGIS FeaureServer: http://services.arcgis.com/<daat provider id>/ArcGIS/rest/services/<folder>/FeatureServer

Some services to test:
http://data.dhs.opendata.arcgis.com/data.json (Open Data catalog)
http://services.arcgis.com/rOo16HdIMeOBI4Mb/ArcGIS/rest/services/Zoning_Data/FeatureServer
http://services.arcgis.com/rOo16HdIMeOBI4Mb/ArcGIS/rest/services/Airports_2/FeatureServer

The first endpoint uses a self-signed certificate, hence you have to add it to
Tomcat with the procedure `this procedure <https://blogs.oracle.com/gc/unable-to-find-valid-certification-path-to-requested-target/>`_


Requirements
------------

ArcGIS ReST API >= 10.41


Functionality
-------------

Currently, only a part of the Feature Service (http://resources.arcgis.com/en/help/arcgis-rest-api/#/Feature_Service/02r3000000z2000000/)
is implemented, which allows to:

* It can retrieve a list of avaialable layers from a FeatureServer
* It can query a layer by bounding box

