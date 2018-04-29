package org.geotools.gce.imagemosaic;

/**
 * Policy for excess granule removal
 *
 * @author Andrea Aime
 */
public enum ExcessGranulePolicy {

    /** No excess granule removal is performed. This is the default */
    NONE,
    /** Excess granule removal based on image ROI, performed in raster space */
    ROI
    // TODO: we could add a policy based on vector footprint in model space, if there is one that's
    // accurate enough
}
