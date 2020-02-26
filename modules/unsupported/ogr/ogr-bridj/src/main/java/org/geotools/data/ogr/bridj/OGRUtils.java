/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.ogr.bridj;

import static org.geotools.data.ogr.bridj.CplErrorLibrary.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;

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
        if (ptr == null) {
            return null;
        } else {
            return ptr.getCString();
        }
    }

    /** Gets a layer name in a version independent way */
    public static String getLayerName(Pointer layer) {
        Pointer<Byte> namePtr = null;
        try {
            // this one is more efficient but has been added recently
            if (HAS_L_GETNAME) {
                namePtr = OGR_L_GetName(layer);
            }
        } catch (Exception e) {
            HAS_L_GETNAME = false;
        }
        if (namePtr == null) {
            Pointer layerDefinition = OGR_L_GetLayerDefn(layer);
            namePtr = OGR_FD_GetName(layerDefinition);
        }
        return getCString(namePtr);
    }
}
