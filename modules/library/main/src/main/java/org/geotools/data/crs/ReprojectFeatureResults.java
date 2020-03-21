/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.crs;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;

/**
 * ReprojectFeatureReader provides a reprojection for FeatureTypes.
 *
 * <p>ReprojectFeatureResults is a wrapper used to reproject GeometryAttributes to a user supplied
 * CoordinateReferenceSystem from the original CoordinateReferenceSystem supplied by the original
 * FeatureResults.
 *
 * <p>Example Use:
 *
 * <pre><code>
 * ReprojectFeatureResults results =
 *     new ReprojectFeatureResults( originalResults, reprojectCS );
 *
 * CoordinateReferenceSystem originalCS =
 *     originalResults.getFeatureType().getDefaultGeometry().getCoordinateSystem();
 *
 * CoordinateReferenceSystem newCS =
 *     results.getFeatureType().getDefaultGeometry().getCoordinateSystem();
 *
 * assertEquals( reprojectCS, newCS );
 * </code></pre>
 *
 * @author aaime
 * @author $Author: jive $ (last modification)
 * @version $Id$ TODO: handle the case where there is more than one geometry and the other
 *     geometries have a different CS than the default geometry
 */
public class ReprojectFeatureResults extends AbstractFeatureCollection {
    FeatureCollection<SimpleFeatureType, SimpleFeature> results;
    MathTransform transform;

    /** Creates a new reprojecting feature results */
    public ReprojectFeatureResults(
            FeatureCollection<SimpleFeatureType, SimpleFeature> results,
            CoordinateReferenceSystem destinationCS)
            throws IOException, SchemaException, TransformException, OperationNotFoundException,
                    NoSuchElementException, FactoryException {

        super(forceType(origionalType(results), destinationCS));
        this.results = origionalCollection(results);

        CoordinateReferenceSystem originalCs = null;
        if (results instanceof ForceCoordinateSystemFeatureResults)
            originalCs = results.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
        else
            originalCs =
                    this.results.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
        this.transform = CRS.findMathTransform(originalCs, destinationCS, true);
    }

    public Iterator openIterator() {
        return new ReprojectFeatureIterator(results.features(), getSchema(), transform);
    }

    @SuppressWarnings("PMD.CloseResource")
    public void closeIterator(Iterator close) {
        if (close == null) return;
        if (close instanceof ReprojectFeatureIterator) {
            ReprojectFeatureIterator iterator = (ReprojectFeatureIterator) close;
            iterator.close();
        }
    }

    public int size() {
        return results.size();
    }

    private static FeatureCollection<SimpleFeatureType, SimpleFeature> origionalCollection(
            FeatureCollection<SimpleFeatureType, SimpleFeature> results) {
        while (true) {
            if (results instanceof ReprojectFeatureResults) {
                results = ((ReprojectFeatureResults) results).getOrigin();
            }
            if (results instanceof ForceCoordinateSystemFeatureResults) {
                results = ((ForceCoordinateSystemFeatureResults) results).getOrigin();
            }
            break;
        }
        return results;
    }

    private static SimpleFeatureType origionalType(
            FeatureCollection<SimpleFeatureType, SimpleFeature> results) {
        while (true) {
            if (results instanceof ReprojectFeatureResults) {
                results = ((ReprojectFeatureResults) results).getOrigin();
            }
            if (results instanceof ForceCoordinateSystemFeatureResults) {
                results = ((ForceCoordinateSystemFeatureResults) results).getOrigin();
            }
            break;
        }
        return results.getSchema();
    }

    private static SimpleFeatureType forceType(
            SimpleFeatureType startingType, CoordinateReferenceSystem forcedCS)
            throws SchemaException {
        if (forcedCS == null) {
            throw new NullPointerException("CoordinateSystem required");
        }
        CoordinateReferenceSystem originalCs =
                startingType.getGeometryDescriptor().getCoordinateReferenceSystem();

        if (forcedCS.equals(originalCs)) {
            return startingType;
        } else {
            return FeatureTypes.transform(startingType, forcedCS);
        }
    }

    /**
     * This method computes reprojected bounds the hard way, but computing them feature by feature.
     * This method could be faster if computed the reprojected bounds by reprojecting the original
     * feature bounds a Shape object, thus getting the true shape of the reprojected envelope, and
     * then computing the minumum and maximum coordinates of that new shape. The result would not a
     * true representation of the new bounds, but it would be guaranteed to be larger that the true
     * representation.
     *
     * @see org.geotools.data.FeatureResults#getBounds()
     */
    public ReferencedEnvelope getBounds() {
        SimpleFeatureIterator r = features();
        try {
            Envelope newBBox = new Envelope();
            Envelope internal;
            SimpleFeature feature;

            while (r.hasNext()) {
                feature = r.next();
                final Geometry geometry = ((Geometry) feature.getDefaultGeometry());
                if (geometry != null) {
                    internal = geometry.getEnvelopeInternal();
                    newBBox.expandToInclude(internal);
                }
            }
            return ReferencedEnvelope.reference(newBBox);
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while computing reprojected bounds", e);
        } finally {
            r.close();
        }
    }

    /** Returns the feature results wrapped by this reprojecting feature results */
    public FeatureCollection<SimpleFeatureType, SimpleFeature> getOrigin() {
        return results;
    }

    public void accepts(
            org.opengis.feature.FeatureVisitor visitor, org.opengis.util.ProgressListener progress)
            throws IOException {
        if (canDelegate(visitor)) {
            results.accepts(visitor, progress);
        } else {
            super.accepts(visitor, progress);
        }
    }

    protected boolean canDelegate(FeatureVisitor visitor) {
        return ReprojectingFeatureCollection.isGeometryless(visitor, schema);
    }
}
