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
import org.opengis.geometry.BoundingBox3D;


/**
 * An extension to the general BBOX filter for supporting 3D Bounding Boxes that have a minimum and maximum Z-value.
 * 
 * {@linkplain SpatialOperator Spatial operator} that evaluates to {@code true} when the bounding
 * box of the feature's geometry overlaps the bounding box provided in this object's properties.
 * An implementation may choose to throw an exception if one attempts to test
 * features that are in a different SRS than the SRS contained here.
 * 
 *
 * @source $URL$
 * @author Niels Charlier
 * @since GeoAPI 2.0
 */
@XmlElement("BBOX3D")
public interface BBOX3D extends BBOX {
	/** Operator name used to check FilterCapabilities */
	public static String NAME = "BBOX3D";
	
	/**
     * Return 3D Bounding Box object representing the bounds of the filter
     * 
     * @Return Bounds of Filter
     */
    BoundingBox3D getBounds();

}
