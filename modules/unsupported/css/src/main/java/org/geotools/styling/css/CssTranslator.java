/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.FileUtils;
import org.geotools.brewer.styling.builder.ChannelSelectionBuilder;
import org.geotools.brewer.styling.builder.ColorMapBuilder;
import org.geotools.brewer.styling.builder.ColorMapEntryBuilder;
import org.geotools.brewer.styling.builder.ContrastEnhancementBuilder;
import org.geotools.brewer.styling.builder.FeatureTypeStyleBuilder;
import org.geotools.brewer.styling.builder.FillBuilder;
import org.geotools.brewer.styling.builder.FontBuilder;
import org.geotools.brewer.styling.builder.GraphicBuilder;
import org.geotools.brewer.styling.builder.HaloBuilder;
import org.geotools.brewer.styling.builder.LineSymbolizerBuilder;
import org.geotools.brewer.styling.builder.MarkBuilder;
import org.geotools.brewer.styling.builder.PointPlacementBuilder;
import org.geotools.brewer.styling.builder.PointSymbolizerBuilder;
import org.geotools.brewer.styling.builder.PolygonSymbolizerBuilder;
import org.geotools.brewer.styling.builder.RasterSymbolizerBuilder;
import org.geotools.brewer.styling.builder.RuleBuilder;
import org.geotools.brewer.styling.builder.StrokeBuilder;
import org.geotools.brewer.styling.builder.StyleBuilder;
import org.geotools.brewer.styling.builder.SymbolizerBuilder;
import org.geotools.brewer.styling.builder.TextSymbolizerBuilder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.css.Value.Function;
import org.geotools.styling.css.Value.Literal;
import org.geotools.styling.css.Value.MultiValue;
import org.geotools.styling.css.selector.AbstractSelectorVisitor;
import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Or;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.selector.TypeName;
import org.geotools.styling.css.util.FeatureTypeGuesser;
import org.geotools.styling.css.util.OgcFilterBuilder;
import org.geotools.styling.css.util.PseudoClassRemover;
import org.geotools.styling.css.util.ScaleRangeExtractor;
import org.geotools.styling.css.util.TypeNameExtractor;
import org.geotools.styling.css.util.TypeNameSimplifier;
import org.geotools.styling.css.util.UnboundSimplifyingFilterVisitor;
import org.geotools.util.Converters;
import org.geotools.util.Range;
import org.geotools.util.logging.Logging;
import org.geotools.xml.styling.SLDTransformer;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Style;

/**
 * Transforms a GeoCSS into an equivalent GeoTools {@link Style} object
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CssTranslator {

    /**
     * The ways the CSS -> SLD transformation can be performed
     *
     * @author Andrea Aime - GeoSolutions
     */
    static enum TranslationMode {
        /** Generates fully exclusive rules, extra rules are removed */
        Exclusive,
        /**
         * Sets the "exclusive" evaluation mode in the FeatureTypeStyle and delegates finding the
         * first matching rules to the renderer, will generate more rules, but work a lot less to do
         * so by avoiding to compute the domain coverage
         */
        Simple,
        /**
         * The translator will pick Exclusive by default, but if the rules to be turned into SLD go
         * beyond
         */
        Flat,
        /**
         * All rules are merged straight forward if filters are exactly matching only with the
         * direct following pseudo rules. There is no cascading going on, no creation of additional
         * rules. After merging the rules are sorted by z-index.
         */
        Auto;
    };

    static final Logger LOGGER = Logging.getLogger(CssTranslator.class);

    static final String DIRECTIVE_MAX_OUTPUT_RULES = "maxOutputRules";

    static final String DIRECTIVE_AUTO_THRESHOLD = "autoThreshold";

    static final String DIRECTIVE_TRANSLATION_MODE = "mode";

    static final String DIRECTIVE_STYLE_TITLE = "styleTitle";

    static final String DIRECTIVE_STYLE_ABSTRACT = "styleAbstract";

    static final int MAX_OUTPUT_RULES_DEFAULT =
            Integer.valueOf(
                    System.getProperty("org.geotools.css." + DIRECTIVE_MAX_OUTPUT_RULES, "10000"));

    static final int AUTO_THRESHOLD_DEFAULT =
            Integer.valueOf(
                    System.getProperty("org.geotools.css." + DIRECTIVE_AUTO_THRESHOLD, "100"));

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    /** Matches the title tag inside a rule comment */
    static final Pattern TITLE_PATTERN = Pattern.compile("^.*@title\\s*(?:\\:\\s*)?(.+)\\s*$");

    /** Matches the abstract tag inside a rule comment */
    static final Pattern ABSTRACT_PATTERN =
            Pattern.compile("^.*@abstract\\s*(?:\\:\\s*)?(.+)\\s*$");

    /** The global composite property */
    static final String COMPOSITE = "composite";

    /** The global composite-base property */
    static final String COMPOSITE_BASE = "composite-base";

    /** The attribute sorting property */
    static final String SORT_BY = "sort-by";

    /** The sort group for z-ordering */
    static final String SORT_BY_GROUP = "sort-by-group";

    /** The transformation */
    static final String TRANSFORM = "transform";

    @SuppressWarnings("serial")
    static final Map<String, String> POLYGON_VENDOR_OPTIONS =
            new HashMap<String, String>() {
                {
                    put("graphic-margin", "graphic-margin");
                    put("fill-label-obstacle", "labelObstacle");
                    put("fill-random", "random");
                    put("fill-random-seed", "random-seed");
                    put("fill-random-tile-size", "random-tile-size");
                    put("fill-random-symbol-count", "random-symbol-count");
                    put("fill-random-space-around", "random-space-around");
                    put("fill-random-rotation", "random-rotation");
                    put("fill-composite", "composite");
                }
            };

    @SuppressWarnings("serial")
    static final Map<String, String> TEXT_VENDOR_OPTIONS =
            new HashMap<String, String>() {
                {
                    put("label-padding", TextSymbolizer.SPACE_AROUND_KEY);
                    put("label-group", "group");
                    put("label-max-displacement", TextSymbolizer.MAX_DISPLACEMENT_KEY);
                    put("label-min-group-distance", TextSymbolizer.MIN_GROUP_DISTANCE_KEY);
                    put("label-repeat", TextSymbolizer.LABEL_REPEAT_KEY);
                    put("label-all-group", TextSymbolizer.LABEL_ALL_GROUP_KEY);
                    put("label-remove-overlaps", TextSymbolizer.REMOVE_OVERLAPS_KEY);
                    put("label-allow-overruns", TextSymbolizer.ALLOW_OVERRUNS_KEY);
                    put("label-follow-line", TextSymbolizer.FOLLOW_LINE_KEY);
                    put("label-underline-text", TextSymbolizer.UNDERLINE_TEXT_KEY);
                    put("label-strikethrough-text", TextSymbolizer.STRIKETHROUGH_TEXT_KEY);
                    put("label-char-spacing", TextSymbolizer.CHAR_SPACING_KEY);
                    put("label-word-spacing", TextSymbolizer.WORD_SPACING_KEY);
                    put("label-max-angle-delta", TextSymbolizer.MAX_ANGLE_DELTA_KEY);
                    put("label-auto-wrap", TextSymbolizer.AUTO_WRAP_KEY);
                    put("label-force-ltr", TextSymbolizer.FORCE_LEFT_TO_RIGHT_KEY);
                    put("label-conflict-resolution", TextSymbolizer.CONFLICT_RESOLUTION_KEY);
                    put("label-fit-goodness", TextSymbolizer.GOODNESS_OF_FIT_KEY);
                    put("label-kerning", TextSymbolizer.KERNING_KEY);
                    put("label-polygon-align", TextSymbolizer.POLYGONALIGN_KEY);
                    put("shield-resize", "graphic-resize");
                    put("shield-margin", "graphic-margin");
                }
            };

    @SuppressWarnings("serial")
    static final Map<String, String> LINE_VENDOR_OPTIONS =
            new HashMap<String, String>() {
                {
                    put("stroke-label-obstacle", "labelObstacle");
                    put("stroke-composite", "composite");
                }
            };

    @SuppressWarnings("serial")
    static final Map<String, String> POINT_VENDOR_OPTIONS =
            new HashMap<String, String>() {
                {
                    put("mark-label-obstacle", "labelObstacle");
                    put("mark-composite", "composite");
                }
            };

    @SuppressWarnings("serial")
    static final Map<String, String> RASTER_VENDOR_OPTIONS =
            new HashMap<String, String>() {
                {
                    put("raster-composite", "composite");
                }
            };

    @SuppressWarnings("serial")
    static final Map<String, String> CONTRASTENHANCMENT_VENDOR_OPTIONS =
            new HashMap<String, String>() {
                {
                    put("raster-contrast-enhancement-algorithm", "algorithm");

                    put("raster-contrast-enhancement-min", "minValue");
                    put("raster-contrast-enhancement-max", "maxValue");

                    put("raster-contrast-enhancement-normalizationfactor", "normalizationFactor");
                    put("raster-contrast-enhancement-correctionfactor", "correctionFactor");
                    // short forms for lazy people
                    put("rce-algorithm", "algorithm");

                    put("rce-min", "minValue");
                    put("rce-max", "maxValue");

                    put("rce-normalizationfactor", "normalizationFactor");
                    put("rce-correctionfactor", "correctionFactor");
                }
            };

    /** Limits how many output rules we are going to generate */
    int maxCombinations = MAX_OUTPUT_RULES_DEFAULT;

    public int getMaxCombinations() {
        return maxCombinations;
    }

    /**
     * Maximum number of rule combinations before bailing out of the power set generation
     *
     * @param maxCombinations
     */
    public void setMaxCombinations(int maxCombinations) {
        this.maxCombinations = maxCombinations;
    }

    /**
     * Translates a CSS stylesheet into an equivalent GeoTools {@link Style} object
     *
     * @param stylesheet
     * @return
     */
    public Style translate(Stylesheet stylesheet) {
        // get the directives influencing translation
        int maxCombinations = getMaxCombinations(stylesheet);
        final TranslationMode mode = getTranslationMode(stylesheet);
        int autoThreshold = getAutoThreshold(stylesheet);

        List<CssRule> topRules = stylesheet.getRules();
        List<CssRule> allRules = expandNested(topRules);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Starting with " + allRules.size() + "  rules in the stylesheet");
        }

        // prepare the full SLD builder
        StyleBuilder styleBuilder = new StyleBuilder();
        styleBuilder.name("Default Styler");
        styleBuilder.title(stylesheet.getDirectiveValue(DIRECTIVE_STYLE_TITLE));
        styleBuilder.styleAbstract(stylesheet.getDirectiveValue(DIRECTIVE_STYLE_ABSTRACT));

        int translatedRuleCount = 0;
        if (mode == TranslationMode.Flat) {
            translatedRuleCount = translateFlat(allRules, styleBuilder);
        } else {
            translatedRuleCount =
                    translateCss(mode, allRules, styleBuilder, maxCombinations, autoThreshold);
        }

        // check that we have generated at least one rule in output
        if (translatedRuleCount == 0) {
            throw new IllegalArgumentException(
                    "Invalid CSS style, no rule seems to activate "
                            + "any symbolization. The properties activating the symbolizers are fill, "
                            + "stroke, mark, label, raster-channels, have any been used in a rule matching any feature?");
        }

        return styleBuilder.build();
    }

    private List<CssRule> expandNested(List<CssRule> topRules) {
        RulesCombiner combiner = new RulesCombiner(new UnboundSimplifyingFilterVisitor());
        List<CssRule> expanded =
                topRules.stream()
                        .flatMap(r -> r.expandNested(combiner).stream())
                        .collect(Collectors.toList());
        return expanded;
    }

    private int translateCss(
            TranslationMode mode,
            List<CssRule> allRules,
            StyleBuilder styleBuilder,
            int maxCombinations,
            int autoThreshold) {
        // split rules by index and typename, then build the power set for each group and
        // generate the rules and symbolizers
        Map<Integer, List<CssRule>> zIndexRules = organizeByZIndex(allRules);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Split the rules into " + zIndexRules + "  sets after z-index separation");
        }
        int translatedRuleCount = 0;
        for (Map.Entry<Integer, List<CssRule>> zEntry : zIndexRules.entrySet()) {
            final Integer zIndex = zEntry.getKey();
            List<CssRule> rules = zEntry.getValue();
            Collections.sort(rules, CssRuleComparator.DESCENDING);
            Map<String, List<CssRule>> typenameRules = organizeByTypeName(rules);
            // build the SLD
            for (Map.Entry<String, List<CssRule>> entry : typenameRules.entrySet()) {
                String featureTypeName = entry.getKey();
                List<CssRule> localRules = entry.getValue();
                final FeatureType targetFeatureType =
                        getTargetFeatureType(featureTypeName, localRules);
                if (targetFeatureType != null) {
                    // attach the target feature type to all Data selectors to allow range based
                    // simplification
                    for (CssRule rule : localRules) {
                        rule.getSelector()
                                .accept(
                                        new AbstractSelectorVisitor() {
                                            @Override
                                            public Object visit(Data data) {
                                                data.featureType = targetFeatureType;
                                                return super.visit(data);
                                            }
                                        });
                    }
                }
                // at this point we can have rules with selectors having two scale ranges
                // in or, we should split them, as we cannot represent them in SLD
                // (and yes, this changes their selectivity a bit, could not find a reasonable
                // solution out of this so far, past the power set we might end up with
                // and and of two selectors, that internally have ORs of scales, which could
                // be quite complicated to un-tangle)
                List<CssRule> flattenedRules = flattenScaleRanges(localRules);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Preparing power set expansion with "
                                    + flattenedRules.size()
                                    + "  rules for feature type: "
                                    + featureTypeName);
                }
                // The simplifying visitor that will cache the results to avoid re-computing
                // over and over the same simplifications
                CachedSimplifyingFilterVisitor cachedSimplifier =
                        new CachedSimplifyingFilterVisitor(targetFeatureType);
                RulePowerSetBuilder builder =
                        new RulePowerSetBuilder(flattenedRules, cachedSimplifier, maxCombinations) {
                            @Override
                            protected java.util.List<CssRule> buildResult(
                                    java.util.List<CssRule> rules) {
                                if (zIndex != null && zIndex > 0) {
                                    TreeSet<Integer> zIndexes = getZIndexesForRules(rules);
                                    if (!zIndexes.contains(zIndex)) {
                                        return null;
                                    }
                                }
                                return super.buildResult(rules);
                            }
                        };
                List<CssRule> combinedRules = builder.buildPowerSet();
                if (combinedRules.isEmpty()) {
                    continue;
                }
                // create the feature type style for this typename
                FeatureTypeStyleBuilder ftsBuilder = styleBuilder.featureTypeStyle();
                // regardless of the translation mode, the first rule matching is
                // the only one that we want to be applied (in exclusive mode it will be
                // the only one matching, the simple mode we want the evaluation to stop there)
                ftsBuilder.option(
                        FeatureTypeStyle.KEY_EVALUATION_MODE,
                        FeatureTypeStyle.VALUE_EVALUATION_MODE_FIRST);

                if (featureTypeName != null) {
                    ftsBuilder.setFeatureTypeNames(
                            Arrays.asList((Name) new NameImpl(featureTypeName)));
                }
                Collections.sort(combinedRules, CssRuleComparator.DESCENDING);
                int rulesCount = combinedRules.size();
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Generated "
                                    + rulesCount
                                    + " combined rules after filtered power set expansion");
                }
                String composite = null;
                Boolean compositeBase = null;
                String sortBy = null;
                String sortByGroup = null;
                Expression transform = null;
                // setup the tool that will eliminate redundant rules (if necessary)
                DomainCoverage coverage = new DomainCoverage(targetFeatureType, cachedSimplifier);
                if (mode == TranslationMode.Exclusive) {
                    // create a SLD rule for each css one, making them exclusive, that is,
                    // remove from each rule the union of the zoom/data domain matched by previous
                    // rules
                    coverage.exclusiveRulesEnabled = true;
                } else if (mode == TranslationMode.Auto) {
                    if (rulesCount < autoThreshold) {
                        LOGGER.fine(
                                "Sticking to Exclusive translation mode, rules number is "
                                        + rulesCount
                                        + " with a threshold of "
                                        + autoThreshold);
                        coverage.exclusiveRulesEnabled = true;
                        coverage.complexityThreshold = autoThreshold;
                    } else {
                        LOGGER.info(
                                "Switching to Simple translation mode, rules number is "
                                        + rulesCount
                                        + " with a threshold of "
                                        + autoThreshold);
                        coverage.exclusiveRulesEnabled = false;
                        // switch the translation mode permanently from this point on
                        mode = TranslationMode.Simple;
                    }

                } else {
                    // just skip rules with the same selector
                    coverage.exclusiveRulesEnabled = false;
                }
                // generate the SLD rules
                for (int i = 0; i < rulesCount; i++) {
                    // skip eventual combinations that are not sporting any
                    // root pseudo class
                    CssRule cssRule = combinedRules.get(i);
                    if (!cssRule.hasSymbolizerProperty()) {
                        continue;
                    }
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Current domain coverage: " + coverage);
                        LOGGER.fine("Adding rule to domain coverage: " + cssRule);
                        LOGGER.fine("Rules left to process: " + (rulesCount - i));
                    }
                    List<CssRule> derivedRules = coverage.addRule(cssRule);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                                "Derived rules not yet covered in domain coverage: "
                                        + derivedRules.size()
                                        + "\n"
                                        + derivedRules);
                    }
                    for (CssRule derived : derivedRules) {
                        if (!derived.hasNonNullSymbolizerProperty()) {
                            continue;
                        }
                        buildSldRule(derived, ftsBuilder, targetFeatureType);

                        translatedRuleCount++;

                        // Reminder about why this is done the way it's done. These are all rule
                        // properties
                        // in CSS and are subject to override. In SLD they contribute to containing
                        // FeatureTypeStyle, so the first one found wins and controls this z-level

                        // check if we have global composition going, and use the value of
                        // the first rule providing the information (the one with the highest
                        // priority)
                        if (composite == null) {
                            List<Value> values =
                                    derived.getPropertyValues(PseudoClass.ROOT, COMPOSITE)
                                            .get(COMPOSITE);
                            if (values != null && !values.isEmpty()) {
                                composite = values.get(0).toLiteral();
                            }
                        }
                        if (compositeBase == null) {
                            List<Value> values =
                                    derived.getPropertyValues(PseudoClass.ROOT, COMPOSITE_BASE)
                                            .get(COMPOSITE_BASE);
                            if (values != null && !values.isEmpty()) {
                                compositeBase = Boolean.valueOf(values.get(0).toLiteral());
                            }
                        }

                        // check if we have any sort-by
                        if (sortBy == null) {
                            List<Value> values =
                                    derived.getPropertyValues(PseudoClass.ROOT, SORT_BY)
                                            .get(SORT_BY);
                            if (values != null && !values.isEmpty()) {
                                sortBy = values.get(0).toLiteral();
                            }
                        }

                        // check if we have any sort-by-group
                        if (sortByGroup == null) {
                            List<Value> values =
                                    derived.getPropertyValues(PseudoClass.ROOT, SORT_BY_GROUP)
                                            .get(SORT_BY_GROUP);
                            if (values != null && !values.isEmpty()) {
                                sortByGroup = values.get(0).toLiteral();
                            }
                        }

                        // check if we have a transform, apply it
                        if (transform == null) {
                            List<Value> values =
                                    derived.getPropertyValues(PseudoClass.ROOT, TRANSFORM)
                                            .get(TRANSFORM);
                            if (values != null && !values.isEmpty()) {
                                transform = values.get(0).toExpression();
                            }
                        }
                    }

                    if (composite != null) {
                        ftsBuilder.option(COMPOSITE, composite);
                    }
                    if (Boolean.TRUE.equals(compositeBase)) {
                        ftsBuilder.option(COMPOSITE_BASE, "true");
                    }
                    if (sortBy != null) {
                        ftsBuilder.option(FeatureTypeStyle.SORT_BY, sortBy);
                    }
                    if (sortByGroup != null) {
                        ftsBuilder.option(FeatureTypeStyle.SORT_BY_GROUP, sortByGroup);
                    }
                    if (transform != null) {
                        ftsBuilder.transformation(transform);
                    }
                }
            }
        }
        return translatedRuleCount;
    }

    private int translateFlat(List<CssRule> allRules, StyleBuilder styleBuilder) {
        List<CssRule> finalRules = new ArrayList<>();
        CssRule actualRule = null;
        Map<PseudoClass, List<Property>> properties = null;
        Set<PseudoClass> mixablePseudoClasses = null;

        int translatedRuleCount = 0;
        for (CssRule rule : allRules) {
            if (rule.getProperties().get(PseudoClass.ROOT) == null) {
                Selector simplified = (Selector) rule.selector.accept(new PseudoClassRemover());
                if (actualRule != null && actualRule.getSelector().equals(simplified)) {
                    boolean changed = false;
                    for (Map.Entry<PseudoClass, List<Property>> item : rule.properties.entrySet()) {
                        if (mixablePseudoClasses.contains(item.getKey())) {
                            properties.put(item.getKey(), item.getValue());
                            changed = true;
                        }
                    }
                    if (changed) {
                        actualRule =
                                new CssRule(actualRule.selector, properties, actualRule.comment);
                    }
                }
            } else {
                if (actualRule != null) {
                    finalRules.add(actualRule);
                }
                actualRule = rule;
                mixablePseudoClasses = actualRule.getMixablePseudoClasses();
                properties = new LinkedHashMap<>(actualRule.properties);
            }
        }

        if (actualRule != null) {
            finalRules.add(actualRule);
        }

        if (finalRules.isEmpty()) {
            return 0;
        }

        Map<Integer, List<CssRule>> zIndexRules = organizeByZIndex(finalRules);

        for (Map.Entry<Integer, List<CssRule>> zEntry : zIndexRules.entrySet()) {
            List<CssRule> rules = zEntry.getValue();
            Map<String, List<CssRule>> typenameRules = organizeByTypeName(rules);
            // build the SLD
            for (Map.Entry<String, List<CssRule>> entry : typenameRules.entrySet()) {
                String featureTypeName = entry.getKey();
                List<CssRule> localRules = entry.getValue();
                final FeatureType targetFeatureType =
                        getTargetFeatureType(featureTypeName, localRules);
                List<CssRule> flattenedRules = flattenScaleRanges(localRules);

                FeatureTypeStyleBuilder ftsBuilder = styleBuilder.featureTypeStyle();
                if (featureTypeName != null) {
                    ftsBuilder.setFeatureTypeNames(
                            Arrays.asList((Name) new NameImpl(featureTypeName)));
                }

                String composite = null;
                Boolean compositeBase = null;
                String sortBy = null;
                String sortByGroup = null;

                // generate the SLD rules
                for (CssRule cssRule : flattenedRules) {
                    if (!cssRule.hasNonNullSymbolizerProperty()) {
                        continue;
                    }

                    buildSldRule(cssRule, ftsBuilder, targetFeatureType);
                    translatedRuleCount++;

                    // check if we have global composition going, and use the value of
                    // the first rule providing the information (the one with the highest
                    // priority)
                    if (composite == null) {
                        List<Value> values =
                                cssRule.getPropertyValues(PseudoClass.ROOT, COMPOSITE)
                                        .get(COMPOSITE);
                        if (values != null && !values.isEmpty()) {
                            composite = values.get(0).toLiteral();
                        }
                    }
                    if (compositeBase == null) {
                        List<Value> values =
                                cssRule.getPropertyValues(PseudoClass.ROOT, COMPOSITE_BASE)
                                        .get(COMPOSITE_BASE);
                        if (values != null && !values.isEmpty()) {
                            compositeBase = Boolean.valueOf(values.get(0).toLiteral());
                        }
                    }

                    // check if we have any sort-by
                    if (sortBy == null) {
                        List<Value> values =
                                cssRule.getPropertyValues(PseudoClass.ROOT, SORT_BY).get(SORT_BY);
                        if (values != null && !values.isEmpty()) {
                            sortBy = values.get(0).toLiteral();
                        }
                    }

                    // check if we have any sort-by-group
                    if (sortByGroup == null) {
                        List<Value> values =
                                cssRule.getPropertyValues(PseudoClass.ROOT, SORT_BY_GROUP)
                                        .get(SORT_BY_GROUP);
                        if (values != null && !values.isEmpty()) {
                            sortByGroup = values.get(0).toLiteral();
                        }
                    }
                }
                if (composite != null) {
                    ftsBuilder.option(COMPOSITE, composite);
                }
                if (Boolean.TRUE.equals(compositeBase)) {
                    ftsBuilder.option(COMPOSITE_BASE, "true");
                }
                if (sortBy != null) {
                    ftsBuilder.option(FeatureTypeStyle.SORT_BY, sortBy);
                }
                if (sortByGroup != null) {
                    ftsBuilder.option(FeatureTypeStyle.SORT_BY_GROUP, sortByGroup);
                }
            }
        }

        return translatedRuleCount;
    }

    private TranslationMode getTranslationMode(Stylesheet stylesheet) {
        String value = stylesheet.getDirectiveValue(DIRECTIVE_TRANSLATION_MODE);
        if (value != null) {
            try {
                return TranslationMode.valueOf(value);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Invalid translation mode '"
                                + value
                                + "', supported values are: "
                                + Arrays.toString(TranslationMode.values()));
            }
        }

        return TranslationMode.Auto;
    }

    private int getMaxCombinations(Stylesheet stylesheet) {
        int maxCombinations = this.maxCombinations;
        String maxOutputRulesDirective = stylesheet.getDirectiveValue(DIRECTIVE_MAX_OUTPUT_RULES);
        if (maxOutputRulesDirective != null) {
            Integer converted = Converters.convert(maxOutputRulesDirective, Integer.class);
            if (converted == null) {
                throw new IllegalArgumentException(
                        "Invalid value for "
                                + DIRECTIVE_MAX_OUTPUT_RULES
                                + ", it should be a positive integer value, it was "
                                + maxOutputRulesDirective);
            }
            maxCombinations = converted;
        }
        return maxCombinations;
    }

    private int getAutoThreshold(Stylesheet stylesheet) {
        int result = AUTO_THRESHOLD_DEFAULT;
        String autoThreshold = stylesheet.getDirectiveValue(DIRECTIVE_AUTO_THRESHOLD);
        if (autoThreshold != null) {
            Integer converted = Converters.convert(autoThreshold, Integer.class);
            if (converted == null) {
                throw new IllegalArgumentException(
                        "Invalid value for "
                                + DIRECTIVE_AUTO_THRESHOLD
                                + ", it should be a positive integer value, it was "
                                + autoThreshold);
            }
            result = converted;
        }
        return result;
    }

    /**
     * SLD rules can have two or more selectors in OR using different scale ranges, however the SLD
     * model does not allow for that. Flatten them into N different rules, with the same properties,
     * but different selectors
     *
     * @param rules
     * @return
     */
    private List<CssRule> flattenScaleRanges(List<CssRule> rules) {
        List<CssRule> result = new ArrayList<>();
        for (CssRule rule : rules) {
            if (rule.getSelector() instanceof Or) {
                Or or = (Or) rule.getSelector();
                List<Selector> others = new ArrayList<>();
                for (Selector child : or.getChildren()) {
                    ScaleRangeExtractor extractor = new ScaleRangeExtractor();
                    Range<Double> range = extractor.getScaleRange(child);
                    if (range == null) {
                        others.add(child);
                    } else {
                        result.add(new CssRule(child, rule.getProperties(), rule.getComment()));
                    }
                }
                if (others.size() == 1) {
                    result.add(new CssRule(others.get(0), rule.getProperties(), rule.getComment()));
                } else if (others.size() > 0) {
                    result.add(
                            new CssRule(new Or(others), rule.getProperties(), rule.getComment()));
                }
            } else {
                result.add(rule);
            }
        }

        return result;
    }

    /**
     * This method builds a target feature type based on the provided rules, subclasses can override
     * and maybe pick the feature type from a well known source
     */
    protected FeatureType getTargetFeatureType(String featureTypeName, List<CssRule> rules) {
        FeatureTypeGuesser guesser = new FeatureTypeGuesser();
        for (CssRule rule : rules) {
            guesser.addRule(rule);
        }

        return guesser.getFeatureType();
    }

    /**
     * Splits the rules into different sets by feature type name
     *
     * @param rules
     * @return
     */
    private Map<String, List<CssRule>> organizeByTypeName(List<CssRule> rules) {
        TypeNameExtractor extractor = new TypeNameExtractor();
        for (CssRule rule : rules) {
            rule.getSelector().accept(extractor);
        }

        // extract all typename specific rules
        Map<String, List<CssRule>> result = new LinkedHashMap<>();
        Set<TypeName> typeNames = extractor.getTypeNames();
        if (typeNames.size() == 1 && typeNames.contains(TypeName.DEFAULT)) {
            // no layer specific stuff
            result.put(TypeName.DEFAULT.name, rules);
        }

        for (TypeName tn : typeNames) {
            List<CssRule> typeNameRules = new ArrayList<>();
            for (CssRule rule : rules) {
                TypeNameSimplifier simplifier = new TypeNameSimplifier(tn);
                Selector simplified = (Selector) rule.getSelector().accept(simplifier);
                if (simplified != Selector.REJECT) {
                    typeNameRules.add(
                            new CssRule(simplified, rule.getProperties(), rule.getComment()));
                }
            }
            result.put(tn.name, typeNameRules);
        }

        return result;
    }

    /**
     * Organizes them rules by ascending z-index
     *
     * @param rules
     * @return
     */
    private Map<Integer, List<CssRule>> organizeByZIndex(List<CssRule> rules) {
        TreeSet<Integer> indexes = getZIndexesForRules(rules);
        Map<Integer, List<CssRule>> result = new TreeMap<>();
        if (indexes.size() == 1) {
            result.put(indexes.first(), rules);
        } else {
            // now for each level extract the sub-rules attached to that level,
            // considering that properties not associated to a level, bind to all levels
            int symbolizerPropertyCount = 0;
            for (Integer index : indexes) {
                List<CssRule> rulesByIndex = new ArrayList<>();
                for (CssRule rule : rules) {
                    CssRule subRule = rule.getSubRuleByZIndex(index);
                    if (subRule != null) {
                        if (subRule.hasSymbolizerProperty()) {
                            symbolizerPropertyCount++;
                        }
                        rulesByIndex.add(subRule);
                    }
                }
                // do we have at least one property that will trigger the generation
                // of a symbolizer in here?
                if (symbolizerPropertyCount > 0) {
                    result.put(index, rulesByIndex);
                }
            }
        }

        return result;
    }

    private TreeSet<Integer> getZIndexesForRules(List<CssRule> rules) {
        // collect and sort all the indexes first
        TreeSet<Integer> indexes = new TreeSet<>(new ZIndexComparator());
        for (CssRule rule : rules) {
            Set<Integer> ruleIndexes = rule.getZIndexes();
            if (ruleIndexes.contains(null)) {
                ruleIndexes.remove(null);
                ruleIndexes.add(0);
            }
            indexes.addAll(ruleIndexes);
        }
        return indexes;
    }

    /**
     * Turns an SLD compatible {@link CssRule} into a {@link Rule}, appending it to the {@link
     * FeatureTypeStyleBuilder}
     *
     * @param cssRule
     * @param fts
     * @param targetFeatureType
     */
    void buildSldRule(CssRule cssRule, FeatureTypeStyleBuilder fts, FeatureType targetFeatureType) {
        // check we have a valid scale range
        Range<Double> scaleRange = ScaleRangeExtractor.getScaleRange(cssRule);
        if (scaleRange != null && scaleRange.isEmpty()) {
            return;
        }

        // check we have a valid filter
        Filter filter = OgcFilterBuilder.buildFilter(cssRule.getSelector(), targetFeatureType);
        if (filter == Filter.EXCLUDE) {
            return;
        }

        // ok, build the rule
        RuleBuilder ruleBuilder;
        ruleBuilder = fts.rule();
        ruleBuilder.filter(filter);
        String title = getCombinedTag(cssRule.getComment(), TITLE_PATTERN, ", ");
        if (title != null) {
            ruleBuilder.title(title);
        }
        String ruleAbstract = getCombinedTag(cssRule.getComment(), ABSTRACT_PATTERN, "\n");
        if (ruleAbstract != null) {
            ruleBuilder.ruleAbstract(ruleAbstract);
        }
        if (scaleRange != null) {
            Double minValue = scaleRange.getMinValue();
            if (minValue != null && minValue > 0) {
                ruleBuilder.min(minValue);
            }
            Double maxValue = scaleRange.getMaxValue();
            if (maxValue != null && maxValue < Double.POSITIVE_INFINITY) {
                ruleBuilder.max(maxValue);
            }
        }

        // see if we can fold the stroke into a polygon symbolizer
        boolean generateStroke = cssRule.hasProperty(PseudoClass.ROOT, "stroke");
        boolean lineSymbolizerSpecificProperties =
                cssRule.hasAnyVendorProperty(PseudoClass.ROOT, LINE_VENDOR_OPTIONS.keySet())
                        || !sameGeometry(cssRule, "stroke-geometry", "fill-geometry");
        boolean includeStrokeInPolygonSymbolizer =
                generateStroke && !lineSymbolizerSpecificProperties;
        boolean generatePolygonSymbolizer = cssRule.hasProperty(PseudoClass.ROOT, "fill");
        if (generatePolygonSymbolizer) {
            addPolygonSymbolizer(cssRule, ruleBuilder, includeStrokeInPolygonSymbolizer);
        }
        if (generateStroke && !(generatePolygonSymbolizer && includeStrokeInPolygonSymbolizer)) {
            addLineSymbolizer(cssRule, ruleBuilder);
        }
        if (cssRule.hasProperty(PseudoClass.ROOT, "mark")) {
            addPointSymbolizer(cssRule, ruleBuilder);
        }
        if (cssRule.hasProperty(PseudoClass.ROOT, "label")) {
            addTextSymbolizer(cssRule, ruleBuilder);
        }
        if (cssRule.hasProperty(PseudoClass.ROOT, "raster-channels")) {
            addRasterSymbolizer(cssRule, ruleBuilder);
        }
    }

    private boolean sameGeometry(CssRule cssRule, String geomProperty1, String geomProperty2) {
        Property p1 = cssRule.getProperty(PseudoClass.ROOT, geomProperty1);
        Property p2 = cssRule.getProperty(PseudoClass.ROOT, geomProperty2);
        return Objects.equals(p1, p2);
    }

    private String getCombinedTag(String comment, Pattern p, String separator) {
        if (comment == null || comment.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        for (String line : comment.split("\n")) {
            Matcher matcher = p.matcher(line);
            if (matcher.matches()) {
                String text = matcher.group(1).trim();
                if (!text.isEmpty()) {
                    if (sb.length() > 0) {
                        sb.append(separator);
                    }
                    sb.append(text);
                }
            }
        }

        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * Builds a polygon symbolizer into the current rule, if a <code>fill</code> property is found
     *
     * @param cssRule
     * @param ruleBuilder
     * @param includeStrokeInPolygonSymbolizer
     */
    private void addPolygonSymbolizer(
            CssRule cssRule, RuleBuilder ruleBuilder, boolean includeStrokeInPolygonSymbolizer) {
        Map<String, List<Value>> values;
        if (includeStrokeInPolygonSymbolizer) {
            values =
                    cssRule.getPropertyValues(
                            PseudoClass.ROOT,
                            "fill",
                            "graphic-margin",
                            "-gt-graphic-margin",
                            "stroke");
        } else {
            values =
                    cssRule.getPropertyValues(
                            PseudoClass.ROOT, "fill", "graphic-margin", "-gt-graphic-margin");
        }
        if (values == null || values.isEmpty()) {
            return;
        }
        int repeatCount = getMaxRepeatCount(values);
        for (int i = 0; i < repeatCount; i++) {
            Value fill = getValue(values, "fill", i);
            if (fill == null) {
                continue;
            }
            PolygonSymbolizerBuilder pb = ruleBuilder.polygon();
            Expression fillGeometry = getExpression(values, "fill-geometry", i);
            if (fillGeometry != null) {
                pb.geometry(fillGeometry);
            }
            FillBuilder fb = pb.fill();
            buildFill(cssRule, fb, values, i);
            if (includeStrokeInPolygonSymbolizer) {
                StrokeBuilder sb = pb.stroke();
                buildStroke(cssRule, sb, values, i);
            }
            addVendorOptions(pb, POLYGON_VENDOR_OPTIONS, values, i);
        }
    }

    /**
     * Builds a point symbolizer into the current rule, if a <code>mark</code> property is found
     *
     * @param cssRule
     * @param ruleBuilder
     */
    private void addPointSymbolizer(CssRule cssRule, RuleBuilder ruleBuilder) {
        Map<String, List<Value>> values = cssRule.getPropertyValues(PseudoClass.ROOT, "mark");
        if (values == null || values.isEmpty()) {
            return;
        }
        int repeatCount = getMaxRepeatCount(values);
        for (int i = 0; i < repeatCount; i++) {
            final PointSymbolizerBuilder pb = ruleBuilder.point();
            Expression markGeometry = getExpression(values, "mark-geometry", i);
            if (markGeometry != null) {
                pb.geometry(markGeometry);
            }
            for (Value markValue : getMultiValue(values, "mark", i)) {
                new SubgraphicBuilder("mark", markValue, values, cssRule, i) {

                    @Override
                    protected GraphicBuilder getGraphicBuilder() {
                        return pb.graphic();
                    }
                };
            }

            addVendorOptions(pb, POINT_VENDOR_OPTIONS, values, i);
        }
    }

    /**
     * Builds a text symbolizer into the current rule, if a <code>label</code> property is found
     *
     * @param cssRule
     * @param ruleBuilder
     */
    private void addTextSymbolizer(CssRule cssRule, RuleBuilder ruleBuilder) {
        Map<String, List<Value>> values =
                cssRule.getPropertyValues(PseudoClass.ROOT, "label", "font", "shield", "halo");
        if (values == null || values.isEmpty()) {
            return;
        }
        int repeatCount = getMaxRepeatCount(values);
        for (int i = 0; i < repeatCount; i++) {
            final TextSymbolizerBuilder tb = ruleBuilder.text();
            Expression labelGeometry = getExpression(values, "label-geometry", i);
            if (labelGeometry != null) {
                tb.geometry(labelGeometry);
            }

            // special handling for label, we allow multi-valued and treat as concatenation
            Value labelValue = getValue(values, "label", i);
            Expression labelExpression;
            if (labelValue instanceof MultiValue) {
                MultiValue m = (MultiValue) labelValue;
                List<Expression> parts = new ArrayList<>();
                for (Value mv : m.values) {
                    parts.add(mv.toExpression());
                }

                labelExpression =
                        FF.function("Concatenate", parts.toArray(new Expression[parts.size()]));
            } else {
                labelExpression = labelValue.toExpression();
            }
            tb.label(labelExpression);

            Expression[] anchor = getExpressionArray(values, "label-anchor", i);
            Expression[] offsets = getExpressionArray(values, "label-offset", i);
            if (offsets != null && offsets.length == 1) {
                tb.linePlacement().offset(offsets[0]);
            } else if (offsets != null || anchor != null) {
                PointPlacementBuilder ppb = tb.pointPlacement();
                if (anchor != null) {
                    if (anchor.length == 2) {
                        ppb.anchor().x(anchor[0]);
                        ppb.anchor().y(anchor[1]);
                    } else if (anchor.length == 1) {
                        ppb.anchor().x(anchor[0]);
                        ppb.anchor().y(anchor[0]);
                    } else {
                        throw new IllegalArgumentException(
                                "Invalid anchor specification, should be two "
                                        + "floats between 0 and 1 with a space in between, instead it is "
                                        + getValue(values, "label-anchor", i));
                    }
                }
                if (offsets != null) {
                    if (offsets.length == 2) {
                        ppb.displacement().x(offsets[0]);
                        ppb.displacement().y(offsets[1]);
                    } else if (offsets.length == 1) {
                        ppb.displacement().x(offsets[0]);
                        ppb.displacement().y(offsets[0]);
                    } else {
                        throw new IllegalArgumentException(
                                "Invalid anchor specification, should be two "
                                        + "floats (or 1 for line placement with a certain offset) instead it is "
                                        + getValue(values, "label-anchor", i));
                    }
                }
            }
            Expression rotation = getMeasureExpression(values, "label-rotation", i, "deg");
            if (rotation != null) {
                tb.pointPlacement().rotation(rotation);
            }
            for (Value shieldValue : getMultiValue(values, "shield", i)) {
                new SubgraphicBuilder("shield", shieldValue, values, cssRule, i) {

                    @Override
                    protected GraphicBuilder getGraphicBuilder() {
                        return tb.shield();
                    }
                };
            }
            // the color
            Expression fill = getExpression(values, "font-fill", i);
            if (fill != null) {
                tb.fill().color(fill);
            }
            Expression opacity = getExpression(values, "font-opacity", i);
            if (opacity != null) {
                tb.fill().opacity(opacity);
            }
            // the fontdi
            Map<String, List<Value>> fontLikeProperties =
                    cssRule.getPropertyValues(PseudoClass.ROOT, "font");
            if (!fontLikeProperties.isEmpty()
                    && (fontLikeProperties.size() > 1
                            || fontLikeProperties.get("font-fill") == null)) {
                int maxSize =
                        getMaxMultiValueSize(
                                values,
                                i,
                                "font-family",
                                "font-style",
                                "font-weight",
                                "font-family");
                for (int j = 0; j < maxSize; j++) {
                    FontBuilder fb = tb.newFont();
                    Expression fontFamily =
                            getExpression(getValueInMulti(values, "font-family", i, j));
                    if (fontFamily != null) {
                        fb.family(fontFamily);
                    }
                    Expression fontStyle =
                            getExpression(getValueInMulti(values, "font-style", i, j));
                    if (fontStyle != null) {
                        fb.style(fontStyle);
                    }
                    Expression fontWeight =
                            getExpression(getValueInMulti(values, "font-weight", i, j));
                    if (fontWeight != null) {
                        fb.weight(fontWeight);
                    }
                    Expression fontSize =
                            getMeasureExpression(getValueInMulti(values, "font-size", i, j), "px");
                    if (fontSize != null) {
                        fb.size(fontSize);
                    }
                }
            }
            // the halo
            if (!cssRule.getPropertyValues(PseudoClass.ROOT, "halo").isEmpty()) {
                HaloBuilder hb = tb.halo();
                Expression haloRadius = getMeasureExpression(values, "halo-radius", i, "px");
                if (haloRadius != null) {
                    hb.radius(haloRadius);
                }
                Expression haloColor = getExpression(values, "halo-color", i);
                if (haloColor != null) {
                    hb.fill().color(haloColor);
                }
                Expression haloOpacity = getExpression(values, "halo-opacity", i);
                if (haloOpacity != null) {
                    hb.fill().opacity(haloOpacity);
                }
            }
            Expression priority = getExpression(values, "label-priority", i);
            if (priority == null) {
                // for backwards compatibility
                priority = getExpression(values, "-gt-label-priority", i);
            }
            if (priority != null) {
                tb.priority(priority);
            }
            addVendorOptions(tb, TEXT_VENDOR_OPTIONS, values, i);
        }
    }

    /**
     * Builds a raster symbolizer into the current rule, if a <code>raster-channels</code> property
     * is found
     *
     * @param cssRule
     * @param ruleBuilder
     */
    private void addRasterSymbolizer(CssRule cssRule, RuleBuilder ruleBuilder) {
        Map<String, List<Value>> values =
                cssRule.getPropertyValues(PseudoClass.ROOT, "raster", "rce");
        if (values == null || values.isEmpty()) {
            return;
        }

        int repeatCount = getMaxRepeatCount(values);
        for (int i = 0; i < repeatCount; i++) {
            RasterSymbolizerBuilder rb = ruleBuilder.raster();
            Expression[] channelExpressions = getExpressionArray(values, "raster-channels", i);
            String[] constrastEnhancements =
                    getStringArray(values, "raster-contrast-enhancement", i);
            HashMap<String, Expression> constrastParameters = new HashMap<>();
            for (String cssKey : values.keySet()) {
                String vendorOptionKey = cssKey;
                if (vendorOptionKey.startsWith("-gt-")) {
                    vendorOptionKey = vendorOptionKey.substring(4);
                }
                String sldKey = CONTRASTENHANCMENT_VENDOR_OPTIONS.get(vendorOptionKey);
                if (sldKey != null) {
                    constrastParameters.put(sldKey, getExpression(values, cssKey, i));
                }
            }
            Expression[] gammas = getExpressionArray(values, "raster-gamma", i);
            if (!"auto".equals(channelExpressions[0].evaluate(null, String.class))) {
                ChannelSelectionBuilder cs = rb.channelSelection();
                if (channelExpressions.length == 1) {
                    applyContrastEnhancement(
                            cs.gray().channelName(channelExpressions[0]).contrastEnhancement(),
                            constrastEnhancements,
                            constrastParameters,
                            gammas,
                            0);
                } else if (channelExpressions.length == 2 || channelExpressions.length > 3) {
                    throw new IllegalArgumentException(
                            "raster-channels can accept the name of one or three bands, not "
                                    + channelExpressions.length);
                } else {
                    applyContrastEnhancement(
                            cs.red().channelName(channelExpressions[0]).contrastEnhancement(),
                            constrastEnhancements,
                            constrastParameters,
                            gammas,
                            0);
                    applyContrastEnhancement(
                            cs.green().channelName(channelExpressions[1]).contrastEnhancement(),
                            constrastEnhancements,
                            constrastParameters,
                            gammas,
                            1);
                    applyContrastEnhancement(
                            cs.blue().channelName(channelExpressions[2]).contrastEnhancement(),
                            constrastEnhancements,
                            constrastParameters,
                            gammas,
                            2);
                }
            } else {
                applyContrastEnhancement(
                        rb.contrastEnhancement(),
                        constrastEnhancements,
                        constrastParameters,
                        gammas,
                        0);
            }

            Expression opacity = getExpression(values, "raster-opacity", i);
            if (opacity != null) {
                rb.opacity(opacity);
            }
            Expression geom = getExpression(values, "raster-geometry", i);
            if (geom != null) {
                rb.geometry(geom);
            }
            Value v = getValue(values, "raster-color-map", i);
            if (v != null) {
                if (v instanceof Function) {
                    v = new MultiValue(v);
                }
                if (!(v instanceof MultiValue)) {
                    throw new IllegalArgumentException(
                            "Invalid color map, it must be comprised of one or more color-map-entry function: "
                                    + v);
                } else {
                    MultiValue cm = (MultiValue) v;
                    ColorMapBuilder cmb = rb.colorMap();
                    for (Value entry : cm.values) {
                        if (!(entry instanceof Function)) {
                            throw new IllegalArgumentException(
                                    "Invalid color map content, it must be a color-map-entry function"
                                            + entry);
                        }
                        Function f = (Function) entry;
                        if (!"color-map-entry".equals(f.name)) {
                            throw new IllegalArgumentException(
                                    "Invalid color map content, it must be a color-map-entry function"
                                            + entry);
                        } else if (f.parameters.size() < 2 || f.parameters.size() > 3) {
                            throw new IllegalArgumentException(
                                    "Invalid color map content, it must be a color-map-entry function "
                                            + "with either 2 parameters (color and value) or 3 parameters "
                                            + "(color, value and opacity)"
                                            + entry);
                        }
                        ColorMapEntryBuilder eb = cmb.entry();
                        eb.color(f.parameters.get(0).toExpression());
                        eb.quantity(f.parameters.get(1).toExpression());
                        if (f.parameters.size() == 3) {
                            eb.opacity(f.parameters.get(2).toExpression());
                        }
                    }
                    String type = getLiteral(values, "raster-color-map-type", i, null);
                    if (type != null) {
                        if ("intervals".equals(type)) {
                            cmb.type(ColorMap.TYPE_INTERVALS);
                        } else if ("ramp".equals(type)) {
                            cmb.type(ColorMap.TYPE_RAMP);
                        } else if ("values".equals(type)) {
                            cmb.type(ColorMap.TYPE_VALUES);
                        } else {
                            throw new IllegalArgumentException("Invalid color map type " + type);
                        }
                    }
                }
            }

            addVendorOptions(rb, RASTER_VENDOR_OPTIONS, values, i);
        }
    }

    /**
     * Applies contrast enhancement for the i-th band
     *
     * @param ceb
     * @param constrastEnhancements
     * @param constrastParameters
     * @param gammas
     * @param i
     */
    private void applyContrastEnhancement(
            ContrastEnhancementBuilder ceb,
            String[] constrastEnhancements,
            Map<String, Expression> constrastParameters,
            Expression[] gammas,
            int i) {
        if (constrastEnhancements != null && constrastEnhancements.length > 0) {
            String contrastEnhancementName;
            if (constrastEnhancements.length > i) {
                contrastEnhancementName = constrastEnhancements[0];
            } else {
                contrastEnhancementName = constrastEnhancements[i];
            }

            //
            if ("histogram".equals(contrastEnhancementName)) {
                ceb.histogram(constrastParameters);
            } else if ("normalize".equals(contrastEnhancementName)) {
                ceb.normalize(constrastParameters);
            } else if ("exponential".equals(contrastEnhancementName)) {
                ceb.exponential(constrastParameters);
            } else if ("logarithmic".equals(contrastEnhancementName)) {
                ceb.logarithmic(constrastParameters);
            } else if (!"none".equals(contrastEnhancementName)) {
                //
                throw new IllegalArgumentException(
                        "Invalid contrast enhancement name "
                                + contrastEnhancementName
                                + ", valid values are 'none', 'histogram', 'normalize', 'exponential' or 'logarithmic'");
            }
        } else {
            ceb.unset();
        }
        if (gammas != null && gammas.length > 0) {
            Expression gamma;
            if (gammas.length > i) {
                gamma = gammas[0];
            } else {
                gamma = gammas[i];
            }
            ceb.gamma(gamma);
        }
    }

    /** Builds a graphic object into the current style build parent */
    abstract class SubgraphicBuilder {
        public SubgraphicBuilder(
                String propertyName,
                Value v,
                Map<String, List<Value>> values,
                CssRule cssRule,
                int i) {
            if (v != null) {
                if (!(v instanceof Function)) {
                    throw new IllegalArgumentException(
                            "The value of '" + propertyName + "' must be a symbol or a url");
                }
                Function f = (Function) v;
                GraphicBuilder gb = getGraphicBuilder();
                if (Function.SYMBOL.equals(f.name)) {
                    buildMark(f.parameters.get(0), cssRule, propertyName, i, gb);
                } else if (Function.URL.equals(f.name)) {
                    Value graphicLocation = f.parameters.get(0);
                    String location = graphicLocation.toLiteral();
                    // to turn stuff into SLD we need to make sure the URL is a valid one
                    // try {
                    // new URL(location);
                    // } catch (MalformedURLException e) {
                    // location = "file://" + location;
                    // }
                    String mime = getLiteral(values, propertyName + "-mime", i, "image/jpeg");
                    gb.externalGraphic(location, mime);
                } else {
                    throw new IllegalArgumentException(
                            "'"
                                    + propertyName
                                    + "' accepts either a 'symbol' or a 'url' function, the following function is unrecognized: "
                                    + f);
                }

                Expression rotation =
                        getMeasureExpression(values, propertyName + "-rotation", i, "deg");
                if (rotation != null) {
                    gb.rotation(rotation);
                }
                Expression size = getMeasureExpression(values, propertyName + "-size", i, "px");
                if (size != null) {
                    gb.size(size);
                }
                Expression[] anchor = getExpressionArray(values, propertyName + "-anchor", i);
                Expression[] offsets = getExpressionArray(values, propertyName + "-offset", i);
                if (anchor != null) {
                    if (anchor.length == 2) {
                        gb.anchor().x(anchor[0]);
                        gb.anchor().y(anchor[1]);
                    } else if (anchor.length == 1) {
                        gb.anchor().x(anchor[0]);
                        gb.anchor().y(anchor[0]);
                    } else {
                        throw new IllegalArgumentException(
                                "Invalid anchor specification, should be two "
                                        + "floats between 0 and 1 with a space in between, instead it is "
                                        + getValue(values, propertyName + "-anchor", i));
                    }
                }
                if (offsets != null) {
                    if (offsets.length == 2) {
                        gb.displacement().x(offsets[0]);
                        gb.displacement().y(offsets[1]);
                    } else if (offsets.length == 1) {
                        gb.displacement().x(offsets[0]);
                        gb.displacement().y(offsets[0]);
                    } else {
                        throw new IllegalArgumentException(
                                "Invalid anchor specification, should be two "
                                        + "floats (or 1 for line placement with a certain offset) instead it is "
                                        + getValue(values, propertyName + "-anchor", i));
                    }
                }
                if ("mark".equals(propertyName)) {
                    Expression opacity = getExpression(values, "mark-opacity", i);
                    if (opacity != null) {
                        gb.opacity(opacity);
                    }
                }
            }
        }

        protected abstract GraphicBuilder getGraphicBuilder();
    }

    /**
     * Builds the fill using a FillBuilder
     *
     * @param cssRule
     * @param fb
     * @param values
     * @param i
     */
    private void buildFill(
            CssRule cssRule, final FillBuilder fb, Map<String, List<Value>> values, int i) {
        for (Value fillValue : getMultiValue(values, "fill", i)) {
            if (Function.isGraphicsFunction(fillValue)) {
                new SubgraphicBuilder("fill", fillValue, values, cssRule, i) {

                    @Override
                    protected GraphicBuilder getGraphicBuilder() {
                        return fb.graphicFill();
                    }
                };
            } else if (fillValue != null) {
                fb.color(getExpression(fillValue));
            }
        }
        Expression opacity = getExpression(values, "fill-opacity", i);
        if (opacity != null) {
            fb.opacity(opacity);
        }
    }

    /**
     * Adds a line symbolizer, assuming the <code>stroke<code> property is found
     *
     * @param cssRule
     * @param ruleBuilder
     */
    private void addLineSymbolizer(CssRule cssRule, RuleBuilder ruleBuilder) {
        Map<String, List<Value>> values = cssRule.getPropertyValues(PseudoClass.ROOT, "stroke");
        if (values == null || values.isEmpty()) {
            return;
        }
        int repeatCount = getMaxRepeatCount(values);
        for (int i = 0; i < repeatCount; i++) {
            if (getValue(values, "stroke", i) == null) {
                continue;
            }

            LineSymbolizerBuilder lb = ruleBuilder.line();
            Expression strokeGeometry = getExpression(values, "stroke-geometry", i);
            if (strokeGeometry != null) {
                lb.geometry(strokeGeometry);
            }

            Expression strokeOffset = getExpression(values, "stroke-offset", i);
            if (strokeOffset != null && !isZero(strokeOffset)) {
                lb.perpendicularOffset(strokeOffset);
            }

            StrokeBuilder strokeBuilder = lb.stroke();
            buildStroke(cssRule, strokeBuilder, values, i);
            addVendorOptions(lb, LINE_VENDOR_OPTIONS, values, i);
        }
    }

    /**
     * Returns true if the expression is a constant value zero
     *
     * @param expression
     * @return
     */
    private boolean isZero(Expression expression) {
        if (!(expression instanceof org.opengis.filter.expression.Literal)) {
            return false;
        }
        org.opengis.filter.expression.Literal l =
                (org.opengis.filter.expression.Literal) expression;
        return l.evaluate(null, Double.class) == 0;
    }

    /**
     * Builds a stroke using the stroke buidler for the i-th set of property values
     *
     * @param cssRule
     * @param strokeBuilder
     * @param values
     * @param i
     */
    private void buildStroke(
            CssRule cssRule,
            final StrokeBuilder strokeBuilder,
            final Map<String, List<Value>> values,
            final int i) {

        boolean simpleStroke = false;
        for (Value strokeValue : getMultiValue(values, "stroke", i)) {
            if (Function.isGraphicsFunction(strokeValue)) {
                new SubgraphicBuilder("stroke", strokeValue, values, cssRule, i) {

                    @Override
                    protected GraphicBuilder getGraphicBuilder() {
                        String repeat = getLiteral(values, "stroke-repeat", i, "repeat");
                        if ("repeat".equals(repeat)) {
                            return strokeBuilder.graphicStroke();
                        } else {
                            return strokeBuilder.fillBuilder();
                        }
                    }
                };
            } else if (strokeValue != null) {
                simpleStroke = true;
                strokeBuilder.color(strokeValue.toExpression());
            }
        }
        if (simpleStroke) {
            Expression opacity = getExpression(values, "stroke-opacity", i);
            if (opacity != null) {
                strokeBuilder.opacity(opacity);
            }
            Expression width = getMeasureExpression(values, "stroke-width", i, "px");
            if (width != null) {
                strokeBuilder.width(width);
            }
            Expression lineCap = getExpression(values, "stroke-linecap", i);
            if (lineCap != null) {
                strokeBuilder.lineCap(lineCap);
            }
            Expression lineJoin = getExpression(values, "stroke-linejoin", i);
            if (lineJoin != null) {
                strokeBuilder.lineJoin(lineJoin);
            }
        }
        final Value dasharrayValue = getValue(values, "stroke-dasharray", i);
        if (isLiterals(dasharrayValue)) {
            float[] dasharray = getFloatArray(values, "stroke-dasharray", i);
            if (dasharray != null) {
                strokeBuilder.dashArray(dasharray);
            }
        } else if (dasharrayValue instanceof MultiValue) {
            MultiValue mv = (MultiValue) dasharrayValue;
            List<Expression> expressions = new ArrayList<>();
            for (Value v : mv.values) {
                expressions.add(v.toExpression());
            }
            strokeBuilder.dashArray(expressions);
        } else if (dasharrayValue != null) {
            List<Expression> expressions = new ArrayList<>();
            expressions.add(dasharrayValue.toExpression());
            strokeBuilder.dashArray(expressions);
        }

        Expression dashOffset = getMeasureExpression(values, "stroke-dashoffset", i, "px");
        if (dashOffset != null) {
            strokeBuilder.dashOffset(dashOffset);
        }
    }

    /**
     * Returns true if the value is a {@link Literal}, or a {@link MultiValue} made of {@link
     * Literal}
     *
     * @param value
     * @return
     */
    private boolean isLiterals(Value value) {
        if (value instanceof Literal) {
            return true;
        } else if (value instanceof MultiValue) {
            MultiValue mv = (MultiValue) value;
            for (Value v : mv.values) {
                if (!(v instanceof Literal)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the vendor options available
     *
     * @param sb
     * @param vendorOptions
     * @param values
     * @param idx
     */
    private void addVendorOptions(
            SymbolizerBuilder<?> sb,
            Map<String, String> vendorOptions,
            Map<String, List<Value>> values,
            int idx) {
        for (String cssKey : values.keySet()) {
            String vendorOptionKey = cssKey;
            if (vendorOptionKey.startsWith("-gt-")) {
                vendorOptionKey = vendorOptionKey.substring(4);
            }
            String sldKey = vendorOptions.get(vendorOptionKey);
            if (sldKey != null) {
                String value = getLiteral(values, cssKey, idx, null);
                if (value != null) {
                    sb.option(sldKey, value);
                }
            }
        }
    }

    /**
     * Builds a mark into the graphic builder from the idx-th set of property alues
     *
     * @param markName
     * @param cssRule
     * @param indexedPseudoClass
     * @param idx
     * @param gb
     */
    private void buildMark(
            Value markName,
            CssRule cssRule,
            String indexedPseudoClass,
            int idx,
            GraphicBuilder gb) {
        MarkBuilder mark = gb.mark();
        mark.name(markName.toExpression());
        // see if we have a pseudo-selector for this idx
        Map<String, List<Value>> values =
                getValuesForIndexedPseudoClass(cssRule, indexedPseudoClass, idx);
        if (values == null || values.isEmpty()) {
            mark.fill().reset();
            mark.stroke().reset();
        } else {
            // unless specified and empty, a mark always has a fill and a stroke
            if (values.containsKey("fill") && values.get("fill") != null) {
                FillBuilder fb = mark.fill();
                buildFill(cssRule, fb, values, idx);
            } else if (!values.containsKey("fill")) {
                mark.fill();
            }

            if (values.containsKey("stroke") && values.get("stroke") != null) {
                StrokeBuilder sb = mark.stroke();
                buildStroke(cssRule, sb, values, idx);
            } else if (!values.containsKey("stroke")) {
                mark.stroke();
            }
        }
        Expression size = getMeasureExpression(values, "size", idx, "px");
        if (size != null) {
            gb.size(size);
        }
        Expression rotation = getMeasureExpression(values, "rotation", idx, "deg");
        if (rotation != null) {
            gb.rotation(rotation);
        }
    }

    /**
     * Returns the set of values for the idx-th pseudo-class taking into account both generic and
     * non indexed pseudo class names
     *
     * @param cssRule
     * @param pseudoClassName
     * @param idx
     * @return
     */
    private Map<String, List<Value>> getValuesForIndexedPseudoClass(
            CssRule cssRule, String pseudoClassName, int idx) {
        Map<String, List<Value>> combined = new LinkedHashMap<>();
        // catch all ones
        combined.putAll(cssRule.getPropertyValues(PseudoClass.newPseudoClass("symbol")));
        // catch all index specific
        combined.putAll(cssRule.getPropertyValues(PseudoClass.newPseudoClass("symbol", idx + 1)));
        // symbol specific ones
        combined.putAll(cssRule.getPropertyValues(PseudoClass.newPseudoClass(pseudoClassName)));
        // symbol and index specific ones
        combined.putAll(
                cssRule.getPropertyValues(PseudoClass.newPseudoClass(pseudoClassName, idx + 1)));
        return combined;
    }

    /**
     * Builds an expression out of the i-th value
     *
     * @param valueMap
     * @param name
     * @param i
     * @return
     */
    private Expression getExpression(Map<String, List<Value>> valueMap, String name, int i) {
        Value v = getValue(valueMap, name, i);
        return getExpression(v);
    }

    /**
     * Builds/grabs an expression from the specified value, if a multi value is passed the first
     * value will be used
     *
     * @param v
     * @return
     */
    private Expression getExpression(Value v) {
        if (v == null) {
            return null;
        } else {
            if (v instanceof MultiValue) {
                return ((MultiValue) v).values.get(0).toExpression();
            } else {
                return v.toExpression();
            }
        }
    }

    /**
     * Returns an expression for the i-th value of the specified property, taking into account units
     * of measure
     *
     * @param valueMap
     * @param name
     * @param i
     * @param defaultUnit
     * @return
     */
    private Expression getMeasureExpression(
            Map<String, List<Value>> valueMap, String name, int i, String defaultUnit) {
        Value v = getValue(valueMap, name, i);
        return getMeasureExpression(v, defaultUnit);
    }

    private Expression getMeasureExpression(Value v, String defaultUnit) {
        if (v == null) {
            return null;
        } else if (v instanceof Literal) {
            String literal = v.toLiteral();
            if (literal.endsWith(defaultUnit)) {
                String simplified = literal.substring(0, literal.length() - defaultUnit.length());
                return FF.literal(simplified);
            } else {
                return FF.literal(literal);
            }
        } else {
            return v.toExpression();
        }
    }

    /**
     * Returns the i-th value of the specified property
     *
     * @param valueMap
     * @param name
     * @param i
     * @return
     */
    private Value getValue(Map<String, List<Value>> valueMap, String name, int i) {
        List<Value> values = valueMap.get(name);
        if (values == null || values.isEmpty()) {
            return null;
        }

        Value result = null;
        if (values.size() == 1) {
            result = values.get(0);
        } else if (i < values.size()) {
            result = values.get(i);
        }

        if (result == null || result instanceof Value.None) {
            return null;
        }
        return result;
    }

    private List<Value> getMultiValue(Map<String, List<Value>> valueMap, String name, int i) {
        Value value = getValue(valueMap, name, i);
        if (value instanceof MultiValue) {
            return ((MultiValue) value).values;
        } else if (value == null) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(value);
        }
    }

    public int getMaxMultiValueSize(Map<String, List<Value>> valueMap, int i, String... names) {
        int max = 0;
        for (String name : names) {
            List<Value> values = getMultiValue(valueMap, name, i);
            int size = values.size();
            if (size > max) {
                max = size;
            }
        }
        return max;
    }

    public Value getValueInMulti(
            Map<String, List<Value>> valueMap, String name, int i, int valueIdx) {
        List<Value> values = getMultiValue(valueMap, name, i);
        if (values.isEmpty()) {
            return null;
        } else if (values.size() <= valueIdx) {
            return values.get(values.size() - 1);
        } else {
            return values.get(valueIdx);
        }
    }

    /**
     * Returns the i-th value of the specified property, as a literal
     *
     * @param valueMap
     * @param name
     * @param i
     * @param defaultValue
     * @return
     */
    private String getLiteral(
            Map<String, List<Value>> valueMap, String name, int i, String defaultValue) {
        Value v = getValue(valueMap, name, i);
        if (v == null) {
            return defaultValue;
        } else {
            return v.toLiteral();
        }
    }

    /**
     * Returns the i-th value of the specified property, as a array of floats
     *
     * @param valueMap
     * @param name
     * @param i
     * @return
     */
    private float[] getFloatArray(Map<String, List<Value>> valueMap, String name, int i) {
        double[] doubles = getDoubleArray(valueMap, name, i);
        if (doubles == null) {
            return null;
        } else {
            float[] floats = new float[doubles.length];
            for (int j = 0; j < doubles.length; j++) {
                floats[j] = (float) doubles[j];
            }
            return floats;
        }
    }

    /**
     * Returns the i-th value of the specified property, as a array of doubles
     *
     * @param valueMap
     * @param name
     * @param i
     * @return
     */
    private double[] getDoubleArray(Map<String, List<Value>> valueMap, String name, int i) {
        Value v = getValue(valueMap, name, i);
        if (v == null) {
            return null;
        }
        if (v instanceof MultiValue) {
            MultiValue m = (MultiValue) v;
            if (m.values.size() == 0) {
                return null;
            }
            double[] result = new double[m.values.size()];
            for (int j = 0; j < m.values.size(); j++) {
                String literal = m.values.get(j).toLiteral();
                if (literal.endsWith("%")) {
                    literal = literal.substring(0, literal.length() - 1);
                    double d = Double.parseDouble(literal);
                    result[j] = d / 100d;
                } else {
                    result[j] = Double.parseDouble(literal);
                }
            }
            return result;

        } else {
            return new double[] {Double.parseDouble(v.toLiteral())};
        }
    }

    /**
     * Returns the i-th value of the specified property, as a array of strings
     *
     * @param valueMap
     * @param name
     * @param i
     * @return
     */
    private String[] getStringArray(Map<String, List<Value>> valueMap, String name, int i) {
        Value v = getValue(valueMap, name, i);
        if (v == null) {
            return null;
        }
        if (v instanceof MultiValue) {
            MultiValue m = (MultiValue) v;
            if (m.values.size() == 0) {
                return null;
            }
            String[] result = new String[m.values.size()];
            for (int j = 0; j < m.values.size(); j++) {
                result[j] = m.values.get(j).toLiteral();
            }
            return result;

        } else {
            return new String[] {v.toLiteral()};
        }
    }

    /**
     * Returns the i-th value of the specified property, as a array of expressions
     *
     * @param valueMap
     * @param name
     * @param i
     * @return
     */
    private Expression[] getExpressionArray(Map<String, List<Value>> valueMap, String name, int i) {
        Value v = getValue(valueMap, name, i);
        if (v == null) {
            return null;
        }
        if (v instanceof MultiValue) {
            MultiValue m = (MultiValue) v;
            if (m.values.size() == 0) {
                return null;
            }
            Expression[] result = new Expression[m.values.size()];
            for (int j = 0; j < m.values.size(); j++) {
                result[j] = m.values.get(j).toExpression();
            }
            return result;

        } else {
            return new Expression[] {v.toExpression()};
        }
    }

    /**
     * Returns the max number of property values in the provided property set (for repeated
     * symbolizers)
     *
     * @param valueMap
     * @return
     */
    private int getMaxRepeatCount(Map<String, List<Value>> valueMap) {
        int max = 1;
        for (List<Value> values : valueMap.values()) {
            max = Math.max(max, values.size());
        }

        return max;
    }

    public static void main(String[] args) throws IOException, TransformerException {
        if (args.length != 2) {
            System.err.println("Usage: CssTranslator <input.css> <output.sld>");
            System.exit(-1);
        }
        File input = new File(args[0]);
        if (!input.exists()) {
            System.err.println("Could not locate input file " + input.getPath());
            System.exit(-2);
        }
        File output = new File(args[1]);
        File outputParent = output.getParentFile();
        if (!outputParent.exists() && !outputParent.mkdirs()) {
            System.err.println(
                    "Output file parent directory does not exist, and cannot be created: "
                            + outputParent.getPath());
            System.exit(-2);
        }

        long start = System.currentTimeMillis();

        String css = FileUtils.readFileToString(input);
        Stylesheet styleSheet = CssParser.parse(css);

        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setLevel(java.util.logging.Level.FINE);

        org.geotools.util.logging.Logging.getLogger(CssTranslator.class)
                .setLevel(java.util.logging.Level.FINE);
        org.geotools.util.logging.Logging.getLogger(CssTranslator.class).addHandler(handler);

        CssTranslator translator = new CssTranslator();
        Style style = translator.translate(styleSheet);

        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();
        NamedLayer layer = styleFactory.createNamedLayer();
        layer.addStyle((org.geotools.styling.Style) style);
        sld.layers().add(layer);
        SLDTransformer tx = new SLDTransformer();
        tx.setIndentation(2);
        try (FileOutputStream fos = new FileOutputStream(output)) {
            tx.transform(sld, fos);
        }
        long end = System.currentTimeMillis();

        System.out.println("Translation performed in " + (end - start) / 1000d + " seconds");
    }
}
