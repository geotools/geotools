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
package org.geotools.geometry.jts;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class OffsetCurveBuilderTest {

    static final double EPS = 2e-1;

    static final boolean INTERACTIVE =
            true; // Boolean.getBoolean("org.geotools.image.test.interactive");

    static final boolean INTERACTIVE_ON_SUCCESS =
            Boolean.getBoolean("org.geotools.image.test.interactive.on.success");

    Geometry curve;

    Geometry offsetCurve;

    @Rule
    public TestWatcher interactiveReporter =
            new TestWatcher() {

                protected void succeeded(org.junit.runner.Description description) {
                    if (curve != null && INTERACTIVE_ON_SUCCESS) {
                        displayCurves(false);
                    }
                };

                @Override
                protected void failed(Throwable e, org.junit.runner.Description description) {
                    if (curve != null) {
                        // System.out.println("Original geometry: " + curve);
                        // System.out.println("Offset geometry: " + offsetCurve);
                    }
                    if (curve != null && INTERACTIVE) {
                        displayCurves(true);
                    }
                }

                private void displayCurves(boolean failed) {
                    BufferedImage image = drawCurves();
                    ImageDisplay dialog = new ImageDisplay(image, failed ? "Failure" : "Success");
                    dialog.setModal(true);
                    dialog.setVisible(true);
                }

                private BufferedImage drawCurves() {
                    final int SIZE = 400;
                    BufferedImage bi = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_3BYTE_BGR);
                    Envelope envelope = curve.getEnvelopeInternal();
                    if (offsetCurve != null) {
                        envelope.expandToInclude(offsetCurve.getEnvelopeInternal());
                    }
                    if (envelope.getWidth() == 0) {
                        envelope.expandBy(envelope.getHeight(), 0);
                    }
                    if (envelope.getHeight() == 0) {
                        envelope.expandBy(0, envelope.getWidth());
                    }
                    envelope.expandBy(envelope.getWidth() * 0.1, envelope.getHeight() * 0.1);

                    double scale = SIZE / Math.max(envelope.getWidth(), envelope.getHeight());

                    double tx = -envelope.getMinX() * scale;
                    double ty = envelope.getMinY() * scale + SIZE;

                    AffineTransform at = new AffineTransform(scale, 0.0d, 0.0d, -scale, tx, ty);
                    Graphics2D graphics = bi.createGraphics();
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(0, 0, SIZE, SIZE);
                    graphics.setColor(Color.BLACK);
                    graphics.setStroke(new BasicStroke(4));
                    graphics.draw(new LiteShape(curve, at, false));
                    graphics.setColor(Color.RED);
                    graphics.setStroke(
                            new BasicStroke(
                                    4,
                                    BasicStroke.CAP_SQUARE,
                                    BasicStroke.JOIN_ROUND,
                                    1,
                                    new float[] {8, 8},
                                    0));
                    graphics.draw(new LiteShape(offsetCurve, at, false));
                    graphics.dispose();

                    return bi;
                };
            };

    class ImageDisplay extends JDialog {
        private static final long serialVersionUID = -8640087805737551918L;

        boolean accept = false;

        public ImageDisplay(RenderedImage image, String title) {
            JPanel content = new JPanel(new BorderLayout());
            this.setContentPane(content);
            this.setTitle(title);
            final JLabel topLabel =
                    new JLabel(
                            "<html><body>"
                                    + "The curve (black) and its offset (red) "
                                    + "</html></body>");
            topLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
            content.add(topLabel, BorderLayout.NORTH);

            ScrollingImagePanel imageViewer =
                    new ScrollingImagePanel(
                            image,
                            Math.min(400, image.getWidth()) + 100,
                            Math.min(400, image.getHeight()) + 100);
            content.add(imageViewer);

            JButton close = new JButton("Close");
            close.addActionListener(
                    new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            ImageDisplay.this.setVisible(false);
                        }
                    });
            content.add(close, BorderLayout.SOUTH);
            pack();
        }
    }

    Geometry offset(Geometry geometry, double offset) {
        this.curve = geometry;
        this.offsetCurve = new OffsetCurveBuilder(offset).offset(curve);
        return offsetCurve;
    }

    Geometry geometry(String wkt) throws ParseException {
        return new WKTReader().read(wkt);
    }

    @Test
    public void nullSafe() {
        Geometry offset = new OffsetCurveBuilder(10).offset(null);
        assertNull(offset);
    }

    @Test
    public void testHorizontalSegmentPositiveOffset() throws ParseException {
        Geometry offset = simpleOffsetTest("LINESTRING(0 0, 10 0)", 2);
        assertTrue(offset.getEnvelopeInternal().getMinY() == 2);
    }

    @Test
    public void testHorizontalSegmentNegativeOffset() throws ParseException {
        Geometry offset = simpleOffsetTest("LINESTRING(0 0, 10 0)", -2);
        assertTrue(offset.getEnvelopeInternal().getMinY() == -2);
    }

    @Test
    public void testDiagonalSegmentPositiveOffset() throws ParseException {
        simpleOffsetTest("LINESTRING(0 0, 10 10)", 2);
    }

    @Test
    public void testAllDiagonalsLeft() throws Exception {
        double offset = 2;
        testAllDiagonals(offset);
    }

    @Test
    public void testAllDiagonalsRight() throws Exception {
        double offset = -2;
        testAllDiagonals(offset);
    }

    private void testAllDiagonals(double offset) {
        double[] ordinates = new double[4];
        ordinates[0] = 0;
        ordinates[1] = 0;
        for (int i = 0; i < 360; i++) {
            double angle = toRadians(i);
            ordinates[2] = 10 * cos(angle);
            ordinates[3] = 10 * sin(angle);
            curve =
                    new GeometryFactory()
                            .createLineString(
                                    new PackedCoordinateSequenceFactory().create(ordinates, 2, 0));
            simpleOffsetTest(curve, offset);
        }
    }

    @Test
    public void testLShapedInternal() throws ParseException {
        simpleOffsetTest("LINESTRING(0 10, 0 0, 10 0)", 2);
    }

    @Test
    public void testLShapedExternal() throws ParseException {
        simpleOffsetTest("LINESTRING(0 10, 0 0, 10 0)", 2);
    }

    @Test
    public void testUShapedInternal() throws ParseException {
        simpleOffsetTest("LINESTRING(0 10, 0 0, 10 0, 10 10)", 2);
    }

    @Test
    public void testUShapedExternal() throws ParseException {
        simpleOffsetTest("LINESTRING(0 10, 0 0, 10 0, 10 10)", -2);
    }

    @Test
    public void testClockArmsRight() throws ParseException {
        for (int i = 30; i < 360; i++) {
            testClockArms(-2, i);
        }
    }

    @Test
    public void testClockArmsLeft() throws ParseException {
        for (int i = 1; i < 330; i++) {
            testClockArms(2, i);
        }
    }

    private void testClockArms(double offset, int a) {
        double angle = toRadians(a);

        double[] ordinates = new double[6];
        ordinates[0] = 10;
        ordinates[1] = 0;
        ordinates[2] = 0;
        ordinates[3] = 0;
        ordinates[4] = 10 * cos(angle);
        ordinates[5] = 10 * sin(angle);
        curve =
                new GeometryFactory()
                        .createLineString(
                                new PackedCoordinateSequenceFactory().create(ordinates, 2, 0));
        simpleOffsetTest(curve, offset);
    }

    @Test
    public void testTriangleOuter() throws Exception {
        Geometry offset = simpleOffsetTest("LINEARRING(0 10, 0 0, 10 0, 0 10)", -2);
        assertThat(offset, instanceOf(LinearRing.class));
    }

    @Test
    public void testTriangleInner() throws Exception {
        Geometry offset = simpleOffsetTest("LINEARRING(0 10, 0 0, 10 0, 0 10)", 2);
        assertThat(offset, instanceOf(LinearRing.class));
    }

    @Test
    public void testSimpleLoopGenerator() throws Exception {
        simpleOffsetTest("LINESTRING(0 0, 5 0, 5 -1, 7 -1, 7 0,  10 0)", 2);
    }

    @Test
    public void testElongatedLoopGenerator() throws Exception {
        Geometry geom = geometry("LINESTRING(0 0, 5 0, 5 -10, 7 -10, 7 0,  10 0)");
        Geometry offset = offset(geom, 1.5);
        assertTrue(offset.isValid());
        assertTrue(offset.getLength() > 0);
        // this one "fails", but the output cannot be really called wrong anymore, if we are trying
        // to
        // offset a road at least
        Geometry expected =
                geometry(
                        "LINESTRING (0 1.5, 5 1.5, 5.260472266500395 1.477211629518312, 5.513030214988503 1.4095389311788626, 5.75 1.299038105676658, 5.964181414529809 1.149066664678467, 6.149066664678467 0.9641814145298091, 6.299038105676658 0.7500000000000002, 6.409538931178862 0.5130302149885032, 6.477211629518312 0.2604722665003956, 6.5 0.0000000000000001, 6.5 -8.5, 5.5 -8.5, 5.5 0.0000000000000001, 5.522788370481688 0.2604722665003956, 5.590461068821138 0.5130302149885032, 5.700961894323342 0.7499999999999998, 5.850933335321533 0.9641814145298091, 6.035818585470191 1.149066664678467, 6.25 1.299038105676658, 6.486969785011497 1.4095389311788624, 6.739527733499605 1.477211629518312, 7 1.5, 10 1.5)");
        assertTrue(expected.equalsExact(offset, 0.1));
    }

    @Test
    public void testElongatedNonLoopGenerator() throws Exception {
        simpleOffsetTest("LINESTRING(0 0, 4 0, 4 -10, 9 -10, 9 0,  10 0)", 2);
    }

    @Test
    public void testSelfIntersectLeft() throws Exception {
        Geometry geom = geometry("LINESTRING(0 0, 10 0, 10 -10, 3 -10, 3 3)");
        Geometry offset = offset(geom, 2);
        assertTrue(offset.isValid());
        assertTrue(offset.getLength() > 0);
        // the offset line intersects the original one, because it's also self intersecting, so we
        // cannot have this test
        // assertEquals(2, offset.distance(geom), EPS);
        Geometry expected =
                geometry(
                        "LINESTRING (0 2, 10 2, 10.34729635533386 1.969615506024416, 10.684040286651337 1.8793852415718169, 11 1.7320508075688774, 11.28557521937308 1.532088886237956, 11.532088886237956 1.2855752193730787, 11.732050807568877 1.0000000000000002, 11.879385241571816 0.6840402866513376, 11.969615506024416 0.3472963553338608, 12 0.0000000000000001, 12 -10, 11.969615506024416 -10.34729635533386, 11.879385241571818 -10.684040286651337, 11.732050807568877 -11, 11.532088886237956 -11.28557521937308, 11.28557521937308 -11.532088886237956, 11 -11.732050807568877, 10.684040286651339 -11.879385241571816, 10.34729635533386 -11.969615506024416, 10 -12, 2.9999999999999996 -12, 2.6527036446661394 -11.969615506024416, 2.3159597133486622 -11.879385241571816, 2 -11.732050807568877, 1.714424780626921 -11.532088886237956, 1.467911113762044 -11.28557521937308, 1.2679491924311228 -11, 1.1206147584281831 -10.684040286651337, 1.030384493975584 -10.34729635533386, 1 -10, 1 3)");
        assertTrue(expected.equalsExact(offset, 0.1));
    }

    @Test
    public void testSelfIntersectRight() throws Exception {
        Geometry geom = geometry("LINESTRING(0 0, 10 0, 10 -10, 3 -10, 3 3)");
        Geometry offset = offset(geom, -1);
        assertTrue(offset.isValid());
        assertTrue(offset.getLength() > 0);
        // the offset line intersects the original one, because it's also self intersecting, so we
        // cannot have this test
        // assertEquals(2, offset.distance(geom), EPS);
        assertEquals(geometry("LINESTRING (0 -1, 9 -1, 9 -9, 4 -9, 4 3)"), offset);
    }

    @Test
    public void testLoopRoad1() throws Exception {
        String wkt = "LINESTRING (13 470, 0 270, 24 251, 67 264)";
        simpleOffsetTest(wkt, 50);
    }

    @Test
    public void testLoopRoad2() throws Exception {
        String wkt = "LINESTRING (13 470, 0 270, 24 251, 67 264, 108 279)";
        simpleOffsetTest(wkt, 50);
    }

    @Test
    public void testLoopRoad3() throws Exception {
        String wkt = "LINESTRING (67 264, 108 279, 134 279, 143 262, 135 0)";
        simpleOffsetTest(wkt, -50);
    }

    @Test
    public void testSpike() throws Exception {
        String wkt = "LINESTRING (20 0, 0 67, 9 138, 8 134)";
        simpleOffsetTest(wkt, 50);
        simpleOffsetTest(wkt, -50);
        simpleOffsetTest(wkt, 100);
        simpleOffsetTest(wkt, -100);
    }

    @Test
    public void testInwardsSpike() throws Exception {
        String wkt =
                "LINESTRING (594005.44863915 4920198.37095076, 594015.86677618 4920163.01783546, 594006.98015873 4920062.97719912, 593985.83866536 4920003.77506624, 593948.20205744 4919978.69410905, 593949.41233583 4919981.13611074)";
        simpleOffsetTest(wkt, 50);
        simpleOffsetTest(wkt, -50);
        simpleOffsetTest(wkt, 100);
        simpleOffsetTest(wkt, -100);
    }

    private Geometry simpleOffsetTest(String wkt, final double offsetDistance)
            throws ParseException {
        Geometry geom = geometry(wkt);
        return simpleOffsetTest(geom, offsetDistance);
    }

    private Geometry simpleOffsetTest(Geometry geom, final double offsetDistance) {
        Geometry offset = offset(geom, offsetDistance);
        assertTrue(offset.isValid());
        assertTrue(offset.getLength() > 0);
        assertEquals(abs(offsetDistance), offset.distance(geom), EPS * abs(offsetDistance));
        offset.apply(
                new GeometryComponentFilter() {

                    @Override
                    public void filter(Geometry geom) {
                        if (geom instanceof LineString) {
                            LineString ls = (LineString) geom;
                            CoordinateSequence cs = ls.getCoordinateSequence();
                            if (cs.size() < 2) {
                                return;
                            }
                            double px = cs.getOrdinate(0, 0);
                            double py = cs.getOrdinate(0, 1);
                            for (int i = 1; i < cs.size(); i++) {
                                double cx = cs.getOrdinate(i, 0);
                                double cy = cs.getOrdinate(i, 1);
                                if (cx == px && cy == py) {
                                    fail(
                                            "Found two subsequent ordinates with the same value: "
                                                    + cx
                                                    + ", "
                                                    + py);
                                }
                                px = cx;
                                py = cy;
                            }
                        }
                    }
                });
        return offset;
    }

    public static void main(String[] args) throws ParseException {
        // simple utility to take a random WKT and make it into something looking like test data
        // (simplify large coordinates, excess precision)
        final double tolerance = 1;
        String wkt =
                "LINESTRING (809 2365, 796 2165, 820 2146, 863 2159, 904 2174, 930 2174, 939 2157, 931 1895)";
        Geometry geom = new WKTReader().read(wkt);
        Envelope envelope = geom.getEnvelopeInternal();
        final double minx = envelope.getMinX();
        final double miny = envelope.getMinY();
        geom.apply(
                new CoordinateSequenceFilter() {

                    @Override
                    public boolean isGeometryChanged() {
                        return true;
                    }

                    @Override
                    public boolean isDone() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public void filter(CoordinateSequence seq, int i) {
                        double x = seq.getOrdinate(i, 0);
                        double y = seq.getOrdinate(i, 1);
                        x -= minx;
                        y -= miny;
                        x = Math.round(x / tolerance) * tolerance;
                        y = Math.round(y / tolerance) * tolerance;
                        seq.setOrdinate(i, 0, x);
                        seq.setOrdinate(i, 1, y);
                    }
                });

        // System.out.println(geom.toText());
    }
}
