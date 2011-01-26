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
 * LayerFeatureConstraints define what features and feature types are referenced
 * in a layer.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="LayerFeatureConstraints"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         LayerFeatureConstraints define what
 *              features &amp; feature types are         referenced in a
 *              layer.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:FeatureTypeConstraint" maxOccurs="unbounded"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public interface LayerFeatureConstraints {
    /**
     * @return The feature type constraints.
     */
    FeatureTypeConstraint[] getFeatureTypeConstraints();

    /**
     * @param constraints The new feature type constraints.
     */
    void setFeatureTypeConstraints(FeatureTypeConstraint[] constraints);
}
