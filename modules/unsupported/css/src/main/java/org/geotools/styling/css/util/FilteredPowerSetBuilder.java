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
package org.geotools.styling.css.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base class to build a power set from a set of object, filtering it during construction to avoid
 * trying sub-trees that lead to no results
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 * @param <T> The type of the domain elements
 * @param <R> The type of the power set elements (combinations might generate a new type of object)
 */
public abstract class FilteredPowerSetBuilder<T, R> {

    /**
     * The original list of values from which we'll build the power set
     */
    private List<T> domain;

    /**
     * Signatures that have been rejected, that we already know won't generate an entry in the
     * result
     */
    private Set<Signature> rejects = new HashSet<>();

    /**
     * Initializes the power set builds with the initial domain values
     * 
     * @param domain
     */
    public FilteredPowerSetBuilder(List<T> domain) {
        this.domain = domain;
    }

    /**
     * See if a certain signature matches an already rejected signature
     * 
     * @param s
     * @param k
     * @return
     */
    private boolean rejected(Signature s, int k) {
        // see if rejected already
        for (Signature reject : rejects) {
            if (s.contains(reject, k)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Builds the power set
     * 
     * @return
     */
    public List<R> buildPowerSet() {
        List<R> result = new ArrayList<>();
        Signature s = Signature.newSignature(domain.size());
        fill(s, 0, domain.size(), result);
        result = postFilterResult(result);
        return result;
    }

    /**
     * Allows subclasses to filter the results after they have been built
     * 
     * @param result
     * @return
     */
    protected List<R> postFilterResult(List<R> result) {
        return result;
    }

    /**
     * Recursively builds all possible signatures in the domain (will stop immediately if a
     * signature is not accepted, or builds on top of a already rejected signature)
     * 
     * @param s
     * @param k
     * @param n
     * @param result
     */
    void fill(Signature s, int k, int n, List<R> result) {
        List<T> objects = listFromSignature(s);
        if (!objects.isEmpty()) {
            if (!accept(objects)) {
                rejects.add((Signature) s.clone());
                return;
            }
        }

        if (k == n) {
            List<R> combined = buildResult(objects);
            if (combined != null) {
                result.addAll(combined);
            }
        } else {
            s.set(k, true);
            if (!rejected(s, k)) {
                fill(s, k + 1, n, result);
            }
            s.set(k, false);
            if (!rejected(s, k)) {
                fill(s, k + 1, n, result);
            }
        }
    }

    /**
     * Builds a result from a combination of input objects. The method can return null to identify a
     * combination that does not generate anything useful, but whose set of object could still
     * generate a valid combination when grown with more objects (thus, not a candidate for
     * returning false in {@link #accept(List)})
     * 
     * @param objects
     * @return
     */
    protected abstract List<R> buildResult(List<T> objects);

    /**
     * Checks if a certain list of objects should be accepted, or not. If rejected, a signature will
     * be built from this set, and any superset of these objects will also be rejected
     * 
     * @param set
     * @return
     */
    protected abstract boolean accept(List<T> set);

    /**
     * Returns the list of values associated to this signature
     * 
     * @param signature
     * @return
     */
    private List<T> listFromSignature(Signature signature) {
        List<T> test = new ArrayList<>();
        for (int i = 0; i < domain.size(); i++) {
            if (signature.get(i)) {
                test.add(domain.get(i));
            }
        }
        return test;
    }

}
