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
package org.geotools.xs.bindings;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xml.impl.DatatypeConverterImpl;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:QName.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="QName" id="QName"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:appinfo&gt;
 *              &lt;hfp:hasFacet name="length"/&gt;
 *              &lt;hfp:hasFacet name="minLength"/&gt;
 *              &lt;hfp:hasFacet name="maxLength"/&gt;
 *              &lt;hfp:hasFacet name="pattern"/&gt;
 *              &lt;hfp:hasFacet name="enumeration"/&gt;
 *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
 *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
 *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
 *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
 *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
 *          &lt;/xs:appinfo&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#QName"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:anySimpleType"&gt;
 *          &lt;xs:whiteSpace value="collapse" fixed="true" id="QName.whiteSpace"/&gt;
 *      &lt;/xs:restriction&gt;
 *  &lt;/xs:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class XSQNameBinding implements SimpleBinding {
    protected NamespaceContext namespaceContext;

    public XSQNameBinding(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return XS.QNAME;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding returns objects of type {@link QName}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return QName.class;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding returns objects of type {@link QName}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        
        //if value passed in was null just return "null" qname
        if (value == null) {
            return new QName(null);
        }
        
        QName qName = null;
        try {
            qName = DatatypeConverterImpl.getInstance().parseQName((String) value, namespaceContext);
        }
        catch( Exception e ) {
            //could occur if a prefix that was not registered was found
        }
        
        //try to set the prefix
        if (qName != null && (qName.getPrefix() == null || qName.getPrefix().equals( "") ) ) {
            if ( qName.getNamespaceURI() != null && !"".equals(qName.getNamespaceURI()) ) {
                String prefix = namespaceContext.getPrefix(qName.getNamespaceURI());
                if ( prefix != null && !"".equals( prefix ) ) {
                    qName = new QName( qName.getNamespaceURI(), qName.getLocalPart(), prefix );
                }
            }
            return qName;
        }

        //could not parse with convert, parse manually
        String s = (String) value;
        int i = s.indexOf(':');

        if (i != -1) {
            String prefix = s.substring(0, i);
            String local = s.substring(i + 1);

            return new QName(null, local, prefix);
        }

        return new QName(null, s);
    }

    public String encode(Object object, String value) throws Exception {
        try {
            return DatatypeConverterImpl.getInstance().printQName(
                    (QName) object, namespaceContext);
        } catch (Exception e) {
            //will happen if a prefix is not in the context, serialize manually
            QName qName = (QName) object;
            if ( qName.getPrefix() == null || qName.getPrefix().equals( "") ) {
                return qName.getLocalPart();
            }
            
            return qName.getPrefix() + ":" + qName.getLocalPart();
            
        }        
    }
}
