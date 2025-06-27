/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;

/** An elementary source element (a File, a URL, ...) of an ImageMosaic */
abstract class ImageMosaicSourceElement<T> {

    /** Return the inner object */
    abstract T getInnerObject();

    /** Get the location of this element, taking into account the configuration */
    abstract String getLocation(CatalogBuilderConfiguration config) throws IOException;

    /**
     * Provide this element to the specified {@link PropertiesCollector} so that it can collect it for properties
     * extraction
     */
    abstract void addToCollector(PropertiesCollector pc);

    /** Have the provided eventHandler firing an event related to the harvesting of this element */
    abstract void fireHarvestingEvent(
            ImageMosaicEventHandlers eventHandler, int elementIndex, int numElements, String message);

    /** a {@link File} ImageMosaic source element */
    static class FileElement extends ImageMosaicSourceElement<File> {

        File file;

        FileElement(File file) {
            this.file = file;
        }

        @Override
        File getInnerObject() {
            return file;
        }

        @Override
        String getLocation(CatalogBuilderConfiguration config) throws IOException {
            return prepareLocation(config, file);
        }

        @Override
        void addToCollector(PropertiesCollector pc) {
            pc.collect(getInnerObject());
        }

        @Override
        void fireHarvestingEvent(
                ImageMosaicEventHandlers eventHandler, int elementIndex, int numElements, String message) {
            eventHandler.fireFileEvent(Level.FINE, file, true, message, (elementIndex + 1) * 99.0 / numElements);
        }

        /** Prepare the location on top of the configuration and file to be processed. */
        private static String prepareLocation(
                CatalogBuilderConfiguration runConfiguration, final File fileBeingProcessed) throws IOException {
            // absolute
            String pathType = runConfiguration.getParameter(Utils.Prop.PATH_TYPE);
            String absolutePath = runConfiguration.getParameter(Utils.Prop.ABSOLUTE_PATH);
            if (Boolean.valueOf(absolutePath) || PathType.ABSOLUTE.name().equals(pathType)) {
                return fileBeingProcessed.getAbsolutePath();
            }

            // relative (harvesting of PathType.URL is not supported)
            String targetPath = fileBeingProcessed.getCanonicalPath();
            String basePath = runConfiguration.getParameter(Utils.Prop.ROOT_MOSAIC_DIR);
            String relative = getRelativePath(targetPath, basePath, File.separator);
            // escaping
            return relative;
        }

        /**
         * Get the relative path from one file to another, specifying the directory separator. If one of the provided
         * resources does not exist, it is assumed to be a file unless it ends with '/' or '\'.
         *
         * @param targetPath targetPath is calculated to this file
         * @param basePath basePath is calculated from this file
         * @param pathSeparator directory separator. The platform default is not assumed so that we can test Unix
         *     behaviour when running on Windows (for example)
         */
        private static String getRelativePath(String targetPath, String basePath, String pathSeparator) {

            // Normalize the paths
            String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
            String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

            // Undo the changes to the separators made by normalization
            if (pathSeparator.equals("/")) {
                normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
                normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);

            } else if (pathSeparator.equals("\\")) {
                normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
                normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);

            } else {
                throw new IllegalArgumentException("Unrecognised dir separator '" + pathSeparator + "'");
            }

            String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
            String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

            // First get all the common elements. Store them as a string,
            // and also count how many of them there are.
            StringBuilder common = new StringBuilder();

            int commonIndex = 0;
            while (commonIndex < target.length
                    && commonIndex < base.length
                    && target[commonIndex].equals(base[commonIndex])) {
                common.append(target[commonIndex] + pathSeparator);
                commonIndex++;
            }

            if (commonIndex == 0) {
                // No single common path element. This most
                // likely indicates differing drive letters, like C: and D:.
                // These paths cannot be relativized.
                throw new RuntimeException("No common path element found for '"
                        + normalizedTargetPath
                        + "' and '"
                        + normalizedBasePath
                        + "'");
            }

            // The number of directories we have to backtrack depends on whether the base is a file
            // or a dir
            // For example, the relative path from
            //
            // /foo/bar/baz/gg/ff to /foo/bar/baz
            //
            // ".." if ff is a file
            // "../.." if ff is a directory
            //
            // The following is a heuristic to figure out if the base refers to a file or dir. It's
            // not perfect, because the resource referred to by this path may not actually exist,
            // but it's the best I can do
            boolean baseIsFile = true;

            File baseResource = new File(normalizedBasePath);

            if (baseResource.exists()) {
                baseIsFile = baseResource.isFile();

            } else if (basePath.endsWith(pathSeparator)) {
                baseIsFile = false;
            }

            StringBuilder relative = new StringBuilder();

            if (base.length != commonIndex) {
                int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

                for (int i = 0; i < numDirsUp; i++) {
                    relative.append(".." + pathSeparator);
                }
            }
            relative.append(normalizedTargetPath.substring(common.length()));
            return relative.toString();
        }
    }

    /** A {@link URL} ImageMosaic source element. */
    static class URLElement extends ImageMosaicSourceElement<URL> {

        URL url;

        public URLElement(URL url) {
            this.url = url;
        }

        @Override
        URL getInnerObject() {
            return url;
        }

        @Override
        String getLocation(CatalogBuilderConfiguration config) throws IOException {
            return url.toString();
        }

        @Override
        void addToCollector(PropertiesCollector pc) {
            pc.collect(getInnerObject());
        }

        @Override
        void fireHarvestingEvent(
                ImageMosaicEventHandlers eventHandler, int elementIndex, int numElements, String message) {
            eventHandler.fireUrlEvent(Level.FINE, url, true, message, (elementIndex + 1) * 99.0 / numElements);
        }
    }

    /** A {@link URL} ImageMosaic source element. */
    static class URIElement extends ImageMosaicSourceElement<URI> {

        URI uri;

        public URIElement(URI url) {
            this.uri = url;
        }

        @Override
        URI getInnerObject() {
            return uri;
        }

        @Override
        String getLocation(CatalogBuilderConfiguration config) throws IOException {
            return uri.toString();
        }

        @Override
        void addToCollector(PropertiesCollector pc) {
            pc.collect(getInnerObject());
        }

        @Override
        void fireHarvestingEvent(
                ImageMosaicEventHandlers eventHandler, int elementIndex, int numElements, String message) {
            eventHandler.fireURIEvent(Level.FINE, uri, true, message, (elementIndex + 1) * 99.0 / numElements);
        }
    }
}
