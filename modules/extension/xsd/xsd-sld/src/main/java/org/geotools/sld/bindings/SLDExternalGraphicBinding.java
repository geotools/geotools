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

import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.net.URI;
import javax.xml.namespace.QName;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.StyleFactory;
import org.geotools.util.Converters;
import org.geotools.xml.*;


/**
 * Binding object for the element http://www.opengis.net/sld:ExternalGraphic.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="ExternalGraphic"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         An &quot;ExternalGraphic&quot; gives
 *              a reference to an external raster or         vector
 *              graphical object.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:OnlineResource"/&gt;
 *              &lt;xsd:element ref="sld:Format"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class SLDExternalGraphicBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDExternalGraphicBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.EXTERNALGRAPHIC;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ExternalGraphic.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //the Converters wrapper here is a workaround for http://jira.codehaus.org/browse/GEOT-2457
        // for some reason on the IBM JDK returns a string, we should really find out why instead 
        // of applying this bandaid
        URI uri = Converters.convert( node.getChildValue("OnlineResource"), URI.class );
        String format = (String) node.getChildValue("Format");

        return styleFactory.createExternalGraphic(uri.toURL(), format);
    }
}
