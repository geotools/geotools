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

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.geopkg.TileMatrix;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gpkg:gridsettype_grids.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="gridsettype_grids" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *            &lt;xs:sequence&gt;
 *              &lt;xs:element maxOccurs="unbounded" name="grid" type="gridtype"/&gt;
 *            &lt;/xs:sequence&gt;
 *          &lt;/xs:complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class Gridsettype_gridsBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GPKG.gridsettype_grids;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return List.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        List<TileMatrix> matrices = new ArrayList<TileMatrix>();

        for (Object child : node.getChildValues("grid")) {
            matrices.add((TileMatrix) child);
        }

        return matrices;
    }
}
