package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;

/**
 * 
 *
 * @source $URL$
 */
public class EAttributeValueIsNull extends EObjectAttributeValueCondition {

    public EAttributeValueIsNull(EAttribute eAttribute) {
        super(eAttribute, ConditionEncoder.IS_NULL);
    }

}
