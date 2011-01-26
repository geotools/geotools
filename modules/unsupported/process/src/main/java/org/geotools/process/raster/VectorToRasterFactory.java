/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
 *    NOTICE REGARDING STATUS AS PUBLIC DOMAIN WORK AND ABSENCE OF ANY WARRANTIES
 *
 *    The work (source code) was prepared by an officer or employee of the
 *    United States Government as part of that person's official duties, thus
 *    it is a "work of the U.S. Government," which is in the public domain and
 *    not elegible for copyright protection.  See, 17 U.S.C. § 105.  No warranty
 *    of any kind is given regarding the work.
 */

package org.geotools.process.raster;

import java.awt.Dimension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.Parameter;
import org.geotools.process.feature.AbstractFeatureCollectionProcessFactory;
import org.geotools.text.Text;
import org.opengis.geometry.Envelope;
import org.opengis.util.InternationalString;

/**
 * Factory for a Process the rasterizes vector features in an input FeatureCollection.
 *
 * @author Steve Ansari, NOAA
 * @author Michael Bedward
 *
 * @source $URL$
 */
public class VectorToRasterFactory extends AbstractFeatureCollectionProcessFactory {

    private static final String VERSION = "1.0.0";

    /**
     * Output grid dimensions expressed (in cells).
     */
    static final Parameter<Dimension> GRID_DIM = new Parameter<Dimension>(
            "dim",
            Dimension.class,
            Text.text("Dim"),
            Text.text("Dimensions of the output grid in cells"),
            true, // this parameter is mandatory
            1, 1, null, null);

    /**
     * Title for the output grid. Optional - if not provided the grid's
     * title will be derived from the feature type.
     */
    static final Parameter<String> TITLE = new Parameter<String>(
            "title",
            String.class,
            Text.text("Title"),
            Text.text("An optional title for the output grid"),
            false, // this parameter is optional
            1, 1, null, null);

    /**
     * The source of values for cells in the output grid coverage. 
     * This is either the name ({@code String}) of a numeric feature property
     * or an {@code org.opengis.filter.expression.Expression} that can be
     * evaluated to a numeric value.
     * <p>
     * This parameter is mandatory.
     */
    static final Parameter<Object> ATTRIBUTE = new Parameter<Object>(
            "attribute",
            Object.class,
            Text.text("Attribute"),
            Text.text("The feature attribute to use for raster cell values"),
            true, // this parameter is mandatory
            1, 1, null, null);

    /**
     * The bounds, in world coordinates, of the area to be rasterized.
     * Optional: if not provided the bounds of the input FeatureCollection
     * will be used.
     */
    static final Parameter<Envelope> BOUNDS = new Parameter<Envelope>(
            "bounds",
            Envelope.class,
            Text.text("Bounds"),
            Text.text("Bounds of the area to rasterize"),
            false, // this parameter is optional
            1, 1, null, null);

    /**
     * The result of the operation is a FeatureCollection.
     * This can be the input FeatureCollection, modified by the process
     * or a new FeatureCollection.
     */
    static final Parameter<GridCoverage2D> RESULT = new Parameter<GridCoverage2D>(
            "result", GridCoverage2D.class, Text.text("Result"), Text
                    .text("Rasterized features"));

    static final Map<String,Parameter<?>> resultInfo = new HashMap<String, Parameter<?>>();
    static {
        resultInfo.put( ATTRIBUTE.key, ATTRIBUTE );
        resultInfo.put( GRID_DIM.key, GRID_DIM );
        resultInfo.put( RESULT.key, RESULT );
    }

    @Override
    protected void addParameters(Map<String, Parameter<?>> parameters) {
        parameters.put(BOUNDS.key, BOUNDS);
    }

    public InternationalString getTitle() {
        return Text.text("Rasterize features");
    }

    public InternationalString getDescription() {
        return Text.text("Rasterize all or selected features in a FeatureCollection");
    }

    public VectorToRasterProcess create() {
        return new VectorToRasterProcess(this);
    }

    public Map<String, Parameter<?>> getResultInfo(Map<String, Object> parameters) throws IllegalArgumentException {
        return Collections.unmodifiableMap(resultInfo);
    }

    public boolean supportsProgress() {
        return true;
    }

    public String getVersion() {
        return VERSION;
    }

}
