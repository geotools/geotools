/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.javafx;

/**
 * @author Jochen Saalfeld (jochen@intevation.de)
 * @author Alexander Woestmann (awoestmann@intevation.de)
 */

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.WMSLayer;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.jfree.fx.FXGraphics2D;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class is going to Manage the Display of a Map based on a WFS Service. It should have some
 * widgets to zoom and to draw a Bounding Box.
 */
public class FXMap extends Parent {

    DefaultFeatureCollection polygonFeatureCollection;
    Layer displayLayer;
    WebMapServer wms;

    private String serviceURL;
    private String serviceName;
    private int dimensionX;
    private int dimensionY;
    private static final String FORMAT = "image/png";
    private static final boolean TRANSPARACY = true;
    private static final String INIT_SPACIAL_REF_SYS = "EPSG:4326";
    private static final int INIT_LAYER_NUMBER = 0;
    private static final String POLYGON_LAYER_TITLE = "polygon-layer";
    private static final Logger log = Logger.getLogger(FXMap.class.getName());
    private WMSCapabilities capabilities;
    private List layers;
    private VBox vBox;
    private Label sourceLabel;

    private GeneralEnvelope layerBBox;
    private GeneralEnvelope maxBBox;

    private TextField epsgField;
    private Button updateImageButton;

    private int markerCount;

    private double mouseXPosOnClick;
    private double mouseYPosOnClick;
    private double lastMouseXPos;
    private double lastMouseYPos;

    private double previousMouseXPosOnClick;
    private double previousMouseYPosOnClick;

    private static final double DRAGGING_OFFSET = 4;
    private static final double HUNDRED = 100d;

    private static final double INITIAL_EXTEND_X1 = 850028;
    private static final double INITIAL_EXTEND_Y1 = 6560409;
    private static final double INITIAL_EXTEND_X2 = 1681693;
    private static final double INITIAL_EXTEND_Y2 = 5977713;
    private final double RELATIVE_MAP_MARGIN = 0.99;

    private static final double TEN_PERCENT_OF = 0.01d;

    private static String WPSG_WGS84 = "EPSG:4326";
    private static String WGS84 = "4326";
    private static String POLYGON_NAME = "polygon";
    private Group boxGroup;
    private AffineTransform screenToWorld;
    private AffineTransform worldToScreen;
    private CoordinateReferenceSystem crs;

    private int zoomLevel;
    private int lastZoomLevel;
    private Timer zoomTimer;
    private TimerTask zoomTask;
    private final int ZOOM_TIMEOUT = 1500;
    private final int MAX_ZOOM_LEVEL = 100;
    private final double ZOOM_FACTOR = 1.5;

    private double aspectXY;
    private Rectangle2D imageViewport;
    private MapContent mapContent;
    private GraphicsContext gc;
    private Canvas mapCanvas;
    private ScrollPane mapPane;
    private StreamingRenderer renderer;
    private FXGraphics2D graphics;

    private String selectedPolygonName;
    private String selectedPolygonID;
    private GeometryDescriptor geomDesc;
    private String geometryAttributeName;
    private String source;
    private FilterFactory2 ff;
    private StyleFactory sf;
    private StyleBuilder sb;

    private static final Color OUTLINE_COLOR = Color.BLACK;
    private static final Color SELECTED_COLOUR = Color.YELLOW;
    private static final Color FILL_COLOR = Color.CYAN;
    private static final Float OUTLINE_WIDTH = 0.3f;
    private static final Float FILL_TRANSPARACY = 0.4f;
    private static final Float STROKY_TRANSPARACY = 0.8f;

    /**
     * gets the children of this node.
     *
     * @return the children of the node
     */
    @Override
    public ObservableList getChildren() {
        return super.getChildren();
    }

    /**
     * adds a node to this map.
     *
     * @param n the node
     */
    public void add(Node n) {
        this.vBox.getChildren().remove(n);
        this.vBox.getChildren().add(n);
    }

    /**
     * Constructor
     *
     * @param wms WMS server
     * @param layer Layer containing map
     * @param dimensionX Map width
     * @param dimensionY map height
     * @param bounds Bounding box
     */
    public FXMap(
            WebMapServer wms,
            Layer layer,
            int dimensionX,
            int dimensionY,
            org.opengis.geometry.Envelope bounds)
            throws NoSuchAuthorityCodeException, FactoryException {

        System.setProperty("org.geotools.referencing.forceXY", "true");
        mapCanvas = new Canvas(dimensionX, dimensionY);
        gc = mapCanvas.getGraphicsContext2D();
        zoomLevel = 0;
        lastZoomLevel = 0;
        GeneralEnvelope layerBounds = null;
        this.crs = CRS.decode(this.INIT_SPACIAL_REF_SYS);
        layerBounds = layer.getEnvelope(crs);

        this.layerBBox = new GeneralEnvelope(bounds);
        this.layerBBox.setCoordinateReferenceSystem(this.crs);
        this.maxBBox = layerBBox;

        this.vBox = new VBox();
        this.wms = wms;
        this.displayLayer = layer;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;

        layers = new ArrayList<Layer>(0);
        layers.add(layer);

        WMSLayer wmsLayer = new WMSLayer(wms, displayLayer);
        this.mapContent = new MapContent();
        this.mapContent.addLayer(wmsLayer);
        this.mapContent.getViewport().setCoordinateReferenceSystem(crs);
        this.mapContent.getViewport().setBounds(new ReferencedEnvelope(layerBBox));

        this.getChildren().add(vBox);
        mapPane = new ScrollPane();
        mapPane.setMaxSize(dimensionX, dimensionY);
        mapPane.setPrefSize(dimensionX, dimensionY);
        mapPane.setContent(mapCanvas);
        mapPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mapPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.vBox.getChildren().add(mapPane);

        this.mapCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new OnMouseReleasedEvent());
        this.mapCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new OnMousePressedEvent());
        this.mapCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new OnMousePressedEvent());
        this.mapCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new OnMouseDraggedEvent());
        this.mapCanvas.addEventHandler(ScrollEvent.SCROLL, new OnMouseScrollEvent());

        zoomTimer = new Timer(true);
        try {
            repaint();
            refreshViewport();
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
    }

    /**
     * Checks, if the map was initialized properly.
     *
     * @return True if map was initialized, else false
     */
    public boolean isInitialized() {
        if (this.wms != null
                && this.mapContent != null
                && this.layers.size() > 0
                && this.getExtent() != null) {
            return true;
        } else {
            return false;
        }
    }

    /** Clears all drawn shapes */
    public void clearShapes() {
        ArrayList<Object> list = new ArrayList<Object>(1);
        list.add(this.vBox);
        this.getChildren().retainAll(list);
        markerCount = 0;
    }

    /**
     * Resize map
     *
     * @param width New width
     * @param height New height
     */
    public void resize(double width, double height) {
        this.mapCanvas.setWidth(width);
        this.mapCanvas.setHeight(height);
        this.mapPane.setPrefWidth(width);
        this.mapPane.setPrefHeight(height);
        this.dimensionX = (int) width;
        this.dimensionY = (int) height;
        repaint();
    }

    /** Repaint the map using Geotools StreamingRenderer and MapContent */
    public void repaint() {
        renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        graphics = new FXGraphics2D(this.gc);
        graphics.setBackground(java.awt.Color.BLACK);
        gc.clearRect(0, 0, dimensionX, dimensionY);
        Rectangle rectangle = new Rectangle(dimensionX, dimensionY);
        renderer.paint(graphics, rectangle, mapContent.getViewport().getBounds());
        Platform.runLater(
                () -> {
                    clearShapes();
                });
    }

    /** Recalculate screen-to-world and world-to-screen transformations */
    private void refreshViewport() {
        this.mapContent.getViewport().setScreenArea(new Rectangle(dimensionX, dimensionY));
        screenToWorld = mapContent.getViewport().getScreenToWorld();
        worldToScreen = mapContent.getViewport().getWorldToScreen();
    }

    /** Reset extent to initial value */
    public void resetExtent() {
        setExtent(new ReferencedEnvelope(this.layerBBox));
    }

    /**
     * Set Extent
     *
     * @param newExtent the new Extent
     */
    public void setExtent(ReferencedEnvelope newExtent) {
        this.mapContent.getViewport().setBounds(newExtent);
        repaint();
    }

    /**
     * Get the current map extent.
     *
     * @return The extent
     */
    public ReferencedEnvelope getExtent() {
        return mapContent.getViewport().getBounds();
    }

    /**
     * Adds a layer to the map and repaints
     *
     * @param layer The new layer
     */
    public void addLayer(org.geotools.map.Layer layer) {
        if (layer != null) {
            this.mapContent.addLayer(layer);
            repaint();
        }
    }

    /**
     * Get the current layers.
     *
     * @return The layers as list
     */
    public List<org.geotools.map.Layer> getLayers() {
        return this.mapContent.layers();
    }

    /**
     * Scales the map to zoom in/out without reloading the map
     *
     */
    private void scaleMap(double zoomDelta) {
        Point2D.Double lower;
        Point2D.Double upper;
        double newZoom = ZOOM_FACTOR * zoomDelta;
        double xScale = 0;
        double yScale = 0;

        if (zoomDelta > 0) {
            lower =
                    new Point2D.Double(
                            (dimensionX / 2) - (0.5 * dimensionX / newZoom),
                            (dimensionY / 2) - (0.5 * dimensionY / newZoom));
            upper =
                    new Point2D.Double(
                            (dimensionX / 2) + (0.5 * dimensionX / newZoom),
                            (dimensionY / 2) + (0.5 * dimensionY / newZoom));
            xScale = (upper.getX() - lower.getX()) / dimensionX;
            yScale = (upper.getY() - lower.getY()) / dimensionY;
            xScale += 1;
            yScale += 1;
        } else {
            newZoom *= -1;
            lower =
                    new Point2D.Double(
                            (dimensionX / 2) - (0.5 * dimensionX * newZoom),
                            (dimensionY / 2) - (0.5 * dimensionY * newZoom));
            upper =
                    new Point2D.Double(
                            (dimensionX / 2) + (0.5 * dimensionX * newZoom),
                            (dimensionY / 2) + (0.5 * dimensionY * newZoom));
            // TODO: Zoom out scaling
            xScale = 0.5;
            yScale = 0.5;
        }
        mapCanvas.setScaleX(mapCanvas.getScaleX() * xScale);
        mapCanvas.setScaleY(mapCanvas.getScaleY() * yScale);
    }

    /**
     * Set the used coordinate reference system
     *
     * @param crs The new crs
     */
    public void setMapCRS(CoordinateReferenceSystem crs) {
        this.mapContent.getViewport().setCoordinateReferenceSystem(crs);
        try {
            this.maxBBox =
                    new GeneralEnvelope(new ReferencedEnvelope(maxBBox).transform(crs, true));
            this.layerBBox =
                    new GeneralEnvelope(new ReferencedEnvelope(layerBBox).transform(crs, true));
        } catch (Exception tEx) {
            log.log(Level.SEVERE, tEx.getMessage());
        }
        this.crs = crs;
        repaint();
    }

    /**
     * Transform a point from screen to world coordinates.
     *
     * @param screenPoint Point in screen coordinates
     * @return Transformed point
     */
    public Point2D.Double transformScreenToWorld(Point2D.Double screenPoint) {
        Point2D.Double worldPoint = new Point2D.Double();
        this.mapContent.getViewport().getScreenToWorld().transform(screenPoint, worldPoint);
        return worldPoint;
    }

    /**
     * Transform a point from world to screen coordinates.
     *
     * @param worldPoint Point in world coordinates
     * @return Transformed point
     */
    public Point2D.Double transformWorldToScreen(Point2D.Double worldPoint) {
        Point2D.Double screenPoint = new Point2D.Double();
        this.mapContent.getViewport().getWorldToScreen().transform(worldPoint, screenPoint);
        return screenPoint;
    }

    /**
     * Zooms in/out. TODO: center zoom on mouse position
     *
     * @param x Mouse position x
     * @param y Mouse position y
     */
    private void zoom(int zoomDelta, double x, double y) {
        if (zoomDelta == 0) {
            return;
        }
        Point2D.Double lower;
        Point2D.Double upper;
        double newZoom = ZOOM_FACTOR * zoomDelta;
        ReferencedEnvelope newBounds;

        if (zoomDelta > 0) {
            lower =
                    new Point2D.Double(
                            (dimensionX / 2) - (0.5 * dimensionX / newZoom),
                            (dimensionY / 2) - (0.5 * dimensionY / newZoom));
            upper =
                    new Point2D.Double(
                            (dimensionX / 2) + (0.5 * dimensionX / newZoom),
                            (dimensionY / 2) + (0.5 * dimensionY / newZoom));
        } else {
            newZoom *= -1;
            lower =
                    new Point2D.Double(
                            (dimensionX / 2) - (0.5 * dimensionX * newZoom),
                            (dimensionY / 2) - (0.5 * dimensionY * newZoom));
            upper =
                    new Point2D.Double(
                            (dimensionX / 2) + (0.5 * dimensionX * newZoom),
                            (dimensionY / 2) + (0.5 * dimensionY * newZoom));
        }
        lower = transformScreenToWorld(lower);
        upper = transformScreenToWorld(upper);
        newBounds =
                new ReferencedEnvelope(
                        lower.getX(), upper.getX(), lower.getY(), upper.getY(), this.crs);

        setExtent(newBounds);
    }

    /**
     * Drags the map.
     *
     * @param fromXScreen Original x coordinate in screen coordinates
     * @param fromYScreen Original y coordinate in screen coordinates
     * @param toXScreen Target x coordinate in screen coordinates
     * @param toYScreen Target y coordinate in screen coordinates
     */
    private void drag(double fromXScreen, double fromYScreen, double toXScreen, double toYScreen) {
        mapCanvas.setTranslateX(0);
        mapCanvas.setTranslateY(0);

        ReferencedEnvelope bBox = this.mapContent.getViewport().getBounds();

        Point2D.Double minXY =
                transformScreenToWorld(new Point2D.Double(0 - toXScreen, 0 - toYScreen));
        Point2D.Double maxXY =
                transformScreenToWorld(
                        new Point2D.Double(dimensionX - toXScreen, dimensionY - toYScreen));

        ReferencedEnvelope newBounds =
                new ReferencedEnvelope(
                        minXY.getX(), maxXY.getX(), minXY.getY(), maxXY.getY(), this.crs);
        // TODO: Prevent exceeding max coordinate bounds
        if (!maxBBox.contains(newBounds, true)) {
            newBounds = bBox;
        }
        setExtent(newBounds);
    }

    /**
     * Draws a marker on a specific position.
     *
     * @param xPosition Marker x coordinate in screen coordinates
     * @param yPosition Marker y coordinate in screen coordinates
     */
    public void drawMarker(double xPosition, double yPosition) {
        double markerSpan =
                Math.sqrt(
                                Math.pow(this.mapCanvas.getWidth(), 2)
                                        + Math.pow(this.mapCanvas.getHeight(), 2))
                        / HUNDRED;
        double upperLeftX = xPosition - markerSpan;
        double upperLeftY = yPosition + markerSpan;
        double upperRightX = xPosition + markerSpan;
        double upperRightY = yPosition + markerSpan;
        double lowerLeftX = xPosition - markerSpan;
        double lowerLeftY = yPosition - markerSpan;
        double lowerRightX = xPosition + markerSpan;
        double lowerRightY = yPosition - markerSpan;
        Line upperLeftToLowerRight = new Line(upperLeftX, upperLeftY, lowerRightX, lowerRightY);
        Line upperRightToLowerLeft = new Line(upperRightX, upperRightY, lowerLeftX, lowerLeftY);
        upperLeftToLowerRight.setFill(null);
        upperLeftToLowerRight.setStroke(Color.RED);
        upperLeftToLowerRight.setStrokeWidth(2);
        upperLeftToLowerRight.setVisible(true);
        upperRightToLowerLeft.setFill(null);
        upperRightToLowerLeft.setStroke(Color.RED);
        upperRightToLowerLeft.setStrokeWidth(2);
        upperRightToLowerLeft.setVisible(true);
        this.getChildren().add(upperLeftToLowerRight);
        this.getChildren().add(upperRightToLowerLeft);
    }

    /**
     * Draws a box, defined by two corners.
     *
     * @param beginX First corner x coordinate in screen coordinates
     * @param beginY First corner y coordinate in screen coordinates
     * @param endX Second corner x coordinate in screen coordinates
     * @param endY Second corner y coordinate in screen coordinates
     */
    private void drawBox(double beginX, double beginY, double endX, double endY) {
        Line upperLine = new Line(beginX, beginY, endX, beginY);
        upperLine.setFill(null);
        upperLine.setStroke(Color.RED);
        upperLine.setStrokeWidth(2);
        this.getChildren().add(upperLine);

        Line leftLine = new Line(beginX, beginY, beginX, endY);
        leftLine.setFill(null);
        leftLine.setStroke(Color.RED);
        leftLine.setStrokeWidth(2);
        this.getChildren().add(leftLine);

        Line buttomLine = new Line(beginX, endY, endX, endY);
        buttomLine.setFill(null);
        buttomLine.setStroke(Color.RED);
        buttomLine.setStrokeWidth(2);
        this.getChildren().add(buttomLine);

        Line rightLine = new Line(endX, beginY, endX, endY);
        rightLine.setFill(null);
        rightLine.setStroke(Color.RED);
        rightLine.setStrokeWidth(2);
        this.getChildren().add(rightLine);
    }

    /** Eventhandler for mouse events on map. */
    private class OnMousePressedEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            // WHEN ON SAME X,Y SET MARKER, WEHN MARKER SET, MAKE BBBOX, WHEN
            // ON DIFFERENT, MOVE MAP. WHEN DOUBLE LEFT-CLICKED, ZOOM IN, WHEN
            // DOUBLE RIGHT, ZOOM OUT
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (e.getClickCount() > 1) {
                    zoom(10, 0, 0);
                }
                if (e.getClickCount() == 1) {
                    mouseXPosOnClick = e.getSceneX();
                    mouseYPosOnClick = e.getSceneY();
                    lastMouseXPos = mouseXPosOnClick;
                    lastMouseYPos = mouseYPosOnClick;
                }
            }
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                if (e.getClickCount() > 1) {
                    zoom(-10, 0, 0);
                }
                if (e.getClickCount() == 1) {
                    boxGroup.getChildren().clear();
                }
            }
            Point2D clickWorld =
                    transformScreenToWorld(new Point2D.Double(e.getSceneX(), e.getSceneY()));
        }
    }

    /** Eventhandler for mouse events on map. */
    private class OnMouseReleasedEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if (e.getX() < (mouseXPosOnClick + DRAGGING_OFFSET)
                    && e.getX() > (mouseXPosOnClick - DRAGGING_OFFSET)
                    && e.getY() < (mouseYPosOnClick + DRAGGING_OFFSET)
                    && e.getY() > (mouseYPosOnClick - DRAGGING_OFFSET)) {
                drawMarker(mouseXPosOnClick, mouseYPosOnClick);
                markerCount++;
                if (markerCount == 2) {
                    if (mouseXPosOnClick > previousMouseXPosOnClick) {
                        drawBox(
                                mouseXPosOnClick,
                                mouseYPosOnClick,
                                previousMouseXPosOnClick,
                                previousMouseYPosOnClick);
                    } else {
                        drawBox(
                                previousMouseXPosOnClick,
                                previousMouseYPosOnClick,
                                mouseXPosOnClick,
                                mouseYPosOnClick);
                    }
                } else if (markerCount > 2) {
                    markerCount = 0;
                }
                previousMouseXPosOnClick = mouseXPosOnClick;
                previousMouseYPosOnClick = mouseYPosOnClick;
            } else {
                drag(0, 0, mapCanvas.getTranslateX(), mapCanvas.getTranslateY());
                markerCount = 0;
            }
        }
    }

    /** Eventhandler for mouse events on map. */
    private class OnMouseScrollEvent implements EventHandler<ScrollEvent> {
        @Override
        public void handle(ScrollEvent e) {
            // WHEN SCROLLED IN, ZOOOM IN, WHEN SCROLLED OUT, ZOOM OUT
            if (e.getDeltaY() > 0) {
                zoomLevel++;
            }
            if (e.getDeltaY() < 0) {
                zoomLevel--;
            }
            scaleMap(zoomLevel - lastZoomLevel);
            try {
                zoomTimer.cancel();
            } catch (IllegalStateException ex) {
                log.log(Level.WARNING, ex.getMessage());
            }
            zoomTimer = new Timer(true);
            zoomTask =
                    new TimerTask() {
                        public void run() {
                            int zoomDelta = zoomLevel - lastZoomLevel;
                            zoom(zoomDelta, e.getX(), e.getY());
                            lastZoomLevel = zoomLevel;
                            mapCanvas.setScaleX(1);
                            mapCanvas.setScaleY(1);
                        }
                    };
            zoomTimer.schedule(zoomTask, ZOOM_TIMEOUT);
        }
    }

    /** Event handler for dragging events, does not reload the actual map */
    private class OnMouseDraggedEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            double xOffset = lastMouseXPos - e.getSceneX();
            double yOffset = lastMouseYPos - e.getSceneY();
            lastMouseXPos = e.getSceneX();
            lastMouseYPos = e.getSceneY();
            mapCanvas.setTranslateX(mapCanvas.getTranslateX() + (-1 * xOffset));
            mapCanvas.setTranslateY(mapCanvas.getTranslateY() + (-1 * yOffset));
        }
    }
}
