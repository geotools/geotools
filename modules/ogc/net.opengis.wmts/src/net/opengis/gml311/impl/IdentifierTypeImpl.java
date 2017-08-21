/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IdentifierType;
import net.opengis.gml311.StringOrRefType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Identifier Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.IdentifierTypeImpl#getNameGroup <em>Name Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IdentifierTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IdentifierTypeImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IdentifierTypeImpl#getRemarks <em>Remarks</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IdentifierTypeImpl extends MinimalEObjectImpl.Container implements IdentifierType {
    /**
     * The cached value of the '{@link #getNameGroup() <em>Name Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNameGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap nameGroup;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * The cached value of the '{@link #getRemarks() <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRemarks()
     * @generated
     * @ordered
     */
    protected StringOrRefType remarks;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IdentifierTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getIdentifierType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getNameGroup() {
        if (nameGroup == null) {
            nameGroup = new BasicFeatureMap(this, Gml311Package.IDENTIFIER_TYPE__NAME_GROUP);
        }
        return nameGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getName() {
        return (CodeType)getNameGroup().get(Gml311Package.eINSTANCE.getIdentifierType_Name(), true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetName(CodeType newName, NotificationChain msgs) {
        return ((FeatureMap.Internal)getNameGroup()).basicAdd(Gml311Package.eINSTANCE.getIdentifierType_Name(), newName, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(CodeType newName) {
        ((FeatureMap.Internal)getNameGroup()).set(Gml311Package.eINSTANCE.getIdentifierType_Name(), newName);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.IDENTIFIER_TYPE__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StringOrRefType getRemarks() {
        return remarks;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRemarks(StringOrRefType newRemarks, NotificationChain msgs) {
        StringOrRefType oldRemarks = remarks;
        remarks = newRemarks;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.IDENTIFIER_TYPE__REMARKS, oldRemarks, newRemarks);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRemarks(StringOrRefType newRemarks) {
        if (newRemarks != remarks) {
            NotificationChain msgs = null;
            if (remarks != null)
                msgs = ((InternalEObject)remarks).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.IDENTIFIER_TYPE__REMARKS, null, msgs);
            if (newRemarks != null)
                msgs = ((InternalEObject)newRemarks).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.IDENTIFIER_TYPE__REMARKS, null, msgs);
            msgs = basicSetRemarks(newRemarks, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.IDENTIFIER_TYPE__REMARKS, newRemarks, newRemarks));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.IDENTIFIER_TYPE__NAME_GROUP:
                return ((InternalEList<?>)getNameGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.IDENTIFIER_TYPE__NAME:
                return basicSetName(null, msgs);
            case Gml311Package.IDENTIFIER_TYPE__REMARKS:
                return basicSetRemarks(null, msgs);
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
            case Gml311Package.IDENTIFIER_TYPE__NAME_GROUP:
                if (coreType) return getNameGroup();
                return ((FeatureMap.Internal)getNameGroup()).getWrapper();
            case Gml311Package.IDENTIFIER_TYPE__NAME:
                return getName();
            case Gml311Package.IDENTIFIER_TYPE__VERSION:
                return getVersion();
            case Gml311Package.IDENTIFIER_TYPE__REMARKS:
                return getRemarks();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.IDENTIFIER_TYPE__NAME_GROUP:
                ((FeatureMap.Internal)getNameGroup()).set(newValue);
                return;
            case Gml311Package.IDENTIFIER_TYPE__NAME:
                setName((CodeType)newValue);
                return;
            case Gml311Package.IDENTIFIER_TYPE__VERSION:
                setVersion((String)newValue);
                return;
            case Gml311Package.IDENTIFIER_TYPE__REMARKS:
                setRemarks((StringOrRefType)newValue);
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
            case Gml311Package.IDENTIFIER_TYPE__NAME_GROUP:
                getNameGroup().clear();
                return;
            case Gml311Package.IDENTIFIER_TYPE__NAME:
                setName((CodeType)null);
                return;
            case Gml311Package.IDENTIFIER_TYPE__VERSION:
                setVersion(VERSION_EDEFAULT);
                return;
            case Gml311Package.IDENTIFIER_TYPE__REMARKS:
                setRemarks((StringOrRefType)null);
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
            case Gml311Package.IDENTIFIER_TYPE__NAME_GROUP:
                return nameGroup != null && !nameGroup.isEmpty();
            case Gml311Package.IDENTIFIER_TYPE__NAME:
                return getName() != null;
            case Gml311Package.IDENTIFIER_TYPE__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
            case Gml311Package.IDENTIFIER_TYPE__REMARKS:
                return remarks != null;
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
        result.append(" (nameGroup: ");
        result.append(nameGroup);
        result.append(", version: ");
        result.append(version);
        result.append(')');
        return result.toString();
    }

} //IdentifierTypeImpl
