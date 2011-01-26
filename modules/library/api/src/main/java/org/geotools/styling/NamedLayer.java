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

import java.util.List;


/**
 * A NamedLayer is used to refer to a layer that has a name in a WMS.
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="NamedLayer"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A NamedLayer is a layer of data that has a name advertised by a WMS.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:Name"/&gt;
 *       &lt;xsd:element ref="sld:LayerFeatureConstraints" minOccurs="0"/&gt;
 *       &lt;xsd:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *         &lt;xsd:element ref="sld:NamedStyle"/&gt;
 *         &lt;xsd:element ref="sld:UserStyle"/&gt;
 *       &lt;/xsd:choice&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 * </p>
 * @source $URL$
 */
public interface NamedLayer extends StyledLayer {
	
	public List<FeatureTypeConstraint> layerFeatureConstraints();
    public FeatureTypeConstraint[] getLayerFeatureConstraints();
    public void setLayerFeatureConstraints(FeatureTypeConstraint[] constraints);

    public List<Style> styles();
    public Style[] getStyles();
    public void addStyle(Style sl);

    /**
     * Used to navigate a Style/SLD.
     *
     * @param visitor
     */
    void accept(StyleVisitor visitor);
}
