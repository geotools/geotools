/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.DataStore;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * A UserLayer allows a user-defined layer to be built from WFS and WCS data.
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="UserLayer"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A UserLayer allows a user-defined layer to be built from WFS and
 *       WCS data.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:RemoteOWS" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:LayerFeatureConstraints"/&gt;
 *       &lt;xsd:element ref="sld:UserStyle" maxOccurs="unbounded"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 * </p>
 *
 * @source $URL$
 */
public interface UserLayer extends StyledLayer {
    public RemoteOWS getRemoteOWS();

    public DataStore getInlineFeatureDatastore();

    public SimpleFeatureType getInlineFeatureType();

    public void setInlineFeatureDatastore(DataStore store);

    public void setInlineFeatureType(SimpleFeatureType ft);

    public void setRemoteOWS(RemoteOWS service);

    public List<FeatureTypeConstraint> layerFeatureConstraints();
    public FeatureTypeConstraint[] getLayerFeatureConstraints();
    public void setLayerFeatureConstraints(FeatureTypeConstraint[] constraints);

    public List<Style> userStyles();  
    public Style[] getUserStyles();
    public void setUserStyles(Style[] styles);

    public void addUserStyle(Style style);

    /**
     * Used to navigate a Style/SLD.
     *
     * @param visitor
     */
    void accept(org.geotools.styling.StyleVisitor visitor);
}
