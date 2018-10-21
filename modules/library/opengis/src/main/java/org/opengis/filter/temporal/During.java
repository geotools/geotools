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
 * Filter operator that determines if a temporal object is located during another temporal object as
 * defined by the Filter Encoding Specification.
 *
 * <p>The During operator is defined by ISO 19108 and has the following semantics:
 *
 * <table border='1'>
 *   <tr align='center'>
 *     <td>t1,t2</td><td>t1[],t2</td><td>t1,t2[]</td><td>t1[],t2[]</td>
 *   </tr>
 *   <tr>
 *     <td>n/a</td><td>n/a</td><td>t2.start < t1 < t2.end</td><td>t1.start > t2.start and t1.end < t2.end</td>
 *   </tr>
 * </table>
 *
 * @author Justin Deoliveira, OpenGeo
 * @see http://portal.opengeospatial.org/files/?artifact_id=39968
 * @since 8.0
 */
@XmlElement("During")
public interface During extends BinaryTemporalOperator {
    /** Operator name */
    public static final String NAME = "During";
}
