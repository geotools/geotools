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
package org.geotools.imageio.metadata;

import java.awt.image.DataBuffer;

import org.geotools.util.NumberRange;

/**
 * A {@code <Band>} element in
 * {@linkplain SpatioTemporalMetadataFormat metadata format}.
 * 
 * @author Martin Desruisseaux
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-core/src/main/java/org/geotools/imageio/metadata/Band.java $
 */
public class Band extends MetadataAccessor {

    /** * */
    final ChildList<Category> categories;

    /** * */
    final Statistics statistics;

    /**
     * Creates a parser for a band. This constructor should not be invoked
     * directly; use {@link SpatioTemporalMetadata#getBand} instead.
     * 
     * @param metadata
     *                The metadata which contains this band.
     * @param bandIndex
     *                The band index for this instance.
     */
    protected Band(final SpatioTemporalMetadata metadata,
            final int bandIndex) {
        this(metadata.getSampleDimensions(), bandIndex);
        selectChild(bandIndex);
    }

    /**
     * Creates a parser for a band. This constructor should not be invoked
     * directly; use {@link SpatioTemporalMetadata#getBand} instead.
     * 
     * @param parent
     *                The set of all bands.
     * @param bandIndex
     *                The band index for this instance.
     */
    Band(final ChildList<Band> parent, final int bandIndex) {
        super(parent);
        selectChild(bandIndex);
        this.categories = new ChildList.Categories(this);
        this.statistics = new Statistics(this, SpatioTemporalMetadataFormat.MD_BD_STATISTICS, null);
    }

    /**
     * Returns the name for this band, or {@code null} if none.
     */
    public String getName() {
        return getString(SpatioTemporalMetadataFormat.MD_COMM_NAME);
    }

    /**
     * Sets the name for this band.
     * 
     * @param name
     *                The band name, or {@code null} if none.
     */
    public void setName(final String name) {
        setString(SpatioTemporalMetadataFormat.MD_COMM_NAME, name);
    }

    /**
     * Returns the range of valid values for this band. The range use the
     * {@link Integer} type if possible, or the {@link Double} type otherwise.
     * Note that range {@linkplain NumberRange#getMinValue minimum value},
     * {@linkplain NumberRange#getMaxValue maximum value} or both may be null if
     * no {@code "minValue"} or {@code "maxValue"} attribute were found for the
     * {@code "Bands/Band"} element.
     */
    public NumberRange getValidRange() {
        Number minimum, maximum;
        minimum = getInteger("minValue");
        maximum = getInteger("maxValue");
        final Class<? extends Number> type;
        if (minimum == null || maximum == null) {
            minimum = getDouble("minValue");
            maximum = getDouble("maxValue");
            type = Double.class;
        } else {
            type = Integer.class;
        }
        // Note: minimum and/or maximum may be null, in which case the range in
        // unbounded.
        return new NumberRange(type, minimum, true, maximum, true);
    }

    /**
     * Sets the range of valid values. The values should be integers most of the
     * time since they are packed values (often index in a color palette). But
     * floating point values are allowed too.
     * <p>
     * If the minimal or maximal value may be unknown, consider invoking
     * <code>{@link #setPackedValues setPackedValues}(minValue, maxValue, &hellip;)</code>
     * instead. The later can infers default bounds according a given data type.
     * 
     * @param minValue
     *                The minimal valid <em>packed</em> value, or
     *                {@link Double#NEGATIVE_INFINITY} if none.
     * @param maxValue
     *                The maximal valid <em>packed</em> value, or
     *                {@link Double#POSITIVE_INFINITY} if none.
     * 
     * @see #setPackedValues
     */
    public void setValidRange(final double minValue, final double maxValue) {
        final int minIndex = (int) minValue;
        final int maxIndex = (int) maxValue;
        if (minIndex == minValue && maxIndex == maxValue) {
            setInteger("minValue", minIndex);
            setInteger("maxValue", maxIndex);
        } else {
            setDouble("minValue", minValue);
            setDouble("maxValue", maxValue);
        }
    }

    /**
     * Returns the fill values for this band, or {@code null} if none.
     */
    public double[] getNoDataValues() {
        return getDoubles("fillValues", true);
    }

    /**
     * Sets the fill values for this band. This method formats all fill values
     * as integers if possible, or all values as floating points otherwise. We
     * apply a "all or nothing" rule for consistency.
     * 
     * @param fillValues
     *                The packed values used for missing data, or {@code null}
     *                if none.
     * 
     * @see #setPackedValues
     */
    public void setNoDataValues(final double[] fillValues) {
        if (fillValues != null) {
            int[] asIntegers = new int[fillValues.length];
            for (int i = 0; i < fillValues.length; i++) {
                final double value = fillValues[i];
                if ((asIntegers[i] = (int) value) != value) {
                    asIntegers = null; // Not integers; stop the check.
                    break;
                }
            }
            if (asIntegers != null) {
                setIntegers("fillValues", asIntegers);
                return;
            }
        }
        setDoubles("fillValues", fillValues);
    }

    /**
     * Defines valid and fill <em>packed</em> values as a combinaison of
     * <code>{@linkplain #setValidRange(double,double) setValidRange}(minValue, maxValue)</code>
     * and
     * <code>{linkplain #setNoDataValues(double[]) setNoDataValues}(fillValues)</code>.
     * <p>
     * If the minimal or maximal value is
     * {@linkplain Double#isInfinite infinite} and the data type is an integer
     * type, then this method replaces the infinite values by default bounds
     * inferred from the data type and the fill values.
     * 
     * @param minValue
     *                The minimal valid <em>packed</em> value, or
     *                {@link Double#NEGATIVE_INFINITY} if unknown.
     * @param maxValue
     *                The maximal valid <em>packed</em> value, or
     *                {@link Double#POSITIVE_INFINITY} if unknown.
     * @param fillValues
     *                The packed values used for missing data, or {@code null}
     *                if none.
     * @param dataType
     *                The raw data type as one of {@link DataBuffer} constants,
     *                or {@link DataBuffer#TYPE_UNDEFINED} if unknown.
     * 
     * @see #setValidRange
     * @see #setNoDataValues
     */
    public void setPackedValues(double minValue, double maxValue, final double[] fillValues, final int dataType) {
        minValue = replaceInfinity(minValue, fillValues, dataType);
        maxValue = replaceInfinity(maxValue, fillValues, dataType);
        setValidRange(minValue, maxValue);
        setNoDataValues(fillValues);
    }

    /**
     * If the specified value is infinity, then replace that values by a bounds
     * inferred from the specified fill values and data type.
     * 
     * @param value
     *                The value.
     * @param fillValues
     *                The packed values used for missing data, or {@code null}
     *                if none.
     * @param dataType
     *                The raw data type as one of {@link DataBuffer} constants,
     *                or {@link DataBuffer#TYPE_UNDEFINED} if unknown.
     */
    private static double replaceInfinity(double value, final double[] fillValues, final int dataType) {
        final boolean negative;
        if (value == Double.NEGATIVE_INFINITY) {
            negative = true;
        } else if (value == Double.POSITIVE_INFINITY) {
            negative = false;
        } else {
            return value;
        }
        final double midValue;
        switch (dataType) {
        default: {
            // Unsigned integer: computes the upper bound according the data
            // length.
            final long range = 1L << DataBuffer.getDataTypeSize(dataType);
            value = negative ? 0 : range - 1;
            midValue = range >>> 1;
            break;
        }
        case DataBuffer.TYPE_SHORT: {
            value = negative ? Short.MIN_VALUE : Short.MAX_VALUE;
            midValue = 0;
            break;
        }
        case DataBuffer.TYPE_INT: {
            value = negative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            midValue = 0;
            break;
        }
        case DataBuffer.TYPE_FLOAT:
        case DataBuffer.TYPE_DOUBLE:
        case DataBuffer.TYPE_UNDEFINED: {
            // Unbounded or undefined type: nothing to do.
            return value;
        }
        }
        /*
         * Considers only the fill values that are close to the bounds we just
         * computed. We use the middle value (always 0 for signed data type) as
         * the threshold for choosing which bounds is close to that value. In
         * other words, for signed data type we consider only positive or
         * negative fill values (depending the 'value' sign), not both in same
         * time.
         * 
         * For each fill value to consider, reduces the range of valid values in
         * such a way that it doesn't include that fill value. The exclusion is
         * performed by subtracting 1, which should be okay since we known at
         * this stage that the data type is integer.
         */
        if (fillValues != null) {
            double valueDistance = Math.abs(value - midValue);
            for (int i = 0; i < fillValues.length; i++) {
                final double fillValue = fillValues[i];
                if ((fillValue < midValue) == negative) {
                    final double fillDistance = Math.abs(fillValue - midValue);
                    if (fillDistance <= valueDistance) {
                        valueDistance = fillDistance;
                        value = fillValue - 1; // Value must be exclusive.
                    }
                }
            }
        }
        return value;
    }

    /**
     * Returns the scale factor from packed to geophysics values, or {@code 1}
     * if none.
     */
    public double getScale() {
        final Double scale = getDouble(SpatioTemporalMetadataFormat.MD_BD_SCALE);
        return (scale != null) ? scale.doubleValue() : 1.0;
    }

    /**
     * Sets the scale factor for this band.
     * 
     * @param scale
     *                The scale from packed to geophysics values, or {@code 1}
     *                if none.
     */
    public void setScale(final double scale) {
        setDouble(SpatioTemporalMetadataFormat.MD_BD_SCALE, scale);
    }

    /**
     * Returns the UoM if any.
     */
    public String getUoM() {
        return getString(SpatioTemporalMetadataFormat.MD_BD_UOM);
    }

    /**
     * Sets the UoM for this band.
     * 
     * @param UoM the Unit Of Measure of this band.
     */
    public void setUoM(final String uom) {
        setString(SpatioTemporalMetadataFormat.MD_BD_UOM, uom);
    }
    
    /**
     * Returns the offset from packed to geophysics values, or {@code 0} if
     * none.
     */
    public double getOffset() {
        final Double offset = getDouble(SpatioTemporalMetadataFormat.MD_BD_OFFSET);
        return (offset != null) ? offset.doubleValue() : 0.0;
    }

    /**
     * Sets the offset for this band.
     * 
     * @param offset
     *                The offset from packed to geophysics values, or {@code 0}
     *                if none.
     */
    public void setOffset(final double offset) {
        setDouble("offset", offset);
    }

    /**
     * 
     */
    public void addCategory(final String name, final String description,
            final String range) {
        Category candidate = this.categories.addChild();
        candidate.setString(SpatioTemporalMetadataFormat.MD_BD_CAT_NAME, name);
        candidate.setString(SpatioTemporalMetadataFormat.MD_BD_CAT_DESCRIPTION, description);
        candidate.setString(SpatioTemporalMetadataFormat.MD_BD_CAT_RANGE, range);
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // Category methods
    //
    // ////////////////////////////////////////////////////////////////////////
    static final class Category extends MetadataAccessor {
        protected Category(final Band metadata, final int index) {
            super(metadata);
            selectChild(index);
        }

        /**
         * Creates a parser for an category.
         * 
         * @param parent
         *                The set of all categories.
         * @param index
         *                The categories index for this instance.
         */
        Category(final ChildList<Category> parent, final int index) {
            super(parent);
            selectChild(index);
        }
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // Statistics
    //
    // ////////////////////////////////////////////////////////////////////////
    static final class Statistics extends MetadataAccessor {

        /** Histogram node * */
        final MetadataAccessor histogram;

        /**
         * Implicit constructor.
         * 
         * @param parentNode
         * @param parentPath
         * @param childPath
         */
        protected Statistics(MetadataAccessor parentNode, String parentPath, String childPath) {
            super(parentNode, parentPath, childPath);
            this.histogram = new MetadataAccessor(this,
                    SpatioTemporalMetadataFormat.MD_BD_STAT_HISTOGRAM, null);
        }

        /**
         * Sets Statistics information.
         * 
         * @param mean
         * @param mode
         * @param stdDev
         */
        public void setStatistics(final String mean, final String mode, final String stdDev) {
            this.setString(SpatioTemporalMetadataFormat.MD_BD_STAT_MEAN, mean);
            this.setString(SpatioTemporalMetadataFormat.MD_BD_STAT_MODE, mode);
            this.setString(SpatioTemporalMetadataFormat.MD_BD_STAT_STDDEV, stdDev);
        }

        /**
         * Sets Statistics Histogram information.
         * 
         * @param bins
         * @param values
         */
        public void setHistogram(final String bins, final String values) {
            this.histogram.setString(SpatioTemporalMetadataFormat.MD_BD_STAT_HIST_BINS, bins);
            this.histogram.setString(SpatioTemporalMetadataFormat.MD_BD_STAT_HIST_VALUES, values);
        }

        /**
         * Returns Statistics Mean.
         * 
         * @return
         */
        public String getMean() {
            return this.getString(SpatioTemporalMetadataFormat.MD_BD_STAT_MEAN);
        }

        /**
         * Returns Statistics Mode.
         * 
         * @return
         */
        public String getMode() {
            return this.getString(SpatioTemporalMetadataFormat.MD_BD_STAT_MODE);
        }

        /**
         * Returns Statistics Standard Deviation.
         * 
         * @return
         */
        public String getStdDev() {
            return this.getString(SpatioTemporalMetadataFormat.MD_BD_STAT_STDDEV);
        }

        /**
         * Returns Statistics Histogram bins.
         * 
         * @return
         */
        public String getHistogramBins() {
            return this.histogram.getString(SpatioTemporalMetadataFormat.MD_BD_STAT_HIST_BINS);
        }

        /**
         * Returns Statistics Histogram values.
         * 
         * @return
         */
        public String getHistogramValues() {
            return this.histogram.getString(SpatioTemporalMetadataFormat.MD_BD_STAT_HIST_VALUES);
        }
    }
}
