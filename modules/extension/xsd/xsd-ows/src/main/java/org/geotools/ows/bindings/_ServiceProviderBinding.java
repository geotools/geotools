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

import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.ServiceProviderType;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ows:_ServiceProvider.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="_ServiceProvider"&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="ProviderName" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;A unique identifier for the service provider organization. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="ProviderSite" type="ows:OnlineResourceType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Reference to the most relevant web site of the service provider. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element name="ServiceContact" type="ows:ResponsiblePartySubsetType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Information for contacting the service provider. The OnlineResource element within this ServiceContact element should not be used to reference a web site of the service provider. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class _ServiceProviderBinding extends AbstractComplexEMFBinding {
    public _ServiceProviderBinding(Ows10Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS._ServiceProvider;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
