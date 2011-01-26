/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import java.util.List;
import java.util.Set;
import org.opengis.annotation.Extension;
import org.opengis.annotation.UML;
import org.opengis.annotation.XmlElement;

import org.opengis.feature.type.Name;
import org.opengis.filter.Id;
import org.opengis.metadata.citation.OnLineResource;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

/**
 * Represents a style that applies to features or coverage.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@XmlElement("FeatureTypeStyle")
@UML(identifier="PF_FeaturePortrayal", specification=ISO_19117)
public interface FeatureTypeStyle {

    /**
     * Returns a name for this style.
     * This can be any string that uniquely identifies this style within a given
     * canvas.  It is not meant to be human-friendly.  (The "title" property is
     * meant to be human friendly.)
     * @return a name for this style.
     */
    @XmlElement("Name")
    String getName();

    /**
     * Returns the description of this style.
     *
     * @return Description with usual informations used
     * for user interfaces.
     */
    @XmlElement("Description")
    @UML(identifier="description", obligation=OPTIONAL, specification=ISO_19117)
    Description getDescription();

    /**
     * Returns a collection of Object identifying features object.
     *
     * <p>
     * ISO 19117 extends FeatureTypeStyle be providing this method.
     * This method enable the possibility to use a feature type style
     * on a given list of features only, which is not possible in OGC SE.
     * </p>
     *
     * @return Collection<String>
     */
    @UML(identifier="definedForInst", obligation=OPTIONAL, specification=ISO_19117)
    Id getFeatureInstanceIDs();
    
    /**
     * <p>
     * Returns the names of the feature type that this style is meant to act
     * upon.
     * </p>
     * <p>
     * In OGC Symbology Encoding define this method to return a single
     * String, and ISO 19117 use a Collection of String. We've choosen
     * ISO because it is more logic that a featureTypeStyle can be applied
     * to multiple featuretypes and not limited to a single one.
     * </p>
     *
     * @return the name of the feature type that this style is meant
     * to act upon.
     */
    @XmlElement("FeatureTypeName")
    @UML(identifier="definedFor", obligation=OPTIONAL, specification=ISO_19117)
    Set<Name> featureTypeNames();

    /**
     * Returns a collection that identifies the more general "type" of geometry
     * that this style is meant to act upon.
     * In the current OGC SE specifications, this is an experimental element and
     * can take only one of the following values:
     * <p>
     * <ul>
     *   <li>{@code generic:point}</li>
     *   <li>{@code generic:line}</li>
     *   <li>{@code generic:polygon}</li>
     *   <li>{@code generic:text}</li>
     *   <li>{@code generic:raster}</li>
     *   <li>{@code generic:any}</li>
     * </ul>
     * <p>
     *
     */
    @XmlElement("SemanticTypeIdentifier")
    Set<SemanticType> semanticTypeIdentifiers();

    /**
     * Returns the list of rules contained by this style.
     *
     * @return the list of rules. can not be null but can be empty.
     */
    @XmlElement("Rule")
    @UML(identifier="portrayalRule", obligation=MANDATORY, specification=ISO_19117)
    List<? extends Rule> rules();

    /**
     * It is common to have a style coming from a external xml file, this method
     * provide a way to get the original source if there is one.
     * OGC SLD specification can use this method to know if a style must be
     * written completely or if writing the online resource path is enough.
     * 
     * @return OnlineResource or null
     */
    OnLineResource getOnlineResource();
    
    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);

}
