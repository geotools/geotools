/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

package org.opengis.feature;

/**
 * FeatureVisitor interface to allow for container optimised traversal.
 * <p>
 * The iterator construct from the Collections api is well understood and
 * loved, but breaks down for working with large GIS data volumes. By using a
 * visitor we allow the implementor of a Feature Collection to make use of
 * additional resources (such as multiple processors or tiled data)
 * concurrently.
 * </p>
 * This interface is most often used for calculations and data
 * transformations and an implementations may intercept known visitors
 * (such as "bounds" or reprojection) and engage an alternate work flow.
 * </p>
 * @author Cory Horner (Refractions Research, Inc)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/feature/FeatureVisitor.java $
 */
public interface FeatureVisitor {
    /**
     * Visit the provided feature.
     * <p>
     * Please consult the documentation for the FeatureCollection you are visiting
     * to learn more - the provided feature may be invalid, or read only.
     * @param feature
     */
    void visit(Feature feature);
}
