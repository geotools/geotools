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
package org.geotools.geopkg.wps.xml;

import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.Binding;

/**
 * Binding test case for http://www.opengis.net/gpkg:geopkgtype_features.
 *
 * <p>
 *
 * <pre>
 *   <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="geopkgtype_features" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *            &lt;xs:complexContent&gt;
 *              &lt;xs:extension base="layertype"&gt;
 *                &lt;xs:sequence&gt;
 *                  &lt;xs:element name="featuretype" type="xs:string"/&gt;
 *                  &lt;xs:element minOccurs="0" name="propertynames" type="xs:string"/&gt;
 *                  &lt;xs:element minOccurs="0" name="filter" type="fes:FilterType"/&gt;
 *                &lt;/xs:sequence&gt;
 *              &lt;/xs:extension&gt;
 *            &lt;/xs:complexContent&gt;
 *          &lt;/xs:complexType&gt;
 *
 *    </code>
 *   </pre>
 *
 * @generated
 */
public class Geopkgtype_tilesBindingTest extends GPKGTestSupport {

    public void testType() {
        assertEquals(
                GeoPackageProcessRequest.Layer.class, binding(GPKG.geopkgtype_tiles).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GPKG.geopkgtype_tiles).getExecutionMode());
    }
}
