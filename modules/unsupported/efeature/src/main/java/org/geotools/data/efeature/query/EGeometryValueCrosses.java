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
public class EGeometryValueCrosses extends EObjectAttributeValueCondition {

    public EGeometryValueCrosses(EAttribute eAttribute, Literal geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.crosses(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueCrosses(EAttribute eAttribute, Object geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.crosses(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueCrosses(EAttribute eAttribute, Geometry geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.crosses(eAttribute.getEAttributeType(), geometry, swapped));
    }

}
