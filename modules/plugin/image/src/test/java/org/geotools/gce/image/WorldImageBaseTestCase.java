/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.gce.image;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class WorldImageBaseTestCase {

    /**
     * Helper class used to filter files basing the decision on the file extensions. It is not so smart I know.... :-(
     *
     * @author giannecchini
     */
    static class MyFileFilter implements FilenameFilter {
        private Set<String> extensions = new HashSet<>();

        public MyFileFilter() {
            String[] extensions = {".gif", ".jpg", ".jpeg", ".tif", ".tiff", ".png", ".bmp"};
            this.setExtensions(extensions);
        }

        public MyFileFilter(String[] extensions) {
            this.setExtensions(extensions);
        }

        /** Sets the extensions that are allowed */
        private void setExtensions(String[] extensions) {
            if (extensions != null) {
                this.extensions.clear();

                for (String extension : extensions) this.extensions.add(extension);
            }
        }

        /** Checks whether or not a file is acceptable following the conditions stated by the given extensions. */
        @Override
        public boolean accept(File file, String name) {
            if (!this.extensions.isEmpty()) {
                Iterator it = this.extensions.iterator();

                while (it.hasNext())
                    if (name.endsWith((String) it.next())) {
                        return true;
                    }
            }

            return false;
        }
    }
}
