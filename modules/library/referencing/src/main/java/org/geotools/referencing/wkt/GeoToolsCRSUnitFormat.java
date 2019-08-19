/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.wkt;

import javax.measure.format.UnitFormat;
import org.geotools.measure.Units;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.util.GeoToolsUnitFormat;
import org.opengis.metadata.citation.Citation;
import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;

/**
 * Provides unit formatting for EPSG and ESRI WKT dialects
 *
 * @author Andrea Aime - GeoSolutions
 */
abstract class GeoToolsCRSUnitFormat extends GeoToolsUnitFormat {

    /** Holds the standard unit format. */
    private static final ESRIFormat ESRI = new ESRIFormat();

    /** Holds the ASCIIFormat unit format. */
    private static final EPSGFormat EPSG = new EPSGFormat();

    public static UnitFormat getInstance(Citation citation) {
        if (CRS.equalsIgnoreMetadata(Citations.ESRI, citation)) {
            return ESRI;
        } else {
            return EPSG;
        }
    }

    /**
     * Base class that just copies {@link UnitFormat} default instance contents
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class BaseGT2Format extends GeoToolsUnitFormat.BaseGT2Format {

        protected static void esriLabelsAndAliases(BaseGT2Format format) {
            format.label(NonSI.DEGREE_ANGLE, "Degree");
            format.label(SI.METRE, "Meter");
            format.label(SI.METRE.multiply(0.3047997101815088), "Foot_Gold_Coast");
            format.label(USCustomary.FOOT, "Foot");
            format.label(USCustomary.FOOT_SURVEY, "Foot_US");
        }

        protected static void epsgLabelsAndAliases(BaseGT2Format format) {
            format.label(NonSI.DEGREE_ANGLE, "degree");
        }
    }

    /**
     * Subclass adding overrides for the EPSG dialect
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class EPSGFormat extends BaseGT2Format {
        private static final long serialVersionUID = -1207705344688824557L;

        public EPSGFormat() {
            super();
            initUnits(Units.getDefaultFormat());
            epsgLabelsAndAliases(this);
        }
    }

    /**
     * Subclass adding overrides for the ESRI dialect
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class ESRIFormat extends BaseGT2Format {
        private static final long serialVersionUID = 5769662824845469523L;

        public ESRIFormat() {
            super();
            initUnits(Units.getDefaultFormat());
            esriLabelsAndAliases(this);
        }
    }
}
