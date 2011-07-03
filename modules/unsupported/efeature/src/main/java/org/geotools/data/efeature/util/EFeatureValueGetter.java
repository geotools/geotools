package org.geotools.data.efeature.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.IEStructuralFeatureValueGetter;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;

/**
 * @author kengu - 11. juni 2011
 *
 */
public final class EFeatureValueGetter implements IEStructuralFeatureValueGetter {

    /** eFeatureMap */
    private static Map<EAttribute, Map<EClass, EAttribute>> eFeatureMap;
    
    /** eAttributeMap */
    private Map<EAttribute, Map<EClass, EAttribute>> eAttributeMap;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * @param eMappingMap
     */
    public EFeatureValueGetter(EFeatureInfo...eFeatureInfos) {
        this.eAttributeMap = create(eFeatureInfos);
    }

    // ----------------------------------------------------- 
    //  IEStructuralFeatureValueGetter implementation
    // -----------------------------------------------------
    
    @Override
    public EObject resolve(EObject eObject) {
        //  
        // Do nothing, just return the same object
        //
        return eObject;
    }

    @Override
    public Object eGet(EObject eObject, EStructuralFeature eFeature, boolean resolve) {
        //
        // Prepare information
        //
        EClass eObjectClass = eObject.eClass();
        EClass eFeatureClass = EFeaturePackage.eINSTANCE.getEFeature();
        //
        // Is direct lookup possible or only option?
        //
        if(eAttributeMap.size()==0 || eFeatureClass.isSuperTypeOf(eObjectClass)) {
            return eObject.eGet(eFeature);
        }
        //
        // Get EClass to EAttribute mapping for given feature
        //
        Map<EClass,EAttribute> eClassMap = eAttributeMap.get(eFeature);
        if(eClassMap==null) {
            //
            // Not found, just fall through leaving the problem for EMF to solve
            //
            return eObject.eGet(eFeature);
        }
        //
        // Map given feature to EAttribute in given object
        //
        eFeature = eClassMap.get(eObject.eClass());
        //
        // Use EMF reflection to retrieve value
        //
        return eObject.eGet(eFeature);
    }

    @Override
    public List<EObject> eContents(EObject eObject, EObjectCondition eCondition) {
        //
        // TODO Implement content filtering
        //
        return eObject.eContents();
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    /**
     * Construct mapping from array of {@link EFeatureInfo}s
     * </p>
     * @param eFeatureInfos
     * @return a new {@link EFeatureValueGetter}
     */
    private final static Map<EAttribute, Map<EClass, EAttribute>> create(EFeatureInfo...eFeatureInfos) {        
        //
        // Prepare construction of EFeature mappings
        //
        EFeaturePackage ePackage = EFeaturePackage.eINSTANCE;
        EClass eFeatureClass = ePackage.getEFeature();
        //
        // Initialize create mapping onto EFeature?
        //
        if(eFeatureMap==null) {
            eFeatureMap = new HashMap<EAttribute, Map<EClass,EAttribute>>();
            for(EAttribute it : eFeatureClass.getEAllAttributes()) {
                Map<EClass,EAttribute> eClassMap = new HashMap<EClass,EAttribute>();
                eClassMap.put(eFeatureClass, it);
                eFeatureMap.put(it,eClassMap);
            }            
        }
        //
        // Copy from EFeature onto mapping
        //
        final Map<EAttribute, Map<EClass,EAttribute>>  
            eAttributeMap = new HashMap<EAttribute, Map<EClass,EAttribute>>(eFeatureMap);                
        //
        // Then add mappings from implementations onto EFeature
        //        
        for(EFeatureInfo it : eFeatureInfos) {
            //
            // Prepare information about structure
            //
            EClass eClass = it.eClass();            
            //
            // Add mapping from EFeature ID to implementation (is required)
            //
            eAttributeMap.get(ePackage.getEFeature_ID()).put(eClass,it.eIDAttribute());
            //
            // Add mapping from EFeature SRID to implementation (optional)
            //
            EAttribute eAttribute = it.eSRIDAttribute();
            if(eAttribute!=null) {
                eAttributeMap.get(ePackage.getEFeature_SRID()).put(eClass,eAttribute);
            }
            //
            // Add mapping from EFeature default geometry to implementation (optional)
            //
            eAttribute = it.eDefaultGeometry();
            if(eAttribute!=null) {
                eAttributeMap.get(ePackage.getEFeature_Default()).put(eClass,it.eDefaultGeometry());
            }
        }
        //
        // Construct structural value getter
        //
        return eAttributeMap;
        
    }
    
}