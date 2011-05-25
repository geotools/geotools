/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.swing.utils.MapLayerUtils;
import org.opengis.feature.Feature;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.operation.MathTransform;

/**
 * Helper class used by {@code InfoTool} to query {@code MapLayers}
 * with raster feature data ({@code GridCoverage2D} or {@code AbstractGridCoverage2DReader}).
 *
 * @see InfoTool
 * @see VectorLayerHelper
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $URL$
 */
public class GridLayerHelper extends InfoToolHelper<List<Number>> {
    protected final WeakReference<GridCoverage2D> covRef;

    /**
     * Create a new helper to work with the given raster data source.
     *
     * @param context the {@code MapContext} associated with this helper
     * @param rasterSource an instance of either
     *        {@code GridCoverage2D} or {@code AbstractGridCoverage2DReader
     */
    public GridLayerHelper(MapContext context, MapLayer layer) {
        super(context, null);

        Object rasterSource = null;
        try {
            FeatureIterator<? extends Feature> iter = layer.getFeatureSource().getFeatures().features();
            String gridAttrName = MapLayerUtils.getGridAttributeName(layer);
            rasterSource = iter.next().getProperty(gridAttrName).getValue();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to access raster feature data", ex);
        }

        GridCoverage2D cov = null;
        try {
            if (AbstractGridCoverage2DReader.class.isAssignableFrom(rasterSource.getClass())) {
                cov = ((AbstractGridCoverage2DReader) rasterSource).read(null);
            } else {
                cov = (GridCoverage2D) rasterSource;
            }

            this.covRef = new WeakReference<GridCoverage2D>(cov);

        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }

        setCRS(cov.getCoordinateReferenceSystem());
    }

    /**
     * Get band values at the given position
     *
     * @param pos the location to query
     *
     * @param params not used at present
     *
     * @return a {@code List} of band values; will be empty if {@code pos} was
     *         outside the coverage bounds
     *
     * @throws Exception if the grid coverage could not be queried
     */
    @Override
    public List<Number> getInfo(DirectPosition2D pos, Object ...params) 
            throws Exception {
        
        List<Number> list = new ArrayList<Number>();

        if (isValid()) {
            GridCoverage2D cov = covRef.get();
            ReferencedEnvelope env = new ReferencedEnvelope(cov.getEnvelope2D());
            DirectPosition2D trPos = getTransformed(pos);
            if (env.contains(trPos)) {
                Object objArray = cov.evaluate(trPos);
                Number[] bandValues = asNumberArray(objArray);
                if (bandValues != null) {
                    for (Number value : bandValues) {
                        list.add(value);
                    }
                }
            }
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return (getMapContext() != null && covRef != null && covRef.get() != null);
    }


    /**
     * Convert the Object returned by {@linkplain GridCoverage2D#evaluate(DirectPosition)} into
     * an array of {@code Numbers}.
     *
     * @param objArray an Object representing a primitive array
     *
     * @return a new array of Numbers
     */
    private Number[] asNumberArray(Object objArray) {
        Number[] numbers = null;

        if (objArray instanceof byte[]) {
            byte[] values = (byte[]) objArray;
            numbers = new Number[values.length];
            for (int i = 0; i < values.length; i++) {
                numbers[i] = ((int)values[i]) & 0xff;
            }

        } else if (objArray instanceof int[]) {
            int[] values = (int[]) objArray;
            numbers = new Number[values.length];
            for (int i = 0; i < values.length; i++) {
                numbers[i] = values[i];
            }

        } else if (objArray instanceof float[]) {
            float[] values = (float[]) objArray;
            numbers = new Number[values.length];
            for (int i = 0; i < values.length; i++) {
                numbers[i] = values[i];
            }
        } else if (objArray instanceof double[]) {
            double[] values = (double[]) objArray;
            numbers = new Number[values.length];
            for (int i = 0; i < values.length; i++) {
                numbers[i] = values[i];
            }
        }

        return numbers;
    }

    /**
     * Transform the query position into the coordinate reference system of the
     * data (if different to that of the {@code MapContext}).
     *
     * @param pos query position in {@code MapContext} coordinates
     *
     * @return query position in data ({@code MapLayer}) coordinates
     */
    private DirectPosition2D getTransformed(DirectPosition2D pos) {
        if (isTransformRequired()) {
            MathTransform tr = getTransform();
            if (tr == null) {
                throw new IllegalStateException("MathTransform should not be null");
            }

            try {
                return (DirectPosition2D) tr.transform(pos, null);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        return pos;
    }

}
