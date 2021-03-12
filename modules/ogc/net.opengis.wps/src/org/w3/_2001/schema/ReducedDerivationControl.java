/**
 */
package org.w3._2001.schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Reduced Derivation Control</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 *    A utility type, not for public use
 * <!-- end-model-doc -->
 * @see org.w3._2001.schema.SchemaPackage#getReducedDerivationControl()
 * @model extendedMetaData="name='reducedDerivationControl'"
 * @generated
 */
public enum ReducedDerivationControl implements Enumerator {
	/**
	 * The '<em><b>Extension</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EXTENSION_VALUE
	 * @generated
	 * @ordered
	 */
	EXTENSION(0, "extension", "extension"),

	/**
	 * The '<em><b>Restriction</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RESTRICTION_VALUE
	 * @generated
	 * @ordered
	 */
	RESTRICTION(1, "restriction", "restriction");

	/**
	 * The '<em><b>Extension</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EXTENSION
	 * @model name="extension"
	 * @generated
	 * @ordered
	 */
	public static final int EXTENSION_VALUE = 0;

	/**
	 * The '<em><b>Restriction</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RESTRICTION
	 * @model name="restriction"
	 * @generated
	 * @ordered
	 */
	public static final int RESTRICTION_VALUE = 1;

	/**
	 * An array of all the '<em><b>Reduced Derivation Control</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ReducedDerivationControl[] VALUES_ARRAY =
		new ReducedDerivationControl[] {
			EXTENSION,
			RESTRICTION,
		};

	/**
	 * A public read-only list of all the '<em><b>Reduced Derivation Control</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ReducedDerivationControl> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Reduced Derivation Control</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ReducedDerivationControl get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ReducedDerivationControl result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Reduced Derivation Control</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ReducedDerivationControl getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ReducedDerivationControl result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Reduced Derivation Control</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ReducedDerivationControl get(int value) {
		switch (value) {
			case EXTENSION_VALUE: return EXTENSION;
			case RESTRICTION_VALUE: return RESTRICTION;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private ReducedDerivationControl(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //ReducedDerivationControl
