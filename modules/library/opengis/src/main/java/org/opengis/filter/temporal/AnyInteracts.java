/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
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
package org.opengis.filter.temporal;

import org.opengis.annotation.XmlElement;

/**
 * Filter operator that determines if two temporal periods interact in any way as defined by the
 * Filter Encoding Specification.
 *
 * <p>The OverlappedBy operator is defined as a shortcut for the following:
 *
 * <pre>
 *  NOT (Before OR Meets OR MetBy OR After).
 * </pre>
 *
 * @author Justin Deoliveira, OpenGeo
 * @see http://portal.opengeospatial.org/files/?artifact_id=39968
 * @since 8.0
 */
@XmlElement("AnyInteracts")
public interface AnyInteracts extends BinaryTemporalOperator {
    /** Operator name */
    public static final String NAME = "AnyInteracts";
}
