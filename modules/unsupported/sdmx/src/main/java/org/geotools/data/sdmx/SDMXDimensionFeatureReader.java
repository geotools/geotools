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
 *
 */

package org.geotools.data.sdmx;

import it.bancaditalia.oss.sdmx.api.DataFlowStructure;
import it.bancaditalia.oss.sdmx.api.Dataflow;
import it.bancaditalia.oss.sdmx.api.Dimension;
import it.bancaditalia.oss.sdmx.api.GenericSDMXClient;
import it.bancaditalia.oss.sdmx.exceptions.SdmxException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature reader of SDMX tuples
 *
 * @author lmorandini
 */
public class SDMXDimensionFeatureReader extends SDMXFeatureReader {

    protected Iterator<Entry<String, String>> dimIter;

    public SDMXDimensionFeatureReader(
            GenericSDMXClient clientIn,
            SimpleFeatureType featureTypeIn,
            Dataflow dataflowIn,
            DataFlowStructure dfStructureIn,
            String expression,
            Logger logger)
            throws IOException, SdmxException {

        super(clientIn, featureTypeIn, dataflowIn, dfStructureIn, logger);

        try {
            // If the list of dimensions has to be returned, returns aonly those
            if (SDMXDataStore.DIMENSIONS_EXPR_ALL.equals(expression.toUpperCase())) {
                Iterator<Dimension> iter = dfStructureIn.getDimensions().iterator();
                Map<String, String> dimensions = new HashMap<String, String>();
                iter.forEachRemaining(
                        dim -> {
                            dimensions.put(dim.getId(), dim.getName());
                        });
                this.dimIter = dimensions.entrySet().iterator();
                // If all the codes of the fiven dimension has to be returned
            } else {
                this.dimIter =
                        dfStructureIn
                                .getDimension(expression.toUpperCase())
                                .getCodeList()
                                .getCodes()
                                .entrySet()
                                .iterator();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e);
        }
    }

    /** @see FeatureReader#getFeatureType() */
    @Override
    public SimpleFeatureType getFeatureType() {
        if (this.featureType == null) {
            throw new IllegalStateException(
                    "No dimension codes were retrieved, shouldn't be calling getFeatureType()");
        }
        return this.featureType;
    }

    /** @see FeatureReader#hasNext() */
    @Override
    public boolean hasNext() {
        return dimIter.hasNext();
    }

    /**
     * @throws IOException
     * @see FeatureReader#next()
     */
    @Override
    public SimpleFeature next() throws NoSuchElementException, IOException {

        if (this.hasNext() == false) {
            return null;
        }

        Entry dim = this.dimIter.next();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(this.featureType);
        builder.set(SDMXDataStore.GEOMETRY_ATTR, null);
        builder.set(SDMXDataStore.CODE_KEY, dim.getKey().toString());
        builder.set(SDMXDataStore.DESCRIPTION_KEY, dim.getValue().toString());

        return builder.buildFeature((new FeatureIdImpl(dim.getKey().toString())).toString());
    }

    @Override
    public void close() {
        // TODO
    }
}
