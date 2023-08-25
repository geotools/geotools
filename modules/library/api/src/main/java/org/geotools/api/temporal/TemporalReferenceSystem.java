/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.ReferenceSystem;

/**
 * Provides information about a temporal reference system.
 *
 * <p>This interface is similar to ReferenceSytem and may be deprecated in the future. It is made
 * available currently in order to *exactly* match this ISO 19108 specification with a "domain of
 * validity" association to an Extent.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
public interface TemporalReferenceSystem extends ReferenceSystem {
    /**
     * Provides a name that uniquely identifies the temporal referece system.
     *
     * <p>Currently returns MD_Identifier, which is defined in ISO 19115, while ISO 19108 requires
     * that RS_Identifier (defined in ISO 19111 and http://www.opengis.org/docs/03-073r1.zip) is
     * returned. From the looks of it, org.geotools.api.referencing.ReferenceSystem could also fit
     * the bill.
     *
     * @return {@link ReferenceIdentifier} for the temporal reference system
     */
    @Override
    ReferenceIdentifier getName();

    /**
     * Identifies the space and time within which the reference system is applicable. The return
     * type allows for describing both spatial and temporal extent. This attribute shall be used
     * whenever an application schema includes {@link TemporalPosition}s referenced to a reference
     * system which has a valid extent that is less than the extent of a data set containing such
     * values.
     *
     * <p>Please note this is very similar to ReferenceSystem.getValidArea() from ISO 19115.
     */
    @Override
    Extent getDomainOfValidity();
}
