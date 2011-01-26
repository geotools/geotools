/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.metadata;

import java.util.Arrays;
import org.geotools.resources.XArray;
import org.opengis.geometry.Envelope;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.metadata.spatial.PixelOrientation;

import org.geotools.util.NumberRange;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A combinaison of {@code <Envelope>} and {@code <RectifiedGrid>} elements in
 * {@linkplain GeographicMetadataFormat geographic metadata format}. This class offers similar
 * service than {@linkplain Envelope envelope} and {@link GridEnvelope grid range}, except
 * that the maximum value for {@linkplain #getOrdinateRange coordinate range} and
 * {@linkplain #getGridRange grid range} are inclusives.
 * <p>
 * The {@code <GridEnvelope>} child element is typically (but not always) initialized
 * to the following ranges:
 * <ul>
 *   <li>[0 .. {@linkplain java.awt.image.RenderedImage#getWidth image width} - 1]</li>
 *   <li>[0 .. {@linkplain java.awt.image.RenderedImage#getHeight image height} - 1]</li>
 * </ul>
 * </p>
 * However <var>n</var>-dimensional grid coverages may contains additional entries.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Cédric Briançon
 */
public class ImageGeometry extends MetadataAccessor {
    /**
     * The {@code "boundedBy/lowerCorner"} node.
     */
    private MetadataAccessor lowerCorner;

    /**
     * The {@code "boundedBy/upperCorner"} node.
     */
    private MetadataAccessor upperCorner;

    /**
     * The {@code "rectifiedGridDomain/cells"} node.
     */
    private MetadataAccessor cells;

    /**
     * The {@code "rectifiedGridDomain/limits/low"} node.
     */
    private MetadataAccessor low;

    /**
     * The {@code "rectifiedGridDomain/limits/high"} node.
     */
    private MetadataAccessor high;

    /**
     * The {@code "rectifiedGridDomain/localizationGrid"} node.
     */
    private MetadataAccessor localizationGrid;

    /**
     * The {@code "rectifiedGridDomain/pixelOrientation"} node.
     */
    private MetadataAccessor pixelOrientation;

    /**
     * Creates a parser for a grid geometry. This constructor should not be invoked
     * directly; use {@link GeographicMetadata#getGeometry} instead.
     *
     * @param metadata The metadata node.
     */
    protected ImageGeometry(final GeographicMetadata metadata) {
        super(metadata, "rectifiedGridDomain", null);
    }

    /**
     * Returns the number of dimensions. If the {@link #low} array
     * and the cells don't have the same dimension, then a warning is logged and
     * the smallest dimension is returned.
     * If one of them is empty, the dimension of the oter one is then returned.
     */
    public int getDimension() {
        final int dim1 = (low != null) ? low.getUserObject(int[].class).length : 0;
        final int dim2 = (cells != null) ? cells.childCount() : 0;
        if (dim2 == 0) {
            return dim1;
        }
        if (dim1 == 0) {
            return dim2;
        }
        if (dim1 != dim2) {
            warning("getDimension", ErrorKeys.MISMATCHED_DIMENSION_$2,
                    new int[] {dim1, dim2});
        }
        return Math.min(dim1, dim2);
    }

    /**
     * Returns the range of grid index along the specified dimension. Note that range
     * {@linkplain NumberRange#getMinValue minimum value},
     * {@linkplain NumberRange#getMaxValue maximum value} or both may be null if no
     * {@code "low"} or {@code "high"} attribute were found for the
     * {@code "rectifiedGridDomain/limits"} element.
     *
     * @param dimension The dimension index, from 0 inclusive to {@link #getDimension}
     *                  exclusive.
     */
    public NumberRange<Integer> getGridRange(final int dimension) {
        final int minimum = (low  != null) ? low. getUserObject(int[].class)[dimension] : 0;
        final int maximum = (high != null) ? high.getUserObject(int[].class)[dimension] : 0;
        return NumberRange.create(minimum, true, maximum, true);
    }

    /**
     * Set the grid range along the specified dimension. If the dimension is greater
     * than the current envelope dimension, then this dimension is added.
     *
     * @param dimension The dimension to set. It can eventually be greater than {@link #getDimension}.
     * @param minimum   The minimum value along the specified dimension (inclusive).
     * @param maximum   The maximum value along the specified dimension (<strong>inclusive</strong>).
     */
    public void setGridRange(final int dimension, final int minimum, final int maximum) {
        int[] lows  = getLowAccessor(). getUserObject(int[].class);
        int[] highs = getHighAccessor().getUserObject(int[].class);
        final int length = dimension + 1;
        if (lows == null) {
            lows = new int[length];
        } else {
            final int oldLength = lows.length;
            if (length > oldLength) {
                lows = XArray.resize(lows, length);
            }
        }
        if (highs == null) {
            highs = new int[length];
        } else {
            final int oldLength = highs.length;
            if (length > oldLength) {
                highs = XArray.resize(highs, length);
            }
        }
        lows[dimension]  = minimum;
        highs[dimension] = maximum;
        getLowAccessor(). setUserObject(lows);
        getHighAccessor().setUserObject(highs);
    }

    /**
     * Returns the range of ordinate values along the specified dimension. Note that range
     * {@linkplain NumberRange#getMinValue minimum value},
     * {@linkplain NumberRange#getMaxValue maximum value} or both may be null if no
     * {@code "lowerCorner"} or {@code "upperCorner"} attribute were found for the
     * {@code "boundedBy/Envelope"} element.
     *
     * @param dimension The dimension index, from 0 inclusive to {@link #getDimension} exclusive.
     */
    public NumberRange<Double> getOrdinateRange(final int dimension) {
        final double lower = (lowerCorner != null) ?
            lowerCorner.getUserObject(double[].class)[dimension] : Double.NaN;
        final double upper = (upperCorner != null) ?
            upperCorner.getUserObject(double[].class)[dimension] : Double.NaN;
        return new NumberRange(Double.class, lower, true, upper, true);
    }

    /**
     * Set the envelope range along the specified dimension. If the dimension is greater
     * than the current envelope dimension, then this dimension is added.
     *
     * @param dimension The dimension to set. It can eventually be greater than {@link #getDimension}.
     * @param minimum   The minimum value along the specified dimension (inclusive).
     * @param maximum   The maximum value along the specified dimension (<strong>inclusive</strong>).
     */
    public void setOrdinateRange(final int dimension, final double minimum, final double maximum) {
        double[] lowers = getLowerCornerAccessor().getUserObject(double[].class);
        double[] uppers = getUpperCornerAccessor().getUserObject(double[].class);
        final int length = dimension + 1;
        if (lowers == null) {
            lowers = new double[length];
            Arrays.fill(lowers, Double.NaN);
        } else {
            final int oldLength = lowers.length;
            if (length > oldLength) {
                lowers = XArray.resize(lowers, length);
                Arrays.fill(lowers, oldLength, length, Double.NaN);
            }
        }
        if (uppers == null) {
            uppers = new double[length];
            Arrays.fill(uppers, Double.NaN);
        } else {
            final int oldLength = uppers.length;
            if (length > oldLength) {
                uppers = XArray.resize(uppers, length);
                Arrays.fill(uppers, oldLength, length, Double.NaN);
            }
        }
        lowers[dimension]  = minimum;
        uppers[dimension]  = maximum;
        getLowerCornerAccessor().setUserObject(lowers);
        getUpperCornerAccessor().setUserObject(uppers);
    }

    /**
     * Returns the ordinate values along the specified dimension, or {@code null} if none.
     * This method returns a non-null values only if an array of was explicitly specified,
     * for example by a call to {@link #setOrdinates}.
     *
     * @param dimension The dimension index, from 0 inclusive to {@link #getDimension} exclusive.
     */
    public double[] getOrdinates(final int dimension) {
        if (localizationGrid == null) {
            return new double[0];
        }
        localizationGrid.selectChild(dimension);
        return (double[]) getLocalizationGridAccessor().getUserObject();
    }

    /**
     * Set the ordinate values along the specified dimension. The minimum and
     * maximum coordinates will be determined from the specified array.
     *
     * @param dimension The dimension to set, from 0 inclusive to {@link #getDimension} exclusive.
     * @param values The coordinate values.
     */
    public void setOrdinates(final int dimension, final double[] values) {
        double minimum = Double.POSITIVE_INFINITY;
        double maximum = Double.NEGATIVE_INFINITY;
        if (values != null) {
            for (int i=0; i<values.length; i++) {
                final double value = values[i];
                if (value < minimum) minimum = value;
                if (value > maximum) maximum = value;
            }
        }
        setOrdinateRange(dimension, minimum, maximum);
        getLocalizationGridAccessor().selectChild(dimension);
        getLocalizationGridAccessor().setUserObject(values);
    }

    /**
     * Adds ordinate values for an envelope along a dimension. Invoking this method
     * will increase the envelope {@linkplain #getDimension dimension} by one. This method
     * may be invoked in replacement of {@link #addOffsetVector} when every cell
     * coordinates need to be specified explicitly.
     *
     * @param minIndex The minimal index value, inclusive. This is usually 0.
     * @param values The coordinate values.
     *
     * @see #addOffsetVector
     */
    public void addOrdinates(final int minIndex, final double[] values) {
        int[] lows  = getLowAccessor(). getUserObject(int[].class);
        int[] highs = getHighAccessor().getUserObject(int[].class);
        if (lows != null && highs != null) {
            final int last = Math.max(lows.length, highs.length);
            if (last != lows.length || last != highs.length) {
                warning("addOrdinates", ErrorKeys.MISMATCHED_DIMENSION_$2,
                        new int[]{lows.length, highs.length});
            }
            lows = XArray.resize(lows, last + 1);
            highs = XArray.resize(highs, last + 1);
            lows[last] = minIndex;
            highs[last] = minIndex + values.length - 1;
        } else {
            if (lows == null) {
                lows = new int[1];
                lows[0] = minIndex;
            }
            if (highs == null) {
                highs = new int[1];
                highs[0] = minIndex + values.length - 1;
            }
        }
        getLowAccessor(). setUserObject(lows);
        getHighAccessor().setUserObject(highs);
        setOrdinates(getLocalizationGridAccessor().appendChild(), values);
    }

    /**
     * Returns the offset vector for the specified dimension.
     *
     * @param dimension The dimension index, from 0 inclusive to {@link #getDimension} exclusive.
     */
    public double[] getOffsetVector(final int dimension) {
        if (cells == null) {
            return new double[0];
        }
        cells.selectChild(dimension);
        return (double[]) getCellsAccessor().getUserObject();
    }

    /**
     * Set the offset vector for the specified dimension.
     *
     * @param dimension The dimension to set, from 0 inclusive to {@link #getDimension} exclusive.
     * @param values    The offset values.
     */
    public void setOffsetVector(final int dimension, final double[] values) {
        final MetadataAccessor cells = getCellsAccessor();
        cells.selectChild(dimension);
        cells.setUserObject(values);
    }

    /**
     * Adds offset vector values for a new dimension.
     *
     * @param values The offset values for this new dimension.
     */
    public void addOffsetVector(final double[] values) {
        setOffsetVector(getCellsAccessor().appendChild(), values);
    }

    /**
     * Returns the point in a pixel corresponding to the Earth location of the pixel,
     * or {@code null} if not defined. In the JAI framework, this is typically the
     * {@linkplain PixelOrientation#UPPER_LEFT upper left} corner.
     * In some OGC specifications, this is often the pixel
     * {@linkplain PixelOrientation#CENTER center}.
     *
     * @param pixelOrientation The pixel orientation (usually {@code "center"},
     *        {@code "lower left"}, {@code "lower right"}, {@code "upper right"}
     *        or {@code "upper left"}), or {@code null} if unknown.
     *
     * @see PixelOrientation
     */
    public String getPixelOrientation() {
        return (pixelOrientation != null) ?
            pixelOrientation.getUserObject(String.class) : null;
    }

    /**
     * Set the pixel orientation to the specified value. The pixel orientation gives
     * the point in a pixel corresponding to the Earth location of the pixel. In the
     * JAI framework, this is typically the
     * {@linkplain PixelOrientation#UPPER_LEFT upper left} corner. In some OGC
     * specifications, this is often the pixel {@linkplain PixelOrientation#CENTER center}.
     *
     * @param pixelOrientation The pixel orientation (usually {@code "center"},
     *        {@code "lower left"}, {@code "lower right"}, {@code "upper right"}
     *        or {@code "upper left"}), or {@code null} if unknown.
     *
     * @see PixelOrientation
     */
    public void setPixelOrientation(final String pixelOrientation) {
        if (GeographicMetadataFormat.PIXEL_ORIENTATIONS.contains(pixelOrientation)) {
            getPixelOrientationAccessor().setUserObject(pixelOrientation);
        } else {
            warning("setPixelOrientation", ErrorKeys.BAD_PARAMETER_$2, pixelOrientation);
        }
    }

    /**
     * Builds a {@linkplain MetadataAccessor cells accessor} if it is
     * not already instanciated, and returns it, or return the current one if defined.
     */
    private MetadataAccessor getCellsAccessor() {
        if (cells == null) {
            cells = new MetadataAccessor(metadata, "rectifiedGridDomain/cells", "offsetVector");
        }
        return cells;
    }

    /**
     * Builds a {@linkplain MetadataAccessor high accessor} if it is not
     * already instanciated, and returns it, or return the current one if defined.
     */
    private MetadataAccessor getHighAccessor() {
        if (high == null) {
            high = new MetadataAccessor(metadata, "rectifiedGridDomain/limits/high", null);
        }
        return high;
    }

    /**
     * Builds a {@linkplain MetadataAccessor localizationGrid accessor} if it is not
     * already instanciated, and returns it, or return the current one if defined.
     */
    private MetadataAccessor getLocalizationGridAccessor() {
        if (localizationGrid == null) {
            localizationGrid = new MetadataAccessor(metadata,
                "rectifiedGridDomain/localizationGrid", "ordinates");
        }
        return localizationGrid;
    }

    /**
     * Builds a {@linkplain MetadataAccessor low accessor} if it is not
     * already instanciated, and returns it, or return the current one if defined.
     */
    private MetadataAccessor getLowAccessor() {
        if (low == null) {
            low = new MetadataAccessor(metadata, "rectifiedGridDomain/limits/low", null);
        }
        return low;
    }

    /**
     * Builds a {@linkplain MetadataAccessor lowerCorner accessor} if it is not
     * already instanciated, and returns it, or return the current one if defined.
     */
    private MetadataAccessor getLowerCornerAccessor() {
        if (lowerCorner == null) {
            lowerCorner = new MetadataAccessor(metadata, "boundedBy/lowerCorner", null);
        }
        return lowerCorner;
    }

    /**
     * Builds a {@linkplain MetadataAccessor pixel orientation accessor} if it is not
     * already instanciated, and returns it, or return the current one if defined.
     */
    private MetadataAccessor getPixelOrientationAccessor() {
        if (pixelOrientation == null) {
            pixelOrientation = new MetadataAccessor(metadata,
                "rectifiedGridDomain/pixelOrientation", null);
        }
        return pixelOrientation;
    }

    /**
     * Builds a {@linkplain MetadataAccessor upperCorner accessor} if it is not
     * already instanciated, and returns it, or return the current one if defined.
     */
    private MetadataAccessor getUpperCornerAccessor() {
        if (upperCorner == null) {
            upperCorner = new MetadataAccessor(metadata, "boundedBy/upperCorner", null);
        }
        return upperCorner;
    }
}
