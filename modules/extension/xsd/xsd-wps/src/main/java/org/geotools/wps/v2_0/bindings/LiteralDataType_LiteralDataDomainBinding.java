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
import net.opengis.wps20.LiteralDataDomainType1;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:LiteralDataType_LiteralDataDomain.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="LiteralDataType_LiteralDataDomain" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  							&lt;complexContent&gt;
 *
 *  								&lt;extension base="wps:LiteralDataDomainType"&gt;
 *
 *  									&lt;attribute name="default" type="boolean" use="optional"&gt;
 *
 *  										&lt;annotation&gt;
 *
 *  											&lt;documentation&gt;
 *
 *  												Indicates that this LiteralDataDomain is the default domain.
 *
 *  											&lt;/documentation&gt;
 *
 *  										&lt;/annotation&gt;
 *
 *  									&lt;/attribute&gt;
 *
 *  								&lt;/extension&gt;
 *
 *  							&lt;/complexContent&gt;
 *
 *  						&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class LiteralDataType_LiteralDataDomainBinding extends AbstractComplexEMFBinding {

    public LiteralDataType_LiteralDataDomainBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WPS.LiteralDataType_LiteralDataDomain;
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
        return LiteralDataDomainType1.class;
    }
}
