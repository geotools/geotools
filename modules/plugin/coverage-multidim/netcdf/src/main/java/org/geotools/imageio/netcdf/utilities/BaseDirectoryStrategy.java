/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.imageio.netcdf.utilities;

import static org.geotools.imageio.netcdf.utilities.NetCDFUtilities.NETCDF_DATA_DIR;
import static org.geotools.imageio.netcdf.utilities.NetCDFUtilities.NETCDF_ROOT;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/** Strategy for determining the base directory for the NetCDF ancillary files. */
public abstract class BaseDirectoryStrategy {
    private static final Logger LOGGER = Logging.getLogger(BaseDirectoryStrategy.class);

    /**
     * Get the base directory for ancillary files.
     *
     * @param parentDirectory
     * @return
     */
    public abstract File getBaseDirectory(File parentDirectory);

    /** Creates the strategy for the base directory. */
    public static BaseDirectoryStrategy createStrategy() {
        File externalDir = getSystemPropertyDirectory(NETCDF_DATA_DIR);

        // if there is no external directory, go for the default, sidecar directory
        if (externalDir == null) return new SidecarDirectoryStrategy();

        boolean tree = Boolean.getBoolean(NetCDFUtilities.NETCDF_DATA_DIR_TREE);
        if (!tree) return new FlatDirectoryStrategy(externalDir);

        File rootDir = getSystemPropertyDirectory(NETCDF_ROOT);
        if (rootDir == null) return new HierarchicalDirectoryStrategy(externalDir);
        return new HierarchicalRelativeDirectoryStrategy(externalDir, rootDir);
    }

    /** On Windows we need to remove the colon from the path */
    static String sanitizeWindowsPath(String absolutePath) {
        return absolutePath.replace(":", "");
    }

    /**
     * Fetch a directory from the system properties, check it exists, it's a directory, it's writable, otherwise return
     * <code>null</code>
     *
     * @param propertyName
     * @return
     */
    static File getSystemPropertyDirectory(String propertyName) {
        return Optional.ofNullable(System.getProperty(propertyName))
                .map(p -> new File(p))
                .filter(f -> isValidDir(f, propertyName))
                .orElse(null);
    }

    private static boolean isValidDir(File file, String dirPropertyName) {
        String dir = file.getAbsolutePath();
        if (!file.exists()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("The specified "
                        + dirPropertyName
                        + " property doesn't refer "
                        + "to an existing folder. Please check the path: "
                        + dir);
            }
            return false;
        } else if (!file.isDirectory()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("The specified "
                        + dirPropertyName
                        + " property doesn't refer "
                        + "to a directory. Please check the path: "
                        + dir);
            }
            return false;
        } else if (!file.canWrite()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("The specified "
                        + dirPropertyName
                        + " property refers to "
                        + "a directory which can't be written. Please check the path and"
                        + " the permissions for: "
                        + dir);
            }
            return false;
        }
        return true;
    }

    /** Ancillary files are placed in the same parent directory as the netcdf files. This is the default. */
    private static class SidecarDirectoryStrategy extends BaseDirectoryStrategy {

        @Override
        public File getBaseDirectory(File parentDirectory) {
            return parentDirectory;
        }
    }

    /** Ancillary files are stored in an external flat directory */
    private static class FlatDirectoryStrategy extends BaseDirectoryStrategy {
        private final File externalDirectory;

        public FlatDirectoryStrategy(File externalDirectory) {
            this.externalDirectory = externalDirectory;
        }

        @Override
        public File getBaseDirectory(File parentDirectory) {
            return externalDirectory;
        }
    }

    /** Ancillary files are stored in a directory structure that mirrors the netcdf files, from the root */
    private static class HierarchicalDirectoryStrategy extends BaseDirectoryStrategy {
        private final File externalDirectory;

        public HierarchicalDirectoryStrategy(File externalDirectory) {
            this.externalDirectory = externalDirectory;
        }

        @Override
        public File getBaseDirectory(File parentDirectory) {
            return new File(externalDirectory, sanitizeWindowsPath(parentDirectory.getAbsolutePath()));
        }
    }

    /** Ancillary files are stored in a directory structure that mirrors the netcdf files, from a configured base */
    private static class HierarchicalRelativeDirectoryStrategy extends BaseDirectoryStrategy {
        private final File externalDirectory;
        private final File netcdfRoot;

        public HierarchicalRelativeDirectoryStrategy(File externalDirectory, File netcdfRoot) {
            this.externalDirectory = externalDirectory;
            this.netcdfRoot = netcdfRoot;
        }

        @Override
        public File getBaseDirectory(File parentDirectory) {
            Path relative = netcdfRoot.toPath().relativize(parentDirectory.toPath());
            return new File(externalDirectory, relative.toString());
        }
    }
}
