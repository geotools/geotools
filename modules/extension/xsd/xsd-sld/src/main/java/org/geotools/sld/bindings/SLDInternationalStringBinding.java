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
package org.geotools.sld.bindings;

import java.util.Map;
import org.geotools.styling.StyleFactory;
import org.geotools.util.GrowableInternationalString;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.util.InternationalString;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:InternationalStringType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="InternationalStringType" mixed="true"&gt;
 *    &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       The "InternationalStringType" contains localized elements for the
 *       container element.  A "mixed" element-content
 *       model is used with localized value elements and default text value.
 *     &lt;/xsd:documentation&gt;
 *    &lt;/xsd:annotation&gt;
 *    &lt;xsd:sequence minOccurs="0" maxOccurs="unbounded"&gt;
 *     &lt;xsd:element ref="sld:Localized"/&gt;
 *    &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public abstract class SLDInternationalStringBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDInternationalStringBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    @Override
    public Class getType() {
        return InternationalString.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Map<String, Object> map = (Map<String, Object>) value;
        GrowableInternationalString intString =
                new GrowableInternationalString(map.get(null).toString()) {

                    @Override
                    public String toString() {
                        return super.toString(null);
                    }
                };
        for (String key : map.keySet()) {
            if (key != null && key.equalsIgnoreCase("localized")) {
                Iterable translations = (Iterable) map.get(key);
                for (Object obj : translations) {
                    Map<String, String> translation = (Map<String, String>) obj;
                    intString.add("", "_" + translation.get("lang"), translation.get(null));
                }
            }
        }
        return intString;
    }
}
