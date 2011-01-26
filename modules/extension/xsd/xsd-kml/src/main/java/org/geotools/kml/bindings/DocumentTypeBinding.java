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
package org.geotools.kml.bindings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.kml.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.Binding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:DocumentType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType final="#all" name="DocumentType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:ContainerType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:Feature"/&gt;
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
 * @source $URL$
 */
public class DocumentTypeBinding extends AbstractComplexBinding {
    public static final SimpleFeatureType featureType;
    static {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(FeatureTypeBinding.featureType);
        tb.setName("document");

        //&lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:Feature"/&gt;
        tb.add("Feature", FeatureCollection.class);

        featureType = tb.buildFeatureType();
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return KML.DocumentType;
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

    public int getExecutionMode() {
        return Binding.AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);

        SimpleFeature feature = (SimpleFeature) value;
        b.init(feature);

        //&lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:Feature"/&gt;
        b.set("Feature", node.getChildValues(SimpleFeature.class));

        return b.buildFeature(feature.getID());
    }
    
    public List getProperties(Object object) throws Exception {
        Object[] prop = new Object[2];
        prop[0] = KML.Placemark;
        if ( object instanceof FeatureCollection ) {
            //TODO: this does not close the iterator!!
            prop[1] = ((FeatureCollection)object).iterator();
        }
        else if ( object instanceof Collection ) {
            prop[1] = ((Collection)object).iterator();
        }
        else if ( object instanceof SimpleFeature ) {
            SimpleFeature feature = (SimpleFeature) object;
            prop[1] = feature.getAttribute( "Feature" );
        }
        
        ArrayList l = new ArrayList();
        l.add( prop );
        return l;
    }
}

