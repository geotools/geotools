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
import net.opengis.wps20.JobControlOptionsTypeMember0;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractSimpleBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:JobControlOptionsType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;simpleType name="JobControlOptionsType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;
 *
 *  				This attribute type is used to specify process control options.
 *
 *  				The WPS specification only defines "execute-sync" and "execute-async",
 *
 *  				each with an associated execution protocol.
 *
 *  				Extensions may specify additional control options, such as "dimiss" which is
 *
 *  				defined in the WPS dismiss extension.
 *
 *  			&lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;union&gt;
 *
 *  			&lt;simpleType&gt;
 *
 *  				&lt;restriction base="string"&gt;
 *
 *  					&lt;enumeration value="sync-execute"/&gt;
 *
 *  					&lt;enumeration value="async-execute"/&gt;
 *
 *  				&lt;/restriction&gt;
 *
 *  			&lt;/simpleType&gt;
 *
 *   			&lt;simpleType&gt;
 *
 *  				&lt;restriction base="string"/&gt;
 *
 *  			&lt;/simpleType&gt;
 *
 *  		&lt;/union&gt;
 *
 *  	&lt;/simpleType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class JobControlOptionsTypeBinding extends AbstractSimpleBinding {

    /** @generated */
    @Override
    public QName getTarget() {
        return WPS.JobControlOptionsType;
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
        return JobControlOptionsTypeMember0.class;
    }
}
