/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import it.geosolutions.jaiext.classifier.LinearColorMap;
import it.geosolutions.jaiext.classifier.LinearColorMap.LinearColorMapType;
import it.geosolutions.jaiext.classifier.LinearColorMapElement;
import it.geosolutions.jaiext.piecewise.PiecewiseUtilities;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.util.SuppressFBWarnings;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Builder facility for creating a {@link LinearColorMap} using elements from {@link
 * RasterSymbolizer} {@link ColorMapTransform} element.
 *
 * <p>This class is not intended to be thread safe.
 *
 * @author Simone Giannecchini, GeoSolutions
 */
public class SLDColorMapBuilder {

    public static final Color defaultColorForValuesToPreserve = new Color(0, 0, 0, 0);

    /** Default color to fill gaps.* */
    public static final Color defaultGapsColor = new Color(0, 0, 0, 0);

    /**
     * List of {@link LinearColorMapElement} we are putting together to create the final {@link
     * LinearColorMap}*
     */
    private final List<LinearColorMapElement> colormapElements =
            new ArrayList<LinearColorMapElement>();

    /**
     * {@link LinearColorMapType} *
     *
     * @uml.property name="linearColorMapType"
     */
    private int linearColorMapType = -1;

    /** {@link Color} used to fill gaps.* */
    private Color gapsColor;

    /** {@link Color} use for the values that we want to preserve through the color map.* */
    private Color preservedValuesColor;

    /** List of values that we want to preserve through the color map.* */
    private final List<Double> preservedValues = new ArrayList<Double>();

    /** Number of colors we can distribute to each {@link ColorMapTransformElement}.* */
    private int colorsPerColorMapElement;

    /** Used during {@link LinearColorMap} creation to hold the last color. */
    private Color lastColorValue;

    private String name;

    /** Default constructor for the {@link SLDColorMapBuilder} class. */
    public SLDColorMapBuilder() {
        this.name = "sld-colormap";
    }

    /**
     * Constructor for the {@link SLDColorMapBuilder} class.
     *
     * @param name name for the {@link LinearColorMap} we will create at the end of this process.
     */
    public SLDColorMapBuilder(final String name) {
        ColorMapUtilities.ensureNonNull("name", name);
        this.name = name;
    }

    /**
     * Sets the default {@link Color} to use when a value falls outside the range of values for
     * provided color map elements.
     *
     * <p>Note that once the underlying colormap has been built this method will throw an {@link
     * IllegalStateException} if invoked.
     *
     * <p>In case one would want to unset the default color, he should simply call this method with
     * a <code>null</code> value.
     *
     * @param defaultColor The default {@link Color} to use when a value falls outside the provided
     *     color map elements.
     * @uml.property name="gapsColor"
     */
    public void setGapsColor(final Color defaultColor) {

        // /////////////////////////////////////////////////////////////////////
        //
        // Do we already have a liner color map?
        //
        // /////////////////////////////////////////////////////////////////////
        checkIfColorMapCreated();

        // set the defult color
        this.gapsColor = defaultColor;
    }

    /**
     * Checks whether or not the underlying {@link LinearColorMap} has been already created.
     *
     * @throws IllegalStateException In case the the underlying {@link LinearColorMap} has been
     *     already created.
     */
    private void checkIfColorMapCreated() throws IllegalStateException {
        // /////////////////////////////////////////////////////////////////////
        //
        // Do we already have a liner color map?
        //
        // /////////////////////////////////////////////////////////////////////
        if (this.colorMap != null)
            throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));
    }

    /**
     * Sets the {@link LinearColorMapType} for this {@link SLDColorMapBuilder} .
     *
     * @see LinearColorMapType
     * @return this {@link SLDColorMapBuilder} .
     * @uml.property name="linearColorMapType"
     */
    public SLDColorMapBuilder setLinearColorMapType(int colorMapType) {
        // //
        //
        // Do we already have a liner color map?
        //
        // //
        checkIfColorMapCreated();

        ////
        //
        // Color map type cannot be changed once it has been set.
        //
        /////
        if (LinearColorMapType.validateColorMapTye(this.linearColorMapType))
            throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));

        ////
        //
        // provided Color map type validation.
        //
        /////
        if (!LinearColorMapType.validateColorMapTye(colorMapType))
            throw new IllegalArgumentException(
                    Errors.format(
                            ErrorKeys.ILLEGAL_ARGUMENT_$2,
                            "colorMapType",
                            Integer.toString(colorMapType)));
        /// set the linear colormap type
        this.linearColorMapType = colorMapType;
        return this;
    }

    /**
     * Retrieves the {@link LinearColorMapType} for this {@link SLDColorMapBuilder} .
     *
     * <p><code>-1</code> is returned in case the {@link LinearColorMapType} is still unspecified.
     *
     * @return the {@link LinearColorMapType} for this {@link SLDColorMapBuilder} or <code>-1</code>
     *     case the {@link LinearColorMapType} is still unspecified.
     * @uml.property name="linearColorMapType"
     */
    public int getLinearColorMapType() {
        return linearColorMapType;
    }

    /**
     * Add a new {@link ColorMapEntry} to the list of {@link ColorMapEntry} we want to use for
     * building a {@link LinearColorMap}.
     *
     * <p>
     */
    public SLDColorMapBuilder addColorMapEntry(ColorMapEntry colorMapEntry) {
        ///////////////////////////////////////////////////////////////////////
        //
        // INITIAL CHECKS
        //
        ///////////////////////////////////////////////////////////////////////
        // Color map type must be already set.
        ColorMapUtilities.ensureNonNull("colorMapEntry", colorMapEntry);
        // Color map already created.
        checkIfColorMapCreated();
        // Color map type must be already set.
        // Number of domains must be already set.
        ColorMapUtilities.ensureNonNull("colorMapEntry", colorMapEntry);
        if (this.numberColorMapEntries == -1
                || linearColorMapType == -1
                || numberColorMapEntries < colormapElements.size() + 1)
            throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));

        ////
        //
        // Check that we already computed the number of colors for each range
        //
        /////
        init();

        ///////////////////////////////////////////////////////////////////////
        //
        // ACTUAL WORK
        //
        ///////////////////////////////////////////////////////////////////////
        ////
        //
        // Parse the provided  ColorMapEntry
        //
        /////
        // label
        String label = colorMapEntry.getLabel();
        label = label == null ? "ColorMapEntry" + this.colormapElements.size() : label;

        // quantity
        final double q = getQuantity(colorMapEntry);

        // color and opacity
        Color newColorValue = getColor(colorMapEntry);
        ColorMapUtilities.ensureNonNull("newColorValue", newColorValue);
        final double opacityValue = getOpacity(colorMapEntry);
        newColorValue =
                new Color(
                        newColorValue.getRed(),
                        newColorValue.getGreen(),
                        newColorValue.getBlue(),
                        (int) (opacityValue * 255 + 0.5));

        ////
        //
        // Create a specific domain for it
        //
        /////
        final boolean firstEntry = this.colormapElements.isEmpty();
        if (firstEntry) {

            ////
            //
            // if this is the first entry we are adding, we have a pretty special treatment for it
            // since it has to cover, depending on the colormap type a big part of the possible
            // values.
            //
            //
            ////

            switch (linearColorMapType) {
                case ColorMap.TYPE_RAMP:
                    colormapElements.add(
                            LinearColorMapElement.create(
                                    label,
                                    newColorValue,
                                    RangeFactory.create(Double.NEGATIVE_INFINITY, false, q, false),
                                    0));
                    break;
                case ColorMap.TYPE_VALUES:
                    colormapElements.add(LinearColorMapElement.create(label, newColorValue, q, 0));
                    break;
                case ColorMap.TYPE_INTERVALS:
                    colormapElements.add(
                            LinearColorMapElement.create(
                                    label,
                                    newColorValue,
                                    RangeFactory.create(Double.NEGATIVE_INFINITY, false, q, false),
                                    0));
                    break;
                default:
                    // should not happen
                    throw new IllegalArgumentException(
                            Errors.format(
                                    ErrorKeys.ILLEGAL_ARGUMENT_$2,
                                    "ColorMapTransform.type",
                                    Double.toString(opacityValue),
                                    Integer.valueOf(linearColorMapType)));
            }

        } else {
            ////
            //
            // this is NOT the first entry we are adding, hence we need to connect it to the
            // previous one.
            //
            //
            ////

            // Get the previous category
            final int newColorMapElementIndex = this.colormapElements.size();
            final LinearColorMapElement previous =
                    (LinearColorMapElement) this.colormapElements.get(newColorMapElementIndex - 1);

            // //
            //
            // Build the new one.
            //
            // //
            double previousMax = ((Range) previous.getRange()).getMax().doubleValue();
            Color[] previousColors = previous.getColors();
            if (PiecewiseUtilities.compare(previousMax, q) != 0) {
                Range valueRange = RangeFactory.create(previousMax, true, q, false);

                switch (linearColorMapType) {
                    case ColorMap.TYPE_RAMP:
                        Color[] colors = new Color[] {lastColorValue, newColorValue};
                        int previousMaximum = (int) previous.getOutputRange().getMax().intValue();
                        // the piecewise machinery will complain if we have different colors
                        // on touching ranges, work around it by not including the previous
                        // max at the beginning of the range in case that happens (uses might
                        // want to have a sharp jump in a ramp, achieved by having two subseqent
                        // entries with the same value, but different color)
                        boolean minIncluded =
                                previousColors[previousColors.length - 1].equals(colors[0]);
                        Range sampleRange =
                                RangeFactory.create(
                                        previousMaximum,
                                        minIncluded,
                                        colorsPerColorMapElement + previousMaximum,
                                        false);
                        colormapElements.add(
                                LinearColorMapElement.create(
                                        label, colors, valueRange, sampleRange));
                        break;
                    case ColorMap.TYPE_VALUES:
                        colormapElements.add(
                                LinearColorMapElement.create(
                                        label, newColorValue, q, newColorMapElementIndex));
                        break;
                    case ColorMap.TYPE_INTERVALS:
                        colormapElements.add(
                                LinearColorMapElement.create(
                                        label, newColorValue, valueRange, newColorMapElementIndex));
                        break;
                    default:
                        throw new IllegalArgumentException(
                                Errors.format(
                                        ErrorKeys.ILLEGAL_ARGUMENT_$2,
                                        "ColorMapTransform.type",
                                        Double.toString(opacityValue),
                                        Integer.valueOf(linearColorMapType)));
                }
            }
        }
        lastColorValue = newColorValue;
        return this;
    }

    /** initialization of the basic element for this {@link SLDColorMapBuilder}. */
    private void init() {
        if (numberOfColorMapElements != -1) return;
        // //
        //
        // A ColorMapTransform with a single entry makes sense only if we have
        // ColorMapTransform type VALUES
        //
        // //
        if (numberColorMapEntries == 1 && linearColorMapType != ColorMap.TYPE_VALUES)
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "colormap entries", "1"));

        // /////////////////////////////////////////////////////////////////////
        //
        // PREPARATION
        //
        // /////////////////////////////////////////////////////////////////////
        numberOfColorMapElements = numberColorMapEntries;
        if (linearColorMapType == ColorMap.TYPE_RAMP) {

            // //
            //
            // Let's distribute the number of colors that we'll use between the
            // various color map elements we'll build. We start by checking if we can use
            // 256 colors, otherwise we switch to 65536.
            //
            // Keep into account that some categories are inherently single
            // valued, hence we take them out of the count because we don't.
            //
            // //
            double colorsToDistribute =
                    !extendedColors && numberColorMapEntries < 256 ? 256 : 65536;
            // default color
            if (gapsColor != null) colorsToDistribute--;
            // preserved values
            if (preservedValuesColor != null) colorsToDistribute--;

            // compute the number of colors we can use for each color map element
            colorsPerColorMapElement =
                    (int)
                            ((colorsToDistribute)
                                    /
                                    // we remove one since the first and last element use 1 color
                                    // only, hence we want to account only the internal ranges
                                    (numberOfColorMapElements - 1));

            // now keep into account the last element
            numberOfColorMapElements++;
        } else colorsPerColorMapElement = 1;
    }

    /**
     * Retrieves the values to preserve.
     *
     * @return an array of double which represents the values that need to be preserved by the
     *     {@link ColorMapTransform} we will create.
     */
    public double[] getValuesToPreserve() {
        if (this.preservedValues.size() == 0) return new double[0];
        final double retVal[] = new double[this.preservedValues.size()];
        int i = 0;
        for (Double value : preservedValues) retVal[i++] = value.doubleValue();
        return retVal;
    }

    /**
     * Add a value that we should try to preserve while applying the color map.
     *
     * <p>This means that all the values we add using this method will be mapped to the same color
     * which can be set using {@link #setColorForValuesToPreserve(Color)}.
     *
     * <p>
     *
     * @return this {@link SLDColorMapBuilder}.
     */
    public SLDColorMapBuilder addValueToPreserve(final double value) {
        checkIfColorMapCreated();

        // add the value to preserve if not already in
        assert preservedValues != null;
        preservedValues.add(value);
        return this;
    }

    /**
     * Set the color to use for the values we want to preserve.
     *
     * @return this {@link SLDColorMapBuilder}.
     */
    public SLDColorMapBuilder setColorForValuesToPreserve(final Color color) {
        ColorMapUtilities.ensureNonNull("color", color);
        checkIfColorMapCreated();
        // @todo TODO check number of color map entries
        this.preservedValuesColor = color;
        return this;
    }

    /** @return */
    public Color getColorForValuesToPreserve() {
        return this.preservedValuesColor;
    }

    /** */
    @SuppressFBWarnings("NP_NULL_PARAM_DEREF")
    private static Color getColor(ColorMapEntry entry) throws NumberFormatException {
        ColorMapUtilities.ensureNonNull("ColorMapEntry", entry);
        Expression color = entry.getColor();
        ColorMapUtilities.ensureNonNull("color", color);
        String colorString = (String) color.evaluate(null, String.class);
        if (colorString != null && colorString.startsWith("${")) {
            color = ExpressionExtractor.extractCqlExpressions(colorString);
            colorString = color.evaluate(null, String.class);
        }
        ColorMapUtilities.ensureNonNull("colorString", colorString);
        return Color.decode(colorString);
    }

    /** */
    private static double getOpacity(ColorMapEntry entry)
            throws IllegalArgumentException, MissingResourceException {

        ColorMapUtilities.ensureNonNull("ColorMapEntry", entry);
        // //
        //
        // As stated in <a
        // href="https://portal.opengeospatial.org/files/?artifact_id=1188">
        // OGC Styled-Layer Descriptor Report (OGC 02-070) version
        // 1.0.0.</a>:
        // "Not all systems can support opacity in colormaps. The default
        // opacity is 1.0 (fully opaque)."
        //
        // //
        ColorMapUtilities.ensureNonNull("entry", entry);
        Expression opacity = entry.getOpacity();
        Double opacityValue = null;
        if (opacity != null) opacityValue = (Double) opacity.evaluate(null, Double.class);
        else return 1.0;
        if (opacityValue == null && opacity instanceof Literal) {
            String opacityExp = opacity.evaluate(null, String.class);
            opacity = ExpressionExtractor.extractCqlExpressions(opacityExp);
            opacityValue = opacity.evaluate(null, Double.class);
        }
        if (opacityValue == null) {
            throw new IllegalArgumentException(
                    "Opacity value null or could not be converted to a double" + opacity);
        }
        if ((opacityValue.doubleValue() - 1) > 0 || opacityValue.doubleValue() < 0) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "Opacity", opacityValue));
        }
        return opacityValue.doubleValue();
    }

    /** */
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    private static double getQuantity(ColorMapEntry entry) {
        ColorMapUtilities.ensureNonNull("ColorMapEntry", entry);
        Expression quantity = entry.getQuantity();
        ColorMapUtilities.ensureNonNull("quantity", quantity);
        Double quantityString = quantity.evaluate(null, Double.class);
        if (quantityString == null && quantity instanceof Literal) {
            String quantityExp = quantity.evaluate(null, String.class);
            quantity = ExpressionExtractor.extractCqlExpressions(quantityExp);
            quantityString = quantity.evaluate(null, Double.class);
        }
        ColorMapUtilities.ensureNonNull("quantityString", quantityString);
        double q = quantityString.doubleValue();
        return q;
    }

    /** @uml.property name="extendedColors" */
    private boolean extendedColors = false;

    /**
     * Getter of the property <tt>extendedColors</tt>
     *
     * @return Returns the extendedColors.
     * @uml.property name="extendedColors"
     */
    public boolean getExtendedColors() {
        return extendedColors;
    }

    /**
     * Setter of the property <tt>extendedColors</tt>
     *
     * <p>Unless this property is set prior to start working with this {@link SLDColorMapBuilder} we
     * will make use of only 256 colors. If we use extended colors, then we'll be able to use up to
     * 65536 colors.
     *
     * <p>Note that this imposes a limitation on the maximum number of {@link ColorMapEntry} we can
     * use.
     *
     * @param extendedColors The extendedColors to set.
     * @uml.property name="extendedColors"
     */
    public SLDColorMapBuilder setExtendedColors(boolean extendedColors) {
        if (this.numberColorMapEntries != -1)
            throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));
        checkIfColorMapCreated();
        this.extendedColors = extendedColors;
        return this;
    }

    /** @uml.property name="numberColorMapEntries" */
    private int numberColorMapEntries = -1;

    /**
     * Getter of the property <tt>numberColorMapEntries</tt>
     *
     * @return Returns the numberColorMapEntries.
     * @uml.property name="numberColorMapEntries"
     */
    public int getNumberColorMapEntries() {
        return numberColorMapEntries;
    }

    /**
     * Setter of the property <tt>numberColorMapEntries</tt>
     *
     * @param numberColorMapEntries The numberColorMapEntries to set.
     * @uml.property name="numberColorMapEntries"
     */
    public SLDColorMapBuilder setNumberColorMapEntries(final int numberColorMapEntries) {
        checkIfColorMapCreated();
        if (this.numberColorMapEntries != -1)
            throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));
        if (numberColorMapEntries <= 0 || numberColorMapEntries > (extendedColors ? 65536 : 256))
            throw new IllegalArgumentException(
                    Errors.format(
                            ErrorKeys.ILLEGAL_ARGUMENT_$2,
                            "numberColorMapEntries",
                            Integer.toString(numberColorMapEntries)));
        this.numberColorMapEntries = numberColorMapEntries;
        return this;
    }

    /** @uml.property name="numberOfColorMapElements" */
    private int numberOfColorMapElements = -1;

    /** This is the target object for this builder.* */
    private LinearColorMap colorMap;

    /**
     * Getter of the property <tt>numberOfColorMapElements</tt>
     *
     * @return Returns the numberOfColorMapElements.
     * @uml.property name="numberOfColorMapElements"
     */
    public int getNumberOfColorMapElements() {
        return numberOfColorMapElements;
    }

    /** */
    public LinearColorMap buildLinearColorMap() {
        if (this.numberColorMapEntries == -1)
            throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));

        // /////////////////////////////////////////////////////////////////////
        //
        // Do we already have an object?
        //
        // /////////////////////////////////////////////////////////////////////
        if (this.colorMap != null) return this.colorMap;

        // /////////////////////////////////////////////////////////////////////
        //
        // Create the last category
        //
        // /////////////////////////////////////////////////////////////////////
        LinearColorMapElement last =
                (LinearColorMapElement) this.colormapElements.get(this.colormapElements.size() - 1);
        if (linearColorMapType == ColorMap.TYPE_RAMP) {

            // //
            //
            // Get the previous category
            //
            // //
            final LinearColorMapElement previous = last;

            // //
            //
            // Build the last one
            //
            // //
            last =
                    LinearColorMapElement.create(
                            "ColorMapEntry" + this.colormapElements.size(),
                            lastColorValue,
                            RangeFactory.create(
                                    previous.getRange().getMax().doubleValue(),
                                    true,
                                    Double.POSITIVE_INFINITY,
                                    false),
                            previous.getOutputRange().getMax().intValue());
            this.colormapElements.add(last);
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // Create the list of no data classification domain elements. Note that
        // all of them
        //
        // /////////////////////////////////////////////////////////////////////
        final LinearColorMapElement preservedValuesElement[] =
                new LinearColorMapElement[preservedValues.size()];
        final int value = (int) last.getOutputMaximum() + 1;
        for (int i = 0; i < preservedValuesElement.length; i++) {
            preservedValuesElement[i] =
                    LinearColorMapElement.create(
                            org.geotools.metadata.i18n.Vocabulary.format(
                                            org.geotools.metadata.i18n.VocabularyKeys.NODATA)
                                    + Integer.toString(i + 1),
                            preservedValuesColor,
                            RangeFactory.create(preservedValues.get(i), preservedValues.get(i)),
                            value);
        }

        this.colorMap =
                new LinearColorMap(
                        name,
                        (LinearColorMapElement[])
                                colormapElements.toArray(
                                        new LinearColorMapElement[colormapElements.size()]),
                        preservedValuesElement,
                        this.gapsColor);
        return colorMap;
    }
}
