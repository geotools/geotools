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
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.data.DataSourceException;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
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
public class ReprojectFeatureIterator implements Iterator {
    FeatureIterator<SimpleFeature> reader;
    SimpleFeatureType schema;
    GeometryCoordinateSequenceTransformer transformer = new GeometryCoordinateSequenceTransformer();

    public ReprojectFeatureIterator(FeatureIterator<SimpleFeature> reader, SimpleFeatureType schema,
        MathTransform transform) {
        this.reader = reader;
        this.schema = schema;
        transformer.setMathTransform((MathTransform2D)transform);
        
        //set hte target coordinate system
        transformer.setCoordinateReferenceSystem(schema.getCoordinateReferenceSystem());
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
    public Object next()
        throws NoSuchElementException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        //grab the next feature
        SimpleFeature next = reader.next();
        
        Object[] attributes = next.getAttributes().toArray();

        try {
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i] instanceof Geometry) {
                    attributes[i] = transformer.transform((Geometry) attributes[i]);
                }
            }
        } catch (TransformException e) {
            throw (IllegalStateException)new IllegalStateException("A transformation exception occurred while reprojecting data on the fly").initCause(e);
        }

        return SimpleFeatureBuilder.build(schema, attributes, next.getID());
    }

    public void remove() {
        throw new UnsupportedOperationException("On the fly reprojection disables remove");
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
    public boolean hasNext(){
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
    public void close() {
        if (reader == null) {
            return;
        }
        reader.close();
        reader = null;
        schema = null;
    }
}
