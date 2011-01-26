/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import net.opengis.wfs.UpdateElementType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Versioned Update Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.VersionedUpdateElementType#getFeatureVersion <em>Feature Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getVersionedUpdateElementType()
 * @model extendedMetaData="name='VersionedUpdateElementType' kind='elementOnly'"
 * @generated
 */
public interface VersionedUpdateElementType extends UpdateElementType {
    /**
     * Returns the value of the '<em><b>Feature Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               The syntax and semantics is the same as featureVersion in Query. If specified, update
     *               will check that every updated feature is still at the specified version before
     *               executing, and will fail if a change occurred on the server in the meantime.
     *             
     * <!-- end-model-doc -->
     * @return the value of the '<em>Feature Version</em>' attribute.
     * @see #setFeatureVersion(String)
     * @see net.opengis.wfsv.WfsvPackage#getVersionedUpdateElementType_FeatureVersion()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='featureVersion'"
     * @generated
     */
    String getFeatureVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.VersionedUpdateElementType#getFeatureVersion <em>Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Version</em>' attribute.
     * @see #getFeatureVersion()
     * @generated
     */
    void setFeatureVersion(String value);

} // VersionedUpdateElementType
