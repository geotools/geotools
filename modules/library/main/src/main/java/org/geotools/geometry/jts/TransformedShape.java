/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

// J2SE dependencies
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.geotools.referencing.operation.matrix.XAffineTransform;


/**
 * Apply an arbitrary {@link AffineTransform} on a {@link Shape}. This class is
 * used internally by {@link RenderedMarks}. It is designed for reuse with many
 * different affine transforms and shapes. This class is <strong>not</strong>
 * thread-safe.
 * 
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class TransformedShape extends AffineTransform implements Shape {
	/**
	 * The wrapped shape.
	 */
	public Shape shape;

	/**
	 * A temporary point.
	 */
	private final Point2D.Double point = new Point2D.Double();

	/**
	 * A temporary rectangle.
	 */
	private final Rectangle2D.Double rectangle = new Rectangle2D.Double();

	/**
	 * Construct a transformed shape initialized to the identity transform.
	 */
	public TransformedShape() {
	}

	/**
	 * Returns the 6 coefficients values.
	 */
	public void getMatrix(final float[] matrix, int offset) {
		matrix[offset] = (float) getScaleX(); // m00
		matrix[++offset] = (float) getShearY(); // m10
		matrix[++offset] = (float) getShearX(); // m01
		matrix[++offset] = (float) getScaleY(); // m11
		matrix[++offset] = (float) getTranslateX(); // m02
		matrix[++offset] = (float) getTranslateY(); // m12
	}

	/**
	 * Set the transform from a flat matrix.
	 * 
	 * @param matrix
	 *            The flat matrix.
	 * @param offset
	 *            The index of the first element to use in <code>matrix</code>.
	 */
	public void setTransform(final float[] matrix, int offset) {
		setTransform(matrix[offset], matrix[++offset], matrix[++offset],
				matrix[++offset], matrix[++offset], matrix[++offset]);
	}

	/**
	 * Set the transform from a flat matrix.
	 * 
	 * @param matrix
	 *            The flat matrix.
	 * 
	 */
	public void setTransform(final double[] matrix) {
		setTransform(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4],
				matrix[5]);
	}

	/**
	 * Apply a uniform scale.
	 */
	public void scale(final double s) {
		scale(s, s);
	}

	/**
	 * Tests if the specified coordinates are inside the boundary of the
	 * <code>Shape</code>.
	 */
	public boolean contains(double x, double y) {
		point.x = x;
		point.y = y;
		return contains(point);
	}

	/**
	 * Tests if a specified {@link Point2D} is inside the boundary of the
	 * <code>Shape</code>.
	 */
	public boolean contains(final Point2D p) {
		try {
			return shape.contains(inverseTransform(p, point));
		} catch (NoninvertibleTransformException exception) {
			exceptionOccured(exception, "contains");
			return false;
		}
	}

	/**
	 * Tests if the interior of the <code>Shape</code> entirely contains the
	 * specified rectangular area.
	 */
	public boolean contains(double x, double y, double width, double height) {
		rectangle.x = x;
		rectangle.y = y;
		rectangle.width = width;
		rectangle.height = height;
		return contains(rectangle);
	}

	/**
	 * Tests if the interior of the <code>Shape</code> entirely contains the
	 * specified <code>Rectangle2D</code>. This method might conservatively
	 * return <code>false</code>.
	 */
	public boolean contains(final Rectangle2D r) {
		try {
			return shape.contains(XAffineTransform.inverseTransform(this, r,
					rectangle));
		} catch (NoninvertibleTransformException exception) {
			exceptionOccured(exception, "contains");
			return false;
		}
	}

	/**
	 * Tests if the interior of the <code>Shape</code> intersects the interior
	 * of a specified rectangular area.
	 */
	public boolean intersects(double x, double y, double width, double height) {
		rectangle.x = x;
		rectangle.y = y;
		rectangle.width = width;
		rectangle.height = height;
		return intersects(rectangle);
	}

	/**
	 * Tests if the interior of the <code>Shape</code> intersects the interior
	 * of a specified <code>Rectangle2D</code>. This method might
	 * conservatively return <code>true</code>.
	 */
	public boolean intersects(final Rectangle2D r) {
		try {
			return shape.intersects(XAffineTransform.inverseTransform(this, r,
					rectangle));
		} catch (NoninvertibleTransformException exception) {
			exceptionOccured(exception, "intersects");
			return false;
		}
	}

	/**
	 * Returns an integer {@link Rectangle} that completely encloses the
	 * <code>Shape</code>.
	 */
	public Rectangle getBounds() {
		final Rectangle rect = shape.getBounds();
		return (Rectangle) XAffineTransform.transform(this, rect, rect);
	}

	/**
	 * Returns a high precision and more accurate bounding box of the
	 * <code>Shape</code> than the <code>getBounds</code> method.
	 * 
	 * @todo REVISIT: tranform currently results in a new rectangle being
	 *        created, is this a memory overhead?
	 */
	public Rectangle2D getBounds2D() {
		final Rectangle2D rect = shape.getBounds2D();
		return XAffineTransform.transform(this, rect, null);
		// REVISIT: used to read (this,rect,rect) - this can result in an
		// unmidifiable geometry exception
	}

	/**
	 * Returns an iterator object that iterates along the <code>Shape</code>
	 * boundary and provides access to the geometry of the <code>Shape</code>
	 * outline.
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		if (!isIdentity()) {
			if (at == null || at.isIdentity()) {
				return shape.getPathIterator(this);
			}
			at = new AffineTransform(at);
			at.concatenate(this);
		}
		return shape.getPathIterator(at);
	}

	/**
	 * Returns an iterator object that iterates along the <code>Shape</code>
	 * boundary and provides access to a flattened view of the
	 * <code>Shape</code> outline geometry.
	 */
	public PathIterator getPathIterator(AffineTransform at,
			final double flatness) {
		if (!isIdentity()) {
			if (at == null || at.isIdentity()) {
				return shape.getPathIterator(this, flatness);
			}
			at = new AffineTransform(at);
			at.concatenate(this);
		}
		return shape.getPathIterator(at, flatness);
	}

	/**
	 * Invoked when an inverse transform was required but the transform is not
	 * invertible. This error should not happen. However, even if it happen, it
	 * will not prevent the application to work since <code>contains(...)</code>
	 * method may conservatively return <code>false</code>. We will just log
	 * a warning message and continue.
	 */
	private static void exceptionOccured(
			final NoninvertibleTransformException exception, final String method) {
		final LogRecord record = new LogRecord(Level.WARNING, exception
				.getLocalizedMessage());
		record.setSourceClassName(TransformedShape.class.getName());
		record.setSourceMethodName(method);
		record.setThrown(exception);
		org.geotools.util.logging.Logging.getLogger("org.geotools.renderer.lite").log(record);
	}
}
