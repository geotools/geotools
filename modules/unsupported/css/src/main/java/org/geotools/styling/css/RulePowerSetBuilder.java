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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.util.FilteredPowerSetBuilder;
import org.geotools.util.logging.Logging;

/**
 * Gives a list of Rules, it builds their power set, making it so that any set of rules extracted as
 * at least a chance to match a feature (e.g., the rule selectors are not contractiding each other)
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public class RulePowerSetBuilder extends FilteredPowerSetBuilder<CssRule, CssRule> {

    static final Logger LOGGER = Logging.getLogger(RulePowerSetBuilder.class);

    List<CssRule> lastRuleSet;

    Selector lastCombinedSelector;

    int maxCombinations = -1;

    int count = 0;

    public RulePowerSetBuilder(List<CssRule> domain) {
        super(sortAscendingSpecificity(domain));
    }

    public RulePowerSetBuilder(List<CssRule> domain, int maxCombinations) {
        super(sortAscendingSpecificity(domain));
        this.maxCombinations = maxCombinations;
    }

    private static List<CssRule> sortAscendingSpecificity(List<CssRule> rules) {
        List<CssRule> copy = new ArrayList<>(rules);
        Collections.sort(copy, new CssRuleComparator());
        return copy;
    }

    @Override
    protected CssRule buildResult(List<CssRule> rules) {
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
            Selector combinedSelector = combineSelectors(rules);

            // apply cascading on properties
            Map<PseudoClass, Map<String, Property>> properties = new LinkedHashMap<>();
            for (CssRule cssRule : rules) {
                for (Map.Entry<PseudoClass, List<Property>> entry : cssRule.getProperties()
                        .entrySet()) {
                    PseudoClass ps = entry.getKey();
                    Map<String, Property> psProperties = properties.get(ps);
                    if (psProperties == null) {
                        psProperties = new HashMap<String, Property>();
                        properties.put(ps, psProperties);
                    }
                    for (Property p : entry.getValue()) {
                        psProperties.put(p.getName(), p);
                    }
                    if (ps != PseudoClass.ROOT) {
                        // we also have to fill values for the pseudo classes owned by this one
                        for (PseudoClass containedClass : properties.keySet()) {
                            if (ps.contains(containedClass)) {
                                Map<String, Property> containedProperties = properties
                                        .get(containedClass);
                                for (Property p : entry.getValue()) {
                                    containedProperties.put(p.getName(), p);
                                }
                            }
                        }
                    }
                }
            }

            // build the new rule
            Map<PseudoClass, List<Property>> newProperties = new LinkedHashMap<>();
            for (Map.Entry<PseudoClass, Map<String, Property>> entry : properties.entrySet()) {
                newProperties.put(entry.getKey(),
                        new ArrayList<Property>(entry.getValue().values()));
            }
            String comment = getCombinedComment(rules);
            CssRule result = new CssRule(combinedSelector, newProperties, comment);
            result.setAncestry(rules);
            combined = result;
        }

        // make sure we're not going beyond the max generated rules
        if (maxCombinations > 0 && count++ > maxCombinations) {
            LOGGER.severe("Bailing out, the CSS rule combinations have already generated more than "
                    + "maxCombinations SLD rules, giving up. Please simplify your CSS style");
        } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("New rule: " + combined);
        }

        return combined;
    }

    private String getCombinedComment(List<CssRule> rules) {
        StringBuilder sb = new StringBuilder();
        for (CssRule rule : rules) {
            if (rule.getComment() != null) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(rule.getComment());
            }
        }

        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return null;
        }
    }

    @Override
    protected boolean accept(List<CssRule> rules) {
        if (count > maxCombinations) {
            return false;
        }
        Selector combined = combineSelectors(rules);
        return combined != Selector.REJECT;
    }

    Selector combineSelectors(List<CssRule> rules) {
        if (rules == lastCombinedSelector) {
            return lastCombinedSelector;
        }
        Selector s;
        if (rules.size() == 1) {
            s = rules.get(0).getSelector();
        } else {
            s = rules.get(0).getSelector();
            for (int i = 1; i < rules.size() && s != Selector.REJECT; i++) {
                CssRule rule = rules.get(i);
                s = Selector.and(s, rule.getSelector());
            }
        }
        this.lastRuleSet = rules;
        this.lastCombinedSelector = s;
        return s;
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
        Collections.sort(filtered, Collections.reverseOrder(new CssRuleComparator()));

        // we can have rules with the same selector, generated when combining a rule with *,
        // fold them if they are covering each other (and they should)
        List<CssRule> folded = new ArrayList<>();
        CssRule prev = filtered.get(0);
        for (int i = 1; i < filtered.size(); i++) {
            CssRule curr = filtered.get(i);
            if (curr.covers(prev)) {
                prev = curr;
            } else if (prev.covers(curr)) {
                // nothing to do, skip it
            } else {
                folded.add(prev);
                prev = curr;
            }
        }
        folded.add(prev);
        return folded;
    }

}
