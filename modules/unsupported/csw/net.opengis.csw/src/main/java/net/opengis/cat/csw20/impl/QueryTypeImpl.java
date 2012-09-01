/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.ElementSetNameType;
import net.opengis.cat.csw20.QueryConstraintType;
import net.opengis.cat.csw20.QueryType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.QueryTypeImpl#getElementSetName <em>Element Set Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.QueryTypeImpl#getElementName <em>Element Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.QueryTypeImpl#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.QueryTypeImpl#getSortBy <em>Sort By</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.QueryTypeImpl#getTypeNames <em>Type Names</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryTypeImpl extends AbstractQueryTypeImpl implements QueryType {
    /**
     * The cached value of the '{@link #getElementSetName() <em>Element Set Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementSetName()
     * @generated
     * @ordered
     */
    protected ElementSetNameType elementSetName;

    /**
     * The cached value of the '{@link #getElementName() <em>Element Name</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementName()
     * @generated
     * @ordered
     */
    protected EList<QName> elementName;

    /**
     * The cached value of the '{@link #getConstraint() <em>Constraint</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConstraint()
     * @generated
     * @ordered
     */
    protected QueryConstraintType constraint;

    /**
     * The cached value of the '{@link #getSortBy() <em>Sort By</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSortBy()
     * @generated
     * @ordered
     */
    protected SortBy sortBy;

    /**
     * The cached value of the '{@link #getTypeNames() <em>Type Names</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeNames()
     * @generated
     * @ordered
     */
    protected List<QName> typeNames;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected QueryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.QUERY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ElementSetNameType getElementSetName() {
        return elementSetName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetElementSetName(ElementSetNameType newElementSetName, NotificationChain msgs) {
        ElementSetNameType oldElementSetName = elementSetName;
        elementSetName = newElementSetName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME, oldElementSetName, newElementSetName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setElementSetName(ElementSetNameType newElementSetName) {
        if (newElementSetName != elementSetName) {
            NotificationChain msgs = null;
            if (elementSetName != null)
                msgs = ((InternalEObject)elementSetName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME, null, msgs);
            if (newElementSetName != null)
                msgs = ((InternalEObject)newElementSetName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME, null, msgs);
            msgs = basicSetElementSetName(newElementSetName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME, newElementSetName, newElementSetName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<QName> getElementName() {
        if (elementName == null) {
            elementName = new EDataTypeUniqueEList<QName>(QName.class, this, Csw20Package.QUERY_TYPE__ELEMENT_NAME);
        }
        return elementName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryConstraintType getConstraint() {
        return constraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConstraint(QueryConstraintType newConstraint, NotificationChain msgs) {
        QueryConstraintType oldConstraint = constraint;
        constraint = newConstraint;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_TYPE__CONSTRAINT, oldConstraint, newConstraint);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConstraint(QueryConstraintType newConstraint) {
        if (newConstraint != constraint) {
            NotificationChain msgs = null;
            if (constraint != null)
                msgs = ((InternalEObject)constraint).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.QUERY_TYPE__CONSTRAINT, null, msgs);
            if (newConstraint != null)
                msgs = ((InternalEObject)newConstraint).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.QUERY_TYPE__CONSTRAINT, null, msgs);
            msgs = basicSetConstraint(newConstraint, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_TYPE__CONSTRAINT, newConstraint, newConstraint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SortBy getSortBy() {
        return sortBy;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSortBy(SortBy newSortBy, NotificationChain msgs) {
        SortBy oldSortBy = sortBy;
        sortBy = newSortBy;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_TYPE__SORT_BY, oldSortBy, newSortBy);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSortBy(SortBy newSortBy) {
        if (newSortBy != sortBy) {
            NotificationChain msgs = null;
            if (sortBy != null)
                msgs = ((InternalEObject)sortBy).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.QUERY_TYPE__SORT_BY, null, msgs);
            if (newSortBy != null)
                msgs = ((InternalEObject)newSortBy).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.QUERY_TYPE__SORT_BY, null, msgs);
            msgs = basicSetSortBy(newSortBy, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_TYPE__SORT_BY, newSortBy, newSortBy));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<QName> getTypeNames() {
        return typeNames;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTypeNames(List<QName> newTypeNames) {
        List<QName> oldTypeNames = typeNames;
        typeNames = newTypeNames;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.QUERY_TYPE__TYPE_NAMES, oldTypeNames, typeNames));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME:
                return basicSetElementSetName(null, msgs);
            case Csw20Package.QUERY_TYPE__CONSTRAINT:
                return basicSetConstraint(null, msgs);
            case Csw20Package.QUERY_TYPE__SORT_BY:
                return basicSetSortBy(null, msgs);
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
            case Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME:
                return getElementSetName();
            case Csw20Package.QUERY_TYPE__ELEMENT_NAME:
                return getElementName();
            case Csw20Package.QUERY_TYPE__CONSTRAINT:
                return getConstraint();
            case Csw20Package.QUERY_TYPE__SORT_BY:
                return getSortBy();
            case Csw20Package.QUERY_TYPE__TYPE_NAMES:
                return getTypeNames();
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
            case Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME:
                setElementSetName((ElementSetNameType)newValue);
                return;
            case Csw20Package.QUERY_TYPE__ELEMENT_NAME:
                getElementName().clear();
                getElementName().addAll((Collection<? extends QName>)newValue);
                return;
            case Csw20Package.QUERY_TYPE__CONSTRAINT:
                setConstraint((QueryConstraintType)newValue);
                return;
            case Csw20Package.QUERY_TYPE__SORT_BY:
                setSortBy((SortBy)newValue);
                return;
            case Csw20Package.QUERY_TYPE__TYPE_NAMES:
                setTypeNames((List<QName>)newValue);
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
            case Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME:
                setElementSetName((ElementSetNameType)null);
                return;
            case Csw20Package.QUERY_TYPE__ELEMENT_NAME:
                getElementName().clear();
                return;
            case Csw20Package.QUERY_TYPE__CONSTRAINT:
                setConstraint((QueryConstraintType)null);
                return;
            case Csw20Package.QUERY_TYPE__SORT_BY:
                setSortBy((SortBy)null);
                return;
            case Csw20Package.QUERY_TYPE__TYPE_NAMES:
                setTypeNames((List<QName>)null);
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
            case Csw20Package.QUERY_TYPE__ELEMENT_SET_NAME:
                return elementSetName != null;
            case Csw20Package.QUERY_TYPE__ELEMENT_NAME:
                return elementName != null && !elementName.isEmpty();
            case Csw20Package.QUERY_TYPE__CONSTRAINT:
                return constraint != null;
            case Csw20Package.QUERY_TYPE__SORT_BY:
                return sortBy != null;
            case Csw20Package.QUERY_TYPE__TYPE_NAMES:
                return typeNames != null;
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
        result.append(" (elementName: ");
        result.append(elementName);
        result.append(", typeNames: ");
        result.append(typeNames);
        result.append(')');
        return result.toString();
    }

} //QueryTypeImpl
