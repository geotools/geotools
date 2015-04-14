# WMS

## IP Review

 - Jody Garnett, August 7th, 2006

 - Adrian Custer, June 2008

STATUS: DIRTY

 * :star: Code is clean
 * :warning: Two icons of unknown origin
 * :warning: Test xml data origin unknown: http://jira.codehaus.org/browse/GEOT-1891

```
org.getools.catalog.wms
org.geotools.data.ows
org.geotools.data.wms
```

Fine.

```
org.geotools.data.wms.gce
```

An attempt, successful, to wrap a WMS as a grid coverage exchanged (too bad it was a silly idea).

```
org.geotools.data.wms.request
org.geotools.data.wms.response
```

Fine.

```
org.geotools.data.wms.xml
```

Bleah, scary classes that start with a lowercase letter.


```
org.geotools.data.ows.test (test)
org.geotools.data.wms.[gce|xml].test (test)
```

Fine, note most of the useful tests are "online".

## Initial Review

### src

```
org.geotools.catalog.wms
```

* Files in this package originated from uDig. 
* WMSGeoResource.java - no copyright, authors dzwiers, jdeolive, rgould. Copyright added.

```
org.geotools.data.ows
```

* StyleImpl.java - no copyright, author rgould. Copyright added.

```
org.geotools.data.wms
```

* WMS1_1_0.java - no copyright, author rgould. Copyright added.
* WMS1_3_0.java - no copyright, author rgould. Copyright added.

```
org.geotools.data.wms.request
```

* AbstractGetFeatureInfoRequest.java - no copyright, author rgould. Copyright added.
* AbstractGetMapRequest.java - no copyright, author rgould. Copyright added.
* GetCapabilitiesRequest.java - no copyright, author rgould. Copyright added.

```
org.geotools.data.wms.response
```

* GetCapabilitiesResponse.java - no copyright, author rgould. Copyright added.

```
org.geotools.data.wms.xml
```

* ogcComplexType.java - no copyright, author rgould. Copyright added.
* ogcComplexTypes.java - no copyright, author rgould. Copyright added.
* ogcElement.java - no copyright, author rgould. Copyright added.
* OGCSchema.java - no copyright, author rgould. Copyright added.
* ogcSimpleType.java - no copyright, author rgould. Copyright added.
* ogcSimpleTypes.java - no copyright, author rgould. Copyright added.
* WMSSchema.java - no copyright, author rgould. Copyright added.

### test

```
org.geotools.data.ows.test
```

* LayerInheritanceTest.java - no copyright, author Bill Woodward. Copyright added.

```
org.geotools.data.wms.test
```

* AbstractGetMapRequestTest.java - no copyright, author rgould. Copyright added.
* Geot553Test.java - udig copyright (oops), author rgould. Copyright fixed.
* ServersTest.java - no copyright, author rgould. Copyright added.

```
org.geotools.data.xml.test
```

* WMSSchemaTest.java - no copyright, author rgould. Copyright added.