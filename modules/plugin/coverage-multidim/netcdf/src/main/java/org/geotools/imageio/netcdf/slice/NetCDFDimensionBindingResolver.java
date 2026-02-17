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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.coverage.io.CoverageSource.DomainType;
import org.geotools.imageio.netcdf.VariableAdapter;
import org.geotools.imageio.netcdf.slice.NetCDFSliceProvider.AdditionalDomainBinding;

/**
 * Resolves schema attribute names to NetCDF logical dimensions (time, elevation, additional).
 *
 * <p>This class centralizes the mapping between feature attributes and NetCDF dimensions.
 */
public final class NetCDFDimensionBindingResolver implements DimensionBindingResolver {

    private final Map<String, Binding> bindingsByAttribute;

    /** Creates a resolver for the given variable adapter and dimension bindings. */
    public NetCDFDimensionBindingResolver(
            VariableAdapter adapter,
            String timeAttribute,
            String elevationAttribute,
            List<AdditionalDomainBinding> additionalBindings) {

        Map<String, Binding> map = new LinkedHashMap<>();

        if (adapter.getNDimensionIndex(VariableAdapter.T) >= 0) {
            put(
                    map,
                    timeAttribute,
                    new Binding(Kind.TIME, normalize(timeAttribute), VariableAdapter.T, DomainType.DATE));
        }

        if (adapter.getNDimensionIndex(VariableAdapter.Z) >= 0) {
            put(
                    map,
                    elevationAttribute,
                    new Binding(Kind.ELEVATION, normalize(elevationAttribute), VariableAdapter.Z, DomainType.NUMBER));
        }

        if (additionalBindings != null) {
            for (AdditionalDomainBinding binding : additionalBindings) {
                if (binding == null) {
                    continue;
                }
                if (adapter.getNDimensionIndex(binding.logicalDimension()) < 0) {
                    continue;
                }
                put(
                        map,
                        binding.attributeName(),
                        new Binding(
                                Kind.ADDITIONAL,
                                normalize(binding.attributeName()),
                                binding.logicalDimension(),
                                binding.type()));
            }
        }

        this.bindingsByAttribute = Map.copyOf(map);
    }

    /** Resolves a schema attribute name to its corresponding binding, or null if unknown. */
    @Override
    public Binding resolve(String attributeName) {
        return bindingsByAttribute.get(normalize(attributeName));
    }

    private void put(Map<String, Binding> map, String attributeName, Binding binding) {
        String normalized = normalize(attributeName);
        if (normalized != null) {
            map.put(normalized, binding);
        }
    }
}
