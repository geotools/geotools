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
public class EGeometryValueDWithin extends EObjectAttributeValueCondition {

    public EGeometryValueDWithin(EAttribute eAttribute, Literal geometry, double distance)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.dwithin(eAttribute.getEAttributeType(), geometry, distance));
    }

    public EGeometryValueDWithin(EAttribute eAttribute, Geometry geometry, double distance)
            throws EFeatureEncoderException {
        super(eAttribute, SpatialConditionEncoder.dwithin(eAttribute.getEAttributeType(), geometry, distance));
    }

}
