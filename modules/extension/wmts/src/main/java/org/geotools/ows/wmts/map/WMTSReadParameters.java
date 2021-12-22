/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.map;

import java.awt.Color;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.renderer.lite.gridcoverage2d.GridCoverageRendererUtilities;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** This class is used to centralize the handling of generic raster read params used in WMTS. */
class WMTSReadParameters {

    private GridGeometry2D gridGeom;
    private Color backGround;
    private Interpolation interpolation;

    private GeneralEnvelope originalEnvelope;

    private CoordinateReferenceSystem sourceCRS;

    private static final int DEF_WIDTH = 640;

    WMTSReadParameters(GeneralParameterValue[] parameters, GeneralEnvelope originalEnvelope) {
        if (parameters != null) initFromGeneralParams(parameters);
        this.originalEnvelope = originalEnvelope;
    }

    private void initFromGeneralParams(GeneralParameterValue[] parameters) {
        for (GeneralParameterValue param : parameters) {
            final GeneralParameterDescriptor descriptor = param.getDescriptor();
            if (descriptor.getName().equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                this.gridGeom = (GridGeometry2D) ((ParameterValue) param).getValue();
            } else if (descriptor.getName().equals(AbstractGridFormat.BACKGROUND_COLOR.getName()))
                this.backGround = (Color) ((ParameterValue) param).getValue();
            else if (descriptor.getName().equals(AbstractGridFormat.INTERPOLATION.getName()))
                this.interpolation = (Interpolation) ((ParameterValue) param).getValue();
            else if (descriptor.getName().equals(WMTSMapLayer.SOURCE_CRS.getName()))
                this.sourceCRS = (CoordinateReferenceSystem) ((ParameterValue) param).getValue();
        }
    }

    /** Get the interpolation method to be used. */
    Interpolation getInterpolation() {
        if (interpolation == null) this.interpolation = new InterpolationNearest();
        return interpolation;
    }

    /** Get the background color as an array of double. */
    double[] getBKGArray() {
        Color color;
        if (backGround == null) color = Color.BLACK;
        else color = backGround;
        return GridCoverageRendererUtilities.colorToArray(color);
    }

    /** Get the requestedEnvelope. */
    Envelope getRequestedEnvelope() {
        Envelope result = gridGeom.getEnvelope();
        if (result == null) result = originalEnvelope;
        return result;
    }

    /** Get the image width. */
    int getWidth() {
        int result = DEF_WIDTH;
        if (gridGeom.getEnvelope() != null)
            // the range high value is the highest pixel included in the
            // raster, the actual width is one more than that
            result = gridGeom.getGridRange().getHigh(0) + 1;
        return result;
    }

    /** Get the image height. */
    int getHeight() {
        int height;
        if (gridGeom.getEnvelope() == null) {
            double ySpanRatio = originalEnvelope.getSpan(1) / originalEnvelope.getSpan(0);
            height = (int) Math.round(ySpanRatio * DEF_WIDTH);
        } else {
            // the range high value is the highest pixel included in the
            // raster, the actual height is one more than that
            height = gridGeom.getGridRange().getHigh(1) + 1;
        }
        return height;
    }

    /** Get source CRS */
    CoordinateReferenceSystem getSourceCRS() {
        return sourceCRS;
    }
}
