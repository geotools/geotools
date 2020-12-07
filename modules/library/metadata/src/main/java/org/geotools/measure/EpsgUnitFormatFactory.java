package org.geotools.measure;

import si.uom.NonSI;
import tech.units.indriya.format.SimpleUnitFormat;

public final class EpsgUnitFormatFactory {

    public static UnitFormat getInstance() {
        return INSTANCE;
    }

    public static SimpleUnitFormat create() {
        return new EpsgUnitFormat();
    }

    private EpsgUnitFormatFactory() {}

    private static final EpsgUnitFormat INSTANCE = new EpsgUnitFormat();

    /**
     * Subclass adding overrides for the EPSG dialect
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class EpsgUnitFormat extends GeoToolsUnitFormatForwarder.BaseUnitFormat implements UnitFormat {
        private static final long serialVersionUID = -1207705344688824557L;

        public EpsgUnitFormat() {
            addEpsgLabelsAndAliases(this);
        }
    }

    static void addEpsgLabelsAndAliases(GeoToolsUnitFormatForwarder.BaseUnitFormat format) {
        format.label(NonSI.DEGREE_ANGLE, "degree");
    }

}
