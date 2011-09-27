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
public class EAttributeValueIsEqual extends EObjectAttributeValueCondition {

    public EAttributeValueIsEqual(EAttribute eAttribute, Literal value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.eq(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsEqual(EAttribute eAttribute, Object value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.eq(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsEqual(EAttribute eAttribute, Date value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.eq(eAttribute.getEAttributeType(),value));
    }

    public EAttributeValueIsEqual(EAttribute eAttribute, Boolean value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.eq(eAttribute.getEAttributeType(),value));
    }
    
    public EAttributeValueIsEqual(EAttribute eAttribute, String value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.eq(eAttribute.getEAttributeType(),value));
    }
    
    public EAttributeValueIsEqual(EAttribute eAttribute, Character value)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.eq(eAttribute.getEAttributeType(),value));
    }
    

}
