/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import java.util.Collection;
import java.util.List;

import net.opengis.fes20.AbstractAdhocQueryExpressionType;
import net.opengis.fes20.Fes20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Adhoc Query Expression Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl#getAbstractProjectionClause <em>Abstract Projection Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl#getAbstractSelectionClause <em>Abstract Selection Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl#getAbstractSortingClause <em>Abstract Sorting Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl#getAliases <em>Aliases</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl#getTypeNames <em>Type Names</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractAdhocQueryExpressionTypeImpl extends AbstractQueryExpressionTypeImpl implements AbstractAdhocQueryExpressionType {
    /**
     * The cached value of the '{@link #getAbstractProjectionClause() <em>Abstract Projection Clause</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractProjectionClause()
     * @generated
     * @ordered
     */
    protected EList<Object> abstractProjectionClause;

    /**
     * The default value of the '{@link #getAbstractSelectionClause() <em>Abstract Selection Clause</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractSelectionClause()
     * @generated
     * @ordered
     */
    protected static final Object ABSTRACT_SELECTION_CLAUSE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAbstractSelectionClause() <em>Abstract Selection Clause</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractSelectionClause()
     * @generated
     * @ordered
     */
    protected Object abstractSelectionClause = ABSTRACT_SELECTION_CLAUSE_EDEFAULT;

    /**
     * The default value of the '{@link #getAbstractSortingClause() <em>Abstract Sorting Clause</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractSortingClause()
     * @generated
     * @ordered
     */
    protected static final Object ABSTRACT_SORTING_CLAUSE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAbstractSortingClause() <em>Abstract Sorting Clause</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractSortingClause()
     * @generated
     * @ordered
     */
    protected Object abstractSortingClause = ABSTRACT_SORTING_CLAUSE_EDEFAULT;

    /**
     * The cached value of the '{@link #getAliases() <em>Aliases</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAliases()
     * @generated
     * @ordered
     */
    protected EList<String> aliases;

    /**
     * The cached value of the '{@link #getTypeNames() <em>Type Names</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeNames()
     * @generated
     * @ordered
     */
    protected EList<Object> typeNames;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractAdhocQueryExpressionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Object> getAbstractProjectionClause() {
        if (abstractProjectionClause == null) {
            abstractProjectionClause = new EDataTypeUniqueEList<Object>(Object.class, this, Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_PROJECTION_CLAUSE);
        }
        return abstractProjectionClause;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getAbstractSelectionClause() {
        return abstractSelectionClause;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAbstractSelectionClause(Object newAbstractSelectionClause) {
        Object oldAbstractSelectionClause = abstractSelectionClause;
        abstractSelectionClause = newAbstractSelectionClause;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SELECTION_CLAUSE, oldAbstractSelectionClause, abstractSelectionClause));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getAbstractSortingClause() {
        return abstractSortingClause;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAbstractSortingClause(Object newAbstractSortingClause) {
        Object oldAbstractSortingClause = abstractSortingClause;
        abstractSortingClause = newAbstractSortingClause;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SORTING_CLAUSE, oldAbstractSortingClause, abstractSortingClause));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getAliases() {
        if (aliases == null) {
            aliases = new EDataTypeUniqueEList<String>(String.class, this, Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ALIASES);
        }
        return aliases;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public EList<Object> getTypeNames() {
        if (typeNames == null) {
            typeNames = new EDataTypeEList<Object>(Object.class, this, Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__TYPE_NAMES);
        }
        return typeNames;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_PROJECTION_CLAUSE:
                return getAbstractProjectionClause();
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SELECTION_CLAUSE:
                return getAbstractSelectionClause();
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SORTING_CLAUSE:
                return getAbstractSortingClause();
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ALIASES:
                return getAliases();
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__TYPE_NAMES:
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
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_PROJECTION_CLAUSE:
                getAbstractProjectionClause().clear();
                getAbstractProjectionClause().addAll((Collection<? extends Object>)newValue);
                return;
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SELECTION_CLAUSE:
                setAbstractSelectionClause(newValue);
                return;
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SORTING_CLAUSE:
                setAbstractSortingClause(newValue);
                return;
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ALIASES:
                getAliases().clear();
                getAliases().addAll((Collection<? extends String>)newValue);
                return;
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__TYPE_NAMES:
                getTypeNames().clear();
                getTypeNames().addAll((Collection<? extends Object>)newValue);
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
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_PROJECTION_CLAUSE:
                getAbstractProjectionClause().clear();
                return;
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SELECTION_CLAUSE:
                setAbstractSelectionClause(ABSTRACT_SELECTION_CLAUSE_EDEFAULT);
                return;
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SORTING_CLAUSE:
                setAbstractSortingClause(ABSTRACT_SORTING_CLAUSE_EDEFAULT);
                return;
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ALIASES:
                getAliases().clear();
                return;
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__TYPE_NAMES:
                getTypeNames().clear();
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
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_PROJECTION_CLAUSE:
                return abstractProjectionClause != null && !abstractProjectionClause.isEmpty();
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SELECTION_CLAUSE:
                return ABSTRACT_SELECTION_CLAUSE_EDEFAULT == null ? abstractSelectionClause != null : !ABSTRACT_SELECTION_CLAUSE_EDEFAULT.equals(abstractSelectionClause);
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SORTING_CLAUSE:
                return ABSTRACT_SORTING_CLAUSE_EDEFAULT == null ? abstractSortingClause != null : !ABSTRACT_SORTING_CLAUSE_EDEFAULT.equals(abstractSortingClause);
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ALIASES:
                return aliases != null && !aliases.isEmpty();
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__TYPE_NAMES:
                return typeNames != null && !typeNames.isEmpty();
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
        result.append(" (abstractProjectionClause: ");
        result.append(abstractProjectionClause);
        result.append(", abstractSelectionClause: ");
        result.append(abstractSelectionClause);
        result.append(", abstractSortingClause: ");
        result.append(abstractSortingClause);
        result.append(", aliases: ");
        result.append(aliases);
        result.append(", typeNames: ");
        result.append(typeNames);
        result.append(')');
        return result.toString();
    }

} //AbstractAdhocQueryExpressionTypeImpl
