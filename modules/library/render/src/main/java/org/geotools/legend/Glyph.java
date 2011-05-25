/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.legend;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import org.geotools.styling.Rule;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
 
 
/**
 * Renderers a small Glyph used to represent a Map Layer in a legend.
 * <p>
 * Because GeoTools is used both with Swing and SWT applications this class
 * is set up to produce BufferedImage.
 * 
 * @author Johann Sorel (AlterSIG)
 *
 *
 * @source $URL$
 */
public class Glyph {
 
    /** Utility class for working with Images, Features and Styles */
    private static Drawer drawer = Drawer.create();
 
    private final static int DEFAULT_WIDTH = 16;
    private final static int DEFAULT_HEIGHT = 16;
    private static final int DEFAULT_DEPTH = BufferedImage.TYPE_INT_ARGB;    
    private static final Color DEFAULT_BORDER = new Color(0,0,0);
    private static final Color DEFAULT_FILL = new Color(27,158,119, 255);
 
 
    /**
     * Create a transparent image, this is a *real* resource against the
     * provided display.
     *
     * @return
     */
    public static BufferedImage image() {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH);
        return bi;
    }
 
    /**
     * Render a icon based on the current style.
     * <p>
     * Simple render of point in the center of the screen.
     * </p>
     * @param style
     * @return Icon representing style applyed to an image
     */
    public static BufferedImage point( final Rule rule ) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH);
        drawer.drawDirect( bi, drawer.feature(drawer.point(7,7)), rule );
        return bi;
    }
 
    /**
     * Icon for point data in the provided color
     * <p>
     * XXX: Suggest point( SLD style ) at a later time.
     * </p>
     * @param color
     * @param fill
     * @return ImageDescriptor
     */
    public static BufferedImage point( final Color color, final Color fill ) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH);
 
        Graphics2D gc = (Graphics2D) bi.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
 
        Stroke stroke = new BasicStroke(1);
        gc.setStroke(stroke);
 
        Color c = color;
        Color f = fill;
 
        if( c == null ) c = Color.BLACK;
        if( f == null ) f = Color.LIGHT_GRAY;
 
        gc.setColor(f);
        gc.fillRect( 8,7, 5, 5 );
 
        gc.setColor(c);
        gc.drawRect( 8,7, 5, 5 );
 
        return bi;
    }
 
    /**
     * Complex render of Geometry allowing presentation of point, line and polygon styles.
     * <p>
     * Layout:<pre><code>
     *    1 2 3 4 5 6 7 8 9101112131415
     *   0
     *  1          LL                 L
     *  2          L L                L
     *  3         L  L               L
     *  4        L    L             L
     *  5        L     L            L
     *  6       L      L           L
     *  7      L        L         L
     *  8      L         L        L
     *  9     L          L       L
     * 10    L            L     L
     * 11    L             L    L
     * 12   L              L   L
     * 13  L                L L
     * 14  L                 LL
     * 15
     * </code><pre>
     * </p>
     * @param style
     * @return Icon representing geometry style
     */
    public static BufferedImage line( final Rule rule ) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH);
        drawer.drawDirect( bi, drawer.feature(drawer.line(new int[]{1,14, 6,0, 11,14, 15,1})), rule );
        return bi;
    }
 
    /**
     * Icon for linestring in the provided color and width.
     * <p>
     * XXX: Suggest line( SLD style ) at a later time.
     * </p>
     * @param black
     * @return Icon
     */
    public static BufferedImage line( Color color, int width ) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH);
 
        Graphics2D gc = (Graphics2D) bi.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
 
        Color color2 = color;
        int width2 = width;
 
        if (color2 == null) color2 = Color.BLACK;
        if (width2 <= 0) width2 = 1;
 
        final int finalWidth = width2;
        final Color finalColor = color2;
 
        Stroke stroke = new BasicStroke(finalWidth);
        gc.setStroke(stroke);
 
        gc.setColor(finalColor);
        gc.drawLine(1, 13, 6, 2);
        gc.drawLine(6, 2, 9, 13);
        gc.drawLine(9, 13, 14, 2);
 
        return bi;
    }
 
    /**
     * Complex render of Geometry allowing presentation of point, line and polygon styles.
     * <p>
     * Layout:<pre><code>
     *    1 2 3 4 5 6 7 8 9101112131415
     *   0
     *  1
     *  2
     *  3           L                 L
     *  4       p  L L           PPPPPP
     *  5         L   L     PPPPP   L p
     *  6        L     LPPPP       L  p
     *  7       L    PPPL         L   p
     *  8      L   PP    L       L    p
     *  9     L   P       L     L     P
     * 10    L   P         L   L      P
     * 11   L   P           L L       P
     * 12  L   P             L        P
     * 13      p                      P
     * 14      PPPPPPPPPPPPPPPPPPPPPPPP
     * 15
     * </code><pre>
     * </p>
     * @param style
     * @return Icon representing geometry style
     */
    public static BufferedImage geometry( final Rule rule ) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH);
        drawer.drawDirect( bi, drawer.feature(drawer.polygon(new int[]{1,14, 3,9, 4,6,  6,4,  9,3, 14,1, 14,14})), rule );
        drawer.drawDirect( bi,drawer.feature(drawer.line(new int[]{0,12, 6,3, 11,12, 15,3})),rule );
        drawer.drawDirect( bi, drawer.feature(drawer.point(4,4)), rule );
        return bi;
    }
 
    /**
     * Icon for generic Geometry or Geometry Collection.
     * @param color
     * @param fill
     *
     * @return Icon
     */
    public static BufferedImage geometry( final Color color, final Color fill ) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
 
        Graphics2D gc = (Graphics2D) bi.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
 
        Stroke stroke = new BasicStroke(1);
        gc.setStroke(stroke);
 
        Color c = color;
        Color f = fill;
 
        if( c == null ) c = Color.BLACK;
        if( f == null ) f = Color.LIGHT_GRAY;
 
        gc.setColor(f);
        gc.fillRoundRect( 2,1, 13, 13, 2, 2 );
 
        gc.setColor(c);
        gc.drawRoundRect( 2,1, 13, 13, 2, 2 );
 
        return bi;
    }
 
    /**
     * Render of a polygon allowing style.
     * <p>
     * Layout:<pre><code>
     *    1 2 3 4 5 6 7 8 9101112131415
     *   0
     *  1
     *  2                      PPPPPPPP
     *  3                PPPPPP       P
     *  4           PPPPPP            P
     *  5        PPP                  p
     *  6      PP                     p
     *  7     P                       p
     *  8    P                        p
     *  9   P                         P
     * 10   P                         P
     * 11  P                          P
     * 12  P                          P
     * 13  P                          P
     * 14  PPPPPPPPPPPPPPPPPPPPPPPPPPPP
     * 15
     * </code><pre>
     * </p>
     * @param style
     * @return Icon representing geometry style
     */
    public static BufferedImage Polygon( final Rule rule ){
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH);
        drawer.drawDirect( bi, drawer.feature(drawer.polygon(new int[]{1,14, 3,9, 4,6,  6,4,  9,3, 14,1, 14,14})), rule );
        return bi;
    }
 
    /**
     * Icon for polygon in provided border, fill and width
     *
     * @param black
     * @param gray
     * @param i
     * @return
     */
    public static BufferedImage polygon( final Color color, final Color fill, final int width ) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
 
        Graphics2D gc = (Graphics2D) bi.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
 
        Color c = color;
        Color f = fill;
        int w = (width > 0) ? width : 1;
 
        if( c == null ) c = Color.BLACK;
        if( f == null ) f = Color.LIGHT_GRAY;
 
        Stroke stroke = new BasicStroke(w);
        gc.setStroke(stroke);
 
        int[] xs = new int[]{1,3,4,6,9,14,14};
        int[] ys = new int[]{14,9,6,4,3,1,14};
        int nb = 7;
 
        gc.setColor(c);
        gc.fillPolygon( xs, ys, nb);
        gc.setColor(f);
        gc.drawPolygon( xs, ys, nb);
 
        return bi;
    }
 
    /**
     * Icon for grid data, small grid made up of provided colors.
     * <p>
     * Layout:<pre><code>
     *    0 1 2 3 4 5 6 7 8 9 101112131415
     *  0
     *  1   AAAAAAAAAAAAABBBBBBBBBBBBBB
     *  2   AAAAAAAAAAAAABBBBBBBBBBBBBB
     *  3   AAAAAAAAAAAAABBBBBBBBBBBBBB
     *  4   AAAAAAAAAAAAABBBBBBBBBBBBBB
     *  5   AAAAAAAAAAAAABBBBBBBBBBBBBB
     *  6   AAAAAAAAAAAAABBBBBBBBBBBBBB
     *  7   AAAAAAAAAAAAABBBBBBBBBBBBBB
     *  8   CCCCCCCCCCCCCDDDDDDDDDDDDDD
     *  9   CCCCCCCCCCCCCDDDDDDDDDDDDDD
     * 10   CCCCCCCCCCCCCDDDDDDDDDDDDDD
     * 11   CCCCCCCCCCCCCDDDDDDDDDDDDDD
     * 12   CCCCCCCCCCCCCDDDDDDDDDDDDDD
     * 13   CCCCCCCCCCCCCDDDDDDDDDDDDDD
     * 14   CCCCCCCCCCCCCDDDDDDDDDDDDDD
     * 15
     * </code><pre>
     * </p>
     * @param a
     * @param b
     * @param c
     * @param d1
     * @return Icon representing a grid
     *
     */
    public static BufferedImage grid( Color a, Color b, Color c, Color d) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
 
        Graphics2D gc = (Graphics2D) bi.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
 
        if (a == null) a = Color.BLACK;
        if (b == null) b = Color.DARK_GRAY;
        if (c == null) c = Color.LIGHT_GRAY;
        if (d == null) d = Color.WHITE;
 
        gc.setColor( a );
        gc.fillRect( 0, 0, 7, 7);
 
        gc.setColor( b );
        gc.fillRect( 7, 0, 15, 7 );
 
        gc.setColor( c );
        gc.fillRect( 0, 7, 7, 15 );
 
        gc.setColor( d );
        gc.fillRect( 7, 7, 15, 15 );
 
        gc.setColor( Color.BLACK  );
        gc.drawRect( 0, 0, 7, 7 );
        gc.drawRect( 0, 0, 15, 7 );
        gc.drawRect( 0, 7, 7, 15 );
        gc.drawRect( 0, 7, 15, 15 );
 
        return bi;
    }
 
    /**
     * Render of a color swatch allowing style.
     * <p>
     * Layout:<pre><code>
     *    0 1 2 3 4 5 6 7 8 9 101112131415
     *  0
     *  1  dddddddddddddddddddddddddddd
     *  2 dCCCCCCCCCCCCCCCCCCCCCcCCCCCCd
     *  3 dCCCCCCCCCCCCCCCCCCCCCCcCCCCCd
     *  4 dCCCCCCCCCCCCCCCCCCCCCCCcCCCCd
     *  5 dCCCCCCCCCCCCCCCCCCCCCCCCcCCCd
     *  6 dCCCCCCCCCCCCCCCCCCCCCCCCCcCCd
     *  7 dCCCCCCCCCCCCCCCCCCCCCCCCCCcCd
     *  8 dCcCCCCCCCCCCCCCCCCCCCCCCCCCCd
     *  9 dCCcCCCCCCCCCCCCCCCCCCCCCCCCCd
     * 10 dCCCcCCCCCCCCCCCCCCCCCCCCCCCCd
     * 11 dCCCCcCCCCCCCCCCCCCCCCCCCCCCCd
     * 12 dCCCCCcCCCCCCCCCCCCCCCCCCCCCCd
     * 13 ddCCCCCcCCCCCCCCCCCCCCCCCCCCdd
     * 14  ddddddddddddddddddddddddddd
     * 15
     * </code><pre>
     * </p>
     * @param style
     * @return Icon representing geometry style
     */
    public static BufferedImage swatch( Color c ) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
 
        Graphics2D gc = (Graphics2D) bi.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
 
        Color c2=c;
        if( c == null ) c2=Color.GRAY;
        else            c2=c;
 
        final Color color=c2;
 
        int saturation = color.getRed() + color.getGreen() + color.getBlue();
        final Color contrast = saturation < 384 ? c.brighter() : c.darker();
 
        gc.setColor( color );
        gc.fillRoundRect( 0, 0, 14, 14, 2, 2);
 
        gc.setColor( contrast );
        gc.drawRoundRect( 0, 0, 14, 14, 2, 2 );
 
        return bi;
    }
 
    /**
     * Icon for grid data, small grid made up of provided colors.
     * Layout:<pre><code>
     *    0 1 2 3 4 5 6 7 8 9 101112131415
     *  0
     *  1 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     *  2 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     *  3 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     *  4 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     *  5 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     *  6 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     *  7 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     *  8 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     *  9 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     * 10 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     * 11 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     * 12 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     * 14 AABBCDEEFfGgHhIiJjKkllmmnnoopp
     * 15
     * </code><pre>
     * </p>
     * @param c palette of colors
     * @return Icon representing a palette
     *
     */
    public static BufferedImage palette( Color c[]) {
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
 
        Graphics2D gc = (Graphics2D) bi.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
 
 
        final Color[] colors = new Color[16];
        Color color = Color.GRAY;
        if( c == null ){
            for( int i=0; i<16; i++) color = Color.GRAY;
        } else {
            for( int i=0; i<16; i++) {
                int lookup = (i*c.length)/16;
                if( c[ lookup ] != null ) color = c[ lookup ];
                colors[i] = color;
            }
        }
 
 
        for( int i=0; i<16;i++){
            gc.setColor( colors[i] );
            gc.drawLine(i,0,i,15);
        }
 
        gc.setColor( Color.GRAY );
        gc.drawRoundRect( 0, 0, 14, 14, 2, 2 );
 
        return bi;
    }
 
    public static BufferedImage icon( SimpleFeatureType ft ) {
        if( ft==null || ft.getGeometryDescriptor()==null )
            return null;
 
        Class binding = ft.getGeometryDescriptor().getType().getBinding();
		if( Point.class.isAssignableFrom(binding)
        || MultiPoint.class.isAssignableFrom(binding) ){
            return point(DEFAULT_BORDER, DEFAULT_FILL);
        }
 
        if( LineString.class.isAssignableFrom(binding)
        || MultiLineString.class.isAssignableFrom(binding)
        || LinearRing.class.isAssignableFrom(binding)){
            return line(DEFAULT_BORDER, 1);
        }
 
        if( Polygon.class.isAssignableFrom(binding)
        || MultiPolygon.class.isAssignableFrom(binding) ){
            return polygon(DEFAULT_BORDER, DEFAULT_FILL, 1);
        }
 
        return geometry(DEFAULT_BORDER, DEFAULT_FILL);
    }
 
}
