/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract General Parameter Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract parameter value or group of parameter values.
 * 			
 * This abstract complexType is expected to be extended and restricted for well-known operation methods with many instances, in Application Schemas that define operation-method-specialized element names and contents. Specific parameter value elements are directly contained in concrete subtypes, not in this abstract type. All concrete types derived from this type shall extend this type to include one "...Value" element with an appropriate type, which should be one of the element types allowed in the ParameterValueType. In addition, all derived concrete types shall extend this type to include a "valueOfParameter" element that references one element substitutable for the "OperationParameter" element. 
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractGeneralParameterValueType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractGeneralParameterValueType' kind='empty'"
 * @generated
 */
public interface AbstractGeneralParameterValueType extends EObject {
} // AbstractGeneralParameterValueType
