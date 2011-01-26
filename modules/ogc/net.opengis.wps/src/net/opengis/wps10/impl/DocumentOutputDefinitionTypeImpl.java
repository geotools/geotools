/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.ows11.LanguageStringType;

import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Output Definition Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.DocumentOutputDefinitionTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentOutputDefinitionTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentOutputDefinitionTypeImpl#isAsReference <em>As Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentOutputDefinitionTypeImpl extends OutputDefinitionTypeImpl implements DocumentOutputDefinitionType {
    /**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected LanguageStringType title;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected LanguageStringType abstract_;

    /**
     * The default value of the '{@link #isAsReference() <em>As Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAsReference()
     * @generated
     * @ordered
     */
    protected static final boolean AS_REFERENCE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAsReference() <em>As Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAsReference()
     * @generated
     * @ordered
     */
    protected boolean asReference = AS_REFERENCE_EDEFAULT;

    /**
     * This is true if the As Reference attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean asReferenceESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DocumentOutputDefinitionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.DOCUMENT_OUTPUT_DEFINITION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageStringType getTitle() {
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTitle(LanguageStringType newTitle, NotificationChain msgs) {
        LanguageStringType oldTitle = title;
        title = newTitle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE, oldTitle, newTitle);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTitle(LanguageStringType newTitle) {
        if (newTitle != title) {
            NotificationChain msgs = null;
            if (title != null)
                msgs = ((InternalEObject)title).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE, null, msgs);
            if (newTitle != null)
                msgs = ((InternalEObject)newTitle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE, null, msgs);
            msgs = basicSetTitle(newTitle, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE, newTitle, newTitle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageStringType getAbstract() {
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstract(LanguageStringType newAbstract, NotificationChain msgs) {
        LanguageStringType oldAbstract = abstract_;
        abstract_ = newAbstract;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT, oldAbstract, newAbstract);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAbstract(LanguageStringType newAbstract) {
        if (newAbstract != abstract_) {
            NotificationChain msgs = null;
            if (abstract_ != null)
                msgs = ((InternalEObject)abstract_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT, null, msgs);
            if (newAbstract != null)
                msgs = ((InternalEObject)newAbstract).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT, null, msgs);
            msgs = basicSetAbstract(newAbstract, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT, newAbstract, newAbstract));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isAsReference() {
        return asReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAsReference(boolean newAsReference) {
        boolean oldAsReference = asReference;
        asReference = newAsReference;
        boolean oldAsReferenceESet = asReferenceESet;
        asReferenceESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE, oldAsReference, asReference, !oldAsReferenceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetAsReference() {
        boolean oldAsReference = asReference;
        boolean oldAsReferenceESet = asReferenceESet;
        asReference = AS_REFERENCE_EDEFAULT;
        asReferenceESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE, oldAsReference, AS_REFERENCE_EDEFAULT, oldAsReferenceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetAsReference() {
        return asReferenceESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE:
                return basicSetTitle(null, msgs);
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT:
                return basicSetAbstract(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE:
                return getTitle();
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT:
                return getAbstract();
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE:
                return isAsReference() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE:
                setTitle((LanguageStringType)newValue);
                return;
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT:
                setAbstract((LanguageStringType)newValue);
                return;
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE:
                setAsReference(((Boolean)newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE:
                setTitle((LanguageStringType)null);
                return;
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT:
                setAbstract((LanguageStringType)null);
                return;
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE:
                unsetAsReference();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE:
                return title != null;
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT:
                return abstract_ != null;
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE:
                return isSetAsReference();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (asReference: ");
        if (asReferenceESet) result.append(asReference); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //DocumentOutputDefinitionTypeImpl
