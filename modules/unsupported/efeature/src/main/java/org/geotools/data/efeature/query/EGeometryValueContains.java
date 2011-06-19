package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;

public class EGeometryValueContains extends EObjectAttributeValueCondition {

    public EGeometryValueContains(EAttribute eAttribute, Literal geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.contains(eAttribute.getEAttributeType(), geometry, swapped));
    }

    public EGeometryValueContains(EAttribute eAttribute, Object geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.contains(eAttribute.getEAttributeType(), geometry, swapped));
    }
    
    public EGeometryValueContains(EAttribute eAttribute, Geometry geometry, boolean swapped)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.contains(eAttribute.getEAttributeType(), geometry, swapped));
    }

}
