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
package org.geotools.filter.v1_0;

import java.net.URI;
import javax.xml.namespace.QName;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/ogc:FeatureIdType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="FeatureIdType"&gt;
 *      &lt;xsd:attribute name="fid" type="xsd:anyURI" use="required"/&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class OGCFeatureIdTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;

    public OGCFeatureIdTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return OGC.FeatureIdType;
    }

    /** @generated modifiable */
    @Override
    public Class getType() {
        return FeatureId.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return factory.featureId(node.getAttributeValue("fid").toString());
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("fid".equals(name.getLocalPart())) {
            FeatureId featureId = (FeatureId) object;

            // &lt;xsd:attribute name="fid" type="xsd:anyURI" use="required"/&gt;
            if (featureId != null) {
                return new URI(featureId.getID());
            }
        }

        return null;
    }
}
