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
import it.bancaditalia.oss.sdmx.api.GenericSDMXClient;
import it.bancaditalia.oss.sdmx.api.PortableTimeSeries;
import it.bancaditalia.oss.sdmx.exceptions.SdmxException;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature reader of SDMX tuples
 *
 * @author lmorandini
 */
public abstract class SDMXFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    protected SimpleFeatureType featureType;
    protected Logger LOGGER;
    protected GenericSDMXClient client;
    protected Iterator<PortableTimeSeries> tsIter;
    protected PortableTimeSeries ts;
    protected Iterator<String> timeIter;
    protected Iterator<Double> obsIter;
    protected boolean empty;
    protected int featIndex = 0;

    public SDMXFeatureReader(
            GenericSDMXClient clientIn,
            SimpleFeatureType featureTypeIn,
            Dataflow dataflowIn,
            DataFlowStructure dfStructureIn,
            Logger logger)
            throws IOException, SdmxException {

        this.featureType = featureTypeIn;
        this.featIndex = 0;
        this.LOGGER = logger;
        this.client = clientIn;
        this.empty = false;
    }

    /** @see FeatureReader#getFeatureType() */
    @Override
    public SimpleFeatureType getFeatureType() {
        if (this.featureType == null) {
            throw new IllegalStateException(
                    "No features were retrieved, shouldn't be calling getFeatureType()");
        }
        return this.featureType;
    }

    /** @see FeatureReader#hasNext() */
    @Override
    public boolean hasNext() {
        return false;
    }

    /**
     * @throws IOException
     * @see FeatureReader#next()
     */
    @Override
    public SimpleFeature next() throws NoSuchElementException, IOException {
        return null;
    }

    @Override
    public void close() {
        // TODO
    }
}
