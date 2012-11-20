/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.DCPType;
import net.opengis.ows20.DomainType;
import net.opengis.ows20.MetadataType;
import net.opengis.ows20.OperationType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.OperationTypeImpl#getDCP <em>DCP</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.OperationTypeImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.OperationTypeImpl#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.OperationTypeImpl#getMetadataGroup <em>Metadata Group</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.OperationTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.OperationTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationTypeImpl extends EObjectImpl implements OperationType {
    /**
     * The cached value of the '{@link #getDCP() <em>DCP</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDCP()
     * @generated
     * @ordered
     */
    protected EList<DCPType> dCP;

    /**
     * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameter()
     * @generated
     * @ordered
     */
    protected EList<DomainType> parameter;

    /**
     * The cached value of the '{@link #getConstraint() <em>Constraint</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConstraint()
     * @generated
     * @ordered
     */
    protected EList<DomainType> constraint;

    /**
     * The cached value of the '{@link #getMetadataGroup() <em>Metadata Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadataGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap metadataGroup;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OperationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.OPERATION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DCPType> getDCP() {
        if (dCP == null) {
            dCP = new EObjectContainmentEList<DCPType>(DCPType.class, this, Ows20Package.OPERATION_TYPE__DCP);
        }
        return dCP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DomainType> getParameter() {
        if (parameter == null) {
            parameter = new EObjectContainmentEList<DomainType>(DomainType.class, this, Ows20Package.OPERATION_TYPE__PARAMETER);
        }
        return parameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DomainType> getConstraint() {
        if (constraint == null) {
            constraint = new EObjectContainmentEList<DomainType>(DomainType.class, this, Ows20Package.OPERATION_TYPE__CONSTRAINT);
        }
        return constraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMetadataGroup() {
        if (metadataGroup == null) {
            metadataGroup = new BasicFeatureMap(this, Ows20Package.OPERATION_TYPE__METADATA_GROUP);
        }
        return metadataGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MetadataType> getMetadata() {
        return getMetadataGroup().list(Ows20Package.Literals.OPERATION_TYPE__METADATA);
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.OPERATION_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.OPERATION_TYPE__DCP:
                return ((InternalEList<?>)getDCP()).basicRemove(otherEnd, msgs);
            case Ows20Package.OPERATION_TYPE__PARAMETER:
                return ((InternalEList<?>)getParameter()).basicRemove(otherEnd, msgs);
            case Ows20Package.OPERATION_TYPE__CONSTRAINT:
                return ((InternalEList<?>)getConstraint()).basicRemove(otherEnd, msgs);
            case Ows20Package.OPERATION_TYPE__METADATA_GROUP:
                return ((InternalEList<?>)getMetadataGroup()).basicRemove(otherEnd, msgs);
            case Ows20Package.OPERATION_TYPE__METADATA:
                return ((InternalEList<?>)getMetadata()).basicRemove(otherEnd, msgs);
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
            case Ows20Package.OPERATION_TYPE__DCP:
                return getDCP();
            case Ows20Package.OPERATION_TYPE__PARAMETER:
                return getParameter();
            case Ows20Package.OPERATION_TYPE__CONSTRAINT:
                return getConstraint();
            case Ows20Package.OPERATION_TYPE__METADATA_GROUP:
                if (coreType) return getMetadataGroup();
                return ((FeatureMap.Internal)getMetadataGroup()).getWrapper();
            case Ows20Package.OPERATION_TYPE__METADATA:
                return getMetadata();
            case Ows20Package.OPERATION_TYPE__NAME:
                return getName();
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
            case Ows20Package.OPERATION_TYPE__DCP:
                getDCP().clear();
                getDCP().addAll((Collection<? extends DCPType>)newValue);
                return;
            case Ows20Package.OPERATION_TYPE__PARAMETER:
                getParameter().clear();
                getParameter().addAll((Collection<? extends DomainType>)newValue);
                return;
            case Ows20Package.OPERATION_TYPE__CONSTRAINT:
                getConstraint().clear();
                getConstraint().addAll((Collection<? extends DomainType>)newValue);
                return;
            case Ows20Package.OPERATION_TYPE__METADATA_GROUP:
                ((FeatureMap.Internal)getMetadataGroup()).set(newValue);
                return;
            case Ows20Package.OPERATION_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection<? extends MetadataType>)newValue);
                return;
            case Ows20Package.OPERATION_TYPE__NAME:
                setName((String)newValue);
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
            case Ows20Package.OPERATION_TYPE__DCP:
                getDCP().clear();
                return;
            case Ows20Package.OPERATION_TYPE__PARAMETER:
                getParameter().clear();
                return;
            case Ows20Package.OPERATION_TYPE__CONSTRAINT:
                getConstraint().clear();
                return;
            case Ows20Package.OPERATION_TYPE__METADATA_GROUP:
                getMetadataGroup().clear();
                return;
            case Ows20Package.OPERATION_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Ows20Package.OPERATION_TYPE__NAME:
                setName(NAME_EDEFAULT);
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
            case Ows20Package.OPERATION_TYPE__DCP:
                return dCP != null && !dCP.isEmpty();
            case Ows20Package.OPERATION_TYPE__PARAMETER:
                return parameter != null && !parameter.isEmpty();
            case Ows20Package.OPERATION_TYPE__CONSTRAINT:
                return constraint != null && !constraint.isEmpty();
            case Ows20Package.OPERATION_TYPE__METADATA_GROUP:
                return metadataGroup != null && !metadataGroup.isEmpty();
            case Ows20Package.OPERATION_TYPE__METADATA:
                return !getMetadata().isEmpty();
            case Ows20Package.OPERATION_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
        result.append(" (metadataGroup: ");
        result.append(metadataGroup);
        result.append(", name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }

} //OperationTypeImpl
