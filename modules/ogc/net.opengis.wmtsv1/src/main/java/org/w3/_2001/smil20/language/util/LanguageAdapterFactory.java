/**
 */
package org.w3._2001.smil20.language.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.w3._2001.smil20.AnimateColorPrototype;
import org.w3._2001.smil20.AnimateMotionPrototype;
import org.w3._2001.smil20.AnimatePrototype;
import org.w3._2001.smil20.SetPrototype;

import org.w3._2001.smil20.language.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.language.LanguagePackage
 * @generated
 */
public class LanguageAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static LanguagePackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = LanguagePackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LanguageSwitch<Adapter> modelSwitch =
        new LanguageSwitch<Adapter>() {
            @Override
            public Adapter caseAnimateColorType(AnimateColorType object) {
                return createAnimateColorTypeAdapter();
            }
            @Override
            public Adapter caseAnimateMotionType(AnimateMotionType object) {
                return createAnimateMotionTypeAdapter();
            }
            @Override
            public Adapter caseAnimateType(AnimateType object) {
                return createAnimateTypeAdapter();
            }
            @Override
            public Adapter caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            @Override
            public Adapter caseSetType(SetType object) {
                return createSetTypeAdapter();
            }
            @Override
            public Adapter caseAnimateColorPrototype(AnimateColorPrototype object) {
                return createAnimateColorPrototypeAdapter();
            }
            @Override
            public Adapter caseAnimateMotionPrototype(AnimateMotionPrototype object) {
                return createAnimateMotionPrototypeAdapter();
            }
            @Override
            public Adapter caseAnimatePrototype(AnimatePrototype object) {
                return createAnimatePrototypeAdapter();
            }
            @Override
            public Adapter caseSetPrototype(SetPrototype object) {
                return createSetPrototypeAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.language.AnimateColorType <em>Animate Color Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.language.AnimateColorType
     * @generated
     */
    public Adapter createAnimateColorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.language.AnimateMotionType <em>Animate Motion Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.language.AnimateMotionType
     * @generated
     */
    public Adapter createAnimateMotionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.language.AnimateType <em>Animate Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.language.AnimateType
     * @generated
     */
    public Adapter createAnimateTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.language.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.language.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.language.SetType <em>Set Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.language.SetType
     * @generated
     */
    public Adapter createSetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.AnimateColorPrototype <em>Animate Color Prototype</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.AnimateColorPrototype
     * @generated
     */
    public Adapter createAnimateColorPrototypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.AnimateMotionPrototype <em>Animate Motion Prototype</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.AnimateMotionPrototype
     * @generated
     */
    public Adapter createAnimateMotionPrototypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.AnimatePrototype <em>Animate Prototype</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.AnimatePrototype
     * @generated
     */
    public Adapter createAnimatePrototypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.w3._2001.smil20.SetPrototype <em>Set Prototype</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.w3._2001.smil20.SetPrototype
     * @generated
     */
    public Adapter createSetPrototypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //LanguageAdapterFactory
