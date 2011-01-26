package org.geotools.caching.util;

import org.geotools.caching.spatialindex.Region;
import org.geotools.filter.spatial.BBOXImpl;

import com.vividsolutions.jts.geom.Envelope;

public class CacheUtil {
	/**
	 * Extracts an envelope from a bbox filter.
	 * 
	 * @param filter
	 * @return
	 */
	public static Envelope extractEnvelope(BBOXImpl filter) {
        return new Envelope(filter.getMinX(), filter.getMaxX(), filter.getMinY(), filter.getMaxY());
    }
	
    /**
     * Converts and envelope to a region.
     *
     * @param e
     * @return null if e is null; otherwise a region
     */
    public static Region convert(Envelope e) {
        if (e == null) return null;
        return new Region(new double[] { e.getMinX(), e.getMinY() },
            new double[] { e.getMaxX(), e.getMaxY() });
    }

    /**
     * Converts a region to an envelope.
     *
     * @param r
     * @return
     */
    public static Envelope convert(Region r) {
        return new Envelope(r.getLow(0), r.getHigh(0), r.getLow(1), r.getHigh(1));
    }
}
