/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;

/**
 * Default implementation of {@link FileResourceInfo}. It simply returns a {@link
 * DefaultCloseableIterator} built on top of the underlying file list.
 */
public class DefaultFileResourceInfo extends DefaultResourceInfo implements FileResourceInfo {

    protected List<FileGroup> files;

    public DefaultFileResourceInfo(List<FileGroup> files) {
        this.files = files;
    }

    /**
     * Default implementation will ignore the specified query.
     *
     * <p>Subclasses should override this method to implement proper query parsing
     */
    @Override
    public CloseableIterator<FileGroup> getFiles(Query query) {
        return new DefaultCloseableIterator<FileGroup>(files.iterator());
    }
}
