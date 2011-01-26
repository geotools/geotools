/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage;

import java.awt.Color;

import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.util.NumberRange;


/**
 * A "geophysics" view of a category. Sample values in this category are equal to geophysics
 * values.   By definition, the {@link #getSampleToGeophysics} method for this class returns
 * the identity transform, or {@code null} if this category is a qualitative one.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class GeophysicsCategory extends Category {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7164422654831370784L;

    /**
     * Creates a new instance of geophysics category.
     *
     * @param  inverse The originating {@link Category}.
     * @param  isQuantitative {@code true} if the originating category is quantitative.
     * @throws TransformException if a transformation failed.
     */
    GeophysicsCategory(Category inverse, boolean isQuantitative) throws TransformException {
        super(inverse, isQuantitative);
    }

    /**
     * Returns the category name localized in the specified locale.
     */
    @Override
    public InternationalString getName() {
        assert !(inverse instanceof GeophysicsCategory);
        return inverse.getName();
    }

    /**
     * Returns the set of colors for this category.
     */
    @Override
    public Color[] getColors() {
        assert !(inverse instanceof GeophysicsCategory);
        return inverse.getColors();
    }

    /**
     * Returns the range of geophysics value.
     *
     * @return The range of geophysics values.
     * @throws IllegalStateException if sample values can't be transformed into geophysics values.
     *
     * @todo The algorithm for finding minimum and maximum values is very simple for
     *       now and will not work if the transformation has local extremas. We would
     *       need some more sophesticated algorithm for the most general cases. Such
     *       a general algorithm would be usefull in the super-class constructor as well.
     */
    @Override
    public NumberRange<? extends Number> getRange() throws IllegalStateException {
        if (range == null) try {
            final MathTransform1D tr = inverse.transform;
            final NumberRange<? extends Number> r = inverse.range;
            boolean minIncluded = r.isMinIncluded();
            boolean maxIncluded = r.isMaxIncluded();
            double min  = tr.transform(r.getMinimum());
            double max  = tr.transform(r.getMaximum());
            double min2 = tr.transform(r.getMinimum(!minIncluded));
            double max2 = tr.transform(r.getMaximum(!maxIncluded));
            if ((minIncluded ? min2 : min) > (maxIncluded ? max2 : max)) {
                final double  tmp, tmp2;
                final boolean tmpIncluded;
                tmp=min;   tmp2=min2;  tmpIncluded=minIncluded;
                min=max;   min2=max2;  minIncluded=maxIncluded;
                max=tmp;   max2=tmp2;  maxIncluded=tmpIncluded;
            }
            assert Double.doubleToLongBits(minimum) == Double.doubleToLongBits(minIncluded ? min : min2);
            assert Double.doubleToLongBits(maximum) == Double.doubleToLongBits(maxIncluded ? max : max2);
            range = new Range(min, minIncluded, max, maxIncluded, min2, max2);

        } catch (TransformException cause) {
            throw new IllegalStateException(Errors.format(ErrorKeys.BAD_TRANSFORM_$1,
                    Classes.getClass(inverse.transform)), cause);
        }
        return range;
    }

    /**
     * Returns a transform from sample values to geophysics values, which (by definition)
     * is an identity transformation. If this category is not a quantitative one, then
     * this method returns {@code null}.
     */
    @Override
    public MathTransform1D getSampleToGeophysics() {
        return isQuantitative() ? LinearTransform1D.IDENTITY : null;
    }

    /**
     * Returns {@code true} if this category is quantitative.
     */
    @Override
    public boolean isQuantitative() {
        assert !(inverse instanceof GeophysicsCategory) : inverse;
        return inverse.isQuantitative();
    }

    /**
     * Returns a new category for the same range of sample values but a different color palette.
     */
    @Override
    public Category recolor(final Color[] colors) {
        assert !(inverse instanceof GeophysicsCategory) : inverse;
        return inverse.recolor(colors).inverse;
    }

    /**
     * Changes the mapping from sample to geophysics values.
     */
    @Override
    public Category rescale(MathTransform1D sampleToGeophysics) {
        if (sampleToGeophysics.isIdentity()) {
            return this;
        }
        sampleToGeophysics = (MathTransform1D) ConcatenatedTransform.create(
                             inverse.getSampleToGeophysics(), sampleToGeophysics);
        return inverse.rescale(sampleToGeophysics).inverse;
    }

    /**
     * If {@code false}, returns a category with the original sample values.
     */
    @Override
    public Category geophysics(final boolean geo) {
        // Assertion below is for preventing recursive invocation.
        assert !(inverse instanceof GeophysicsCategory) : inverse;
        return inverse.geophysics(geo);
    }




    /**
     * Range of geophysics values computed from the range of the {@linkplain #inverse indexed
     * category}. The {@code inverse.transform} transformation is used for computing the
     * inclusive and exclusive minimum and maximum values of this range.  We compute both the
     * inclusive and exclusive values because we can't rely on the default implementation, which
     * looks for the nearest representable number. For example is the range of index values is 0
     * to 10 exclusive (or 0 to 9 inclusive) and the scale is 2, then the range of geophysics
     * values is 0 to 20 exclusive or 0 to 18 inclusive, not 0 to 19.9999... The numbers between
     * 18 and 20 is a "gray area" where we don't know for sure what the user intend to do.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     *
     * @see GeophysicsCategory#getRange
     */
    private static final class Range extends NumberRange<Double> {
        /**
         * Serial number for interoperability with different versions.
         */
        private static final long serialVersionUID = -1416908614729956928L;

        /**
         * The minimal value to be returned by {@link #getMinimum(boolean)} when the
         * {@code inclusive} flag is the opposite of {@link #isMinIncluded()}.
         */
        private final double minimum2;

        /**
         * The maximal value to be returned by {@link #getMaximum(boolean)} when the
         * {@code inclusive} flag is the opposite of {@link #isMaxIncluded()}.
         */
        private final double maximum2;

        /**
         * Constructs a range of {@code double} values.
         */
        public Range(final double minimum,  final boolean isMinIncluded,
                     final double maximum,  final boolean isMaxIncluded,
                     final double minimum2, final double  maximum2)
        {
            super(NumberRange.create(minimum, isMinIncluded, maximum, isMaxIncluded));
            this.minimum2 = minimum2;
            this.maximum2 = maximum2;
        }

        /**
         * Returns the minimum value with the specified inclusive or exclusive state.
         */
        @Override
        public double getMinimum(final boolean inclusive) {
            return (inclusive == isMinIncluded()) ? getMinimum() : minimum2;
        }

        /**
         * Returns the maximum value with the specified inclusive or exclusive state.
         */
        @Override
        public double getMaximum(final boolean inclusive) {
            return (inclusive == isMaxIncluded()) ? getMaximum() : maximum2;
        }
    }
}
