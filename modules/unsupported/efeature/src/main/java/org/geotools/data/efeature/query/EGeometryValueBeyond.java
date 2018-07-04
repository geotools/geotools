package org.geotools.data.efeature.query;

import org.locationtech.jts.geom.Geometry;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

/** @source $URL$ */
public class EGeometryValueBeyond extends EObjectAttributeValueCondition {

    public EGeometryValueBeyond(EAttribute eAttribute, Literal geometry, double distance)
            throws EFeatureEncoderException {
        super(
                eAttribute,
                SpatialConditionEncoder.beyond(eAttribute.getEAttributeType(), geometry, distance));
    }

    public EGeometryValueBeyond(EAttribute eAttribute, Object geometry, double distance)
            throws EFeatureEncoderException {
        super(
                eAttribute,
                SpatialConditionEncoder.beyond(eAttribute.getEAttributeType(), geometry, distance));
    }

    public EGeometryValueBeyond(EAttribute eAttribute, Geometry geometry, double distance)
            throws EFeatureEncoderException {
        super(
                eAttribute,
                SpatialConditionEncoder.beyond(eAttribute.getEAttributeType(), geometry, distance));
    }
}
