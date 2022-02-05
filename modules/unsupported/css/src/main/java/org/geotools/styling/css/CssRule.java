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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.styling.Style;
import org.geotools.styling.css.Value.Function;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.util.PseudoClassExtractor;
import org.geotools.styling.css.util.PseudoClassRemover;
import org.geotools.util.Converters;

/**
 * A rule in CSS
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CssRule {

    public static final Integer NO_Z_INDEX = null;

    enum ZIndexMode {
        NoZIndexAll,
        NoZIndexZero;
    }

    Selector selector;

    Map<PseudoClass, List<Property>> properties;

    String comment;

    List<CssRule> ancestry;

    List<CssRule> nestedRules;

    /**
     * Builds a CSS rule
     *
     * @param selector The rule selector
     * @param properties The set of rule properties
     */
    public CssRule(Selector selector, List<Property> properties) {
        super();
        this.setSelector(selector);
        PseudoClassExtractor extractor = new PseudoClassExtractor();
        selector.accept(extractor);
        this.setProperties(new HashMap<>());
        Set<PseudoClass> pseudoClasses = extractor.getPseudoClasses();
        for (PseudoClass ps : pseudoClasses) {
            this.getProperties().put(ps, properties);
        }
    }

    /**
     * Builds a CSS rule
     *
     * @param selector The rule selector
     * @param properties The set of rule properties
     * @param comment The rule comment (can be used to generate SLD's title and abstract
     */
    public CssRule(Selector selector, List<Property> properties, String comment) {
        this(selector, properties);
        this.setComment(comment);
    }

    /**
     * Builds a CSS rule
     *
     * @param selector The rule selector
     * @param properties The set of rule properties, already organized by pseudo-selector
     * @param comment The rule comment (can be used to generate SLD's title and abstract
     */
    CssRule(Selector selector, Map<PseudoClass, List<Property>> properties, String comment) {
        this.setSelector(selector);
        this.setProperties(properties);
        this.setComment(comment);
    }

    @Override
    public String toString() {
        String base =
                "Rule [\n    selector="
                        + getSelector()
                        + ",\n    properties="
                        + getProperties()
                        + "]";
        return base;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getProperties() == null) ? 0 : getProperties().hashCode());
        result = prime * result + ((getSelector() == null) ? 0 : getSelector().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CssRule other = (CssRule) obj;
        if (getComment() == null) {
            if (other.getComment() != null) return false;
        } else if (!getComment().equals(other.getComment())) return false;
        if (getProperties() == null) {
            if (other.getProperties() != null) return false;
        } else if (!getProperties().equals(other.getProperties())) return false;
        if (getSelector() == null) {
            if (other.getSelector() != null) return false;
        } else if (!getSelector().equals(other.getSelector())) return false;
        return true;
    }

    /**
     * Returns the property values by pseudo-class, matching those that satisfy the specified name
     * prefixes
     */
    public Map<String, List<Value>> getPropertyValues(
            PseudoClass pseudoClass, String... symbolizerPrefixes) {
        List<Property> psProperties = getProperties().get(pseudoClass);
        if (psProperties == null) {
            return Collections.emptyMap();
        }
        Map<String, List<Value>> result = new LinkedHashMap<>();
        if (symbolizerPrefixes != null && symbolizerPrefixes.length > 0) {
            for (Property property : psProperties) {
                for (String symbolizerPrefix : symbolizerPrefixes) {
                    if (symbolizerPrefix == null
                            || property.getName().startsWith(symbolizerPrefix)
                            || property.getName().startsWith("-gt-" + symbolizerPrefix)) {
                        result.put(property.getName(), property.getValues());
                    }
                }
            }
        } else {
            for (Property property : psProperties) {
                result.put(property.getName(), property.getValues());
            }
        }

        return result;
    }

    /** Returns true if the rule has any property for the given name, in the give pseudo-class */
    public boolean hasProperty(PseudoClass pseudoClass, String propertyName) {
        List<Property> psProperties = getProperties().get(pseudoClass);
        if (psProperties == null) {
            return false;
        }
        for (Property property : psProperties) {
            if (propertyName.equals(property.getName()) && property.hasValues()) {
                return true;
            }
        }

        return false;
    }

    /** Returns the property with a given name (will look for an exact match) */
    public Property getProperty(PseudoClass pseudoClass, String propertyName) {
        List<Property> psProperties = getProperties().get(pseudoClass);
        if (psProperties == null) {
            return null;
        }
        for (Property property : psProperties) {
            if (propertyName.equals(property.getName())) {
                return property;
            }
        }

        return null;
    }

    /**
     * Returns true if any of the "vendor" properties specified is found in the given pseudo-class
     */
    public boolean hasAnyVendorProperty(PseudoClass pseudoClass, Collection<String> propertyNames) {
        List<Property> psProperties = getProperties().get(pseudoClass);
        if (psProperties == null) {
            return false;
        }
        for (Property property : psProperties) {
            String name = property.getName();
            if (name.startsWith("-gt-")) {
                name = name.substring(4);
            }
            if (propertyNames.contains(name)) {
                return true;
            }
        }

        return false;
    }

    /** Returns true if any of the properties specified is found in the given pseudo-class */
    public boolean hasAnyProperty(PseudoClass pseudoClass, Collection<String> propertyNames) {
        List<Property> psProperties = getProperties().get(pseudoClass);
        if (psProperties == null) {
            return false;
        }
        for (Property property : psProperties) {
            if (propertyNames.contains(property.getName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * This rule covers the other if it has the same selector, and has all the properties of the
     * other, plus eventually some more
     */
    public boolean covers(CssRule other) {
        if (!other.getSelector().equals(getSelector())) {
            return false;
        }
        Set<PseudoClass> pseudoClasses = this.getProperties().keySet();
        Set<PseudoClass> otherPseudoClasses = other.getProperties().keySet();
        if (!pseudoClasses.containsAll(otherPseudoClasses)) {
            return false;
        }
        for (PseudoClass pc : otherPseudoClasses) {
            List<Property> properties = this.getProperties().get(pc);
            List<Property> otherProperties = other.getProperties().get(pc);
            for (Property p : otherProperties) {
                if (!properties.contains(p)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Extracts a sub-rule at the given z-index. Will return null if this rule has nothing at that
     * specific z-index
     */
    public CssRule getSubRuleByZIndex(Integer zIndex, ZIndexMode zIndexMode) {
        Map<PseudoClass, List<Property>> zProperties = new HashMap<>();
        List<Integer> zIndexes = new ArrayList<>();
        for (Map.Entry<PseudoClass, List<Property>> entry : this.getProperties().entrySet()) {
            List<Property> props = entry.getValue();
            collectZIndexesInProperties(props, zIndexes);
            // the list of z-index values is positional, people will normally set them in
            // increasing order, but we don't want to make assumptions... users could
            // even repeat the same z-index multiple times, take care of that as well
            ListIterator<Integer> it = zIndexes.listIterator();
            while (it.hasNext()) {
                int zIndexPosition = it.nextIndex();
                Integer nextZIndex = it.next();
                if (nextZIndex == NO_Z_INDEX) {
                    if (zIndexMode == ZIndexMode.NoZIndexAll
                            || !PseudoClass.ROOT.equals(entry.getKey())) {
                        // this set of properties is z-index independent
                        zProperties.put(entry.getKey(), new ArrayList<>(props));
                    } else if (zIndex == 0) {
                        zProperties.put(entry.getKey(), new ArrayList<>(props));
                    }
                } else if (nextZIndex == null || !nextZIndex.equals(zIndex)) {
                    continue;
                } else {
                    // extract the property values at that position
                    List<Property> zIndexProperties = new ArrayList<>();
                    for (Property property : props) {
                        if (isZIndex(property)) {
                            continue;
                        }
                        List<Value> values = property.getValues();
                        if (zIndexPosition < values.size()) {
                            Property p =
                                    new Property(
                                            property.getName(),
                                            Arrays.asList(values.get(zIndexPosition)));
                            zIndexProperties.add(p);
                        } else if (values.size() == 1) {
                            // properties that does not have multiple values are bound to all levels
                            zIndexProperties.add(property);
                        }
                    }
                    // if we collected any, add to the result
                    if (!zIndexProperties.isEmpty()) {
                        zProperties.put(entry.getKey(), zIndexProperties);
                    }
                }
            }
        }

        List<CssRule> nestedByZIndex = Collections.emptyList();
        if (nestedRules != null) {
            nestedByZIndex =
                    nestedRules.stream()
                            .map(r -> r.getSubRuleByZIndex(zIndex, zIndexMode))
                            .filter(r -> r != null)
                            .collect(Collectors.toList());
        }

        if (!zProperties.isEmpty()) {
            // if the properties had an original z-index, mark it, we'll need it
            // to figure out if a combination of rules can be applied at a z-index > 0, or not
            if (zIndex != null && zIndexes.contains(zIndex)) {
                List<Property> rootProperties = zProperties.get(PseudoClass.ROOT);
                if (rootProperties == null) {
                    rootProperties = new ArrayList<>();
                    zProperties.put(PseudoClass.ROOT, rootProperties);
                }
                rootProperties.add(
                        new Property(
                                "z-index",
                                Arrays.asList(new Value.Literal(String.valueOf(zIndex)))));
            }
            CssRule zRule = new CssRule(this.getSelector(), zProperties, this.getComment());
            zRule.nestedRules = nestedByZIndex;
            zRule.ancestry = Arrays.asList(this);
            return zRule;
        } else {
            return null;
        }
    }

    /** Returns all z-index values used by this rule */
    public Set<Integer> getZIndexes() {
        Set<Integer> indexes = new TreeSet<>(new ZIndexComparator());
        List<Integer> singleListIndexes = new ArrayList<>();
        for (List<Property> list : getProperties().values()) {
            collectZIndexesInProperties(list, singleListIndexes);
            indexes.addAll(singleListIndexes);
        }

        return indexes;
    }

    /** Returns the z-index values, in the order they are submitted */
    void collectZIndexesInProperties(List<Property> properties, List<Integer> zIndexes) {
        if (!zIndexes.isEmpty()) {
            zIndexes.clear();
        }
        for (Property property : properties) {
            if (isZIndex(property)) {
                if (!zIndexes.isEmpty()) {
                    // we have two z-index in the same set of properties? keep the latest
                    zIndexes.clear();
                }
                List<Value> values = property.getValues();
                for (Value value : values) {
                    if (value instanceof Value.Literal) {
                        String body = ((Value.Literal) value).body;
                        Integer zIndex = Converters.convert(body, Integer.class);
                        if (zIndex == null) {
                            throw new IllegalArgumentException(
                                    "Invalid value for z-index, it should be an integer: " + body);
                        } else {
                            zIndexes.add(zIndex);
                        }
                    } else {
                        throw new IllegalArgumentException(
                                "z-index must be integer literals, they cannot be expressions, multi-values or any other type: "
                                        + value);
                    }
                }
            }
        }
        // if we did not find the z-index property, the only z-index is the default one (which is
        if (zIndexes.isEmpty()) {
            zIndexes.add(null);
        }
    }

    private boolean isZIndex(Property property) {
        String name = property.getName();
        return "z-index".equals(name) || "raster-z-index".equals(name);
    }

    /** Returns the rule selector */
    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    /** Returns the rules properties, organized by pseudo-class */
    public Map<PseudoClass, List<Property>> getProperties() {
        return properties;
    }

    public void setProperties(Map<PseudoClass, List<Property>> properties) {
        this.properties = properties;
    }

    /** Returns the rule comment */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the original rules from which this rule originated (rules get re-organized and
     * combined a lot during the translation process to Geotools {@link Style}
     */
    public List<CssRule> getAncestry() {
        return ancestry;
    }

    public void setAncestry(List<CssRule> ancestry) {
        this.ancestry = ancestry;
    }

    /** Returns the rules nested in this one */
    public List<CssRule> getNestedRules() {
        return this.nestedRules;
    }

    /**
     * Returns true if the style has at least one property activating a symbolizer, e.g., fill,
     * stroke, mark, label or raster-channel
     */
    boolean hasSymbolizerProperty() {
        List<Property> rootProperties = getProperties().get(PseudoClass.ROOT);
        if (rootProperties == null) {
            return false;
        }
        for (Property property : rootProperties) {
            String name = property.getName();
            switch (name) {
                case "fill":
                case "stroke":
                case "mark":
                case "label":
                case "raster-channels":
                    return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the style has at least one property activating a symbolizer, e.g., fill,
     * stroke, mark, label or raster-channel
     */
    boolean hasNonNullSymbolizerProperty() {
        List<Property> rootProperties = getProperties().get(PseudoClass.ROOT);
        if (rootProperties == null) {
            return false;
        }
        for (Property property : rootProperties) {
            String name = property.getName();
            switch (name) {
                case "fill":
                case "stroke":
                case "mark":
                case "label":
                case "raster-channels":
                    return property.hasValues();
            }
        }

        return false;
    }

    /**
     * Returns the list of pseudo classes that can be mixed into this rule, meaning we have root
     * properties in which these pseudo classes can be mixed in.
     */
    Set<PseudoClass> getMixablePseudoClasses() {
        List<Property> rootProperties = getProperties().get(PseudoClass.ROOT);
        if (rootProperties == null) {
            return Collections.emptySet();
        }
        Set<PseudoClass> result = new HashSet<>();
        for (Property property : rootProperties) {
            String name = property.getName();
            switch (name) {
                case "fill":
                case "stroke":
                    result.add(PseudoClass.newPseudoClass("symbol"));
                    addPseudoClassesForConditionallyMixableProperty(result, property);
                    break;
                case "mark":
                    result.add(PseudoClass.newPseudoClass("symbol"));
                    result.add(PseudoClass.newPseudoClass("mark"));
                    addIndexedPseudoClasses(result, "mark");
                    break;
                case "label":
                    result.add(PseudoClass.newPseudoClass("symbol"));
                    result.add(PseudoClass.newPseudoClass("shield"));
                    addIndexedPseudoClasses(result, "shield");
                    break;
            }
        }

        return result;
    }

    /**
     * Adds pseudo classes for fill and stroke, whose ability to mix-in depends on whether a
     * function (symbol) or a straight value was used for the value of the property
     */
    private void addPseudoClassesForConditionallyMixableProperty(
            Set<PseudoClass> result, Property property) {
        String propertyName = property.getName();
        List<Value> values = property.getValues();
        if (values.size() == 1 && values.get(0) instanceof Function) {
            result.add(PseudoClass.newPseudoClass(propertyName));
            addIndexedPseudoClasses(result, propertyName);
        } else {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) instanceof Function) {
                    result.add(PseudoClass.newPseudoClass("symbol", i));
                    result.add(PseudoClass.newPseudoClass(propertyName, i + 1));
                }
            }
        }
    }

    /**
     * Collects all properties starting with the propertyName, and adds pseudo classes up to the max
     * index found in said properties
     */
    private void addIndexedPseudoClasses(Set<PseudoClass> result, String propertyName) {
        Map<String, List<Value>> properties = getPropertyValues(PseudoClass.ROOT, propertyName);
        int maxRepeatCount = getMaxRepeatCount(properties);
        if (maxRepeatCount >= 1) {
            for (int i = 1; i <= maxRepeatCount; i++) {
                result.add(PseudoClass.newPseudoClass("symbol", i));
                result.add(PseudoClass.newPseudoClass(propertyName, i));
            }
        }
    }

    /**
     * Returns the max number of property values in the provided property set (for repeated
     * symbolizers)
     */
    private int getMaxRepeatCount(Map<String, List<Value>> valueMap) {
        int max = 1;
        for (List<Value> values : valueMap.values()) {
            max = Math.max(max, values.size());
        }

        return max;
    }

    /**
     * Turns a rule with nested subrules into a flat list of rules (this rule, plus all nested with
     * a properly combined selector and property inheritance)
     */
    public List<CssRule> expandNested(RulesCombiner combiner) {
        if (nestedRules == null || nestedRules.isEmpty()) {
            return Collections.singletonList(this);
        } else {
            List<CssRule> result = new ArrayList<>();
            result.add(this);
            for (CssRule nestedRule : nestedRules) {
                // combine with parent's properties and selectors
                CssRule combined = combiner.combineRules(Arrays.asList(this, nestedRule));
                combined.setComment(nestedRule.getComment());
                combined.setAncestry(null);
                combined.nestedRules = nestedRule.nestedRules;

                final List<CssRule> nestedCombined = combined.expandNested(combiner);
                result.addAll(nestedCombined);
            }
            return result;
        }
    }

    /** Flattens pure pseudo-selector sub-rules into the main rule, as properties */
    public CssRule flattenPseudoSelectors() {
        if (nestedRules == null || nestedRules.isEmpty()) {
            return this;
        }
        List<CssRule> residual = new ArrayList<>();
        List<CssRule> pseudoRules = new ArrayList<>();
        for (CssRule nested : nestedRules) {
            final CssRule flattened = nested.flattenPseudoSelectors();
            final Selector removed =
                    (Selector) flattened.getSelector().accept(new PseudoClassRemover());
            if (Selector.ACCEPT.equals(removed)) {
                CssRule cleaned = new CssRule(removed, flattened.properties, flattened.comment);
                cleaned.nestedRules = flattened.nestedRules;
                pseudoRules.add(cleaned);
            } else {
                CssRule cleaned = new CssRule(removed, flattened.properties, flattened.comment);
                cleaned.nestedRules = flattened.nestedRules;
                residual.add(cleaned);
            }
        }

        if (!pseudoRules.isEmpty()) {
            pseudoRules.add(0, this);
            final CssRule combined =
                    new RulesCombiner(new SimplifyingFilterVisitor()).combineRules(pseudoRules);
            combined.nestedRules = residual;
            return combined;
        } else {
            final CssRule combined = new CssRule(this.selector, this.properties, this.comment);
            combined.nestedRules = residual;
            return combined;
        }
    }
}
