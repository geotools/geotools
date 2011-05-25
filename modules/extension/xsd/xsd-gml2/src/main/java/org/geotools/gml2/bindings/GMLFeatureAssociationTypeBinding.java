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

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.simple.SimpleFeature;


/**
 * Binding object for the type http://www.opengis.net/gml:FeatureAssociationType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="FeatureAssociationType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         An instance of this type (e.g. a
 *              featureMember) can either          enclose or point to a
 *              feature (or feature collection); this          type can be
 *              restricted in an application schema to allow only
 *              specified features as valid participants in the association.
 *              When serving as a simple link that references a remote
 *              feature          instance, the value of the gml:remoteSchema
 *              attribute can be          used to locate a schema fragment
 *              that constrains the target          instance.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence minOccurs="0"&gt;
 *          &lt;element ref="gml:_Feature"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attributeGroup ref="xlink:simpleLink"/&gt;
 *      &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
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
public class GMLFeatureAssociationTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.FeatureAssociationType;
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
        return node.getChildValue(SimpleFeature.class);

        //TODO: xlink and remoteSchema attributes, hard to do because of streaming
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if( GML._Feature.equals( name ) ) {
            return object;    
        }
        return null;
    }
}
