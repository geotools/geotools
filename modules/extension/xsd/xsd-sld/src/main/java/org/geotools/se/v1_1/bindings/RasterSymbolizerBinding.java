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

import org.geotools.se.v1_1.SE;
import org.geotools.sld.bindings.SLDRasterSymbolizerBinding;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:RasterSymbolizer.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="RasterSymbolizer" substitutionGroup="se:Symbolizer" type="se:RasterSymbolizerType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "RasterSymbolizer" is used to specify the rendering of
 *          raster/matrix-coverage data (e.g., satellite images, DEMs).
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * <pre>
 * &lt;xsd:complexType name="RasterSymbolizerType">
 *      &lt;xsd:complexContent>
 *          &lt;xsd:extension base="se:SymbolizerType">
 *              &lt;xsd:sequence>
 *                  &lt;xsd:element ref="se:Geometry" minOccurs="0"/>
 *                  &lt;xsd:element ref="se:Opacity" minOccurs="0"/>
 *                  &lt;xsd:element ref="se:ChannelSelection" minOccurs="0"/>
 *                  &lt;xsd:element ref="se:OverlapBehavior" minOccurs="0"/>
 *                  &lt;xsd:element ref="se:ColorMap" minOccurs="0"/>
 *                  &lt;xsd:element ref="se:ContrastEnhancement" minOccurs="0"/>
 *                  &lt;xsd:element ref="se:ShadedRelief" minOccurs="0"/>
 *                  &lt;xsd:element ref="se:ImageOutline" minOccurs="0"/>
 *               &lt;/xsd:sequence>
 *           &lt;/xsd:extension>
 *       &lt;/xsd:complexContent>
 *   &lt;/xsd:complexType>
 *   </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-sld/src/main/java/org/geotools/se/v1_1/bindings/RasterSymbolizerBinding.java $
 */
public class RasterSymbolizerBinding extends SLDRasterSymbolizerBinding {

    public RasterSymbolizerBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.RasterSymbolizer;
    }
    
    @Override
    public int getExecutionMode() {
        return BEFORE;
    }

}
