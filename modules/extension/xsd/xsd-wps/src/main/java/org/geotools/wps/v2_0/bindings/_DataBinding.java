/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.DataType;
import net.opengis.wps20.Wps20Factory;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:_Data.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType mixed="true" name="_Data" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  			&lt;annotation&gt;
 *
 *  				&lt;documentation&gt;
 *
 *  					This element is used to embed the data in a WPS request or response.
 *
 *  					The content can be XML data, plain character data, or specially encoded binary data (i.e. base64).
 *
 *  				&lt;/documentation&gt;
 *
 *  			&lt;/annotation&gt;
 *
 *  			&lt;complexContent mixed="true"&gt;
 *
 *  				&lt;extension base="anyType"&gt;
 *
 *  					&lt;attributeGroup ref="wps:dataEncodingAttributes"/&gt;
 *
 *  				&lt;/extension&gt;
 *
 *  			&lt;/complexContent&gt;
 *
 *  		&lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class _DataBinding extends AbstractComplexEMFBinding {

    public _DataBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WPS.Data;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return DataType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        DataType type = (DataType) super.parse(instance, node, value);
        Entry entry = FeatureMapUtil.createRawTextEntry(String.valueOf(value));
        type.getAny().add(entry);
        return type;
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        DataType complex = (DataType) object;
        if (!complex.getMixed().isEmpty()
                && complex.getMixed().get(0) != null
                && complex.getMixed().get(0).getValue() != null) {
            Object v = complex.getMixed().get(0).getValue();
            if (v != null) {
                value.appendChild(document.createTextNode(v.toString()));
            }
        }
        return value;
    }
}
