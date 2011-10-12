/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.fes20.FilterType;

import net.opengis.wfs20.PropertyType;
import net.opengis.wfs20.UpdateType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Update Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.UpdateTypeImpl#getProperty <em>Property</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.UpdateTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.UpdateTypeImpl#getInputFormat <em>Input Format</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.UpdateTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.UpdateTypeImpl#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UpdateTypeImpl extends AbstractTransactionActionTypeImpl implements UpdateType {
    /**
     * The cached value of the '{@link #getProperty() <em>Property</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProperty()
     * @generated
     * @ordered
     */
    protected EList<PropertyType> property;

    /**
     * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected static final Filter FILTER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected Filter filter = FILTER_EDEFAULT;

    /**
     * The default value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputFormat()
     * @generated
     * @ordered
     */
    protected static final String INPUT_FORMAT_EDEFAULT = "application/gml+xml; version=3.2";

    /**
     * The cached value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputFormat()
     * @generated
     * @ordered
     */
    protected String inputFormat = INPUT_FORMAT_EDEFAULT;

    /**
     * This is true if the Input Format attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean inputFormatESet;

    /**
     * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected static final String SRS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected String srsName = SRS_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
    protected static final QName TYPE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
    protected QName typeName = TYPE_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UpdateTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.UPDATE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PropertyType> getProperty() {
        if (property == null) {
            property = new EObjectContainmentEList<PropertyType>(PropertyType.class, this, Wfs20Package.UPDATE_TYPE__PROPERTY);
        }
        return property;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilter(Filter newFilter) {
        Filter oldFilter = filter;
        filter = newFilter;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.UPDATE_TYPE__FILTER, oldFilter, filter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInputFormat() {
        return inputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInputFormat(String newInputFormat) {
        String oldInputFormat = inputFormat;
        inputFormat = newInputFormat;
        boolean oldInputFormatESet = inputFormatESet;
        inputFormatESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.UPDATE_TYPE__INPUT_FORMAT, oldInputFormat, inputFormat, !oldInputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetInputFormat() {
        String oldInputFormat = inputFormat;
        boolean oldInputFormatESet = inputFormatESet;
        inputFormat = INPUT_FORMAT_EDEFAULT;
        inputFormatESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.UPDATE_TYPE__INPUT_FORMAT, oldInputFormat, INPUT_FORMAT_EDEFAULT, oldInputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetInputFormat() {
        return inputFormatESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSrsName() {
        return srsName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsName(String newSrsName) {
        String oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.UPDATE_TYPE__SRS_NAME, oldSrsName, srsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getTypeName() {
        return typeName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTypeName(QName newTypeName) {
        QName oldTypeName = typeName;
        typeName = newTypeName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.UPDATE_TYPE__TYPE_NAME, oldTypeName, typeName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.UPDATE_TYPE__PROPERTY:
                return ((InternalEList<?>)getProperty()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.UPDATE_TYPE__PROPERTY:
                return getProperty();
            case Wfs20Package.UPDATE_TYPE__FILTER:
                return getFilter();
            case Wfs20Package.UPDATE_TYPE__INPUT_FORMAT:
                return getInputFormat();
            case Wfs20Package.UPDATE_TYPE__SRS_NAME:
                return getSrsName();
            case Wfs20Package.UPDATE_TYPE__TYPE_NAME:
                return getTypeName();
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
            case Wfs20Package.UPDATE_TYPE__PROPERTY:
                getProperty().clear();
                getProperty().addAll((Collection<? extends PropertyType>)newValue);
                return;
            case Wfs20Package.UPDATE_TYPE__FILTER:
                setFilter((Filter)newValue);
                return;
            case Wfs20Package.UPDATE_TYPE__INPUT_FORMAT:
                setInputFormat((String)newValue);
                return;
            case Wfs20Package.UPDATE_TYPE__SRS_NAME:
                setSrsName((String)newValue);
                return;
            case Wfs20Package.UPDATE_TYPE__TYPE_NAME:
                setTypeName((QName) newValue);
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
            case Wfs20Package.UPDATE_TYPE__PROPERTY:
                getProperty().clear();
                return;
            case Wfs20Package.UPDATE_TYPE__FILTER:
                setFilter(FILTER_EDEFAULT);
                return;
            case Wfs20Package.UPDATE_TYPE__INPUT_FORMAT:
                unsetInputFormat();
                return;
            case Wfs20Package.UPDATE_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
                return;
            case Wfs20Package.UPDATE_TYPE__TYPE_NAME:
                setTypeName(TYPE_NAME_EDEFAULT);
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
            case Wfs20Package.UPDATE_TYPE__PROPERTY:
                return property != null && !property.isEmpty();
            case Wfs20Package.UPDATE_TYPE__FILTER:
                return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
            case Wfs20Package.UPDATE_TYPE__INPUT_FORMAT:
                return isSetInputFormat();
            case Wfs20Package.UPDATE_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
            case Wfs20Package.UPDATE_TYPE__TYPE_NAME:
                return TYPE_NAME_EDEFAULT == null ? typeName != null : !TYPE_NAME_EDEFAULT.equals(typeName);
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
        result.append(" (filter: ");
        result.append(filter);
        result.append(", inputFormat: ");
        if (inputFormatESet) result.append(inputFormat); else result.append("<unset>");
        result.append(", srsName: ");
        result.append(srsName);
        result.append(", typeName: ");
        result.append(typeName);
        result.append(')');
        return result.toString();
    }

} //UpdateTypeImpl
