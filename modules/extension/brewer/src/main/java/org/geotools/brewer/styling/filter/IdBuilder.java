/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.brewer.styling.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.geotools.brewer.styling.builder.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;

/** FilterBuilder acting as a simple wrapper around an Expression. */
public class IdBuilder<P> implements Builder<Id> {
    protected Filter filter; // placeholder just to keep us going right now
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
    protected P parent;

    protected boolean unset = false;
    private List<Identifier> ids = new ArrayList<Identifier>();

    public IdBuilder() {
        reset();
    }

    public IdBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    /** Build the expression. */
    public Id build() {
        if (unset) {
            return null;
        }
        return ff.id(new HashSet<Identifier>(ids));
    }

    public IdBuilder<P> fid(String fid) {
        ids.add(ff.featureId(fid));
        return this;
    }

    public IdBuilder<P> featureId(String fid) {
        ids.add(ff.featureId(fid));
        return this;
    }

    public IdBuilder<P> fid(List<String> fids) {
        for (String fid : fids) {
            ids.add(ff.featureId(fid));
        }
        return this;
    }

    public P end() {
        return parent;
    }

    public IdBuilder<P> reset() {
        this.filter = org.opengis.filter.Filter.EXCLUDE;
        this.unset = false;
        return this;
    }

    public IdBuilder<P> reset(Id filter) {
        if (filter == null) {
            return unset();
        }
        this.ids.clear();
        this.ids.addAll(filter.getIdentifiers());
        this.unset = false;
        return this;
    }

    public IdBuilder<P> unset() {
        this.unset = true;
        this.ids.clear();
        return this;
    }
}
