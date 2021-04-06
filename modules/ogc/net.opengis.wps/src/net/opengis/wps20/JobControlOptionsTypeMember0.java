/**
 */
package net.opengis.wps20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Job Control Options Type Member0</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wps20.Wps20Package#getJobControlOptionsTypeMember0()
 * @model extendedMetaData="name='JobControlOptionsType_._member_._0'"
 * @generated
 */
public enum JobControlOptionsTypeMember0 implements Enumerator {
	/**
	 * The '<em><b>Sync Execute</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SYNC_EXECUTE_VALUE
	 * @generated
	 * @ordered
	 */
	SYNC_EXECUTE(0, "syncExecute", "sync-execute"),

	/**
	 * The '<em><b>Async Execute</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ASYNC_EXECUTE_VALUE
	 * @generated
	 * @ordered
	 */
	ASYNC_EXECUTE(1, "asyncExecute", "async-execute");

	/**
	 * The '<em><b>Sync Execute</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SYNC_EXECUTE
	 * @model name="syncExecute" literal="sync-execute"
	 * @generated
	 * @ordered
	 */
	public static final int SYNC_EXECUTE_VALUE = 0;

	/**
	 * The '<em><b>Async Execute</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ASYNC_EXECUTE
	 * @model name="asyncExecute" literal="async-execute"
	 * @generated
	 * @ordered
	 */
	public static final int ASYNC_EXECUTE_VALUE = 1;

	/**
	 * An array of all the '<em><b>Job Control Options Type Member0</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final JobControlOptionsTypeMember0[] VALUES_ARRAY =
		new JobControlOptionsTypeMember0[] {
			SYNC_EXECUTE,
			ASYNC_EXECUTE,
		};

	/**
	 * A public read-only list of all the '<em><b>Job Control Options Type Member0</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<JobControlOptionsTypeMember0> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Job Control Options Type Member0</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static JobControlOptionsTypeMember0 get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			JobControlOptionsTypeMember0 result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Job Control Options Type Member0</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static JobControlOptionsTypeMember0 getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			JobControlOptionsTypeMember0 result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Job Control Options Type Member0</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static JobControlOptionsTypeMember0 get(int value) {
		switch (value) {
			case SYNC_EXECUTE_VALUE: return SYNC_EXECUTE;
			case ASYNC_EXECUTE_VALUE: return ASYNC_EXECUTE;
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
	private JobControlOptionsTypeMember0(int value, String name, String literal) {
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
	
} //JobControlOptionsTypeMember0
