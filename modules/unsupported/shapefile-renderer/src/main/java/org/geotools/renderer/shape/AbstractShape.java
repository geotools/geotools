/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.shape;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.vividsolutions.jts.geom.Envelope;

/**
 * An abstract java awt shape that will allow a SimpleGeometry to be drawn using Graphics2D 
 * 
 * @author jeichar
 * @since 2.1.x
 * @source $URL$
 */
public abstract class AbstractShape implements Shape {

	protected SimpleGeometry geom;
	
	/**
	 * @param geom
	 */
	public AbstractShape(SimpleGeometry geom) {
		this.geom=geom;
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {
		return new Rectangle((int)geom.bbox.getMinX(), (int)geom.bbox.getMinY(), 
				(int)geom.bbox.getWidth(), (int)geom.bbox.getHeight());
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(geom.bbox.getMinX(), geom.bbox.getMinY(), 
				geom.bbox.getWidth(), geom.bbox.getHeight());
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return geom.bbox.contains(x,y);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D p) {
		return geom.bbox.contains(p.getX(), p.getY());
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		return geom.bbox.intersects(new Envelope( x,x+w,y,y+h));
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return geom.bbox.intersects(new Envelope(r.getMinX(), r.getMaxX(), 
				r.getMinY(), r.getMaxY()));
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) {
		return geom.bbox.contains(new Envelope( x,x+w,y,y+h));
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D r) {
		return geom.bbox.contains(new Envelope(r.getMinX(), r.getMaxX(), 
				r.getMinY(), r.getMaxY()));
	}


}
