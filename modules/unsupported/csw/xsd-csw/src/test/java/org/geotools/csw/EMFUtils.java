package org.geotools.csw;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class EMFUtils {
    
    public static boolean emfEquals(EObject e1, EObject e2) {
        if(e1 == e2) {
            return true;
        } else if(!e1.eClass().equals(e2.eClass())) {
            return false;
        }
        
        
        for (EStructuralFeature sf :  e1.eClass().getEAllStructuralFeatures()) {
            Object o1 = e1.eGet(sf);
            Object o2 = e2.eGet(sf);
            boolean equals = objectEquals(o1, o2);
            
            if(!equals) {
                System.out.println("Comparison failed on " + sf + " e1 has " + o1 + " while o2 has " + o2);
                return false;
            }
        }
        
        return true;
    }

    private static boolean objectEquals(Object o1, Object o2) {
        boolean equals = true;
        if(o1 == null && o2 != null || o1 != null && o2 == null) {
            equals = false;
        } else if(o1 instanceof EObject) {
            if(!emfEquals((EObject) o1, (EObject) o2)) {
                equals = false;
            }
        } else if(o1 instanceof EList) {
            EList l1 = (EList) o1;
            EList l2 = (EList) o2;
            if(l1.size() != l2.size()) {
                equals = false;
            } else {
                for (int i = 0; i < l1.size() && equals; i++) {
                    Object li1 = l1.get(i);
                    Object li2 = l2.get(i);
                    equals = objectEquals(li1, li2);
                }
            }
        } else if(o1 == null) {
            if(o2 != null) {
                equals = false;
            }
        } else {
            equals = o1.equals(o2);
        }
        return equals;
    }

}
