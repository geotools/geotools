/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.versioning.decorator;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DecoratingFeature;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

class VersionedFeatureWrapper {

    @SuppressWarnings("unchecked")
    public static <F extends Feature> F wrap(final F f, final String versionId) {
        if (versionId == null) {
            return f;
        }
        if (f instanceof SimpleFeature) {
            return (F) new SimpleFeatureWrapper((SimpleFeature) f, versionId);
        }
        throw new UnsupportedOperationException(
                "Non simple Features are not yet supported: " + f);
    }

    private static final class SimpleFeatureWrapper extends DecoratingFeature
            implements SimpleFeature {
        private static final FilterFactory2 FILTER_FACTORY = CommonFactoryFinder
                .getFilterFactory2(null);

        private final String versionId;

        public SimpleFeatureWrapper(final SimpleFeature delegate,
                final String versionId) {
            super(delegate);
            this.versionId = versionId;
        }

        @Override
        public FeatureId getIdentifier() {
            FeatureId rid = FILTER_FACTORY.featureId(super.getID(), versionId);
            return rid;
        }

        @Override
        public String getID() {
            return super.getID(); // + '@' + versionId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof SimpleFeature)) {
                return false;
            }
            SimpleFeature other = (SimpleFeature) obj;
            if (!getIdentifier().equalsExact(other.getIdentifier())) {
                return false;
            }
            return super.equals(obj);
        }
        
        @Override
        public int hashCode() {
            return getID().hashCode() * versionId.hashCode() * getFeatureType().hashCode();
        }
    }
}