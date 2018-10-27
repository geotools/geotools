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
package org.geotools.kml.bindings;

import javax.xml.namespace.QName;
import org.geotools.feature.FeatureCollection;
import org.geotools.kml.KML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Binding object for the type http://earth.google.com/kml/2.1:KmlType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType final="#all" name="KmlType"&gt;
 *      &lt;all&gt;
 *          &lt;element minOccurs="0" name="NetworkLinkControl" type="kml:NetworkLinkControlType"/&gt;
 *          &lt;element minOccurs="0" ref="kml:Feature"/&gt;
 *      &lt;/all&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class KmlTypeBinding extends AbstractComplexBinding {
    /** @generated */
    public QName getTarget() {
        return KML.KmlType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return FeatureCollection.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return node.getChildValue(SimpleFeature.class);
    }

    public Object getProperty(Object object, QName name) throws Exception {
        if (KML.Feature.getLocalPart().equals(name.getLocalPart())
                || org.geotools.kml.v22.KML.AbstractFeatureGroup.getLocalPart()
                        .equals(name.getLocalPart())) {
            return object;
        }

        return null;
    }
}
