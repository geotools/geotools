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

import static org.geotools.imageio.netcdf.utilities.BaseDirectoryStrategy.sanitizeWindowsPath;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.io.netcdf.NetCDFReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseDirectoryStrategyTest {

    public static final File NETCDF_CONTAINER = new File("target/netcdfDir").getAbsoluteFile();
    public static final File FLAT_DATA_DIR = new File("target/flatDataDir").getAbsoluteFile();
    public static final File HIERARCHICAL_DATA_DIR = new File("target/hierarchicalDataDir").getAbsoluteFile();
    public static final File RELATIVE_DATA_DIR = new File("target/relativeDataDir").getAbsoluteFile();

    @Before
    public void setup() throws Exception {
        clearSystemProperties();
    }

    @After
    public void tearDown() throws Exception {
        clearSystemProperties();
        FileUtils.deleteDirectory(FLAT_DATA_DIR);
        FileUtils.deleteDirectory(HIERARCHICAL_DATA_DIR);
        FileUtils.deleteDirectory(RELATIVE_DATA_DIR);
        FileUtils.deleteDirectory(NETCDF_CONTAINER);
    }

    private void clearSystemProperties() {
        System.clearProperty(NetCDFUtilities.NETCDF_DATA_DIR);
        System.clearProperty(NetCDFUtilities.NETCDF_DATA_DIR_TREE);
        System.clearProperty(NetCDFUtilities.NETCDF_ROOT);
    }

    @Test
    public void testSidecar() throws Exception {
        BaseDirectoryStrategy strategy = BaseDirectoryStrategy.createStrategy();
        File parentDirectory = new File("target/parent");
        assertEquals(parentDirectory, strategy.getBaseDirectory(parentDirectory));
    }

    @Test
    public void testSidecarIntegration() throws Exception {
        setupAndopenTestNetcdf(NETCDF_CONTAINER);
        checkSidecarDirectory(NETCDF_CONTAINER, "fivedim");
    }

    @Test
    public void testFlat() throws Exception {
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR, FLAT_DATA_DIR.getAbsolutePath());

        FLAT_DATA_DIR.mkdirs();
        BaseDirectoryStrategy strategy = BaseDirectoryStrategy.createStrategy();
        assertEquals(FLAT_DATA_DIR, strategy.getBaseDirectory(new File("target/parent1")));
        assertEquals(FLAT_DATA_DIR, strategy.getBaseDirectory(new File("target/parent2")));
    }

    @Test
    public void testFlatIntegration() throws Exception {
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR, FLAT_DATA_DIR.getAbsolutePath());

        FLAT_DATA_DIR.mkdirs();
        setupAndopenTestNetcdf(NETCDF_CONTAINER);
        checkSidecarDirectory(FLAT_DATA_DIR, "fivedim");
    }

    @Test
    public void testFullHierarchy() throws Exception {
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR, HIERARCHICAL_DATA_DIR.getAbsolutePath());
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR_TREE, "true");

        HIERARCHICAL_DATA_DIR.mkdirs();
        BaseDirectoryStrategy strategy = BaseDirectoryStrategy.createStrategy();
        File parent1 = new File("target/parent1").getAbsoluteFile();
        assertEquals(
                new File(HIERARCHICAL_DATA_DIR, sanitizeWindowsPath(parent1.getPath())),
                strategy.getBaseDirectory(parent1));
        File parent2 = new File("target/parent2").getAbsoluteFile();
        assertEquals(
                new File(HIERARCHICAL_DATA_DIR, sanitizeWindowsPath(parent2.getPath())),
                strategy.getBaseDirectory(parent2));
    }

    @Test
    public void testFullHierarchyIntegration() throws Exception {
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR, HIERARCHICAL_DATA_DIR.getAbsolutePath());
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR_TREE, "true");

        HIERARCHICAL_DATA_DIR.mkdirs();
        setupAndopenTestNetcdf(NETCDF_CONTAINER);
        checkSidecarDirectory(
                new File(HIERARCHICAL_DATA_DIR, sanitizeWindowsPath(NETCDF_CONTAINER.getPath())), "fivedim");
    }

    @Test
    public void testRelativeHierarchy() throws Exception {
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR, RELATIVE_DATA_DIR.getAbsolutePath());
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR_TREE, "true");
        System.setProperty(NetCDFUtilities.NETCDF_ROOT, new File("target").getAbsolutePath());

        RELATIVE_DATA_DIR.mkdirs();
        BaseDirectoryStrategy strategy = BaseDirectoryStrategy.createStrategy();
        File parent1 = new File("target/parent1").getAbsoluteFile();
        assertEquals(new File(RELATIVE_DATA_DIR, "parent1"), strategy.getBaseDirectory(parent1));
        File parent2 = new File("target/parent2").getAbsoluteFile();
        assertEquals(new File(RELATIVE_DATA_DIR, "parent2"), strategy.getBaseDirectory(parent2));
    }

    @Test
    public void testRelativeHierarchyIntegration() throws Exception {
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR, RELATIVE_DATA_DIR.getAbsolutePath());
        System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR_TREE, "true");
        System.setProperty(NetCDFUtilities.NETCDF_ROOT, new File("target").getAbsolutePath());

        RELATIVE_DATA_DIR.mkdirs();
        setupAndopenTestNetcdf(NETCDF_CONTAINER);
        checkSidecarDirectory(new File(RELATIVE_DATA_DIR, NETCDF_CONTAINER.getName()), "fivedim");
    }

    private static void checkSidecarDirectory(File parentDirectory, String ncFileName) {
        File[] sidecarDirs = parentDirectory.listFiles(fn -> fn.getName().startsWith("." + ncFileName + "_"));
        assertNotNull("Parent directory did not exist: " + parentDirectory, sidecarDirs);
        assertEquals(1, sidecarDirs.length);
        File sidecarDir = sidecarDirs[0];
        assertTrue(sidecarDir.isDirectory());
        Set<String> sidecarFiles =
                Arrays.stream(sidecarDir.listFiles()).map(f -> f.getName()).collect(Collectors.toSet());
        assertTrue(sidecarFiles.contains("origin.txt"));
        assertTrue(sidecarFiles.contains(ncFileName + ".idx"));
        assertTrue(sidecarFiles.contains(ncFileName + ".data.db"));
    }

    private static void setupAndopenTestNetcdf(File flatRootDir) throws IOException {
        flatRootDir.mkdirs();
        File netcdf = new File("src/test/resources/org/geotools/coverage/io/netcdf/test-data/fivedim.nc");
        FileUtils.copyFileToDirectory(netcdf, flatRootDir);
        NetCDFReader reader = new NetCDFReader(new File(flatRootDir, "fivedim.nc"), null);
        assertArrayEquals(new String[] {"D"}, reader.getGridCoverageNames());
        reader.dispose();
    }
}
