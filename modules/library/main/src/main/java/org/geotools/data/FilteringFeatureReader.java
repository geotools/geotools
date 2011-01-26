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
package org.geotools.data;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;


/**
 * Basic support for a  FeatureReader<SimpleFeatureType, SimpleFeature> that does filtering.  I think that
 * filtering should perhaps be done in the AttributeReader.  I'm still having
 * a bit of trouble with the split between attributeReader and featureReader
 * as to where the hooks for advanced processing like filtering should take
 * place.  See my note on hasNext(), as the method is currently broken and
 * there are more optimizations that could take place if we had a
 * FilteringAttributeReader.  So this class may go, but I thought I'd put the
 * ideas into code.
 * 
 * <p>
 * Jody here - changed hasNext() to peek as required.
 * </p>
 *
 * @author Chris Holmes
 * @source $URL$
 * @version $Id$
 */
public class FilteringFeatureReader<T extends FeatureType, F extends Feature> implements DelegatingFeatureReader<T,F> {
    protected final FeatureReader<T, F> featureReader;
    protected final Filter filter;
    protected F next;

    /**
     * Creates a new instance of AbstractFeatureReader
     * 
     * <p>
     * Please don't call this method with Filter.INCLUDE or Filter.EXCLUDE (consider
     * not filtering and EmptyFeatureReader instead)
     * </p>
     *
     * @param featureReader  FeatureReader<SimpleFeatureType, SimpleFeature> being filtered
     * @param filter Filter used to limit the results of featureReader
     */
    public FilteringFeatureReader(FeatureReader<T, F> featureReader, Filter filter) {
        this.featureReader = featureReader;
        this.filter = filter;
        next = null;
    }

    /**
     * @return THe delegate reader.
     */
    public  FeatureReader<T, F> getDelegate() {
    	return featureReader;
    }
    
    public F next()
        throws IOException, IllegalAttributeException, NoSuchElementException {
        F f = null;

        if (hasNext()) {
            // hasNext() ensures that next != null
            f = next;
            next = null;

            return f;
        } else {
            throw new NoSuchElementException("No such Feature exsists");
        }
    }

    public void close() throws IOException {
        featureReader.close();
    }

    public T getFeatureType() {
        return featureReader.getFeatureType();
    }

    /**
     * Query for additional content.
     * 
     * <p>
     * This class will peek ahead to see if there is additional content.
     * </p>
     * 
     * <p>
     * Chris has pointed out that we could make use of AttributeReader based filtering:<br>
     * <i>"Also doing things in the Attribute Reader would allow us to do the
     * smart filtering, only looking at the attributes needed for comparison,
     * whereas doing filtering here means we have to create an entire feature
     * each time."</i>
     * </p>
     *
     * @return <code>true</code> if we have additional content
     *
     * @throws IOException If the reader we are filtering encounters a problem
     * @throws DataSourceException See IOException
     */
    public boolean hasNext() throws IOException {
        if (next != null) {
            return true;
        }
        try {
            F peek;

            while (featureReader.hasNext()) {
                peek = featureReader.next();

                if (filter.evaluate(peek)) {
                    next = peek;
                    return true;
                }                                
            }
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Could not peek ahead", e);
        }
        return next != null;
    }
}
