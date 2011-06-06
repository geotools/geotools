package org.geotools.data.efeature.query;

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

public class EAttributeValueIsNotEqual extends EObjectAttributeValueCondition {

    public EAttributeValueIsNotEqual(EAttribute eAttribute, Literal value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.notEquals(value));
    }

    public EAttributeValueIsNotEqual(EAttribute eAttribute, Number value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.notEquals(value));
    }

    public EAttributeValueIsNotEqual(EAttribute eAttribute, Date value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.notEquals(value));
    }

    public EAttributeValueIsNotEqual(EAttribute eAttribute, String value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.notEquals(value));
    }

}
