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

import net.opengis.ows10.GetCapabilitiesType;
import net.opengis.ows10.Ows10Factory;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ows:GetCapabilitiesType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="GetCapabilitiesType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML encoded GetCapabilities operation request. This operation allows clients to retrieve service metadata about a specific service instance. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation. This base type shall be extended by each specific OWS to include the additional required "service" attribute, with the correct value for that OWS. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" name="AcceptVersions" type="ows:AcceptVersionsType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;When omitted, server shall return latest supported version. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="Sections" type="ows:SectionsType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;When omitted or not supported by server, server shall return complete service metadata (Capabilities) document. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="AcceptFormats" type="ows:AcceptFormatsType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;When omitted or not supported by server, server shall return service metadata document using the MIME type "text/xml". &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute name="updateSequence" type="ows:UpdateSequenceType" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;When omitted or not supported by server, server shall return latest complete service metadata document. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
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
public class GetCapabilitiesTypeBinding extends AbstractComplexEMFBinding {
    public GetCapabilitiesTypeBinding(Ows10Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS.GetCapabilitiesType;
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
