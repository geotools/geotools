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

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19117;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.api.annotation.UML;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.metadata.citation.OnLineResource;

/**
 * Represents a style that applies to features or coverage.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@UML(identifier = "PF_FeaturePortrayal", specification = ISO_19117)
public interface FeatureTypeStyle {

    /**
     * Returns a name for this style. This can be any string that uniquely identifies this style
     * within a given canvas. It is not meant to be human-friendly. (The "title" property is meant
     * to be human friendly.)
     *
     * @return a name for this style.
     */
    String getName();

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
     * <p>ISO 19117 extends FeatureTypeStyle be providing this method. This method enable the
     * possibility to use a feature type style on a given list of features only, which is not
     * possible in OGC SE.
     *
     * @return Collection<String>
     */
    @UML(identifier = "definedForInst", obligation = OPTIONAL, specification = ISO_19117)
    Id getFeatureInstanceIDs();

    /**
     * Returns the names of the feature type that this style is meant to act upon.
     *
     * <p>In OGC Symbology Encoding define this method to return a single String, and ISO 19117 use
     * a Collection of String. We've choosen ISO because it is more logic that a featureTypeStyle
     * can be applied to multiple featuretypes and not limited to a single one.
     *
     * @return the name of the feature type that this style is meant to act upon.
     */
    @UML(identifier = "definedFor", obligation = OPTIONAL, specification = ISO_19117)
    Set<Name> featureTypeNames();

    /**
     * Returns a collection that identifies the more general "type" of geometry that this style is
     * meant to act upon. In the current OGC SE specifications, this is an experimental element and
     * can take only one of the following values:
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
     * Returns the list of rules contained by this style.
     *
     * @return the list of rules. can not be null but can be empty.
     */
    @UML(identifier = "portrayalRule", obligation = MANDATORY, specification = ISO_19117)
    List<Rule> rules();

    /**
     * It is common to have a style coming from a external xml file, this method provide a way to
     * get the original source if there is one. OGC SLD specification can use this method to know if
     * a style must be written completely or if writing the online resource path is enough.
     *
     * @return OnlineResource or null
     */
    OnLineResource getOnlineResource();

    void accept(StyleVisitor visitor);

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

    enum RenderingSelectionOptions {
        NORMAL("normal"),
        LEGENDONLY("legendOnly"),
        MAPONLY("mapOnly");

        private String option;

        RenderingSelectionOptions(String option) {
            this.option = option;
        }

        public String getOption() {
            return option;
        }
    }
}
