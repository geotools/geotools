/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;



/**
 * An Extent gives feature/coverage/raster/matrix dimension extent.
 *
 * <p>
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Extent"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         An Extent gives
 *              feature/coverage/raster/matrix dimension extent.        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:Name"/&gt;
 *              &lt;xsd:element ref="sld:Value"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *  </code>
 *  </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public interface Extent {
    /**
     * DOCUMENT ME!
     *
     * @return The name of the extent.
     */
    String getName();

    /**
     * DOCUMENT ME!
     *
     * @param name Thw new name of the extent.
     */
    void setName(String name);

    /**
     * DOCUMENT ME!
     *
     * @return The value of the exent.
     */
    String getValue();

    /**
     * DOCUMENT ME!
     *
     * @param value The new value of the exent.
     */
    void setValue(String value);
}
