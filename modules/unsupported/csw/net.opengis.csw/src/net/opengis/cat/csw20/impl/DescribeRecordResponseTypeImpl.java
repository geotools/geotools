/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.util.Collection;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DescribeRecordResponseType;
import net.opengis.cat.csw20.SchemaComponentType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Describe Record Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.DescribeRecordResponseTypeImpl#getSchemaComponent <em>Schema Component</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeRecordResponseTypeImpl extends EObjectImpl implements DescribeRecordResponseType {
    /**
     * The cached value of the '{@link #getSchemaComponent() <em>Schema Component</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchemaComponent()
     * @generated
     * @ordered
     */
    protected EList<SchemaComponentType> schemaComponent;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DescribeRecordResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.DESCRIBE_RECORD_RESPONSE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SchemaComponentType> getSchemaComponent() {
        if (schemaComponent == null) {
            schemaComponent = new EObjectContainmentEList<SchemaComponentType>(SchemaComponentType.class, this, Csw20Package.DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT);
        }
        return schemaComponent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT:
                return ((InternalEList<?>)getSchemaComponent()).basicRemove(otherEnd, msgs);
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
            case Csw20Package.DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT:
                return getSchemaComponent();
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
            case Csw20Package.DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT:
                getSchemaComponent().clear();
                getSchemaComponent().addAll((Collection<? extends SchemaComponentType>)newValue);
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
            case Csw20Package.DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT:
                getSchemaComponent().clear();
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
            case Csw20Package.DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT:
                return schemaComponent != null && !schemaComponent.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //DescribeRecordResponseTypeImpl
