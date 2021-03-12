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

package org.geotools.ows.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows20.LanguagesType;
import net.opengis.ows20.Ows20Factory;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.geotools.ows.v2_0.OWS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/ows/2.0:GetCapabilitiesType_AcceptLanguages.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="GetCapabilitiesType_AcceptLanguages" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *            &lt;sequence&gt;
 *
 *              &lt;element maxOccurs="unbounded" ref="ows:Language"/&gt;
 *
 *            &lt;/sequence&gt;
 *
 *          &lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class CapabilitiesBaseType_LanguagesBinding extends AbstractComplexEMFBinding {

    public CapabilitiesBaseType_LanguagesBinding(Ows20Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return OWS.CapabilitiesBaseType_Languages;
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
        return LanguagesType.class;
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

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        LanguagesType languagesType = (LanguagesType) eObject;
        if (!StringUtils.isEmpty(languagesType.getLanguage()) && !lax) {
            value = languagesType.getLanguage() + "," + value;
        }
        super.setProperty(eObject, property, value, lax);
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        LanguagesType complex = (LanguagesType) object;
        if (!StringUtils.isEmpty(complex.getLanguage())) {
            for (String currentLanguage : complex.getLanguage().split(",")) {
                Element elem =
                        document.createElementNS(
                                OWS.Language.getNamespaceURI(), OWS.Language.getLocalPart());
                elem.appendChild(document.createTextNode(currentLanguage));
                value.appendChild(elem);
            }
        }
        return value;
    }
}
