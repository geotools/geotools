package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.statements.WHERE;

public class EFeatureFilter {

    private WHERE where;

    public EFeatureFilter(WHERE where) {
        this.where = (where == null ? new WHERE(EObjectCondition.E_TRUE) : where);
    }

    public WHERE getWhereClause() {
        return where;
    }

    public boolean matches(EObject eObject) {
        return where.matches(eObject);
    }

    public boolean shouldPrune(EObject eObject) {
        return where.shouldPrune(eObject);
    }

}
