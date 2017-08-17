/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.gml311.AbstractGeneralOperationParameterRefType;
import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IdentifierType;
import net.opengis.gml311.OperationMethodType;
import net.opengis.gml311.StringOrRefType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Method Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.OperationMethodTypeImpl#getMethodID <em>Method ID</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OperationMethodTypeImpl#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OperationMethodTypeImpl#getMethodFormula <em>Method Formula</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OperationMethodTypeImpl#getSourceDimensions <em>Source Dimensions</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OperationMethodTypeImpl#getTargetDimensions <em>Target Dimensions</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OperationMethodTypeImpl#getUsesParameter <em>Uses Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationMethodTypeImpl extends OperationMethodBaseTypeImpl implements OperationMethodType {
    /**
     * The cached value of the '{@link #getMethodID() <em>Method ID</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethodID()
     * @generated
     * @ordered
     */
    protected EList<IdentifierType> methodID;

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
     * The cached value of the '{@link #getMethodFormula() <em>Method Formula</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethodFormula()
     * @generated
     * @ordered
     */
    protected CodeType methodFormula;

    /**
     * The default value of the '{@link #getSourceDimensions() <em>Source Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceDimensions()
     * @generated
     * @ordered
     */
    protected static final BigInteger SOURCE_DIMENSIONS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSourceDimensions() <em>Source Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceDimensions()
     * @generated
     * @ordered
     */
    protected BigInteger sourceDimensions = SOURCE_DIMENSIONS_EDEFAULT;

    /**
     * The default value of the '{@link #getTargetDimensions() <em>Target Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetDimensions()
     * @generated
     * @ordered
     */
    protected static final BigInteger TARGET_DIMENSIONS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTargetDimensions() <em>Target Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetDimensions()
     * @generated
     * @ordered
     */
    protected BigInteger targetDimensions = TARGET_DIMENSIONS_EDEFAULT;

    /**
     * The cached value of the '{@link #getUsesParameter() <em>Uses Parameter</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesParameter()
     * @generated
     * @ordered
     */
    protected EList<AbstractGeneralOperationParameterRefType> usesParameter;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OperationMethodTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getOperationMethodType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IdentifierType> getMethodID() {
        if (methodID == null) {
            methodID = new EObjectContainmentEList<IdentifierType>(IdentifierType.class, this, Gml311Package.OPERATION_METHOD_TYPE__METHOD_ID);
        }
        return methodID;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_METHOD_TYPE__REMARKS, oldRemarks, newRemarks);
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
                msgs = ((InternalEObject)remarks).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_METHOD_TYPE__REMARKS, null, msgs);
            if (newRemarks != null)
                msgs = ((InternalEObject)newRemarks).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_METHOD_TYPE__REMARKS, null, msgs);
            msgs = basicSetRemarks(newRemarks, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_METHOD_TYPE__REMARKS, newRemarks, newRemarks));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getMethodFormula() {
        return methodFormula;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMethodFormula(CodeType newMethodFormula, NotificationChain msgs) {
        CodeType oldMethodFormula = methodFormula;
        methodFormula = newMethodFormula;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA, oldMethodFormula, newMethodFormula);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMethodFormula(CodeType newMethodFormula) {
        if (newMethodFormula != methodFormula) {
            NotificationChain msgs = null;
            if (methodFormula != null)
                msgs = ((InternalEObject)methodFormula).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA, null, msgs);
            if (newMethodFormula != null)
                msgs = ((InternalEObject)newMethodFormula).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA, null, msgs);
            msgs = basicSetMethodFormula(newMethodFormula, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA, newMethodFormula, newMethodFormula));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getSourceDimensions() {
        return sourceDimensions;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceDimensions(BigInteger newSourceDimensions) {
        BigInteger oldSourceDimensions = sourceDimensions;
        sourceDimensions = newSourceDimensions;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_METHOD_TYPE__SOURCE_DIMENSIONS, oldSourceDimensions, sourceDimensions));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getTargetDimensions() {
        return targetDimensions;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetDimensions(BigInteger newTargetDimensions) {
        BigInteger oldTargetDimensions = targetDimensions;
        targetDimensions = newTargetDimensions;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_METHOD_TYPE__TARGET_DIMENSIONS, oldTargetDimensions, targetDimensions));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractGeneralOperationParameterRefType> getUsesParameter() {
        if (usesParameter == null) {
            usesParameter = new EObjectContainmentEList<AbstractGeneralOperationParameterRefType>(AbstractGeneralOperationParameterRefType.class, this, Gml311Package.OPERATION_METHOD_TYPE__USES_PARAMETER);
        }
        return usesParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_ID:
                return ((InternalEList<?>)getMethodID()).basicRemove(otherEnd, msgs);
            case Gml311Package.OPERATION_METHOD_TYPE__REMARKS:
                return basicSetRemarks(null, msgs);
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA:
                return basicSetMethodFormula(null, msgs);
            case Gml311Package.OPERATION_METHOD_TYPE__USES_PARAMETER:
                return ((InternalEList<?>)getUsesParameter()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_ID:
                return getMethodID();
            case Gml311Package.OPERATION_METHOD_TYPE__REMARKS:
                return getRemarks();
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA:
                return getMethodFormula();
            case Gml311Package.OPERATION_METHOD_TYPE__SOURCE_DIMENSIONS:
                return getSourceDimensions();
            case Gml311Package.OPERATION_METHOD_TYPE__TARGET_DIMENSIONS:
                return getTargetDimensions();
            case Gml311Package.OPERATION_METHOD_TYPE__USES_PARAMETER:
                return getUsesParameter();
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
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_ID:
                getMethodID().clear();
                getMethodID().addAll((Collection<? extends IdentifierType>)newValue);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__REMARKS:
                setRemarks((StringOrRefType)newValue);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA:
                setMethodFormula((CodeType)newValue);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__SOURCE_DIMENSIONS:
                setSourceDimensions((BigInteger)newValue);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__TARGET_DIMENSIONS:
                setTargetDimensions((BigInteger)newValue);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__USES_PARAMETER:
                getUsesParameter().clear();
                getUsesParameter().addAll((Collection<? extends AbstractGeneralOperationParameterRefType>)newValue);
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
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_ID:
                getMethodID().clear();
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__REMARKS:
                setRemarks((StringOrRefType)null);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA:
                setMethodFormula((CodeType)null);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__SOURCE_DIMENSIONS:
                setSourceDimensions(SOURCE_DIMENSIONS_EDEFAULT);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__TARGET_DIMENSIONS:
                setTargetDimensions(TARGET_DIMENSIONS_EDEFAULT);
                return;
            case Gml311Package.OPERATION_METHOD_TYPE__USES_PARAMETER:
                getUsesParameter().clear();
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
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_ID:
                return methodID != null && !methodID.isEmpty();
            case Gml311Package.OPERATION_METHOD_TYPE__REMARKS:
                return remarks != null;
            case Gml311Package.OPERATION_METHOD_TYPE__METHOD_FORMULA:
                return methodFormula != null;
            case Gml311Package.OPERATION_METHOD_TYPE__SOURCE_DIMENSIONS:
                return SOURCE_DIMENSIONS_EDEFAULT == null ? sourceDimensions != null : !SOURCE_DIMENSIONS_EDEFAULT.equals(sourceDimensions);
            case Gml311Package.OPERATION_METHOD_TYPE__TARGET_DIMENSIONS:
                return TARGET_DIMENSIONS_EDEFAULT == null ? targetDimensions != null : !TARGET_DIMENSIONS_EDEFAULT.equals(targetDimensions);
            case Gml311Package.OPERATION_METHOD_TYPE__USES_PARAMETER:
                return usesParameter != null && !usesParameter.isEmpty();
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
        result.append(" (sourceDimensions: ");
        result.append(sourceDimensions);
        result.append(", targetDimensions: ");
        result.append(targetDimensions);
        result.append(')');
        return result.toString();
    }

} //OperationMethodTypeImpl
