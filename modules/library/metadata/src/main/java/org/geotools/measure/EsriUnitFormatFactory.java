package org.geotools.measure;

import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;
import tech.units.indriya.format.SimpleUnitFormat;

public final class EsriUnitFormatFactory {

    public static UnitFormat getInstance() {
        return INSTANCE;
    }

    public static SimpleUnitFormat create() {
        return new EsriUnitFormat();
    }

    private EsriUnitFormatFactory() {}

    private static final EsriUnitFormat INSTANCE = new EsriUnitFormat();

    /**
     * Subclass adding overrides for the ESRI dialect
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class EsriUnitFormat extends GeoToolsUnitFormatForwarder.BaseUnitFormat implements UnitFormat {
        private static final long serialVersionUID = 5769662824845469523L;

        public EsriUnitFormat() {
            addEsriLabelsAndAliases(this);
        }
    }

    static void addEsriLabelsAndAliases(GeoToolsUnitFormatForwarder.BaseUnitFormat format) {
        format.label(NonSI.DEGREE_ANGLE, "Degree");
        format.label(SI.METRE, "Meter");
        format.label(SI.METRE.multiply(0.3047997101815088), "Foot_Gold_Coast");
        format.alias(USCustomary.FOOT, "Foot");
        format.label(USCustomary.FOOT_SURVEY, "Foot_US");
    }

}
