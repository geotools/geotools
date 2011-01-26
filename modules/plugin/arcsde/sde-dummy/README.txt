This module produces a fake ArcSDE Java API in order to let the geotools
arsde plugin compile without requiring the presence of the ArcSDE Java
API jars from ESRI.

The code is written without looking at the ESRI jars or their javadocs, 
by incrementally adding the classes and methods needed to fix the compile
errors on the arcsde plugin, so it was agreed by the OSGEO mentors that 
it is clean from a reverse-engineering perspective.  
