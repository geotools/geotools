/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata;

import java.util.Collection;
import org.geotools.api.metadata.citation.ResponsibleParty;
import org.geotools.api.util.InternationalString;

/**
 * New metadata element, not found in ISO 19115, which is required to describe geographic data.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface ExtendedElementInformation {
    /**
     * Name of the extended metadata element.
     *
     * @return Name of the extended metadata element.
     */
    String getName();

    /**
     * Short form suitable for use in an implementation method such as XML or SGML. Returns {@code
     * null} if the {@linkplain #getDataType data type} is {@linkplain Datatype#CODE_LIST_ELEMENT
     * code list element}, in which case {@link #getDomainCode} may be used instead.
     *
     * @return Short form suitable for use in an implementation method such as XML or SGML, or
     *     {@code null}.
     */
    String getShortName();

    /**
     * Three digit code assigned to the extended element. Returns a non-null value only if the
     * {@linkplain #getDataType data type} is {@linkplain Datatype#CODE_LIST_ELEMENT code list
     * element}, in which case {@link #getShortName} may be used instead.
     *
     * @return Three digit code assigned to the extended element, or {@code null}.
     */
    Integer getDomainCode();

    /**
     * Definition of the extended element.
     *
     * @return Definition of the extended element.
     */
    InternationalString getDefinition();

    /**
     * Obligation of the extended element.
     *
     * @return Obligation of the extended element, or {@code null}.
     */
    Obligation getObligation();

    /**
     * Condition under which the extended element is mandatory. Returns a non-null value only if the
     * {@linkplain #getObligation obligation} is {@linkplain Obligation#CONDITIONAL conditional}.
     *
     * @return The condition under which the extended element is mandatory, or {@code null}.
     */
    InternationalString getCondition();

    /**
     * Code which identifies the kind of value provided in the extended element.
     *
     * @return The kind of value provided in the extended element.
     */
    Datatype getDataType();

    /**
     * Maximum occurrence of the extended element. Returns {@code null} if it doesn't apply, for
     * example if the {@linkplain #getDataType data type} is {@linkplain Datatype#ENUMERATION
     * enumeration}, {@linkplain Datatype#CODE_LIST code list} or {@linkplain
     * Datatype#CODE_LIST_ELEMENT code list element}.
     *
     * @return Maximum occurrence of the extended element, or {@code null}.
     */
    Integer getMaximumOccurrence();

    /**
     * Valid values that can be assigned to the extended element. Returns {@code null} if it doesn't
     * apply, for example if the {@linkplain #getDataType data type} is {@linkplain
     * Datatype#ENUMERATION enumeration}, {@linkplain Datatype#CODE_LIST code list} or {@linkplain
     * Datatype#CODE_LIST_ELEMENT code list element}.
     *
     * @return Valid values that can be assigned to the extended element, or {@code null}.
     */
    InternationalString getDomainValue();

    /**
     * Name of the metadata entity(s) under which this extended metadata element may appear. The
     * name(s) may be standard metadata element(s) or other extended metadata element(s).
     *
     * @return Name of the metadata entity(s) under which this extended metadata element may appear.
     */
    Collection<String> getParentEntity();

    /**
     * Specifies how the extended element relates to other existing elements and entities.
     *
     * @return How the extended element relates to other existing elements and entities.
     */
    InternationalString getRule();

    /**
     * Reason for creating the extended element.
     *
     * @return Reason for creating the extended element.
     */
    Collection<? extends InternationalString> getRationales();

    /**
     * Name of the person or organization creating the extended element.
     *
     * @return Name of the person or organization creating the extended element.
     */
    Collection<? extends ResponsibleParty> getSources();
}
