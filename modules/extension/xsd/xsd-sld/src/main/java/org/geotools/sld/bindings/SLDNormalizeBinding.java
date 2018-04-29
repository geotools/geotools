/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.styling.NormalizeContrastMethodStrategy;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:Normalize.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="Normalize"&gt;
 *      &lt;xsd:complexType/&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 * @source $URL$
 */
public class SLDNormalizeBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;
    FilterFactory filterFactory;

    public SLDNormalizeBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        this.styleFactory = styleFactory;
        this.filterFactory = filterFactory;
    }
    /** @generated */
    public QName getTarget() {
        return SLD.NORMALIZE;
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

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return NormalizeContrastMethodStrategy.class;
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

        NormalizeContrastMethodStrategy ret = new NormalizeContrastMethodStrategy();
        if (node.getChildValue("Algorithm") != null) {
            Expression algor = (Expression) node.getChildValue("Algorithm");
            ret.setAlgorithm(algor);
        }
        List<Node> params = node.getChildren("Parameter");
        for (Node param : params) {
            String key = (String) param.getAttributeValue("name");
            ret.addParameter(key, (Expression) param.getValue());
        }
        return ret;
    }
}
