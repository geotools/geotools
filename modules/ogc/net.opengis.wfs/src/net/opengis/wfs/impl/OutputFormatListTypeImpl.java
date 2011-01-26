/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;
import net.opengis.wfs.OutputFormatListType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Format List Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.OutputFormatListTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.OutputFormatListTypeImpl#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputFormatListTypeImpl extends EObjectImpl implements OutputFormatListType {
	/**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
	protected FeatureMap group;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected OutputFormatListTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.OUTPUT_FORMAT_LIST_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, WfsPackage.OUTPUT_FORMAT_LIST_TYPE__GROUP);
        }
        return group;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getFormat() {
        return getGroup().list(WfsPackage.Literals.OUTPUT_FORMAT_LIST_TYPE__FORMAT);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__GROUP:
                return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
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
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__FORMAT:
                return getFormat();
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
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__FORMAT:
                getFormat().clear();
                getFormat().addAll((Collection)newValue);
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
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__GROUP:
                getGroup().clear();
                return;
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__FORMAT:
                getFormat().clear();
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
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case WfsPackage.OUTPUT_FORMAT_LIST_TYPE__FORMAT:
                return !getFormat().isEmpty();
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
        result.append(" (group: ");
        result.append(group);
        result.append(')');
        return result.toString();
    }

} //OutputFormatListTypeImpl