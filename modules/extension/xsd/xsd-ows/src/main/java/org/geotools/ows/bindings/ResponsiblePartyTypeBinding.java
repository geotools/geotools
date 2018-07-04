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
package org.geotools.ows.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import org.geotools.ows.OWS;
import org.geotools.xml.*;

/**
 * Binding object for the type http://www.opengis.net/ows:ResponsiblePartyType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="ResponsiblePartyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Identification of, and means of communication with, person responsible for the server. At least one of IndividualName, OrganisationName, or PositionName shall be included. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" ref="ows:IndividualName"/&gt;
 *          &lt;element minOccurs="0" ref="ows:OrganisationName"/&gt;
 *          &lt;element minOccurs="0" ref="ows:PositionName"/&gt;
 *          &lt;element minOccurs="0" ref="ows:ContactInfo"/&gt;
 *          &lt;element ref="ows:Role"/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 * @source $URL$
 */
public class ResponsiblePartyTypeBinding extends AbstractComplexEMFBinding {
    public ResponsiblePartyTypeBinding(Ows10Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return OWS.ResponsiblePartyType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
