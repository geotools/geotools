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
public class EGeometryValueOverlaps extends EObjectAttributeValueCondition {

    public EGeometryValueOverlaps(EAttribute eAttribute, Literal geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.overlaps(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueOverlaps(EAttribute eAttribute, Object geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.overlaps(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueOverlaps(EAttribute eAttribute, Geometry geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.overlaps(eAttribute.getEAttributeType(), geometry, swapped));
    }

}
