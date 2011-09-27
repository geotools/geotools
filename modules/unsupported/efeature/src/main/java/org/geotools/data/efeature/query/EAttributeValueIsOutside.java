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
public class EAttributeValueIsOutside extends EObjectAttributeValueCondition {

    public EAttributeValueIsOutside(EAttribute eAttribute, Literal lower, Literal upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(eAttribute.getEAttributeType(), lower, upper));
    }
        
    public EAttributeValueIsOutside(EAttribute eAttribute, Object lower, Object upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(eAttribute.getEAttributeType(), lower, upper));
    }
   
    public EAttributeValueIsOutside(EAttribute eAttribute, Number lower, Number upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(lower, upper));
    }
    
    public EAttributeValueIsOutside(EAttribute eAttribute, Date lower, Date upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(lower, upper));
    }
    
    public EAttributeValueIsOutside(EAttribute eAttribute, String lower, String upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(lower, upper));
    }
    
    public EAttributeValueIsOutside(EAttribute eAttribute, Character lower, Character upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(lower, upper));
    }    

}
