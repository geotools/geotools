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
public class EAttributeValueIsGreaterEqual extends EObjectAttributeValueCondition {

    public EAttributeValueIsGreaterEqual(EAttribute eAttribute, Literal value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.ge(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsGreaterEqual(EAttribute eAttribute, Object value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.ge(eAttribute.getEAttributeType(),value));
    }
    
    public EAttributeValueIsGreaterEqual(EAttribute eAttribute, Number value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.ge(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsGreaterEqual(EAttribute eAttribute, Date value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.ge(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsGreaterEqual(EAttribute eAttribute, Boolean value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.ge(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsGreaterEqual(EAttribute eAttribute, Character value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.ge(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsGreaterEqual(EAttribute eAttribute, String value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.ge(eAttribute.getEAttributeType(),value));
    }

}
