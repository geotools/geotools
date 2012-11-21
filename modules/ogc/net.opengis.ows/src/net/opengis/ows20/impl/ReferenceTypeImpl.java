/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.CodeType;
import net.opengis.ows20.LanguageStringType;
import net.opengis.ows20.MetadataType;
import net.opengis.ows20.ReferenceType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.ReferenceTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.ReferenceTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.ReferenceTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.ReferenceTypeImpl#getMetadataGroup <em>Metadata Group</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.ReferenceTypeImpl#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceTypeImpl extends AbstractReferenceBaseTypeImpl implements ReferenceType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected CodeType identifier;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected EList<LanguageStringType> abstract_;

    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final String FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected String format = FORMAT_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ReferenceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.REFERENCE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        CodeType oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.REFERENCE_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        if (newIdentifier != identifier) {
            NotificationChain msgs = null;
            if (identifier != null)
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.REFERENCE_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.REFERENCE_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.REFERENCE_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LanguageStringType> getAbstract() {
        if (abstract_ == null) {
            abstract_ = new EObjectContainmentEList<LanguageStringType>(LanguageStringType.class, this, Ows20Package.REFERENCE_TYPE__ABSTRACT);
        }
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(String newFormat) {
        String oldFormat = format;
        format = newFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.REFERENCE_TYPE__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMetadataGroup() {
        if (metadataGroup == null) {
            metadataGroup = new BasicFeatureMap(this, Ows20Package.REFERENCE_TYPE__METADATA_GROUP);
        }
        return metadataGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MetadataType> getMetadata() {
        return getMetadataGroup().list(Ows20Package.Literals.REFERENCE_TYPE__METADATA);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.REFERENCE_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case Ows20Package.REFERENCE_TYPE__ABSTRACT:
                return ((InternalEList<?>)getAbstract()).basicRemove(otherEnd, msgs);
            case Ows20Package.REFERENCE_TYPE__METADATA_GROUP:
                return ((InternalEList<?>)getMetadataGroup()).basicRemove(otherEnd, msgs);
            case Ows20Package.REFERENCE_TYPE__METADATA:
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
            case Ows20Package.REFERENCE_TYPE__IDENTIFIER:
                return getIdentifier();
            case Ows20Package.REFERENCE_TYPE__ABSTRACT:
                return getAbstract();
            case Ows20Package.REFERENCE_TYPE__FORMAT:
                return getFormat();
            case Ows20Package.REFERENCE_TYPE__METADATA_GROUP:
                if (coreType) return getMetadataGroup();
                return ((FeatureMap.Internal)getMetadataGroup()).getWrapper();
            case Ows20Package.REFERENCE_TYPE__METADATA:
                return getMetadata();
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
            case Ows20Package.REFERENCE_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case Ows20Package.REFERENCE_TYPE__ABSTRACT:
                getAbstract().clear();
                getAbstract().addAll((Collection<? extends LanguageStringType>)newValue);
                return;
            case Ows20Package.REFERENCE_TYPE__FORMAT:
                setFormat((String)newValue);
                return;
            case Ows20Package.REFERENCE_TYPE__METADATA_GROUP:
                ((FeatureMap.Internal)getMetadataGroup()).set(newValue);
                return;
            case Ows20Package.REFERENCE_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection<? extends MetadataType>)newValue);
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
            case Ows20Package.REFERENCE_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case Ows20Package.REFERENCE_TYPE__ABSTRACT:
                getAbstract().clear();
                return;
            case Ows20Package.REFERENCE_TYPE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case Ows20Package.REFERENCE_TYPE__METADATA_GROUP:
                getMetadataGroup().clear();
                return;
            case Ows20Package.REFERENCE_TYPE__METADATA:
                getMetadata().clear();
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
            case Ows20Package.REFERENCE_TYPE__IDENTIFIER:
                return identifier != null;
            case Ows20Package.REFERENCE_TYPE__ABSTRACT:
                return abstract_ != null && !abstract_.isEmpty();
            case Ows20Package.REFERENCE_TYPE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case Ows20Package.REFERENCE_TYPE__METADATA_GROUP:
                return metadataGroup != null && !metadataGroup.isEmpty();
            case Ows20Package.REFERENCE_TYPE__METADATA:
                return !getMetadata().isEmpty();
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
        result.append(" (format: ");
        result.append(format);
        result.append(", metadataGroup: ");
        result.append(metadataGroup);
        result.append(')');
        return result.toString();
    }

} //ReferenceTypeImpl
