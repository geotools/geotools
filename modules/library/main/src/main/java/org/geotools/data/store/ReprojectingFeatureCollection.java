/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.FeatureAttributeVisitor;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.spatial.DefaultCRSFilterVisitor;
import org.geotools.filter.spatial.ReprojectingFilterVisitor;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * SimpleFeatureCollection decorator that reprojects all geometries of the features within the feature collection.
 *
 * @author Justin
 */
public class ReprojectingFeatureCollection extends DecoratingSimpleFeatureCollection {
    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    /** The transform to the target coordinate reference system */
    MathTransform transform;

    /** The schema of reprojected features */
    SimpleFeatureType schema;

    /** The target coordinate reference system */
    CoordinateReferenceSystem target;

    /** Transformer used to transform geometries; */
    GeometryCoordinateSequenceTransformer transformer;

    public ReprojectingFeatureCollection(
            FeatureCollection<SimpleFeatureType, SimpleFeature> delegate, CoordinateReferenceSystem target) {
        this(DataUtilities.simple(delegate), target);
    }

    public ReprojectingFeatureCollection(SimpleFeatureCollection delegate, CoordinateReferenceSystem target) {
        this(delegate, delegate.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem(), target);
    }

    public ReprojectingFeatureCollection(
            FeatureCollection<SimpleFeatureType, SimpleFeature> delegate,
            CoordinateReferenceSystem source,
            CoordinateReferenceSystem target) {
        this(DataUtilities.simple(delegate), source, target);
    }

    public ReprojectingFeatureCollection(
            SimpleFeatureCollection delegate, CoordinateReferenceSystem source, CoordinateReferenceSystem target) {
        super(delegate);
        this.target = target;
        SimpleFeatureType schema = delegate.getSchema();
        this.schema = reType(schema, target);

        if (source == null) {
            throw new NullPointerException("source crs");
        }
        if (target == null) {
            throw new NullPointerException("destination crs");
        }

        this.transform = transform(source, target);
        transformer = new GeometryCoordinateSequenceTransformer();
    }

    public void setTransformer(GeometryCoordinateSequenceTransformer transformer) {
        this.transformer = transformer;
    }

    private MathTransform transform(CoordinateReferenceSystem source, CoordinateReferenceSystem target) {
        try {
            return CRS.findMathTransform(source, target, true);
        } catch (FactoryException e) {
            throw new IllegalArgumentException("Could not create math transform", e);
        }
    }

    private SimpleFeatureType reType(SimpleFeatureType type, CoordinateReferenceSystem target) {
        try {
            return FeatureTypes.transform(type, target);
        } catch (SchemaException e) {
            throw new IllegalArgumentException("Could not transform source schema", e);
        }
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        return new DelegateFeatureReader<>(getSchema(), features());
    }

    @Override
    public SimpleFeatureIterator features() {
        try {
            return new ReprojectingFeatureIterator(delegate.features(), transform, schema, transformer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SimpleFeatureType getSchema() {
        return this.schema;
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        // reproject the filter to the delegate native crs
        CoordinateReferenceSystem crs = getSchema().getCoordinateReferenceSystem();
        CoordinateReferenceSystem crsDelegate = delegate.getSchema().getCoordinateReferenceSystem();
        if (crs != null) {
            DefaultCRSFilterVisitor defaulter = new DefaultCRSFilterVisitor(FF, crs);
            filter = (Filter) filter.accept(defaulter, null);
            if (crsDelegate != null && !CRS.equalsIgnoreMetadata(crs, crsDelegate)) {
                ReprojectingFilterVisitor reprojector = new ReprojectingFilterVisitor(FF, delegate.getSchema());
                filter = (Filter) filter.accept(reprojector, null);
            }
        }

        return new ReprojectingFeatureCollection(delegate.subCollection(filter), target);
    }

    @Override
    public SimpleFeatureCollection sort(SortBy order) {
        // return new ReprojectingFeatureList( delegate.sort( order ), target );
        throw new UnsupportedOperationException("Not yet");
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        List<T> list = new ArrayList<>();
        try (SimpleFeatureIterator i = features()) {
            while (i.hasNext()) {
                list.add((T) i.next());
            }
            return list.toArray(a);
        }
    }

    public boolean add(SimpleFeature o) {
        // must back project any geometry attributes
        throw new UnsupportedOperationException("Not yet");
        // return delegate.add( o );
    }

    /**
     * This method computes reprojected bounds the hard way, but computing them feature by feature. This method could be
     * faster if computed the reprojected bounds by reprojecting the original feature bounds a Shape object, thus
     * getting the true shape of the reprojected envelope, and then computing the minimum and maximum coordinates of
     * that new shape. The result would not a true representation of the new bounds.
     *
     * @see org.geotools.data.FeatureResults#getBounds()
     */
    @Override
    public ReferencedEnvelope getBounds() {
        try (SimpleFeatureIterator r = features()) {
            Envelope newBBox = new Envelope();
            Envelope internal;
            SimpleFeature feature;

            while (r.hasNext()) {
                feature = r.next();
                final Geometry geom = (Geometry) feature.getDefaultGeometry();
                if (geom != null) {
                    internal = geom.getEnvelopeInternal();
                    newBBox.expandToInclude(internal);
                }
            }
            return new ReferencedEnvelope(newBBox, target);
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while computing reprojected bounds", e);
        }
    }

    @Override
    protected boolean canDelegate(FeatureVisitor visitor) {
        return isGeometryless(visitor, schema);
    }

    /**
     * Returns true if the visitor is geometryless, that is, it's not accessing a geometry field in the target schema
     */
    public static boolean isGeometryless(FeatureVisitor visitor, SimpleFeatureType schema) {
        if (visitor instanceof FeatureAttributeVisitor) {
            // pass through unless one of the expressions requires the geometry attribute
            FilterAttributeExtractor extractor = new FilterAttributeExtractor(schema);
            for (Expression e : ((FeatureAttributeVisitor) visitor).getExpressions()) {
                e.accept(extractor, null);
            }

            for (PropertyName pname : extractor.getPropertyNameSet()) {
                AttributeDescriptor att = (AttributeDescriptor) pname.evaluate(schema);
                if (att instanceof GeometryDescriptor) {
                    return false;
                }
            }
            return true;
        } else if (visitor instanceof CountVisitor) {
            return true;
        }
        return false;
    }
}
