export JNAERATOR=/home/aaime/devel/gdal/jnaerator-0.9.9-SNAPSHOT-shaded.jar
export GDAL_BASE=/home/aaime/devel/gdal/gdal-1.8.0
java -jar $JNAERATOR -I. -I$GDAL_BASE/gcore -I$GDAL_BASEport -I$GDAL_BASEogr -package org.geotools.data.ogr.bridj -library osr $GDAL_BASE/ogr/ogr_srs_api.h -library ogr $GDAL_BASE/ogr/ogr_core.h $GDAL_BASE/ogr/ogr_api.h -library cplError $GDAL_BASE/port/cpl_error.h -o src/main/java  -v -runtime BridJ -reification  -nocpp -DCPL_DLL= -DCPL_STDCALL= -DCPL_C_START= -DCPL_C_END= -noComp -parseInOneChunk 
