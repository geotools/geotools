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
 * Binding object for the type http://www.opengis.net/wps/2.0:_Format.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="_Format" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  			&lt;annotation&gt;
 *
 *  				&lt;documentation&gt;References the XML schema, format, and encoding of a complex value. &lt;/documentation&gt;
 *
 *  			&lt;/annotation&gt;
 *
 *  			&lt;attribute name="mimeType" type="ows:MimeType" use="optional"&gt;
 *
 *  				&lt;annotation&gt;
 *
 *  					&lt;documentation&gt;
 *
 *  						Media type of the data.
 *
 *  					&lt;/documentation&gt;
 *
 *  				&lt;/annotation&gt;
 *
 *  			&lt;/attribute&gt;
 *
 *  			&lt;attribute name="encoding" type="anyURI" use="optional"&gt;
 *
 *  				&lt;annotation&gt;
 *
 *  					&lt;documentation&gt;
 *
 *  						Encoding procedure or character set of the data (e.g. raw or base64).
 *
 *  					&lt;/documentation&gt;
 *
 *  				&lt;/annotation&gt;
 *
 *  			&lt;/attribute&gt;
 *
 *  			&lt;attribute name="schema" type="anyURI" use="optional"&gt;
 *
 *  				&lt;annotation&gt;
 *
 *  					&lt;documentation&gt;
 *
 *  						Identification of the data schema.
 *
 *  					&lt;/documentation&gt;
 *
 *  				&lt;/annotation&gt;
 *
 *  			&lt;/attribute&gt;
 *
 *  			&lt;attribute name="maximumMegabytes" type="positiveInteger" use="optional"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							The maximum size of the input data, in megabytes.
 *
 *  							If the input exceeds this size, the server may return an error
 *
 *  							instead of processing the inputs.
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  				&lt;/attribute&gt;
 *
 *  			&lt;attribute name="default" type="boolean" use="optional"/&gt;
 *
 *  		&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class _FormatBinding extends AbstractComplexEMFBinding {

    public _FormatBinding(Wps20Factory factory) {
        super(factory);
    }

    @Override
    public QName getTarget() {
        return WPS._Format;
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
