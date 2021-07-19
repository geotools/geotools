/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.measure;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Objects;
import javax.measure.MetricPrefix;

/**
 * This class contains a unit prefix and allows the definition of additional aliases for this unit
 * prefix.
 */
// class can be a `record` in future versions of Java
public final class PrefixDefinition {

    private final MetricPrefix prefix;
    private final List<String> prefixAliases;

    public static PrefixDefinition of(MetricPrefix prefix, List<String> prefixAlias) {
        return new PrefixDefinition(prefix, prefixAlias);
    }

    public static PrefixDefinition of(MetricPrefix prefix, String... prefixAlias) {
        return new PrefixDefinition(prefix, asList(prefixAlias));
    }

    private PrefixDefinition(MetricPrefix prefix, List<String> prefixAliases) {
        this.prefix = prefix;
        this.prefixAliases = prefixAliases;
    }

    public MetricPrefix getPrefix() {
        return prefix;
    }

    public List<String> getPrefixAliases() {
        return prefixAliases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrefixDefinition that = (PrefixDefinition) o;
        return prefix == that.prefix && Objects.equals(prefixAliases, that.prefixAliases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, prefixAliases);
    }

    @Override
    public String toString() {
        return "PrefixDefinition{" + "prefix=" + prefix + ", prefixAliases=" + prefixAliases + '}';
    }
}
