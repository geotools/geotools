/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.SemanticType;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.util.Utilities;

/**
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
 *
 *
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class FeatureTypeStyle implements Cloneable, org.geotools.api.style.FeatureTypeStyle {

    /** This option influences how multiple rules matching the same feature are evaluated */
    public static final String KEY_EVALUATION_MODE = "ruleEvaluation";
    /** The standard behavior, all the matching rules are executed */
    public static final String VALUE_EVALUATION_MODE_ALL = "all";
    /** Only the first matching rule gets executed, all the others are skipped */
    public static final String VALUE_EVALUATION_MODE_FIRST = "first";
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
     *   <li>{opacity} indicates the opacity level to be used during the operation, defaults to 1
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
     * effects (feature by feature composition as opposed to layer by layer one).
     *
     * <p>Important note: for most compositing operation to work properly, the graphics used for the
     * rendering should be derived from an image that has an alpha channel and transparent
     * background (as most of the operations consider the transparency of the target surface in
     * their math)
     */
    public static final String COMPOSITE = "composite";
    /**
     * Boolean value, if true the current feature type style will be treated as a base for the
     * subsequent feature type styles in the rendering stack (including other layer ones) as opposed
     * to use the merged backdrop rendered so far. When the top of the stack is reached, or another
     * base is found, this FTS will be merged into the backdrop, eventually using the indicated
     * composite operator
     */
    public static final String COMPOSITE_BASE = "composite-base";
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
    public static final String SORT_BY = "sortBy";
    /**
     * String value controlling cross layer z-ordering. Several feature type styles in the same
     * sortByGroup will have their features globally ordered before painting, for example, in order
     * to respect their real world relationships. FeatureTypeStyle are grouped only if they are
     * adjacent in the overall MapContent (even across layers). In case compositing is used in the
     * same FeatureTypeStyle, the first value in group will be used for the entire group.
     */
    public static final String SORT_BY_GROUP = "sortByGroup";
    /**
     * String value allowing to control whether an SLD element should be included when applying the
     * style to render maps or a legends. The option can be used also on Rule and all the
     * Symbolizers. Possible values are normal, legendOnly, mapOnly.
     */
    public static final String VENDOR_OPTION_INCLUSION = "inclusion";
    /** This option influences how multiple rules matching the same feature are evaluated */

    private List<Rule> rules = new ArrayList<>();
    private Set<SemanticType> semantics = new LinkedHashSet<>();
    private Id featureInstances = null;
    private Set<Name> featureTypeNames = new LinkedHashSet<>();

    private Description description = new Description();
    private String name = "name";
    private OnLineResource online = null;
    private Expression transformation = null;

    protected Map<String, String> options;

    /** Creates a new instance of FeatureTypeStyleImpl */
    protected FeatureTypeStyle(Rule... rules) {
        this(Arrays.asList(rules));
    }

    protected FeatureTypeStyle(List<Rule> arules) {
        rules = new ArrayList<>();
        rules.addAll(arules);
    }

    /** Creates a new instance of FeatureTypeStyleImpl */
    protected FeatureTypeStyle() {
        rules = new ArrayList<>();
    }

    public FeatureTypeStyle(org.geotools.api.style.FeatureTypeStyle fts) {
        this.description = new Description(fts.getDescription());
        this.featureInstances = fts.getFeatureInstanceIDs();
        this.featureTypeNames = new LinkedHashSet<>(fts.featureTypeNames());
        this.name = fts.getName();
        this.rules = new ArrayList<>();
        if (fts.rules() != null) {
            for (org.geotools.api.style.Rule rule : fts.rules()) {
                rules.add(Rule.cast(rule)); // need to deep copy?
            }
        }
        this.semantics = new LinkedHashSet<>(fts.semanticTypeIdentifiers());
        this.online = fts.getOnlineResource();
        this.transformation = fts.getTransformation();
    }

    @Override
    public List<Rule> rules() {
        return rules;
    }

    @Override
    public Set<SemanticType> semanticTypeIdentifiers() {
        return semantics;
    }

    @Override
    public Set<Name> featureTypeNames() {
        return featureTypeNames;
    }

    @Override
    public Id getFeatureInstanceIDs() {
        return featureInstances;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object accept(StyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates a deep copy clone of the FeatureTypeStyle.
     *
     * @see org.geotools.styling.FeatureTypeStyle#clone()
     */
    @Override
    public Object clone() {
        FeatureTypeStyle clone;

        try {
            clone = (FeatureTypeStyle) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new AssertionError(e); // this should never happen.
        }

        final List<Rule> rulesCopy = new ArrayList<>();

        for (final Rule rl : rules) {
            rulesCopy.add((Rule) ((Cloneable) rl).clone());
        }

        clone.rules = new ArrayList<>();
        clone.featureTypeNames = new LinkedHashSet<>();
        clone.semantics = new LinkedHashSet<>();
        final List<Rule> cloneRules = clone.rules();
        cloneRules.addAll(rulesCopy);
        clone.featureTypeNames().addAll(featureTypeNames);
        clone.semanticTypeIdentifiers().addAll(semantics);

        return clone;
    }

    /**
     * Overrides hashCode.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (rules != null) {
            result = (PRIME * result) + rules.hashCode();
        }

        if (featureInstances != null) {
            result = (PRIME * result) + featureInstances.hashCode();
        }

        if (semantics != null) {
            result = (PRIME * result) + semantics.hashCode();
        }

        if (featureTypeNames != null) {
            result = (PRIME * result) + featureTypeNames.hashCode();
        }

        if (name != null) {
            result = (PRIME * result) + name.hashCode();
        }

        if (description != null) {
            result = (PRIME * result) + description.hashCode();
        }

        if (options != null) {
            result = PRIME * result + options.hashCode();
        }

        if (transformation != null) {
            result = PRIME * result + transformation.hashCode();
        }

        if (online != null) {
            result = PRIME * result + online.hashCode();
        }

        return result;
    }

    /**
     * Compares this FeatureTypeStyleImpl with another.
     *
     * <p>Two FeatureTypeStyles are equal if they contain equal properties and an equal list of
     * Rules.
     *
     * @param oth The other FeatureTypeStyleImpl to compare with.
     * @return True if this and oth are equal.
     */
    @Override
    public boolean equals(Object oth) {

        if (this == oth) {
            return true;
        }

        if (oth instanceof FeatureTypeStyle) {
            FeatureTypeStyle other = (FeatureTypeStyle) oth;

            return Utilities.equals(name, other.name)
                    && Utilities.equals(description, other.description)
                    && Utilities.equals(rules, other.rules)
                    && Utilities.equals(featureTypeNames, other.featureTypeNames)
                    && Utilities.equals(semantics, other.semantics)
                    && Utilities.equals(getOptions(), other.getOptions())
                    && Utilities.equals(getTransformation(), other.getTransformation())
                    && Utilities.equals(getOnlineResource(), other.getOnlineResource());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("FeatureTypeStyleImpl");
        buf.append("[");
        if (name != null) {
            buf.append(" name=");
            buf.append(name);
        } else {
            buf.append(" UNNAMED");
        }
        buf.append(", ");
        buf.append(featureTypeNames);
        buf.append(", rules=<");
        buf.append(rules.size());
        buf.append(">");
        if (!rules.isEmpty()) {
            buf.append("(");
            buf.append(rules.get(0));
            if (rules.size() > 1) {
                buf.append(",...");
            }
            buf.append(")");
        }
        if (options != null) {
            buf.append(", options=" + options);
        }
        buf.append("]");
        return buf.toString();
    }

    public void setOnlineResource(OnLineResource online) {
        this.online = online;
    }

    @Override
    public OnLineResource getOnlineResource() {
        return online;
    }

    static FeatureTypeStyle cast(org.geotools.api.style.FeatureTypeStyle featureTypeStyle) {
        if (featureTypeStyle == null) {
            return null;
        } else if (featureTypeStyle instanceof FeatureTypeStyle) {
            return (FeatureTypeStyle) featureTypeStyle;
        } else {
            FeatureTypeStyle copy = new FeatureTypeStyle();
            // the above is a deep copy - replace with cast if we can
            return copy;
        }
    }

    @Override
    public Expression getTransformation() {
        return transformation;
    }

    @Override
    public void setTransformation(Expression transformation) {
        this.transformation = transformation;
    }

    @Override
    public boolean hasOption(String key) {
        return options != null && options.containsKey(key);
    }

    @Override
    public Map<String, String> getOptions() {
        if (options == null) {
            options = new LinkedHashMap<>();
        }
        return options;
    }
}
