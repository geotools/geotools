/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Or;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.util.OgcFilterBuilder;
import org.geotools.styling.css.util.ScaleRangeExtractor;
import org.geotools.styling.css.util.UnboundSimplifyingFilterVisitor;
import org.geotools.util.NumberRange;
import org.geotools.util.Range;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * Represents the current coverage of the scale/filter domain, and has helper methods to add {@link
 * CssRule} to the covereage, split them into subrules by scale ranges, compare them with the
 * existing coverage, and genenerate rules matching only what's left to be covered.
 *
 * @author Andrea Aime - GeoSolutions
 */
class DomainCoverage {

    /** The full range of scales possible. Once this is covered, the whole domain is */
    static final NumberRange<Double> FULL_SCALE_RANGE =
            new NumberRange<>(Double.class, 0d, Double.POSITIVE_INFINITY);

    static final Logger LOGGER = Logging.getLogger(DomainCoverage.class);

    /**
     * A simplified representation of a Selector that takes apart the three main components, scale
     * range, filter and pseudoClass, to make it compatible with the SLD filtering model. A Selector
     * expressed in CSS language can be converted into a list of these.
     *
     * @author Andrea Aime - GeoSolutions
     */
    class SLDSelector {

        NumberRange<Double> scaleRange;

        Filter filter;

        Integer complexity;

        public SLDSelector(NumberRange<?> scaleRange, Filter filter) {
            this.scaleRange =
                    new NumberRange<>(
                            Double.class,
                            scaleRange.getMinimum(),
                            scaleRange.isMinIncluded(),
                            scaleRange.getMaximum(),
                            scaleRange.isMaxIncluded());
            this.filter = filter;
        }

        /**
         * Returns a list of scale dependent filters that represent the difference (the uncovered
         * area) between this {@link SLDSelector} and then specified rule
         */
        public List<SLDSelector> difference(SLDSelector other) {
            List<SLDSelector> result = new ArrayList<>();

            // fast interaction tests
            if (!this.scaleRange.intersects(other.scaleRange)) {
                return Collections.singletonList(this);
            }

            // first case, portions of scale range not overlapping
            NumberRange<?>[] scaleRangeDifferences = this.scaleRange.subtract(other.scaleRange);
            for (NumberRange<?> scaleRangeDifference : scaleRangeDifferences) {
                result.add(new SLDSelector(scaleRangeDifference, this.filter));
            }

            // second case, scale ranges overlapping, but filter/pseudoclass not
            NumberRange<?> scaleRangeIntersection = this.scaleRange.intersect(other.scaleRange);
            if (scaleRangeIntersection != null && !scaleRangeIntersection.isEmpty()) {
                And difference = FF.and(this.filter, FF.not(other.filter));
                Filter simplifiedDifference = simplify(difference);
                if (simplifiedDifference != Filter.EXCLUDE) {
                    result.add(new SLDSelector(scaleRangeIntersection, simplifiedDifference));
                }
            }

            return result;
        }

        @Override
        public String toString() {
            return "SLDSelector [scaleRange=" + scaleRange + ", filter=" + ECQL.toCQL(filter) + "]";
        }

        public Selector toSelector(SimplifyingFilterVisitor visitor) {
            Selector selector = Selector.and(new ScaleRange(scaleRange), new Data(filter), visitor);

            return selector;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((filter == null) ? 0 : filter.hashCode());
            result = prime * result + ((scaleRange == null) ? 0 : scaleRange.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            SLDSelector other = (SLDSelector) obj;
            if (!getOuterType().equals(other.getOuterType())) return false;
            if (filter == null) {
                if (other.filter != null) return false;
            } else if (!filter.equals(other.filter)) return false;
            if (scaleRange == null) {
                if (other.scaleRange != null) return false;
            } else if (!scaleRange.equals(other.scaleRange)) return false;
            return true;
        }

        private DomainCoverage getOuterType() {
            return DomainCoverage.this;
        }

        public int getComplexity() {
            if (complexity == null) {
                FilterComplexityVisitor visitor = new FilterComplexityVisitor();
                this.filter.accept(visitor, null);
                complexity = visitor.count;
            }
            return complexity;
        }
    }

    /**
     * Orders SLDSelector by the scale range (using the minimum value)
     *
     * @author Andrea Aime - GeoSolutions
     */
    private class SLDSelectorComparator implements Comparator<SLDSelector> {

        @Override
        public int compare(SLDSelector o1, SLDSelector o2) {
            NumberRange<Double> sr1 = o1.scaleRange;
            NumberRange<Double> sr2 = o2.scaleRange;
            if (sr1.getMinimum() == sr2.getMinimum()) {
                if (sr1.isMinIncluded()) {
                    return sr2.isMinIncluded() ? 0 : -1;
                } else {
                    return sr2.isMinIncluded() ? 1 : 0;
                }

            } else {
                return sr1.getMinimum() > sr2.getMinimum() ? 1 : -1;
            }
        }
    }

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    /** The current domain coverage */
    private List<SLDSelector> elements;

    /** The target feature type for this domain coverage computation */
    private FeatureType targetFeatureType;

    /**
     * A simplifier visitor that will cache results that have been simplified already, since this
     * class unites/intersects filters a lot in order to compute the coverage
     */
    private UnboundSimplifyingFilterVisitor simplifier;

    /**
     * The set of selectors generated so far. We can get several repeated selectors due to
     * conditional pseudo-classes, but only the first one will be not covered
     */
    Set<SLDSelector> generatedSelectors = new HashSet<>();

    /** When true, the detailed (expensive) coverage computation will generate exclusive rules */
    boolean exclusiveRulesEnabled = true;

    /**
     * If the threshold is set, switches out of exclusive mode once the total complexity of the
     * coverage goes beyond the threshold.
     */
    int complexityThreshold = 0;

    /** Create a new domain coverage for the given feature type */
    public DomainCoverage(
            FeatureType targetFeatureType, UnboundSimplifyingFilterVisitor simplifier) {
        this.elements = new ArrayList<>();
        this.targetFeatureType = targetFeatureType;
        this.simplifier = simplifier;
    }

    /**
     * Adds a rule to the domain, and returns a list of rules representing bits of the domain that
     * were still not covered by the previous rules
     */
    public List<CssRule> addRule(CssRule rule) {
        Selector selector = rule.getSelector();

        // turns the rule in a set of domain coverage expressions (simplified selectors)
        List<SLDSelector> ruleCoverage =
                toSLDSelectors(selector, targetFeatureType).stream()
                        .filter(s -> !generatedSelectors.contains(s))
                        .collect(Collectors.toList());
        if (ruleCoverage.isEmpty()) {
            return Collections.emptyList();
        } else {
            generatedSelectors.addAll(ruleCoverage);
        }

        if (exclusiveRulesEnabled && complexityThreshold > 0) {
            final int totalComplexity = getTotalComplexity();
            if (totalComplexity > complexityThreshold) {
                LOGGER.log(
                        Level.INFO,
                        "Switching CSS translation to non exclusive mode as total "
                                + "domain coverage complexity {0} went above threshold {1}",
                        new Object[] {totalComplexity, complexityThreshold});
                exclusiveRulesEnabled = false;
            }
        }

        // if we are just checking for straight duplicates, let it go
        if (!exclusiveRulesEnabled) {
            return coverageToRules(rule, ruleCoverage);
        }

        // for each rule we have in the domain, get the differences, if any, with this rule,
        // emit them as derived rules, and increase the coverage
        if (elements.isEmpty()) {
            elements.addAll(ruleCoverage);
            return coverageToRules(rule, ruleCoverage);
        } else {
            List<SLDSelector> reducedCoverage = new ArrayList<>(ruleCoverage);
            for (SLDSelector element : elements) {
                List<SLDSelector> difference = new ArrayList<>();
                for (SLDSelector rc : reducedCoverage) {
                    List<SLDSelector> ruleDifference = rc.difference(element);
                    difference.addAll(ruleDifference);
                }
                reducedCoverage = difference;
                if (reducedCoverage.isEmpty()) {
                    break;
                }
            }

            if (!reducedCoverage.isEmpty()) {
                List<CssRule> derivedRules = new ArrayList<>();
                reducedCoverage = combineSLDSelectors(reducedCoverage);
                for (SLDSelector rc : reducedCoverage) {
                    derivedRules.add(
                            new CssRule(
                                    rc.toSelector(simplifier),
                                    rule.getProperties(),
                                    rule.getComment()));
                }

                elements.addAll(reducedCoverage);

                // so far, this sorting done just for the sake of readability during debugging
                elements = combineSLDSelectors(elements);
                return derivedRules;
            } else {
                return Collections.emptyList();
            }
        }
    }

    private List<SLDSelector> combineSLDSelectors(List<SLDSelector> elements) {
        Collections.sort(elements, new SLDSelectorComparator());
        List<SLDSelector> combined = new ArrayList<>();
        SLDSelector prev = null;
        for (SLDSelector ss : elements) {
            if (prev == null) {
                prev = ss;
            } else if (prev.scaleRange.equals(ss.scaleRange)) {
                org.opengis.filter.Or or = FF.or(ss.filter, prev.filter);
                Filter simplified = simplify(or);
                prev = new SLDSelector(prev.scaleRange, simplified);
            } else if (prev.scaleRange.getMaximum() == ss.scaleRange.getMinimum()
                    && prev.filter.equals(ss.filter)) {
                NumberRange combinedRange =
                        new NumberRange<>(
                                Double.class,
                                prev.scaleRange.getMinimum(),
                                prev.scaleRange.isMinIncluded(),
                                ss.scaleRange.getMaximum(),
                                ss.scaleRange.isMaxIncluded());
                prev = new SLDSelector(combinedRange, prev.filter);
            } else {
                combined.add(prev);
                prev = ss;
            }
        }
        if (prev != null) {
            combined.add(prev);
        }
        return combined;
    }

    private int getTotalComplexity() {
        int total = 0;
        for (SLDSelector selector : elements) {
            total += selector.getComplexity();
        }
        return total;
    }

    private List<CssRule> coverageToRules(CssRule rule, List<SLDSelector> ruleCoverage) {

        List<CssRule> result = new ArrayList<>();
        for (SLDSelector ss : ruleCoverage) {
            result.add(
                    new CssRule(
                            ss.toSelector(simplifier), rule.getProperties(), rule.getComment()));
        }

        return result;
    }

    /** Turns the specified selector into a list of "standardized" SLDSelector */
    List<SLDSelector> toSLDSelectors(Selector selector, FeatureType targetFeatureType) {
        List<SLDSelector> result = new ArrayList<>();
        if (selector instanceof Or) {
            Or or = (Or) selector;
            for (Selector s : or.getChildren()) {
                if (s instanceof Or) {
                    throw new IllegalArgumentException(
                            "Unexpected or selector nested inside another one, "
                                    + "at this point they should have been all flattened: "
                                    + selector);
                }
                toIndependentSLDSelectors(s, targetFeatureType, result);
            }
        } else {
            toIndependentSLDSelectors(selector, targetFeatureType, result);
        }

        return result;
    }

    /**
     * Flattens a single SLD selector into a list of {@link SLDSelector}, adding them into the
     * scaleDependentFilters list
     */
    private void toIndependentSLDSelectors(
            Selector selector,
            FeatureType targetFeatureType,
            List<SLDSelector> scaleDependentFilters) {
        Range<Double> range = ScaleRangeExtractor.getScaleRange(selector);
        if (range == null) {
            range = FULL_SCALE_RANGE;
        }
        Filter filter = OgcFilterBuilder.buildFilter(selector, targetFeatureType);
        boolean merged = false;
        for (SLDSelector existing : scaleDependentFilters) {
            if (existing.scaleRange.equals(range)) {
                if (existing.filter instanceof org.opengis.filter.Or) {
                    org.opengis.filter.Or or = (org.opengis.filter.Or) existing.filter;
                    List<Filter> children = new ArrayList<>(or.getChildren());
                    children.add(filter);
                    existing.filter = simplify(FF.or(children));
                } else {
                    existing.filter = simplify(FF.or(existing.filter, filter));
                }
                merged = true;
                break;
            }
        }
        if (!merged) {
            scaleDependentFilters.add(new SLDSelector(new NumberRange<>(range), filter));
        }
    }

    /**
     * Simplifies a filter via the simplifying filter visitor, taking into account the target
     * feature type
     */
    Filter simplify(Filter filter) {
        return (Filter) filter.accept(simplifier, null);
    }

    @Override
    public String toString() {
        StringBuilder sb =
                new StringBuilder("DomainCoverage[items=").append(elements.size()).append(",\n");
        for (SLDSelector selector : elements) {
            sb.append(selector).append("\n");
        }
        sb.append("] // DomainCoverage end");
        return sb.toString();
    }

    void setExclusiveRulesEnabled(boolean exclusiveRulesEnabled) {
        this.exclusiveRulesEnabled = exclusiveRulesEnabled;
    }
}
