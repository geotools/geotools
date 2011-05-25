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

import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.GML;
import org.geotools.wfs.CompositeFeatureCollection;
import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.simple.SimpleFeature;


/**
 * Binding object for the type http://www.opengis.net/wfs:FeatureCollectionType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="FeatureCollectionType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              This type defines a container for the response to a
 *              GetFeature or GetFeatureWithLock request.  If the
 *              request is GetFeatureWithLock, the lockId attribute
 *              must be populated.  The lockId attribute can otherwise
 *              be safely ignored.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="gml:AbstractFeatureCollectionType"&gt;
 *              &lt;xsd:attribute name="lockId" type="xsd:string" use="optional"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                    The value of the lockId attribute is an identifier
 *                    that a Web Feature Service generates when responding
 *                    to a GetFeatureWithLock request.  A client application
 *                    can use this value in subsequent operations (such as a
 *                    Transaction request) to reference the set of locked
 *                    features.
 *                 &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *              &lt;xsd:attribute name="timeStamp" type="xsd:dateTime" use="optional"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                    The timeStamp attribute should contain the date and time
 *                    that the response was generated.
 *                 &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *              &lt;xsd:attribute name="numberOfFeatures"
 *                  type="xsd:nonNegativeInteger" use="optional"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                    The numberOfFeatures attribute should contain a
 *                    count of the number of features in the response.
 *                    That is a count of all features elements dervied
 *                    from gml:AbstractFeatureType.
 *                 &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
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
public class FeatureCollectionTypeBinding extends AbstractComplexEMFBinding {
    public FeatureCollectionTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.FeatureCollectionType;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        FeatureCollectionType fc = (FeatureCollectionType) object;
        if ( !fc.getFeature().isEmpty() ) {
            FeatureCollection first = (FeatureCollection) fc.getFeature().get( 0 );
            
            if( GML.boundedBy.equals( name ) ) {
                if ( fc.getFeature().size() == 1 ) {
                    return first.getBounds();    
                }
                else {
                    //aggregate
                    ReferencedEnvelope bounds = new ReferencedEnvelope(first.getBounds());
                    for ( int i = 1; i < fc.getFeature().size(); i++ ) {
                        FeatureCollection features = (FeatureCollection) fc.getFeature().get( i );
                        bounds.expandToInclude( features.getBounds() );
                    }
                    return bounds;
                }
                
            }
            
            if ( GML.featureMember.equals( name ) ) {
                if (fc.getFeature().size() > 1) {
                    //wrap in a single
                    return new CompositeFeatureCollection(fc.getFeature());
                }

                //just return the single
                return first;
            }    
        }
        
        return super.getProperty(object, name);
    }
    
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        FeatureCollectionType fct = (FeatureCollectionType) super.parse(instance, node, value);
        SimpleFeatureCollection fc = null;
        
        //gml:featureMembers
        fc = (SimpleFeatureCollection) node.getChildValue(FeatureCollection.class);
        if (fc == null) {
            fc = new DefaultFeatureCollection(null, null);
        }
        
        //check for an array
        SimpleFeature[] featureMembers = (SimpleFeature[]) node.getChildValue(SimpleFeature[].class);
        if (featureMembers != null) {
            for (int i = 0; i < featureMembers.length; i++) {
                fc.add(featureMembers[i]);
            }
        }
        else {
            //gml:featureMember
            List<SimpleFeature> featureMember = node.getChildValues( SimpleFeature.class );
            for (SimpleFeature f : featureMember ) {
                fc.add( f );
            }
        }
        
        if ( !fc.isEmpty() ) {
            fct.getFeature().add(fc);
        }

        return fct;
    }
    
    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        if ("featureMembers".equalsIgnoreCase(property)) {
            //ignore feature, handled in parse()
        }
        else {
            super.setProperty(eObject, property, value, lax);
        }
    }
    
}
