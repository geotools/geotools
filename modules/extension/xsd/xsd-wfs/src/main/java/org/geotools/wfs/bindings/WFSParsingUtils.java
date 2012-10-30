/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs20.FeatureCollectionType;
import net.opengis.wfs20.Wfs20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

import org.geotools.wfs.CompositeFeatureCollection;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.EMFUtils;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.simple.SimpleFeature;

public class WFSParsingUtils {

    public static EObject FeatureCollectionType_parse(EObject fct, ElementInstance instance, Node node) {
        
        SimpleFeatureCollection fc = null;
        
        //gml:featureMembers
        fc = (SimpleFeatureCollection) node.getChildValue(FeatureCollection.class);
        if (fc == null) {
            fc = new DefaultFeatureCollection(null, null);
        }
        
        //check for an array
        SimpleFeature[] featureMembers = (SimpleFeature[]) node.getChildValue(SimpleFeature[].class);
        if (featureMembers != null) {
            if( fc instanceof Collection){
                for (int i = 0; i < featureMembers.length; i++) {
                    ((Collection)fc).add(featureMembers[i]);
                }
            }
            else {
                throw new IllegalStateException("Require DefaultFeatureCollection or ListFeatureCollection");
            }
        }
        else {
            //gml:featureMember
            if( fc instanceof Collection){
                List<SimpleFeature> featureMember = node.getChildValues( SimpleFeature.class );
                for (SimpleFeature f : featureMember ) {
                    ((Collection)fc).add( f );
                }
            }
            else {
                throw new IllegalStateException("Require DefaultFeatureCollection or ListFeatureCollection");
            }
        }
        
        if ( !fc.isEmpty() ) {
            if (EMFUtils.has(fct, "feature")) {
                //wfs 1.0, 1.1
                EMFUtils.add(fct, "feature", fc);
            }
            else {
                //wfs 2.0
                EMFUtils.add(fct, "member", fc);
            }
        }
        
        return fct;
    }
    
    public static Object FeatureCollectionType_getProperty(EObject fc, QName name) {
        List<FeatureCollection> features = features(fc);
        FeatureCollection first = features.get( 0 );
        
        if( "boundedBy".equals( name.getLocalPart() ) ) {
            ReferencedEnvelope bounds = null;
            if ( features.size() == 1 ) {
                bounds = first.getBounds();
            }
            else {
                //aggregate
                bounds = new ReferencedEnvelope(first.getBounds());
                for ( int i = 1; i < features.size(); i++ ) {
                    bounds.expandToInclude( features.get( i ).getBounds() );
                }
            }
            if (bounds == null || bounds.isNull()) {
                //wfs 2.0 does not allow for "gml:Null"
                if (WFS.NAMESPACE.equals(name.getNamespaceURI())) {
                    return null;
                }
            }
            return bounds;
        }
        
        if ( "featureMember".equals( name.getLocalPart() ) || "member".equals(name.getLocalPart())) {
            if (features.size() == 1) {
                //just return the single
                return first;
            }

            //different behaviour across versions, in wfs < 2.0 we merge all the features into 
            // a single collection, wfs 2.0+ we create a feature collection that contains multiple
            // feature collections
            if ("featureMember".equals(name.getLocalPart())) {
                //wrap in a single
                return new CompositeFeatureCollection(features);
            }

            
            //we need to recalculate numberMatched, and numberRetunred 
            int numberMatched = -1;
            if (EMFUtils.has(fc, "numberMatched")) {
                Number n = (Number) EMFUtils.get(fc, "numberMatched");
                numberMatched = n != null ? n.intValue() : -1;
            }
            else if (EMFUtils.has(fc, "numberOfFeatures")) {
                Number n = (Number)EMFUtils.get(fc, "numberOfFeatures");
                numberMatched = n != null ? n.intValue() : -1;
            }

            //create new feature collections
            List<FeatureCollectionType> members = new ArrayList(features.size());
            for (Iterator<FeatureCollection> it = features.iterator(); it.hasNext(); ) {
                FeatureCollection featureCollection = it.next();
                
                FeatureCollectionType member = Wfs20Factory.eINSTANCE.createFeatureCollectionType();
                member.setTimeStamp((Calendar) EMFUtils.get(fc, "timeStamp"));
                member.getMember().add(featureCollection);
                members.add(member);

                if (numberMatched == -1) {
                    continue;
                }

                //TODO: calling size() here is bad because it requires a nother trip to the 
                //underlying datastore... perhaps try to keep count of the size of each feature
                // collection at a higher level
                int size = featureCollection.size();
                
                member.setNumberReturned(BigInteger.valueOf(size));

                if (it.hasNext()) {
                    numberMatched -= size;
                    member.setNumberMatched(BigInteger.valueOf(size));
                }
                else {
                    member.setNumberMatched(BigInteger.valueOf(numberMatched));
                }
            }
            return members;
        }
        
        return null;
    }
    
    public static List<FeatureCollection> features(EObject obj) {
        return (List) (EMFUtils.has(obj, "feature") 
            ? EMFUtils.get(obj, "feature") : EMFUtils.get(obj, "member"));
    }
}
