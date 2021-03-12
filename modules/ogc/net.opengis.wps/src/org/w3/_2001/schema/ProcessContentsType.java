/**
 */
package org.w3._2001.schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Process Contents Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3._2001.schema.SchemaPackage#getProcessContentsType()
 * @model extendedMetaData="name='processContents_._type'"
 * @generated
 */
public enum ProcessContentsType implements Enumerator {
	/**
	 * The '<em><b>Skip</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SKIP_VALUE
	 * @generated
	 * @ordered
	 */
	SKIP(0, "skip", "skip"),

	/**
	 * The '<em><b>Lax</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LAX_VALUE
	 * @generated
	 * @ordered
	 */
	LAX(1, "lax", "lax"),

	/**
	 * The '<em><b>Strict</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STRICT_VALUE
	 * @generated
	 * @ordered
	 */
	STRICT(2, "strict", "strict");

	/**
	 * The '<em><b>Skip</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SKIP
	 * @model name="skip"
	 * @generated
	 * @ordered
	 */
	public static final int SKIP_VALUE = 0;

	/**
	 * The '<em><b>Lax</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LAX
	 * @model name="lax"
	 * @generated
	 * @ordered
	 */
	public static final int LAX_VALUE = 1;

	/**
	 * The '<em><b>Strict</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STRICT
	 * @model name="strict"
	 * @generated
	 * @ordered
	 */
	public static final int STRICT_VALUE = 2;

	/**
	 * An array of all the '<em><b>Process Contents Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ProcessContentsType[] VALUES_ARRAY =
		new ProcessContentsType[] {
			SKIP,
			LAX,
			STRICT,
		};

	/**
	 * A public read-only list of all the '<em><b>Process Contents Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ProcessContentsType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Process Contents Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ProcessContentsType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ProcessContentsType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Process Contents Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ProcessContentsType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ProcessContentsType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Process Contents Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ProcessContentsType get(int value) {
		switch (value) {
			case SKIP_VALUE: return SKIP;
			case LAX_VALUE: return LAX;
			case STRICT_VALUE: return STRICT;
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
	private ProcessContentsType(int value, String name, String literal) {
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
	
} //ProcessContentsType
