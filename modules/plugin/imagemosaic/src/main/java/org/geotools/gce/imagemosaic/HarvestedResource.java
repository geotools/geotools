/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.geotools.coverage.grid.io.DefaultHarvestedSource;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;

/** Types of Resources being supported by ImageMosaic harvest operation. */
enum HarvestedResource {
    FILE(File.class) {
        @Override
        public void harvest(
                String defaultCoverage,
                Object source,
                Hints hints,
                final List<HarvestedSource> result,
                ImageMosaicReader reader) {
            File file;
            if (source instanceof Collection<?>) {
                file = (File) ((Collection<?>) source).iterator().next();
            } else {
                file = (File) source;
            }
            // Harvesting file
            harvestCollection(defaultCoverage, result, reader, Collections.singletonList(file));
        }
    },
    DIRECTORY(File.class) {
        @Override
        public void harvest(
                String defaultCoverage,
                Object source,
                Hints hints,
                final List<HarvestedSource> result,
                ImageMosaicReader reader) {
            File directory;
            if (source instanceof Collection<?>) {
                directory = (File) ((Collection<?>) source).iterator().next();
            } else {
                directory = (File) source;
            }
            // Harvesting directory
            harvestCalculation(defaultCoverage, result, reader, directory, null);
        }
    },
    FILE_COLLECTION(File.class) {
        @Override
        public void harvest(
                String defaultCoverage,
                Object source,
                Hints hints,
                final List<HarvestedSource> result,
                final ImageMosaicReader reader) {
            // I have already checked that it is a Collection of File objects
            @SuppressWarnings("unchecked")
            Collection<File> files = (Collection<File>) source;
            harvestCollection(defaultCoverage, result, reader, files);
        }
    },
    URL(URL.class) {
        @Override
        public void harvest(
                String defaultCoverage,
                Object source,
                Hints hints,
                final List<HarvestedSource> result,
                ImageMosaicReader reader) {
            harvestURLCollection(defaultCoverage, result, reader, Collections.singletonList((URL) source));
        }
    },
    URL_COLLECTION(URL.class) {
        @Override
        public void harvest(
                String defaultCoverage,
                Object source,
                Hints hints,
                final List<HarvestedSource> result,
                ImageMosaicReader reader) {
            Collection<URL> urls = null;

            if (source instanceof Collection<?>) {
                try {
                    @SuppressWarnings("unchecked")
                    Collection<URL> cast = (Collection<URL>) source;
                    urls = cast;
                } catch (ClassCastException e) {
                    // Log the exception
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, e.getMessage(), e);
                    }
                }
            } else {
                urls = Collections.singletonList((URL) source);
            }
            // Harvesting Urls
            harvestURLCollection(defaultCoverage, result, reader, urls);
        }
    },
    URI(URI.class) {
        @Override
        public void harvest(
                String defaultCoverage,
                Object source,
                Hints hints,
                final List<HarvestedSource> result,
                ImageMosaicReader reader) {
            harvestURICollection(defaultCoverage, result, reader, Collections.singletonList((URI) source));
        }
    },
    URI_COLLECTION(URI.class) {
        @Override
        public void harvest(
                String defaultCoverage,
                Object source,
                Hints hints,
                final List<HarvestedSource> result,
                ImageMosaicReader reader) {
            Collection<URI> uris = null;

            if (source instanceof Collection<?>) {
                try {
                    @SuppressWarnings("unchecked")
                    Collection<URI> cast = (Collection<URI>) source;
                    uris = cast;
                } catch (ClassCastException e) {
                    // Log the exception
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, e.getMessage(), e);
                    }
                }
            } else {
                uris = Collections.singletonList((URI) source);
            }
            // Harvesting Urls
            harvestURICollection(defaultCoverage, result, reader, uris);
        }
    };

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(HarvestedResource.class);

    /** Harvesting of the input resource. The result will be strored inside the {@link List} object. */
    public abstract void harvest(
            String defaultCoverage,
            Object source,
            Hints hints,
            final List<HarvestedSource> result,
            ImageMosaicReader reader);

    /** Returns the HarvestedResource associated to the input Object */
    public static HarvestedResource getResourceFromObject(Object source) {
        // Check if the resource is a File or a Directory
        if (source instanceof File) {
            return getResourceFromFile((File) source);
        }
        if (source instanceof URL) {
            return URL;
        }
        if (source instanceof URI) {
            return URI;
        }
        // For a String instance, it is converted to String
        if (source instanceof String) {
            return getResourceFromString((String) source);
        }
        // Check if the input Object is a File/URL Collection
        if (source instanceof Collection<?>) {
            Object sample = ((Collection) source).iterator().next();
            // Let's check if it's a Collection of files
            if (sample instanceof File) {
                Collection<File> files = null;
                try {
                    @SuppressWarnings("unchecked")
                    Collection<File> cast = (Collection<File>) source;
                    files = cast;
                } catch (ClassCastException e) {
                    // Log the exception
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, e.getMessage(), e);
                    }
                }
                // If the files are present
                if (files != null) {
                    // No File is saved
                    int fileSize = files.size();
                    // Check on the File Size
                    if (fileSize < 1) {
                        return null;
                    } else if (fileSize == 1) {
                        // If the Collection size is 1 then the object can be only a file or a
                        // directory
                        return getResourceFromFile(files.iterator().next());
                    } else {
                        return FILE_COLLECTION;
                    }
                }
                // We only support File and URL and we assume the collection
                // will be of homogeneous type.
            } else if (sample instanceof URL) {
                return URL_COLLECTION;
            } else if (sample instanceof String) {
                HarvestedResource resource = getResourceFromString((String) sample);
                switch (resource) {
                    case FILE:
                        return FILE_COLLECTION;
                    case URL:
                        return URL_COLLECTION;
                    case URI:
                        return URI_COLLECTION;
                    default:
                        throw new RuntimeException("Unexepected collection content: " + resource);
                }
            }
        }
        return null;
    }

    private static HarvestedResource getResourceFromString(String source) {
        java.net.URL url = null;
        try {
            url = new URL(source);
        } catch (MalformedURLException e) {
            // We are guessing the type doing attempts.
            // Don't log exceptions
        }

        String protocol = null;
        if (url != null && (protocol = url.getProtocol()) != null && !"file".equalsIgnoreCase(protocol)) {
            return URL;
        }

        try {
            URI uri = new URI(source);
            if (uri.getScheme() != null && !"file".equals(uri.getScheme())) {
                return URI;
            }
        } catch (URISyntaxException e) {
            // still doing just guesses, see above
        }

        File file = new File(source);
        return getResourceFromFile(file);
    }

    /** Check if the File Object is a DIRECTORY or not and return the associated {@link HarvestedResource} */
    private static HarvestedResource getResourceFromFile(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                return DIRECTORY;
            } else {
                return FILE;
            }
        }
        return null;
    }

    /** Method for harvesting on a directory */
    private static void harvestCollection(
            String defaultCoverage,
            final List<HarvestedSource> result,
            final ImageMosaicReader reader,
            Collection<File> files) {

        // prepare the walker configuration
        CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();
        configuration.setParameter(Utils.Prop.ABSOLUTE_PATH, Boolean.toString(Utils.DEFAULT_PATH_BEHAVIOR));

        // Setting of the HARVEST_DIRECTORY property for passing the checks even if it is
        // not used
        // Selection of the first file
        Iterator<File> it = files.iterator();
        String indexingPath = it.next().getAbsolutePath();
        configuration.setParameter(Utils.Prop.HARVEST_DIRECTORY, indexingPath);

        if (defaultCoverage == null) {
            String[] coverageNames = reader.getGridCoverageNames();
            defaultCoverage =
                    coverageNames != null && coverageNames.length > 0 ? coverageNames[0] : Utils.DEFAULT_INDEX_NAME;
        }

        configuration.setParameter(Utils.Prop.INDEX_NAME, defaultCoverage);
        configuration.setHints(new Hints(Utils.MOSAIC_READER, reader));

        File mosaicSource = URLs.urlToFile(reader.sourceURL);
        if (!mosaicSource.isDirectory()) {
            mosaicSource = mosaicSource.getParentFile();
        }

        configuration.setParameter(Utils.Prop.ROOT_MOSAIC_DIR, mosaicSource.getAbsolutePath());

        // run the walker and collect information
        ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
        final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(configuration, eventHandler) {
            @Override
            protected GranuleCatalog buildCatalog() throws IOException {
                return reader.granuleCatalog;
            }
        };
        // Creation of the Walker for the File List
        ImageMosaicReader.ImageMosaicFileCollectionWalker walker =
                new ImageMosaicReader.ImageMosaicFileCollectionWalker(catalogHandler, eventHandler, files);
        eventHandler.addProcessingEventListener(new ImageMosaicEventHandlers.ProcessingEventListener() {

            @Override
            public void getNotification(ImageMosaicEventHandlers.ProcessingEvent event) {
                if (event instanceof ImageMosaicEventHandlers.FileProcessingEvent) {
                    ImageMosaicEventHandlers.FileProcessingEvent fileEvent =
                            (ImageMosaicEventHandlers.FileProcessingEvent) event;
                    result.add(new DefaultHarvestedSource(
                            fileEvent.getFile(), fileEvent.isIngested(), fileEvent.getMessage()));
                }
            }

            @Override
            public void exceptionOccurred(ImageMosaicEventHandlers.ExceptionEvent event) {
                // nothing to do
            }
        });
        // Wait the Walker ends its operations
        walker.run();
    }

    /** Method for harvesting on a directory */
    private static void harvestURLCollection(
            String defaultCoverage,
            final List<HarvestedSource> result,
            final ImageMosaicReader reader,
            Collection<URL> urls) {

        // prepare the walker configuration
        CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();

        if (defaultCoverage == null) {
            String[] coverageNames = reader.getGridCoverageNames();
            defaultCoverage =
                    coverageNames != null && coverageNames.length > 0 ? coverageNames[0] : Utils.DEFAULT_INDEX_NAME;
        }

        configuration.setParameter(Utils.Prop.INDEX_NAME, defaultCoverage);
        configuration.setHints(new Hints(Utils.MOSAIC_READER, reader));
        File mosaicSource = URLs.urlToFile(reader.sourceURL);
        if (!mosaicSource.isDirectory()) {
            mosaicSource = mosaicSource.getParentFile();
        }
        // Setting of the HARVEST_DIRECTORY property for passing the checks even if it is
        // not used
        String mosaicRootPath = mosaicSource.getAbsolutePath();
        configuration.setParameter(Utils.Prop.HARVEST_DIRECTORY, mosaicRootPath);
        configuration.setParameter(Utils.Prop.ROOT_MOSAIC_DIR, mosaicRootPath);

        // run the walker and collect information
        ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
        final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(configuration, eventHandler) {
            @Override
            protected GranuleCatalog buildCatalog() throws IOException {
                return reader.granuleCatalog;
            }
        };

        SourceSPIProviderFactory urlSourceSPIProvider = null;
        RasterManager rasterManager = reader.getRasterManager(defaultCoverage);
        if (rasterManager == null) {
            // We might be in the case of an empty mosaic not yet initialized.
            // let's try with the indexer parsing if any.
            Indexer indexer = IndexerUtils.initializeIndexer(null, mosaicSource);
            boolean canBeEmpty = IndexerUtils.getParameterAsBoolean(Utils.Prop.CAN_BE_EMPTY, indexer);
            if (!canBeEmpty) {
                eventHandler.fireException(
                        new IOException("The specified mosaic can't be empty but no default granules have been found"));
                return;
            }
            urlSourceSPIProvider = IndexerUtils.getSourceSPIProviderFactory(indexer);
        } else {
            MosaicConfigurationBean config = rasterManager.getConfiguration();
            urlSourceSPIProvider = config.getCatalogConfigurationBean().getUrlSourceSPIProvider();
        }

        if (urlSourceSPIProvider == null) {
            eventHandler.fireException(new IOException(
                    "Unable to harvest the provided URL collection. No source SPI provider has been found"));
            return;
        }

        // Creation of the Walker for the URL List
        ImageMosaicReader.ImageMosaicURLCollectionWalker walker = new ImageMosaicReader.ImageMosaicURLCollectionWalker(
                catalogHandler, eventHandler, urlSourceSPIProvider, urls);
        eventHandler.addProcessingEventListener(new ImageMosaicEventHandlers.ProcessingEventListener() {

            @Override
            public void getNotification(ImageMosaicEventHandlers.ProcessingEvent event) {
                if (event instanceof ImageMosaicEventHandlers.URLProcessingEvent) {
                    ImageMosaicEventHandlers.URLProcessingEvent urlEvent =
                            (ImageMosaicEventHandlers.URLProcessingEvent) event;
                    result.add(new DefaultHarvestedSource(
                            urlEvent.getUrl(), urlEvent.isIngested(), urlEvent.getMessage()));
                }
            }

            @Override
            public void exceptionOccurred(ImageMosaicEventHandlers.ExceptionEvent event) {
                // nothing to do
            }
        });
        // Wait the Walker ends its operations
        walker.run();
    }

    /** Method for harvesting on a list of URIs (which are typically not valid URLs */
    private static void harvestURICollection(
            String defaultCoverage,
            final List<HarvestedSource> result,
            final ImageMosaicReader reader,
            Collection<URI> urls) {

        // prepare the walker configuration
        CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();

        if (defaultCoverage == null) {
            String[] coverageNames = reader.getGridCoverageNames();
            defaultCoverage =
                    coverageNames != null && coverageNames.length > 0 ? coverageNames[0] : Utils.DEFAULT_INDEX_NAME;
        }

        configuration.setParameter(Utils.Prop.INDEX_NAME, defaultCoverage);
        configuration.setHints(new Hints(Utils.MOSAIC_READER, reader));
        File mosaicSource = URLs.urlToFile(reader.sourceURL);
        if (!mosaicSource.isDirectory()) {
            mosaicSource = mosaicSource.getParentFile();
        }
        // Setting of the HARVEST_DIRECTORY property for passing the checks even if it is
        // not used
        String mosaicRootPath = mosaicSource.getAbsolutePath();
        configuration.setParameter(Utils.Prop.HARVEST_DIRECTORY, mosaicRootPath);
        configuration.setParameter(Utils.Prop.ROOT_MOSAIC_DIR, mosaicRootPath);

        // run the walker and collect information
        ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
        final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(configuration, eventHandler) {
            @Override
            protected GranuleCatalog buildCatalog() throws IOException {
                return reader.granuleCatalog;
            }
        };

        SourceSPIProviderFactory sspFactory = null;
        RasterManager rasterManager = reader.getRasterManager(defaultCoverage);
        if (rasterManager == null) {
            // We might be in the case of an empty mosaic not yet initialized.
            // let's try with the indexer parsing if any.
            Indexer indexer = IndexerUtils.initializeIndexer(null, mosaicSource);
            boolean canBeEmpty = IndexerUtils.getParameterAsBoolean(Utils.Prop.CAN_BE_EMPTY, indexer);
            if (!canBeEmpty) {
                eventHandler.fireException(
                        new IOException("The specified mosaic can't be empty but no default granules have been found"));
                return;
            }
            sspFactory = IndexerUtils.getSourceSPIProviderFactory(indexer);
        } else {
            MosaicConfigurationBean config = rasterManager.getConfiguration();
            sspFactory = config.getCatalogConfigurationBean().getUrlSourceSPIProvider();
        }

        if (sspFactory == null) {
            eventHandler.fireException(new IOException(
                    "Unable to harvest the provided URI collection. No source SPI provider has been found"));
            return;
        }

        // Creation of the Walker for the URI List
        ImageMosaicReader.ImageMosaicURICollectionWalker walker =
                new ImageMosaicReader.ImageMosaicURICollectionWalker(catalogHandler, eventHandler, sspFactory, urls);
        eventHandler.addProcessingEventListener(new ImageMosaicEventHandlers.ProcessingEventListener() {

            @Override
            public void getNotification(ImageMosaicEventHandlers.ProcessingEvent event) {
                if (event instanceof ImageMosaicEventHandlers.URIProcessingEvent) {
                    ImageMosaicEventHandlers.URIProcessingEvent uriEvent =
                            (ImageMosaicEventHandlers.URIProcessingEvent) event;
                    result.add(new DefaultHarvestedSource(
                            uriEvent.getURI(), uriEvent.isIngested(), uriEvent.getMessage()));
                }
            }

            @Override
            public void exceptionOccurred(ImageMosaicEventHandlers.ExceptionEvent event) {
                // nothing to do
            }
        });
        // Wait the Walker ends its operations
        walker.run();
    }

    /** Method for harvesting on a directory */
    private static void harvestCalculation(
            String defaultCoverage,
            final List<HarvestedSource> result,
            final ImageMosaicReader reader,
            File directory,
            IOFileFilter filter) {
        // prepare the walker configuration
        CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();
        configuration.setParameter(Utils.Prop.ABSOLUTE_PATH, Boolean.toString(Utils.DEFAULT_PATH_BEHAVIOR));
        String indexingPath = directory.getAbsolutePath();
        configuration.setParameter(Utils.Prop.HARVEST_DIRECTORY, indexingPath);
        if (defaultCoverage == null) {
            String[] coverageNames = reader.getGridCoverageNames();
            defaultCoverage =
                    coverageNames != null && coverageNames.length > 0 ? coverageNames[0] : Utils.DEFAULT_INDEX_NAME;
        }
        configuration.setParameter(Utils.Prop.INDEX_NAME, defaultCoverage);
        configuration.setHints(new Hints(Utils.MOSAIC_READER, reader));

        File mosaicSource = URLs.urlToFile(reader.sourceURL);
        if (!mosaicSource.isDirectory()) {
            mosaicSource = mosaicSource.getParentFile();
        }

        configuration.setParameter(Utils.Prop.ROOT_MOSAIC_DIR, mosaicSource.getAbsolutePath());

        // run the walker and collect information
        ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
        final ImageMosaicConfigHandler catalogHandler =
                new HarvestedResource.HarvestMosaicConfigHandler(configuration, eventHandler, reader);
        // build the index
        ImageMosaicDirectoryWalker walker = new ImageMosaicDirectoryWalker(catalogHandler, eventHandler, filter);
        eventHandler.addProcessingEventListener(new ImageMosaicEventHandlers.ProcessingEventListener() {

            @Override
            public void getNotification(ImageMosaicEventHandlers.ProcessingEvent event) {
                if (event instanceof ImageMosaicEventHandlers.FileProcessingEvent) {
                    ImageMosaicEventHandlers.FileProcessingEvent fileEvent =
                            (ImageMosaicEventHandlers.FileProcessingEvent) event;
                    result.add(new DefaultHarvestedSource(
                            fileEvent.getFile(), fileEvent.isIngested(), fileEvent.getMessage()));
                }
            }

            @Override
            public void exceptionOccurred(ImageMosaicEventHandlers.ExceptionEvent event) {
                // nothing to do
            }
        });

        walker.run();
    }

    Class<?> elementType;

    HarvestedResource(Class<?> elementType) {
        this.elementType = elementType;
    }

    /**
     * Returns the element type of the resource. It can be the actual type, for single sources, or the type of the
     * contained element, for collection sources
     */
    public Class<?> getElementType() {
        return elementType;
    }

    private static class HarvestMosaicConfigHandler extends ImageMosaicConfigHandler {
        private final ImageMosaicReader reader;

        public HarvestMosaicConfigHandler(
                CatalogBuilderConfiguration configuration,
                ImageMosaicEventHandlers eventHandler,
                ImageMosaicReader reader) {
            super(configuration, eventHandler);
            this.reader = reader;
        }

        @Override
        protected GranuleCatalog buildCatalog() throws IOException {
            return reader.granuleCatalog;
        }

        @Override
        public Map<String, MosaicConfigurationBean> getConfigurations() {
            Map<String, MosaicConfigurationBean> configurations = super.getConfigurations();
            if (configurations.isEmpty()) {
                // populate with the existing configurations
                for (String coverage : reader.getGridCoverageNames()) {
                    MosaicConfigurationBean base =
                            reader.getRasterManager(coverage).getConfiguration();
                    configurations.put(coverage, new MosaicConfigurationBean(base));
                }
            }

            return configurations;
        }
    }
}
