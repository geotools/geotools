package org.geotools.data.ogr;

import static org.geotools.data.ogr.bridj.OgrLibrary.*;
import static org.geotools.data.ogr.bridj.CplErrorLibrary.*;

import java.io.IOException;

import org.bridj.Pointer;

/**
 * Common utilities to deal with OGR pointers
 * 
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("rawtypes")
class OGRUtils {

    private static boolean HAS_L_GETNAME;

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
     * Gets a layer name in a version independent way
     * @param layer
     */
    public static String getLayerName(Pointer layer) {
    	Pointer<Byte> namePtr = null;
    	try {
    		// this one is more efficient but has been added recently
    		if(HAS_L_GETNAME) {
    			namePtr = OGR_L_GetName(layer);
    		}
    	} catch(Exception e) {
    		HAS_L_GETNAME = false;
    	}
    	if(namePtr == null) {
	    	Pointer layerDefinition = OGR_L_GetLayerDefn(layer);
			namePtr = OGR_FD_GetName(layerDefinition);
    	}
		return getCString(namePtr);
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
        
        String error = getCString(CPLGetLastErrorMsg());

        switch (ogrError) {
        case OGRERR_CORRUPT_DATA:
            throw new IOException("OGR reported a currupt data error: " + error);
        case OGRERR_FAILURE:
            throw new IOException("OGR reported a generic failure: " + error);
        case OGRERR_INVALID_HANDLE:
            throw new IOException("OGR reported an invalid handle error: " + error);
        case OGRERR_NOT_ENOUGH_DATA:
            throw new IOException("OGR reported not enough data was provided in the last call: " + error);
        case OGRERR_NOT_ENOUGH_MEMORY:
            throw new IOException("OGR reported not enough memory is available: " + error);
        case OGRERR_UNSUPPORTED_GEOMETRY_TYPE:
            throw new IOException("OGR reported a unsupported geometry type error: " + error);
        case OGRERR_UNSUPPORTED_OPERATION:
            throw new IOException("OGR reported a unsupported operation error: " + error);
        case OGRERR_UNSUPPORTED_SRS:
            throw new IOException("OGR reported a unsupported SRS error: " + error);
        default:
            throw new IOException("OGR reported an unrecognized error code: " + ogrError);
        }
    }

}
