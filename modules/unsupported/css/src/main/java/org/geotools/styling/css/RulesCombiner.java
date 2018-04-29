/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.Selector;

class RulesCombiner {

    SimplifyingFilterVisitor simplifier;

    List<CssRule> lastRuleSet;

    Selector lastCombinedSelector;

    public RulesCombiner(SimplifyingFilterVisitor simplifier) {
        this.simplifier = simplifier;
    }

    CssRule combineRules(List<CssRule> rules) {
        CssRule combined;
        // build the main rule
        Selector combinedSelector = combineSelectors(rules);

        // apply cascading on properties
        Map<PseudoClass, Map<String, Property>> properties = new LinkedHashMap<>();
        for (CssRule cssRule : rules) {
            for (Map.Entry<PseudoClass, List<Property>> entry :
                    cssRule.getProperties().entrySet()) {
                PseudoClass ps = entry.getKey();
                Map<String, Property> psProperties = properties.get(ps);
                if (psProperties == null) {
                    psProperties = new LinkedHashMap<String, Property>();
                    properties.put(ps, psProperties);
                }
                for (Property p : entry.getValue()) {
                    psProperties.put(p.getName(), p);
                }
                if (ps != PseudoClass.ROOT) {
                    // we also have to fill values for the pseudo classes owned by this one
                    for (PseudoClass containedClass : properties.keySet()) {
                        if (ps.contains(containedClass)) {
                            Map<String, Property> containedProperties =
                                    properties.get(containedClass);
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
            newProperties.put(entry.getKey(), new ArrayList<Property>(entry.getValue().values()));
        }
        String comment = getCombinedComment(rules);
        combined = new CssRule(combinedSelector, newProperties, comment);
        combined.setAncestry(rules);
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

    Selector combineSelectors(List<CssRule> rules) {
        if (rules == lastRuleSet) {
            return lastCombinedSelector;
        }
        Selector s;
        if (rules.size() == 1) {
            s = rules.get(0).getSelector();
        } else {
            s = rules.get(0).getSelector();
            for (int i = 1; i < rules.size() && s != Selector.REJECT; i++) {
                CssRule rule = rules.get(i);
                s = Selector.and(s, rule.getSelector(), simplifier);
            }
        }
        this.lastRuleSet = rules;
        this.lastCombinedSelector = s;
        return s;
    }
}
