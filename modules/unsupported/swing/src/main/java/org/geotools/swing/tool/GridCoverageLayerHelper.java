/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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


import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;


/**
 * Used by {@linkplain InfoTool} to query {@linkplain GridCoverageLayer} objects.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
public class GridCoverageLayerHelper extends InfoToolHelper {

    /**
     * Creates a new helper instance.
     */
    public GridCoverageLayerHelper() {
    }

    @Override
    public void setLayer(Layer layer) {
        if (!(layer instanceof GridCoverageLayer)) {
            throw new IllegalArgumentException("layer must be an instance of GridCoverageLayer");
        }

        super.setLayer(layer);
    }

    @Override
    public boolean isSupportedLayer(Layer layer) {
        return layer instanceof GridCoverageLayer;
    }

    @Override
    public InfoToolResult getInfo(DirectPosition2D pos) throws Exception {
        InfoToolResult result = null;

        if (isValid()) {
            GridCoverage2D source = ((GridCoverageLayer) layerRef.get()).getCoverage();
            ReferencedEnvelope env = new ReferencedEnvelope(source.getEnvelope2D());
            DirectPosition2D trPos =
                    InfoToolHelperUtils.getTransformed(pos, getContentToLayerTransform());

            if (env.contains(trPos)) {
                result = new InfoToolResult();
                Object objArray = source.evaluate(trPos);
                Number[] bandValues = InfoToolHelperUtils.asNumberArray(objArray);

                if (bandValues != null) {
                    for (int i = 0; i < bandValues.length; i++) {
                        result.setFeatureValue("Band " + i, bandValues[i]);
                    }
                }
            }
        }

        return result;
    }

}
