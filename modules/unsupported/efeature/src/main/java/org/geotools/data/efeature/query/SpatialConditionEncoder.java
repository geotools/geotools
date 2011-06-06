package org.geotools.data.efeature.query;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;

import org.eclipse.emf.query.conditions.Condition;
import org.geotools.data.efeature.DataTypes;
import org.geotools.resources.CRSUtilities;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.distance.DistanceOp;

public class SpatialConditionEncoder {

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------

    public static Unit<?> getUnit(GeometryDescriptor descriptor) {
        CoordinateReferenceSystem crs = descriptor.getCoordinateReferenceSystem();
        return CRSUtilities.getUnit(crs.getCoordinateSystem());
    }

    public static double convert(DistanceBufferOperator filter, GeometryDescriptor descriptor) {
        double distance = filter.getDistance();
        String unit = filter.getDistanceUnits();
        return convert(descriptor, distance, unit);
    }

    public static double convert(GeometryDescriptor descriptor, double distance, String unit) {
        Unit<?> fromUnit = Unit.valueOf(unit);
        Unit<?> toUnit = getUnit(descriptor);
        UnitConverter c = fromUnit.getConverterTo(toUnit);
        return c.convert(distance);

    }

    // ----------------------------------------------------- 
    //  Spatial relation comparison methods
    // -----------------------------------------------------

    public static Condition bbox(Literal value, boolean swapped) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return bbox((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition bbox(Geometry value, final boolean swapped) {
        final Envelope bbox = value.getEnvelopeInternal();
        if (bbox.isNull()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Envelope extent = ((Geometry) object).getEnvelopeInternal();
                    if (swapped) {
                        return extent.intersects(bbox);
                    }
                    return bbox.intersects(extent);
                }
                return false;
            }
        };
    }

    public static Condition beyond(Literal value, double distance) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            beyond((Geometry) v, distance);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition beyond(final Geometry value, final double distance) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }

        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    return !DistanceOp.isWithinDistance(value, (Geometry) object, distance);
                }
                return false;
            }
        };
    }

    public static Condition dwithin(Literal value, double distance) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            dwithin((Geometry) v, distance);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition dwithin(final Geometry value, final double distance) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }

        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    return DistanceOp.isWithinDistance(value, (Geometry) object, distance);
                }
                return false;
            }
        };
    }

    public static Condition contains(Literal value, boolean swapped)
            throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return contains((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition contains(final Geometry value, final boolean swapped) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Geometry geom = (Geometry) object;
                    if (swapped) {
                        return geom.intersects(value);
                    }
                    return value.contains(geom);
                }
                return false;
            }
        };
    }

    public static Condition crosses(Literal value, boolean swapped) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return crosses((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition crosses(final Geometry value, final boolean swapped) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Geometry geom = (Geometry) object;
                    if (swapped) {
                        return geom.crosses(value);
                    }
                    return value.crosses(geom);
                }
                return false;
            }
        };
    }

    public static Condition disjoint(Literal value, boolean swapped)
            throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return disjoint((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition disjoint(final Geometry value, final boolean swapped) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Geometry geom = (Geometry) object;
                    if (swapped) {
                        return geom.disjoint(value);
                    }
                    return value.disjoint(geom);
                }
                return false;
            }
        };
    }

    public static Condition equals(Literal value, boolean swapped) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return equals((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition equals(final Geometry value, final boolean swapped) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Geometry geom = (Geometry) object;
                    if (swapped) {
                        return geom.equals(value);
                    }
                    return value.equals(geom);
                }
                return false;
            }
        };
    }

    public static Condition intersects(Literal value, boolean swapped)
            throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return intersects((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition intersects(final Geometry value, final boolean swapped) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Geometry geom = (Geometry) object;
                    if (swapped) {
                        return geom.intersects(value);
                    }
                    return value.intersects(geom);
                }
                return false;
            }
        };
    }

    public static Condition overlaps(Literal value, boolean swapped)
            throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return overlaps((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition overlaps(final Geometry value, final boolean swapped) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Geometry geom = (Geometry) object;
                    if (swapped) {
                        return geom.overlaps(value);
                    }
                    return value.overlaps(geom);
                }
                return false;
            }
        };
    }

    public static Condition touches(Literal value, boolean swapped) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return touches((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition touches(final Geometry value, final boolean swapped) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Geometry geom = (Geometry) object;
                    if (swapped) {
                        return geom.touches(value);
                    }
                    return value.touches(geom);
                }
                return false;
            }
        };
    }

    public static Condition within(Literal value, boolean swapped) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isGeometry(v)) {
            return within((Geometry) v, swapped);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition within(final Geometry value, final boolean swapped) {
        if (value.isEmpty()) {
            return Condition.FALSE;
        }
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                if (object instanceof Geometry) {
                    Geometry geom = (Geometry) object;
                    if (swapped) {
                        return geom.within(value);
                    }
                    return value.within(geom);
                }
                return false;
            }
        };
    }

}
