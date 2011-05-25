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

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/gml:AbstractFeatureCollectionBaseType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="AbstractFeatureCollectionBaseType" abstract="true"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         This abstract base type just makes the
 *              boundedBy element mandatory          for a feature
 *              collection.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base="gml:AbstractFeatureType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="gml:description" minOccurs="0"/&gt;
 *                  &lt;element ref="gml:name" minOccurs="0"/&gt;
 *                  &lt;element ref="gml:boundedBy"/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute name="fid" type="ID" use="optional"/&gt;
 *          &lt;/restriction&gt;
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
public class GMLAbstractFeatureCollectionBaseTypeBinding extends AbstractComplexBinding {
    FeatureCollections fcFactory;

    public GMLAbstractFeatureCollectionBaseTypeBinding(FeatureCollections fcFactory) {
        this.fcFactory = fcFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.AbstractFeatureCollectionBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return FeatureCollection.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: the geotools feature api doesn't allow for use to supply the 
        // "correct" subclass without hacking, so for now we just create a 
        // default feature collection.
        return fcFactory.newCollection();
    }
}
