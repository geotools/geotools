package org.geotools.data.efeature.query;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.query.conditions.Condition;
import org.geotools.data.efeature.DataBuilder;
import org.geotools.data.efeature.DataTypes;
import org.geotools.resources.CRSUtilities;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.distance.DistanceOp;

/**
 * 
 *
 * @source $URL$
 */
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
    
    public static Condition bbox(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return bbox((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition bbox(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return bbox((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition bbox(Literal value, boolean swapped) throws EFeatureEncoderException {
        return bbox(value.getValue(),swapped);
    }

    public static Condition bbox(Object value, boolean swapped) throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return bbox((Geometry) value, swapped);
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

    public static Condition beyond(EDataType type, Literal value, double distance) throws EFeatureEncoderException {
        return beyond((Geometry)DataBuilder.toValue(type, value),distance);
    }

    public static Condition beyond(EDataType type, Object value, double distance) throws EFeatureEncoderException {
        return beyond((Geometry)DataBuilder.toValue(type, value),distance);
    }
    
    public static Condition beyond(Literal value, double distance) throws EFeatureEncoderException {
        return beyond(value.getValue(),distance);
    }
    
    public static Condition beyond(Object value, double distance) throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            beyond((Geometry) value, distance);
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

    public static Condition dwithin(EDataType type, Literal value, double distance) throws EFeatureEncoderException {
        return dwithin((Geometry)DataBuilder.toValue(type, value),distance);
    }

    public static Condition dwithin(EDataType type, Object value, double distance) throws EFeatureEncoderException {
        return dwithin((Geometry)DataBuilder.toValue(type, value),distance);
    }
    
    public static Condition dwithin(Literal value, double distance) throws EFeatureEncoderException {
        return dwithin(value.getValue(),distance);
    }
    
    public static Condition dwithin(Object value, double distance) throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            dwithin((Geometry) value, distance);
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

    public static Condition contains(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return contains((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition contains(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return contains((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition contains(Literal value, boolean swapped) throws EFeatureEncoderException {
        return contains(value.getValue(),swapped);
    }
    
    public static Condition contains(Object value, boolean swapped)
            throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return contains((Geometry) value, swapped);
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
                        return geom.contains(value);
                    }
                    return value.contains(geom);
                }
                return false;
            }
        };
    }

    public static Condition crosses(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return crosses((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition crosses(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return crosses((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition crosses(Literal value, boolean swapped) throws EFeatureEncoderException {
        return crosses(value.getValue(),swapped);
    }
       
    public static Condition crosses(Object value, boolean swapped) throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return crosses((Geometry) value, swapped);
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

    public static Condition disjoint(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return disjoint((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition disjoint(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return disjoint((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition disjoint(Literal value, boolean swapped) throws EFeatureEncoderException {
        return disjoint(value.getValue(),swapped);
    }
           
    public static Condition disjoint(Object value, boolean swapped)
            throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return disjoint((Geometry) value, swapped);
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

    public static Condition equals(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return equals((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition equals(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return equals((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition equals(Literal value, boolean swapped) throws EFeatureEncoderException {
        return equals(value.getValue(),swapped);
    }
               
    public static Condition equals(Object value, boolean swapped) throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return equals((Geometry) value, swapped);
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

    public static Condition intersects(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return intersects((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition intersects(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return intersects((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition intersects(Literal value, boolean swapped) throws EFeatureEncoderException {
        return intersects(value.getValue(),swapped);
    }
                   
    public static Condition intersects(Object value, boolean swapped)
            throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return intersects((Geometry) value, swapped);
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

    public static Condition overlaps(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return overlaps((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition overlaps(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return overlaps((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition overlaps(Literal value, boolean swapped) throws EFeatureEncoderException {
        return overlaps(value.getValue(),swapped);
    }
    
    public static Condition overlaps(Object value, boolean swapped)
            throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return overlaps((Geometry) value, swapped);
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

    public static Condition touches(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return touches((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition touches(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return touches((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition touches(Literal value, boolean swapped) throws EFeatureEncoderException {
        return touches(value.getValue(),swapped);
    }
    
    public static Condition touches(Object value, boolean swapped) throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return touches((Geometry) value, swapped);
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

    public static Condition within(EDataType type, Literal value, boolean swapped) throws EFeatureEncoderException {
        return within((Geometry)DataBuilder.toValue(type, value),swapped);
    }

    public static Condition within(EDataType type, Object value, boolean swapped) throws EFeatureEncoderException {
        return within((Geometry)DataBuilder.toValue(type, value),swapped);
    }
    
    public static Condition within(Literal value, boolean swapped) throws EFeatureEncoderException {
        return within(value.getValue(),swapped);
    }
    
    public static Condition within(Object value, boolean swapped) throws EFeatureEncoderException {
        if (DataTypes.isGeometry(value)) {
            return within((Geometry) value, swapped);
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
