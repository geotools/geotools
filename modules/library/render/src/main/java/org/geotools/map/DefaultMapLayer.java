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
import java.util.Collection;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.memory.CollectionSource;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Default implementation of the MapLayer implementation
 * 
 * @author wolf
 * @source $URL$
 */
public class DefaultMapLayer implements MapLayer {
    

    /** Simple wrapper around a raw collection */
    private CollectionSource source = null;

    /** Holds value of property FeatureSource. */
    protected FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;

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
			fireMapLayerListenerLayerChanged(new MapLayerEvent(
					DefaultMapLayer.this, MapLayerEvent.DATA_CHANGED));
		}
	};

	/**
	 * Creates a new instance of DefaultMapLayer
	 * 
	 * @param featureSource
	 *            the data source for this layer
	 * @param style
	 *            the style used to represent this layer
	 * @param title
	 *            the layer title
	 * 
	 * @throws NullPointerException
	 *             DOCUMENT ME!
	 */
	public DefaultMapLayer(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, Style style,
			String title) {
		if ((featureSource == null) || (style == null) || (title == null)) {
			//throw new NullPointerException();
		}

		// enable data source listening
//		featureSource.addFeatureListener(sourceListener);

		this.featureSource = featureSource;
		this.style = style;
		this.title = title;
		this.visible = true;
        this.selected = false;
	}

    public DefaultMapLayer(CollectionSource source, Style style, String title) {
        
        if ((source == null) || (style == null) || (title == null)) {
            throw new NullPointerException();
        }
        this.source = source;
        this.style = style;
        this.title = title;
        this.visible = true;
        this.selected = false;
    }
    
    
	/**
	 * Creates a new instance of DefaultMapLayer
	 * 
	 * @param featureSource
	 *            the data source for this layer
	 * @param style
	 *            the style used to represent this layer
	 */
	public DefaultMapLayer(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, Style style) {
		this(featureSource, style, "");
	}

    
    /**
     * Creates a new instance of DefaultMapLayer using a non-emtpy feature collection as a parameter
     * 
     * @param collection
     *            the source feature collection
     * @param style
     *            the style used to represent this layer
     * @param title layer title
     */
    public DefaultMapLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> collection,
            Style style, String title) {
        this(DataUtilities.source(collection), style, title);
    }

    public DefaultMapLayer(Collection collection, Style style,
            String title) {
        this( new CollectionSource( collection ), style, title);
    }
	/**
	 * Creates a new instance of DefaultMapLayer using a non-emtpy feature
	 * collection as a parameter
	 * 
	 * @param collection
	 *            the source feature collection
	 * @param style
	 *            the style used to represent this layer
	 */
	public DefaultMapLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, Style style) {
		this(DataUtilities.source(collection), style, "");
	}

    public DefaultMapLayer(Collection collection, Style style) {
        //this(DataUtilities.source(collection), style, "");
    }
    
	/**
	 * * Add a new layer and trigger a {@link LayerListEvent}.
	 * 
	 * @param coverage
	 *            The new layer that has been added.
	 * @param style
	 * @throws SchemaException 
	 * @throws FactoryRegistryException 
	 * @throws TransformException 
	 */
	public DefaultMapLayer(GridCoverage coverage, Style style) throws TransformException, FactoryRegistryException, SchemaException {

		this(FeatureUtilities.wrapGridCoverage((GridCoverage2D) coverage), style, "");

	}
	
	/**
	 * Constructor which adds a new layer and trigger a {@link LayerListEvent}.
	 * 
	 * @param reader
	 *            a reader with the new layer that will be added.
	 * @param style
	 * @param title
	 * @param params GeneralParameterValue[] that describe how the {@link AbstractGridCoverage2DReader} shall read the images
	 * 
	 * @throws SchemaException 
	 * @throws FactoryRegistryException 
	 * @throws TransformException 
	 */
	public DefaultMapLayer(AbstractGridCoverage2DReader reader, Style style, String title, GeneralParameterValue[] params)
	throws TransformException, FactoryRegistryException, SchemaException {
	    
	    this(FeatureUtilities.wrapGridCoverageReader(reader,params), style, title);
	    
	}

	/**
	 * Constructor which adds a new layer and trigger a {@link LayerListEvent}.
	 * 
	 * @param reader
	 *            a reader with the new layer that will be added.
	 * @param style
	 * @param title
	 * 
	 * @throws SchemaException 
	 * @throws FactoryRegistryException 
	 * @throws TransformException 
	 */
	public DefaultMapLayer(AbstractGridCoverage2DReader reader, Style style, String title)
			throws TransformException, FactoryRegistryException, SchemaException {

		this(FeatureUtilities.wrapGridCoverageReader(reader,null), style, title);

	}

	
	/**
	 * Constructor which adds a new layer and triggers a {@link LayerListEvent}.
	 * 
	 * @param reader
	 *            a reader with the new layer that will be added
	 * @param style
	 * 
	 * @throws SchemaException 
	 * @throws FactoryRegistryException 
	 * @throws TransformException 
	 */
	public DefaultMapLayer(AbstractGridCoverage2DReader reader, Style style) 
	  throws TransformException, 
	         FactoryRegistryException, 
	         SchemaException {

		this(FeatureUtilities.wrapGridCoverageReader(reader,null), style, "");

	}

	/**
	 * * Add a new layer and trigger a {@link LayerListEvent}.
	 * 
	 * @param coverage
	 *            The new layer that has been added.
	 * @param style
	 * @param title
	 * @throws SchemaException 
	 * @throws FactoryRegistryException 
	 * @throws TransformException 
	 */
	public DefaultMapLayer(GridCoverage coverage, Style style, String title)
			throws TransformException, FactoryRegistryException, SchemaException {

		this(FeatureUtilities.wrapGridCoverage((GridCoverage2D) coverage), style, title);

	}
	/**
	 * Getter for property featureSource.
	 * 
	 * @return Value of property featureSource.
	 */
	public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource() {
		return this.featureSource;
	}

    public CollectionSource getSource() {
        return this.source;
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
		fireMapLayerListenerLayerChanged(new MapLayerEvent(this,
				MapLayerEvent.STYLE_CHANGED));
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

		fireMapLayerListenerLayerChanged(new MapLayerEvent(this,
				MapLayerEvent.METADATA_CHANGED));
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

		MapLayerEvent event = new MapLayerEvent(this,
				MapLayerEvent.VISIBILITY_CHANGED);

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
	 * @param selected new value of property selected.
	 */
	public void setSelected(boolean selected) {
		if (this.selected == selected) {
			return;
		}

		// change selection status and fire events
		this.selected = selected;

		MapLayerEvent event = new MapLayerEvent(this,
				MapLayerEvent.SELECTION_CHANGED);

		if (selected) {
			fireMapLayerListenerLayerSelected(event);
		} else {
			fireMapLayerListenerLayerDeselected(event);
		}
	}

	/**
	 * Returns the definition query established for this layer.
	 * 
	 * @return the definition query established for this layer. If not set, just
	 *         returns {@link Query.ALL}, if set, returns a copy of the actual
	 *         query object to avoid external modification
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
	 * If present (other than <code>Query.ALL</code>, a renderer or consumer
	 * must use it to limit the number of returned features based on the filter
	 * it holds and the value of the maxFeatures attributes, and also can use it
	 * as a performance control to limit the number of requested attributes
	 * </p>
	 * 
	 * @param query
	 *            the full filter for this layer.
	 * 
	 * @throws NullPointerException
	 *             if no query is passed on. If you want to reset a definition
	 *             query, pass it {@link Query.ALL} instead of <code>null</code>
	 * 
	 * @task TODO: test that the query filter is siutable for the layer's
	 *       <code>FeatureSource</code> schema
	 * 
	 * @see org.geotools.map.MapLayer#setQuery(org.geotools.data.Query)
	 */
	public void setQuery(final Query query) {
		if (query == null) {
			throw new NullPointerException(
					"must provide a Query. Do you mean Query.ALL?");
		}

		// be prudent
		this.query = new DefaultQuery(query);
		fireMapLayerListenerLayerChanged(new MapLayerEvent(this,
				MapLayerEvent.FILTER_CHANGED));
	}
	public ReferencedEnvelope getBounds() {
            // CRS could also be null, depends on FeatureType
            CoordinateReferenceSystem sourceCrs = featureSource.getSchema()
                    .getCoordinateReferenceSystem();
    
            try {
                ReferencedEnvelope bounds = featureSource.getBounds();
                if (null != bounds) {
                    // returns the bounds based on features
                    return bounds;
                }
    
                if (sourceCrs != null) {
                    // returns the envelope based on the CoordinateReferenceSystem
                    Envelope envelope = CRS.getEnvelope(sourceCrs);
                    if (envelope != null) {
                        return new ReferencedEnvelope(envelope); // nice!
                    }
                }
    
            } catch (MismatchedDimensionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // return a default ReferencedEnvelope
            return new ReferencedEnvelope(sourceCrs);
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
	public synchronized void addMapLayerListener(
			org.geotools.map.event.MapLayerListener listener) {
		if (listenerList == null) {
			listenerList = new javax.swing.event.EventListenerList();
		}

		if (listenerList.getListenerCount() == 0 && featureSource != null ) {
			// enable data source listening
			featureSource.addFeatureListener(sourceListener);
		}
		listenerList.add(org.geotools.map.event.MapLayerListener.class,
				listener);
	}

	/**
	 * Removes MapLayerListener from the list of listeners.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public synchronized void removeMapLayerListener(
			org.geotools.map.event.MapLayerListener listener) {
		listenerList.remove(org.geotools.map.event.MapLayerListener.class,
				listener);
		if (listenerList.getListenerCount() == 0 && featureSource != null ) {
			featureSource.removeFeatureListener(sourceListener);
		}
	}

	/**
	 * Notifies all registered listeners about the event.
	 * 
	 * @param event
	 *            The event to be fired
	 */
	protected void fireMapLayerListenerLayerChanged(
			org.geotools.map.event.MapLayerEvent event) {
		if (listenerList == null) {
			return;
		}

		Object[] listeners = listenerList.getListenerList();
		final int length=listeners.length;
		for (int i = length - 2; i >= 0; i -= 2) {
			if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
				((org.geotools.map.event.MapLayerListener) listeners[i + 1])
						.layerChanged(event);
			}
		}
	}

	/**
	 * Notifies all registered listeners about the event.
	 * 
	 * @param event
	 *            The event to be fired
	 */
    protected void fireMapLayerListenerLayerShown(
			org.geotools.map.event.MapLayerEvent event) {
		if (listenerList == null) {
			return;
		}

		Object[] listeners = listenerList.getListenerList();
		final int length=listeners.length;
		for (int i = length - 2; i >= 0; i -= 2) {
			if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
				((org.geotools.map.event.MapLayerListener) listeners[i + 1])
						.layerShown(event);
			}
		}
	}

	/**
	 * Notifies all registered listeners about the event.
	 * 
	 * @param event
	 *            The event to be fired
	 */
    protected void fireMapLayerListenerLayerHidden(
			org.geotools.map.event.MapLayerEvent event) {
		if (listenerList == null) {
			return;
		}

		Object[] listeners = listenerList.getListenerList();
		final int length=listeners.length;
		for (int i = length - 2; i >= 0; i -= 2) {
			if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
				((org.geotools.map.event.MapLayerListener) listeners[i + 1])
						.layerHidden(event);
			}
		}
	}

	/**
	 * Notifies all registered listeners about the selection event.
	 *
	 * @param event
	 *            The event to be fired
	 */
    protected void fireMapLayerListenerLayerSelected(
			org.geotools.map.event.MapLayerEvent event) {
		if (listenerList == null) {
			return;
		}

		Object[] listeners = listenerList.getListenerList();
		final int length=listeners.length;
		for (int i = length - 2; i >= 0; i -= 2) {
			if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
				((org.geotools.map.event.MapLayerListener) listeners[i + 1])
						.layerSelected(event);
			}
		}
	}

	/**
	 * Notifies all registered listeners about the deselection event.
	 *
	 * @param event
	 *            The event to be fired
	 */
    protected void fireMapLayerListenerLayerDeselected(
			org.geotools.map.event.MapLayerEvent event) {
		if (listenerList == null) {
			return;
		}

		Object[] listeners = listenerList.getListenerList();
		final int length=listeners.length;
		for (int i = length - 2; i >= 0; i -= 2) {
			if (listeners[i] == org.geotools.map.event.MapLayerListener.class) {
				((org.geotools.map.event.MapLayerListener) listeners[i + 1])
						.layerDeselected(event);
			}
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("DefaultMapLayer[ ");
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
