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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.styling.css.selector.Accept;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.util.FilteredPowerSetBuilder;
import org.geotools.styling.css.util.PseudoClassRemover;
import org.geotools.styling.css.util.UnboundSimplifyingFilterVisitor;
import org.geotools.util.logging.Logging;

/**
 * Gives a list of Rules, it builds their power set, making it so that any set of rules extracted as
 * at least a chance to match a feature (e.g., the rule selectors are not contractiding each other)
 *
 * @author Andrea Aime - GeoSolutions
 */
class RulePowerSetBuilder extends FilteredPowerSetBuilder<CssRule, CssRule> {

    static final Logger LOGGER = Logging.getLogger(RulePowerSetBuilder.class);

    RulesCombiner combiner;

    int maxCombinations = -1;

    int count = 0;

    UnboundSimplifyingFilterVisitor simplifier;

    /**
     * These are pseudo class bits that mix in the main rule set, or not, depending on whether their
     * pseudo class is a match. They are treated separately to reduce the number of rules the power
     * set generates
     */
    List<CssRule> mixins;

    private static List[] classifyRules(List<CssRule> domain) {
        List<CssRule> main = new ArrayList<>();
        List<CssRule> mixins = new ArrayList<>();
        for (CssRule rule : domain) {
            if (rule.getProperties().get(PseudoClass.ROOT) == null) {
                Selector simplified = (Selector) rule.selector.accept(new PseudoClassRemover());
                rule = new CssRule(simplified, rule.properties, rule.comment);
                mixins.add(rule);
            } else {
                main.add(rule);
            }
        }
        Collections.sort(main, CssRuleComparator.ASCENDING);
        Collections.sort(mixins, CssRuleComparator.ASCENDING);

        return new List[] {main, mixins};
    }

    public RulePowerSetBuilder(List<CssRule> domain, UnboundSimplifyingFilterVisitor simplifier) {
        this(classifyRules(domain), simplifier, -1);
    }

    RulePowerSetBuilder(
            List<CssRule> domain, UnboundSimplifyingFilterVisitor simplifier, int maxCombinations) {
        this(classifyRules(domain), simplifier, maxCombinations);
    }

    protected RulePowerSetBuilder(
            List[] domainMixins, UnboundSimplifyingFilterVisitor simplifier, int maxCombinations) {
        super(domainMixins[0]);
        this.mixins = domainMixins[1];
        this.maxCombinations = maxCombinations;
        this.simplifier = simplifier;
        this.combiner = new RulesCombiner(simplifier);
    }

    @Override
    protected List<CssRule> buildResult(List<CssRule> rules) {
        boolean foundSymbolizerProperty = false;
        for (CssRule rule : rules) {
            if (rule.hasSymbolizerProperty()) {
                foundSymbolizerProperty = true;
                break;
            }
        }
        // if none of the source rules has a symbolizer property, there is nothing to combine,
        // the cascade still won't generate any output
        if (!foundSymbolizerProperty) {
            return null;
        }

        CssRule combined;
        if (rules.size() == 1) {
            combined = rules.get(0);
        } else {
            combined = combiner.combineRules(rules);
        }

        // do we have mixins to consider now?
        List<CssRule> results = new ArrayList<>();
        if (mixins == null || mixins.size() == 0) {
            results.add(combined);
        } else {
            List<CssRule> applicableMixins = getApplicableMixins(combined);

            if (applicableMixins.size() > 0) {
                int idx = 0;

                // let's see if all mixins are applying without conditinos
                for (; idx < applicableMixins.size(); idx++) {
                    CssRule mixin = applicableMixins.get(idx);
                    Selector mixedSelector =
                            Selector.and(mixin.selector, combined.selector, simplifier);
                    if (mixedSelector == Selector.REJECT) {
                        // this mixin is no good
                        continue;
                    } else if (mixedSelector.equals(combined.selector)) {
                        // this mixin always applies
                        combined = combiner.combineRules(Arrays.asList(combined, mixin));
                    } else {
                        break;
                    }
                }

                // in this case we stumbled into a mixin that adds a condition to the selector,
                // from here on we have to perform another power set expansion with the remaining
                // mixins
                if (idx < applicableMixins.size()) {
                    List<CssRule> list = new ArrayList<>();
                    list.add(combined);
                    list.addAll(applicableMixins.subList(idx, applicableMixins.size()));
                    RulePowerSetBuilder builder =
                            new RulePowerSetBuilder(
                                    new List[] {list, Collections.emptyList()},
                                    simplifier,
                                    maxCombinations - count);
                    List<CssRule> conditionalPowerSet = builder.buildPowerSet();
                    results.addAll(conditionalPowerSet);
                } else {
                    results.add(combined);
                }

            } else {
                results.add(combined);
            }
        }

        // make sure we're not going beyond the max generated rules
        count += results.size();
        if (maxCombinations > 0 && count > maxCombinations) {
            LOGGER.severe(
                    "Bailing out, the CSS rule combinations have already generated more than "
                            + "maxCombinations SLD rules, giving up. Please simplify your CSS style");
        } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("New rule (" + count + "):" + combined);
        }

        return results;
    }

    /**
     * Returns all the mixins that can be combined with the rule at hand, that is, mixins that have
     * their pseudo-classes matched by the main rule symbolizers. Two lists will be returned, an
     * in-conditional one, where the mixins just blend into the main rule, and a conditional one,
     * where the mixin adds its own conditions, and thus require its own power set expansion
     */
    private List<CssRule> getApplicableMixins(CssRule rule) {
        Set<PseudoClass> mixablePseudoClasses = rule.getMixablePseudoClasses();

        List<CssRule> result = new ArrayList<>();
        for (CssRule mixin : mixins) {
            Set<PseudoClass> pseudoClasses = mixin.properties.keySet();
            // scroll to avoid building extra sets
            boolean found = false;
            for (PseudoClass pseudoClass : pseudoClasses) {
                if (mixablePseudoClasses.contains(pseudoClass)) {
                    found = true;
                    break;
                }
            }

            if (found && acceptMixinCssRule(rule, mixin)) {
                result.add(mixin);
            }
        }

        return result;
    }

    /** Filter applicable mixin rules. Defaults to accepting all rules. */
    protected boolean acceptMixinCssRule(CssRule rule, CssRule mixinRule) {
        return true;
    }

    @Override
    protected boolean accept(List<CssRule> rules) {
        if (count > maxCombinations) {
            return false;
        }
        Selector combined = combiner.combineSelectors(rules);
        return combined != Selector.REJECT;
    }

    @Override
    protected List<CssRule> postFilterResult(List<CssRule> result) {
        List<CssRule> filtered = new ArrayList<>();
        // first remove the rules that have no root pseudo selector, they are contributing nothing
        for (CssRule cssRule : result) {
            if (cssRule.getProperties().get(PseudoClass.ROOT) != null) {
                filtered.add(cssRule);
            }
        }
        if (filtered.size() <= 1) {
            return filtered;
        }

        // sort by selectivity
        Collections.sort(filtered, CssRuleComparator.DESCENDING);

        return filtered;
    }

    @Override
    protected boolean isInclude(CssRule t) {
        return t.getSelector() instanceof Accept;
    }
}
