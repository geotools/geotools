/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.geotools.styling.css.util.UnboundSimplifyingFilterVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;

/**
 * A simplifying filter visitor that caches the results, to avoid repeating their computation over
 * and over
 *
 * @author Andrea Aime - GeoSolutions
 */
class CachedSimplifyingFilterVisitor extends UnboundSimplifyingFilterVisitor {
    // filters we know are already simplified
    Map<Filter, Filter> cache = new WeakHashMap<Filter, Filter>();

    public CachedSimplifyingFilterVisitor(FeatureType ft) {
        setFeatureType(ft);
        setRangeSimplicationEnabled(true);
    }

    @Override
    public Object visit(And filter, Object extraData) {
        Filter result = cache.get(filter);
        if (result == null) {
            result = (Filter) super.visit(filter, extraData);
            cache.put(filter, result);
        }
        return result;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        Filter result = cache.get(filter);
        if (result == null) {
            result = (Filter) super.visit(filter, extraData);
            cache.put(filter, result);
        }

        return result;
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        Filter result = cache.get(filter);
        if (result == null) {
            result = (Filter) super.visit(filter, extraData);
            cache.put(filter, result);
        }

        return result;
    }

    protected List<Filter> extraAndSimplification(Object extraData, List<Filter> filters) {
        if (filters.size() > 1) {
            // if there are nested ors and top level filters, try factoring out common expression,
            // e.g., (A | B) & A -> A
            Set<Filter> topLevel = new HashSet<>();
            for (Filter filter : filters) {
                if (!(filter instanceof Or)) {
                    topLevel.add(filter);
                }
            }
            for (int i = 0; i < filters.size(); ) {
                Filter f = filters.get(i);
                boolean skip = false;
                if (f instanceof Or) {
                    Or or = ((Or) f);
                    for (Filter child : or.getChildren()) {
                        if (topLevel.contains(child)) {
                            skip = true;
                            break;
                        }
                    }
                }
                if (skip) {
                    filters.remove(i);
                } else {
                    i++;
                }
            }
        }
        // if there are nested Ors, try distribution, see if this helps reduce the
        // overall expression
        if (filters.size() > 1) {
            for (int i = 0; i < filters.size(); i++) {
                Filter f = filters.get(i);
                if (f instanceof Or) {
                    Or or = ((Or) f);
                    Filter reduced = null;
                    boolean twoOrMore = false;
                    for (Filter child : or.getChildren()) {
                        List<Filter> newList = new ArrayList<>(filters);
                        newList.remove(or);
                        newList.add(child);
                        And and = getFactory(extraData).and(newList);
                        Filter simplified = (Filter) and.accept(this, extraData);
                        if (simplified == Filter.EXCLUDE) {
                            continue;
                        } else if (simplified == Filter.INCLUDE) {
                            return Collections.singletonList((Filter) Filter.INCLUDE);
                        } else if (reduced == null) {
                            reduced = simplified;
                        } else if (!simplified.equals(reduced)) {
                            twoOrMore = true;
                            break;
                        }
                    }

                    if (reduced == null) {
                        return Collections.singletonList((Filter) Filter.EXCLUDE);
                    } else if (!twoOrMore) {
                        filters.clear();
                        if (!(reduced instanceof And)) {
                            return Collections.singletonList(reduced);
                        } else {
                            filters.addAll(((And) reduced).getChildren());
                            filters = basicAndSimplification(filters);
                            // this assumes we'll never stumble into a single children "or",
                            // because those are simplified out at the beginning of this procedure
                            i = 0;
                        }
                    }
                }
            }
        }
        return filters;
    }

    protected List<Filter> extraOrSimplification(Object extraData, List<Filter> filters) {
        if (filters.size() > 1) {
            // if there are nested ands and top level filters, try factoring out common expression,
            // e.g., (A & B) | A -> A
            Set<Filter> topLevel = new HashSet<>();
            for (Filter filter : filters) {
                if (!(filter instanceof And)) {
                    topLevel.add(filter);
                }
            }
            for (int i = 0; i < filters.size(); ) {
                Filter f = filters.get(i);
                boolean skip = false;
                if (f instanceof And) {
                    And and = ((And) f);
                    for (Filter child : and.getChildren()) {
                        if (topLevel.contains(child)) {
                            skip = true;
                            break;
                        }
                    }
                }
                if (skip) {
                    filters.remove(i);
                } else {
                    i++;
                }
            }
        }
        if (filters.size() > 1) {
            // if there are nested Ands, try distribution, see if this helps reduce the
            // overall expression
            for (int i = 0; i < filters.size(); i++) {
                Filter f = filters.get(i);
                if (f instanceof And) {
                    And and = ((And) f);
                    Filter reduced = null;
                    boolean twoOrMore = false;
                    for (Filter child : and.getChildren()) {
                        List<Filter> newList = new ArrayList<>(filters);
                        newList.remove(and);
                        newList.add(child);
                        Or or = getFactory(extraData).or(newList);
                        Filter simplified = (Filter) or.accept(this, extraData);
                        if (simplified == Filter.EXCLUDE) {
                            return Collections.singletonList((Filter) Filter.EXCLUDE);
                        } else if (simplified == Filter.INCLUDE) {
                            continue;
                        } else if (reduced == null) {
                            reduced = simplified;
                        } else if (!simplified.equals(reduced)) {
                            twoOrMore = true;
                            break;
                        }
                    }

                    if (reduced == null) {
                        return Collections.singletonList((Filter) Filter.INCLUDE);
                    } else if (!twoOrMore) {
                        filters.clear();
                        if (!(reduced instanceof Or)) {
                            return Collections.singletonList(reduced);
                        } else {
                            filters.addAll(((Or) reduced).getChildren());
                            filters = basicOrSimplification(filters);
                            // this assumes we'll never stumble into a single children "or",
                            // because those are simplified out at the beginning of this procedure
                            i = 0;
                        }
                    }
                }
            }
        }
        return filters;
    }
}
