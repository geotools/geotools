# Module XML WCS Bindings

Module Maintainer: Justin Deoliveira

## IP Review:
 
 - Alessio Fabiani, August 21th, 2008
 - Torben Barsballe July 3, 2015

STATUS: DIRTY

 * :warning: Code is missing some headers
 * :warning: All XML Schema files (c) Open Geospatial Consortium

### src/main/java/
```
org.geotools.gml4wcs
org.geotools.gml4wcs.bindings
org.geotools.wcs.bindings
```
No headers/no licence included
```
org.geotools.wcs
org.geotools.wcs.v1_1
org.geotools.wcs.v1_1.bindings
org.geotools.wcs.v2_0

```
Fine.

### src/main/resources/
```
org.geotools.gml4wcs
org.geotools.wcs
```
No license included.

```
org.geotools.wcs.v1_1

```
Copyright (c) 2007 Open Geospatial Consortium, Inc. All Rights Reserved.

```
org.geotools.wcs.v2_0
org.geotools.wcs.v2_0.range_subsetting.v1_0
org.geotools.wcs.v2_0.scal.v1_0
org.geotools.wcs.v2_0.wcseo.v1_0
```
Copyright (c) 2012 Open Geospatial Consortium.

### src/test
```
org.geotools.wcs.v1_1
org.geotools.wcs.v2_0
```
No headers/no license included

## Initial Review

STATUS as of July 2008: CLEAN

 * All classes created for the geotools project by the module maintainer have been updated to assign (c) to OSGEO in the header.
 * Much of the binding classes (marked with a @generated tag and then modified by hand) lack the @author attribution tag. 
