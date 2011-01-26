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
package org.geotools.wfs.bindings;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.WfsFactory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;


/**
 * Binding object for the type http://www.opengis.net/wfs:FeatureTypeType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="FeatureTypeType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              An element of this type that describes a feature in an application
 *              namespace shall have an xml xmlns specifier, e.g.
 *              xmlns:bo="http://www.BlueOx.org/BlueOx"
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="Name" type="xsd:QName"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    Name of this feature type, including any namespace prefix
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element name="Title" type="xsd:string"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    Title of this feature type, normally used for display
 *                    to a human.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element minOccurs="0" name="Abstract" type="xsd:string"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    Brief narrative description of this feature type, normally
 *                    used for display to a human.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0" ref="ows:Keywords"/&gt;
 *          &lt;xsd:choice&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element name="DefaultSRS" type="xsd:anyURI"&gt;
 *                      &lt;xsd:annotation&gt;
 *                          &lt;xsd:documentation&gt;
 *                          The DefaultSRS element indicated which spatial
 *                          reference system shall be used by a WFS to
 *                          express the state of a spatial feature if not
 *                          otherwise explicitly identified within a query
 *                          or transaction request.  The SRS may be indicated
 *                          using either the EPSG form (EPSG:posc code) or
 *                          the URL form defined in subclause 4.3.2 of
 *                          refernce[2].
 *                       &lt;/xsd:documentation&gt;
 *                      &lt;/xsd:annotation&gt;
 *                  &lt;/xsd:element&gt;
 *                  &lt;xsd:element maxOccurs="unbounded" minOccurs="0"
 *                      name="OtherSRS" type="xsd:anyURI"&gt;
 *                      &lt;xsd:annotation&gt;
 *                          &lt;xsd:documentation&gt;
 *                          The OtherSRS element is used to indicate other
 *                          supported SRSs within query and transaction
 *                          operations.  A supported SRS means that the
 *                          WFS supports the transformation of spatial
 *                          properties between the OtherSRS and the internal
 *                          storage SRS.  The effects of such transformations
 *                          must be considered when determining and declaring
 *                          the guaranteed data accuracy.
 *                       &lt;/xsd:documentation&gt;
 *                      &lt;/xsd:annotation&gt;
 *                  &lt;/xsd:element&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:element name="NoSRS"&gt;
 *                  &lt;xsd:complexType name="FeatureTypeType_NoSRS"/&gt;
 *              &lt;/xsd:element&gt;
 *          &lt;/xsd:choice&gt;
 *          &lt;xsd:element minOccurs="0" name="Operations" type="wfs:OperationsType"/&gt;
 *          &lt;xsd:element minOccurs="0" name="OutputFormats" type="wfs:OutputFormatListType"/&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="1" ref="ows:WGS84BoundingBox"/&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0"
 *              name="MetadataURL" type="wfs:MetadataURLType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class FeatureTypeTypeBinding extends AbstractComplexEMFBinding {
    public FeatureTypeTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.FeatureTypeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return FeatureTypeType.class;
    }
    
    @SuppressWarnings({ "unchecked", "nls" })
    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        if ("OtherSRS".equals(property)) {
            if (value instanceof Collection) {
                Collection<URI> formatListAsUris = (Collection<URI>) value;
                List<String> formatListAsString = new ArrayList<String>();
                for (URI uri : formatListAsUris) {
                    formatListAsString.add(uri.toString());
                }
                value = formatListAsString;
            } else {
                URI uri = (URI) value;
                value = uri.toString();
            }
        }
        super.setProperty(eObject, property, value, lax);
    }
}
