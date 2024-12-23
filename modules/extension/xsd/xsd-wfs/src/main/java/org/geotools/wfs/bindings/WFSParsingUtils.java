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
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.SrsSyntax;
import org.geotools.referencing.CRS;
import org.geotools.wfs.CompositeFeatureCollection;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.EMFUtils;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

public class WFSParsingUtils {

    public static EObject FeatureCollectionType_parse(EObject fct, ElementInstance instance, Node node) {

        // gml:featureMembers
        SimpleFeatureCollection fc = (SimpleFeatureCollection) node.getChildValue(FeatureCollection.class);
        if (fc == null) {
            fc = new DefaultFeatureCollection(null, null);
        }

        // check for an array
        SimpleFeature[] featureMembers = node.getChildValue(SimpleFeature[].class);
        if (featureMembers != null) {
            Collection<SimpleFeature> collection = DataUtilities.collectionCast(fc);
            for (SimpleFeature featureMember : featureMembers) {
                collection.add(featureMember);
            }
        } else {
            Collection<SimpleFeature> collection = DataUtilities.collectionCast(fc);
            List<SimpleFeature> featureMember = node.getChildValues(SimpleFeature.class);
            for (SimpleFeature f : featureMember) {
                collection.add(f);
            }
        }

        if (!fc.isEmpty()) {
            if (EMFUtils.has(fct, "feature")) {
                // wfs 1.0, 1.1
                EMFUtils.add(fct, "feature", fc);
            } else {
                // wfs 2.0
                EMFUtils.add(fct, "member", fc);
            }
        }

        return fct;
    }

    public static Object FeatureCollectionType_getProperty(EObject fc, QName name) {
        List<FeatureCollection> features = features(fc);
        FeatureCollection first = features.get(0);

        if ("boundedBy".equals(name.getLocalPart())) {
            ReferencedEnvelope bounds = null;
            if (features.size() == 1) {
                bounds = first.getBounds();
            } else {
                bounds = aggregateEnvelopes(features, first);
            }
            if (bounds == null || bounds.isNull()) {
                // wfs 2.0 does not allow for "gml:Null"
                if (WFS.NAMESPACE.equals(name.getNamespaceURI())) {
                    return null;
                }
            }
            return bounds;
        }

        if ("featureMember".equals(name.getLocalPart()) || "member".equals(name.getLocalPart())) {
            if (features.size() == 1) {
                // just return the single
                return first;
            }

            // different behaviour across versions, in wfs < 2.0 we merge all the features into
            // a single collection, wfs 2.0+ we create a feature collection that contains multiple
            // feature collections
            if ("featureMember".equals(name.getLocalPart())) {
                // wrap in a single
                return new CompositeFeatureCollection(features);
            }

            // we need to recalculate numberMatched, and numberRetunred
            int numberMatched = -1;
            if (EMFUtils.has(fc, "numberMatched")) {
                Number n = (Number) EMFUtils.get(fc, "numberMatched");
                numberMatched = n != null ? n.intValue() : -1;
            } else if (EMFUtils.has(fc, "numberOfFeatures")) {
                Number n = (Number) EMFUtils.get(fc, "numberOfFeatures");
                numberMatched = n != null ? n.intValue() : -1;
            }

            // create new feature collections
            List<FeatureCollectionType> members = new ArrayList<>(features.size());
            for (Iterator<FeatureCollection> it = features.iterator(); it.hasNext(); ) {
                FeatureCollection featureCollection = it.next();

                FeatureCollectionType member = Wfs20Factory.eINSTANCE.createFeatureCollectionType();
                member.setTimeStamp((Calendar) EMFUtils.get(fc, "timeStamp"));
                member.getMember().add(featureCollection);
                members.add(member);

                if (numberMatched == -1) {
                    continue;
                }

                // TODO: calling size() here is bad because it requires a nother trip to the
                // underlying datastore... perhaps try to keep count of the size of each feature
                // collection at a higher level
                int size = featureCollection.size();

                member.setNumberReturned(BigInteger.valueOf(size));

                if (it.hasNext()) {
                    numberMatched -= size;
                    member.setNumberMatched(BigInteger.valueOf(size));
                } else {
                    member.setNumberMatched(BigInteger.valueOf(numberMatched));
                }
            }
            return members;
        }

        return null;
    }

    /**
     * Aggregates multiple envelopes into one, eventually returning a WGS84 one in case the coordinate reference systems
     * of the various envelopes differs
     */
    private static ReferencedEnvelope aggregateEnvelopes(List<FeatureCollection> features, FeatureCollection first) {
        ReferencedEnvelope bounds;
        // aggregate
        List<ReferencedEnvelope> envelopes = new ArrayList<>(features.size());
        ReferencedEnvelope firstBounds = first.getBounds();
        envelopes.add(firstBounds);
        for (int i = 1; i < features.size(); i++) {
            envelopes.add(features.get(i).getBounds());
        }
        boolean consistent = true;
        for (ReferencedEnvelope envelope : envelopes) {
            if (!CRS.equalsIgnoreMetadata(
                    firstBounds.getCoordinateReferenceSystem(), envelope.getCoordinateReferenceSystem())) {
                consistent = false;
                break;
            }
        }

        if (consistent) {
            bounds = ReferencedEnvelope.create(envelopes.get(0));
            for (int i = 1; i < envelopes.size(); i++) {
                bounds.expandToInclude(envelopes.get(i));
            }
        } else {
            try {
                // get WGS84 in lat/lon order
                CoordinateReferenceSystem wgs84 = CRS.decode(SrsSyntax.OGC_URN.getSRS("EPSG:4326"));
                bounds = ReferencedEnvelope.create(envelopes.get(0).transform(wgs84, true));
                for (int i = 1; i < envelopes.size(); i++) {
                    bounds.expandToInclude(envelopes.get(i).transform(wgs84, true));
                }
            } catch (FactoryException | TransformException e) {
                throw new RuntimeException("Failed to aggregate envelopes from multiple CRSs", e);
            }
        }
        return bounds;
    }

    @SuppressWarnings("unchecked")
    public static List<FeatureCollection> features(EObject obj) {
        return (List) (EMFUtils.has(obj, "feature") ? EMFUtils.get(obj, "feature") : EMFUtils.get(obj, "member"));
    }
}
