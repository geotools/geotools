/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import javax.xml.namespace.QName;
import org.geotools.api.style.StyleFactory;
import org.geotools.se.v1_1.SE;
import org.geotools.sld.bindings.SLDChannelSelectionBinding;

/**
 * Binding object for the element http://www.opengis.net/se:ChannelSelection.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="ChannelSelection" type="se:ChannelSelectionType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          "ChannelSelection" specifies the false-color channel selection
 *          for a multi-spectral raster source.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class ChannelSelectionBinding extends SLDChannelSelectionBinding {

    public ChannelSelectionBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SE.ChannelSelection;
    }
}
