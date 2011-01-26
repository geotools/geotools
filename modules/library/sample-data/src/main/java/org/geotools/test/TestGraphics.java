package org.geotools.test;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class you can use in test cases to ensure a renderer is doing what you expected.
 * <p>
 * Originally made to check up on SLD settings; adding to support data in case it is of general
 * utility. Please be advised it is very hard to write cross platform tests for things
 *  
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class TestGraphics extends Graphics2D{
    public Map<Key, ?> hints;
    public Shape clip;
    public Shape draw;
    public GlyphVector glyphs;
    public float y;
    public float x;
    public Object image;
    public ImageObserver observer;
    public AffineTransform transform;
    public BufferedImageOp op;
    public String string;
    public Color background;
    public boolean fill;
    public Composite composite;
    public Paint paint;
    public Font font;
    
    @Override
    public void addRenderingHints(Map<?, ?> toAdd) {
        //hints.putAll( toAdd );
    }

    @Override
    public void clip(Shape s) {
        clip = s;
    }

    @Override
    public void draw(Shape s) {
        draw = s;
    }

    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        this.x = x;
        this.y = y;
        glyphs = g;
    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        this.image = img;
        this.transform = xform;
        this.observer = obs;
        return true;
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        this.image = img;
        this.x = x;
        this.y = y;
        this.op = op;
    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        this.image = img;
        this.transform = xform;
    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        this.image = img;
        this.transform = xform;
    }

    @Override
    public void drawString(String str, int x, int y) {
        this.string = str;
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawString(String s, float x, float y) {
        this.string= s;
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        StringBuffer build = new StringBuffer();
        char c;
        while( (c = iterator.next()) != iterator.DONE ){
            build.append( c );
        }
        this.string = build.toString();
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x,
            float y) {
        StringBuffer build = new StringBuffer();
        char c;
        while( (c = iterator.next()) != iterator.DONE ){
            build.append( c );
        }
        this.string = build.toString();
        this.x = x;
        this.y = y;
    }

    @Override
    public void fill(Shape s) {
        this.draw = s;
        this.fill = true;
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public Composite getComposite() {
        return composite;
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return null;
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return null;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public Object getRenderingHint(Key hintKey) {
        return hints.get( hintKey );
    }

    @Override
    public RenderingHints getRenderingHints() {
        return new RenderingHints( hints );
    }

    @Override
    public Stroke getStroke() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AffineTransform getTransform() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void rotate(double theta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rotate(double theta, double x, double y) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void scale(double sx, double sy) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setBackground(Color color) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setComposite(Composite comp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPaint(Paint paint) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setRenderingHint(Key hintKey, Object hintValue) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStroke(Stroke s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setTransform(AffineTransform Tx) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shear(double shx, double shy) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void transform(AffineTransform Tx) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void translate(int x, int y) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void translate(double tx, double ty) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Graphics create() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle,
            int arcAngle) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor,
            ImageObserver observer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height,
            ImageObserver observer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height,
            Color bgcolor, ImageObserver observer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
            int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
            int sx1, int sy1, int sx2, int sy2, Color bgcolor,
            ImageObserver observer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        x = x1;
        y = y1;
        fill = false;
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        draw( new Rectangle( x,y, width,height) );
    }

    @Override
    public void drawPolygon(int[] points, int[] points2, int points3) {
        draw( new Polygon( points, points2, points3 ) );
    }

    @Override
    public void drawPolyline(int[] points, int[] points2, int points3) {
        draw( new Polygon( points, points2, points3 ) );
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height,
            int arcWidth, int arcHeight) {
        draw( new RoundRectangle2D.Float(x,y,width,height,arcWidth, arcHeight ));
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle,
            int arcAngle) {
        fill( new Rectangle( x,y, width,height) );
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        fill( new Rectangle( x,y, width,height) );
    }

    @Override
    public void fillPolygon(int[] points, int[] points2, int points3) {
        fill( new Polygon( points, points2, points3 ) );
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        fill( new Rectangle( x,y,width,height));
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height,
            int arcWidth, int arcHeight) {
        fill( new RoundRectangle2D.Float(x,y,width,height,arcWidth, arcHeight ));
    }

    @Override
    public Shape getClip() {
        return clip;
    }

    @Override
    public Rectangle getClipBounds() {
        return clip != null ? clip.getBounds() : null;
    }

    @Override
    public Color getColor() {
        return (Color) paint;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return null;
    }

    @Override
    public void setClip(Shape clip) {
        this.clip = clip;
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        setClip( new Rectangle(x,y,width,height));
    }

    @Override
    public void setColor(Color c) {
        paint = c;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public void setPaintMode() {        
    }

    @Override
    public void setXORMode(Color c1) {
        // TODO Auto-generated method stub
        
    }

}
