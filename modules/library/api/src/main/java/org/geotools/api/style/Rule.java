/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.filter.Filter;
import org.geotools.api.metadata.citation.OnLineResource;

/**
 * A rule consists of two important parts: a {@linkplain Filter filter} and a list of {@linkplain
 * Symbol symbols}. When it is time to draw a given feature, the rendering engine examines each rule
 * in the FeatureStyle, first checking its Filter (or ElseFilter). If the Filter passes, then every
 * Symbolizer for that rule is applied to the given feature.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface Rule {

    /**
     * Returns a name for this rule. This can be any string that uniquely identifies this rule
     * within a given canvas. It is not meant to be human-friendly. (The "title" property is meant
     * to be human friendly.)
     *
     * @return a name for this rule.
     */
    String getName();

    /**
     * Sets the name of the rule.
     *
     * @param name The name of the rule. This provides a way to identify a rule.
     */
    void setName(String name);

    /**
     * Description for this rule.
     *
     * @return Human readable description for use in user interfaces
     * @since 2.5.x
     */
    Description getDescription();

    /**
     * Filter used to select content for this rule to display.
     *
     * <p>This filter is only consulted if isElseFilter is false.
     */
    void setFilter(Filter filter);

    /** @param isElse if this rule should accept any features not already rendered */
    void setElseFilter(boolean isElse);

    /** */
    GraphicLegend getLegend();

    /**
     * Description for this rule.
     *
     * @param description Human readable title and abstract.
     */
    void setDescription(Description description);

    /**
     * The smallest value for scale denominator at which symbolizers contained by this rule should
     * be applied.
     *
     * @param scale The smallest (inclusive) denominator value that this rule will be active for.
     */
    void setMinScaleDenominator(double scale);

    /**
     * The largest value for scale denominator at which symbolizers contained by this rule should be
     * applied.
     *
     * @param scale The largest (exclusive) denominator value that this rule will be active for.
     */
    void setMaxScaleDenominator(double scale);

    /**
     * This is the filter used to select content for this rule to display
     *
     * <p>
     *
     * @return Filter use to select content for this rule to display, Filter.INCLUDES to include all
     *     content; or use Filter.EXCLUDES to mark this as an "else" Rule accepting all remaining
     *     content
     */
    Filter getFilter();

    /**
     * Returns true if this {@code Rule} is to fire only if no other rules in the containing style
     * have fired yet. If this is true, then the {@linkplain #getFilter filter} must be
     * Filter.EXCLUDES.
     *
     * @return true if the filter is an else filter
     */
    boolean isElseFilter();

    /**
     * Returns the minimum value (inclusive) in the denominator of the current map scale at which
     * this {@code Rule} will fire. If, for example, the {@code MinScaleDenominator} were 10000,
     * then this rule would only fire at scales of 1:X where X is greater than 10000. A value of
     * zero indicates that there is no minimum.
     *
     * @return Min scale double value
     */
    double getMinScaleDenominator();

    /**
     * Returns the maximum value (exclusive) in the denominator of the current map scale at which
     * this {@code Rule} will fire. If, for example, the {@code MaxScaleDenominator} were 98765,
     * then this rule would only fire at scales of 1:X where X is less than 98765. A value of {@link
     * Double#POSITIVE_INFINITY} indicates that there is no maximum.
     *
     * @return Max scale double value
     */
    double getMaxScaleDenominator();

    /** @param legend */
    void setLegend(GraphicLegend legend);

    /**
     * The symbolizers contain the actual styling information for different geometry types. A single
     * feature may be rendered by more than one of the symbolizers returned by this method. It is
     * important that the symbolizers be applied in the order in which they are returned if the end
     * result is to be as intended. All symbolizers should be applied to all features which make it
     * through the filters in this rule regardless of the features' geometry. For example, a polygon
     * symbolizer should be applied to line geometries and even points. If this is not the desired
     * beaviour, ensure that either the filters block inappropriate features or that the
     * FeatureTypeStyler which contains this rule has its FeatureTypeName or SemanticTypeIdentifier
     * set appropriately.
     *
     * @return An array of symbolizers to be applied, in sequence, to all of the features addressed
     *     by the FeatureTypeStyler which contains this rule.
     */
    Symbolizer[] getSymbolizers();

    /**
     * Symbolizers used, in order, to portray the features selected by this rule.
     *
     * <p>Please note that this list may be modified direct.
     */
    List<Symbolizer> symbolizers();

    /** @return Location where this style is defined; file or server; or null if unknown */
    OnLineResource getOnlineResource();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /** @param resource Indicates where this style is defined */
    void setOnlineResource(OnLineResource resource);

    /** Determines if a vendor option with the specific key has been set on this Rule. */
    default boolean hasOption(String key) {
        return false;
    }

    /**
     * Map of vendor options for the Rule.
     *
     * <p>Client code looking for the existence of a single option should use {@link
     * #hasOption(String)}
     */
    default Map<String, String> getOptions() {
        return new HashMap<>();
    }

    /** Used to traverse the style data structure. */
    void accept(StyleVisitor visitor);
}
