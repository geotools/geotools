package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;

public class EGeometryValueTouches extends EObjectAttributeValueCondition {

    public EGeometryValueTouches(EAttribute eAttribute, Literal geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.touches(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueTouches(EAttribute eAttribute, Object geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.touches(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueTouches(EAttribute eAttribute, Geometry geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.touches(eAttribute.getEAttributeType(), geometry, swapped));
    }

}
