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
package org.geotools.renderer.composite;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import org.geotools.image.test.ImageAssert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CompositeTest {

    private static BufferedImage BKG;

    private static BufferedImage BKG2;

    private static BufferedImage MAP;

    private static BufferedImage MAP2;

    private String name;

    private Composite composite;

    public CompositeTest(String name, Composite composite) {
        this.name = name;
        this.composite = composite;
    }

    @BeforeClass
    public static void readSourceImages() throws Exception {
        BKG = ImageIO.read(CompositeTest.class.getResourceAsStream("test-data/bkg.png"));
        BKG2 = ImageIO.read(CompositeTest.class.getResourceAsStream("test-data/bkg2.png"));
        MAP = ImageIO.read(CompositeTest.class.getResourceAsStream("test-data/map.png"));
        MAP2 = ImageIO.read(CompositeTest.class.getResourceAsStream("test-data/map2.png"));
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        List<Object[]> result = new ArrayList<>();

        // compositing modes
        result.add(new Object[] {"copy", AlphaComposite.getInstance(AlphaComposite.SRC)});
        result.add(new Object[] {"destination", AlphaComposite.getInstance(AlphaComposite.DST)});
        result.add(
                new Object[] {"source-over", AlphaComposite.getInstance(AlphaComposite.SRC_OVER)});
        result.add(
                new Object[] {
                    "destination-over", AlphaComposite.getInstance(AlphaComposite.DST_OVER)
                });
        result.add(new Object[] {"source-in", AlphaComposite.getInstance(AlphaComposite.SRC_IN)});
        result.add(
                new Object[] {"destination-in", AlphaComposite.getInstance(AlphaComposite.DST_IN)});
        result.add(new Object[] {"source-out", AlphaComposite.getInstance(AlphaComposite.SRC_OUT)});
        result.add(
                new Object[] {
                    "destination-out", AlphaComposite.getInstance(AlphaComposite.DST_OUT)
                });
        result.add(
                new Object[] {"source-atop", AlphaComposite.getInstance(AlphaComposite.SRC_ATOP)});
        result.add(
                new Object[] {
                    "destination-atop", AlphaComposite.getInstance(AlphaComposite.DST_ATOP)
                });
        result.add(new Object[] {"xor", AlphaComposite.getInstance(AlphaComposite.XOR)});

        // blending modes
        result.add(new Object[] {"multiply", BlendComposite.MULTIPLY_COMPOSITE});
        result.add(new Object[] {"screen", BlendComposite.SCREEN_COMPOSITE});
        result.add(new Object[] {"overlay", BlendComposite.OVERLAY_COMPOSITE});
        result.add(new Object[] {"darken", BlendComposite.DARKEN_COMPOSITE});
        result.add(new Object[] {"lighten", BlendComposite.LIGHTEN_COMPOSITE});
        result.add(new Object[] {"color-dodge", BlendComposite.COLOR_DODGE_COMPOSITE});
        result.add(new Object[] {"color-burn", BlendComposite.COLOR_BURN_COMPOSITE});
        result.add(new Object[] {"hard-light", BlendComposite.HARD_LIGHT_COMPOSITE});
        result.add(new Object[] {"soft-light", BlendComposite.SOFT_LIGHT_COMPOSITE});
        result.add(new Object[] {"difference", BlendComposite.DIFFERENCE_COMPOSITE});
        result.add(new Object[] {"exclusion", BlendComposite.EXCLUSION_COMPOSITE});

        return result;
    }

    @Test
    public void testCompositeInteger1() throws Exception {
        BufferedImage bkg = convertImage(BKG, BufferedImage.TYPE_INT_ARGB);
        BufferedImage map = convertImage(MAP, BufferedImage.TYPE_INT_ARGB);

        BufferedImage blended = blend(bkg, map);
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend1-"
                                + name
                                + ".png");
        ImageAssert.assertEquals(reference, blended, 0);
    }

    @Test
    public void testCompositeByte1() throws Exception {
        BufferedImage bkg = convertImage(BKG, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage map = convertImage(MAP, BufferedImage.TYPE_4BYTE_ABGR);

        BufferedImage blended = blend(bkg, map);
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend1-"
                                + name
                                + ".png");
        ImageAssert.assertEquals(reference, blended, 0);
    }

    @Test
    public void testCompositeInteger2() throws Exception {
        BufferedImage bkg2 = convertImage(BKG2, BufferedImage.TYPE_INT_ARGB);
        BufferedImage map2 = convertImage(MAP2, BufferedImage.TYPE_INT_ARGB);

        BufferedImage blended = blend(bkg2, map2);
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend2-"
                                + name
                                + ".png");
        ImageAssert.assertEquals(reference, blended, 0);
    }

    @Test
    public void testCompositeByte2() throws Exception {
        BufferedImage bkg2 = convertImage(BKG2, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage map2 = convertImage(MAP2, BufferedImage.TYPE_4BYTE_ABGR);

        BufferedImage blended = blend(bkg2, map2);
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend2-"
                                + name
                                + ".png");
        ImageAssert.assertEquals(reference, blended, 0);
    }

    private BufferedImage blend(BufferedImage src, BufferedImage dst) {
        BufferedImage blend = new BufferedImage(src.getWidth(), dst.getWidth(), src.getType());
        Graphics2D graphics = (Graphics2D) blend.getGraphics();
        graphics.drawRenderedImage(src, new AffineTransform());
        graphics.setComposite(composite);
        graphics.drawRenderedImage(dst, new AffineTransform());

        graphics.dispose();

        return blend;
    }

    private BufferedImage convertImage(BufferedImage src, int imageType) {
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), imageType);
        Graphics2D graphics = (Graphics2D) result.getGraphics();
        graphics.drawRenderedImage(src, new AffineTransform());
        graphics.dispose();

        return result;
    }
}
