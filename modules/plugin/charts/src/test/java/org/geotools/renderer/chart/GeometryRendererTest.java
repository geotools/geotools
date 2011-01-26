/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.chart;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import junit.framework.TestCase;

import org.geotools.test.TestData;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryRendererTest extends TestCase {

    WKTReader wkt = new WKTReader();
    
    public void testPoint() throws Exception {
        render(wkt.read("POINT(6 10)"));
    }
    
    public void testLineString() throws Exception {
        render(wkt.read("LINESTRING(3 4,10 50,20 25)"));
    }
    
    public void testPolygon() throws Exception {
        render(wkt.read("POLYGON((1 1,5 1,5 5,1 5,1 1))"));
    }
    
    public void testPolygonWithHole() throws Exception {
        render(wkt.read("POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2))"));
    }
    
    public void testMultiPoint() throws Exception {
        render(wkt.read("MULTIPOINT((3.5 5.6),(4.8 10.5))"));
    }
    
    public void testMultiLineString() throws Exception {
        render(wkt.read("MULTILINESTRING((3 4,10 50,20 25),(-5 -8,-10 -8,-15 -4))"));
    }
    
    public void testMultiPolygon() throws Exception {
        render(wkt.read(
            "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))"));
    }
    
    public void testMulipleGeometries() throws Exception {
        render(wkt.read("POINT(6 10)"), wkt.read("LINESTRING(3 4,10 50,20 25)"), 
            wkt.read("POLYGON((1 1,5 1,5 5,1 5,1 1))"));
    }
    
    void render(Geometry... geoms) throws Exception {
        GeometryDataset dataset = new GeometryDataset(geoms);
        GeometryRenderer r = new GeometryRenderer();
        
        XYPlot plot = new XYPlot(dataset, dataset.getDomain(), dataset.getRange(), r);
        showChart(plot);
    }
    
    void showChart(XYPlot plot) throws Exception {
        JFreeChart chart = new JFreeChart(plot);
        chart.setAntiAlias(true);
        ChartPanel panel = new ChartPanel(chart, true);
        
        final String headless = System.getProperty("java.awt.headless", "false");
        if (!headless.equalsIgnoreCase("true") && TestData.isInteractiveTest()) {
            try {
                JFrame frame = new JFrame(getName());
                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        e.getWindow().dispose();
                    }
                });
                frame.setContentPane(panel);
                frame.setSize(new Dimension(500,500));
                frame.setVisible(true);

                Thread.sleep(5000);
                frame.dispose();
            } catch (HeadlessException exception) {
                // The test is running on a machine without X11 display. Ignore.
                return;
            }
        }
    }
}
