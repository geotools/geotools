/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.spatial;

// Annotations
import org.opengis.annotation.XmlElement;


/**
 * {@linkplain SpatialOperator Spatial operator} that evaluates to {@code true} when the bounding
 * box of the feature's geometry overlaps the bounding box provided in this object's properties.
 * An implementation may choose to throw an exception if one attempts to test
 * features that are in a different SRS than the SRS contained here.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("BBOX")
public interface BBOX extends BinarySpatialOperator {
	/** Operator name used to check FilterCapabilities */
	public static String NAME = "BBOX";
	
    /**
     * Name of the geometric property that will be used in this spatial operator.
     * <p>
     * This may be null if the default spatial property is to be used.
     * @deprecated Please check getExpression1(), if it is a PropertyName
     */
    @XmlElement("PropertyName")
    String getPropertyName();

    /**
     * Returns the spatial reference system in which the bounding box
     * coordinates contained by this object should be interpreted.
     * <p>
     * This string must take one of two forms: either 
     * <ul>
     * <li>"EPSG:xxxxx" where "xxxxx" is a valid EPSG coordinate system code;
     * <li>OGC URI format
     * <li>or an OGC well-known-text representation of a coordinate system as
     *     defined in the OGC Simple Features for SQL specification.
     * </ul>
     * @deprecated please use getExpression2(), if it is a literal BoundingBox.getCoordinateReferenceSystem()
     */
    String getSRS();

    /**
     * Assuming getExpression2() is a literal bounding box access
     * the minimum value for the first coordinate.
     * 
     * @deprecated please use getExpression2(), to check for a literal BoundingBox.getMinimum(0)
     */
    double getMinX();

    /**
     * Assuming getExpression2() is a literal bounding box access 
     * the minimum value for the second ordinate.
     * @deprecated please use getExpression2(), to check for a literal BoundingBox.getMinimum(1)
     */
    double getMinY();

    /**
     * Assuming getExpression2() is a literal bounding box access 
     * the maximum value for the first ordinate.
     * 
     * @deprecated please use getExpression2(), to check for a literal BoundingBox.getMaximum(0)
     */
    double getMaxX();

    /**
     * Assuming getExpression2() is a literal bounding box access
     * the maximum value for the second coordinate.
     * @deprecated please use getExpression2(), to check for a literal BoundingBox.getMaximum(1)
     */
    double getMaxY();
}
