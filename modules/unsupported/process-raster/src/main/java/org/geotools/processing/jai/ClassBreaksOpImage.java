/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.processing.jai;

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.media.jai.PixelAccessor;
import javax.media.jai.ROI;
import javax.media.jai.StatisticsOpImage;
import javax.media.jai.UnpackedImageData;
import org.geotools.process.raster.classify.Classification;

/** Abstract base class for various operations corresponding to classification method. */
public abstract class ClassBreaksOpImage extends StatisticsOpImage {

    /* number of classes */
    protected Integer numClasses;

    /* range of values to calculate per band */
    protected Double[][] extrema;

    /* bands to process */
    protected Integer[] bands;

    /* no data value */
    protected Double noData;

    public ClassBreaksOpImage(
            RenderedImage image,
            Integer numClasses,
            Double[][] extrema,
            ROI roi,
            Integer[] bands,
            Integer xStart,
            Integer yStart,
            Integer xPeriod,
            Integer yPeriod,
            Double noData) {

        super(image, roi, xStart, yStart, xPeriod, yPeriod);

        this.numClasses = numClasses;
        this.extrema = extrema;
        this.bands = bands;
        this.noData = noData;
    }

    @Override
    protected String[] getStatisticsNames() {
        return new String[] {ClassBreaksDescriptor.CLASSIFICATION_PROPERTY};
    }

    @Override
    public Object getProperty(String name) {
        Object obj = properties.getProperty(ClassBreaksDescriptor.CLASSIFICATION_PROPERTY);
        if (obj == java.awt.Image.UndefinedProperty) {
            // not calculated yet, give subclass a chance to optimize in cases where enough
            // parameters are specified that the image does not have to be scanned
            Classification c = preCalculate();
            if (c != null) {
                properties.setProperty(ClassBreaksDescriptor.CLASSIFICATION_PROPERTY, c);
            }
        }

        return super.getProperty(name);
    }

    @Override
    public void setProperty(String name, Object value) {
        if (value instanceof Classification) {
            // calculation over, calculate the breaks
            Classification c = (Classification) value;
            for (int b = 0; b < bands.length; b++) {
                postCalculate(c, b);
            }
        }

        super.setProperty(name, value);
    }

    @Override
    protected Object createStatistics(String name) {
        if (ClassBreaksDescriptor.CLASSIFICATION_PROPERTY.equals(name)) {
            return createClassification();
        }
        return null;
    }

    @Override
    protected void accumulateStatistics(String name, Raster raster, Object obj) {
        if (!ClassBreaksDescriptor.CLASSIFICATION_PROPERTY.equals(name)) {
            return;
        }

        Classification c = (Classification) obj;

        // ClassifiedStats2 stats = (ClassifiedStats2) obj;
        SampleModel sampleModel = raster.getSampleModel();

        Rectangle bounds = raster.getBounds();

        LinkedList rectList;
        if (roi == null) { // ROI is the whole Raster
            rectList = new LinkedList();
            rectList.addLast(bounds);
        } else {
            rectList =
                    roi.getAsRectangleList(
                            bounds.x, bounds.y,
                            bounds.width, bounds.height);
            if (rectList == null) {
                return; // ROI does not intersect with Raster boundary.
            }
        }

        PixelAccessor accessor = new PixelAccessor(sampleModel, null);

        ListIterator iterator = rectList.listIterator(0);

        while (iterator.hasNext()) {
            Rectangle r = (Rectangle) iterator.next();
            int tx = r.x;
            int ty = r.y;

            // Find the actual ROI based on start and period.
            r.x = startPosition(tx, xStart, xPeriod);
            r.y = startPosition(ty, yStart, yPeriod);
            r.width = tx + r.width - r.x;
            r.height = ty + r.height - r.y;

            if (r.width <= 0 || r.height <= 0) {
                continue; // no pixel to count in this rectangle
            }

            switch (accessor.sampleType) {
                case PixelAccessor.TYPE_BIT:
                case DataBuffer.TYPE_BYTE:
                case DataBuffer.TYPE_USHORT:
                case DataBuffer.TYPE_SHORT:
                case DataBuffer.TYPE_INT:
                    // countPixelsInt(accessor, raster, r, xPeriod, yPeriod, breaks);
                    // break;
                case DataBuffer.TYPE_FLOAT:
                case DataBuffer.TYPE_DOUBLE:
                default:
                    calculate(accessor, raster, r, xPeriod, yPeriod, c);
                    break;
            }
        }
    }

    void calculate(
            PixelAccessor accessor,
            Raster raster,
            Rectangle rect,
            int xPeriod,
            int yPeriod,
            Classification c) {
        UnpackedImageData uid = accessor.getPixels(raster, rect, DataBuffer.TYPE_DOUBLE, false);

        double[][] doubleData = uid.getDoubleData();
        int pixelStride = uid.pixelStride * xPeriod;
        int lineStride = uid.lineStride * yPeriod;
        int[] offsets = uid.bandOffsets;

        for (int i = 0; i < bands.length; i++) {
            int b = bands[i];

            double[] data = doubleData[b];
            int lineOffset = offsets[b]; // line offset

            for (int h = 0; h < rect.height; h += yPeriod) {
                int pixelOffset = lineOffset; // pixel offset
                lineOffset += lineStride;

                for (int w = 0; w < rect.width; w += xPeriod) {
                    double d = data[pixelOffset];
                    pixelOffset += pixelStride;

                    // skip no data
                    if (noData != null && noData.equals(d)) {
                        continue;
                    }

                    handleValue(d, c, i);
                }
            }
        }
    }

    protected abstract void handleValue(double d, Classification c, int band);

    protected abstract Classification createClassification();

    protected Classification preCalculate() {
        return null;
    }

    protected abstract void postCalculate(Classification c, int band);

    /** Finds the first pixel at or after <code>pos</code> to be counted. */
    private int startPosition(int pos, int start, int Period) {
        int t = (pos - start) % Period;
        return t == 0 ? pos : pos + (Period - t);
    }
}
