package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

public class EAttributeValueIsBetween extends EObjectAttributeValueCondition {

    public EAttributeValueIsBetween(EAttribute eAttribute, Literal lower, Literal upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.between(lower, upper));
    }

}
