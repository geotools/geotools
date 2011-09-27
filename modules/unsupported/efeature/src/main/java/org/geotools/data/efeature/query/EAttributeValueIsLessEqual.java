package org.geotools.data.efeature.query;

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

/**
 * 
 *
 * @source $URL$
 */
public class EAttributeValueIsLessEqual extends EObjectAttributeValueCondition {

    public EAttributeValueIsLessEqual(EAttribute eAttribute, Literal value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.le(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsLessEqual(EAttribute eAttribute, Object value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.le(eAttribute.getEAttributeType(),value));
    }
    
    public EAttributeValueIsLessEqual(EAttribute eAttribute, Number value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.le(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsLessEqual(EAttribute eAttribute, Date value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.le(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsLessEqual(EAttribute eAttribute, Boolean value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.le(eAttribute.getEAttributeType(),value));
    }
    
    public EAttributeValueIsLessEqual(EAttribute eAttribute, Character value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.le(eAttribute.getEAttributeType(),value));
    }
    
    
    public EAttributeValueIsLessEqual(EAttribute eAttribute, String value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.le(eAttribute.getEAttributeType(),value));
    }

}
