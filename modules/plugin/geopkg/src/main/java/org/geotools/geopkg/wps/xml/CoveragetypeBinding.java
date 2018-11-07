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

import javax.xml.namespace.QName;
import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gpkg:coveragetype.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="coveragetype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element minOccurs="0" name="minZoom" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxZoom" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="minColumn" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxColumn" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="minRow" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxRow" type="xs:nonNegativeInteger"/&gt;
 *      &lt;/xs:sequence&gt;
 *    &lt;/xs:complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class CoveragetypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GPKG.coveragetype;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GeoPackageProcessRequest.TilesLayer.TilesCoverage.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        GeoPackageProcessRequest.TilesLayer.TilesCoverage coverage =
                new GeoPackageProcessRequest.TilesLayer.TilesCoverage();

        coverage.setMinZoom((Integer) node.getChildValue("minZoom"));
        coverage.setMaxZoom((Integer) node.getChildValue("maxZoom"));
        coverage.setMinRow((Integer) node.getChildValue("minRow"));
        coverage.setMaxRow((Integer) node.getChildValue("maxRow"));
        coverage.setMinColumn((Integer) node.getChildValue("minColumn"));
        coverage.setMaxColumn((Integer) node.getChildValue("maxColumn"));

        return coverage;
    }
}
