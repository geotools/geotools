/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.GeomCollectionIterator;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.style.GraphicStyle2D;
import org.geotools.renderer.style.IconStyle2D;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.Style2D;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A simple class that knows how to paint a Shape object onto a Graphic given a
 * Style2D. It's the last step of the rendering engine, and has been factored
 * out since both renderers do use the same painting logic.
 * 
 * @author Andrea Aime
 * @source $URL$
 */
public final class StyledShapePainter {
	private final static AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

	/** Observer for image loading */
	private final static Canvas imgObserver = new Canvas();

	/** The logger for the rendering module. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(StyledShapePainter.class.getName());

	LabelCache labelCache;

	/**
	 * Construct <code>StyledShapePainter</code>.
	 */
	public StyledShapePainter(LabelCache labelCache) {
		this.labelCache = labelCache;
	}

	/**
	 * Invoked automatically when a polyline is about to be draw. This
	 * implementation paints the polyline according to the rendered style
	 * 
	 * @param graphics
	 *            The graphics in which to draw.
	 * @param shape
	 *            The polygon to draw.
	 * @param style
	 *            The style to apply, or <code>null</code> if none.
	 * @param scale
	 *            The scale denominator for the current zoom level
	 * @throws FactoryException 
	 * @throws TransformException 
	 */
	public void paint(final Graphics2D graphics, final LiteShape2 shape,
			final Style2D style, final double scale) {
		if (style == null) {
			// TODO: what's going on? Should not be reached...
			LOGGER.severe("ShapePainter has been asked to paint a null style!!");

			return;
		}

		// Is the current scale within the style scale range?
		if (!style.isScaleInRange(scale)) {
			LOGGER.fine("Out of scale");
			return;
		}

		if(style instanceof IconStyle2D) {
		    AffineTransform temp = graphics.getTransform();
		    try {
    		    IconStyle2D icoStyle = (IconStyle2D) style;
    		    Icon icon = icoStyle.getIcon();
    		    graphics.setComposite(icoStyle.getComposite());

    		    // the displacement to be applied to all points, centers the icon and applies the 
    		    // Graphic displacement as well
                float dx = - (float) (icon.getIconWidth() / 2.0 + icoStyle.getDisplacementX()); 
    			float dy = - (float) (icon.getIconHeight() / 2.0 + icoStyle.getDisplacementY());
    			
    			// iterate over all points
    			float[] coords = new float[2];
    			PathIterator citer = getPathIterator(shape);
    			AffineTransform markAT = new AffineTransform(temp);
                while (!(citer.isDone())) {
                    citer.currentSegment(coords);
                    
                    markAT.setTransform(temp);
                    markAT.translate(coords[0] + dx , coords[1] + dy);
                    markAT.rotate(icoStyle.getRotation());
                    graphics.setTransform(markAT);
                    
                    icon.paintIcon(null, graphics, 0, 0);
                    citer.next();
                }
		    } finally {
		        graphics.setTransform(temp);
		    }
		} else if (style instanceof MarkStyle2D) {
			PathIterator citer = getPathIterator(shape);

			// get the point onto the shape has to be painted
			float[] coords = new float[2];
			MarkStyle2D ms2d = (MarkStyle2D) style;

			Shape transformedShape ;
			while (!(citer.isDone())) {
				citer.currentSegment(coords);
				transformedShape = ms2d.getTransformedShape(coords[0],
						coords[1]);
				if (transformedShape != null) {
					if (ms2d.getFill() != null) {
						graphics.setPaint(ms2d.getFill());
						graphics.setComposite(ms2d.getFillComposite());
						graphics.fill(transformedShape);
					}

					if (ms2d.getContour() != null) {
						graphics.setPaint(ms2d.getContour());
						graphics.setStroke(ms2d.getStroke());
						graphics.setComposite(ms2d.getContourComposite());
						graphics.draw(transformedShape);
					}
					citer.next();
				}
			}
		} else if (style instanceof GraphicStyle2D) {
			float[] coords = new float[2];
			PathIterator iter = getPathIterator(shape);
			iter.currentSegment(coords);

			GraphicStyle2D gs2d = (GraphicStyle2D) style;

			while (!(iter.isDone())) {
                iter.currentSegment(coords);
    			renderImage(graphics, coords[0], coords[1],
    					(Image) gs2d.getImage(), gs2d.getRotation(), gs2d
    							.getOpacity(), false);
    			iter.next();
			}
		} else {
			// if the style is a polygon one, process it even if the polyline is
			// not closed (by SLD specification)
			if (style instanceof PolygonStyle2D) {
				PolygonStyle2D ps2d = (PolygonStyle2D) style;

				if (ps2d.getFill() != null) {
					Paint paint = ps2d.getFill();

					if (paint instanceof TexturePaint) {
					    paint = (TexturePaint) paint;
					}

					graphics.setPaint(paint);
					graphics.setComposite(ps2d.getFillComposite());
					fillLiteShape(graphics, shape);
				}
				if (ps2d.getGraphicFill() != null) {
				    Shape oldClip = graphics.getClip();
				    try {
					paintGraphicFill(graphics, shape, ps2d.getGraphicFill(), scale);
				    } finally {
				        graphics.setClip(oldClip);
				    }
				}
			}

			if (style instanceof LineStyle2D) {
				LineStyle2D ls2d = (LineStyle2D) style;

				if (ls2d.getStroke() != null) {
					// see if a graphic stroke is to be used, the drawing method
					// is completely
					// different in this case
					if (ls2d.getGraphicStroke() != null) {
						drawWithGraphicsStroke(graphics, shape, ls2d
								.getGraphicStroke());
					} else {
						Paint paint = ls2d.getContour();

						if (paint instanceof TexturePaint) {
							TexturePaint tp = (TexturePaint) paint;
							BufferedImage image = tp.getImage();
							Rectangle2D rect = tp.getAnchorRect();
							AffineTransform at = graphics.getTransform();
							double width = rect.getWidth() * at.getScaleX();
							double height = rect.getHeight() * at.getScaleY();
							Rectangle2D scaledRect = new Rectangle2D.Double(0,
									0, width, height);
							paint = new TexturePaint(image, scaledRect);
						}

						// debugShape(shape);
						Stroke stroke = ls2d.getStroke();
						if (graphics
								.getRenderingHint(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON) {
							if (stroke instanceof BasicStroke) {
								BasicStroke bs = (BasicStroke) stroke;
								stroke = new BasicStroke(
										bs.getLineWidth() + 0.5f, bs
												.getEndCap(), bs.getLineJoin(),
										bs.getMiterLimit(), bs.getDashArray(),
										bs.getDashPhase());
							}
						}

						graphics.setPaint(paint);
						graphics.setStroke(stroke);
						graphics.setComposite(ls2d.getContourComposite());
						graphics.draw(shape);
					}
				}
			}
		}
	}

	/**
	 * Extracts a ath iterator from the shape
	 * @param shape
	 * @return
	 */
    private PathIterator getPathIterator(final LiteShape2 shape) {
        // DJB: changed this to handle multi* geometries and line and
        // polygon geometries better
        GeometryCollection gc;
        if (shape.getGeometry() instanceof GeometryCollection)
        	gc = (GeometryCollection) shape.getGeometry();
        else {
        	Geometry[] gs = new Geometry[1];
        	gs[0] = shape.getGeometry();
        	gc = shape.getGeometry().getFactory().createGeometryCollection(
        			gs); // make a Point,Line, or Poly into a GC
        }
        GeomCollectionIterator citer = new GeomCollectionIterator(gc,
        		IDENTITY_TRANSFORM, false, 1.0);
        return citer;
    }

	public void debugShape(Shape shape) {
		float[] pt = new float[2];
		PathIterator iter = shape.getPathIterator(null);
		while (!(iter.isDone())) {

			int type = iter.currentSegment(pt);
			String event = "unknown";
			if (type == PathIterator.SEG_CLOSE)
				event = "SEG_CLOSE";
			if (type == PathIterator.SEG_CUBICTO)
				event = "SEG_CUBIC";
			if (type == PathIterator.SEG_LINETO)
				event = "SEG_LINETO";
			if (type == PathIterator.SEG_MOVETO)
				event = "SEG_MOVETO";
			if (type == PathIterator.SEG_QUADTO)
				event = "SEG_QUADTO";
			System.out.println(event + " " + pt[0] + "," + pt[1]);
			iter.next();
		}
	}

	// draws the image along the path
	private void drawWithGraphicsStroke(Graphics2D graphics, Shape shape,
			BufferedImage image) {
		PathIterator pi = shape.getPathIterator(null, 10.0);
		double[] coords = new double[4];
		int type;

		// I suppose the image has been already scaled and its square
		int imageSize = image.getWidth();

		double[] first = new double[2];
		double[] previous = new double[2];
		type = pi.currentSegment(coords);
		first[0] = coords[0];
		first[1] = coords[1];
		previous[0] = coords[0];
		previous[1] = coords[1];

		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.finest("starting at " + first[0] + "," + first[1]);
		}

		pi.next();

		while (!pi.isDone()) {
			type = pi.currentSegment(coords);

			switch (type) {
			case PathIterator.SEG_MOVETO:

				// nothing to do?
				if (LOGGER.isLoggable(Level.FINEST)) {
					LOGGER.finest("moving to " + coords[0] + "," + coords[1]);
				}

				break;

			case PathIterator.SEG_CLOSE:

				// draw back to first from previous
				coords[0] = first[0];
				coords[1] = first[1];

				if (LOGGER.isLoggable(Level.FINEST)) {
					LOGGER.finest("closing from " + previous[0] + ","
							+ previous[1] + " to " + coords[0] + ","
							+ coords[1]);
				}

			// no break here - fall through to next section
			case PathIterator.SEG_LINETO:

				// draw from previous to coords
				if (LOGGER.isLoggable(Level.FINEST)) {
					LOGGER.finest("drawing from " + previous[0] + ","
							+ previous[1] + " to " + coords[0] + ","
							+ coords[1]);
				}

				double dx = coords[0] - previous[0];
				double dy = coords[1] - previous[1];
				double len = Math.sqrt((dx * dx) + (dy * dy)); // - imageWidth;

				double theta = Math.atan2(dx, dy);
				dx = (Math.sin(theta) * imageSize);
				dy = (Math.cos(theta) * imageSize);

				if (LOGGER.isLoggable(Level.FINEST)) {
					LOGGER.finest("dx = " + dx + " dy " + dy + " step = "
							+ Math.sqrt((dx * dx) + (dy * dy)));
				}

				double rotation = -(theta - (Math.PI / 2d));
				double x = previous[0];
				double y = previous[1];

				if (LOGGER.isLoggable(Level.FINEST)) {
					LOGGER.finest("len =" + len + " imageSize " + imageSize);
				}

				double dist = 0;

				for (dist = 0; dist < len - imageSize; dist += imageSize) {
					/* graphic.drawImage(image2,(int)x-midx,(int)y-midy,null); */
					renderImage(graphics, x, y, image, rotation, 1, true);
//					Use this code to visually debug the x,y used to draw the image
//					graphics.setColor(Color.BLACK);
//					graphics.setStroke(new BasicStroke());
//					graphics.draw(new Line2D.Double(x, y, x, y));
					
				    x += dx;
				    y += dy;
				}

				if (LOGGER.isLoggable(Level.FINEST)) {
					LOGGER.finest("loop end dist " + dist + " len " + len + " "
							+ (len - dist));
				}

				double remainder = len - dist;
				int remainingWidth = (int) Math.round(remainder);

				if (remainingWidth > 0) {
					// clip and render image
					if (LOGGER.isLoggable(Level.FINEST)) {
						LOGGER
								.finest("about to use clipped image "
										+ remainder);
					}

					// the +2 is a magic number. That is, I don't know exactly
					// where it comes from, but closing images always seem to be missing
					// two pixels...
					BufferedImage img = new BufferedImage(remainingWidth + 2,
							image.getHeight(), image.getType());
					Graphics2D ig = img.createGraphics();
					ig.drawImage(image, 0, 0, imgObserver);
					

					renderImage(graphics, x, y, img, rotation, 1, true);
				}

				break;

			default:
				LOGGER
						.warning("default branch reached in drawWithGraphicStroke");
			}

			previous[0] = coords[0];
			previous[1] = coords[1];
			pi.next();
		}
	}

	/**
	 * Renders an image on the device
	 * 
	 * @param graphics
	 *            the image location on the screen, x coordinate
	 * @param x
	 *            the image location on the screen, y coordinate
	 * @param y
	 *            the image
	 * @param image
	 *            DOCUMENT ME!
	 * @param rotation
	 *            the image rotatation
	 * @param opacity
	 *            DOCUMENT ME!
	 */
	private void renderImage(Graphics2D graphics, double x, double y,
			Image image, double rotation, float opacity, boolean leftMiddle) {
		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.finest("drawing Image @" + x + "," + y);
		}
        
		AffineTransform temp = graphics.getTransform();
		try {
    		AffineTransform markAT = new AffineTransform(graphics.getTransform());
    		markAT.translate(x, y);
    		markAT.rotate(rotation);
    		graphics.setTransform(markAT);
    		graphics.setComposite(AlphaComposite.getInstance(
    				AlphaComposite.SRC_OVER, opacity));
    		
    
    		// we moved the origin to the middle of the image.
    		if(leftMiddle) {
    		    graphics.drawImage(image, 0, -image
    	                .getHeight(imgObserver) / 2, imgObserver);
    		} else {
    		    graphics.drawImage(image, -image.getWidth(imgObserver) / 2, -image
    				.getHeight(imgObserver) / 2, imgObserver);
    		}
		} finally {
		    graphics.setTransform(temp);
		}
	}
	
	/**
	 * Filling multipolygons might result in holes where two polygons overlap. In this method we
	 * work around that by drawing each polygon as a separate shape
	 * @param g
	 * @param shape
	 */
	void fillLiteShape(Graphics2D g, LiteShape2 shape) {
	    if(shape.getGeometry() instanceof MultiPolygon && shape.getGeometry().getNumGeometries() > 1) {
	        MultiPolygon mp = (MultiPolygon) shape.getGeometry();
	        for (int i = 0; i < mp.getNumGeometries(); i++) {
                Polygon p = (Polygon) mp.getGeometryN(i);
                try {
                    g.fill(new LiteShape2(p, null, null, false, false));
                } catch(Exception e) {
                    // should not really happen, but anyways
                    throw new RuntimeException("Unexpected error occurred while rendering a multipolygon", e);
                }
            }
	    } else {
	        g.fill(shape);
	    }
	    
	}


	/**
	 * Paints a graphic fill for a given shape.
	 * 
	 * @param graphics Graphics2D on which to paint.
	 * @param shape Shape whose fill is to be painted.
	 * @param graphicFill a Style2D that specified the graphic fill.
	 * @param scale the scale of the current render.
	 * @throws TransformException
	 * @throws FactoryException
	 */
    private void paintGraphicFill(Graphics2D graphics, Shape shape, Style2D graphicFill, double scale) 
    {
    	// retrieves the bounds of the provided shape
    	Rectangle2D boundsShape = shape.getBounds2D();
    	
    	// retrieves the size of the stipple to be painted based on the provided graphic fill
        Rectangle2D stippleSize = null;
        if (graphicFill instanceof MarkStyle2D)
        {
        	Rectangle2D boundsFill = ((MarkStyle2D)graphicFill).getShape().getBounds2D();
        	double size = ((MarkStyle2D)graphicFill).getSize();
            double aspect = (boundsFill.getHeight() > 0 && boundsFill.getWidth() > 0) ? boundsFill.getWidth()/boundsFill.getHeight() : 1.0;
        	stippleSize = new Rectangle2D.Double(0, 0, size*aspect, size);
        } else if(graphicFill instanceof IconStyle2D) {
            Icon icon = ((IconStyle2D)graphicFill).getIcon();
            stippleSize = new Rectangle2D.Double(0, 0, icon.getIconWidth(), icon.getIconHeight());
        } else {
        	// if graphic fill does not provide bounds information, it is considered
        	// to be unsupported for stipple painting
        	return;
        }

        // computes the number of times the graphic will be painted as a stipple
        int toX = (int) Math.ceil(boundsShape.getWidth() / stippleSize.getWidth());
        int toY = (int) Math.ceil(boundsShape.getHeight() / stippleSize.getHeight());
        
        // creates a copy of the Graphics so that we can change it freely
        Graphics2D g = (Graphics2D)graphics.create();
        // adds the provided shape to the Graphics current clip region
        g.clip(shape);
        // retrieves the full clip region
        Shape clipShape = g.getClip();
        Rectangle2D boundsClip = clipShape.getBounds2D();
        
        // adjust the iteration indexes to avoid iterating a lot over areas that we won't be rendering
        int fromX = 0;
        if(boundsClip.getMinX() > boundsShape.getMinX()) {
            fromX = (int) Math.floor((boundsClip.getMinX() - boundsShape.getMinX()) / stippleSize.getWidth());
        }
        if(boundsClip.getMaxX() < boundsShape.getMaxX()) {
            toX -= (int) Math.floor((boundsShape.getMaxX() - boundsClip.getMaxX()) / stippleSize.getWidth());
        }
        
        // adjust the iteration indexes to avoid iterating a lot over areas that we won't be rendering
        int fromY = 0;
        if(boundsClip.getMinY() > boundsShape.getMinY()) {
            fromY = (int) Math.floor((boundsClip.getMinY() - boundsShape.getMinY()) / stippleSize.getHeight());
        }
        if(boundsClip.getMaxY() < boundsShape.getMaxY()) {
            toY -= (int) Math.floor((boundsShape.getMaxY() - boundsClip.getMaxY()) / stippleSize.getHeight());
        }
        
        // paints graphic fill as a stipple
        for (int i = fromX; i < toX; i++)
        {
            for (int j = fromY; j < toY; j++)
            {
            	// computes this stipple's shift in the X and Y directions
            	double translateX = boundsShape.getMinX() + i * stippleSize.getWidth();
            	double translateY = boundsShape.getMinY() + j * stippleSize.getHeight();
            	
            	// only does anything if current stipple intersects the clip region
                if (!clipShape.intersects(translateX, translateY, stippleSize.getWidth(), stippleSize.getHeight()))
                	continue;
                
                // creates a LiteShape2 for the stipple and paints it 
                LiteShape2 stippleShape = createStippleShape(stippleSize, translateX, translateY);
                paint(g, stippleShape, graphicFill, scale);
            }
        }
    }

    /**
     * Creates a stipple shape given a stipple size and a shift in the x and y directions.
     * The returned shape should be appropriate for painting a stipple using a GraphicFill.
     * 
     * @param stippleSize a Rectangle whose width and height indicate the size of the stipple.
     * @param translateX a translation value in the X dimension.
     * @param translateY a translation value in the Y dimension.
     * @return a LiteShape2 appropriate for painting a stipple using a GraphicFill.
     * @throws TransformException
     * @throws FactoryException
     */
    private LiteShape2 createStippleShape(Rectangle2D stippleSize, double translateX, double translateY)
    {
    	// builds the JTS geometry for the translated stipple
        GeometryFactory geomFactory = new GeometryFactory();
        Coordinate coord = new Coordinate(stippleSize.getCenterX() + translateX, stippleSize.getCenterY() + translateY);
        Geometry geom = geomFactory.createPoint(coord);
        
    	// builds a LiteShape2 object from the JTS geometry
        AffineTransform2D identityTransf = new AffineTransform2D(new AffineTransform());
        Decimator nullDecimator = new Decimator(-1, -1);
        LiteShape2 stippleShape;
        try {
            stippleShape = new LiteShape2(geom, identityTransf, nullDecimator, false);
        } catch(Exception e) {
            throw new RuntimeException("Unxpected exception building lite shape", e);
        }
        
        return stippleShape;
    }
	
}
