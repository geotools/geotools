/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso;

import java.util.Collection;

import org.opengis.metadata.Datatype;
import org.opengis.metadata.Obligation;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.ExtendedElementInformation;
import org.opengis.util.InternationalString;


/**
 * New metadata element, not found in ISO 19115, which is required to describe geographic data.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ExtendedElementInformationImpl extends MetadataEntity
        implements ExtendedElementInformation
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -935396252908733907L;

    /**
     * Name of the extended metadata element.
     */
    private String name;

    /**
     * Short form suitable for use in an implementation method such as XML or SGML.
     */
    private String shortName;

    /**
     * Three digit code assigned to the extended element.
     * Non-null only if the {@linkplain #getDataType data type}
     * is {@linkplain Datatype#CODE_LIST_ELEMENT code list element}.
     */
    private Integer domainCode;

    /**
     * Definition of the extended element.
     */
    private InternationalString definition;

    /**
     * Obligation of the extended element.
     */
    private Obligation obligation;

    /**
     * Condition under which the extended element is mandatory.
     * Non-null value only if the {@linkplain #getObligation obligation}
     * is {@linkplain Obligation#CONDITIONAL conditional}.
     */
    private InternationalString condition;

    /**
     * Code which identifies the kind of value provided in the extended element.
     */
    private Datatype dataType;

    /**
     * Maximum occurrence of the extended element.
     * Returns {@code null} if it doesn't apply, for example if the
     * {@linkplain #getDataType data type} is {@linkplain Datatype#ENUMERATION enumeration},
     * {@linkplain Datatype#CODE_LIST code list} or {@linkplain Datatype#CODE_LIST_ELEMENT
     * code list element}.
     */
    private Integer maximumOccurrence;

    /**
     * Valid values that can be assigned to the extended element.
     * Returns {@code null} if it doesn't apply, for example if the
     * {@linkplain #getDataType data type} is {@linkplain Datatype#ENUMERATION enumeration},
     * {@linkplain Datatype#CODE_LIST code list} or {@linkplain Datatype#CODE_LIST_ELEMENT
     * code list element}.
     */
    private InternationalString domainValue;

    /**
     * Name of the metadata entity(s) under which this extended metadata element may appear.
     * The name(s) may be standard metadata element(s) or other extended metadata element(s).
     */
    private Collection<String> parentEntity;

    /**
     * Specifies how the extended element relates to other existing elements and entities.
     */
    private InternationalString rule;

    /**
     * Reason for creating the extended element.
     */
    private Collection<InternationalString> rationales;

    /**
     * Name of the person or organization creating the extended element.
     */
    private Collection<ResponsibleParty> sources;

    /**
     * Construct an initially empty extended element information.
     */
    public ExtendedElementInformationImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ExtendedElementInformationImpl(final ExtendedElementInformation source) {
        super(source);
    }

    /**
     * Create an extended element information initialized to the given values.
     */
    public ExtendedElementInformationImpl(final String              name,
                                          final InternationalString definition,
                                          final InternationalString condition,
                                          final Datatype            dataType,
                                          final Collection<String>  parentEntity,
                                          final InternationalString rule,
                                          final Collection<? extends ResponsibleParty> sources)
    {
        setName        (name);
        setDefinition  (definition);
        setCondition   (condition);
        setDataType    (dataType);
        setParentEntity(parentEntity);
        setRule        (rule);
        setSources     (sources);
    }

    /**
     * Name of the extended metadata element.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the extended metadata element.
     */
    public synchronized void setName(final String newValue) {
        checkWritePermission();
        name = newValue;
    }

    /**
     * Short form suitable for use in an implementation method such as XML or SGML.
     * NOTE: other methods may be used.
     * Returns {@code null} if the {@linkplain #getDataType data type}
     * is {@linkplain Datatype#CODE_LIST_ELEMENT code list element}.
     */
    public String getShortName()  {
        return shortName;
    }

    /**
     * Set a short form suitable for use in an implementation method such as XML or SGML.
     */
    public synchronized void setShortName(final String newValue)  {
        checkWritePermission();
        shortName = newValue;
    }

    /**
     * Three digit code assigned to the extended element.
     * Returns a non-null value only if the {@linkplain #getDataType data type}
     * is {@linkplain Datatype#CODE_LIST_ELEMENT code list element}.
     */
    public Integer getDomainCode() {
        return domainCode;
    }

    /**
     * Set a three digit code assigned to the extended element.
     */
    public synchronized void setDomainCode(final Integer newValue) {
        checkWritePermission();
        domainCode = newValue;
    }

    /**
     * Definition of the extended element.
     */
    public InternationalString getDefinition()  {
        return definition;
    }

    /**
     * Set the definition of the extended element.
     */
    public synchronized void setDefinition(final InternationalString newValue)  {
        checkWritePermission();
        definition = newValue;
    }

    /**
     * Obligation of the extended element.
     */
    public Obligation getObligation()  {
        return obligation;
    }

    /**
     * Set the obligation of the extended element.
     */
    public synchronized void setObligation(final Obligation newValue)  {
        checkWritePermission();
        obligation = newValue;
    }

    /**
     * Condition under which the extended element is mandatory.
     * Returns a non-null value only if the {@linkplain #getObligation obligation}
     * is {@linkplain Obligation#CONDITIONAL conditional}.
     */
    public InternationalString getCondition() {
        return condition;
    }

    /**
     * Set the condition under which the extended element is mandatory.
     */
    public synchronized void setCondition(final InternationalString newValue) {
        checkWritePermission();
        condition = newValue;
    }

    /**
     * Code which identifies the kind of value provided in the extended element.
     */
    public Datatype getDataType() {
        return dataType;
    }

    /**
     * Set the code which identifies the kind of value provided in the extended element.
     */
    public synchronized void setDataType(final Datatype newValue) {
        checkWritePermission();
        dataType = newValue;
    }

    /**
     * Maximum occurrence of the extended element.
     * Returns {@code null} if it doesn't apply, for example if the
     * {@linkplain #getDataType data type} is {@linkplain Datatype#ENUMERATION enumeration},
     * {@linkplain Datatype#CODE_LIST code list} or {@linkplain Datatype#CODE_LIST_ELEMENT
     * code list element}.
     */
    public Integer getMaximumOccurrence() {
        return maximumOccurrence;
    }

    /**
     * Set the maximum occurrence of the extended element.
     */
    public synchronized void setMaximumOccurrence(final Integer newValue) {
        checkWritePermission();
        maximumOccurrence = newValue;
    }

    /**
     * Valid values that can be assigned to the extended element.
     * Returns {@code null} if it doesn't apply, for example if the
     * {@linkplain #getDataType data type} is {@linkplain Datatype#ENUMERATION enumeration},
     * {@linkplain Datatype#CODE_LIST code list} or {@linkplain Datatype#CODE_LIST_ELEMENT
     * code list element}.
     */
    public InternationalString getDomainValue() {
        return domainValue;
    }

    /**
     * Set the valid values that can be assigned to the extended element.
     */
    public synchronized void setDomainValue(final InternationalString newValue) {
        checkWritePermission();
        domainValue = newValue;
    }

    /**
     * Name of the metadata entity(s) under which this extended metadata element may appear.
     * The name(s) may be standard metadata element(s) or other extended metadata element(s).
     */
    public synchronized Collection<String> getParentEntity() {
        return parentEntity = nonNullCollection(parentEntity, String.class);
    }

    /**
     * Set the name of the metadata entity(s) under which this extended metadata element may appear.
     */
    public synchronized void setParentEntity(
            final Collection<? extends String> newValues)
    {
        parentEntity = copyCollection(newValues, parentEntity, String.class);
    }

    /**
     * Specifies how the extended element relates to other existing elements and entities.
     */
    public InternationalString getRule() {
        return rule;
    }

    /**
     * Set how the extended element relates to other existing elements and entities.
     */
    public synchronized void setRule(final InternationalString newValue) {
        checkWritePermission();
        rule = newValue;
    }

    /**
     * Reason for creating the extended element.
     */
    public synchronized Collection<InternationalString> getRationales() {
        return (rationales = nonNullCollection(rationales, InternationalString.class));
    }

    /**
     * Set the reason for creating the extended element.
     */
    public synchronized void setRationales(
            final Collection<? extends InternationalString> newValues)
    {
        rationales = copyCollection(newValues, rationales, InternationalString.class);
    }

    /**
     * Name of the person or organization creating the extended element.
     */
    public synchronized Collection<ResponsibleParty> getSources() {
        return sources = nonNullCollection(sources, ResponsibleParty.class);
    }

    /**
     * Set the name of the person or organization creating the extended element.
     */
    public synchronized void setSources(
            final Collection<? extends ResponsibleParty> newValues)
    {
        sources = copyCollection(newValues, sources, ResponsibleParty.class);
    }

    /**
     * Sets the {@code xmlMarshalling} flag to {@code true}, since the marshalling
     * process is going to be done.
     * This method is automatically called by JAXB, when the marshalling begins.
     *
     * @param marshaller Not used in this implementation.
     */
///    private void beforeMarshal(Marshaller marshaller) {
///        xmlMarshalling(true);
///    }

    /**
     * Sets the {@code xmlMarshalling} flag to {@code false}, since the marshalling
     * process is finished.
     * This method is automatically called by JAXB, when the marshalling ends.
     *
     * @param marshaller Not used in this implementation
     */
///    private void afterMarshal(Marshaller marshaller) {
///        xmlMarshalling(false);
///    }
}
