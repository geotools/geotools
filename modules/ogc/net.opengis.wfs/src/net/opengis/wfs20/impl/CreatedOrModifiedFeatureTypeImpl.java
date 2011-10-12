/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import net.opengis.fes20.ResourceIdType;

import net.opengis.wfs20.CreatedOrModifiedFeatureType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.opengis.filter.identity.FeatureId;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Created Or Modified Feature Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.CreatedOrModifiedFeatureTypeImpl#getResourceId <em>Resource Id</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.CreatedOrModifiedFeatureTypeImpl#getHandle <em>Handle</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CreatedOrModifiedFeatureTypeImpl extends EObjectImpl implements CreatedOrModifiedFeatureType {
    /**
     * The cached value of the '{@link #getResourceId() <em>Resource Id</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceId()
     * @generated
     * @ordered
     */
    protected EList<FeatureId> resourceId;

    /**
     * The default value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
    protected static final String HANDLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
    protected String handle = HANDLE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CreatedOrModifiedFeatureTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.CREATED_OR_MODIFIED_FEATURE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<FeatureId> getResourceId() {
        if (resourceId == null) {
            resourceId = new EDataTypeUniqueEList<FeatureId>(FeatureId.class, this, Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__RESOURCE_ID);
        }
        return resourceId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHandle() {
        return handle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHandle(String newHandle) {
        String oldHandle = handle;
        handle = newHandle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__HANDLE, oldHandle, handle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__RESOURCE_ID:
                return getResourceId();
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__HANDLE:
                return getHandle();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__RESOURCE_ID:
                getResourceId().clear();
                getResourceId().addAll((Collection<? extends FeatureId>)newValue);
                return;
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__HANDLE:
                setHandle((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__RESOURCE_ID:
                getResourceId().clear();
                return;
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__HANDLE:
                setHandle(HANDLE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__RESOURCE_ID:
                return resourceId != null && !resourceId.isEmpty();
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE__HANDLE:
                return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (resourceId: ");
        result.append(resourceId);
        result.append(", handle: ");
        result.append(handle);
        result.append(')');
        return result.toString();
    }

} //CreatedOrModifiedFeatureTypeImpl
