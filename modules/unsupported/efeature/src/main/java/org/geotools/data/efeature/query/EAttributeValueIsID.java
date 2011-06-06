package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;

public class EAttributeValueIsID extends EObjectAttributeValueCondition {

    public EAttributeValueIsID(EAttribute eAttribute, String id) throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.equals(id));
    }

}
