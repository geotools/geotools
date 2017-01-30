# YSLD

Module Maintainer: Kevin Smith & Justin Deolivera

## IP Review:

 - Torben Barsballe - January 23, 2017

Status: CLEAN

 * :star: Code is clean
 * :star: Test data is clean. Much of it comes from the GeoServer SLD Cookbook, but GeoTools has been given permission to use this data.

### src/main:

```
org.geotools.ysld.encode
org.geotools.ysld.parse
org.geotools.ysld.transform.sld
org.geotools.ysld.transform.ysld
org.geotools.ysld.validate
```

Fine


```
org.geotools.ysld
```

`Colors.java`: Uses the set of X11 color names (MIT/X11 License). Compatible with the GeoTools license.


### src/test:

```
org.geotools.ysld.encode
org.geotools.ysld.parse
org.geotools.ysld.transform.sld
```

`YSLDEncodeCookbookTest.java`, `YSLDParseCookbookTest.java`, and `SLDTransformerTest.java`: Contains SLDs from GeoServer SLD Cookbook (CC BY 3.0/GPL 2.0). The GeoServer project has given GeoTools full permission to use these files.

```
org.geotools.ysld.poly
```

Fine

```
org.geotools.ysld.sld
```

All files: SLD styles from GeoServer SLD Cookbook (CC BY 3.0/GPL 2.0). The GeoServer project has given GeoTools full permission to use these files.

```
org.geotools.ysld.validate
```

Fine

```
org.geotools.ysld
```

`point.sld`: The default point.sld from GeoServer (GPL 2.0). Not used by gt-ysld. Removed.
