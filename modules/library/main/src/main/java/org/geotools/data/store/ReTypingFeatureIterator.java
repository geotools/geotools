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
package org.geotools.data.store;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * FeatureIterator wrapper which re-types features on the fly based on a target feature type.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class ReTypingFeatureIterator implements SimpleFeatureIterator {

    /** The delegate iterator */
    SimpleFeatureIterator delegate;

    /** The target feature type */
    SimpleFeatureType target;

    /** Indexes to delegate attributes */
    final int[] delegateIndexes;

    SimpleFeatureBuilder builder;

    public ReTypingFeatureIterator(
            SimpleFeatureIterator delegate, SimpleFeatureType source, SimpleFeatureType target) {
        this.delegate = delegate;
        this.target = target;
        delegateIndexes = findDelegateIndexes(source, target);
        this.builder = new SimpleFeatureBuilder(target);
    }

    public SimpleFeatureIterator getDelegate() {
        return delegate;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public SimpleFeature next() {
        SimpleFeature next = delegate.next();
        String id = next.getID();

        try {
            for (int idx : delegateIndexes) {
                builder.add(next.getAttribute(idx));
            }
            builder.featureUserData(next);

            return builder.buildFeature(id);
        } catch (IllegalAttributeException e) {
            throw new RuntimeException("Couldn't retype feature with id: " + id, e);
        }
    }

    /**
     * Supplies mapping from original to target FeatureType.
     *
     * <p>Will also ensure that original can cover target
     *
     * @param target Desired FeatureType
     * @param original Original FeatureType
     * @return Mapping from original to target FeatureType
     * @throws IllegalArgumentException if unable to provide a mapping
     */
    private int[] findDelegateIndexes(SimpleFeatureType original, SimpleFeatureType target) {
        if (target.equals(original)) {
            throw new IllegalArgumentException(
                    "FeatureReader allready produces contents with the correct schema");
        }

        if (target.getAttributeCount() > original.getAttributeCount()) {
            throw new IllegalArgumentException(
                    "Unable to retype "
                            + original.getName()
                            + " (original does not cover target type)");
        }

        String xpath;
        int[] indexes = new int[target.getAttributeCount()];

        for (int i = 0; i < target.getAttributeCount(); i++) {
            AttributeDescriptor attrib = target.getDescriptor(i);
            xpath = attrib.getLocalName();

            AttributeDescriptor origAttrib = original.getDescriptor(xpath);
            if (origAttrib instanceof GeometryDescriptor) {
                if (!(attrib instanceof GeometryDescriptor)) {
                    throw new IllegalArgumentException(
                            "Unable to retype "
                                    + origAttrib.getLocalName()
                                    + " (target isn't a geometry attribute).");
                }
                CoordinateReferenceSystem origCrs =
                        ((GeometryDescriptor) origAttrib).getCoordinateReferenceSystem();
                if (origCrs != null
                        && !origCrs.equals(
                                ((GeometryDescriptor) attrib).getCoordinateReferenceSystem())) {
                    throw new IllegalArgumentException(
                            "Unable to retype "
                                    + original.getName()
                                    + " (target have a different crs).");
                }
            }
            if (origAttrib == null
                    || !attrib.getType()
                            .getBinding()
                            .isAssignableFrom(origAttrib.getType().getBinding())) {
                throw new IllegalArgumentException(
                        "Unable to retype "
                                + original.getName()
                                + " (original does not cover "
                                + xpath
                                + ")");
            }
            indexes[i] = original.indexOf(origAttrib.getName());
        }

        return indexes;
    }

    @Override
    public void close() {
        delegate.close();
    }
}
