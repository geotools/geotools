/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.math.BigInteger;

import net.opengis.wfs.GetGmlObjectType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.opengis.filter.identity.GmlObjectId;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Gml Object Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.GetGmlObjectTypeImpl#getGmlObjectId <em>Gml Object Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetGmlObjectTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetGmlObjectTypeImpl#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetGmlObjectTypeImpl#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetGmlObjectTypeImpl extends BaseRequestTypeImpl implements GetGmlObjectType {
	/**
     * The default value of the '{@link #getGmlObjectId() <em>Gml Object Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getGmlObjectId()
     * @generated
     * @ordered
     */
	protected static final GmlObjectId/*<String>*/ GML_OBJECT_ID_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getGmlObjectId() <em>Gml Object Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getGmlObjectId()
     * @generated
     * @ordered
     */
	protected GmlObjectId/*<String>*/ gmlObjectId = GML_OBJECT_ID_EDEFAULT;

	/**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
	protected static final String OUTPUT_FORMAT_EDEFAULT = "GML3";

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
     * The default value of the '{@link #getTraverseXlinkDepth() <em>Traverse Xlink Depth</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkDepth()
     * @generated
     * @ordered
     */
	protected static final String TRAVERSE_XLINK_DEPTH_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTraverseXlinkDepth() <em>Traverse Xlink Depth</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkDepth()
     * @generated
     * @ordered
     */
	protected String traverseXlinkDepth = TRAVERSE_XLINK_DEPTH_EDEFAULT;

	/**
     * The default value of the '{@link #getTraverseXlinkExpiry() <em>Traverse Xlink Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkExpiry()
     * @generated
     * @ordered
     */
	protected static final BigInteger TRAVERSE_XLINK_EXPIRY_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTraverseXlinkExpiry() <em>Traverse Xlink Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkExpiry()
     * @generated
     * @ordered
     */
	protected BigInteger traverseXlinkExpiry = TRAVERSE_XLINK_EXPIRY_EDEFAULT;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected GetGmlObjectTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.GET_GML_OBJECT_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GmlObjectId/*<String>*/ getGmlObjectId() {
        return gmlObjectId;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setGmlObjectId(GmlObjectId/*<String>*/ newGmlObjectId) {
        Object oldGmlObjectId = gmlObjectId;
        gmlObjectId = newGmlObjectId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_GML_OBJECT_TYPE__GML_OBJECT_ID, oldGmlObjectId, gmlObjectId));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_GML_OBJECT_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.GET_GML_OBJECT_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
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
	public String getTraverseXlinkDepth() {
        return traverseXlinkDepth;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTraverseXlinkDepth(String newTraverseXlinkDepth) {
        String oldTraverseXlinkDepth = traverseXlinkDepth;
        traverseXlinkDepth = newTraverseXlinkDepth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_DEPTH, oldTraverseXlinkDepth, traverseXlinkDepth));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public BigInteger getTraverseXlinkExpiry() {
        return traverseXlinkExpiry;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTraverseXlinkExpiry(BigInteger newTraverseXlinkExpiry) {
        BigInteger oldTraverseXlinkExpiry = traverseXlinkExpiry;
        traverseXlinkExpiry = newTraverseXlinkExpiry;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_EXPIRY, oldTraverseXlinkExpiry, traverseXlinkExpiry));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.GET_GML_OBJECT_TYPE__GML_OBJECT_ID:
                return getGmlObjectId();
            case WfsPackage.GET_GML_OBJECT_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_DEPTH:
                return getTraverseXlinkDepth();
            case WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_EXPIRY:
                return getTraverseXlinkExpiry();
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
            case WfsPackage.GET_GML_OBJECT_TYPE__GML_OBJECT_ID:
                setGmlObjectId((GmlObjectId/*<String>*/) newValue);
                return;
            case WfsPackage.GET_GML_OBJECT_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_DEPTH:
                setTraverseXlinkDepth((String)newValue);
                return;
            case WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_EXPIRY:
                setTraverseXlinkExpiry((BigInteger)newValue);
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
            case WfsPackage.GET_GML_OBJECT_TYPE__GML_OBJECT_ID:
                setGmlObjectId(GML_OBJECT_ID_EDEFAULT);
                return;
            case WfsPackage.GET_GML_OBJECT_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
                return;
            case WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_DEPTH:
                setTraverseXlinkDepth(TRAVERSE_XLINK_DEPTH_EDEFAULT);
                return;
            case WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_EXPIRY:
                setTraverseXlinkExpiry(TRAVERSE_XLINK_EXPIRY_EDEFAULT);
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
            case WfsPackage.GET_GML_OBJECT_TYPE__GML_OBJECT_ID:
                return GML_OBJECT_ID_EDEFAULT == null ? gmlObjectId != null : !GML_OBJECT_ID_EDEFAULT.equals(gmlObjectId);
            case WfsPackage.GET_GML_OBJECT_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
            case WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_DEPTH:
                return TRAVERSE_XLINK_DEPTH_EDEFAULT == null ? traverseXlinkDepth != null : !TRAVERSE_XLINK_DEPTH_EDEFAULT.equals(traverseXlinkDepth);
            case WfsPackage.GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_EXPIRY:
                return TRAVERSE_XLINK_EXPIRY_EDEFAULT == null ? traverseXlinkExpiry != null : !TRAVERSE_XLINK_EXPIRY_EDEFAULT.equals(traverseXlinkExpiry);
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
        result.append(" (gmlObjectId: ");
        result.append(gmlObjectId);
        result.append(", outputFormat: ");
        if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
        result.append(", traverseXlinkDepth: ");
        result.append(traverseXlinkDepth);
        result.append(", traverseXlinkExpiry: ");
        result.append(traverseXlinkExpiry);
        result.append(')');
        return result.toString();
    }

} //GetGmlObjectTypeImpl