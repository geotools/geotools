/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.util.List;
import java.util.Map;
import org.opengis.filter.expression.Expression;
import org.opengis.metadata.citation.OnLineResource;

/**
 * How to style a feature type. This is introduced as a convenient package that can be used
 * independently for feature types, for example in GML Default Styling. The "layer" concept is
 * discarded inside of this element and all processing is relative to feature types. The
 * FeatureTypeName is allowed to be optional, but only one feature type may be in context and it
 * must match the syntax and semantics of all attribute references inside of the FeatureTypeStyle.
 *
 * <p>The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188">OGC Styled-Layer Descriptor
 * Report (OGC 02-070) version 1.0.0.</a>:
 *
 * <pre><code>
 * &lt;xsd:element name="FeatureTypeStyle"&gt;
 * &lt;xsd:annotation&gt;
 *   &lt;xsd:documentation&gt;
 *     A FeatureTypeStyle contains styling information specific to one
 *    feature type.  This is the SLD level that separates the 'layer'
 *     handling from the 'feature' handling.
 *   &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:Title" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:Abstract" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:FeatureTypeName" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:SemanticTypeIdentifier" minOccurs="0"
 *                   maxOccurs="unbounded"/&gt;
 *       &lt;xsd:element ref="sld:Rule" maxOccurs="unbounded"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 *
 * @version $Id$
 * @author James Macgill, CCG
 */
public interface FeatureTypeStyle extends org.opengis.style.FeatureTypeStyle {

    /** This option influences how multiple rules matching the same feature are evaluated */
    public static String KEY_EVALUATION_MODE = "ruleEvaluation";

    /** The standard behavior, all the matching rules are executed */
    public static String VALUE_EVALUATION_MODE_ALL = "all";

    /** Only the first matching rule gets executed, all the others are skipped */
    public static String VALUE_EVALUATION_MODE_FIRST = "first";

    /**
     * Applies a color composition/blending operation at the feature type style level (that is,
     * blending the current FTS level against the map below it).
     *
     * <p>The syntax for this key is {code}<VendorOption
     * name="composite">name[,opacity]</VendorOption>{code} where:
     *
     * <ul>
     *   <li>{code}name is one of the <a href="http://www.w3.org/TR/compositing-1/">SVG composition
     *       operations</a>, in particular, copy, destination, source-over, destination-over,
     *       source-in, destination-in, source-out, destination-out, source-atop, destination-atop,
     *       xor, multiply, screen, overlay, darken, lighten, color-dodge, color-burn, hard-light,
     *       soft-light, difference, exclusion
     *   <li>{opacity} indicates the opacity level to be used during the operation, defauls to 1
     * </ul>
     *
     * For example:
     *
     * <ul>
     *   <li>{code}<VendorOption name="composite">source-atop, 0.5</VendorOption>{code} composes the
     *       current FTS exclusively where the previous map has already been drawn, using a 0.5
     *       opacity level
     *   <li>{code}<VendorOption name="composite">multiply</VendorOption>{code} blends the current
     *       FTS with the underlying map using color multiplication
     * </ul>
     *
     * <p>The same vendor option can also be applied at the symbolizer level to achieve different
     * effects (feature by feature composition as oppose to layer by layer one).
     *
     * <p>Important note: for most compositing operation to work properly, the graphics used for the
     * rendering should be derived from an image that has an alpha channel and transparent
     * background (as most of the operations consider the transparency of the target surface in
     * their math)
     */
    public static String COMPOSITE = "composite";

    /**
     * Boolean value, if true the current feature type style will be treated as a base for the
     * subsequent feature type styles in the rendering stack (including other layer ones) as opposed
     * to use the merged backdrop rendered so far. When the top of the stack is reached, or another
     * base is found, this FTS will be merged into the backdrop, eventually using the indicated
     * composite operator
     */
    public static String COMPOSITE_BASE = "composite-base";

    /**
     * String value controlling the order in which the features are loaded from the data source, and
     * thus painted, in this feature type style.
     *
     * <p>The syntax is <code>Attribute1 {A|D},Attribute2 {A|D}...</code>, <code>A</code> is
     * ascending, <code>D</code> is descending. The sorting direction is optional and defaults to
     * ascending if not specified.
     *
     * <p>E.g., <code>cat D,name</code> sorts data by <code>cat</code> in descending order, and then
     * by ascending <code>name</code> within all features having the same <code>cat</code> value.
     */
    public static String SORT_BY = "sortBy";

    /**
     * String value controlling cross layer z-ordering. Several feature type styles in the same
     * sortByGroup will have their features globally ordered before painting, for example, in order
     * to respect their real world relationships. FeatureTypeStyle are grouped only if they are
     * adjacent in the overall MapContent (even across layers). In case compositing is used in the
     * same FeatureTypeStyle, the first value in group will be used for the entire group.
     */
    public static String SORT_BY_GROUP = "sortByGroup";

    void setName(String name);

    /**
     * Description for this style.
     *
     * @return Human readable description for use in user interfaces
     * @since 2.5.x
     */
    Description getDescription();

    /**
     * Rules govern the appearance of any given feature to be styled by this styler.
     *
     * <p>This is *the* list being used to manage the rules!
     *
     * @since GeoTools 2.2.M3, GeoAPI 2.0
     */
    List<Rule> rules();

    /**
     * It is common to have a style coming from a external xml file, this method provide a way to
     * get the original source if there is one.
     *
     * @param online location external file defining this style, or null if not available
     */
    void setOnlineResource(OnLineResource online);

    void accept(org.geotools.styling.StyleVisitor visitor);

    /**
     * The eventual transformation to be applied before rendering the data (should be an expression
     * taking a feature collection or a grid coverage as the evaluation context and returns a
     * feature collection or a grid coverage as an output)
     */
    Expression getTransformation();

    /**
     * Sets the eventual transformation to be applied before rendering the data (should be an
     * expression taking a feature collection or a grid coverage as an input and returns a feature
     * collection or a grid coverage as an output)
     */
    void setTransformation(Expression transformation);

    /** Determines if a vendor option with the specific key has been set on this symbolizer. */
    boolean hasOption(String key);

    /**
     * Map of vendor options for the symbolizer.
     *
     * <p>Client code looking for the existence of a single option should use {@link
     * #hasOption(String)}
     */
    Map<String, String> getOptions();
}
