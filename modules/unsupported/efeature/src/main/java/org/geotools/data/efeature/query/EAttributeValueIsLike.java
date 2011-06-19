package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;

public class EAttributeValueIsLike extends EObjectAttributeValueCondition {

    public EAttributeValueIsLike(EAttribute eAttribute, String pattern)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.like(eAttribute.getEAttributeType(),pattern));
    }

    public EAttributeValueIsLike(EAttribute eAttribute, Object pattern)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.like(eAttribute.getEAttributeType(),pattern));
    }
    
}
