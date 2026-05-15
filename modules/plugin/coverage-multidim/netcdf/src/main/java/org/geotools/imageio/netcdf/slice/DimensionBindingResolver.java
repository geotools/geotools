/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.slice;

import java.util.Locale;
import java.util.Objects;
import org.geotools.coverage.io.CoverageSource;

/**
 * Resolves schema attribute names to logical dimensions (time, elevation, additional).
 *
 * <p>This class centralizes the mapping between feature attributes and dimensions.
 */
public interface DimensionBindingResolver {

    enum Kind {
        TIME,
        ELEVATION,
        ADDITIONAL
    }

    /** Represents a binding between a schema attribute and a NetCDF logical dimension. */
    record Binding(Kind kind, String attributeName, int logicalDimension, CoverageSource.DomainType domainType) {
        public Binding(Kind kind, String attributeName, int logicalDimension, CoverageSource.DomainType domainType) {
            this.kind = Objects.requireNonNull(kind, "kind");
            this.attributeName = attributeName;
            this.logicalDimension = logicalDimension;
            this.domainType = domainType;
        }

        public boolean isTime() {
            return kind == Kind.TIME;
        }

        public boolean isElevation() {
            return kind == Kind.ELEVATION;
        }
    }

    /** Checks if a schema attribute name is recognized by this resolver. */
    default boolean isKnownAttribute(String attributeName) {
        return resolve(attributeName) != null;
    }

    default String normalize(String name) {
        return name == null ? null : name.trim().toUpperCase(Locale.ROOT);
    }

    static String normalizeAttribute(String name) {
        return name == null ? null : name.trim().toUpperCase(Locale.ROOT);
    }

    Binding resolve(String attributeName);
}
