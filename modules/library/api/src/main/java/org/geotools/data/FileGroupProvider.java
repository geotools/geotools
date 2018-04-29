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

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link FileGroup} provider allowing to return a {@link CloseableIterator} of {@link
 * FileGroup}s.
 */
public interface FileGroupProvider {

    /**
     * A Group of Files consisting of a reference to a mainFile, plus a set of support Files (if
     * any) and metadata map.
     */
    public static class FileGroup {

        @Override
        public String toString() {
            return "FileGroup [mainFile="
                    + mainFile
                    + ", supportFiles="
                    + supportFiles
                    + ", metadata="
                    + printMetadata(metadata)
                    + "]";
        }

        private String printMetadata(Map<String, Object> metadata) {
            if (metadata != null && !metadata.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                Set<String> keys = metadata.keySet();
                for (String key : keys) {
                    builder.append(key).append("=").append(metadata.get(key)).append("\n");
                }
                return builder.toString();
            } else {
                return null;
            }
        }

        public FileGroup(File mainFile, List<File> supportFiles, Map<String, Object> metadata) {
            this.mainFile = mainFile;
            this.supportFiles = supportFiles;
            this.metadata = metadata;
        }

        /** The main File of the group */
        File mainFile = null;

        /** The support files (if any) */
        List<File> supportFiles = null;

        /**
         * Metadata for this group. As an instance, domain information) A sample entry of that
         * mapping could be <"time",DateRange> to indicate that this group is covering the reported
         * time range.
         */
        Map<String, Object> metadata;

        public void setMainFile(File mainFile) {
            this.mainFile = mainFile;
        }

        public void setSupportFiles(List<File> supportFiles) {
            this.supportFiles = supportFiles;
        }

        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }

        public Map<String, Object> getMetadata() {
            return metadata;
        }

        public File getMainFile() {
            return mainFile;
        }

        public List<File> getSupportFiles() {
            return supportFiles;
        }
    }

    /**
     * Return {@link FileGroup}s matching the specified query (if any).
     *
     * <p>Specifying a <code>null</code> query will result in returning all the available {@link
     * FileGroup}s.
     */
    CloseableIterator<FileGroup> getFiles(Query query);
}
