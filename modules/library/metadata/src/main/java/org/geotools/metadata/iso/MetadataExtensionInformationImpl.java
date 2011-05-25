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

import org.opengis.metadata.citation.OnLineResource;
import org.opengis.metadata.ExtendedElementInformation;
import org.opengis.metadata.MetadataExtensionInformation;


/**
 * Information describing metadata extensions.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class MetadataExtensionInformationImpl extends MetadataEntity
        implements MetadataExtensionInformation
{
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 573866936088674519L;

    /**
     * Information about on-line sources containing the community profile name and
     * the extended metadata elements. Information for all new metadata elements.
     */
    private OnLineResource extensionOnLineResource;

    /**
     * Provides information about a new metadata element, not found in ISO 19115, which is
     * required to describe geographic data.
     */
    private Collection<ExtendedElementInformation> extendedElementInformation;

    /**
     * Construct an initially empty metadata extension information.
     */
    public MetadataExtensionInformationImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public MetadataExtensionInformationImpl(final MetadataExtensionInformation source) {
        super(source);
    }

    /**
     * Information about on-line sources containing the community profile name and
     * the extended metadata elements. Information for all new metadata elements.
     */
    public OnLineResource getExtensionOnLineResource() {
        return extensionOnLineResource;
    }

   /**
     * Set information about on-line sources.
     */
    public synchronized void setExtensionOnLineResource(final OnLineResource newValue) {
        checkWritePermission();
        this.extensionOnLineResource = newValue;
    }

    /**
     * Provides information about a new metadata element, not found in ISO 19115, which is
     * required to describe geographic data.
     */
    public synchronized Collection<ExtendedElementInformation> getExtendedElementInformation() {
        return (extendedElementInformation = nonNullCollection(extendedElementInformation,
                ExtendedElementInformation.class));
    }

    /**
     * Set information about a new metadata element.
     */
    public synchronized void setExtendedElementInformation(
            final Collection<? extends ExtendedElementInformation> newValues) {
        extendedElementInformation = copyCollection(newValues, extendedElementInformation,
                                                    ExtendedElementInformation.class);
    }
}
