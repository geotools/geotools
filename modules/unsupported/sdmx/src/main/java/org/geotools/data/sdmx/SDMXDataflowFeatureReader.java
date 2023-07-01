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
import it.bancaditalia.oss.sdmx.exceptions.SdmxException;
import it.bancaditalia.oss.sdmx.exceptions.SdmxResponseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

/**
 * Feature reader of SDMX tuples
 *
 * @author lmorandini
 */
public class SDMXDataflowFeatureReader extends SDMXFeatureReader {

    protected class DimensionValue {

        public String name;
        public String value;

        DimensionValue(String s) {
            this.name = s.split(SDMXDataStore.SEPARATOR_DIM)[0];
            this.value = s.split(SDMXDataStore.SEPARATOR_DIM)[1];
        }

        public boolean isMeasure() {
            return this.name.equals(SDMXDataStore.MEASURE_KEY);
        }
    }

    public SDMXDataflowFeatureReader(
            GenericSDMXClient clientIn,
            SimpleFeatureType featureTypeIn,
            Dataflow dataflowIn,
            DataFlowStructure dfStructureIn,
            String sdmxConstraints,
            ArrayList<String> sdmxTimePeriods,
            Logger logger)
            throws IOException, SdmxException {

        super(clientIn, featureTypeIn, dataflowIn, dfStructureIn, logger);

        try {
            this.tsIter =
                    this.client
                            .getTimeSeries(
                                    dataflowIn,
                                    dfStructureIn,
                                    sdmxConstraints,
                                    sdmxTimePeriods.size() > 1 ? sdmxTimePeriods.get(0) : null,
                                    sdmxTimePeriods.size() > 1 ? sdmxTimePeriods.get(1) : null,
                                    false,
                                    null,
                                    false)
                            .iterator();
        } catch (SdmxException e) {
            if (e instanceof SdmxResponseException
                    && ((SdmxResponseException) e).getResponseCode()
                            == SDMXDataStore.ERROR_NORESULTS) {
                this.empty = true;
            } else {
                logger.log(Level.SEVERE, e.getMessage(), e);
                throw new IOException(e);
            }
        }
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

        if (this.empty == true
                || (this.ts == null && this.tsIter.hasNext() == false)
                || (this.ts != null
                        && this.timeIter.hasNext() == false
                        && this.tsIter.hasNext() == false)) {
            return false;
        }

        if (this.ts == null) {
            this.ts = this.tsIter.next();
            this.timeIter = this.ts.getTimeSlots().iterator();
            this.obsIter = this.ts.getObservations().iterator();
        }

        if (this.timeIter.hasNext() == false && this.tsIter.hasNext() == false) {
            return false;
        }

        return true;
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

        if (this.timeIter.hasNext() == false && this.tsIter.hasNext() == true) {
            this.ts = this.tsIter.next();
            this.timeIter = this.ts.getTimeSlots().iterator();
            this.obsIter = this.ts.getObservations().iterator();
        }

        if (this.timeIter.hasNext() == false) {
            return null;
        }

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(this.featureType);
        builder.set(SDMXDataStore.GEOMETRY_ATTR, null);
        builder.set(SDMXDataStore.TIME_KEY, this.timeIter.next());

        this.ts
                .getDimensions()
                .forEach(
                        (dimIn) -> {
                            if (dimIn != null) {
                                DimensionValue dimValue = new DimensionValue(dimIn);
                                builder.set(dimValue.name, dimValue.value);
                            }
                        });

        builder.set(SDMXDataStore.MEASURE_KEY, this.obsIter.next());

        return builder.buildFeature(
                (new FeatureIdImpl(String.valueOf(this.ts.hashCode()))).toString());
    }

    @Override
    public void close() {
        // TODO
    }
}
