/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.efeature.internal;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeaturePackage;

/**
 * 
 * @author kengu - 13. juni 2011
 *
 */
public class EFeatureClassDelegate implements EClass {

    /**
     * Cached weak reference to {@link EClass} implementation
     */
    private WeakReference<EClass> eImpl;
    
    /**
     * Cached weak reference to {@link EFeature} class implementation
     */
    private WeakReference<EClass> eFeatureClass;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * Default constructor
     */
    public EFeatureClassDelegate(EClass eImpl) {
        //
        // Forward to super class
        //
        super();
        //
        // Cache weak references
        //
        this.eImpl = new WeakReference<EClass>(eImpl);
        this.eFeatureClass = new WeakReference<EClass>(EFeaturePackage.eINSTANCE.getEFeature());
    }
    
    // ----------------------------------------------------- 
    //  EFeatureClassDelegate methods
    // -----------------------------------------------------

    public EClass eImpl() {
        return !(eImpl==null || eImpl.get()==null) ? 
                eImpl.get() : null; 
    }

    public EClass eFeatureClass() {
        return !(eFeatureClass==null || eFeatureClass.get()==null) ? 
                eFeatureClass.get() : null; 
    }
    
    // ----------------------------------------------------- 
    //  EClass delegate implementation
    // -----------------------------------------------------
    
    @SuppressWarnings("unchecked")
    public EList<Adapter> eAdapters() {
        return merge(eImpl().eAdapters(),eFeatureClass().eAdapters());
    }

    @SuppressWarnings("unchecked")
    public TreeIterator<EObject> eAllContents() {
        return merge(eImpl().eAllContents(),eFeatureClass().eAllContents());
    }

    public EClass eClass() {
        return eImpl().eClass();
    }

    public EObject eContainer() {
        return eImpl().eContainer();
    }

    public EStructuralFeature eContainingFeature() {
        return eImpl().eContainingFeature();
    }

    public EReference eContainmentFeature() {
        return eImpl().eContainmentFeature();
    }

    public EList<EObject> eContents() {
        return eImpl().eContents();
    }

    @SuppressWarnings("unchecked")
    public EList<EObject> eCrossReferences() {
        return merge(eImpl().eCrossReferences(),eFeatureClass().eCrossReferences());
    }

    public boolean eDeliver() {
        return eImpl().eDeliver() || eFeatureClass().eDeliver();
    }

    public Object eGet(EStructuralFeature eFeature, boolean resolve) {
        if(eImpl().getEAllStructuralFeatures().contains(eFeature)) {
            return eImpl().eGet(eFeature, resolve);
        }
        return eFeatureClass().eGet(eFeature, resolve);       
    }

    public Object eGet(EStructuralFeature eFeature) {
        if(eImpl().getEAllStructuralFeatures().contains(eFeature)) {
            return eImpl().eGet(eFeature);
        }
        return eFeatureClass().eGet(eFeature);
    }

    public Object eInvoke(EOperation eOperation, EList<?> eArgs) throws InvocationTargetException {
        if(eImpl().getEOperations().contains(eOperation)) {
            return eImpl().eInvoke(eOperation, eArgs);
        }
        return eFeatureClass().eInvoke(eOperation, eArgs);
    }

    public boolean eIsProxy() {
        return eImpl().eIsProxy() || eFeatureClass().eIsProxy();
    }

    public boolean eIsSet(EStructuralFeature eFeature) {
        if(eImpl().getEAllStructuralFeatures().contains(eFeature)) {
            return eImpl().eIsSet(eFeature);   
        }
        return eFeatureClass().eIsSet(eFeature);        
    }

    public void eNotify(Notification eNotification) {
        eImpl().eNotify(eNotification);
    }

    public Resource eResource() {
        return eImpl().eResource();
    }

    public void eSet(EStructuralFeature eFeature, Object newValue) {
        if(eImpl().getEAllStructuralFeatures().contains(eFeature)) {
            eImpl().eSet(eFeature, newValue);
        } else {
            eFeatureClass().eSet(eFeature, newValue);
        }
    }

    public void eSetDeliver(boolean eDeliver) {
        eImpl().eSetDeliver(eDeliver);
    }

    public void eUnset(EStructuralFeature eFeature) {
        if(eImpl().getEAllStructuralFeatures().contains(eFeature)) {
            eImpl().eUnset(eFeature);
        } else {
            eFeatureClass().eUnset(eFeature);
        }
    }

    public int getClassifierID() {
        return eImpl().getClassifierID();
    }

    public Object getDefaultValue() {
        return eImpl().getDefaultValue();
    }

    @SuppressWarnings("unchecked")
    public EList<EAttribute> getEAllAttributes() {
        return merge(eImpl().getEAllAttributes(),eFeatureClass().getEAllAttributes());
    }

    @SuppressWarnings("unchecked")
    public EList<EReference> getEAllContainments() {
        return merge(eImpl().getEAllContainments(),eFeatureClass().getEAllContainments());
    }

    @SuppressWarnings("unchecked")
    public EList<EGenericType> getEAllGenericSuperTypes() {
        return merge(eImpl().getEAllGenericSuperTypes(),eFeatureClass().getEAllGenericSuperTypes());
    }

    @SuppressWarnings("unchecked")
    public EList<EOperation> getEAllOperations() {
        return merge(eImpl().getEAllOperations(),eFeatureClass().getEAllOperations());
    }

    @SuppressWarnings("unchecked")
    public EList<EReference> getEAllReferences() {
        return merge(eImpl().getEAllReferences(),eFeatureClass().getEAllReferences());
    }

    @SuppressWarnings("unchecked")
    public EList<EStructuralFeature> getEAllStructuralFeatures() {
        return merge(eImpl().getEAllStructuralFeatures(),eFeatureClass().getEAllStructuralFeatures());
    }

    @SuppressWarnings("unchecked")
    public EList<EClass> getEAllSuperTypes() {
        return merge(eImpl().getEAllSuperTypes(),eFeatureClass().getEAllSuperTypes());
    }

    public EAnnotation getEAnnotation(String eName) {
        if(eImpl().getEAnnotations().contains(eName)) {
            return eImpl().getEAnnotation(eName);
        } 
        return eFeatureClass().getEAnnotation(eName);        
    }

    @SuppressWarnings("unchecked")
    public EList<EAnnotation> getEAnnotations() {
        return merge(eImpl().getEAnnotations(),eFeatureClass().getEAnnotations());
    }

    @SuppressWarnings("unchecked")
    public EList<EAttribute> getEAttributes() {
        return merge(eImpl().getEAttributes(),eFeatureClass().getEAttributes());
    }

    @SuppressWarnings("unchecked")
    public EList<EGenericType> getEGenericSuperTypes() {
        return merge(eImpl().getEGenericSuperTypes(),eFeatureClass().getEGenericSuperTypes());
    }

    public EAttribute getEIDAttribute() {
        return eImpl().getEIDAttribute();
    }

    public EOperation getEOperation(int eID) {
        EOperation eOperation = eImpl().getEOperation(eID);
        if(eOperation==null) {
            eOperation = eFeatureClass().getEOperation(eID);
        }
        return eOperation;
    }

    public EList<EOperation> getEOperations() {
        return eImpl().getEOperations();
    }

    public EPackage getEPackage() {
        return eImpl().getEPackage();
    }

    @SuppressWarnings("unchecked")
    public EList<EReference> getEReferences() {
        return merge(eImpl().getEReferences(),eFeatureClass().getEReferences());
    }

    public EStructuralFeature getEStructuralFeature(int eID) {
        EStructuralFeature eFeature = eImpl().getEStructuralFeature(eID);
        if(eFeature==null) {
            eFeature = eFeatureClass().getEStructuralFeature(eID);
        }
        return eFeature;
    }

    public EStructuralFeature getEStructuralFeature(String eName) {
        EStructuralFeature eFeature = eImpl().getEStructuralFeature(eName);
        if(eFeature==null) {
            eFeature = eFeatureClass().getEStructuralFeature(eName);
        }
        return eFeature;
    }

    @SuppressWarnings("unchecked")
    public EList<EStructuralFeature> getEStructuralFeatures() {
        return merge(eImpl().getEStructuralFeatures(),eFeatureClass().getEStructuralFeatures());
    }

    @SuppressWarnings("unchecked")
    public EList<EClass> getESuperTypes() {
        return merge(eImpl().getESuperTypes(),eFeatureClass().getESuperTypes());
    }

    @SuppressWarnings("unchecked")
    public EList<ETypeParameter> getETypeParameters() {
        return merge(eImpl().getETypeParameters(),eFeatureClass().getETypeParameters());
    }

    public int getFeatureCount() {
        return eImpl().getFeatureCount()+eFeatureClass().getFeatureCount() ;
    }

    public int getFeatureID(EStructuralFeature eFeature) {
        if(eImpl().getEAllStructuralFeatures().contains(eFeature)) {
            return eImpl().getFeatureID(eFeature);
        } 
        return eFeatureClass().getFeatureID(eFeature);
    }

    public Class<?> getInstanceClass() {
        return eImpl().getInstanceClass();
    }

    public String getInstanceClassName() {
        return eImpl().getInstanceClassName();
    }

    public String getInstanceTypeName() {
        return eImpl().getInstanceTypeName();
    }

    public String getName() {
        return eImpl().getName();
    }

    public int getOperationCount() {
        return eImpl().getOperationCount()+eImpl().getOperationCount();
    }

    public int getOperationID(EOperation eOperation) {
        if(eImpl().getEOperations().contains(eOperation)) {
            return eImpl().getOperationID(eOperation);
        } 
        return eFeatureClass().getOperationID(eOperation);
    }

    public EOperation getOverride(EOperation eOperation) {
        if(eImpl().getEOperations().contains(eOperation)) {
            return eImpl().getOverride(eOperation);
        } 
        return eFeatureClass().getOverride(eOperation);
    }

    public boolean isAbstract() {
        return eImpl().isAbstract();
    }

    public boolean isInstance(Object object) {
        return eImpl().isInstance(object);
    }

    public boolean isInterface() {
        return eImpl().isInterface();
    }

    public boolean isSuperTypeOf(EClass eClass) {
        return eImpl().isSuperTypeOf(eClass);
    }

    public void setAbstract(boolean isAbstract) {
        eImpl().setAbstract(isAbstract);
    }

    public void setInstanceClass(Class<?> type) {
        eImpl().setInstanceClass(type);
    }

    public void setInstanceClassName(String type) {
        eImpl().setInstanceClassName(type);
    }

    public void setInstanceTypeName(String type) {
        eImpl().setInstanceTypeName(type);
    }

    public void setInterface(boolean isInterface) {
        eImpl().setInterface(isInterface);
    }

    public void setName(String eName) {
        eImpl().setName(eName);
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------

    private <E> EList<E> merge(List<E>... lists) {
        EContentsEList<E> eList = 
            new EContentsEList<E>(this);
        for(List<E> it : lists) {
            eList.addAll(it);
        }
        return eList;
    }
    
    private <E> MergedTreeIterator<E> merge(TreeIterator<E>... lists) {
        MergedTreeIterator<E> eMerged = new MergedTreeIterator<E>();
        for(TreeIterator<E> it : lists) {
            eMerged.merge(it);
        }
        return eMerged;
    }
        
    // ----------------------------------------------------- 
    //  Inner helper classes
    // -----------------------------------------------------

    private static class MergedTreeIterator<E> implements TreeIterator<E> {
        
        private List<TreeIterator<E>> eMerged = new LinkedList<TreeIterator<E>>();
        
        public void merge(TreeIterator<E> it) {
            eMerged.add(it);
        }

        public boolean hasNext() {
            if(eMerged.size()>0) {                
                //
                // Always check head
                //
                TreeIterator<E> it = eMerged.get(0);
                //
                // If current iterator has more, then return true
                //
                if(it.hasNext()) return true;
                //
                // Else, recursively remove current iterator until 
                // a iterator with move elements is found or all
                // iterators are removed
                //
                eMerged.remove(0);
                //
                // Recursively check for more items
                //
                return hasNext();
            }
            //
            // Has no more items 
            // (all iterators returned false 
            //  and are now removed)
            //
            return false;
        }

        public E next() {
            // 
            // Invalid state?
            //
            if(eMerged.size()==0) {
                throw new NoSuchElementException("No more elements found");
            }      
            //
            // Try current iterator
            //
            return eMerged.get(0).next();
        }

        public void remove() {
            // 
            // Invalid state?
            //
            if(eMerged.size()==0) {
                throw new IllegalStateException ("No elements found");
            }      
            //
            // Try current iterator
            //
            eMerged.get(0).remove();
        }

        public void prune() {
            // 
            // Invalid state?
            //
            if(eMerged.size()==0) {
                throw new IllegalStateException ("No elements found");
            }      
            //
            // Try current iterator
            //
            eMerged.get(0).prune();
        }
        
    }
    
}
