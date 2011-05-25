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
package org.geotools.gml3.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.bindings.GMLEncodingUtils;
import org.geotools.gml3.GML;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.Configuration;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.SchemaIndex;
import org.opengis.feature.Feature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Binding object for the type http://www.opengis.net/gml:AbstractFeatureType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType abstract="true" name="AbstractFeatureType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;An abstract feature provides a set of common properties, including id, metaDataProperty, name and description inherited from AbstractGMLType, plus boundedBy.    A concrete feature type must derive from this type and specify additional  properties in an application schema. A feature must possess an identifying attribute ('id' - 'fid' has been deprecated).&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGMLType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" ref="gml:boundedBy"/&gt;
 *                  &lt;element minOccurs="0" ref="gml:location"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
 *                          &lt;documentation&gt;deprecated in GML version 3.1&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;!-- additional properties must be specified in an application schema --&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class AbstractFeatureTypeBinding extends AbstractComplexBinding {
    FeatureTypeCache ftCache;
    XSDIdRegistry idSet;
    BindingWalkerFactory bwFactory;
    SchemaIndex schemaIndex;
    Configuration configuration;
    GML3EncodingUtils encodingUtils;
    
    public AbstractFeatureTypeBinding(FeatureTypeCache ftCache, BindingWalkerFactory bwFactory,
            SchemaIndex schemaIndex, Configuration configuration, XSDIdRegistry idRegistry, GML3EncodingUtils encodingUtils ) {
        this.ftCache = ftCache;
        this.bwFactory = bwFactory;
        this.schemaIndex = schemaIndex;
        this.configuration = configuration;
        this.idSet = idRegistry;
        this.encodingUtils = encodingUtils;
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
        return Feature.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return GML3ParsingUtils.parseFeature(instance, node, value, ftCache, bwFactory);

        //        //get the definition of the element
        //        XSDElementDeclaration decl = instance.getElementDeclaration();
        //
        //        //look for a feature type in the cache
        //        FeatureType fType = ftCache.get(decl.getName());
        //
        //        if (fType == null) {
        //            fType = GML3ParsingUtils.featureType(decl, bwFactory);
        //            ftCache.put(fType);
        //        }
        //
        //        //TODO: this could pick up wrong thing, node api needs to be 
        //        // namespace aware
        //        String fid = (String) node.getAttributeValue("id");
        //
        //        return GML3ParsingUtils.feature(fType, fid, node);
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        return encodingUtils.AbstractFeatureTypeEncode(object,document,value,idSet);
    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        return encodingUtils.AbstractFeatureTypeGetProperties(object, element, schemaIndex,
                configuration);
    }
}
