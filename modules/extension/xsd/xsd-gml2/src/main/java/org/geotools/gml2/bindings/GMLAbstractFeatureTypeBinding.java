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
package org.geotools.gml2.bindings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.Configuration;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.SchemaIndex;
import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Binding object for the type http://www.opengis.net/gml:AbstractFeatureType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="AbstractFeatureType" abstract="true"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         An abstract feature provides a set of
 *              common properties. A concrete          feature type must
 *              derive from this type and specify additional
 *              properties in an application schema. A feature may
 *              optionally          possess an identifying attribute
 *              (&apos;fid&apos;).       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element ref="gml:description" minOccurs="0"/&gt;
 *          &lt;element ref="gml:name" minOccurs="0"/&gt;
 *          &lt;element ref="gml:boundedBy" minOccurs="0"/&gt;
 *          &lt;!-- additional properties must be specified in an application schema --&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute name="fid" type="ID" use="optional"/&gt;
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
public class GMLAbstractFeatureTypeBinding extends AbstractComplexBinding {
    /** Cache of feature types */
    FeatureTypeCache ftCache;

    /** factory for loading bindings */
    BindingWalkerFactory bwFactory;
    
    /** schema index for looking up types */
    SchemaIndex schemaIndex;

    /** configuration */
    Configuration configuration;

    public GMLAbstractFeatureTypeBinding(FeatureTypeCache ftCache, BindingWalkerFactory bwFactory, SchemaIndex schemaIndex, Configuration configuration) {
        this.ftCache = ftCache;
        this.bwFactory = bwFactory;
        this.schemaIndex = schemaIndex;
        this.configuration = configuration;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.AbstractFeatureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SimpleFeature.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return GML2ParsingUtils.parseFeature(instance, node, value, ftCache, bwFactory);
    }
    
    @Override
    public Element encode(Object object, Document document, Element value)
            throws Exception {
        return GML2EncodingUtils.AbstractFeatureType_encode(object, document, value);
    }
    
    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        return GML2EncodingUtils.AbstractFeatureType_getProperties(object, element, schemaIndex,
                new HashSet<String>(Arrays.asList("name", "description", "boundedBy")),
                configuration);
    }
}
