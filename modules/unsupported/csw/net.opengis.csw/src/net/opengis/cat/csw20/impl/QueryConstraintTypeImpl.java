/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.QueryConstraintType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query Constraint Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.QueryConstraintTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.QueryConstraintTypeImpl#getCqlText <em>Cql Text</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.QueryConstraintTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryConstraintTypeImpl extends EObjectImpl implements QueryConstraintType {
    /**
     * The cached value of the '{@link #getFilter() <em>Filter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected Filter filter;

    /**
     * The default value of the '{@link #getCqlText() <em>Cql Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCqlText()
     * @generated
     * @ordered
     */
    protected static final String CQL_TEXT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCqlText() <em>Cql Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCqlText()
     * @generated
     * @ordered
     */
    protected String cqlText = CQL_TEXT_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected QueryConstraintTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.QUERY_CONSTRAINT_TYPE;
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
    public NotificationChain basicSetFilter(Filter newFilter, NotificationChain msgs) {
        Filter oldFilter = filter;
        filter = newFilter;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER, oldFilter, newFilter);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilter(Filter newFilter) {
        if (newFilter != filter) {
            NotificationChain msgs = null;
            if (filter != null)
                msgs = ((InternalEObject)filter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER, null, msgs);
            if (newFilter != null)
                msgs = ((InternalEObject)newFilter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER, null, msgs);
            msgs = basicSetFilter(newFilter, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER, newFilter, newFilter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCqlText() {
        return cqlText;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCqlText(String newCqlText) {
        String oldCqlText = cqlText;
        cqlText = newCqlText;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_CONSTRAINT_TYPE__CQL_TEXT, oldCqlText, cqlText));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_CONSTRAINT_TYPE__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER:
                return basicSetFilter(null, msgs);
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
            case Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER:
                return getFilter();
            case Csw20Package.QUERY_CONSTRAINT_TYPE__CQL_TEXT:
                return getCqlText();
            case Csw20Package.QUERY_CONSTRAINT_TYPE__VERSION:
                return getVersion();
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
            case Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER:
                setFilter((Filter)newValue);
                return;
            case Csw20Package.QUERY_CONSTRAINT_TYPE__CQL_TEXT:
                setCqlText((String)newValue);
                return;
            case Csw20Package.QUERY_CONSTRAINT_TYPE__VERSION:
                setVersion((String)newValue);
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
            case Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER:
                setFilter((Filter)null);
                return;
            case Csw20Package.QUERY_CONSTRAINT_TYPE__CQL_TEXT:
                setCqlText(CQL_TEXT_EDEFAULT);
                return;
            case Csw20Package.QUERY_CONSTRAINT_TYPE__VERSION:
                setVersion(VERSION_EDEFAULT);
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
            case Csw20Package.QUERY_CONSTRAINT_TYPE__FILTER:
                return filter != null;
            case Csw20Package.QUERY_CONSTRAINT_TYPE__CQL_TEXT:
                return CQL_TEXT_EDEFAULT == null ? cqlText != null : !CQL_TEXT_EDEFAULT.equals(cqlText);
            case Csw20Package.QUERY_CONSTRAINT_TYPE__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
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
        result.append(" (cqlText: ");
        result.append(cqlText);
        result.append(", version: ");
        result.append(version);
        result.append(')');
        return result.toString();
    }

} //QueryConstraintTypeImpl
