package org.geotools.measure;

import javax.measure.Unit;

import tech.units.indriya.format.SimpleUnitFormat;

abstract class GeoToolsUnitFormatForwarder extends SimpleUnitFormat {

    /**
     * Base class that includes {@link javax.measure.format.UnitFormat} default instance contents as
     * well as common NonSI units.
     *
     * @author Andrea Aime - GeoSolutions
     */
    protected static class BaseUnitFormat extends DefaultFormat implements UnitFormat {

        public BaseUnitFormat() {
            SimpleUnitFormat.initDefaultFormat(this);
            Units.registerCustomUnits(this);
        }

        @Override
        public void label(Unit<?> unit, String label) {
            super.label(unit, label);
            addUnit(unit);
        }

        /** Defaults to being a no-op, subclasses can override */
        protected void addUnit(Unit<?> unit) {}
    }

}
