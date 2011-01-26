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

import org.opengis.filter.Filter;


/**
 * A FeatureTypeConstraint identifies a specific feature type and supplies
 * fitlering.
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="FeatureTypeConstraint"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A FeatureTypeConstraint identifies a specific feature type and
 *       supplies fitlering.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:FeatureTypeName" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="ogc:Filter" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:Extent" minOccurs="0" maxOccurs="unbounded"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 * </p>
 *
 * <p></p>
 *
 * @author James Macgill
 * @source $URL$
 */
public interface FeatureTypeConstraint {
    public String getFeatureTypeName();

    public void setFeatureTypeName(String name);

    public Filter getFilter();

    public void setFilter(Filter filter);

    public Extent[] getExtents();

    public void setExtents(Extent[] extents);

    public void accept(org.geotools.styling.StyleVisitor visitor);
}
