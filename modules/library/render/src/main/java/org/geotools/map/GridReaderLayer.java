/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.util.logging.Level;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.Style;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Layer used to draw a raster {@link GridCoverage2DReader}.
 *
 * <p>Direct access to the {@link AbstractGridCoverage2DReader} is available using {@link
 * #getReader()}, the outline of the raster is also available via {@link #toFeatureCollection()} for
 * vector based rendering systems.
 *
 * @author Jody Garnett
 * @version 8.0
 * @since 2.7
 */
public class GridReaderLayer extends RasterLayer {
    /** Grid coverage reader allowing direct access to raster content. */
    protected GridCoverage2DReader reader;

    /** Optional parameters to control the rendering process. */
    protected GeneralParameterValue[] params;

    /**
     * Create a lyaer to draw the provided grid coverage reader.
     *
     * @param reader a reader with the new layer that will be added
     */
    public GridReaderLayer(GridCoverage2DReader reader, Style style) {
        this(reader, style, null, null);
    }

    /**
     * Create a layer to draw the provided grid coverage reader.
     *
     * @param reader a reader with the new layer that will be added.
     */
    public GridReaderLayer(GridCoverage2DReader reader, Style style, String title) {
        this(reader, style, title, null);
    }

    /**
     * Create a layer with optional parameters to control the rendering process.
     *
     * @param reader a reader with the new layer that will be added.
     */
    public GridReaderLayer(
            GridCoverage2DReader reader, Style style, GeneralParameterValue[] params) {
        this(reader, style, null, params);
    }

    /**
     * Create layer title and optional parameters used to control the rendering process.
     *
     * @param reader a reader with the new layer that will be added.
     * @param params GeneralParameterValue[] that describe how the {@link
     *     AbstractGridCoverage2DReader} shall read the images
     */
    public GridReaderLayer(
            GridCoverage2DReader reader,
            Style style,
            String title,
            GeneralParameterValue[] params) {
        super(style, title);
        this.reader = reader;
        this.params = params;
    }

    @Override
    public void dispose() {
        preDispose();
        if (reader != null) {
            try {
                reader.dispose();
            } catch (Exception e) {
                // eat me
            }
            reader = null;
        }
        if (style != null) {
            this.style = null;
        }
        if (params != null) {
            this.params = null;
        }
        super.dispose();
    }

    @Override
    public ReferencedEnvelope getBounds() {
        if (reader != null) {
            CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();
            GeneralEnvelope envelope = reader.getOriginalEnvelope();
            if (envelope != null) {
                return new ReferencedEnvelope(envelope);
            } else if (crs != null) {
                return new ReferencedEnvelope(crs);
            }
        }
        return null;
    }

    /** Reader used for efficient access to raster content. */
    public GridCoverage2DReader getReader() {
        return reader;
    }

    /**
     * Parameter values used when reading.
     *
     * @return parameters used when reader
     */
    public GeneralParameterValue[] getParams() {
        return params;
    }

    public SimpleFeatureCollection toFeatureCollection() {
        SimpleFeatureCollection collection;
        try {
            collection = FeatureUtilities.wrapGridCoverageReader(reader, params);
            return collection;
        } catch (Exception e) {
            LOGGER.log(Level.FINER, "Coverage could not be converted to FeatureCollection", e);
            return null;
        }
    }
}
