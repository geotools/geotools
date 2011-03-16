/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.util.Collection;

import javax.measure.unit.Unit;

import net.opengis.wps10.UOMsType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>UO Ms Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.UOMsTypeImpl#getUOM <em>UOM</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UOMsTypeImpl extends EObjectImpl implements UOMsType {
    /**
     * The cached value of the '{@link #getUOM() <em>UOM</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUOM()
     * @generated
     * @ordered
     */
    protected EList uOM;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UOMsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.UO_MS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * Uses an ECoreEList inatance as we are managing Unit (which is not an EObject)
     * <!-- end-user-doc -->
     */
    public EList getUOM() {
        if (uOM == null) {
            uOM = new EObjectEList(Unit.class, this, Wps10Package.UO_MS_TYPE__UOM );
        }
        return uOM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.UO_MS_TYPE__UOM:
                return getUOM();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wps10Package.UO_MS_TYPE__UOM:
                getUOM().clear();
                getUOM().addAll((Collection)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wps10Package.UO_MS_TYPE__UOM:
                getUOM().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wps10Package.UO_MS_TYPE__UOM:
                return uOM != null && !uOM.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //UOMsTypeImpl
