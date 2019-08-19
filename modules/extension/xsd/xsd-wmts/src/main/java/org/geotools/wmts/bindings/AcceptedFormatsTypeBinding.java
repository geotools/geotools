/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wmts/1.0:AcceptedFormatsType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;simpleType name="AcceptedFormatsType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;Comma separated list of a standard MIME type,
 *  			possibly a parameterized MIME type. &lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;restriction base="string"&gt;
 *  			&lt;pattern value="((application|audio|image|text|video|message|multipart|model)/.+(;\s*.+=.+)*)(,(application|audio|image|text|video|message|multipart|model)/.+(;\s*.+=.+)*)"/&gt;
 *  		&lt;/restriction&gt;
 *  	&lt;/simpleType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class AcceptedFormatsTypeBinding extends AbstractSimpleBinding {

    wmtsv_1Factory factory;

    public AcceptedFormatsTypeBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.AcceptedFormatsType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, value);
    }
}
