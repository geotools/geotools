package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;

public class EGeometryValueIntersects extends EObjectAttributeValueCondition {

    public EGeometryValueIntersects(EAttribute eAttribute, Literal geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.intersects(geometry, swapped));
    }

    public EGeometryValueIntersects(EAttribute eAttribute, Geometry geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.intersects(geometry, swapped));
    }

}
