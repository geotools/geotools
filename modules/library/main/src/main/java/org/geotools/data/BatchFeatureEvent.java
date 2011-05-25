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
package org.geotools.data;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.WeakHashSet;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;

/**
 * Provide batch notification on Commit / Rollback.
 * <p>
 * This is used by FeatureListenerManager to report a bit more detail
 * on transaction commit() and rollback(). Previously these changes
 * were represented as an change event with no known bounds.
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class BatchFeatureEvent extends FeatureEvent {
    private static final long serialVersionUID = 3154238322369916486L;

    public BatchFeatureEvent(FeatureSource<? extends FeatureType, ? extends Feature> featureSource) {
        this(featureSource, new ReferencedEnvelope(), Filter.EXCLUDE);
    }

    public BatchFeatureEvent(FeatureSource<? extends FeatureType, ? extends Feature> featureSource,
            ReferencedEnvelope bounds, Filter filter) {
        super(featureSource, Type.COMMIT, bounds, filter);
    }

    /**
     * This is a weak hash set as we don't need to track
     * changes on FeatureIds that are not being used
     * by the client to track selection.
     */
    @SuppressWarnings("unchecked")
	protected WeakHashSet<Identifier> fids = new WeakHashSet<Identifier>(Identifier.class);

    /**
     * Used to change this into a COMMIT or ROLLBACK if needed.
     * @param type
     */
    public void setType(Type type) {
        this.type = type;
    }
    /**
     * Indicate a change being batched.
     * <p>
     * Will be use to update internal state of bounds and filter; in 
     * the special case of Features being added we will record
     * the FeatureIds in case we need to update them later (this
     * is only required for a *commit* event).
     */
    public void add( FeatureEvent change ){    	
    	if (change.getType() == Type.ADDED) {
            if (change.getFilter() instanceof Id) {
                // store these feature Ids for later
                Id newFeatureIds = (Id) change.getFilter();
                fids.addAll(newFeatureIds.getIdentifiers());
            } else {
                // warning? how can you add features and not know
                // what you are adding?
            }
        }
    	
    	if (filter == Filter.INCLUDE || bounds == ReferencedEnvelope.EVERYTHING) {
            // someone already gave as "generic" something has changed event
            // so we are never going to be able to be more specific
            return;
        }
        if (change.getFilter() == Filter.INCLUDE
                || change.getBounds() == ReferencedEnvelope.EVERYTHING) {
            // something has changed but we are not sure what...
            filter = Filter.INCLUDE;
            bounds = ReferencedEnvelope.EVERYTHING;
            return;
        }
        bounds.expandToInclude(change.getBounds());

        if (filter == Filter.EXCLUDE) {
            // we are just starting out
            filter = change.getFilter();
        } else if (filter instanceof And) {
            And and = (And) filter;
            List<Filter> children = and.getChildren();
            children.add(change.getFilter());
        } else {
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            filter = ff.and(filter, change.getFilter());
        }
    }

    /**
     * Used to update any FeatureId during a commit.
     */
    public void replaceFid(String tempFid, String actualFid) {
        for (Identifier id : fids) {
            if (tempFid.equals(id.getID())) {
                // we have a match!
                if (id instanceof FeatureIdImpl) {
                    FeatureIdImpl featureId = (FeatureIdImpl) id;
                    // update internal structure!
                    featureId.setID(actualFid);
                }
            }
        }
    }
    
    /**
     * This is the set of Identifiers that have been created
     * over the course of this operation.
     * <p>
     * Please note that this is only the set of identifiers that is *still in use*;
     * if no client code is holding on to these Identifiers waiting to see what
     * the final value will be we are not going to hold onto these for you.
     * 
     * @return Set of Identifiers created during this commit
     */
    @SuppressWarnings("unchecked")
    public WeakHashSet<Identifier> getCreatedFeatureIds() {
    	return fids;
    }
}
