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
import java.util.Set;
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
     * Returns the description of this style.
     *
     * @return Description with usual informations used for user interfaces.
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
    List<? extends Rule> rules();

    /**
     * It is common to have a style coming from a external xml file, this method provide a way to
     * get the original source if there is one. OGC SLD specification can use this method to know if
     * a style must be written completely or if writing the online resource path is enough.
     *
     * @return OnlineResource or null
     */
    OnLineResource getOnlineResource();

    /**
     * gets the transformation as expression
     *
     * @return Transformation or null
     */
    Expression getTransformation();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(StyleVisitor visitor, Object extraData);
}
