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
package org.geotools.filter;

// Geotools dependencies

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.SimpleFeaturePropertyAccessorFactory;
import org.geotools.filter.identity.FeatureIdImpl;

/**
 * Defines a ID filter, which holds a list of IDs ( usually feature id;s ). This filter stores a series of IDs, which
 * are used to distinguish features uniquely.
 *
 * <p>Please note that addAllFids( Collection ) may be a performance hog; uDig makes use of its own implementation of
 * FidFilter in order to reuse the internal set of fids between uses.
 *
 * @author Rob Hranac, TOPP
 * @author Justin Deoliveira, TOPP
 *     <p>TODO: this class shoul be renamed to IdFilterImpl
 * @version $Id$
 */
public class FidFilterImpl extends AbstractFilter implements Id {
    /** Logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FidFilterImpl.class);

    /** List of the Identifer. */
    private Set<Identifier> fids = new LinkedHashSet<>();

    private Set<String> ids = new LinkedHashSet<>();

    /** Constructor which takes {@link org.geotools.api.filter.identity.Identifier}, not String. */
    protected FidFilterImpl(Set<? extends Identifier> fids) {
        // check these are really identifiers
        for (Object next : fids) {
            if (!(next instanceof Identifier))
                throw new ClassCastException("Fids must implement Identifier, " + next.getClass() + " does not");
        }
        this.fids = new LinkedHashSet<>(fids);
        for (Identifier identifier : this.fids) {
            ids.add(identifier.getID().toString());
        }
    }

    /** @see org.geotools.api.filter.Id#getIDs() */
    @Override
    public Set<Object> getIDs() {
        return new HashSet<>(getFidsSet());
    }

    /** @see org.geotools.api.filter.Id#getIdentifiers() */
    @Override
    public Set<Identifier> getIdentifiers() {
        return fids;
    }

    /** @see org.geotools.api.filter.identity.FeatureId#setIDs(Set) */
    public void setIDs(Set ids) {
        fids = new HashSet<>();
        addAllFids(ids);
    }

    /**
     * Accessor method for fid set as Strings.
     *
     * @return the internally stored fids.
     */
    public Set<String> getFidsSet() {
        return fids();
    }

    /** Helper method to pull out strings from featureId set. */
    private Set<String> fids() {
        return new HashSet<>(ids);
    }

    /**
     * Adds a feature ID to the filter.
     *
     * @param fid A single feature ID.
     */
    public final void addFid(String fid) {
        LOGGER.finest("got fid: " + fid);
        fids.add(new FeatureIdImpl(fid));
        ids.add(fid);
    }

    /**
     * Adds a collection of feature IDs to the filter.
     *
     * @param fidsToAdd A collection of feature IDs as strings.
     */
    public void addAllFids(Collection fidsToAdd) {
        if (fidsToAdd == null) return;

        for (Object o : fidsToAdd) {
            String fid = (String) o;
            addFid(fid);
        }
    }

    /**
     * Removes a feature ID from the filter.
     *
     * @param fid A single feature ID.
     */
    public final void removeFid(String fid) {
        if (fid == null) {
            return;
        }

        for (Iterator f = fids.iterator(); f.hasNext(); ) {
            Identifier featureId = (Identifier) f.next();
            if (fid.equals(featureId.getID().toString())) {
                f.remove();
                ids.remove(fid);
            }
        }
    }

    /**
     * Removes a collection of feature IDs from the filter.
     *
     * @param fidsToRemove A collection of feature IDs.
     */
    public void removeAllFids(Collection fidsToRemove) {
        if (fidsToRemove == null) return;

        for (Object o : fidsToRemove) {
            String fid = (String) o;
            removeFid(fid);
        }
    }

    /**
     * Determines whether or not the given feature's ID matches this filter.
     *
     * <p>In order to get the object's ID, the {@link PropertyAccessor} capable of dealing with <code>feature</code> has
     * to support the request of the expression <code>"@id"</code>
     *
     * @param feature Specified feature to examine.
     * @return <tt>true</tt> if the feature's ID matches an fid held by this filter, <tt>false</tt> otherwise.
     * @see SimpleFeaturePropertyAccessorFactory
     */
    @Override
    public boolean evaluate(Object feature) {
        if (feature == null) {
            return false;
        }

        // NC - updated, using attributeexpressionimpl will be easiest, don't have to copy and paste
        // lots of code
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        String evaluate = ff.property("@id").evaluate(feature, String.class);
        if (evaluate == null) {
            return false;
        } else {
            return ids.contains(evaluate);
        }
    }

    /**
     * Returns a string representation of this filter.
     *
     * @return String representation of the compare filter.
     */
    @Override
    public String toString() {
        StringBuffer fidFilter = new StringBuffer();

        Iterator fidIterator = fids.iterator();

        while (fidIterator.hasNext()) {
            fidFilter.append(fidIterator.next().toString());

            if (fidIterator.hasNext()) {
                fidFilter.append(", ");
            }
        }

        return "[ " + fidFilter.toString() + " ]";
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance. Typicaly used by Filter decoders, but may
     * also be used by any thing which needs infomration from filter structure. Implementations should always call:
     * visitor.visit(this); It is importatant that this is not left to a parent class unless the parents API is
     * identical.
     *
     * @param visitor The visitor which requires access to this filter, the method must call visitor.visit(this);
     */
    @Override
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    /**
     * Returns a flag indicating object equality.
     *
     * @param filter the filter to test equality on.
     * @return String representation of the compare filter.
     */
    @Override
    public boolean equals(Object filter) {
        LOGGER.finest("condition: " + filter);

        if (filter != null && filter.getClass() == this.getClass()) {
            FidFilterImpl other = (FidFilterImpl) filter;
            return fids.equals(other.fids);
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return a hash code value for this fid filter object.
     */
    @Override
    public int hashCode() {
        return fids.hashCode();
    }
}
