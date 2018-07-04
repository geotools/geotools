/**
 */
package org.w3._2001.smil20;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.Smil20Package
 * @generated
 */
public interface Smil20Factory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    Smil20Factory eINSTANCE = org.w3._2001.smil20.impl.Smil20FactoryImpl.init();

    /**
     * Returns a new object of class '<em>Animate Color Prototype</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Animate Color Prototype</em>'.
     * @generated
     */
    AnimateColorPrototype createAnimateColorPrototype();

    /**
     * Returns a new object of class '<em>Animate Motion Prototype</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Animate Motion Prototype</em>'.
     * @generated
     */
    AnimateMotionPrototype createAnimateMotionPrototype();

    /**
     * Returns a new object of class '<em>Animate Prototype</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Animate Prototype</em>'.
     * @generated
     */
    AnimatePrototype createAnimatePrototype();

    /**
     * Returns a new object of class '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Document Root</em>'.
     * @generated
     */
    DocumentRoot createDocumentRoot();

    /**
     * Returns a new object of class '<em>Set Prototype</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Set Prototype</em>'.
     * @generated
     */
    SetPrototype createSetPrototype();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    Smil20Package getSmil20Package();

} //Smil20Factory
