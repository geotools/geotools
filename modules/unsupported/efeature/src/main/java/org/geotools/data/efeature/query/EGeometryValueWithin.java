package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;

public class EGeometryValueWithin extends EObjectAttributeValueCondition {

    public EGeometryValueWithin(EAttribute eAttribute, Literal geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.within(geometry, swapped));
    }

    public EGeometryValueWithin(EAttribute eAttribute, Geometry geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.within(geometry, swapped));
    }

}
