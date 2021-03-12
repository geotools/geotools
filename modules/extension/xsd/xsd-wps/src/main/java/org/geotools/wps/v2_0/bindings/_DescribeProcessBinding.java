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
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:_DescribeProcess.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="_DescribeProcess" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  			&lt;complexContent&gt;
 *
 *  				&lt;extension base="wps:RequestBaseType"&gt;
 *
 *  					&lt;sequence&gt;
 *
 *  						&lt;element maxOccurs="unbounded" ref="ows:Identifier"&gt;
 *
 *  							&lt;annotation&gt;
 *
 *  								&lt;documentation&gt;
 *
 *  									One or more identifiers for which the process description shall be obtained.
 *
 *  									"ALL"" is reserved to retrieve the  descriptions for all available process offerings.
 *
 *  								&lt;/documentation&gt;
 *
 *  							&lt;/annotation&gt;
 *
 *  						&lt;/element&gt;
 *
 *  					&lt;/sequence&gt;
 *
 *  					&lt;attribute ref="xml:lang" use="optional"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								RFC 4646 language code of the human-readable text (e.g. "en-CA") in the process description.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/attribute&gt;
 *
 *  				&lt;/extension&gt;
 *
 *  			&lt;/complexContent&gt;
 *
 *  		&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class _DescribeProcessBinding extends AbstractComplexEMFBinding {

    public _DescribeProcessBinding(Wps20Factory factory) {
        super(factory);
    }

    @Override
    public QName getTarget() {
        return WPS._DescribeProcess;
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
        return super.getType();
    }
}
