/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import javax.xml.namespace.QName;

import net.opengis.ows11.MetadataType;

import net.opengis.wfs20.ElementType;
import net.opengis.wfs20.ValueListType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.ElementTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.ElementTypeImpl#getValueList <em>Value List</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.ElementTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.ElementTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ElementTypeImpl extends EObjectImpl implements ElementType {
    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected MetadataType metadata;

    /**
     * The cached value of the '{@link #getValueList() <em>Value List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueList()
     * @generated
     * @ordered
     */
    protected ValueListType valueList;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final QName TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected QName type = TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ElementTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.ELEMENT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MetadataType getMetadata() {
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMetadata(MetadataType newMetadata, NotificationChain msgs) {
        MetadataType oldMetadata = metadata;
        metadata = newMetadata;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.ELEMENT_TYPE__METADATA, oldMetadata, newMetadata);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMetadata(MetadataType newMetadata) {
        if (newMetadata != metadata) {
            NotificationChain msgs = null;
            if (metadata != null)
                msgs = ((InternalEObject)metadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.ELEMENT_TYPE__METADATA, null, msgs);
            if (newMetadata != null)
                msgs = ((InternalEObject)newMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.ELEMENT_TYPE__METADATA, null, msgs);
            msgs = basicSetMetadata(newMetadata, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.ELEMENT_TYPE__METADATA, newMetadata, newMetadata));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueListType getValueList() {
        return valueList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValueList(ValueListType newValueList, NotificationChain msgs) {
        ValueListType oldValueList = valueList;
        valueList = newValueList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.ELEMENT_TYPE__VALUE_LIST, oldValueList, newValueList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValueList(ValueListType newValueList) {
        if (newValueList != valueList) {
            NotificationChain msgs = null;
            if (valueList != null)
                msgs = ((InternalEObject)valueList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.ELEMENT_TYPE__VALUE_LIST, null, msgs);
            if (newValueList != null)
                msgs = ((InternalEObject)newValueList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.ELEMENT_TYPE__VALUE_LIST, null, msgs);
            msgs = basicSetValueList(newValueList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.ELEMENT_TYPE__VALUE_LIST, newValueList, newValueList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.ELEMENT_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(QName newType) {
        QName oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.ELEMENT_TYPE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.ELEMENT_TYPE__METADATA:
                return basicSetMetadata(null, msgs);
            case Wfs20Package.ELEMENT_TYPE__VALUE_LIST:
                return basicSetValueList(null, msgs);
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
            case Wfs20Package.ELEMENT_TYPE__METADATA:
                return getMetadata();
            case Wfs20Package.ELEMENT_TYPE__VALUE_LIST:
                return getValueList();
            case Wfs20Package.ELEMENT_TYPE__NAME:
                return getName();
            case Wfs20Package.ELEMENT_TYPE__TYPE:
                return getType();
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
            case Wfs20Package.ELEMENT_TYPE__METADATA:
                setMetadata((MetadataType)newValue);
                return;
            case Wfs20Package.ELEMENT_TYPE__VALUE_LIST:
                setValueList((ValueListType)newValue);
                return;
            case Wfs20Package.ELEMENT_TYPE__NAME:
                setName((String)newValue);
                return;
            case Wfs20Package.ELEMENT_TYPE__TYPE:
                setType((QName)newValue);
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
            case Wfs20Package.ELEMENT_TYPE__METADATA:
                setMetadata((MetadataType)null);
                return;
            case Wfs20Package.ELEMENT_TYPE__VALUE_LIST:
                setValueList((ValueListType)null);
                return;
            case Wfs20Package.ELEMENT_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case Wfs20Package.ELEMENT_TYPE__TYPE:
                setType(TYPE_EDEFAULT);
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
            case Wfs20Package.ELEMENT_TYPE__METADATA:
                return metadata != null;
            case Wfs20Package.ELEMENT_TYPE__VALUE_LIST:
                return valueList != null;
            case Wfs20Package.ELEMENT_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case Wfs20Package.ELEMENT_TYPE__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
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
        result.append(" (name: ");
        result.append(name);
        result.append(", type: ");
        result.append(type);
        result.append(')');
        return result.toString();
    }

} //ElementTypeImpl
