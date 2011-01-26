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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import org.geotools.geometry.jts.LiteShape;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.ShapeUtilities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

/**
 * A custom renderer for geometry objects in a {@link GeometryDataset}.
 * <pre>
 *   Geometry g1 = ...
 *   Geometry g2 = ...
 *   
 *   GeometryDataset data = new GeometryDataset(g1,g2);
 *   GeometryRenderer renderer = new GeometryRenderer();
 *   
 *   XYPlot plot = new XYPlot(dataset, dataset.getDomain(), dataset.getRange(), renderer);
 *   ...
 * </pre>
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class GeometryRenderer extends AbstractXYItemRenderer {

    /**
     * flag controlling whether geometry coordinates should be drawn on top of lines
     */
    boolean renderCoordinates = true;
    
    /**
     * flag controlling whether to fill coordinates
     */
    boolean fillCoordinates = false;
    
    /**
     * flag controlling whether polygons are filled
     */
    boolean fillPolygons;
    
    /**
     * polygon fill opacity
     */
    float polygonFillOpacity = 0.2f;
    
    public GeometryRenderer() {
        setStrokeWidth(2f);
        
        setAutoPopulateSeriesShape(false);
        setBaseShape(new Ellipse2D.Float(-3, -3, 6, 6));
    }
    
    @Override
    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, XYPlot plot,
            XYDataset data, PlotRenderingInfo info) {
        
        return super.initialise(g2, dataArea, plot, data, info);
    }
    
    public void setRenderCoordinates(boolean renderCoordinates) {
        this.renderCoordinates = renderCoordinates;
    }
    
    public boolean isRenderCoordinates() {
        return renderCoordinates;
    }
    
    public void setFillCoordinates(boolean fillCoordinates) {
        this.fillCoordinates = fillCoordinates;
    }
    
    public boolean isFillCoordinates() {
        return fillCoordinates;
    }
    
    public void setFillPolygons(boolean fillPolygons) {
        this.fillPolygons = fillPolygons;
    }
    
    public boolean isFillPolygons() {
        return fillPolygons;
    }
    
    public void setPolygonFillOpacity(float polygonFillOpacity) {
        this.polygonFillOpacity = Math.max(0f, Math.min(polygonFillOpacity, 1f));
    }
    
    public float getPolygonFillOpacity() {
        return polygonFillOpacity;
    }
    
    public void setStrokeWidth(float width) {
        setBaseStroke(new BasicStroke(width));
    }
    
    public void setLegend(boolean legend) {
        setBaseSeriesVisibleInLegend(legend);
    }

    @Override
    public int getPassCount() {
        return renderCoordinates ? 2 : 1;
    }
    
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, 
        PlotRenderingInfo plotInfo, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, 
        XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        
        GeometryDataset gd = (GeometryDataset) dataset;
        Geometry g = gd.getGeometries().get(series);
        
        g2.setPaint(getItemPaint(series, item));
        g2.setStroke(getItemStroke(series, item));
        
        if (pass == 0) {
            drawGeometry(g, g2, series, item, dataArea, plot, domainAxis, rangeAxis);
        }
        
        if (pass == 1 && renderCoordinates) {
            // second pass to render coordinates 
            if (g instanceof Point || g instanceof MultiPoint) {
                //already rendered in pass 0 
                return;
            }
            
            for (Coordinate c : g.getCoordinates()) {
                drawCoordinate(c, g2, series, item, dataArea, plot, domainAxis, rangeAxis);
            }
        }
       
    }
    
    void drawGeometry(Geometry g, Graphics2D g2, int series, int item, Rectangle2D dataArea, 
            XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis) {
        
        if (g instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) g;
            for (int i = 0; i < gc.getNumGeometries(); i++) {
                drawGeometry(
                    gc.getGeometryN(i), g2, series, item, dataArea, plot, domainAxis, rangeAxis);
            }
        }
        else if (g instanceof Point) {
            drawCoordinate(((Point)g).getCoordinate(), g2, series, item, dataArea, plot, domainAxis, 
                rangeAxis);
        }
        else if (g instanceof LineString) {
            g2.draw(new TranslatedLiteShape(g, dataArea, plot, domainAxis, rangeAxis));
        }
        else {
            
            if (fillPolygons) {
                Paint p = getSeriesPaint(series);
                if (p instanceof Color) {
                    Color c = (Color) p;
                    p = new Color(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, polygonFillOpacity);
                }
                g2.setPaint(p);
                g2.fill(new TranslatedLiteShape(g, dataArea, plot, domainAxis, rangeAxis));
                
            }
            g2.setPaint(getSeriesPaint(series));
            g2.draw(new TranslatedLiteShape(g, dataArea, plot, domainAxis, rangeAxis));
        }
    }
    
    void drawCoordinate(Coordinate c, Graphics2D g2, int series, int item, Rectangle2D dataArea, 
        XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis) {
        
        double tx = domainAxis.valueToJava2D(c.x, dataArea, plot.getDomainAxisEdge());
        double ty = rangeAxis.valueToJava2D(c.y, dataArea, plot.getRangeAxisEdge());
        
        Shape shape = getItemShape(series, item);
        shape = ShapeUtilities.createTranslatedShape(shape, tx, ty);
        
        if (fillCoordinates) {
            g2.fill(shape);
        }
        else {
            g2.draw(shape);
        }
    }
    
    @Override
    public LegendItem getLegendItem(int datasetIndex, int series) {
        LegendItem li = super.getLegendItem(datasetIndex, series);

        Stroke outlineStroke = getItemStroke(series, 0);
        Paint fillPaint = li.getFillPaint();
        Paint outlinePaint = fillPaint;
        
        if (!fillCoordinates) {
            fillPaint = new Color(255, 255, 255, 255);
        }
        return new LegendItem(li.getLabel(), li.getDescription(), li.getToolTipText(), 
            li.getURLText(), li.getShape(), fillPaint, outlineStroke, outlinePaint);
    }
    
    static class TranslatedLiteShape extends LiteShape {

        
        Rectangle2D dataArea;
        XYPlot plot;
        ValueAxis domainAxis, rangeAxis;
        
        public TranslatedLiteShape(Geometry geom, Rectangle2D dataArea, XYPlot plot, 
            ValueAxis domainAxis, ValueAxis rangeAxis) {
            
            super(geom, new AffineTransform(), false);
            this.dataArea = dataArea;
            this.plot = plot;
            this.domainAxis = domainAxis;
            this.rangeAxis = rangeAxis;
        }
        
        @Override
        public PathIterator getPathIterator(AffineTransform at) {
            return new TranslatedIterator(super.getPathIterator(at));
        }
        
        class TranslatedIterator implements PathIterator {

            PathIterator delegate;
            
            public TranslatedIterator(PathIterator delegate) {
                this.delegate = delegate;
            }
            
            public int currentSegment(float[] coords) {
                int i = delegate.currentSegment(coords);
                coords[0] = (float) domainAxis.valueToJava2D(coords[0], dataArea, plot.getDomainAxisEdge());
                coords[1] = (float) rangeAxis.valueToJava2D(coords[1], dataArea, plot.getRangeAxisEdge());
                return i;
            }

            public int currentSegment(double[] coords) {
                int i = delegate.currentSegment(coords);
                coords[0] = domainAxis.valueToJava2D(coords[0], dataArea, plot.getDomainAxisEdge());
                coords[1] = rangeAxis.valueToJava2D(coords[1], dataArea, plot.getRangeAxisEdge());
                return i;
            }

            public int getWindingRule() {
                return delegate.getWindingRule();
            }

            public boolean isDone() {
                return delegate.isDone();
            }

            public void next() {
                delegate.next();
            }
            
        }
        
    }

}
