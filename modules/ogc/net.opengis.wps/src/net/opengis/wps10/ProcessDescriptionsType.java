/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Descriptions Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ProcessDescriptionsType#getProcessDescription <em>Process Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getProcessDescriptionsType()
 * @model extendedMetaData="name='ProcessDescriptions_._type' kind='elementOnly'"
 * @generated
 */
public interface ProcessDescriptionsType extends ResponseBaseType {
    /**
     * Returns the value of the '<em><b>Process Description</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.ProcessDescriptionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered list of one or more full Process descriptions, listed in the order in which they were requested in the DescribeProcess operation request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Description</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getProcessDescriptionsType_ProcessDescription()
     * @model type="net.opengis.wps10.ProcessDescriptionType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='ProcessDescription'"
     * @generated
     */
    EList getProcessDescription();

} // ProcessDescriptionsType
