/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.feature.type;


class DescriptorValidator {

	private DescriptorValidator() {
		// no-op
	}

//	public static void validate(AttributeDescriptor schema, List/*<Attribute>*/ content) {
//		if (schema == null) {
//			throw new NullPointerException("schema");
//		}
//		if (content == null) {
//			throw new NullPointerException("content");
//		}
//
//		List/*<AttributeType>*/ allowedTypes = 
//			Descriptors.types(schema.getType());
//		
//		int index = 0;
//		for (Iterator itr = content.iterator(); itr.hasNext();) {
//			Attribute att = (Attribute) itr.next();
//			// att shall not be null
//			checkAttIsNotNull(index, att);
//			// and has to be of one of the allowed types
//			checkAttIsOfAllowedType(allowedTypes, index, att);
//			index++;
//		}
//
//		if (schema.getType() instanceof SequenceType) {
//			validateSequence(schema, content);
//		}
//		else if (schema.getType() instanceof ChoiceType) {
//			validateChoice(schema,content);
//		}
//		else if (schema.getType() instanceof ComplexType) { 
//			validateAll(schema, content);
//		}
//		else {
//			validateNode(schema, content);
//		}
//		
//	}
//
//	/**
//	 * Validates the sequence of Attributes in <code>content</code> until the
//	 * <code>schema.sequence()</code> gets exhausted, and returns the list of
//	 * remaining attributes.
//	 * 
//	 * @param schema
//	 * @param content
//	 * @return
//	 */
//	private static void validateSequence(AttributeDescriptor schema,
//			List/*<Attribute>*/ content) {
//		// empty sequences are allowed, in such a case, content should be empty
//		if (((List)((SequenceType)schema.getType()).attributes()).isEmpty()) {
//			if (content.size() > 0) {
//				throw new IllegalArgumentException(
//						"Sequence is empty, content not allowed");
//			}
//			return;
//		}
//
//		final List/*<AttributeDescriptor>*/ descriptors = 
//			(List) ((SequenceType)schema.getType()).attributes();
//		final int min = schema.getMinOccurs();
//		final int max = schema.getMaxOccurs();
//
//		List/*<Attribute>*/ remaining = processSequence(descriptors, content, min,
//				max);
//
//		if (remaining.size() > 0) {
//			throw new IllegalArgumentException(
//					"Extra content found beyond the specified in the schema: "
//							+ remaining);
//		}
//	}
//
//	private static void validateChoice(AttributeDescriptor choice,
//			List/*<Attribute>*/ content) {
//		
//		ChoiceType type = (ChoiceType) choice.getType();
//		// empty sequences are allowed, in such a case, content should be empty
//		if (type.attributes().isEmpty()) {
//			if (content.size() > 0) {
//				throw new IllegalArgumentException(
//						"Choice is empty, content not allowed");
//			}
//			return;
//		}
//
//		final Set/*<Descriptor>*/ descriptors = (Set) type.attributes();
//		final int min = choice.getMinOccurs();
//		final int max = choice.getMaxOccurs();
//
//		List/*<Attribute>*/ remaining = processChoice(descriptors, content, min,
//				max);
//
//		if (remaining.size() > 0) {
//			throw new IllegalArgumentException(
//					"Extra content found beyond the specified in the schema: "
//							+ remaining);
//		}
//	}
//
//	private static void validateAll(AttributeDescriptor all, List/*<Attribute>*/ content)
//			throws NullPointerException, IllegalArgumentException {
//		if (content == null) {
//			throw new NullPointerException("content");
//		}
//
//		ComplexType ctype = (ComplexType)all.getType();
//		List/*<AttributeType>*/ usedTypes = new ArrayList/*<AttributeType>*/();
//		for (Iterator itr = content.iterator(); itr.hasNext();) {
//			Attribute att = (Attribute) itr.next();
//			AttributeType/*<?>*/ type = att.getType();
//
//			// cannot be more than one instance of its type
//			// (shortcut to multiplicity rangecheck)
//			if (usedTypes.contains(type)) {
//				throw new IllegalArgumentException("Attribute of type "
//						+ type.getName() + " encountered more than once.");
//			}
//			usedTypes.add(type);
//		}
//		// and the multiplicity specified in each AttributeDescriptor respected
//		for (Iterator itr = ctype.attributes().iterator(); itr.hasNext();) {
//			AttributeDescriptor node = (AttributeDescriptor) itr.next();
//			int min = node.getMinOccurs();
//			int max = node.getMaxOccurs();
//			AttributeType/*<?>*/ expectedType = node.getType();
//			if (max == 0 && usedTypes.contains(expectedType)) {
//				throw new IllegalArgumentException(
//						expectedType.getName()
//								+ " was fund, thus it is not allowed since maxOccurs is set to 0");
//			}
//			if (min == 1 && !usedTypes.contains(expectedType)) {
//				throw new IllegalArgumentException(
//						expectedType.getName()
//								+ " was not fund, thus it have to since minOccurs is set to 1");
//			}
//		}
//
//	}
//
//	private static List/*<Attribute>*/ processDescriptor(AttributeDescriptor schema,
//			List/*<Attribute>*/ content) {
//		final int min = schema.getMinOccurs();
//		final int max = schema.getMaxOccurs();
//		
//		if (schema.getType() instanceof ChoiceType) {
//			Set/*<AttributeDescriptor>*/ options = 
//				(Set) ((ChoiceType)schema.getType()).attributes();
//			return processChoice(options, content, min, max);
//		}
//		else if (schema.getType() instanceof SequenceType) {
//			List/*<AttributeDescriptor>*/ sequence =
//				(List) ((SequenceType) schema.getType()).attributes();
//			return processSequence(sequence, content, min, max);
//		}
//		else {
//			AttributeType type = schema.getType();
//			return processType(content, type, min, max);
//		}
//	}
//
//	private static List/*<Attribute>*/ processSequence(
//			List/*<AttributeDescriptor>*/ sequence, List/*<Attribute>*/ content,
//			int min, int max) {
//
//		int count = 0;
//		List/*<Attribute>*/ remaining = content;
//
//		while (true) {
//			for (Iterator itr = sequence.iterator(); itr.hasNext();) {
//				AttributeDescriptor desc = (AttributeDescriptor) itr.next();
//				remaining = processDescriptor(desc, remaining);
//			}
//			if (count < max) {
//				count++;
//			} else {
//				break;
//			}
//			if (count == max || remaining.size() == 0) {
//				break;
//			}
//		}
//
//		return remaining;
//	}
//
//	private static List/*<Attribute>*/ processChoice(
//			Set/*<Descriptor>*/ allowableContent, List/*<Attribute>*/ content, int min,
//			int max) {
//		throw new UnsupportedOperationException("not yet implemented");
//		/*
//		//the choice itself is nullified
//		if (min == 0 && max == 0) {
//			return content;
//		}
//		
//		int count = 0;
//		Map<AttributeType, Descriptor>m;
//		for (Attribute att : content){
//			AttributeType type = att.getType();
//			
//			if (allowableContent.contains(type)) {
//				if (count < max) {
//					count++;
//				} else {
//					break;
//				}
//			} else {
//				break;
//			}
//		}
//		if (count < min) {
//			throw new IllegalArgumentException("Expected at least " + min
//					+ " occurrences of " + allowableContent);
//		}
//
//		if (count == 0)
//			return content;
//
//		return content.subList(count, content.size());
//		*/
//	}
//
//	/**
//	 * process a minimun of <code>min</code> and a maximun of <code>max</code>
//	 * consecutive occurrencies of Attributes of type <code>expectedType</code>
//	 * and return the remaining attributes. Only fails if first attribute is not
//	 * of the expected type or minOccurs has not been satisfied. Never exceeds
//	 * maxOccurs.
//	 * 
//	 * @param content
//	 * @param expectedType
//	 * @param min
//	 * @param max
//	 * @return
//	 */
//	private static List/*<Attribute>*/ processType(List/*<Attribute>*/ content,
//			AttributeType expectedType, int min, int max) {
//		int count = 0;
//		
//		for (Iterator itr = content.iterator(); itr.hasNext();) {
//			Attribute att = (Attribute) itr.next();
//			AttributeType attType = att.getType();
//			if (attType.equals(expectedType)) {
//				count++;
//				if (count == max) {
//					break;
//				}
//			} else {
//				break;
//			}
//		}
//		if (count < min) {
//			throw new IllegalArgumentException("got " + count
//					+ " occurrences of " + expectedType.getName()
//					+ ". Expected at least " + min);
//		}
//		if (count == 0)
//			return content;
//		return content.subList(count, content.size());
//	}
//
//	private static void validateNode(AttributeDescriptor schema,
//			List/*<Attribute>*/ content) {
//		// no-op
//	}
//
//	/**
//	 * @param allowedTypes
//	 * @param index
//	 * @param att
//	 * @return
//	 * @throws IllegalArgumentException
//	 */
//	private static void checkAttIsOfAllowedType(
//			List/*<AttributeType>*/ allowedTypes, int index, Attribute att)
//			throws IllegalArgumentException {
//		AttributeType/*<?>*/ type = att.getType();
//		if (!allowedTypes.contains(type)) {
//			throw new IllegalArgumentException("Attribute of type "
//					+ type.getName() + " found at index " + index
//					+ " but this type is not allowed by this descriptor");
//		}
//	}
//
//	private static void checkAttIsNotNull(int index, Attribute att) {
//		if (att == null) {
//			throw new NullPointerException(
//					"Attribute at index "
//							+ index
//							+ " is null. Attributes can't be null. Do you mean Attribute.get() == null?");
//		}
//	}
}
