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

import javax.xml.namespace.QName;

import net.opengis.wfs.IdentifierGenerationOptionType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.WfsFactory;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.gml2.bindings.GML2ParsingUtils;
import org.geotools.gml3.GML;
import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding object for the type http://www.opengis.net/wfs:InsertElementType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="InsertElementType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              An Insert element may contain a feature collection or one
 *              or more feature instances to be inserted into the
 *              repository.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" ref="gml:_Feature"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute default="GenerateNew" name="idgen"
 *          type="wfs:IdentifierGenerationOptionType" use="optional"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The idgen attribute control how a WFS generates identifiers
 *                 from newly created feature instances using the Insert action.
 *                 The default action is to have the WFS generate a new id for
 *                 the features.  This is also backward compatible with WFS 1.0
 *                 where the only action was for the WFS to generate an new id.
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute name="handle" type="xsd:string" use="optional"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The handle attribute allows a client application
 *                 to assign a client-generated request identifier
 *                 to an Insert action.  The handle is included to
 *                 facilitate error reporting.  If an Insert action
 *                 in a Transaction request fails, then a WFS may
 *                 include the handle in an exception report to localize
 *                 the error.  If no handle is included of the offending
 *                 Insert element then a WFS may employee other means of
 *                 localizing the error (e.g. line number).
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute default="text/xml; subtype=gml/3.1.1"
 *          name="inputFormat" type="xsd:string" use="optional"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 This inputFormat attribute is used to indicate
 *                 the format used to encode a feature instance in
 *                 an Insert element.  The default value of
 *                 'text/xml; subtype=gml/3.1.1' is used to indicate
 *                 that feature encoding is GML3.  Another example
 *                 might be 'text/xml; subtype=gml/2.1.2' indicating
 *                 that the feature us encoded in GML2.  A WFS must
 *                 declare in the capabilities document, using a
 *                 Parameter element, which version of GML it supports.
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute name="srsName" type="xsd:anyURI" use="optional"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                ===== PAV 12NOV2004 ====
 *                WHY IS THIS HERE? WOULDN'T WE KNOW THE INCOMING SRS FROM THE
 *                GML GEOMETRY ELEMENTS?   I ASSUME THAT IF THE INCOMING SRS
 *                DOES NOT MATCH ONE OF THE STORAGE SRS(s) THEN THE WFS WOULD
 *                EITHER PROJECT INTO THE STORAGE SRS OR RAISE AN EXCEPTION.
 *             &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class InsertElementTypeBinding extends AbstractComplexEMFBinding {
    WfsFactory wfsfactory;

    public InsertElementTypeBinding(WfsFactory wfsfactory) {
        this.wfsfactory = wfsfactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.InsertElementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return InsertElementType.class;
    }

    public void initializeChildContext(ElementInstance childInstance, Node node, MutablePicoContainer context) {
        //if an srsName is set for this geometry, put it in the context for 
        // children, so they can use it as well
        if ( node.hasAttribute("srsName") ) {
            try {
                CoordinateReferenceSystem crs = GML2ParsingUtils.crs(node);
                if ( crs != null ) {
                    context.registerComponentInstance(CoordinateReferenceSystem.class, crs);
                }
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @SuppressWarnings("unchecked")
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        InsertElementType insertElement = wfsfactory.createInsertElementType();

        //&lt;xsd:choice&gt;
        //   &lt;xsd:element ref="gml:_FeatureCollection"/&gt;
        //   &lt;xsd:sequence&gt;
        //       &lt;xsd:element maxOccurs="unbounded" ref="gml:_Feature"/&gt;
        //   &lt;/xsd:sequence&gt;
        //&lt;/xsd:choice&gt;
        if (node.hasChild(FeatureCollection.class)) {
            SimpleFeatureCollection fc = (SimpleFeatureCollection) node.getChildValue(FeatureCollection.class);
            insertElement.getFeature().addAll(DataUtilities.list(fc));
        } else if (node.hasChild(SimpleFeature.class)) {
            insertElement.getFeature().addAll(node.getChildValues(SimpleFeature.class));
        }

        //&lt;xsd:attribute default="GenerateNew" name="idgen"
        //              type="wfs:IdentifierGenerationOptionType" use="optional"&gt;
        if (node.hasAttribute("idgen")) {
            insertElement.setIdgen((IdentifierGenerationOptionType) node.getAttributeValue("idgen"));
        }

        //&lt;xsd:attribute name="handle" type="xsd:string" use="optional"&gt;
        if (node.hasAttribute("handle")) {
            insertElement.setHandle((String) node.getAttributeValue("handle"));
        }

        //&lt;xsd:attribute default="text/xml; subtype=gml/3.1.1"
        //               name="inputFormat" type="xsd:string" use="optional"&gt;
        if (node.hasAttribute("inputFormat")) {
            insertElement.setInputFormat((String) node.getAttributeValue("inputFormat"));
        }

        //&lt;xsd:attribute name="srsName" type="xsd:anyURI" use="optional"&gt;
        if (node.hasAttribute("srsName")) {
            insertElement.setSrsName((URI) node.getAttributeValue("srsName"));
        }

        return insertElement;
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        InsertElementType insert = (InsertElementType) object;

        if (GML._Feature.equals(name)) {
            return insert.getFeature();
        }

        return super.getProperty(object, name);
    }}
