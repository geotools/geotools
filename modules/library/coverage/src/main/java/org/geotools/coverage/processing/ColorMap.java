/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.measure.unit.Unit;
import javax.measure.converter.ConversionException;

import org.opengis.util.InternationalString;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.TransformException;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.io.TableWriter;
import org.geotools.util.logging.Logging;
import org.geotools.util.Utilities;
import org.geotools.util.NumberRange;
import org.geotools.util.MeasurementRange;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ColorUtilities;


/**
 * Colors associated to categories. This is the parameter type for the
 * {@link org.geotools.coverage.processing.operation.Recolor} operation.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see org.geotools.coverage.processing.operation.Recolor
 *
 * @todo We need to investigage if this object should be defined as an implementation of
 *       {@link org.geotools.styling.ColorMap}.
 */
public class ColorMap implements Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 1688030908496323012L;

    /**
     * A special category name meaning "<cite>any quantitative value</cite>".
     */
    public static final CharSequence ANY_QUANTITATIVE_CATEGORY =
            Vocabulary.formatInternational(VocabularyKeys.ALL); // TODO: Find a better name.

    /**
     * The colors to apply to categories. Keys are {@link String} objects.
     * Values may be {@link Color} singletons or {@code Color[]} arrays.
     * <p>
     * The {@link #ANY_QUANTITATIVE_CATEGORY} key is replaced by {@code null} in
     * order to avoid confusion with user-specified category with the exact name.
     */
    private Map<String,Object> colorMap;

    /**
     * The range of values for quantitative categories. Values are {@link NumberRange} instances
     * if the range is relative, or {@link MeasurementRange} if the range is geophysics.
     * <p>
     * The {@link #ANY_QUANTITATIVE_CATEGORY} key is replaced by {@code null} in
     * order to avoid confusion with user-specified category with the exact name.
     */
    private Map<String,NumberRange> colorRanges;

    /**
     * If {@code true}, the ARGB values corresponding to any {@linkplain Category category}
     * <strong>not</strong> specified in this color map will be reset to the color specified
     * by the category. The default value is {@code false}.
     */
    private boolean resetUnspecifiedColors;

    /**
     * Creates an initially empty color map.
     */
    public ColorMap() {
    }

    /**
     * Creates a color map initialized to the specified color ramp to be applied on
     * {@linkplain #ANY_QUANTITATIVE_CATEGORY any quantitative category}.
     *
     * @param colors The colors to be given to this map.
     */
    public ColorMap(final Color[] colors) {
        setColors(ANY_QUANTITATIVE_CATEGORY, colors);
    }

    /**
     * Creates a color map initialized to the specified map.
     *
     * @param colorMap A map of ({@linkplain Category#getName category name},
     *        {@linkplain Color colors}) pairs.
     */
    public ColorMap(final Map<? extends CharSequence,Color[]> colorMap) {
        for (final Map.Entry<? extends CharSequence,Color[]> entry : colorMap.entrySet()) {
            setColors(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns the unlocalized flavor of the given name
     * (not to be confused with the default locale).
     *
     * @param category The {@linkplain Category#getName category name}.
     * @return The unlocalized name, or {@code null}.
     */
    private static String unlocalized(final CharSequence name) {
        if (name == ANY_QUANTITATIVE_CATEGORY) {
            return null;
        }
        if (name instanceof InternationalString) {
            return ((InternationalString) name).toString(null);
        } else {
            return name.toString();
        }
    }

    /**
     * Applies colors to the given category.
     *
     * @param category The {@linkplain Category#getName name of the category}
     *        for which to set the colors.
     * @param colors Colors to apply to the specified category, or {@code null}.
     */
    private void setColorObject(final CharSequence category, final Object colors) {
        final String name = unlocalized(category);
        if (colors != null) {
            if (colorMap == null) {
                colorMap = new HashMap<String,Object>();
            }
            colorMap.put(name, colors);
        } else if (colorMap != null) {
            colorMap.remove(name);
            if (colorMap.isEmpty()) {
                colorMap = null; // For more accurate 'equals' implementation.
            }
        }
    }

    /**
     * Applies a uniform color to the given (usually <cite>qualitative</cite>) category.
     *
     * @param category The {@linkplain Category#getName name of the category}
     *        for which to set the color.
     * @param color A uniform color to apply to the specified category, or {@code null}
     *        for removing the color mapping.
     *
     * @see #recolor
     */
    public void setColor(final CharSequence category, final Color color) {
        setColorObject(category, color);
    }

    /**
     * Applies a color ramp to the given (usually <cite>quantitative</cite>) category.
     * The color array may have any length; colors will be interpolated as needed.
     *
     * @param category The {@linkplain Category#getName name of the category} for which to set
     *        the colors, or {@link #ANY_QUANTITATIVE_CATEGORY} if the colors should apply to
     *        any quantitative category.
     * @param colors The colors to apply to the specified category, or {@code null}
     *        or an empty array for removing the color mapping.
     *
     * @see #recolor
     */
    public void setColors(final CharSequence category, final Color[] colors) {
        final Object value;
        if (colors != null) {
            switch (colors.length) {
                default: value = colors.clone(); break;
                case 1:  value = colors[0];      break;
                case 0:  value = null;           break;
            }
        } else {
            value = null;
        }
        setColorObject(category, value);
    }

    /**
     * Returns the color ramp for the given category.
     *
     * @param  category The {@linkplain Category#getName category name}, or
     *         {@link #ANY_QUANTITATIVE_CATEGORY} for fetching the colors to
     *         apply to any quantitative category.
     * @return The color ramp, or {@code null} if none.
     */
    public Color[] getColors(final CharSequence category) {
        if (colorMap == null) {
            return null;
        }
        final String name = unlocalized(category);
        Object colors = colorMap.get(name);
        if (colors == null) {
            if (name!=null && category instanceof InternationalString) {
                // Unlocalized name not found. Search using the localized flavor.
                colors = getColors(category.toString());
                if (colors == null) {
                    return null;
                }
            } else {
                return null;
            }
        }
        if (colors instanceof Color) {
            return new Color[] {(Color) colors};
        }
        return ((Color[]) colors).clone();
    }

    /**
     * Sets a range of geophysics values for the color ramp associated with a quantitative category.
     * For example if the category "<cite>Height</cite>" applies to geophysics values in the range
     * [0..500] metres and if a range of [100..400] metres is defined as below:
     *
     * <blockquote><code>
     * setRelativeRange("Height", new MeasurementRange(0, 100, SI.METRE));
     * setColors("Height", myColorPalette);
     * </code><blockquote>
     *
     * Then {@code myColorPalette} will applies to pixel values in the range [100..400] instead
     * of [0..500]. This is typically used in order to augment the contrast in a range of values
     * of special interest.
     * <p>
     * This method is exclusive with {@link #setRelativeRange}.
     *
     * @param category The {@linkplain Category#getName name of the category}
     *        for which to set the geophysics range.
     * @param range The minimal and maximal values for the color ramp. A {@code null}
     *        value removes the range mapping.
     *
     * @see #recolor
     */
    public void setGeophysicsRange(final CharSequence category, final MeasurementRange<?> range) {
        setRange(category, range);
    }

    /**
     * Sets a relative range of values for the color ramp associated to a quantitative category.
     * For example if the category "<cite>Height</cite>" applies to pixel values in the range
     * [0..200] and if a relative range of [20%..80%] is defined as below:
     *
     * <blockquote><code>
     * setRelativeRange("Height", new NumberRange(20, 80));
     * setColors("Height", myColorPalette);
     * </code><blockquote>
     *
     * Then {@code myColorPalette} will applies to pixel values in the range [40..160] instead
     * of [0..200]. This is typically used in order to augment the contrast in a range of values
     * of special interest.
     * <p>
     * This method is exclusive with {@link #setGeophysicsRange}.
     *
     * @param category The {@linkplain Category#getName name of the category}
     *        for which to set the relative range.
     * @param range The minimal and maximal relative values for the color ramp, as percentages
     *        between 0 and 100. A {@code null} value removes the range mapping.
     *
     * @see #recolor
     */
    public void setRelativeRange(final CharSequence category, final NumberRange<?> range) {
        if (range instanceof MeasurementRange) {
            // The MeasurementRange type is reserved for geophysics ranges.
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, "range"));
        }
        setRange(category, range);
    }

    /**
     * Sets a relative or geophysics range.
     * This method is exclusive with {@link #setGeophysicsRange}.
     *
     * @param category The {@linkplain Category#getName name of the category}
     *        for which to set the relative or geophysics range.
     * @param range The minimal and maximal values for the color ramp.
     */
    private void setRange(final CharSequence category, final NumberRange range) {
        final String name = unlocalized(category);
        if (range != null) {
            if (colorRanges == null) {
                colorRanges = new HashMap<String,NumberRange>();
            }
            colorRanges.put(name, range);
        } else if (colorRanges != null) {
            colorRanges.remove(name);
            if (colorRanges.isEmpty()) {
                colorRanges = null; // For more accurate 'equals' implementation.
            }
        }
    }

    /**
     * Returns the range of geophysics values for the given category.
     *
     * @param  category The {@linkplain Category#getName category name}, or
     *         {@link #ANY_QUANTITATIVE_CATEGORY} for fetching the range to
     *         apply to any quantitative category.
     * @return The geophysics range, or {@code null} if none.
     */
    public MeasurementRange<?> getGeophysicsRange(final CharSequence category) {
        final NumberRange<?> range = getRange(category);
        return (range instanceof MeasurementRange) ? (MeasurementRange) range : null;
    }

    /**
     * Returns the relative range of values for the given category.
     *
     * @param  category The {@linkplain Category#getName category name}, or
     *         {@link #ANY_QUANTITATIVE_CATEGORY} for fetching the relative
     *         range to apply to any quantitative category.
     * @return The relative range, or {@code null} if none.
     */
    public NumberRange getRelativeRange(final CharSequence category) {
        final NumberRange range = getRange(category);
        return (range instanceof MeasurementRange) ? null : range;
    }

    /**
     * Returns the range of relative or geophysics values. If the returned range is an instance of
     * {@link MeasurementRange}, then is is a {@linkplain #getGeophysicsRange geophysics range}.
     * Otherwise it is a {@linkplain #getRelativeRange relative range}.
     *
     * @param  category The {@linkplain Category#getName category name}, or
     *         {@link #ANY_QUANTITATIVE_CATEGORY} for fetching the range to
     *         apply to any quantitative category.
     * @return The relative or geophysics range, or {@code null} if none.
     */
    private NumberRange getRange(final CharSequence category) {
        if (colorRanges == null) {
            return null;
        }
        final String name = unlocalized(category);
        NumberRange range = colorRanges.get(name);
        if (range == null) {
            if (name!=null && category instanceof InternationalString) {
                // Unlocalized name not found. Search using the localized flavor.
                range = colorRanges.get(category.toString());
            }
        }
        return range;
    }

    /**
     * Returns the range of sample values for the given category, or {@code null} if none.
     * This range is computed from the {@linkplain #getRange relative or geophysics range}.
     *
     * @param  category The category for which to compute the range.
     * @param  units The category units, usually {@link GridSampleDimension#getUnits}.
     * @return The range, or {@code null} if none. The lower index is always inclusive
     *         and the upper index is always exclusive.
     */
    private NumberRange getTargetRange(final Category category, final Unit<?> units) {
        NumberRange scale = getRange(category.getName());
        if (scale == null) {
            if (category.isQuantitative()) {
                scale = getRange(ANY_QUANTITATIVE_CATEGORY);
            }
            if (scale == null) {
                return null;
            }
        }
        double  minimum     = scale.getMinimum();
        double  maximum     = scale.getMaximum();
        boolean minIncluded = scale.isMinIncluded();
        boolean maxIncluded = scale.isMaxIncluded();
        if (scale instanceof MeasurementRange) {
            try {
                scale = ((MeasurementRange) scale).convertTo(units);
            } catch (ConversionException e) {
                Logging.unexpectedException(CoverageProcessor.LOGGER, ColorMap.class, "recolor", e);
                return null; // This is allowed by this method contract.
            }
            MathTransform1D tr = category.getSampleToGeophysics();
            if (tr != null) try {
                tr = tr.inverse();
                minimum = tr.transform(minimum);
                maximum = tr.transform(maximum);
            } catch (TransformException e) {
                Logging.unexpectedException(CoverageProcessor.LOGGER, ColorMap.class, "recolor", e);
                return null; // This is allowed by this method contract.
            }
        } else {
            final NumberRange range = category.getRange();
            final double lower  = range.getMinimum();
            final double extent = range.getMaximum() - lower;
            minimum     = (minimum / 100) * extent + lower;
            maximum     = (maximum / 100) * extent + lower;
            minIncluded &= range.isMinIncluded();
            maxIncluded &= range.isMaxIncluded();
        }
        final int lower, upper;
        if (minimum > maximum) {
            lower = round(maximum,  maxIncluded);
            upper = round(minimum, !minIncluded);
        } else {
            lower = round(minimum,  minIncluded);
            upper = round(maximum, !maxIncluded);
        }
        return NumberRange.create(lower, true, upper, false);
    }

    /**
     * Round the specified number to the {@linkplain Math#floor lower} or {@linkplain Math#ceil
     * upper} value, depending if the value is inclusive or not. This method is appropriate for
     * minimal range value. In order to apply it to the maximal range value, {@code included}
     * must be replaced by {@code !included}.
     */
    private static int round(final double value, final boolean included) {
        final double rounded = included ? Math.floor(value) : Math.ceil(value);
        int asInteger = (int) rounded;
        if (!included && value == rounded) {
            asInteger++;
        }
        return asInteger;
    }

    /**
     * If {@code true}, the ARGB values corresponding to any {@linkplain Category category}
     * <strong>not</strong> specified in this color map will be reset to the color specified
     * by the category. The default value is {@code false}.
     *
     * @param reset {@code true} if unspecified colors should be reset to the colors given in
     *        the category object.
     */
    public void setResetUnspecifiedColors(final boolean reset) {
        resetUnspecifiedColors = reset;
    }

    /**
     * If {@code true}, the ARGB values corresponding to any {@linkplain Category category}
     * <strong>not</strong> specified in this color map will be reset to the color specified
     * by the category. The default value is {@code false}.
     *
     * @return {@code true} if unspecified colors should be reset to the colors given in
     *         the category object.
     */
    public boolean getResetUnspecifiedColors() {
        return resetUnspecifiedColors;
    }

    /**
     * Applies to the specified sample dimension the colors given to this color map. This method
     * iterates throug every {@linkplain Category categories} in the given sample dimension. For
     * each category with a {@linkplain Category#getName name} matching one of the (<var>name</var>,
     * <var>colors</var>) or (<var>name</var>, <var>range</var>) entries given to this color map,
     * the {@link Category#recolor recolor} method is invoked on that category and the result
     * inserted into a new sample dimension to be returned.
     * <p>
     * If the optional {@code ARGB} array is non-null, then the ARGB colors for recolorized
     * categories will be written in this array. Only the elements with index in the
     * {@linkplain Category#getRange category range} will be overwritten; other elements
     * will not be modified.
     * <p>
     * <strong>NOTE:</strong> The {@linkplain #setGeophysicsRange geophysics} and
     * {@linkplain #setRelativeRange relative} ranges are taken in account for the
     * {@code ARGB} array only; they do not have impact on the categories to be
     * included in the returned sample dimension.
     *
     * @param  sampleDimension The sample dimension to recolorize.
     * @param  ARGB An optional array where to store the ARGB values of recolorized categories,
     *         or {@code null} if none.
     * @return A new sample dimension, or {@code sampleDimension} if no color change were applied.
     *
     * @see Category#recolor
     */
    public GridSampleDimension recolor(final GridSampleDimension sampleDimension, final int[] ARGB) {
        final GridSampleDimension displayDimension = sampleDimension.geophysics(false);
        boolean changed = false;
        final Category categories[] = (Category[]) displayDimension.getCategories().toArray();
        for (int i=0; i<categories.length; i++) {
            Category category = categories[i];
            Color[] colors = getColors(category.getName());
            if (colors == null) {
                if (category.isQuantitative()) {
                    colors = getColors(ANY_QUANTITATIVE_CATEGORY);
                }
                if (colors == null && resetUnspecifiedColors) {
                    colors = category.getColors();
                }
                // 'colors' may still null, so we will need to check.
            }
            if (ARGB != null) {
                final NumberRange range = category.getRange();
                int lower = ((Number) range.getMinValue()).intValue();
                int upper = ((Number) range.getMaxValue()).intValue();
                if (!range.isMinIncluded()) lower++;
                if ( range.isMaxIncluded()) upper++;
                boolean outOfBounds = false;
                if (lower < 0) {
                    lower = 0;
                    outOfBounds = true;
                }
                if (upper > ARGB.length) {
                    upper = ARGB.length;
                    outOfBounds = true;
                }
                if (outOfBounds) {
                    CoverageProcessor.LOGGER.warning(Errors.format(
                            ErrorKeys.VALUE_OUT_OF_BOUNDS_$3, category, 0, ARGB.length - 1));
                }
                if (upper <= lower) {
                    continue;
                }
                final NumberRange target = getTargetRange(category, sampleDimension.getUnits());
                if (target != null) {
                    if (colors == null) {
                        colors = category.getColors();
                    }
                    if (colors.length >= 2) {
                        assert target.isMinIncluded() && !target.isMaxIncluded() : target;
                        final int lo = Math.max(lower, ((Number) target.getMinValue()).intValue());
                        final int hi = Math.min(upper, ((Number) target.getMaxValue()).intValue());
                        if (lo != lower || hi != upper) {
                            Arrays.fill(ARGB, lower, lo, colors[0].getRGB());
                            Arrays.fill(ARGB, hi, upper, colors[colors.length-1].getRGB());
                            lower = lo;
                            upper = hi;
                        }
                    }
                } else if (colors == null) {
                    /*
                     * If there is no range to change (target == null) and no colors explicitly
                     * specified by the user (colors == null), then there is nothing to do.
                     */
                    continue;
                }
                ColorUtilities.expand(colors, ARGB, lower, upper);
            } else if (colors == null) {
                continue;
            }
            category = category.recolor(colors);
            if (!categories[i].equals(category)) {
                categories[i] = category;
                changed = true;
            }
        }
        if (!changed) {
            return sampleDimension;
        }
        GridSampleDimension result = new GridSampleDimension(displayDimension.getDescription(),
                categories, displayDimension.getUnits());
        if (sampleDimension != displayDimension) {
            result = result.geophysics(true);
        }
        return result;
    }

    /**
     * Returns all category names declared in this color map, in alphabetical order.
     * If the {@link #ANY_QUANTITATIVE_CATEGORY} special value is presents, it will
     * appears last.
     */
    private CharSequence[] getCategoryNames() {
        final Set<String> names;
        if (colorMap != null) {
            if (colorRanges != null) {
                names = new HashSet<String>(colorMap.keySet());
                names.addAll(colorRanges.keySet());
            } else {
                names = colorMap.keySet();
            }
        } else {
            if (colorRanges != null) {
                names = colorRanges.keySet();
            } else {
                names = Collections.emptySet();
            }
        }
        int count = names.size();
        final CharSequence[] asArray = names.toArray(new CharSequence[count]);
        for (int i=count; --i>=0;) {
            if (asArray[i] == null) {
                System.arraycopy(asArray, i+1, asArray, i, --count-i);
                asArray[count] = ANY_QUANTITATIVE_CATEGORY;
                // We could stop the loop here since we should not have any additional
                // null values. However we let the loop continue as a paranoiac check.
            }
        }
        Arrays.sort(asArray, 0, count);
        return asArray;
    }

    /**
     * Returns a hash code value for this color map.
     */
    @Override
    public int hashCode() {
        return (int) serialVersionUID ^
              ((colorMap    != null ? colorMap   .hashCode() : 31) + 37*
               (colorRanges != null ? colorRanges.hashCode() : 31));
    }

    /**
     * Compares this color map with the specified object for equality.
     *
     * @param object The object to compare with this color map.
     * @return {@code true} if the given object is equals to this color map.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && getClass().equals(object.getClass())) {
            final ColorMap that = (ColorMap) object;
            return Utilities.equals(this.colorMap,    that.colorMap) &&
                   Utilities.equals(this.colorRanges, that.colorRanges);
        }
        return false;
    }

    /**
     * Returns a string representation of this color map.
     */
    @Override
    public String toString() {
        final CharSequence[] names = getCategoryNames();
        final TableWriter writer = new TableWriter(null, 1);
        for (int i=0; i<names.length; i++) {
            final CharSequence name = names[i];
            writer.write(name.toString());
            if (colorRanges != null) {
                final NumberRange range = getRange(name);
                if (range != null) {
                    writer.write(' ');
                    writer.write(range.toString());
                    if (!(range instanceof MeasurementRange)) {
                        writer.write('%');
                    }
                }
            }
            writer.nextColumn();
            writer.write(':');
            writer.nextColumn();
            final Color[] colors = getColors(name);
            if (colors != null) {
                final String message;
                if (colors.length == 1) {
                    message = Integer.toHexString(colors[0].getRGB()).toUpperCase();
                } else {
                    message = Vocabulary.format(VocabularyKeys.COLOR_COUNT_$1, colors.length);
                }
                writer.write(message);
            }
            writer.nextLine();
        }
        return writer.toString();
    }
}
