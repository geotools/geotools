/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.data;

import java.io.IOException;

/**
 * A data store that can version enable feature types, and then keep version
 * history for those.
 * TODO: add better docs on how to leverage featureVersion in standard queries,
 * as well as 
 * 
 * @author Andrea Aime, TOPP
 *
 * @source $URL$
 */
public interface VersioningDataStore extends DataStore {
    
    /**
     * Key used in transaction properties to hold the commit author
     */
    public static final String AUTHOR = "VersioningCommitAuthor";
    /**
     * Key used in transaction properties to hold the commit message
     */
    public static final String MESSAGE = "VersioningCommitMessage";

    /**
     * Returns true if the specified feature type is versioned, false otherwise
     * 
     * @param typeName
     * @return
     */
    public boolean isVersioned(String typeName) throws IOException;

    /**
     * Alters the versioned state of a feature type
     * 
     * @param typeName
     *            the type name that must be changed
     * @param versioned
     *            if true, the type gets version enabled, if false versioning is
     *            disabled
     * @param t
     *            the transaction used to performe version enabling. It shall
     *            contain user and commit message as properties.
     * @throws IOException
     */
    public void setVersioned(String typeName, boolean versioned, String author,
            String message) throws IOException;
}
