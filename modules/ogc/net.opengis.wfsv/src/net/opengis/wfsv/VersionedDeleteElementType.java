/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import net.opengis.wfs.DeleteElementType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Versioned Delete Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.VersionedDeleteElementType#getFeatureVersion <em>Feature Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getVersionedDeleteElementType()
 * @model extendedMetaData="name='VersionedDeleteElementType' kind='elementOnly'"
 * @generated
 */
public interface VersionedDeleteElementType extends DeleteElementType {
    /**
     * Returns the value of the '<em><b>Feature Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               See VersionedUpdateElementType featureVersion attribute.
     *             
     * <!-- end-model-doc -->
     * @return the value of the '<em>Feature Version</em>' attribute.
     * @see #setFeatureVersion(String)
     * @see net.opengis.wfsv.WfsvPackage#getVersionedDeleteElementType_FeatureVersion()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='featureVersion'"
     * @generated
     */
    String getFeatureVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.VersionedDeleteElementType#getFeatureVersion <em>Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Version</em>' attribute.
     * @see #getFeatureVersion()
     * @generated
     */
    void setFeatureVersion(String value);

} // VersionedDeleteElementType
