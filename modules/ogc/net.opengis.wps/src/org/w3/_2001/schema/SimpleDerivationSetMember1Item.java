/**
 */
package org.w3._2001.schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Simple Derivation Set Member1 Item</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3._2001.schema.SchemaPackage#getSimpleDerivationSetMember1Item()
 * @model extendedMetaData="name='simpleDerivationSet_._member_._1_._item'"
 * @generated
 */
public enum SimpleDerivationSetMember1Item implements Enumerator {
	/**
	 * The '<em><b>List</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LIST_VALUE
	 * @generated
	 * @ordered
	 */
	LIST(0, "list", "list"),

	/**
	 * The '<em><b>Union</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UNION_VALUE
	 * @generated
	 * @ordered
	 */
	UNION(1, "union", "union"),

	/**
	 * The '<em><b>Restriction</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RESTRICTION_VALUE
	 * @generated
	 * @ordered
	 */
	RESTRICTION(2, "restriction", "restriction");

	/**
	 * The '<em><b>List</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LIST
	 * @model name="list"
	 * @generated
	 * @ordered
	 */
	public static final int LIST_VALUE = 0;

	/**
	 * The '<em><b>Union</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UNION
	 * @model name="union"
	 * @generated
	 * @ordered
	 */
	public static final int UNION_VALUE = 1;

	/**
	 * The '<em><b>Restriction</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RESTRICTION
	 * @model name="restriction"
	 * @generated
	 * @ordered
	 */
	public static final int RESTRICTION_VALUE = 2;

	/**
	 * An array of all the '<em><b>Simple Derivation Set Member1 Item</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SimpleDerivationSetMember1Item[] VALUES_ARRAY =
		new SimpleDerivationSetMember1Item[] {
			LIST,
			UNION,
			RESTRICTION,
		};

	/**
	 * A public read-only list of all the '<em><b>Simple Derivation Set Member1 Item</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SimpleDerivationSetMember1Item> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Simple Derivation Set Member1 Item</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SimpleDerivationSetMember1Item get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SimpleDerivationSetMember1Item result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Simple Derivation Set Member1 Item</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SimpleDerivationSetMember1Item getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SimpleDerivationSetMember1Item result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Simple Derivation Set Member1 Item</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SimpleDerivationSetMember1Item get(int value) {
		switch (value) {
			case LIST_VALUE: return LIST;
			case UNION_VALUE: return UNION;
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
	private SimpleDerivationSetMember1Item(int value, String name, String literal) {
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
	
} //SimpleDerivationSetMember1Item
