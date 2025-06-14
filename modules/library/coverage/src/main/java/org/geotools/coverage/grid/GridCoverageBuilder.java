/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import javax.measure.Unit;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.geometry.GeneralBounds;
import org.geotools.image.io.ImageIOExt;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.GeoTools;

/**
 * Helper class for the creation of {@link GridCoverage2D} instances. The only purpose of this
 * builder is to make {@code GridCoverage2D} construction a little bit easier for some common
 * cases. This class provides default values for each property which make it convenient for simple
 * cases and testing purpose, but is not generic. Users wanting more control and flexibility should
 * use {@link GridCoverageFactory} directly.
 * <p>
 * Usage example:
 *
 * <blockquote><pre>
 * GridCoverageBuilder builder = new GridCoverageBuilder();
 * builder.{@linkplain #setCoordinateReferenceSystem(String) setCoordinateReferenceSystem("EPSG:4326");
 * builder.{@linkplain #setEnvelope(double...) setEnvelope}(-60, 40, -50, 50);
 *
 * // Will use sample value in the range 0 inclusive to 20000 exclusive.
 * builder.{@linkplain #setSampleRange(int,int) setSampleRange}(0, 20000);
 *
 * // Defines elevation (m) = sample / 10
 * Variable elevation = builder.{@linkplain #newVariable newVariable}("Elevation", SI.METRE);
 * elevation.{@linkplain GridCoverageBuilder.Variable#setLinearTransform setLinearTransform}(0.1, 0);
 * elevation.addNodataValue("No data", 32767);
 *
 * // Gets the image, draw anything we want in it.
 * builder.{@linkplain #setImageSize(int,int) setImageSize}(500,500);
 * BufferedImage image = builder.{@linkpalin #getBufferedImage getBufferedImage}();
 * Graphics2D gr = image.createGraphics();
 * gr.draw(...);
 * gr.dispose();
 *
 * // Gets the coverage.
 * GridCoverage2D coverage = builder.{@linkplain #getGridCoverage2D getGridCoverage2D}();
 * </pre></blockquote>
 *
 * @since 2.5
 * @author Martin Desruisseaux
 *
 *
 *
 * @version $Id$
 */
public class GridCoverageBuilder {
    /** The envelope, including coordinate reference system. */
    private GeneralBounds envelope;

    /** The range of sample values. */
    private NumberRange<? extends Number> range;

    /** The default {@linkplain #range}. */
    private static final NumberRange<Integer> DEFAULT_RANGE = NumberRange.create(0, true, 256, false);

    /**
     * The list of variables created. Each variable will be mapped to a {@linkplain GridSampleDimension sample
     * dimension}.
     *
     * @see #newVariable
     */
    protected final List<Variable> variables;

    /** The image size. */
    private int width, height;

    /** The image. Will be created only when first needed. */
    private BufferedImage image;

    /** The grid coverage. Will be created only when first needed. */
    private GridCoverage2D coverage;

    /** The factory to use for creating grid coverages. */
    private final GridCoverageFactory factory;

    /** Creates a builder initialized to default values and factory. */
    public GridCoverageBuilder() {
        this(CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints()));
    }

    /** Creates a builder initialized to default values. */
    public GridCoverageBuilder(final GridCoverageFactory factory) {
        this.factory = factory;
        variables = new ArrayList<>();
        width = 256;
        height = 256;
    }

    /** Wraps an arbitrary envelope to an object that can be stored in {@link #envelope}. */
    private static GeneralBounds wrap(final Bounds envelope) {
        return envelope == null || envelope instanceof GeneralBounds
                ? (GeneralBounds) envelope
                : new GeneralBounds(envelope);
    }

    /**
     * Returns the current coordinate reference system. If no CRS has been {@linkplain #setCoordinateReferenceSystem
     * explicitly defined}, then the default CRS is {@linkplain DefaultGeographicCRS#WGS84 WGS84}.
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return envelope != null ? envelope.getCoordinateReferenceSystem() : DefaultGeographicCRS.WGS84;
    }

    /**
     * Sets the coordinate reference system to the specified value. If an {@linkplain #setEnvelope envelope was
     * previously defined}, it will be reprojected to the new CRS.
     *
     * @throws IllegalArgumentException if the CRS is illegal for the {@linkplain #getEnvelope current envelope}.
     */
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs) throws IllegalArgumentException {
        if (envelope == null) {
            if (crs != null) {
                envelope = wrap(CRS.getEnvelope(crs));
                if (envelope == null) {
                    envelope = new GeneralBounds(crs);
                    envelope.setToNull();
                }
            }
        } else
            try {
                envelope = wrap(CRS.transform(envelope, crs));
            } catch (TransformException exception) {
                throw new IllegalArgumentException(ErrorKeys.ILLEGAL_COORDINATE_REFERENCE_SYSTEM, exception);
            }
        coverage = null;
    }

    /**
     * Sets the coordinate reference system to the specified authority code. This convenience method gives a preference
     * to axis in (<var>longitude</var>, <var>latitude</var>) order.
     *
     * @throws IllegalArgumentException if the given CRS is illegal.
     */
    public void setCoordinateReferenceSystem(final String code) throws IllegalArgumentException {
        final CoordinateReferenceSystem crs;
        try {
            crs = CRS.decode(code, true);
        } catch (FactoryException exception) {
            throw new IllegalArgumentException(ErrorKeys.ILLEGAL_COORDINATE_REFERENCE_SYSTEM, exception);
        }
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Returns a copy of current envelope. If no envelope has been {@linkplain #setEnvelope explicitly defined}, then
     * the default is inferred from the CRS (by default a geographic envelope from 180°W to 180°E and 90°S to 90°N).
     */
    public Bounds getEnvelope() {
        if (envelope != null) {
            return envelope.clone();
        } else {
            final CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
            Bounds candidate = CRS.getEnvelope(crs);
            if (candidate == null) {
                final GeneralBounds copy = new GeneralBounds(crs);
                copy.setToNull();
                candidate = copy;
            }
            return candidate;
        }
    }

    /**
     * Sets the envelope to the specified value. If a {@linkplain #setCoordinateReferenceSystem CRS was previously
     * defined}, the envelope will be reprojected to that CRS. If no CRS was previously defined, then the CRS will be
     * set to the {@linkplain Bounds#getCoordinateReferenceSystem envelope CRS}.
     *
     * @throws IllegalArgumentException if the envelope is illegal for the {@linkplain #getCoordinateReferenceSystem
     *     current CRS}.
     */
    public void setEnvelope(Bounds envelope) throws IllegalArgumentException {
        if (this.envelope != null)
            try {
                envelope = CRS.transform(envelope, this.envelope.getCoordinateReferenceSystem());
            } catch (TransformException exception) {
                throw new IllegalArgumentException(ErrorKeys.ILLEGAL_COORDINATE_REFERENCE_SYSTEM, exception);
            }
        this.envelope = new GeneralBounds(envelope);
        coverage = null;
    }

    /**
     * Sets the envelope to the specified values, which must be the lower corner coordinates followed by upper corner
     * coordinates. The number of arguments provided shall be twice the envelope dimension, and minimum shall not be
     * greater than maximum.
     *
     * <p><b>Example:</b> (<var>x</var><sub>min</sub>, <var>y</var><sub>min</sub>, <var>z</var><sub>min</sub>,
     * <var>x</var><sub>max</sub>, <var>y</var><sub>max</sub>, <var>z</var><sub>max</sub>)
     */
    public void setEnvelope(final double... ordinates) throws IllegalArgumentException {
        GeneralBounds envelope = this.envelope;
        if (envelope == null) {
            envelope = new GeneralBounds(ordinates.length / 2);
        }
        envelope.setEnvelope(ordinates);
        this.envelope = envelope; // Assigns only if successful.
    }

    /**
     * Returns the range of sample values. If no range has been {@linkplain #setSampleRange explicitly defined}, then
     * the default is a range from 0 inclusive to 256 exclusive.
     */
    public NumberRange<? extends Number> getSampleRange() {
        return range != null ? range : DEFAULT_RANGE;
    }

    /** Sets the range of sample values. */
    public void setSampleRange(final NumberRange<? extends Number> range) {
        this.range = range;
        coverage = null;
    }

    /**
     * Sets the range of sample values.
     *
     * @param lower The lower sample value (inclusive), typically 0.
     * @param upper The upper sample value (exclusive), typically 256.
     */
    public void setSampleRange(final int lower, final int upper) {
        setSampleRange(NumberRange.create(lower, true, upper, false));
    }

    /**
     * Returns the image size. If no size has been {@linkplain #setImageSize explicitly defined}, then the default is
     * 256&times;256 pixels.
     */
    public Dimension getImageSize() {
        return new Dimension(width, height);
    }

    /** Sets the image size. */
    public void setImageSize(final Dimension size) {
        width = size.width;
        height = size.height;
        image = null;
        coverage = null;
    }

    /** Sets the image size. */
    public void setImageSize(final int width, final int height) {
        setImageSize(new Dimension(width, height));
    }

    /**
     * Creates a new variable, which will be mapped to a {@linkplain GridSampleDimension sample dimension}. Additional
     * information like scale, offset and nodata values can be provided by invoking setters on the returned variable.
     *
     * @param name The variable name, or {@code null} for a default name.
     * @param units The variable units, or {@code null} if unknown.
     * @return A new variable.
     */
    public Variable newVariable(final CharSequence name, final Unit<?> units) {
        final Variable variable = new Variable(name, units);
        variables.add(variable);
        return variable;
    }

    /**
     * Returns the buffered image to be wrapped by {@link GridCoverage2D}. If no image has been
     * {@linkplain #setBufferedImage explicitly defined}, a new one is created the first time this method is invoked.
     * Users can write in this image before to create the grid coverage.
     */
    public BufferedImage getBufferedImage() {
        if (image == null) {
            final int numBands = variables.size();
            if (numBands == 0) {
                image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            } else {
                final GridSampleDimension sd = variables.get(0).getSampleDimension();
                final ColorModel cm;
                if (numBands == 1) {
                    cm = sd.getColorModel();
                } else {
                    cm = sd.getColorModel(0, numBands);
                }
                final WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
                image = new BufferedImage(cm, raster, false, null);
            }
        }
        return image;
    }

    /**
     * Sets the buffered image. Invoking this method overwrite the {@linkplain #getImageSize image size} with the given
     * image size.
     */
    public void setBufferedImage(final BufferedImage image) {
        setImageSize(image.getWidth(), image.getHeight());
        this.image = image; // Stores only if the above line succeed.
        coverage = null;
    }

    /**
     * Sets the buffered image by reading it from the given file. Invoking this method overwrite the
     * {@linkplain #getImageSize image size} with the given image size.
     *
     * @throws IOException if the image can't be read.
     */
    public void setBufferedImage(final File file) throws IOException {
        setBufferedImage(ImageIOExt.readBufferedImage(file));
    }

    /**
     * Sets the buffered image to a raster filled with random value using the specified random number generator. This
     * method can be used for testing purpose, or for adding noise to a coverage.
     */
    public void setBufferedImage(final Random random) {
        image = null; // Will forces the creation of a new BufferedImage.
        final BufferedImage image = getBufferedImage();
        final WritableRaster raster = image.getRaster();
        final ColorModel model = image.getColorModel();
        final int size;
        if (model instanceof IndexColorModel) {
            size = ((IndexColorModel) model).getMapSize();
        } else {
            size = 1 << Short.SIZE;
        }
        for (int i = raster.getWidth(); --i >= 0; ) {
            for (int j = raster.getHeight(); --j >= 0; ) {
                raster.setSample(i, j, 0, random.nextInt(size));
            }
        }
    }

    /** Returns the grid coverage. */
    public GridCoverage2D getGridCoverage2D() {
        if (coverage == null) {
            final BufferedImage image = getBufferedImage();
            final Bounds envelope = getEnvelope();
            final GridSampleDimension[] bands;
            if (variables.isEmpty()) {
                bands = null;
            } else {
                bands = new GridSampleDimension[variables.size()];
                for (int i = 0; i < bands.length; i++) {
                    bands[i] = variables.get(i).getSampleDimension();
                }
            }
            coverage = factory.create(null, image, envelope, bands, null, null);
        }
        return coverage;
    }

    /**
     * A variable to be mapped to a {@linkplain GridSampleDimension sample dimension}. Variables are created by
     * {@link GridCoverageBuilder#newVariable}.
     *
     * @since 2.5
     * @author Martin Desruisseaux
     * @version $Id$
     */
    public class Variable {
        /** The variable name, or {@code null} for a default name. */
        private final CharSequence name;

        /** The variable units, or {@code null} for a default units. */
        private final Unit<?> units;

        /** The "nodata" values. */
        private final Map<Integer, CharSequence> nodata;

        /**
         * The sample dimension. Will be created when first needed. May be reset to {@code null} after creation if a new
         * sample dimension need to be computed.
         */
        private GridSampleDimension sampleDimension;

        /**
         * Creates a new variable of the given name and units.
         *
         * @param name The variable name, or {@code null} for a default name.
         * @param units The variable units, or {@code null} if unknown.
         * @see GridCoverageBuilder#newVariable
         */
        protected Variable(final CharSequence name, final Unit<?> units) {
            this.name = name;
            this.units = units;
            this.nodata = new TreeMap<>();
        }

        /**
         * Adds a "nodata" value.
         *
         * @param name The name for the "nodata" value.
         * @param value The pixel value to assign to "nodata".
         * @throws IllegalArgumentException if the given pixel value is already assigned.
         */
        public void addNodataValue(final CharSequence name, final int value) throws IllegalArgumentException {
            final Integer key = value;
            final CharSequence old = nodata.put(key, name);
            if (old != null) {
                nodata.put(key, old);
                throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "value", key));
            }
            sampleDimension = null;
        }

        /**
         * Returns a sample dimension for the current {@linkplain GridCoverageBuilder#getSampleRange range of sample
         * values}.
         */
        public GridSampleDimension getSampleDimension() {
            if (sampleDimension == null) {
                NumberRange<? extends Number> range = getSampleRange();
                int lower = (int) Math.floor(range.getMinimum(true));
                int upper = (int) Math.ceil(range.getMaximum(false));
                final Category[] categories = new Category[nodata.size() + 1];
                int i = 0;
                for (final Map.Entry<Integer, CharSequence> entry : nodata.entrySet()) {
                    final int sample = entry.getKey();
                    if (sample >= lower && sample < upper) {
                        if (sample - lower <= upper - sample) {
                            lower = sample + 1;
                        } else {
                            upper = sample;
                        }
                    }
                    categories[i++] = new Category(entry.getValue(), null, sample);
                }
                range = NumberRange.create(lower, true, upper, false);
                categories[i] = new Category(name, null, range, true);
                sampleDimension = new GridSampleDimension(name, categories, units);
            }
            return sampleDimension;
        }

        /** Returns a string representation of this variable. */
        @Override
        public String toString() {
            final StringBuilder buffer = new StringBuilder(getClass().getSimpleName());
            buffer.append('[');
            if (name != null) {
                buffer.append('"').append(name).append('"');
                if (units != null) {
                    buffer.append(' ');
                }
            }
            if (units != null) {
                buffer.append('(').append(units).append(')');
            }
            return buffer.append(']').toString();
        }
    }
}
