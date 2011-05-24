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
package org.geotools.feature;

import java.util.EventObject;

import org.geotools.data.FeatureEvent;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;


/**
 * A simple event object to represent all events triggered by FeatureCollection
 * instances (typically change events).
 *
 * @source $URL$
 */
public class CollectionEvent extends EventObject {
    private static final long serialVersionUID = -1864190177730929948L;

    /*
     * Design Notes:
     *  - Must look at other classes for hints on how to implement nicely.
     */

    /** event type constant denoting the adding of a feature */
    public static final int FEATURES_ADDED = 0;

    /** event type constant denoting the removal of a feature */
    public static final int FEATURES_REMOVED = 1;

    /**
     * event type constant denoting that features in the collection has been
     * modified
     */
    public static final int FEATURES_CHANGED = 2;

    /** Indicates one of FEATURES_ADDED, FEATURES_REMOVED, FEATURES_CHANGED */
    private int type;

    /** Holds value of property features. */
    private SimpleFeature[] features;

    public CollectionEvent(FeatureCollection<? extends FeatureType, ? extends Feature> collection, FeatureEvent event) {
        super(collection);

        switch ( event.getType() ) {
        case ADDED:
            this.type = CollectionEvent.FEATURES_ADDED;
            break;

        case CHANGED:
            this.type = CollectionEvent.FEATURES_CHANGED;
            break;

        case REMOVED:
            this.type = CollectionEvent.FEATURES_REMOVED;
            break;

        default:
            this.type = CollectionEvent.FEATURES_REMOVED;
        }
        this.features = null;
    }

    /**
     * Constructs a new CollectionEvent.
     *
     * @param source the collection which triggered the event
     * @param involvedFeatures DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public CollectionEvent(FeatureCollection<? extends FeatureType, ? extends Feature> source, SimpleFeature[] involvedFeatures, int type) {
        super(source);
        this.type = type;
        this.features = involvedFeatures;
    }

    /**
     * provides access to the SimpleFeatureCollection which fired the event
     *
     * @return The SimpleFeatureCollection which was the event's source.
     */
    @SuppressWarnings("unchecked")
	public FeatureCollection<? extends FeatureType, ? extends Feature> getCollection() {
        return (FeatureCollection<? extends FeatureType, ? extends Feature>) source;
    }

    /**
     * Provides information on the type of change that has occured. Possible
     * types are: add, remove, change
     *
     * @return an int which must be one of FEATURES_ADDED, FEATURES_REMOVED,
     *         FEATURES_CHANGED
     */
    public int getEventType() {
        return type;
    }

    /**
     * Getter for property features.
     *
     * @return Value of property features.
     */
    public SimpleFeature[] getFeatures() {
        return this.features;
    }
}
