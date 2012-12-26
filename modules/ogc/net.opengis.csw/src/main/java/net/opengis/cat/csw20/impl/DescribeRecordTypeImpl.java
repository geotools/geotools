/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.util.Collection;
import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DescribeRecordType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Describe Record Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.DescribeRecordTypeImpl#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DescribeRecordTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DescribeRecordTypeImpl#getSchemaLanguage <em>Schema Language</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeRecordTypeImpl extends RequestBaseTypeImpl implements DescribeRecordType {
    /**
     * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
    protected EList<QName> typeName;

    /**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FORMAT_EDEFAULT = "application/xml";

    /**
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected String outputFormat = OUTPUT_FORMAT_EDEFAULT;

    /**
     * This is true if the Output Format attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean outputFormatESet;

    /**
     * The default value of the '{@link #getSchemaLanguage() <em>Schema Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchemaLanguage()
     * @generated
     * @ordered
     */
    protected static final String SCHEMA_LANGUAGE_EDEFAULT = "http://www.w3.org/XML/Schema";

    /**
     * The cached value of the '{@link #getSchemaLanguage() <em>Schema Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchemaLanguage()
     * @generated
     * @ordered
     */
    protected String schemaLanguage = SCHEMA_LANGUAGE_EDEFAULT;

    /**
     * This is true if the Schema Language attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean schemaLanguageESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DescribeRecordTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.DESCRIBE_RECORD_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<QName> getTypeName() {
        if (typeName == null) {
            typeName = new EDataTypeUniqueEList<QName>(QName.class, this, Csw20Package.DESCRIBE_RECORD_TYPE__TYPE_NAME);
        }
        return typeName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFormat(String newOutputFormat) {
        String oldOutputFormat = outputFormat;
        outputFormat = newOutputFormat;
        boolean oldOutputFormatESet = outputFormatESet;
        outputFormatESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetOutputFormat() {
        String oldOutputFormat = outputFormat;
        boolean oldOutputFormatESet = outputFormatESet;
        outputFormat = OUTPUT_FORMAT_EDEFAULT;
        outputFormatESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetOutputFormat() {
        return outputFormatESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSchemaLanguage() {
        return schemaLanguage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSchemaLanguage(String newSchemaLanguage) {
        String oldSchemaLanguage = schemaLanguage;
        schemaLanguage = newSchemaLanguage;
        boolean oldSchemaLanguageESet = schemaLanguageESet;
        schemaLanguageESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE, oldSchemaLanguage, schemaLanguage, !oldSchemaLanguageESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetSchemaLanguage() {
        String oldSchemaLanguage = schemaLanguage;
        boolean oldSchemaLanguageESet = schemaLanguageESet;
        schemaLanguage = SCHEMA_LANGUAGE_EDEFAULT;
        schemaLanguageESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE, oldSchemaLanguage, SCHEMA_LANGUAGE_EDEFAULT, oldSchemaLanguageESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetSchemaLanguage() {
        return schemaLanguageESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.DESCRIBE_RECORD_TYPE__TYPE_NAME:
                return getTypeName();
            case Csw20Package.DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case Csw20Package.DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE:
                return getSchemaLanguage();
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
            case Csw20Package.DESCRIBE_RECORD_TYPE__TYPE_NAME:
                getTypeName().clear();
                getTypeName().addAll((Collection<? extends QName>)newValue);
                return;
            case Csw20Package.DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case Csw20Package.DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE:
                setSchemaLanguage((String)newValue);
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
            case Csw20Package.DESCRIBE_RECORD_TYPE__TYPE_NAME:
                getTypeName().clear();
                return;
            case Csw20Package.DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
                return;
            case Csw20Package.DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE:
                unsetSchemaLanguage();
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
            case Csw20Package.DESCRIBE_RECORD_TYPE__TYPE_NAME:
                return typeName != null && !typeName.isEmpty();
            case Csw20Package.DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
            case Csw20Package.DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE:
                return isSetSchemaLanguage();
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
        result.append(" (typeName: ");
        result.append(typeName);
        result.append(", outputFormat: ");
        if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
        result.append(", schemaLanguage: ");
        if (schemaLanguageESet) result.append(schemaLanguage); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //DescribeRecordTypeImpl
