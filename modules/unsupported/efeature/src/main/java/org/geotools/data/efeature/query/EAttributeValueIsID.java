package org.geotools.data.efeature.query;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.geotools.data.efeature.EFeatureUtils;
import org.opengis.feature.Feature;
import org.opengis.filter.identity.Identifier;

/**
 * 
 *
 * @source $URL$
 */
public class EAttributeValueIsID extends EObjectAttributeValueCondition {

    public EAttributeValueIsID(EAttribute eAttribute, Object eIDs) throws EFeatureEncoderException {        
        this(eAttribute, toEIDs(eIDs));
    }

    public EAttributeValueIsID(EAttribute eAttribute, String... eIDs) throws EFeatureEncoderException {
        this(eAttribute, EFeatureUtils.toEIDs((Object[])eIDs));
    }
    
    public EAttributeValueIsID(EAttribute eAttribute, Set<Identifier> eIDs) throws EFeatureEncoderException {
        super(eAttribute, eq(eIDs));
    }
    
    public static final Set<Identifier> toEIDs(Object eIDs) throws IllegalArgumentException {
        if( !(eIDs instanceof Set)) {
            throw new IllegalArgumentException("eIDs must be an instance of java.util.Set");
        }
        Set<Identifier> eIDSet = new HashSet<Identifier>();
        for(Object it : (Set<?>)eIDs) {
            if( !(it instanceof Identifier)) {
                throw new IllegalArgumentException("Items in set must implement " +
                		"org.opengis.filter.identity.Identifier");
            }
            eIDSet.add((Identifier)it);
        }
        return eIDSet;
    }
    
    public static final Condition eq(final Set<Identifier> eIDs) {
        return new Condition() {
            
            @Override
            public boolean isSatisfied(Object value) {
                if(value instanceof Feature) {
                    for(Identifier it : eIDs) {
                        if(it.matches(value)) 
                            return true;
                    }
                } else if(value instanceof String) {
                    for(Identifier it : eIDs) {
                        if(it.getID().equals(value)) 
                            return true;
                    }
                }

                return false;
            }
        };
    }
    
}
