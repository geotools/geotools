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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;


public class QueryDisplayPanel extends JPanel {
    FeatureCollection fc = null;

    void setResult(FeatureCollection fc) {
        this.fc = fc;
        repaint();
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

        if (fc != null) {
            FeatureIterator it = fc.features();

            while (it.hasNext()) {
                SimpleFeature f = (SimpleFeature) it.next();
                LineString geom = (LineString) f.getDefaultGeometry();
                Coordinate start = geom.getCoordinateN(0);
                Coordinate current;

                for (int i = 1; i < geom.getNumPoints(); i++) {
                    current = geom.getCoordinateN(i);
                    g2d.draw(new Line2D.Double(start.x, start.y, current.x, current.y));
                    start = current;
                }
            }

            fc.close(it);
        }

        g2d.transform(saveAT);
        g2d.dispose();
    }
}
