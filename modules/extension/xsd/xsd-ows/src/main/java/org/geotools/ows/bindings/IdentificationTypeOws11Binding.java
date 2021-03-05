/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.ows.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows11.Ows11Factory;
import org.geotools.ows.v1_1.OWS;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/ows/1.1:IdentificationType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="IdentificationType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;Extended metadata identifying and describing a set of data. This type shall be extended if needed for each specific OWS to include additional metadata for each type of dataset. If needed, this type should first be restricted for each specific OWS to change the multiplicity (or optionality) of some elements. &lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;complexContent&gt;
 *
 *  			&lt;extension base="ows:BasicIdentificationType"&gt;
 *
 *  				&lt;sequence&gt;
 *
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:BoundingBox"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;Unordered list of zero or more bounding boxes whose union describes the extent of this dataset. &lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/element&gt;
 *
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:OutputFormat"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;Unordered list of zero or more references to data formats supported for server outputs. &lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/element&gt;
 *
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:AvailableCRS"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;Unordered list of zero or more available coordinate reference systems. &lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/element&gt;
 *
 *  				&lt;/sequence&gt;
 *
 *  			&lt;/extension&gt;
 *
 *  		&lt;/complexContent&gt;
 *
 *  	&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class IdentificationTypeOws11Binding extends BasicIdentificationTypeOws11Binding {

    public IdentificationTypeOws11Binding(Ows11Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return OWS.IdentificationType;
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
        return null;
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

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
