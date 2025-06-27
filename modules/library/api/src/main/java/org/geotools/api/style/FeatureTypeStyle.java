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
import java.util.Set;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.metadata.citation.OnLineResource;

/**
 * Represents a style that applies to features or coverage.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification
 *     1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface FeatureTypeStyle {

    /** This option influences how multiple rules matching the same feature are evaluated */
    String KEY_EVALUATION_MODE = "ruleEvaluation";
    /** The standard behavior, all the matching rules are executed */
    String VALUE_EVALUATION_MODE_ALL = "all";
    /** Only the first matching rule gets executed, all the others are skipped */
    String VALUE_EVALUATION_MODE_FIRST = "first";
    /**
     * Applies a color composition/blending operation at the feature type style level (that is, blending the current FTS
     * level against the map below it).
     *
     * <p>The syntax for this key is {code}<VendorOption name="composite">name[,opacity]</VendorOption>{code} where:
     *
     * <ul>
     *   <li>{code}name is one of the <a href="http://www.w3.org/TR/compositing-1/">SVG composition operations</a>, in
     *       particular, copy, destination, source-over, destination-over, source-in, destination-in, source-out,
     *       destination-out, source-atop, destination-atop, xor, multiply, screen, overlay, darken, lighten,
     *       color-dodge, color-burn, hard-light, soft-light, difference, exclusion
     *   <li>{opacity} indicates the opacity level to be used during the operation, defauls to 1
     * </ul>
     *
     * For example:
     *
     * <ul>
     *   <li>{code}<VendorOption name="composite">source-atop, 0.5</VendorOption>{code} composes the current FTS
     *       exclusively where the previous map has already been drawn, using a 0.5 opacity level
     *   <li>{code}<VendorOption name="composite">multiply</VendorOption>{code} blends the current FTS with the
     *       underlying map using color multiplication
     * </ul>
     *
     * <p>The same vendor option can also be applied at the symbolizer level to achieve different effects (feature by
     * feature composition as oppose to layer by layer one).
     *
     * <p>Important note: for most compositing operation to work properly, the graphics used for the rendering should be
     * derived from an image that has an alpha channel and transparent background (as most of the operations consider
     * the transparency of the target surface in their math)
     */
    String COMPOSITE = "composite";
    /**
     * Boolean value, if true the current feature type style will be treated as a base for the subsequent feature type
     * styles in the rendering stack (including other layer ones) as opposed to use the merged backdrop rendered so far.
     * When the top of the stack is reached, or another base is found, this FTS will be merged into the backdrop,
     * eventually using the indicated composite operator
     */
    String COMPOSITE_BASE = "composite-base";
    /**
     * String value controlling the order in which the features are loaded from the data source, and thus painted, in
     * this feature type style.
     *
     * <p>The syntax is <code>Attribute1 {A|D},Attribute2 {A|D}...</code>, <code>A</code> is ascending, <code>D</code>
     * is descending. The sorting direction is optional and defaults to ascending if not specified.
     *
     * <p>E.g., <code>cat D,name</code> sorts data by <code>cat</code> in descending order, and then by ascending <code>
     * name</code> within all features having the same <code>cat</code> value.
     */
    String SORT_BY = "sortBy";
    /**
     * String value controlling cross layer z-ordering. Several feature type styles in the same sortByGroup will have
     * their features globally ordered before painting, for example, in order to respect their real world relationships.
     * FeatureTypeStyle are grouped only if they are adjacent in the overall MapContent (even across layers). In case
     * compositing is used in the same FeatureTypeStyle, the first value in group will be used for the entire group.
     */
    String SORT_BY_GROUP = "sortByGroup";
    /**
     * String value allowing to control whether an SLD element should be included when applying the style to render maps
     * or a legends. The option can be used also on Rule and all the Symbolizers. Possible values are normal,
     * legendOnly, mapOnly.
     */
    String VENDOR_OPTION_INCLUSION = "inclusion";
    /**
     * Set of attributes to be included in vector tiles. Used by the GeoServer vector tiles module, declared here so
     * that it can be referred to from style languages.
     */
    String VT_ATTRIBUTES = "vt-attributes";
    /**
     * Wheter or not a dedicated label layer should be generated for vector tiles. Useful for polygon layers, but also
     * for point and line layers, as it will include only features whose label attributes are not null. Used by the
     * GeoServer vector tiles module, declared here so that it can be referred to from style languages
     */
    String VT_LABELS = "vt-labels";
    /**
     * Set of attributes to be included in the vector tiles label layer. Used by the GeoServer vector tiles module,
     * declared here so that it can be referred to from style languages
     */
    String VT_LABEL_ATTRIBUTES = "vt-label-attributes";

    /** Coalesce the geometries of features sharing the same attribute values */
    String VT_COALESCE = "vt-coalesce";

    /** A boolean vendor option, if true then the raster transformations will receive oversampled data */
    String RT_OVERASAMPLE = "rt-oversample";

    /**
     * Returns a name for this style. This can be any string that uniquely identifies this style within a given canvas.
     * It is not meant to be human-friendly. (The "title" property is meant to be human friendly.)
     *
     * @return a name for this style.
     */
    String getName();

    void setName(String name);

    /**
     * Description for this style.
     *
     * @return Human readable description for use in user interfaces
     * @since 2.5.x
     */
    Description getDescription();

    /**
     * Returns a collection of Object identifying features object.
     *
     * <p>ISO 19117 extends FeatureTypeStyle be providing this method. This method enable the possibility to use a
     * feature type style on a given list of features only, which is not possible in OGC SE.
     *
     * @return Collection<String>
     */
    Id getFeatureInstanceIDs();

    /**
     * Returns the names of the feature type that this style is meant to act upon.
     *
     * <p>In OGC Symbology Encoding define this method to return a single String, and ISO 19117 use a Collection of
     * String. We've choosen ISO because it is more logic that a featureTypeStyle can be applied to multiple
     * featuretypes and not limited to a single one.
     *
     * @return the name of the feature type that this style is meant to act upon.
     */
    Set<Name> featureTypeNames();

    /**
     * Returns a collection that identifies the more general "type" of geometry that this style is meant to act upon. In
     * the current OGC SE specifications, this is an experimental element and can take only one of the following values:
     *
     * <p>
     *
     * <ul>
     *   <li>{@code generic:point}
     *   <li>{@code generic:line}
     *   <li>{@code generic:polygon}
     *   <li>{@code generic:text}
     *   <li>{@code generic:raster}
     *   <li>{@code generic:any}
     * </ul>
     *
     * <p>
     */
    Set<SemanticType> semanticTypeIdentifiers();

    /**
     * Rules govern the appearance of any given feature to be styled by this styler.
     *
     * <p>This is *the* list being used to manage the rules!
     *
     * @since GeoTools 2.2.M3, GeoAPI 2.0
     */
    List<Rule> rules();

    /**
     * It is common to have a style coming from a external xml file, this method provide a way to get the original
     * source if there is one. OGC SLD specification can use this method to know if a style must be written completely
     * or if writing the online resource path is enough.
     *
     * @return OnlineResource or null
     */
    OnLineResource getOnlineResource();

    /**
     * It is common to have a style coming from a external xml file, this method provide a way to get the original
     * source if there is one.
     *
     * @param online location external file defining this style, or null if not available
     */
    void setOnlineResource(OnLineResource online);

    void accept(StyleVisitor visitor);

    /**
     * The eventual transformation to be applied before rendering the data (should be an expression taking a feature
     * collection or a grid coverage as the evaluation context and returns a feature collection or a grid coverage as an
     * output)
     */
    Expression getTransformation();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /**
     * Sets the eventual transformation to be applied before rendering the data (should be an expression taking a
     * feature collection or a grid coverage as an input and returns a feature collection or a grid coverage as an
     * output)
     */
    void setTransformation(Expression transformation);

    /** Determines if a vendor option with the specific key has been set on this symbolizer. */
    boolean hasOption(String key);

    /**
     * Map of vendor options for the symbolizer.
     *
     * <p>Client code looking for the existence of a single option should use {@link #hasOption(String)}
     */
    Map<String, String> getOptions();

    enum RenderingSelectionOptions {
        NORMAL("normal"),
        LEGENDONLY("legendOnly"),
        MAPONLY("mapOnly");

        private final String option;

        RenderingSelectionOptions(String option) {
            this.option = option;
        }

        public String getOption() {
            return option;
        }
    }
}
