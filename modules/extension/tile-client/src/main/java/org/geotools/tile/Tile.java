/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2008, Refractions Research Inc.
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
package org.geotools.tile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.io.ImageIOExt;
import org.geotools.util.logging.Logging;

/**
 * At tile represents a single space on the map within a specific ReferencedEnvelope. It holds a
 * RenderExecutorComposite for fetching its image, and an SWTImage (which is disposed at various
 * times). It listens to events for when to fetch, dispose, and construct new images. From
 *
 * @author GDavis
 * @author Ugo Taddei
 * @since 12
 */
public abstract class Tile implements ImageLoader {

    protected static final Logger LOGGER = Logging.getLogger(Tile.class);

    /**
     * These are the states of the tile. This state represents if the tile needs to be re-rendered
     * or not. A state of new or invalid means the tile should be re-rendered
     */
    public enum RenderState {
        NEW,
        RENDERED,
        INVALID
    };

    /**
     * These states represent the state of the context. If the context is invalid than the rendering
     * stack no longer matches the rendering stack the user has defined and the rendering stack
     * needs to be updated.
     */
    public enum ContextState {
        OKAY,
        INVALID
    };

    /**
     * These states represent if the tile is on or off screen. This information is used to determine
     * what tiles can be disposed.
     */
    public enum ScreenState {
        ONSCREEN,
        OFFSCREEN
    };

    /**
     * These states represent if the tile has been validated in response to a user event.
     *
     * <p>This information is used along with the screen state to determine if a tile can be
     * disposed.
     */
    public enum ValidatedState {
        VALIDATED,
        OLD
    };

    /** The bounds of the tile */
    private ReferencedEnvelope env;

    /** The size of the tile in pixels */
    private int tileSize;

    /** Identifies the tile in the grid space. */
    private TileIdentifier tileIdentifier;

    /** The state of the image. */
    private RenderState renderState = RenderState.NEW;

    /** The state of the rendering stack */
    private ContextState contextState = ContextState.INVALID;

    /** If the tile is on screen or not. */
    private ScreenState screenState = ScreenState.OFFSCREEN;

    /** If after an update event the tile has been validated */
    private ValidatedState tileState = ValidatedState.VALIDATED;

    /** A listener that is notified when the state is changed. */
    private TileStateChangedListener listener = null;

    /** A listener that is notified when the state is changed. */
    private BufferedImage tileImage = null;

    /** A delegate to proved direct loading or load from a disk (cache). */
    private ImageLoader imageLoader = this;

    public void setImageLoader(ImageLoader imageLoader) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("ImageLoader cannot be null");
        }
        this.imageLoader = imageLoader;
    }

    /** for locking on the SWT image to prevent creating it multiple times */
    // private Object SWTLock = new Object();

    public Tile(TileIdentifier tileId, ReferencedEnvelope env, int tileSize) {

        if (env == null) {
            throw new IllegalArgumentException("Envelope cannot be null");
        }
        this.env = env;

        this.tileSize = tileSize;
        if (tileId == null) {
            throw new IllegalArgumentException("TileIdentifier cannot be null");
        }
        this.tileIdentifier = tileId;
    }

    public void setStateChangedListener(TileStateChangedListener listener) {
        this.listener = listener;
    }

    /** Disposes of the tile. */
    public void dispose() {
        // disposeSWTImage();
        setScreenState(ScreenState.OFFSCREEN);
        // TODO: figure out how to properly dispose of the render executors
        // we cannot just call dispose because this drops all the layer
        // listeners
        // that listen for events
        // getRenderExecutor().dispose();
    }

    public BufferedImage getBufferedImage() {

        // TODO REVIEW this getter has side effects!

        if (isImageLoadedOK()) {
            return this.tileImage;
        }

        try {
            this.tileImage = this.imageLoader.loadImageTileImage(this);
            setRenderState(RenderState.RENDERED);

            return this.tileImage;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load image: " + this.getUrl(), e);
            setRenderState(RenderState.INVALID);
            return createErrorImage("Failed: " + getId());
        }
    }

    public BufferedImage loadImageTileImage(Tile tile) throws IOException {
        return ImageIOExt.readBufferedImage(getUrl());
    }

    /**
     * Returns true if the image has been correctly loaded and the render state is {@link
     * RenderState.RENDERED}.
     *
     * @return the tile image
     */
    private boolean isImageLoadedOK() {
        return this.renderState == RenderState.RENDERED && this.tileImage != null;
    }

    /** Gets an image showing an error, possibly indicating a failure to load the tile image. */
    protected BufferedImage createErrorImage(final String message) {
        BufferedImage buffImage = null;

        final int size = getTileSize();
        buffImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = buffImage.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5));

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, size, size);
        graphics.setColor(Color.RED);
        graphics.drawLine(0, 0, size, size);
        graphics.drawLine(0, size, size, 0);
        int mesgWidth = graphics.getFontMetrics().stringWidth(message);
        graphics.drawString(message, (size - mesgWidth) / 2, size / 2);
        graphics.dispose();

        return buffImage;
    }

    /**
     * Creates an swt image from the tiles buffered image. private void createSWTImage() { //
     * synchronize this code to prevent multiple threads from creating the SWT image more times than
     * needed synchronized (SWTLock) { // if the SWTImage is created once the lock is gained, exit
     * if (swtImage != null && !swtImage.isDisposed()) { return; } // otherwise try creating the
     * SWTImage now try { BufferedImage buffImage = getBufferedImage(); swtImage =
     * AWTSWTImageUtils.createSWTImage(buffImage, false); } catch (Exception ex) {
     * java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ex); } } }
     */

    /** @return The size of the tile in pixels. */
    public int getTileSize() {
        return tileSize;
    }

    /** @return the bounds of the tile */
    public ReferencedEnvelope getExtent() {
        return env;
    }

    /**
     * @return the parent render executor public RenderExecutorComposite getRenderExecutor() {
     *     return renderExecutorComp; }
     */
    /**
     * Sets the state of the tiles image.
     *
     * <p>See getRenderState() for a description of the valid states.
     */
    public void setRenderState(RenderState newState) {
        this.renderState = newState;
        if (listener != null) {
            listener.renderStateChanged(this);
        }
    }

    /**
     * Gets the state of the tiled image.
     *
     * <p>One Of:
     *
     * <ul>
     *   <li>RenderState.NEW - a new tile that needs to be rendered
     *   <li>RenderState.Renderer - the tile has been rendered or is in the state of being rendered
     *   <li>RenderState.Invalid - something has changed and the tile's rendered image is not longer
     *       valid and needs to be re-rendered
     * </ul>
     */
    public RenderState getRenderState() {
        return this.renderState;
    }

    /**
     * This function returns the state of the tile render stack. If the context is invalid then the
     * context needs to be updated before the tile is rendered.
     *
     * <p>Should be one of:
     *
     * <ul>
     *   <li>INVALID - The context needs to be updated.
     *   <li>OKAY - The context is okay and does not need updating.
     * </ul>
     *
     * @return the state of the tiles rendering stack
     */
    public ContextState getContextState() {
        return this.contextState;
    }

    /**
     * Sets the state of the tile rendering stack.
     *
     * <p>See getContextState() for valid value descriptions.
     */
    public void setContextState(ContextState newState) {
        this.contextState = newState;
        if (listener != null) {
            listener.contextStateChanged(this);
        }
    }

    /**
     * Sets if the tile is on screen or not.
     *
     * <p>This is used with other information to determine if a tile can be disposed of. Valid
     * values include:
     *
     * <ul>
     *   <li>ONSCREEN - the tile has been requested by the viewport therefore we assume it is on
     *       screen
     *   <li>OFFSCREEN - this tile was not requested by the viewport
     * </ul>
     */
    public ScreenState getScreenState() {
        return this.screenState;
    }

    /**
     * Sets the screen state.
     *
     * <p>See getScreenState() for a description of the valid values.
     */
    public void setScreenState(ScreenState newState) {
        this.screenState = newState;
        if (listener != null) {
            listener.screenStateChanged(this);
        }
    }

    /**
     * Gets the validation state.
     *
     * <p>This is used in conjunction with the screen state to determine it a tile can be disposed
     * of. This state is set during a refresh event that is triggered from some gui event. Valid
     * values include:
     *
     * <ul>
     *   <li>VALIDATED - The tile is validated and ready to be used for painting on the screen.
     *       Don't remove this tile.
     *   <li>OLD - This tile is an old tile that if off screen can be removed.
     * </ul>
     */
    public ValidatedState getTileState() {
        return this.tileState;
    }

    /**
     * Sets the validation state.
     *
     * <p>See getTileState() for a description of valid values.
     */
    public void setTileState(ValidatedState newState) {
        this.tileState = newState;
        if (listener != null) {
            listener.validationStateChanged(this);
        }
    }

    /** Diese Methode wird verwendet um... TODO. */
    public String getId() {
        return this.tileIdentifier.getId();
    }

    /** Diese Methode wird verwendet um... TODO. */
    public TileIdentifier getTileIdentifier() {
        return this.tileIdentifier;
    }

    @Override
    public int hashCode() {
        return getUrl().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Tile)) {
            return false;
        }

        return getUrl().equals(((Tile) other).getUrl());
    }

    public String toString() {
        return this.getId(); // this.getUrl().toString();
    }

    public abstract URL getUrl();
}
