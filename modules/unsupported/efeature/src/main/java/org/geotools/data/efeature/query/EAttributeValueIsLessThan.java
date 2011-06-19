package org.geotools.data.efeature.query;

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

public class EAttributeValueIsLessThan extends EObjectAttributeValueCondition {

    public EAttributeValueIsLessThan(EAttribute eAttribute, Literal value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.lt(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsLessThan(EAttribute eAttribute, Object value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.lt(eAttribute.getEAttributeType(),value));
    }
    
    public EAttributeValueIsLessThan(EAttribute eAttribute, Number value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.lt(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsLessThan(EAttribute eAttribute, Date value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.lt(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsLessThan(EAttribute eAttribute, Boolean value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.lt(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsLessThan(EAttribute eAttribute, Character value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.lt(eAttribute.getEAttributeType(),value));
    }

    
    
    public EAttributeValueIsLessThan(EAttribute eAttribute, String value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.lt(eAttribute.getEAttributeType(),value));
    }

}
