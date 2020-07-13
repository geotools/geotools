/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.AbstractCalcResult;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.util.Converters;
import org.geotools.util.DateRange;
import org.geotools.util.Range;
import org.geotools.util.Utilities;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Generates a list of NumberRanges from a collection
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
class RangeVisitor implements FeatureCalc {

    enum RangeType {
        NUMBER,
        DATE;
    }

    /** A DateRange comparator */
    class DateRangeComparator implements Comparator<DateRange> {

        @Override
        public int compare(DateRange firstDateRange, DateRange secondDateRange) {
            Utilities.ensureNonNull("firstDateRange", firstDateRange);
            Utilities.ensureNonNull("secondDateRange", secondDateRange);
            final long beginFirst = firstDateRange.getMinValue().getTime();
            final long endFirst = firstDateRange.getMaxValue().getTime();
            final long beginSecond = secondDateRange.getMinValue().getTime();
            final long endSecond = secondDateRange.getMaxValue().getTime();
            return NumberRangeComparator.doubleCompare(
                    beginFirst, endFirst, beginSecond, endSecond);
        }
    }

    /** A NumberRange comparator */
    static class NumberRangeComparator implements Comparator<Range<? extends Number>> {

        @Override
        public int compare(
                Range<? extends Number> firstRange, Range<? extends Number> secondRange) {
            Utilities.ensureNonNull("firstRange", firstRange);
            Utilities.ensureNonNull("secondRange", secondRange);
            final Number firstRangeMin = firstRange.getMinValue();
            final Number firstRangeMax = firstRange.getMaxValue();
            final Number secondRangeMin = secondRange.getMinValue();
            final Number secondRangeMax = secondRange.getMaxValue();
            return doubleCompare(
                    firstRangeMin.doubleValue(),
                    firstRangeMax.doubleValue(),
                    secondRangeMin.doubleValue(),
                    secondRangeMax.doubleValue());
        }

        /**
         * Given a set of 4 double representing the extrema of 2 ranges, compare the 2 ranges.
         *
         * @param firstRangeMin the min value of the first range
         * @param firstRangeMax the max value of the first range
         * @param secondRangeMin the min value of the second range
         * @param secondRangeMax the max value of the second range
         * @return TODO: Improve that logic to deal with special cases on intervals management
         */
        public static int doubleCompare(
                final double firstRangeMin,
                final double firstRangeMax,
                final double secondRangeMin,
                final double secondRangeMax) {
            if (firstRangeMin == secondRangeMin && firstRangeMax == secondRangeMax) {
                return 0;
            }
            if (firstRangeMin > secondRangeMin) {
                if (firstRangeMax > secondRangeMax) {
                    return +2;
                } else {
                    return +1;
                }
            } else {
                if (firstRangeMax <= secondRangeMax) {
                    return -2;
                } else {
                    return -1;
                }
            }
        }
    }

    /** expression related to the first attribute to be evaluated */
    protected Expression expr1;

    /** expression related to the second attribute to be evaluated */
    protected Expression expr2;

    /** The comparator instance to sort items inside the Tree set */
    private Comparator comparator;

    /** The set containing the added ranges */
    Set<Range> set = null;

    /**
     * A set of string representations of the returned ranges. Time ranges will be returned into a
     * compact form so that intersecting ranges are merged together into a bigger time range.
     */
    Set<String> minimalRanges = null;

    public RangeVisitor(String attributeTypeName1, String attributeTypeName2) {
        this(attributeTypeName1, attributeTypeName2, RangeType.NUMBER);
    }

    /**
     * Range visitor constructor.
     *
     * @param attributeTypeName1 the name of the attribute to be related to the left side of the
     *     range
     * @param attributeTypeName2 the name of the attribute to be related to the right side of the
     *     range
     * @param rangeType the type of range, one of {@link RangeType#NUMBER},{@link RangeType#DATE}
     */
    public RangeVisitor(String attributeTypeName1, String attributeTypeName2, RangeType rangeType) {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr1 = factory.property(attributeTypeName1);
        expr2 = factory.property(attributeTypeName2);
        this.comparator =
                rangeType == RangeType.NUMBER
                        ? new NumberRangeComparator()
                        : new DateRangeComparator();
        set = new TreeSet(comparator);
    }

    public void init(SimpleFeatureCollection collection) {
        // do nothing
    }

    public void visit(SimpleFeature feature) {
        visit((Feature) feature);
    }

    public void visit(Feature feature) {
        // we ignore null attributes
        final Object firstValue = expr1.evaluate(feature);
        final Object secondValue = expr2.evaluate(feature);
        if (firstValue != null && secondValue != null) {
            set.add(Utils.createRange(firstValue, secondValue));
        }
    }

    public void setValue(Object newSet) {

        if (newSet instanceof Collection) { // convert to set
            this.set = new HashSet((Collection) newSet);
        } else {
            Collection collection = Converters.convert(newSet, List.class);
            if (collection != null) {
                this.set = new HashSet(collection);
            } else {
                this.set = new HashSet(Collections.singleton(newSet));
            }
        }
    }

    /** Reset the collected ranges */
    public void reset() {
        this.set = new TreeSet(comparator);
        this.minimalRanges = null;
    }

    /** Return the minimal set of Ranges */
    public Set<String> getRange() {
        if (minimalRanges == null) {
            minimalRanges = new LinkedHashSet<String>();
            populateRange();
        }
        return minimalRanges;
    }

    /** Populate the set of minimal ranges as a set of Strings */
    protected void populateRange() {
        Iterator<Range> iterator = set.iterator();
        while (iterator.hasNext()) {
            Range range = iterator.next();
            minimalRanges.add((range.getMinValue() + "/" + range.getMaxValue()));
        }
    }

    public CalcResult getResult() {
        if (set.size() < 1) {
            return CalcResult.NULL_RESULT;
        }
        return new RangeResult(set);
    }

    static class RangeResult extends AbstractCalcResult {
        private Set ranges;

        public RangeResult(Set newSet) {
            ranges = newSet;
        }

        public Object getValue() {
            return new HashSet(ranges);
        }

        public boolean isCompatible(CalcResult targetResults) {
            // list each calculation result which can merge with this type of result
            if (targetResults instanceof RangeResult || targetResults == CalcResult.NULL_RESULT)
                return true;
            return false;
        }

        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException("Parameter is not a compatible type");
            }

            if (resultsToAdd == CalcResult.NULL_RESULT) {
                return this;
            }

            if (resultsToAdd instanceof RangeResult) {
                // add one set to the other (to create one big unique list)
                Set newSet = new HashSet(ranges);
                newSet.addAll((Set) resultsToAdd.getValue());
                return new RangeResult(newSet);
            } else {
                throw new IllegalArgumentException(
                        "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}
