/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.wfs20.StoredQueryListItemType;
import net.opengis.wfs20.TitleType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Stored Query List Item Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryListItemTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryListItemTypeImpl#getReturnFeatureType <em>Return Feature Type</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryListItemTypeImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StoredQueryListItemTypeImpl extends EObjectImpl implements StoredQueryListItemType {
    /**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected EList<TitleType> title;

    /**
     * The cached value of the '{@link #getReturnFeatureType() <em>Return Feature Type</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReturnFeatureType()
     * @generated
     * @ordered
     */
    protected EList<QName> returnFeatureType;

    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final String ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected String id = ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected StoredQueryListItemTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.STORED_QUERY_LIST_ITEM_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TitleType> getTitle() {
        if (title == null) {
            title = new EObjectContainmentEList<TitleType>(TitleType.class, this, Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__TITLE);
        }
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<QName> getReturnFeatureType() {
        if (returnFeatureType == null) {
            returnFeatureType = new EDataTypeEList<QName>(QName.class, this, Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__RETURN_FEATURE_TYPE);
        }
        return returnFeatureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getId() {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__TITLE:
                return ((InternalEList<?>)getTitle()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__TITLE:
                return getTitle();
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__RETURN_FEATURE_TYPE:
                return getReturnFeatureType();
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__ID:
                return getId();
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
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection<? extends TitleType>)newValue);
                return;
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__RETURN_FEATURE_TYPE:
                getReturnFeatureType().clear();
                getReturnFeatureType().addAll((Collection<? extends QName>)newValue);
                return;
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__ID:
                setId((String)newValue);
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
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__TITLE:
                getTitle().clear();
                return;
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__RETURN_FEATURE_TYPE:
                getReturnFeatureType().clear();
                return;
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__ID:
                setId(ID_EDEFAULT);
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
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__TITLE:
                return title != null && !title.isEmpty();
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__RETURN_FEATURE_TYPE:
                return returnFeatureType != null && !returnFeatureType.isEmpty();
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
        result.append(" (returnFeatureType: ");
        result.append(returnFeatureType);
        result.append(", id: ");
        result.append(id);
        result.append(')');
        return result.toString();
    }

} //StoredQueryListItemTypeImpl
