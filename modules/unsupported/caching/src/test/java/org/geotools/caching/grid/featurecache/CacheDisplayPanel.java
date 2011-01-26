/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.grid.featurecache;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.geotools.caching.spatialindex.Data;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.Visitor;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;


public class CacheDisplayPanel extends JPanel {
    GridFeatureCache cache;
    HashMap<String, Envelope> queries = new HashMap<String, Envelope>();

    CacheDisplayPanel(GridFeatureCache cache) {
        this.cache = cache;
    }

    void setCurrentQuery(String worker, Envelope query) {
        queries.put(worker, query);
    }

    void removeWorker(String worker) {
        queries.remove(worker);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.clearRect(0, 0, g2d.getClipBounds().width, g2d.getClipBounds().height);

        AffineTransform saveAT = g2d.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.setToTranslation(5, g2d.getClipBounds().height - 5);
        trans.scale(1, -1);

        int width = g2d.getClipBounds().width;
        int height = g2d.getClipBounds().height;
        double scaleFactor = Math.min(width, height);
        trans.scale(scaleFactor, scaleFactor);
        g2d.transform(trans);
        g2d.setStroke(new BasicStroke(0));

        DrawingVisitor v = new DrawingVisitor(g2d);

        cache.readLock();

        try {
            cache.tracker.setDoRecordAccess(false);
            cache.tracker.intersectionQuery(new Region(new double[] { 0, 0 }, new double[] { 1, 1 }),
                v);
        } finally {
            cache.tracker.setDoRecordAccess(true);
            cache.readUnLock();
        }

        if (!queries.isEmpty()) {
            for (Iterator<Entry<String, Envelope>> it = queries.entrySet().iterator();
                    it.hasNext();) {
                Entry<String, Envelope> next = it.next();
                Envelope query = next.getValue();
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(0.005f));

                Rectangle2D.Double window = new Rectangle2D.Double(query.getMinX(),
                        query.getMinY(), query.getWidth(), query.getHeight());
                g2d.draw(window);
                g2d.setTransform(saveAT);

                Point2D.Double p = new Point2D.Double(window.x + (0.5 * window.width),
                        window.y + (0.5 * window.height));
                Point2D pt = trans.transform(p, new Point2D.Double(0, 0));
                g2d.drawString(next.getKey(), (int) pt.getX(), (int) pt.getY());
                g2d.setTransform(trans);
            }
        }

        g2d.setTransform(saveAT);
        g2d.dispose();
    }
}


class DrawingVisitor implements Visitor {
    final static Stroke VALID_STROKE = new BasicStroke(0.005f);
    final static Stroke INVALID_STROKE = new BasicStroke(0.002f);
    final static Stroke DATA_STROKE = new BasicStroke(0.002f);
    Graphics2D graph;

    DrawingVisitor(Graphics2D graph) {
        this.graph = graph;
    }

    public boolean isDataVisitor() {
        return true;
    }

    public void visitData(Data d) {
        graph.setColor(Color.BLACK);
        graph.setStroke(DATA_STROKE);

        SimpleFeature f = (SimpleFeature) d.getData();
        LineString geom = (LineString) f.getDefaultGeometry();
        Coordinate start = geom.getCoordinateN(0);
        Coordinate current;

        for (int i = 1; i < geom.getNumPoints(); i++) {
            current = geom.getCoordinateN(i);
            graph.draw(new Line2D.Double(start.x, start.y, current.x, current.y));
            start = current;
        }
    }

    public void visitNode(Node n) {
        Region region = (Region) n.getShape();

        if (n.getIdentifier().isValid()) {
            graph.setStroke(VALID_STROKE);
            graph.setColor(Color.GREEN);
        } else {
            graph.setStroke(INVALID_STROKE);
            graph.setColor(Color.RED);
        }

        graph.draw(new Rectangle2D.Double(region.getLow(0), region.getLow(1),
                region.getHigh(0) - region.getLow(0), region.getHigh(1) - region.getLow(1)));
    }
}
