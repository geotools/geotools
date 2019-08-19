/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing.operation;

import it.geosolutions.jaiext.iterators.RandomIterFactory;
import javax.media.jai.BorderExtender;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.iterator.RandomIter;
import org.geotools.image.ImageWorker;
import org.geotools.util.factory.GeoTools;

/**
 * Helper class disposing the border op image along with the iterator when {@link #done()} is called
 *
 * @author Andrea Aime - GeoSolutions
 */
class ExtendedRandomIter implements RandomIter {

    RandomIter delegate;
    RenderedOp op;

    public static RandomIter getRandomIterator(
            final PlanarImage src,
            int leftPad,
            int rightPad,
            int topPad,
            int bottomPad,
            BorderExtender extender) {
        RandomIter iterSource;
        if (extender != null) {
            ImageWorker w = new ImageWorker(src).setRenderingHints(GeoTools.getDefaultHints());
            RenderedOp op =
                    w.border(leftPad, rightPad, topPad, bottomPad, extender).getRenderedOperation();
            RandomIter it = RandomIterFactory.create(op, op.getBounds(), true, true);
            return new ExtendedRandomIter(it, op);
        } else {
            iterSource = RandomIterFactory.create(src, src.getBounds(), true, true);
        }
        return iterSource;
    }

    ExtendedRandomIter(RandomIter delegate, RenderedOp op) {
        super();
        this.delegate = delegate;
        this.op = op;
    }

    public int getSample(int x, int y, int b) {
        return delegate.getSample(x, y, b);
    }

    public float getSampleFloat(int x, int y, int b) {
        return delegate.getSampleFloat(x, y, b);
    }

    public double getSampleDouble(int x, int y, int b) {
        return delegate.getSampleDouble(x, y, b);
    }

    public int[] getPixel(int x, int y, int[] iArray) {
        return delegate.getPixel(x, y, iArray);
    }

    public float[] getPixel(int x, int y, float[] fArray) {
        return delegate.getPixel(x, y, fArray);
    }

    public double[] getPixel(int x, int y, double[] dArray) {
        return delegate.getPixel(x, y, dArray);
    }

    public void done() {
        delegate.done();
        op.dispose();
    }
}
