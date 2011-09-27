package org.geotools.data.efeature;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class implements a {@link CoordinateReferenceSystem CRS} cache.
 * <p>
 * Each {@link CoordinateReferenceSystem CRS} is associated with a 
 * spatial reference ID. The reference to each 
 * cached {@link CoordinateReferenceSystem CRS} is {@link WeakReference weak}, 
 * ensuring that unused CRS can be garbage collected when more memory is needed.  
 * </p>
 * @author kengu
 *
 *
 * @source $URL$
 */
public class CRSCache 
{
    private static final WeakHashMap<String, CoordinateReferenceSystem> 
        cache = new WeakHashMap<String, CoordinateReferenceSystem>();

    public static CoordinateReferenceSystem decode(String srid)
            throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = cache.get(srid);
        if (crs == null) {
            //
            // Decode srid into a CRS instance
            //
            crs = CRS.decode(srid);
            //
            // Add to cache
            //
            cache.put(srid, crs);
        }
        return crs;
    }

    public static CoordinateReferenceSystem decode(EFeatureInfo eInfo, boolean tryDefault)
            throws Exception {
        try {
            // Decode SRID code into CRS instance.
            // Use cache to reduce memory footprint.
            //
            eInfo.crs = decode(eInfo.srid);
    
            // Finished
            //
            return eInfo.crs;
        } catch (Exception e) {
            if (!tryDefault) {
                throw e;
            }
        }
        // Try once more, this time using default SRID code
        //
        eInfo.srid = EFeatureConstants.DEFAULT_SRID;
    
        // Do not try to recover this time, throws a
        // CoreExeception it this also fails.
        //
        return decode(eInfo, false);
    }

}
