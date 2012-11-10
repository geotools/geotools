/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import org.geotools.data.Base64;
import org.geotools.se.v1_1.SE;
import org.geotools.util.logging.Logging;
import org.geotools.xml.*;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.xml.namespace.QName;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Binding object for the element http://www.opengis.net/se:InlineContent.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="InlineContent" type="se:InlineContentType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          "InlineContent" is XML- or base64-encoded encoded content in some
 *          externally-defined format that is included in an SE in-line.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType mixed="true" name="InlineContentType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:any minOccurs="0"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="encoding" use="required"&gt;
 *          &lt;xsd:simpleType&gt;
 *              &lt;xsd:restriction base="xsd:string"&gt;
 *                  &lt;xsd:enumeration value="xml"/&gt;
 *                  &lt;xsd:enumeration value="base64"/&gt;
 *              &lt;/xsd:restriction&gt;
 *          &lt;/xsd:simpleType&gt;
 *      &lt;/xsd:attribute&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class InlineContentBinding extends AbstractComplexBinding {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.sld");

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.InlineContent;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Icon.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        String encoding = (String) node.getAttributeValue("encoding");
//        if ("xml".equalsIgnoreCase(encoding)) {
//            Document dom = (Document) node.get
//
        if ("base64".equalsIgnoreCase(encoding)) {
            String base64 = value.toString();
            Icon icon = parseIcon(base64);
            return icon;
        } else {
            throw new IllegalArgumentException("Encoding " + encoding + " not supported");
        }
    }

    private static Icon parseIcon(String content) {
        byte[] bytes = Base64.decode(content);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "could not parse graphic inline content: " + content, e);
            }
        }
        if (image == null) {
            LOGGER.warning("returning empty icon");
            return EmptyIcon.INSTANCE;
        }
        return new ImageIcon(image);
    }

    private static class EmptyIcon implements Icon {
        public static final EmptyIcon INSTANCE = new EmptyIcon();
        @Override public void paintIcon(Component c, Graphics g, int x, int y) { }
        @Override public int getIconWidth() { return 1; }
        @Override public int getIconHeight() { return 1; }
    }
}
