/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Describe Feature Type Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.DescribeFeatureTypeTypeImpl#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DescribeFeatureTypeTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeFeatureTypeTypeImpl extends BaseRequestTypeImpl implements DescribeFeatureTypeType {
	/**
     * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
	protected EList typeName;

	/**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
	protected static final String OUTPUT_FORMAT_EDEFAULT = "text/xml; subtype=gml/3.1.1";

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
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected DescribeFeatureTypeTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.DESCRIBE_FEATURE_TYPE_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getTypeName() {
        if (typeName == null) {
            typeName = new EDataTypeUniqueEList(QName.class, this, WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME);
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME:
                return getTypeName();
            case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
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
            case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME:
                getTypeName().clear();
                getTypeName().addAll((Collection)newValue);
                return;
            case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
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
            case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME:
                getTypeName().clear();
                return;
            case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
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
            case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME:
                return typeName != null && !typeName.isEmpty();
            case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
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
        result.append(" (typeName: ");
        result.append(typeName);
        result.append(", outputFormat: ");
        if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //DescribeFeatureTypeTypeImpl