/**
 */
package net.opengis.wmts.v_1.impl;

import net.opengis.gml311.AbstractFeatureCollectionType;

import net.opengis.wmts.v_1.BinaryPayloadType;
import net.opengis.wmts.v_1.FeatureInfoResponseType;
import net.opengis.wmts.v_1.TextPayloadType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Info Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl#getFeatureCollectionGroup <em>Feature Collection Group</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl#getTextPayload <em>Text Payload</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl#getBinaryPayload <em>Binary Payload</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl#getAnyContent <em>Any Content</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureInfoResponseTypeImpl extends MinimalEObjectImpl.Container implements FeatureInfoResponseType {
    /**
     * The cached value of the '{@link #getFeatureCollectionGroup() <em>Feature Collection Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureCollectionGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap featureCollectionGroup;

    /**
     * The cached value of the '{@link #getTextPayload() <em>Text Payload</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextPayload()
     * @generated
     * @ordered
     */
    protected TextPayloadType textPayload;

    /**
     * The cached value of the '{@link #getBinaryPayload() <em>Binary Payload</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBinaryPayload()
     * @generated
     * @ordered
     */
    protected BinaryPayloadType binaryPayload;

    /**
     * The cached value of the '{@link #getAnyContent() <em>Any Content</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnyContent()
     * @generated
     * @ordered
     */
    protected EObject anyContent;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FeatureInfoResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.FEATURE_INFO_RESPONSE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getFeatureCollectionGroup() {
        if (featureCollectionGroup == null) {
            featureCollectionGroup = new BasicFeatureMap(this, wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP);
        }
        return featureCollectionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractFeatureCollectionType getFeatureCollection() {
        return (AbstractFeatureCollectionType)getFeatureCollectionGroup().get(wmtsv_1Package.Literals.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFeatureCollection(AbstractFeatureCollectionType newFeatureCollection, NotificationChain msgs) {
        return ((FeatureMap.Internal)getFeatureCollectionGroup()).basicAdd(wmtsv_1Package.Literals.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION, newFeatureCollection, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TextPayloadType getTextPayload() {
        return textPayload;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTextPayload(TextPayloadType newTextPayload, NotificationChain msgs) {
        TextPayloadType oldTextPayload = textPayload;
        textPayload = newTextPayload;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD, oldTextPayload, newTextPayload);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTextPayload(TextPayloadType newTextPayload) {
        if (newTextPayload != textPayload) {
            NotificationChain msgs = null;
            if (textPayload != null)
                msgs = ((InternalEObject)textPayload).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD, null, msgs);
            if (newTextPayload != null)
                msgs = ((InternalEObject)newTextPayload).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD, null, msgs);
            msgs = basicSetTextPayload(newTextPayload, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD, newTextPayload, newTextPayload));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryPayloadType getBinaryPayload() {
        return binaryPayload;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBinaryPayload(BinaryPayloadType newBinaryPayload, NotificationChain msgs) {
        BinaryPayloadType oldBinaryPayload = binaryPayload;
        binaryPayload = newBinaryPayload;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD, oldBinaryPayload, newBinaryPayload);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBinaryPayload(BinaryPayloadType newBinaryPayload) {
        if (newBinaryPayload != binaryPayload) {
            NotificationChain msgs = null;
            if (binaryPayload != null)
                msgs = ((InternalEObject)binaryPayload).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD, null, msgs);
            if (newBinaryPayload != null)
                msgs = ((InternalEObject)newBinaryPayload).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD, null, msgs);
            msgs = basicSetBinaryPayload(newBinaryPayload, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD, newBinaryPayload, newBinaryPayload));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getAnyContent() {
        return anyContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnyContent(EObject newAnyContent, NotificationChain msgs) {
        EObject oldAnyContent = anyContent;
        anyContent = newAnyContent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT, oldAnyContent, newAnyContent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnyContent(EObject newAnyContent) {
        if (newAnyContent != anyContent) {
            NotificationChain msgs = null;
            if (anyContent != null)
                msgs = ((InternalEObject)anyContent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT, null, msgs);
            if (newAnyContent != null)
                msgs = ((InternalEObject)newAnyContent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT, null, msgs);
            msgs = basicSetAnyContent(newAnyContent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT, newAnyContent, newAnyContent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP:
                return ((InternalEList<?>)getFeatureCollectionGroup()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION:
                return basicSetFeatureCollection(null, msgs);
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD:
                return basicSetTextPayload(null, msgs);
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD:
                return basicSetBinaryPayload(null, msgs);
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT:
                return basicSetAnyContent(null, msgs);
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
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP:
                if (coreType) return getFeatureCollectionGroup();
                return ((FeatureMap.Internal)getFeatureCollectionGroup()).getWrapper();
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION:
                return getFeatureCollection();
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD:
                return getTextPayload();
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD:
                return getBinaryPayload();
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT:
                return getAnyContent();
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
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP:
                ((FeatureMap.Internal)getFeatureCollectionGroup()).set(newValue);
                return;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD:
                setTextPayload((TextPayloadType)newValue);
                return;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD:
                setBinaryPayload((BinaryPayloadType)newValue);
                return;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT:
                setAnyContent((EObject)newValue);
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
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP:
                getFeatureCollectionGroup().clear();
                return;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD:
                setTextPayload((TextPayloadType)null);
                return;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD:
                setBinaryPayload((BinaryPayloadType)null);
                return;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT:
                setAnyContent((EObject)null);
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
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP:
                return featureCollectionGroup != null && !featureCollectionGroup.isEmpty();
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION:
                return getFeatureCollection() != null;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD:
                return textPayload != null;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD:
                return binaryPayload != null;
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT:
                return anyContent != null;
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
        result.append(" (featureCollectionGroup: ");
        result.append(featureCollectionGroup);
        result.append(')');
        return result.toString();
    }

} //FeatureInfoResponseTypeImpl
