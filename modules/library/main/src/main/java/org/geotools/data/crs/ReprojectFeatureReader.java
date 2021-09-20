/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.geotools.data.DataSourceException;
import org.geotools.data.DelegatingFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.GeometryUtil;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;

/**
 * ReprojectFeatureReader provides a reprojection for FeatureTypes.
 *
 * <p>ReprojectFeatureReader is a wrapper used to reproject GeometryAttributes to a user supplied
 * CoordinateReferenceSystem from the original CoordinateReferenceSystem supplied by the original
 * FeatureReader.
 *
 * <p>Example Use:
 *
 * <pre><code>
 * ReprojectFeatureReader reader =
 *     new ReprojectFeatureReader( originalReader, reprojectCS );
 *
 * CoordinateReferenceSystem originalCS =
 *     originalReader.getFeatureType().getDefaultGeometry().getCoordinateSystem();
 *
 * CoordinateReferenceSystem newCS =
 *     reader.getFeatureType().getDefaultGeometry().getCoordinateSystem();
 *
 * assertEquals( reprojectCS, newCS );
 * </code></pre>
 *
 * TODO: handle the case where there is more than one geometry and the other geometries have a
 * different CS than the default geometry
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author aaime
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class ReprojectFeatureReader
        implements DelegatingFeatureReader<SimpleFeatureType, SimpleFeature> {

    FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    SimpleFeatureType originalType;
    SimpleFeatureType schema;
    GeometryCoordinateSequenceTransformer transformer;
    Map<Name, GeometryCoordinateSequenceTransformer> transformers;

    /**
     * Direct constructor reprojecting the provided reader into the schema indicated (using the
     * supplied math transformation).
     *
     * <p>Please note schema is that of the expected results, You may need to use
     * FeatureTypes.transform( FeatureType, crs ) to create the schema provider.
     *
     * @param reader original reader with results in the original coordinate reference system
     * @param schema This is the target schema describing the results in the expected coordinate
     *     reference system
     * @param transform the math transform used to go from reader coordinate reference system to the
     *     provided schema coordinate reference system
     */
    public ReprojectFeatureReader(
            FeatureReader<SimpleFeatureType, SimpleFeature> reader,
            SimpleFeatureType schema,
            MathTransform transform) {
        this.reader = reader;
        this.schema = schema;
        this.transformer = new GeometryCoordinateSequenceTransformer();
        transformer.setMathTransform(transform);
    }

    /**
     * Constructor that will generate schema and mathTransform for the results.
     *
     * @param reader original reader
     * @param cs Target coordinate reference system; will be used to create the target FeatureType
     *     and MathTransform used to transform the data
     */
    public ReprojectFeatureReader(
            FeatureReader<SimpleFeatureType, SimpleFeature> reader, CoordinateReferenceSystem cs)
            throws SchemaException, OperationNotFoundException, NoSuchElementException,
                    FactoryException {
        if (cs == null) {
            throw new NullPointerException("CoordinateSystem required");
        }

        this.originalType = reader.getFeatureType();

        if (!FeatureTypes.shouldReproject(originalType, cs)) {
            throw new IllegalArgumentException(
                    "CoordinateSystem " + cs + " already used (check before using wrapper)");
        }

        this.schema = FeatureTypes.transform(originalType, cs);

        this.reader = reader;
        this.transformers = new HashMap<>();
        for (int i = 0; i < originalType.getDescriptors().size(); i++) {
            if (originalType.getDescriptor(i) instanceof GeometryDescriptor) {
                GeometryDescriptor descr = (GeometryDescriptor) originalType.getDescriptor(i);
                CoordinateReferenceSystem original = descr.getCoordinateReferenceSystem();
                if (CRS.isCompatible(cs, original)) {
                    GeometryCoordinateSequenceTransformer transformer =
                            new GeometryCoordinateSequenceTransformer();
                    transformer.setMathTransform(CRS.findMathTransform(original, cs, true));
                    transformers.put(originalType.getDescriptor(i).getName(), transformer);
                }
            }
        }
    }

    protected GeometryCoordinateSequenceTransformer getTransformer(Name attributeName) {
        if (transformer != null) {
            return transformer;
        } else {
            return transformers.get(attributeName);
        }
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getDelegate() {
        return reader;
    }

    /**
     * Implement getFeatureType.
     *
     * <p>Description ...
     *
     * @see org.geotools.data.FeatureReader#getFeatureType()
     */
    @Override
    public SimpleFeatureType getFeatureType() {
        if (schema == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        return schema;
    }

    /**
     * Implement next.
     *
     * <p>Description ...
     *
     * @see org.geotools.data.FeatureReader#next()
     */
    @Override
    public SimpleFeature next()
            throws IOException, IllegalAttributeException, NoSuchElementException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        SimpleFeature next = reader.next();
        Object[] attributes = next.getAttributes().toArray();

        try {
            for (int i = 0; i < schema.getDescriptors().size(); i++) {
                if (schema.getDescriptor(i) instanceof GeometryDescriptor
                        && attributes[i] != null) {
                    GeometryDescriptor descr = (GeometryDescriptor) originalType.getDescriptor(i);
                    GeometryCoordinateSequenceTransformer transformer =
                            getTransformer(descr.getName());
                    if (transformer != null) {
                        attributes[i] = transformer.transform((Geometry) attributes[i]);
                        GeometryUtil.setCRS(
                                (Geometry) attributes[i],
                                ((GeometryDescriptor) schema.getDescriptor(i))
                                        .getCoordinateReferenceSystem());
                    }
                }
            }
        } catch (TransformException e) {
            throw new DataSourceException(
                    "A transformation exception occurred while reprojecting data on the fly", e);
        }
        // building the new reprojected feature
        SimpleFeature reprojected = SimpleFeatureBuilder.build(schema, attributes, next.getID());
        // copying the user data if any
        if (next.hasUserData()) {
            reprojected.getUserData().putAll(next.getUserData());
        }
        return reprojected;
    }

    /**
     * Implement hasNext.
     *
     * <p>Description ...
     *
     * @see org.geotools.data.FeatureReader#hasNext()
     */
    @Override
    public boolean hasNext() throws IOException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        return reader.hasNext();
    }

    /**
     * Implement close.
     *
     * <p>Description ...
     *
     * @see org.geotools.data.FeatureReader#close()
     */
    @Override
    public void close() throws IOException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        reader.close();
        reader = null;
        schema = null;
    }
}
