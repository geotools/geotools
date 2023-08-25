/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.citation;

import java.util.Collection;
import java.util.Date;
import org.geotools.api.annotation.Obligation;
import org.geotools.api.annotation.Specification;
import org.geotools.api.annotation.UML;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.util.InternationalString;

/**
 * Standardized resource reference.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 1.0
 */
public interface Citation {
    /**
     * Name by which the cited resource is known.
     *
     * @return The cited resource name.
     */
    InternationalString getTitle();

    /**
     * Short name or other language name by which the cited information is known. Example: "DCW" as
     * an alternative title for "Digital Chart of the World".
     *
     * @return Other names for the resource, or an empty collection if none.
     */
    @UML(
            identifier = "alternateTitle",
            obligation = Obligation.OPTIONAL,
            specification = Specification.ISO_19115)
    Collection<? extends InternationalString> getAlternateTitles();

    /**
     * Version of the cited resource.
     *
     * @return The version, or {@code null} if none.
     */
    InternationalString getEdition();

    /**
     * Date of the edition, or {@code null} if none.
     *
     * @return The edition date, or {@code null} if none.
     */
    Date getEditionDate();

    /**
     * Unique identifier for the resource. Example: Universal Product Code (UPC), National Stock
     * Number (NSN).
     *
     * @return The identifiers, or an empty collection if none.
     */
    Collection<? extends Identifier> getIdentifiers();

    /**
     * Name and position information for an individual or organization that is responsible for the
     * resource. Returns an empty string if there is none.
     *
     * @return The individual or organization that is responsible, or an empty collection if none.
     */
    Collection<? extends ResponsibleParty> getCitedResponsibleParties();

    /**
     * Mode in which the resource is represented, or an empty string if none.
     *
     * @return The presentation mode, or an empty collection if none.
     */
    Collection<PresentationForm> getPresentationForm();

    /**
     * Other information required to complete the citation that is not recorded elsewhere. Returns
     * {@code null} if none.
     *
     * @return Other details, or {@code null} if none.
     */
    InternationalString getOtherCitationDetails();

    /**
     * Common title with holdings note. Note: title identifies elements of a series collectively,
     * combined with information about what volumes are available at the source cited. Returns
     * {@code null} if there is no title.
     *
     * @return The common title, or {@code null} if none.
     */
    InternationalString getCollectiveTitle();

    /**
     * International Standard Book Number, or {@code null} if none.
     *
     * @return The ISBN, or {@code null} if none.
     */
    String getISBN();

    /**
     * International Standard Serial Number, or {@code null} if none.
     *
     * @return The ISSN, or {@code null} if none.
     */
    String getISSN();
}
