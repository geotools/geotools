SDMX DataStore
==============


Overview
--------

Proof of concept of an SDMX datastore. SDMX is a web-service protocol for the exchange of staitstical 
data used by national statistics offices and multilateral organisations such as OCED, EuroStat, 
The World Bank.


Requirements
------------

Connection to an SDMX ReST 2.1 server.



Functionality
-------------

This datastore allow the query of SDMX dataflows with filtes (either WFS or CQL) that bound the 
dimensions and specify the measure to return (only one measure is returned).

When filters are bound to a an empty diemsion level (as in AGE=''), all levels of that dimension 
are returned. 

The list of available provides is:
1. ABS
2. ECB
3. Estat
4. ILO
5. IMF
6. Ineg
7. Istat
8. NBB
9. Oecd
10. UIS
11. WB
12. WITS

But only ABS has been tried (endpoint http://stat.data.abs.gov.au/restsdmx/sdmx.ashx with no username or password).

Before requesting data, dimension members have to be found out, hence every SDMX data cube, has a "twin"
feature type that contains the list of dimensions and dimension members.

For example, the data cube (dataflow, in SDMX jargon) ABORIGINAL_POP_PROJ_REMOTE__SDMX has a "twin" ABORIGINAL_POP_PROJ_REMOTE__SDMX__DIMENSIONS that holds dimensions and members.

The list of dimensions can be gotten with the following query (change hostname as needed): 
curl -XGET "http://geoserverarcgis/geoserver/wfs?request=GetFeature&service=wfs&version=1.1.0&typeName=aurin:ABORIGINAL_POP_PROJ_REMOTE__SDMX__DIMENSIONS&cql_filter=CODE=%27all%27"
that returns a list of all dimensions as features (attributes CODE and DESCRIPTION). 
Please note the expression "CODE='all'" ("CODE" must be uppercase)

Then the members of every dimension can be queried with  
curl -XGET "http://geoserverarcgis/geoserver/wfs?request=GetFeature&service=wfs&version=1.1.0&typeName=aurin:ABORIGINAL_POP_PROJ_REMOTE__SDMX__DIMENSIONS&cql_filter=CODE=%27AGE%27"
that returns a list of all dimensions as features (attributes CODE and DESCRIPTION). 
Please note the expression "CODE='AGE'" ("CODE" must be uppercase)


CQL Examples
------------

All measures bound
MEASURE= '1' and MSTP='TOT' and AGE='TOT' and STATE='1' and REGIONTYPE='STE' and 
REGION in ('1','2','3','4') and FREQUENCY='A'

All ages request
MEASURE= '1' and MSTP='TOT' and STATE='1' and REGIONTYPE='STE' and 
REGION in ('1','2','3','4') and FREQUENCY='A'


WFS Exmaples
------------ 
(Change hostname as needed)

All measures bound request:
curl -XGET "http://geoserverarcgis/wfs?cql_filter=AGE+in+%28%27A15%27%2C%27A10%27%29+and+MEASURE+in+%28%27POP_PROJ%27%29+and+FREQUENCY+in+%28%27A%27%29+and+SERIES+in+%28%272%27%29+and+SEX_ABS+in+%28%272%27%29+and+REGION+in+%28%272%27%29&service=wfs&request=GetFeature&typeName=aurin%3AABORIGINAL_POP_PROJ_REMOTE__SDMX&version=1.1.0"

All ages request:
curl -XGET "http://geoserverarcgis/geoserver/wfs?cql_filter=MEASURE+in+%28%27POP_PROJ%27%29+and+FREQUENCY+in+%28%27A%27%29+and+SERIES+in+%28%272%27%29+and+SEX_ABS+in+%28%272%27%29+and+REGION+in+%28%272%27%29&service=wfs&request=GetFeature&typeName=aurin%3AABORIGINAL_POP_PROJ_REMOTE__SDMX&version=1.1.0"

        