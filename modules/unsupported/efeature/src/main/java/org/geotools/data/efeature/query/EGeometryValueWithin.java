package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 *
 * @source $URL$
 */
public class EGeometryValueWithin extends EObjectAttributeValueCondition {

    public EGeometryValueWithin(EAttribute eAttribute, Literal geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.within(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueWithin(EAttribute eAttribute, Object geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.within(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueWithin(EAttribute eAttribute, Geometry geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.within(eAttribute.getEAttributeType(), geometry, swapped));
    }

}
