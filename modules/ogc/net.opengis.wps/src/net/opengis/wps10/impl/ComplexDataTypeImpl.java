/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.util.Collection;
import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Complex Data Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ComplexDataTypeImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ComplexDataTypeImpl#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ComplexDataTypeImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ComplexDataTypeImpl#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComplexDataTypeImpl extends AnyTypeImpl implements ComplexDataType {
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
     * The cached value of the '{@link #getData() <em>Data</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getData()
     * @generated
     * @ordered
     */
    protected EList data;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ComplexDataTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.COMPLEX_DATA_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.COMPLEX_DATA_TYPE__ENCODING, oldEncoding, encoding));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.COMPLEX_DATA_TYPE__MIME_TYPE, oldMimeType, mimeType));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.COMPLEX_DATA_TYPE__SCHEMA, oldSchema, schema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getData() {
        if (data == null) {
            data = new EDataTypeUniqueEList(Object.class, this, Wps10Package.COMPLEX_DATA_TYPE__DATA);
        }
        return data;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.COMPLEX_DATA_TYPE__ENCODING:
                return getEncoding();
            case Wps10Package.COMPLEX_DATA_TYPE__MIME_TYPE:
                return getMimeType();
            case Wps10Package.COMPLEX_DATA_TYPE__SCHEMA:
                return getSchema();
            case Wps10Package.COMPLEX_DATA_TYPE__DATA:
                return getData();
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
            case Wps10Package.COMPLEX_DATA_TYPE__ENCODING:
                setEncoding((String)newValue);
                return;
            case Wps10Package.COMPLEX_DATA_TYPE__MIME_TYPE:
                setMimeType((String)newValue);
                return;
            case Wps10Package.COMPLEX_DATA_TYPE__SCHEMA:
                setSchema((String)newValue);
                return;
            case Wps10Package.COMPLEX_DATA_TYPE__DATA:
                getData().clear();
                getData().addAll((Collection)newValue);
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
            case Wps10Package.COMPLEX_DATA_TYPE__ENCODING:
                setEncoding(ENCODING_EDEFAULT);
                return;
            case Wps10Package.COMPLEX_DATA_TYPE__MIME_TYPE:
                setMimeType(MIME_TYPE_EDEFAULT);
                return;
            case Wps10Package.COMPLEX_DATA_TYPE__SCHEMA:
                setSchema(SCHEMA_EDEFAULT);
                return;
            case Wps10Package.COMPLEX_DATA_TYPE__DATA:
                getData().clear();
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
            case Wps10Package.COMPLEX_DATA_TYPE__ENCODING:
                return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
            case Wps10Package.COMPLEX_DATA_TYPE__MIME_TYPE:
                return MIME_TYPE_EDEFAULT == null ? mimeType != null : !MIME_TYPE_EDEFAULT.equals(mimeType);
            case Wps10Package.COMPLEX_DATA_TYPE__SCHEMA:
                return SCHEMA_EDEFAULT == null ? schema != null : !SCHEMA_EDEFAULT.equals(schema);
            case Wps10Package.COMPLEX_DATA_TYPE__DATA:
                return data != null && !data.isEmpty();
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
        result.append(", data: ");
        result.append(data);
        result.append(')');
        return result.toString();
    }

} //ComplexDataTypeImpl
