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
package org.geotools.metadata.iso.identification;

import java.util.Collection;
import java.util.Date;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.identification.Usage;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Brief description of ways in which the resource(s) is/are currently used.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class UsageImpl extends MetadataEntity implements Usage {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 4059324536168287490L;

    /**
     * Brief description of the resource and/or resource series usage.
     */
    private InternationalString specificUsage;

    /**
     * Date and time of the first use or range of uses of the resource and/or resource series.
     * Values are milliseconds ellapsed since January 1st, 1970,
     * or {@link Long#MIN_VALUE} if this value is not set.
     */
    private long usageDate = Long.MIN_VALUE;

    /**
     * Applications, determined by the user for which the resource and/or resource series
     * is not suitable.
     */
    private InternationalString userDeterminedLimitations;

    /**
     * Identification of and means of communicating with person(s) and organization(s)
     * using the resource(s).
     */
    private Collection<ResponsibleParty> userContactInfo;

    /**
     * Constructs an initially empty usage.
     */
    public UsageImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public UsageImpl(final Usage source) {
        super(source);
    }

    /**
     * Creates an usage initialized to the specified values.
     */
    public UsageImpl(final InternationalString specificUsage,
                     final Collection<ResponsibleParty> userContactInfo)
    {
        setUserContactInfo(userContactInfo);
        setSpecificUsage  (specificUsage  );
    }

    /**
     * Brief description of the resource and/or resource series usage.
     */
    public InternationalString getSpecificUsage() {
        return specificUsage;
    }

   /**
    * Set a brief description of the resource and/or resource series usage.
    */
    public synchronized void setSpecificUsage(final InternationalString newValue) {
        checkWritePermission();
        specificUsage = newValue;
    }

    /**
     * Date and time of the first use or range of uses of the resource and/or resource series.
     */
    public synchronized Date getUsageDate() {
        return (usageDate!=Long.MIN_VALUE) ? new Date(usageDate) : null;
    }

    /**
     * Set the date and time of the first use.
     */
    public synchronized void setUsageDate(final Date newValue)  {
        checkWritePermission();
        usageDate = (newValue!=null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Applications, determined by the user for which the resource and/or resource series
     * is not suitable.
     */
    public InternationalString getUserDeterminedLimitations() {
        return userDeterminedLimitations;
    }

    /**
     * Set applications, determined by the user for which the resource and/or resource series
     * is not suitable.
     */
    public synchronized void setUserDeterminedLimitations(final InternationalString newValue) {
        checkWritePermission();
        this.userDeterminedLimitations = newValue;
    }

    /**
     * Identification of and means of communicating with person(s) and organization(s)
     * using the resource(s).
     */
    public synchronized Collection<ResponsibleParty> getUserContactInfo() {
        return userContactInfo = nonNullCollection(userContactInfo, ResponsibleParty.class);
    }

    /**
     * Set identification of and means of communicating with person(s) and organization(s)
     * using the resource(s).
     */
    public synchronized void setUserContactInfo(
            final Collection<? extends ResponsibleParty> newValues)
    {
        userContactInfo = copyCollection(newValues, userContactInfo, ResponsibleParty.class);
    }
}
