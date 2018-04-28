/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic.namecollector;

import java.util.Map;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.gce.imagemosaic.Utils;

/** A Default {@link CoverageNameCollectorSPI} implementation */
public class DefaultCoverageNameCollectorSPI implements CoverageNameCollectorSPI {

    public CoverageNameCollector create(Object object, Map<String, String> properties) {
        // Default collector ignores the properties
        if (object instanceof StructuredGridCoverage2DReader) {
            return new DefaultStructuredCoverageNameCollector();
        } else if (object instanceof GridCoverage2DReader) {
            return new DefaultCoverageNameCollector();
        }
        throw new IllegalArgumentException();
    }

    /**
     * The Default {@link CoverageNameCollector} for StructuredGridCoverage readers simply returns
     * the coverageName provided as input of the call (put into the properties map to be handled)
     */
    static class DefaultStructuredCoverageNameCollector extends DefaultCoverageNameCollector {

        public DefaultStructuredCoverageNameCollector() {
            super(Utils.Prop.INPUT_COVERAGE_NAME);
        }
    }

    /**
     * The Default {@link CoverageNameCollector} for classic GridCoverage readers simply returns the
     * value of the properties map matching the required coverageNameMappingKey
     */
    static class DefaultCoverageNameCollector implements CoverageNameCollector {

        protected String coverageNameMappingKey;

        public DefaultCoverageNameCollector() {
            this(Utils.Prop.INDEX_NAME);
        }

        protected DefaultCoverageNameCollector(String coverageNameMappingKey) {
            this.coverageNameMappingKey = coverageNameMappingKey;
        }

        @Override
        public String getName(GridCoverage2DReader reader, Map<String, String> map) {
            if (map != null && map.containsKey(coverageNameMappingKey)) {
                return map.get(coverageNameMappingKey);
            }
            throw new IllegalArgumentException("Unable to retrieve the coverageName");
        }
    }
}
