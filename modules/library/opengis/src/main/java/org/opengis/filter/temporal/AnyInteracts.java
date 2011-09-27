/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.temporal;

import org.opengis.annotation.XmlElement;

/**
 * Filter operator that determines if two temporal periods interact in any way as defined by the 
 * Filter Encoding Specification.
 * <p>
 * The OverlappedBy operator is defined as a shortcut for the following:
 * <pre>
 *  NOT (Before OR Meets OR MetBy OR After).
 * </pre>
 * </p>
 * 
 * @author Justin Deoliveira, OpenGeo
 * @see http://portal.opengeospatial.org/files/?artifact_id=39968
 * @since 8.0
 *
 * @source $URL$
 */
@XmlElement("AnyInteracts")
public interface AnyInteracts extends BinaryTemporalOperator {
    /** Operator name */
    public static final String NAME = "AnyInteracts";
}
