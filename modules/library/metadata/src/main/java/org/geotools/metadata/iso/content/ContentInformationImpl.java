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
package org.geotools.metadata.iso.content;

import org.opengis.metadata.content.ContentInformation;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Description of the content of a dataset.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ContentInformationImpl extends MetadataEntity implements ContentInformation {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -1609535650982322560L;

    /**
     * Constructs an initially empty content information.
     */
    public ContentInformationImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the value from the specified metadata.
     *
     * @since 2.4
     */
    public ContentInformationImpl(final ContentInformation source) {
        super(source);
    }
}
