/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.geometry.jts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

/** @source $URL$ */
public class GeometryClipperTest {

    static final double EPS = 1e-3;
    static final double EPS_CORNERS = 1e-1;
    GeometryClipper clipper;
    Geometry boundsPoly;
    WKTReader wkt;

    @Before
    public void setUp() throws Exception {
        // System.setProperty("org.geotools.test.interactive", "true");
        clipper = new GeometryClipper(new Envelope(0, 10, 0, 10));
        wkt = new WKTReader();
        boundsPoly = wkt.read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
    }

    @Test
    public void testFullyInside() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(1 1, 2 5, 9 1)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertTrue(ls.equalsExact(clipped));
        showResult("Fully inside", ls, clipped);
    }

    @Test
    public void testInsideBorders() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(0 0, 2 5, 10 0)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertTrue(ls.equalsExact(clipped));
        showResult("Inside touching borders", ls, clipped);
    }

    @Test
    public void testFullyOutside() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 0, -5 15, 15 15)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertNull(clipped);
        showResult("Inside touching borders", ls, clipped);
    }

    @Test
    public void testCross() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 -5, 15 15)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertTrue(clipped.equalsExact(wkt.read("LINESTRING(0 0, 10 10)")));
        showResult("Cross", ls, clipped);
    }

    @Test
    public void testTouchLine() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(0 0, 0 10)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertTrue(clipped.equalsExact(ls));
        showResult("Touch border", ls, clipped);
    }

    @Test
    public void testTouchPoint() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 5, 0 5)");
        Geometry clipped = clipper.clip(ls, false);
        assertNull(clipped);
        showResult("Touch point", ls, clipped);
    }

    @Test
    public void testMultiTouch() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 0, 0 1, -5 2, 0 3, -5 4, 0 5)");
        Geometry clipped = clipper.clip(ls, false);
        assertNull(clipped);
        showResult("Multitouch", ls, clipped);
    }

    @Test
    public void testTouchAndCross() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 0, 0 1, -5 2, 5 2, 5 3, -5 3, 0 4)");
        Geometry clipped = clipper.clip(ls, false);
        assertTrue(clipped.equalsExact(wkt.read("LINESTRING(0 2, 5 2, 5 3, 0 3)")));
        showResult("Touch and cross", ls, clipped);
    }

    @Test
    public void testTouchAndParallel() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 0, 0 1, -5 2, 0 2, 0 3, -5 3, 0 4)");
        Geometry clipped = clipper.clip(ls, false);
        assertTrue(clipped.equalsExact(wkt.read("LINESTRING(0 2, 0 3)")));
        showResult("Touch and parallel", ls, clipped);
    }

    @Test
    public void testInsideOut() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-2 8, 12 8, 12 2, -2 2)");
        MultiLineString clipped = (MultiLineString) clipper.clip(ls, false);
        assertTrue(clipped.equalsExact(wkt.read("MULTILINESTRING((0 8, 10 8), (10 2, 0 2))")));
        showResult("Touch border", ls, clipped);
    }

    @Test
    public void testFullyOutsideCircle() throws Exception {
        Point p = (Point) wkt.read("POINT(5 5)");
        LineString ls = ((Polygon) p.buffer(10)).getExteriorRing();
        Geometry clipped = clipper.clip(ls, false);
        assertNull(clipped);
        showResult("Circle around", ls, clipped);
    }

    @Test
    public void testCrossingCircle() throws Exception {
        Point p = (Point) wkt.read("POINT(5 5)");
        LineString ls = ((Polygon) p.buffer(6)).getExteriorRing();
        MultiLineString clipped = (MultiLineString) clipper.clip(ls, false);
        assertEquals(4, clipped.getNumGeometries());
        showResult("Circle around", ls, clipped);
    }

    @Test
    public void testInsidePolygon() throws Exception {
        Geometry g = wkt.read("POINT(5 5)").buffer(2);
        Geometry clipped = clipper.clip(g, false);
        assertTrue(g.equalsExact(clipped));
        showResult("Polygon inside", g, clipped);
    }

    @Test
    public void testOutsidePolygon() throws Exception {
        Geometry g = wkt.read("POINT(5 5)").buffer(10);
        Geometry clipped = clipper.clip(g, false);
        assertTrue(boundsPoly.equalsTopo(clipped));
        showResult("Polygon outside", g, clipped);
    }

    @Test
    public void testPolygonCrossingSide() throws Exception {
        Geometry g = wkt.read("POLYGON((-2 2, 2 2, 2 4, -2 4, -2 2))");
        Geometry clipped = clipper.clip(g, false);
        // assertTrue(clipped.equalsExact(wkt.read("POLYGON((0 2, 2 2, 2 4, 0 4, 0 2))")));
        showResult("Crossing side", g, clipped);
    }

    @Test
    public void testCrossingOtherSide() throws Exception {
        Geometry g = wkt.read("POLYGON((6 2, 12 2, 12 6, 6 6, 6 2))");
        Geometry clipped = clipper.clip(g, false);
        // assertTrue(clipped.equalsExact(wkt.read("POLYGON((0 2, 10 2, 10 10, 0 10, 0 2))")));
        showResult("Donut crossing", g, clipped);
    }

    @Test
    public void testPolygonCrossingTwoSides() throws Exception {
        Geometry g = wkt.read("POLYGON((-2 2, 2 2, 2 12, -2 12, -2 2))");
        Geometry clipped = clipper.clip(g, false);
        assertTrue(clipped.equalsExact(wkt.read("POLYGON((0 2, 2 2, 2 10, 0 10, 0 2))")));
        showResult("Crossing two sides", g, clipped);
    }

    @Test
    public void testPolygonCrossingThreeSides() throws Exception {
        Geometry g = wkt.read("POLYGON((-2 2, 12 2, 12 12, -2 12, -2 2))");
        Geometry clipped = clipper.clip(g, false);
        assertTrue(clipped.equalsExact(wkt.read("POLYGON((0 2, 10 2, 10 10, 0 10, 0 2))")));
        showResult("Crossing three sides", g, clipped);
    }

    @Test
    public void testDonutCrossingInvalid() throws Exception {
        Geometry g = wkt.read("POLYGON((6 2, 14 2, 14 8, 6 8, 6 2), (8 4, 12 4, 12 6, 8 6, 8 4))");
        Geometry clipped = clipper.clip(g, false);
        // System.out.println(clipped);
        assertTrue(
                clipped.equalsExact(
                        wkt.read(
                                "POLYGON ((10 2, 10 8, 6 8, 6 2, 10 2), (10 4, 10 6, 8 6, 8 4, 10 4))")));
        showResult("Donut crossing, invalid geom", g, clipped);
    }

    @Test
    public void testDonutHoleOutside() throws Exception {
        Geometry g =
                wkt.read("POLYGON((6 2, 14 2, 14 8, 6 8, 6 2), (11 4, 12 4, 12 6, 11 6, 11 4))");
        Geometry clipped = clipper.clip(g, false);
        // System.out.println(clipped);
        assertTrue(clipped.equalsExact(wkt.read("POLYGON ((10 2, 10 8, 6 8, 6 2, 10 2))")));
        showResult("Donut crossing, invalid geom", g, clipped);
    }

    @Test
    public void testDonutCrossingValid() throws Exception {
        Geometry g = wkt.read("POLYGON((6 2, 14 2, 14 8, 6 8, 6 2), (8 4, 12 4, 12 6, 8 6, 8 4))");
        Geometry clipped = clipper.clip(g, true);
        assertTrue(
                clipped.equalsExact(
                        wkt.read("POLYGON ((10 2, 6 2, 6 8, 10 8, 10 6, 8 6, 8 4, 10 4, 10 2))")));
        showResult("Donut crossing, valid geom", g, clipped);
    }

    @Test
    public void testFullyOutsideAround() throws Exception {
        Geometry g = wkt.read("POLYGON((0 -10, 20 -10, 20 10, 30 10, 30 -20, 0 -20, 0 -10))");
        Geometry clipped = clipper.clip(g, false);
        assertNull(clipped);
        showResult("Fully outside around", g, clipped);
    }

    @Test
    public void testGeotXYWZ() throws Exception {
        clipper = new GeometryClipper(new Envelope(-11, 761, -11, 611));
        Geometry g =
                wkt.read(
                        "POLYGON((367 -13, 459 105, 653 -42, 611 -96, 562 -60, 514 -124, 367 -13))");
        System.out.println(g.getNumPoints());
        Geometry clipped = clipper.clip(g, false);
        assertNotNull(clipped);
        assertTrue(!clipped.isEmpty());
        //        System.out.println(clipped);
    }

    /** Test invalid polygon (from OSM). */
    @Test
    public void invalidPolygonTest() throws Exception {
        // this is an invalid polygon - see GEOT-5322 for pic and details.  This is in screen
        // coordinates.
        String invalidPolygonWKT =
                "POLYGON ((737.3223167999995 300.6115043555553, 737.9333944888895 298.8183210666666, 734.6862222222226 300.85357795555547, 731.4519751111111 299.20279893333327, 734.2018474666665 300.0128511999999, 738.8875121777774 297.05722311111094, 739.8749348977781 295.00220529777766, 738.6494663111116 293.04009386666667, 739.8239715555555 293.0595726222223, 740.1680810666667 291.096314311111, 742.0000398222228 290.9779854222222, 742.3454691555562 289.6243484444444, 749.0932650666673 289.5820686222221, 748.3574869333343 288.732512711111, 749.4635434666661 287.48391537777775, 753.4318984533338 287.0207533511109, 752.7391328711119 288.27452984888896, 755.191544604445 287.73796295111106, 755.5325548088895 289.0415468088888, 752.2433166222227 290.37469013333316, 755.0832554666667 290.3079708444443, 757.3606769777789 287.83266702222227, 757.0469688888888 288.9282559999999, 758.1241258666669 288.4486144, 757.7301361777781 289.3541944888889, 759.019374933333 289.7385813333333, 762.2620871111121 286.344590222222, 764.3369841777785 286.08804408888864, 763.6562289777785 291.4026495999999, 761.9839596088887 292.3739113244444, 762.433195235556 293.2922436266665, 764.491995022222 292.8312888888888, 764.9500643555557 293.57284693333327, 763.280534755555 294.41848888888876, 764.0451669333333 293.9469937777776, 764.4738816000008 296.85506275555554, 768.546261333333 302.01597724444446, 775.6844515555549 302.2602808888887, 776.6106026666666 303.69028551111114, 783.5582378666668 304.27941319111096, 783.2538595555561 303.19871999999987, 787.7554090666663 298.98134186666675, 794.2554880000007 299.71247786666663, 797.6559416888895 304.5824398222221, 797.6135708444444 308.9094997333334, 795.8814179555557 311.65627733333326, 792.7038776888885 310.7532003555557, 793.5391431111111 312.6568391111109, 791.1233678222225 313.21184711111096, 790.9637603555566 314.31990613333346, 786.6621866666674 313.7359530666665, 787.1284206933333 317.79396494222215, 790.782462293334 318.9490005333332, 795.4531629511121 317.41458432000013, 798.5147044977784 319.2014870755554, 805.0597802666662 317.9516472888886, 800.2897607111117 324.3483249777778, 804.9756302222222 324.0079928888888, 805.5803818666664 324.53246293333314, 802.7551886222227 326.1091043555555, 805.4876757333341 327.1061617777775, 806.7679487999994 325.7249905777778, 808.28506168889 327.3052273777778, 809.5941888000007 325.7510684444442, 810.282407822222 327.42182684444447, 806.2690104888898 327.7403135999998, 804.4354133333336 330.52595768888864, 806.1467221333342 332.1945315555556, 813.3500842666672 333.1780266666665, 810.6970140444437 333.05601137777785, 809.905211733334 334.4197063111112, 808.371214222223 333.6803783111111, 808.8412529777779 335.16872817777767, 810.1757752888889 335.1138417777777, 806.837216711112 337.65090417777765, 809.8295722666662 338.02354915555566, 806.5739349333335 339.94439111111114, 804.4348672000006 339.03671751111096, 804.6997418666679 340.86749297777783, 803.7017241599997 339.6999008711109, 804.2705720888889 341.68928711111107, 802.6454158222232 342.7817358222221, 802.9273571555559 343.89794133333316, 799.3251982222218 344.9686812444445, 798.3326008888898 348.2100736, 795.3077504000012 350.186894222222, 794.888866133334 352.7691946666664, 796.4690574222222 351.98330880000003, 796.974126080001 352.8888888888889, 743.1952545209415 352.8888888888889, 742.6992861866665 351.8572566755556, 744.536829155556 352.44734008888895, 743.2014876444455 351.15236693333327, 748.4974017422219 346.7928530488887, 739.6970365155557 348.1309798399998, 738.9715939555554 347.24050488888884, 745.8135614577777 343.2114107733332, 746.3448035555557 339.3551587555553, 745.0365866666671 337.3208120888887, 742.4229745777775 336.97228799999993, 742.2775210666668 335.5295857777776, 738.4572728888897 333.7384049777779, 738.0062122666668 330.3192007111111, 736.4435427555554 330.04149191111105, 737.7227235555556 329.5907043555553, 737.7972252444442 327.7324401777778, 735.618653866667 323.65628302222194, 731.5624760888895 323.445248, 729.6628104533329 320.88825173333316, 724.8495416888891 325.83817671111115, 728.683716266667 332.1454705777778, 723.9284423111112 335.5659491555555, 726.6814094222227 337.2705678222221, 732.4142620444445 335.68136533333313, 735.8202225777777 340.1564273777776, 734.3186289777777 344.93486648888893, 730.2431089777783 345.9412992, 729.0137173333333 347.83460693333336, 726.1086065777772 348.7445105777779, 724.168240355556 344.3480007111111, 727.6633571555558 342.8152319999999, 727.2347790222229 340.0118840888888, 722.2705180444445 339.46711608888904, 718.4444444444443 340.97975181660604, 718.4444444444443 309.80668544203286, 720.1516117333331 309.5667256888887, 720.5101027555556 307.5459413333333, 725.4580252444448 305.685447111111, 725.9099050666673 304.59764053333333, 723.7044366222226 305.26751857777776, 724.6772366222231 304.3053681777776, 722.432583111111 303.65232924444445, 725.1405397333338 303.0301013333335, 727.9014257777781 299.77810488888895, 729.228893866667 300.5921621333332, 729.0182684444453 302.0866560000002, 730.9589532444452 302.3723747555555, 729.6039736888888 303.0319308799999, 731.2260579555559 303.69602446222234, 736.3402507377778 302.94866375111087, 737.3223167999995 300.6115043555553))";
        Geometry g = wkt.read(invalidPolygonWKT);

        clipper = new GeometryClipper(new Envelope(-12, 780.0, -12, 396.0));

        Geometry result = clipper.clipSafe(g, true, 1); // mimic streaming renderer
        assertTrue(!result.isEmpty());
        assertTrue(result.getArea() > 0);
    }

    public void showResult(String title, Geometry original, Geometry clipped) throws Exception {
        final String headless = System.getProperty("java.awt.headless", "false");
        if (headless.equalsIgnoreCase("true") || !TestData.isInteractiveTest()) {
            return;
        }

        BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D gr = image.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, image.getWidth(), image.getHeight());
        gr.setColor(Color.LIGHT_GRAY);

        gr.setStroke(new BasicStroke(1, 0, 0, 1, new float[] {5, 5}, 0));
        gr.draw(new Line2D.Double(0, 300, 600, 300));
        gr.draw(new Line2D.Double(300, 0, 300, 600));

        AffineTransform at = new AffineTransform();
        at.translate(300, 300);
        at.scale(10, -10);

        gr.setStroke(new BasicStroke(1));
        gr.setColor(Color.LIGHT_GRAY);
        gr.draw(new LiteShape(boundsPoly, at, false));
        gr.setStroke(new BasicStroke(0.5f));
        gr.setColor(Color.BLUE);
        gr.draw(new LiteShape(original, at, false));
        if (clipped != null) {
            gr.setStroke(new BasicStroke(2));
            if (clipped instanceof Polygon || clipped instanceof MultiPolygon) {
                gr.setColor(Color.LIGHT_GRAY);
                gr.fill(new LiteShape(clipped, at, false));
                gr.setColor(Color.BLUE);
            }
            gr.draw(new LiteShape(clipped, at, false));
        }

        gr.dispose();

        JFrame frame = new JFrame(title);
        frame.setContentPane(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);

        Thread.sleep(3000);
        frame.setVisible(false);
    }
}
