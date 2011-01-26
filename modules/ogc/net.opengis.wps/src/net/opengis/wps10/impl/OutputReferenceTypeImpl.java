/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.OutputReferenceType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Reference Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.OutputReferenceTypeImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.OutputReferenceTypeImpl#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.OutputReferenceTypeImpl#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.OutputReferenceTypeImpl#getSchema <em>Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputReferenceTypeImpl extends EObjectImpl implements OutputReferenceType {
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
     * The default value of the '{@link #getHref() <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHref()
     * @generated
     * @ordered
     */
    protected static final String HREF_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHref() <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHref()
     * @generated
     * @ordered
     */
    protected String href = HREF_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OutputReferenceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.OUTPUT_REFERENCE_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_REFERENCE_TYPE__ENCODING, oldEncoding, encoding));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHref() {
        return href;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHref(String newHref) {
        String oldHref = href;
        href = newHref;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_REFERENCE_TYPE__HREF, oldHref, href));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_REFERENCE_TYPE__MIME_TYPE, oldMimeType, mimeType));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_REFERENCE_TYPE__SCHEMA, oldSchema, schema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.OUTPUT_REFERENCE_TYPE__ENCODING:
                return getEncoding();
            case Wps10Package.OUTPUT_REFERENCE_TYPE__HREF:
                return getHref();
            case Wps10Package.OUTPUT_REFERENCE_TYPE__MIME_TYPE:
                return getMimeType();
            case Wps10Package.OUTPUT_REFERENCE_TYPE__SCHEMA:
                return getSchema();
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
            case Wps10Package.OUTPUT_REFERENCE_TYPE__ENCODING:
                setEncoding((String)newValue);
                return;
            case Wps10Package.OUTPUT_REFERENCE_TYPE__HREF:
                setHref((String)newValue);
                return;
            case Wps10Package.OUTPUT_REFERENCE_TYPE__MIME_TYPE:
                setMimeType((String)newValue);
                return;
            case Wps10Package.OUTPUT_REFERENCE_TYPE__SCHEMA:
                setSchema((String)newValue);
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
            case Wps10Package.OUTPUT_REFERENCE_TYPE__ENCODING:
                setEncoding(ENCODING_EDEFAULT);
                return;
            case Wps10Package.OUTPUT_REFERENCE_TYPE__HREF:
                setHref(HREF_EDEFAULT);
                return;
            case Wps10Package.OUTPUT_REFERENCE_TYPE__MIME_TYPE:
                setMimeType(MIME_TYPE_EDEFAULT);
                return;
            case Wps10Package.OUTPUT_REFERENCE_TYPE__SCHEMA:
                setSchema(SCHEMA_EDEFAULT);
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
            case Wps10Package.OUTPUT_REFERENCE_TYPE__ENCODING:
                return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
            case Wps10Package.OUTPUT_REFERENCE_TYPE__HREF:
                return HREF_EDEFAULT == null ? href != null : !HREF_EDEFAULT.equals(href);
            case Wps10Package.OUTPUT_REFERENCE_TYPE__MIME_TYPE:
                return MIME_TYPE_EDEFAULT == null ? mimeType != null : !MIME_TYPE_EDEFAULT.equals(mimeType);
            case Wps10Package.OUTPUT_REFERENCE_TYPE__SCHEMA:
                return SCHEMA_EDEFAULT == null ? schema != null : !SCHEMA_EDEFAULT.equals(schema);
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
        result.append(", href: ");
        result.append(href);
        result.append(", mimeType: ");
        result.append(mimeType);
        result.append(", schema: ");
        result.append(schema);
        result.append(')');
        return result.toString();
    }

} //OutputReferenceTypeImpl
