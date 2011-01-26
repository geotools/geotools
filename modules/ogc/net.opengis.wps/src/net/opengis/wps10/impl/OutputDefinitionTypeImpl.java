/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.ows11.CodeType;

import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Definition Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.OutputDefinitionTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.OutputDefinitionTypeImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.OutputDefinitionTypeImpl#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.OutputDefinitionTypeImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.OutputDefinitionTypeImpl#getUom <em>Uom</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputDefinitionTypeImpl extends EObjectImpl implements OutputDefinitionType {
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
     * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncoding()
     * @generated
     * @ordered
     */
    protected static final String ENCODING_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncoding()
     * @generated
     * @ordered
     */
    protected String encoding = ENCODING_EDEFAULT;

    /**
     * The default value of the '{@link #getMimeType() <em>Mime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMimeType()
     * @generated
     * @ordered
     */
    protected static final String MIME_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMimeType() <em>Mime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMimeType()
     * @generated
     * @ordered
     */
    protected String mimeType = MIME_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getSchema() <em>Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchema()
     * @generated
     * @ordered
     */
    protected static final String SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSchema() <em>Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchema()
     * @generated
     * @ordered
     */
    protected String schema = SCHEMA_EDEFAULT;

    /**
     * The default value of the '{@link #getUom() <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUom()
     * @generated
     * @ordered
     */
    protected static final String UOM_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUom() <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUom()
     * @generated
     * @ordered
     */
    protected String uom = UOM_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OutputDefinitionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.OUTPUT_DEFINITION_TYPE;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
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
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEncoding(String newEncoding) {
        String oldEncoding = encoding;
        encoding = newEncoding;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DEFINITION_TYPE__ENCODING, oldEncoding, encoding));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMimeType(String newMimeType) {
        String oldMimeType = mimeType;
        mimeType = newMimeType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE, oldMimeType, mimeType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSchema() {
        return schema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSchema(String newSchema) {
        String oldSchema = schema;
        schema = newSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DEFINITION_TYPE__SCHEMA, oldSchema, schema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUom() {
        return uom;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUom(String newUom) {
        String oldUom = uom;
        uom = newUom;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DEFINITION_TYPE__UOM, oldUom, uom));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
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
            case Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER:
                return getIdentifier();
            case Wps10Package.OUTPUT_DEFINITION_TYPE__ENCODING:
                return getEncoding();
            case Wps10Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE:
                return getMimeType();
            case Wps10Package.OUTPUT_DEFINITION_TYPE__SCHEMA:
                return getSchema();
            case Wps10Package.OUTPUT_DEFINITION_TYPE__UOM:
                return getUom();
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
            case Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__ENCODING:
                setEncoding((String)newValue);
                return;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE:
                setMimeType((String)newValue);
                return;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__SCHEMA:
                setSchema((String)newValue);
                return;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__UOM:
                setUom((String)newValue);
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
            case Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__ENCODING:
                setEncoding(ENCODING_EDEFAULT);
                return;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE:
                setMimeType(MIME_TYPE_EDEFAULT);
                return;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__SCHEMA:
                setSchema(SCHEMA_EDEFAULT);
                return;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__UOM:
                setUom(UOM_EDEFAULT);
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
            case Wps10Package.OUTPUT_DEFINITION_TYPE__IDENTIFIER:
                return identifier != null;
            case Wps10Package.OUTPUT_DEFINITION_TYPE__ENCODING:
                return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
            case Wps10Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE:
                return MIME_TYPE_EDEFAULT == null ? mimeType != null : !MIME_TYPE_EDEFAULT.equals(mimeType);
            case Wps10Package.OUTPUT_DEFINITION_TYPE__SCHEMA:
                return SCHEMA_EDEFAULT == null ? schema != null : !SCHEMA_EDEFAULT.equals(schema);
            case Wps10Package.OUTPUT_DEFINITION_TYPE__UOM:
                return UOM_EDEFAULT == null ? uom != null : !UOM_EDEFAULT.equals(uom);
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
        result.append(" (encoding: ");
        result.append(encoding);
        result.append(", mimeType: ");
        result.append(mimeType);
        result.append(", schema: ");
        result.append(schema);
        result.append(", uom: ");
        result.append(uom);
        result.append(')');
        return result.toString();
    }

} //OutputDefinitionTypeImpl
