/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2007, GeoTools Project Managment Committee (PMC)
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
package org.geotools.renderedimage.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;

import javax.swing.JPanel;

/**
 * Simple viwer for rendered images, allows for zoom in/out, tile grid display
 * 
 * @author Andrea Aime
 * 
 */
class ZoomableImageDisplay extends JPanel {

    /** The image to display. */
    protected RenderedImage image = null;

    protected double scale = 1.0;

    protected boolean tileGridVisible = true;

    /**
     * Constructs a <code>DisplayJAI</code> and sets its the layout to
     * <code>null</code>.
     */
    public ZoomableImageDisplay() {
        super();
        setLayout(null);
    }

    /**
     * Constructs a <code>DisplayJAI</code>, sets its layout to
     * <code>null</code>, and sets its displayed image.
     * 
     * <p>
     * The preferred size is set such that its width is the image width plus the
     * left and right insets and its height is the image height plus the top and
     * bottom insets.
     * </p>
     * 
     * @param image
     *            The image to display.
     * @throws IllegalArgumentException
     *             if <code>image</code> is <code>null</code>.
     */
    public ZoomableImageDisplay(RenderedImage image) {
        this();
        setImage(image);
    }

    public void setScale(double scale) {
        if (scale <= 0)
            throw new IllegalArgumentException("Scale must be a positive number");
        this.scale = scale;
        refreshComponent();
    }

    public double getScale() {
        return scale;
    }

    /**
     * Sets a new image to display.
     * 
     * @param image
     *            The image to display.
     * @throws IllegalArgumentException
     *             if <code>im</code> is <code>null</code>.
     */
    public void setImage(RenderedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image displayed cannot be null");
        }

        this.image = image;
        refreshComponent();
    }

    public RenderedImage getImage() {
        return image;
    }

    private void refreshComponent() {
        int w = (int) Math.ceil(image.getWidth() * scale);
        int h = (int) Math.ceil(image.getHeight() * scale);
        int minX = (int) Math.ceil(image.getMinX() * scale);
        int minY = (int) Math.ceil(image.getMinY() * scale);
        Insets insets = getInsets();
        Dimension dim = new Dimension(w + insets.left + insets.right + minX, h
                + insets.top + insets.bottom + minY);

        setPreferredSize(dim);
        revalidate();
        repaint();
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // clear damaged area
        Rectangle clipBounds = g2d.getClipBounds();
        g2d.setColor(getBackground());
        g2d.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);

        if (image == null)
            return;

        // Translation moves the entire image within the container
        try {
            g2d.drawRenderedImage(image, AffineTransform.getScaleInstance(scale, scale));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (tileGridVisible) {
            g2d.setColor(Color.WHITE);
            g2d.setXORMode(Color.BLACK);
            // g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP));
            final int x = (int) Math.floor(image.getTileGridXOffset() * scale);
            final int y = (int) Math.floor(image.getTileGridYOffset() * scale);
            final int th = (int) Math.round(image.getTileHeight() * scale);
            final int tw = (int) Math.round(image.getTileWidth() * scale);
            final int xCount = image.getNumXTiles();
            final int yCount = image.getNumYTiles();
            final int minty = image.getMinTileY();
            final int mintx = image.getMinTileX();
            for (int i = mintx; i < xCount + mintx + 1; i++) {
                g2d.drawLine(x + tw * i, y + th * minty, x + tw * i, y + th * (yCount + minty));
            }
            for (int j = minty; j < yCount + minty + 1; j++) {
                g2d.drawLine(x + mintx * tw, y + th * j, x + tw * (xCount + mintx), y + th * j);
            }
        }
    }

    public boolean isTileGridVisible() {
        return tileGridVisible;
    }

    public void setTileGridVisible(boolean visible) {
        this.tileGridVisible = visible;
        repaint();
    }
}
