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

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.resources.geometry.XRectangle2D;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;

/**
 * Helper class used by {@linkplain InfoTool} to query values in a
 * {@linkplain org.geotools.map.GridReaderLayer}.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
public class GridReaderLayerHelper extends InfoToolHelper {

    private WeakReference<AbstractGridCoverage2DReader> sourceRef;

    public GridReaderLayerHelper() {
    }

    @Override
    public boolean isSupportedLayer(Layer layer) {
        return (layer instanceof GridReaderLayer);
    }

    @Override
    public void setLayer(Layer layer) {
        if (!(layer instanceof GridReaderLayer)) {
            throw new IllegalArgumentException("layer must be an instance of GridReaderLayer");
        }

        super.setLayer(layer);
        sourceRef = new WeakReference<AbstractGridCoverage2DReader>(
                ((GridReaderLayer) layer).getReader());
    }

    @Override
    public InfoToolResult getInfo(DirectPosition2D pos) throws Exception {
        InfoToolResult result = new InfoToolResult();

        if (isValid()) {
            AbstractGridCoverage2DReader reader = sourceRef.get();
            DirectPosition2D trPos = 
                    InfoToolHelperUtils.getTransformed(pos, getContentToLayerTransform());

            MathTransform tr = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER).inverse();
            DirectPosition rasterMid = tr.transform(trPos, null);

            Rectangle2D.Double rasterArea = new Rectangle2D.Double();
            rasterArea.setFrameFromCenter(rasterMid.getOrdinate(0), rasterMid.getOrdinate(1),
                    rasterMid.getOrdinate(0) + 10, rasterMid.getOrdinate(1) + 10);

            final Rectangle integerRasterArea = rasterArea.getBounds();

            Rectangle originalArea = null;
            final GridEnvelope gridEnvelope = reader.getOriginalGridRange();
            if (gridEnvelope instanceof GridEnvelope2D) {
                originalArea = (GridEnvelope2D) gridEnvelope;
            } else {
                new Rectangle();
            }

            XRectangle2D.intersect(integerRasterArea, originalArea, integerRasterArea);
            // paranoiac check, did we fall outside the coverage raster area? This should
            // never really happne if the request is well formed.
            if (integerRasterArea.isEmpty()) {
                return null;
            }

            GeneralParameterValue parameter = new org.geotools.parameter.Parameter(
                    AbstractGridFormat.READ_GRIDGEOMETRY2D,
                    new GridGeometry2D(new GridEnvelope2D(integerRasterArea), reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER), reader.getCrs()));

            final GridCoverage2D coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[]{parameter});
            if (coverage != null) {
                try {
                    Object objArray = coverage.evaluate(trPos);
                    Number[] bandValues = InfoToolHelperUtils.asNumberArray(objArray);

                    if (bandValues != null) {
                        for (int i = 0; i < bandValues.length; i++) {
                            result.setFeatureValue("Band " + i, bandValues[i]);
                        }
                    }

                } catch (PointOutsideCoverageException e) {
                    // Do nothing
                }
            }
        }

        return result;
    }

}
