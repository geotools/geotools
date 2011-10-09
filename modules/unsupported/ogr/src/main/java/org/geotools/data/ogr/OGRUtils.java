package org.geotools.data.ogr;

import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import java.io.IOException;

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
    
    public static String getCString(Pointer<Byte> ptr) {
        if(ptr == null) {
            return null;
        } else {
            return ptr.getCString();
        }
    }

    /**
     * Checks the OGRErr status code and throws java exceptions accordingly
     * 
     * @param ogrError
     * @throws IOException
     */
    public static void checkError(int ogrError) throws IOException {
        if (ogrError == OGRERR_NONE) {
            return;
        }

        switch (ogrError) {
        case OGRERR_CORRUPT_DATA:
            throw new IOException("OGR reported a currupt data error");
        case OGRERR_FAILURE:
            throw new IOException("OGR reported a generic failure");
        case OGRERR_INVALID_HANDLE:
            throw new IOException("OGR reported an invalid handle error");
        case OGRERR_NOT_ENOUGH_DATA:
            throw new IOException("OGR reported not enough data was provided in the last call");
        case OGRERR_NOT_ENOUGH_MEMORY:
            throw new IOException("OGR reported not enough memory is available");
        case OGRERR_UNSUPPORTED_GEOMETRY_TYPE:
            throw new IOException("OGR reported a unsupported geometry type error");
        case OGRERR_UNSUPPORTED_OPERATION:
            throw new IOException("OGR reported a unsupported operation error");
        case OGRERR_UNSUPPORTED_SRS:
            throw new IOException("OGR reported a unsupported SRS error");
        default:
            throw new IOException("OGR reported an unrecognized error code: " + ogrError);
        }
    }

}
