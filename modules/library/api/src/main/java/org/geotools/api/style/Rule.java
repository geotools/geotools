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
     * Description for this rule.
     *
     * @return Human readable description for use in user interfaces
     * @since 2.5.x
     */
    Description getDescription();

    /** */
    GraphicLegend getLegend();

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

    /**
     * This method returns the list of Symbolizer objects contained by this {@code Rule}.
     *
     * <p>We use a list of <? extends Symbolizer> to enable the possibility for an implementation to
     * return a special type of Symbolizer. This doesnt mean a Rule must return a list of
     * PointSymbolizer or TextSymbolizer only, no. The purpose of this if to offer the solution to
     * return different implementations like MutableSymbolizer or RichSymbolizer and then avoid
     * redundant cast in the code. If you dont intend to use a special interface you can override
     * this method by : List<Symbolizer> symbolizers();
     *
     * @return the list of Symbolizer
     */
    List<Symbolizer> symbolizers();

    /** @return Location where this style is defined; file or server; or null if unknown */
    OnLineResource getOnlineResource();

    /** Used to traverse the style data structure. */
    void accept(StyleVisitor visitor);

    Map<String, String> getOptions();
}
