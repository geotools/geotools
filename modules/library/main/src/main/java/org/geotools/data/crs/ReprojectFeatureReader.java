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
import java.util.NoSuchElementException;

import org.geotools.data.DataSourceException;
import org.geotools.data.DelegatingFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;


/**
 * ReprojectFeatureReader provides a reprojection for FeatureTypes.
 * 
 * <p>
 * ReprojectFeatureReader  is a wrapper used to reproject  GeometryAttributes
 * to a user supplied CoordinateReferenceSystem from the original
 * CoordinateReferenceSystem supplied by the original FeatureReader.
 * </p>
 * 
 * <p>
 * Example Use:
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
 * </p>
 * TODO: handle the case where there is more than one geometry and the other
 * geometries have a different CS than the default geometry
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author aaime
 * @author $Author: jive $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class ReprojectFeatureReader implements DelegatingFeatureReader<SimpleFeatureType, SimpleFeature>{
    
    FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    SimpleFeatureType schema;
    GeometryCoordinateSequenceTransformer transformer = new GeometryCoordinateSequenceTransformer();
    
    /**
     * Direct constructor reprojecting the provided reader into the schema indicated (using the supplied math transformation).
     * <p>
     * Please note schema is that of the expected results, You may need to use FeatureTypes.transform( FeatureType, crs ) to create the schema provider.
     * 
     * @param reader original reader with results in the original coordinate reference system
     * @param schema This is the target schema describing the results in the expected coordinate reference system
     * @param transform the math transform used to go from reader coordinate reference system to the provided schema coordinate reference system
     */
    public ReprojectFeatureReader(FeatureReader <SimpleFeatureType, SimpleFeature> reader, SimpleFeatureType schema,
        MathTransform transform) {
        this.reader = reader;
        this.schema = schema;
        transformer.setMathTransform((MathTransform2D)transform);
    }
    /**
     * Constructor that will generate schema and mathTransform for the results.
     * 
     * @param reader original reader
     * @param cs Target coordinate reference system; will be used to create the target FeatureType and MathTransform used to transform the data
     */
    public ReprojectFeatureReader(FeatureReader<SimpleFeatureType, SimpleFeature> reader,
        CoordinateReferenceSystem cs)
        throws SchemaException, OperationNotFoundException, NoSuchElementException, FactoryException{
        if (cs == null) {
            throw new NullPointerException("CoordinateSystem required");
        }

        SimpleFeatureType type = reader.getFeatureType();
        CoordinateReferenceSystem original = type.getGeometryDescriptor()
                                                 .getCoordinateReferenceSystem();

        if (cs.equals(original)) {
            throw new IllegalArgumentException("CoordinateSystem " + cs
                + " already used (check before using wrapper)");
        }
        
        this.schema = FeatureTypes.transform(type, cs);
        this.reader = reader;
        transformer.setMathTransform(CRS.findMathTransform(original, cs, true));
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getDelegate() {
        return reader;
    }
    
    /**
     * Implement getFeatureType.
     * 
     * <p>
     * Description ...
     * </p>
     *
     *
     * @throws IllegalStateException DOCUMENT ME!
     *
     * @see org.geotools.data.FeatureReader#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        if (schema == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        return schema;
    }

    /**
     * Implement next.
     * 
     * <p>
     * Description ...
     * </p>
     *
     *
     * @throws IOException
     * @throws IllegalAttributeException
     * @throws NoSuchElementException
     * @throws IllegalStateException DOCUMENT ME!
     * @throws DataSourceException DOCUMENT ME!
     *
     * @see org.geotools.data.FeatureReader#next()
     */
    public SimpleFeature next()
        throws IOException, IllegalAttributeException, NoSuchElementException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        SimpleFeature next = reader.next();
        Object[] attributes = next.getAttributes().toArray();

        try {
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i] instanceof Geometry) {
                    attributes[i] = transformer.transform((Geometry) attributes[i]);
                }
            }
        } catch (TransformException e) {
            throw new DataSourceException("A transformation exception occurred while reprojecting data on the fly",
                e);
        }

        return SimpleFeatureBuilder.build(schema, attributes, next.getID());
    }

    /**
     * Implement hasNext.
     * 
     * <p>
     * Description ...
     * </p>
     *
     *
     * @throws IOException
     * @throws IllegalStateException DOCUMENT ME!
     *
     * @see org.geotools.data.FeatureReader#hasNext()
     */
    public boolean hasNext() throws IOException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        return reader.hasNext();
    }

    /**
     * Implement close.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @throws IOException
     * @throws IllegalStateException DOCUMENT ME!
     *
     * @see org.geotools.data.FeatureReader#close()
     */
    public void close() throws IOException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        reader.close();
        reader = null;
        schema = null;
    }
}
