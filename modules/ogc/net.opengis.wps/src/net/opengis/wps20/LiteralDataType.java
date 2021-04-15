/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Literal Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.LiteralDataType#getLiteralDataDomain <em>Literal Data Domain</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getLiteralDataType()
 * @model extendedMetaData="name='LiteralDataType' kind='elementOnly'"
 * @generated
 */
public interface LiteralDataType extends DataDescriptionType {
	/**
	 * Returns the value of the '<em><b>Literal Data Domain</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.LiteralDataDomainType1}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Literal Data inputs and outputs may be specified for several domains, e.g. distance units in meters,
	 * 								kilometers and feet. One of these must be the default domain.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Literal Data Domain</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getLiteralDataType_LiteralDataDomain()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='LiteralDataDomain'"
	 * @generated
	 */
	EList<LiteralDataDomainType1> getLiteralDataDomain();

} // LiteralDataType
