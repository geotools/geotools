package org.geotools.data.ogr;

import static org.geotools.data.ogr.bridj.OgrLibrary.*;
import static org.geotools.data.ogr.bridj.OsrLibrary.*;

import org.bridj.Pointer;

/**
 * Common utilities to deal with OGR pointers
 * 
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("rawtypes")
class OGRUtils {

    public static void releaseDataSource(Pointer dataSet) {
        if (dataSet != null) {
            OGRReleaseDataSource(dataSet);
            dataSet.release();
        }
    }

    public static void releaseLayer(Pointer layer) {
        if (layer != null) {
            // OGR_L_Dereference(layer);
            layer.release();
        }

    }

    public static void releaseDefinition(Pointer definition) {
        if (definition != null) {
            // OGR_FD_Destroy(definition);
            definition.release();
        }
    }

    public static void releaseSpatialReference(Pointer spatialReference) {
        if (spatialReference != null) {
            // OSRDestroySpatialReference(spatialReference);
            spatialReference.release();
        }

    }

}
