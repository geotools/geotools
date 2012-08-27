/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DCMIRecordType;
import net.opengis.cat.csw20.SimpleLiteral;
import net.opengis.ows10.BoundingBoxType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DCMI Record Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.DCMIRecordTypeImpl#getDCElement <em>DC Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DCMIRecordTypeImpl extends AbstractRecordTypeImpl implements DCMIRecordType {
    
    /**
     * The cached value of the '{@link #getDCElement()() <em>DCElement</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDCElement()()
     * @ordered
     */
    protected EList<SimpleLiteral> dcElement;
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DCMIRecordTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.DCMI_RECORD_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    public EList<SimpleLiteral> getDCElement() {
        if (dcElement == null) {
            dcElement = new EObjectContainmentEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.DCMI_RECORD_TYPE__DC_ELEMENT);
        }
        return dcElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.DCMI_RECORD_TYPE__DC_ELEMENT:
                return ((InternalEList<?>)getDCElement()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.DCMI_RECORD_TYPE__DC_ELEMENT:
                return getDCElement();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Csw20Package.DCMI_RECORD_TYPE__DC_ELEMENT:
                return !getDCElement().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //DCMIRecordTypeImpl
