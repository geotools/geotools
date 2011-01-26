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
package org.geotools.map;

import java.io.IOException;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.memory.CollectionSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.styling.Style;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Implementation of {@link MapLayer} without restricting the return type of {@link #getFeatureSource()}
 * allows better support of the DataAccess API;
 * 
 * <p>
 * This implementation does not support a collection or grid coverage source.
 * <p>
 * This implementation was almost entirely stolen from that of {@link DefaultMapLayer}.
 * <p>
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @source $URL$
 */
public class FeatureSourceMapLayer implements MapLayer {

    /** Holds value of property FeatureSource. */
    protected FeatureSource<? extends FeatureType, ? extends Feature> featureSource;

    /** The style to symbolize the features of this layer */
    protected Style style;

    /** The query to limit the number of rendered features based on its filter */
    protected Query query = Query.ALL;

    /** Holds value of property title. */
    protected String title;

    /** Whether this layer is visible or not. */
    protected boolean visible;

    /** Whether this layer is selected or not. */
    protected boolean selected;

    /** Utility field used by event firing mechanism. */
    protected javax.swing.event.EventListenerList listenerList = null;

    /** Listener to forward feature source events as layer events */
    protected FeatureListener sourceListener = new FeatureListener() {
        public void changed(FeatureEvent featureEvent) {
            fireMapLayerListenerLayerChanged(new MapLayerEvent(FeatureSourceMapLayer.this,
                    MapLayerEvent.DATA_CHANGED));
        }
    };

    /**
     * Constructor
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     * @param title
     *            the layer title
     */
    public FeatureSourceMapLayer(
            FeatureSource<? extends FeatureType, ? extends Feature> featureSource, Style style,
            String title) {
        if (featureSource == null || style == null || title == null) {
            throw new NullPointerException();
        }
        this.featureSource = featureSource;
        this.style = style;
        this.title = title;
        this.visible = true;
        this.selected = false;
    }

    /**
     * Convenience constructor that sets title to the empty string.
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     */
    public FeatureSourceMapLayer(
            FeatureSource<? extends FeatureType, ? extends Feature> featureSource, Style style) {
        this(featureSource, style, "");
    }

    /**
     * Getter for property featureSource.
     * 
     * @return Value of property featureSource.
     */
    public FeatureSource<? extends FeatureType, ? extends Feature> getFeatureSource() {
        return this.featureSource;
    }

    /**
     * Returns null.
     * 
     * @see org.geotools.map.MapLayer#getSource()
     */
    public CollectionSource getSource() {
        return null;
    }

    /**
     * Getter for property style.
     * 
     * @return Value of property style.
     */
    public Style getStyle() {
        return this.style;
    }

    /**
     * Setter for property style.
     * 
     * @param style
     *            New value of property style.
     * 
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    public void setStyle(Style style) {
        if (style == null) {
            throw new NullPointerException();
        }
        this.style = style;
        fireMapLayerListenerLayerChanged(new MapLayerEvent(this, MapLayerEvent.STYLE_CHANGED));
    }

    /**
     * Getter for property title.
     * 
     * @return Value of property title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for property title.
     * 
     * @param title
     *            New value of property title.
     * 
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new NullPointerException();
        }
        this.title = title;
        fireMapLayerListenerLayerChanged(new MapLayerEvent(this, MapLayerEvent.METADATA_CHANGED));
    }

    /**
     * Getter for property visible.
     * 
     * @return Value of property visible.
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Setter for property visible.
     * 
     * @param visible
     *            New value of property visible.
     */
    public void setVisible(boolean visible) {
        if (this.visible == visible) {
            return;
        }
        // change visibility and fire events
        this.visible = visible;
        MapLayerEvent event = new MapLayerEvent(this, MapLayerEvent.VISIBILITY_CHANGED);
        if (visible) {
            fireMapLayerListenerLayerShown(event);
        } else {
            fireMapLayerListenerLayerHidden(event);
        }
    }

    /**
     * Getter for property selected.
     *
     * @return Value of property selected.
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Setter for property selected.
     *
     * @param selected
     *            New value of property selected.
     */
    public void setSelected(boolean selected) {
        if (this.selected == selected) {
            return;
        }
        // change visibility and fire events
        this.selected = selected;
        MapLayerEvent event = new MapLayerEvent(this, MapLayerEvent.SELECTION_CHANGED);
        if (selected) {
            fireMapLayerListenerLayerSelected(event);
        } else {
            fireMapLayerListenerLayerDeselected(event);
        }
    }

    /**
     * Returns the definition query established for this layer.
     * 
     * @return the definition query established for this layer. If not set, just returns
     *         {@link Query.ALL}, if set, returns a copy of the actual query object to avoid
     *         external modification
     * 
     * @see org.geotools.map.MapLayer#getQuery()
     */
    public Query getQuery() {
        return (query == Query.ALL) ? query : new DefaultQuery(query);
    }

    /**
     * Sets a definition query for this layer.
     * 
     * <p>
     * If present (other than <code>Query.ALL</code>, a renderer or consumer must use it to limit
     * the number of returned features based on the filter it holds and the value of the maxFeatures
     * attributes, and also can use it as a performance hto limit the number of requested attributes
     * </p>
     * 
     * @param query
     *            the full filter for this layer.
     * 
     * @throws NullPointerException
     *             if no query is passed on. If you want to reset a definition query, pass it
     *             {@link Query.ALL} instead of <code>null</code>
     * 
     * @task TODO: test that the query filter is suitable for the layer's <code>FeatureSource</code>
     *       schema
     * 
     * @see org.geotools.map.MapLayer#setQuery(org.geotools.data.Query)
     */
    public void setQuery(final Query query) {
        if (query == null) {
            throw new NullPointerException("must provide a Query. Do you mean Query.ALL?");
        }
        // be prudent
        this.query = new DefaultQuery(query);
        fireMapLayerListenerLayerChanged(new MapLayerEvent(this, MapLayerEvent.FILTER_CHANGED));
    }

    public ReferencedEnvelope getBounds() {
        CoordinateReferenceSystem sourceCrs = featureSource.getSchema()
                .getCoordinateReferenceSystem();
        ReferencedEnvelope env;
        try {
            env = new ReferencedEnvelope(featureSource.getBounds(), sourceCrs);
            return env;
        } catch (MismatchedDimensionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // ------------------------------------------------------------------------
    // EVENT HANDLING CODE
    // ------------------------------------------------------------------------

    /**
     * Registers MapLayerListener to receive events.
     * 
     * @param listener
     *            The listener to register.
     */
    public synchronized void addMapLayerListener(org.geotools.map.event.MapLayerListener listener) {
        if (listenerList == null) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        if (listenerList.getListenerCount() == 0 && featureSource != null) {
            // enable data source listening
            featureSource.addFeatureListener(sourceListener);
        }
        listenerList.add(org.geotools.map.event.MapLayerListener.class, listener);
    }

    /**
     * Removes MapLayerListener from the list of listeners.
     * 
     * @param listener
     *            The listener to remove.
     */
    public synchronized void removeMapLayerListener(org.geotools.map.event.MapLayerListener listener) {
        listenerList.remove(org.geotools.map.event.MapLayerListener.class, listener);
        if (listenerList.getListenerCount() == 0 && featureSource != null) {
            featureSource.removeFeatureListener(sourceListener);
        }
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerChanged(org.geotools.map.event.MapLayerEvent event) {
        if (listenerList == null) {
            return;
        }
        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
                ((org.geotools.map.event.MapLayerListener) listeners[i + 1]).layerChanged(event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerShown(org.geotools.map.event.MapLayerEvent event) {
        if (listenerList == null) {
            return;
        }
        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
                ((org.geotools.map.event.MapLayerListener) listeners[i + 1]).layerShown(event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerHidden(org.geotools.map.event.MapLayerEvent event) {
        if (listenerList == null) {
            return;
        }
        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
                ((org.geotools.map.event.MapLayerListener) listeners[i + 1]).layerHidden(event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the selection event.
     *
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerSelected(org.geotools.map.event.MapLayerEvent event) {
        if (listenerList == null) {
            return;
        }
        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
                ((org.geotools.map.event.MapLayerListener) listeners[i + 1]).layerSelected(event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the deselection event.
     *
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerDeselected(org.geotools.map.event.MapLayerEvent event) {
        if (listenerList == null) {
            return;
        }
        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
                ((org.geotools.map.event.MapLayerListener) listeners[i + 1]).layerDeselected(event);
            }
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("FeatureCollectionMapLayer[ ");
        if (title == null || title.length() == 0) {
            buf.append("UNNAMED");
        } else {
            buf.append(title);
        }
        if (visible) {
            buf.append(", VISIBLE");
        } else {
            buf.append(", HIDDEN");
        }
        if (selected) {
            buf.append(", SELECTED");
        } else {
            buf.append(", UNSELECTED");
        }
        buf.append(", style=");
        buf.append(style);
        buf.append(", data=");
        buf.append(featureSource);
        if (query != Query.ALL) {
            buf.append(", query=");
            buf.append(query);
        }
        buf.append("]");
        return buf.toString();
    }

}
