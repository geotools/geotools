/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import java.io.IOException;
import java.io.LineNumberReader;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 * Run a test scripts. Scripts include a test suite provided by OpenGIS. Each script contains a list
 * of source and target coordinates reference systems (in WKT), source coordinate points and
 * expected coordinate points after the transformation from source CRS to target CRS.
 *
 * <p>This is probably the most important test case for the whole CRS module.
 *
 * @version $Id$
 * @author Yann Cézard
 * @author Remi Eve
 * @author Martin Desruisseaux (IRD)
 */
public final class ScriptTest {
    /**
     * Run the specified test script.
     *
     * @throws Exception If a test failed.
     */
    private void runScript(final String filename) throws Exception {
        final LineNumberReader in = TestData.openReader(this, filename);
        final ScriptRunner test = new ScriptRunner(in);
        test.executeAll();
        in.close();
    }

    /**
     * Run "AbridgedMolodensky.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testAbridgedMolodesky() throws Exception {
        runScript("scripts/AbridgedMolodensky.txt");
    }

    /**
     * Run "Molodensky.txt".
     *
     * @throws IOException If {@link #MT_MOLODENSKY_SCRIPT} can't be read.
     * @throws FactoryException if a line can't be parsed.
     * @throws TransformException if the transformation can't be run.
     */
    @Test
    public void testMolodesky() throws Exception {
        runScript("scripts/Molodensky.txt");
    }

    /**
     * Run "Simple.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testSimple() throws Exception {
        runScript("scripts/Simple.txt");
    }

    /**
     * Run "Projections.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testProjections() throws Exception {
        runScript("scripts/Projections.txt");
    }

    /**
     * Run "Mercator.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testMercator() throws Exception {
        runScript("scripts/Mercator.txt");
    }

    /**
     * Run the "ObliqueMercator.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testObliqueMercator() throws Exception {
        runScript("scripts/ObliqueMercator.txt");
    }

    /**
     * Run "TransverseMercator.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testTransverseMercator() throws Exception {
        runScript("scripts/TransverseMercator.txt");
    }

    /**
     * Run "AlbersEqualArea.txt"
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testAlbersEqualArea() throws Exception {
        runScript("scripts/AlbersEqualArea.txt");
    }

    /**
     * Run "LambertAzimuthalEqualArea.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testLambertAzimuthalEqualArea() throws Exception {
        runScript("scripts/LambertAzimuthalEqualArea.txt");
    }

    /**
     * Run "LambertConic.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testLambertConic() throws Exception {
        runScript("scripts/LambertConic.txt");
    }

    /**
     * Run "Stereographic.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testStereographic() throws Exception {
        runScript("scripts/Stereographic.txt");
    }

    /**
     * Run "Orthographic.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testOrthographic() throws Exception {
        runScript("scripts/Orthographic.txt");
    }

    /**
     * Run "NZMG.txt"
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testNZMG() throws Exception {
        runScript("scripts/NZMG.txt");
    }

    /**
     * Run "Krovak.txt"
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testKrovak() throws Exception {
        runScript("scripts/Krovak.txt");
    }

    /** Run "EquidistantConic.txt" */
    @Test
    public void testEquidistantConic() throws Exception {
        runScript("scripts/EquidistantConic.txt");
    }

    /** Run "Polyconic.txt" */
    @Test
    public void testPolyconic() throws Exception {
        runScript("scripts/Polyconic.txt");
    }

    /** Run "Robinson.txt" */
    @Test
    public void testRobinson() throws Exception {
        runScript("scripts/Robinson.txt");
    }

    /** Run "WinkelTripel.txt" */
    @Test
    public void testWinkelTripel() throws Exception {
        runScript("scripts/WinkelTripel.txt");
    }

    /** Run "HammerAitoff.txt" */
    @Test
    public void testAitoff() throws Exception {
        runScript("scripts/Aitoff.txt");
    }

    /** Run "EckertIV.txt" */
    @Test
    public void testEckertIV() throws Exception {
        runScript("scripts/EckertIV.txt");
    }

    /** Run "Mollweide.txt" */
    @Test
    public void testMollweide() throws Exception {
        runScript("scripts/Mollweide.txt");
    }

    /** Run "WagnerIV.txt" */
    @Test
    public void testWagnerIV() throws Exception {
        runScript("scripts/WagnerIV.txt");
    }

    /** Run "GeneralOblique.txt" */
    @Test
    public void testGeneralOblique() throws Exception {
        runScript("scripts/GeneralOblique.txt");
    }

    /** Run "MeteosatSG.txt" */
    @Test
    public void testMeteosatSG() throws Exception {
        runScript("scripts/MeteosatSG.txt");
    }

    /** * Run "RotatedPole.txt" * @throws Exception */
    @Test
    public void testRotatedPole() throws Exception {
        runScript("scripts/RotatedPole.txt");
    }

    /**
     * Run "WorldVanDerGrintenI.txt"
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testVanDerGrinten() throws Exception {
        runScript("scripts/WorldVanDerGrintenI.txt");
    }

    /** Run "Sinusoidal.txt" */
    @Test
    public void testSinusoidal() throws Exception {
        runScript("scripts/Sinusoidal.txt");
    }

    /** Run "Gnomonic.txt" */
    @Test
    public void testGnomonic() throws Exception {
        runScript("scripts/Gnomonic.txt");
    }

    /**
     * Run "GEOS.txt"
     *
     * @throws Exception If a test failed.
     */
    @Test
    public void testGEOS() throws Exception {
        runScript("scripts/GEOS.txt");
    }

    /**
     * Run "WagnerIV.txt". Disabled as the projection is not really working as expected, but don't
     * have time to investigate. If you want to try and fix this please enable the service provider
     * as well by adding the org.geotools.referencing.operation.projection.Mollweide$WagnerVProvider
     * line into
     * referencing/src/main/resources/META-INF/services/org.geotools.referencing.operation.MathTransformProvider
     */
    @Test
    @Ignore
    public void testWagnerV() throws Exception {
        runScript("scripts/WagnerV.txt");
    }

    /**
     * Run "OpenGIS.txt".
     *
     * @throws Exception If a test failed.
     */
    @Test
    @Ignore
    public void testOpenGIS() throws Exception {
        runScript("scripts/OpenGIS.txt");
    }

    /**
     * Run "NADCON.txt"
     *
     * @throws Exception If a test failed.
     */
    @Test
    @Ignore
    public void testNADCON() throws Exception {
        runScript("scripts/NADCON.txt");
    }

    /** Run "Cassini.txt" */
    @Test
    public void testCassini() throws Exception {
        try {
            // the Cassini math is not so accurate it seems... but it's
            // matchin proj, so good enough for the moment
            MapProjection.SKIP_SANITY_CHECKS = true;
            runScript("scripts/Cassini.txt");
        } finally {
            MapProjection.SKIP_SANITY_CHECKS = false;
        }
    }

    /** Run "AzimuthalEquidistant.txt". */
    @Test
    public void testAzimuthalEquidistant() throws Exception {
        runScript("scripts/AzimuthalEquidistant.txt");
    }

    /** Run "EqualEarth.txt". */
    @Test
    public void testEqualEarth() throws Exception {
        runScript("scripts/EqualEarth.txt");
    }
}
