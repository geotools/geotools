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

import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.PortrayalCatalogueReference;


/**
 * Information identifying the portrayal catalogue used.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class PortrayalCatalogueReferenceImpl extends MetadataEntity
        implements PortrayalCatalogueReference
{
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -3095277682987563157L;

    /**
     * Bibliographic reference to the portrayal catalogue cited.
     */
    private Collection<Citation> portrayalCatalogueCitations;

    /**
     * Construct an initially empty portrayal catalogue reference.
     */
    public PortrayalCatalogueReferenceImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public PortrayalCatalogueReferenceImpl(final PortrayalCatalogueReference source) {
        super(source);
    }

    /**
     * Creates a portrayal catalogue reference initialized to the given values.
     */
    public PortrayalCatalogueReferenceImpl(final Collection<Citation> portrayalCatalogueCitations) {
        setPortrayalCatalogueCitations(portrayalCatalogueCitations);
    }

    /**
     * Bibliographic reference to the portrayal catalogue cited.
     */
    public synchronized Collection<Citation> getPortrayalCatalogueCitations() {
        return portrayalCatalogueCitations = nonNullCollection(portrayalCatalogueCitations, Citation.class);
    }

    /**
     * Set bibliographic reference to the portrayal catalogue cited.
     */
    public synchronized void setPortrayalCatalogueCitations(
            Collection<? extends Citation> newValues)
    {
        portrayalCatalogueCitations = copyCollection(newValues, portrayalCatalogueCitations, Citation.class);
    }
}
