/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.parameter;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.media.jai.util.Range;
import javax.media.jai.ParameterList;
import javax.media.jai.ParameterListDescriptor;
import javax.media.jai.ParameterListImpl;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.EnumeratedParameter;

import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.InvalidParameterNameException;

import org.geotools.util.Utilities;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.UnmodifiableArrayList;
import org.geotools.referencing.AbstractIdentifiedObject;


/**
 * Wraps a JAI's {@link ParameterList}. Any change to a {@linkplain #parameter parameter value}
 * in this group is reflected into the {@linkplain #parameters underlying parameter list}, and
 * conversely. This adaptor is provided for interoperability with
 * <A HREF="http://java.sun.com/products/java-media/jai/">Java Advanced Imaging</A>.
 * A typical usage is to wrap a JAI {@linkplain OperationDescriptor operation descriptor} into an
 * {@linkplain ImagingParameterDescriptors imaging parameter descriptor} and create instances of
 * {@code ImagingParameters} through the {@link ImagingParameterDescriptors#createValue createValue}
 * method.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ImagingParameters extends AbstractParameter implements ParameterValueGroup {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1378692626023992530L;

    /**
     * The JAI's parameter list. This is also the backing store for this
     * {@linkplain ParameterValueGroup parameter value group}: all "ordinary" parameters
     * (i.e. <strong>not</strong> including {@linkplain ParameterBlockJAI#getSources sources})
     * are actually stored in this list.
     * <P>
     * If the {@linkplain ImagingParameterDescriptors#descriptor JAI descriptor} is an instance
     * of {@link OperationDescriptor}, then this parameter list is also an instance of
     * {@link ParameterBlockJAI}. The {@linkplain ParameterBlockJAI#getSources sources}
     * must be handled separatly, because the source type for a JAI operator (typically
     * {@link java.awt.image.RenderedImage}) is not the same than the source type for a
     * coverage operation (typically {@link org.opengis.coverage.GridCoverage}).
     */
    public final ParameterList parameters;

    /**
     * The wrappers around each elements in {@link #parameters} as an immutable list.
     * Will be created by {@link #createElements} only when first needed.  Note that
     * while this list may be immutable, <strong>elements</strong> in this list stay
     * modifiable. The goal is to allows the following idiom:
     *
     * <blockquote><pre>
     * values().get(i).setValue(myValue);
     * </pre></blockquote>
     */
    private List<GeneralParameterValue> values;

    /**
     * Constructs a parameter group for the specified descriptor.
     *
     * @param descriptor The descriptor for this group of parameters.
     */
    public ImagingParameters(final ImagingParameterDescriptors descriptor) {
        super(descriptor);
        if (descriptor.operation instanceof OperationDescriptor) {
            // Parameters with sources
            parameters = new ParameterBlockJAI((OperationDescriptor) descriptor.operation,
                                                                     descriptor.registryMode);
        } else {
            // Parameters without sources
            parameters = new ParameterListImpl(descriptor.descriptor);
        }
    }

    /**
     * Constructs a parameter group wrapping the specified JAI parameters.
     * A default {@link ImagingParameterDescriptors} is created.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param parameters The JAI's parameters.
     */
    public ImagingParameters(final Map<String,?> properties, final ParameterList parameters) {
        super(new ImagingParameterDescriptors(properties, parameters.getParameterListDescriptor()));
        this.parameters = parameters;
        ensureNonNull("parameters", parameters);
    }

    /**
     * Returns {@code true} if the specified OGC descriptor is compatible with the specified
     * JAI descriptor. Note that the JAI descriptor is allowed to be less strict than the OGC
     * one. This is okay because {@link ImagingParameter} will keep a reference to the stricter
     * OGC descriptor, which can be used for performing a strict check if we wish.
     *
     * @param descriptor
     *              The OGC descriptor.
     * @param listDescriptor
     *              The JAI descriptor.
     * @param names
     *              The array returned by {@code listDescriptor.getParamNames()},
     *              obtained once for ever by the caller for efficienty.
     * @param types
     *              The array returned by {@code listDescriptor.getParamClasses()},
     *              obtained once for ever by the caller for efficienty.
     * @param names
     *              The array returned by {@code listDescriptor.getEnumeratedParameterNames()},
     *              obtained once for ever by the caller for efficienty.
     */
    private static boolean compatible(final ParameterDescriptor     descriptor,
                                      final ParameterListDescriptor listDescriptor,
                                      final String[] names, final Class<?>[] types,
                                      final String[] enumerated)
    {
        final String name = descriptor.getName().getCode().trim();
        Class<?> type = null;
        if (names != null) {
            for (int i=0; i<names.length; i++) {
                if (name.equalsIgnoreCase(names[i])) {
                    type = types[i];
                    break;
                }
            }
        }
        if (type == null || !type.isAssignableFrom(descriptor.getValueClass())) {
            return false;
        }
        final Range range = listDescriptor.getParamValueRange(name);
        if (range != null) {
            Comparable c;
            c = descriptor.getMinimumValue();
            if (c!=null && !range.contains(c)) {
                return false;
            }
            c = descriptor.getMaximumValue();
            if (c!=null && !range.contains(c)) {
                return false;
            }
        }
        if (enumerated != null) {
            for (int i=0; i<enumerated.length; i++) {
                if (name.equalsIgnoreCase(enumerated[i])) {
                    final EnumeratedParameter[] restrictions;
                    restrictions = listDescriptor.getEnumeratedParameterValues(name);
                    final Set<?> valids = descriptor.getValidValues();
                    if (valids == null || !Arrays.asList(restrictions).containsAll(valids)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Creates and fills the {@link #values} list. Note: this method must creates elements
     * inconditionnally and most not requires synchronization for proper working of the
     * {@link #clone} method.
     *
     * @return The array which is backing {@link #values}. This array is returned only in order
     *         to allow {@link #clone} to modify the values right after the clone. In other cases,
     *         this array should be discarted.
     */
    private GeneralParameterValue[] createElements() {
        final ImagingParameterDescriptors descriptor = (ImagingParameterDescriptors) this.descriptor;
        final ParameterListDescriptor listDescriptor = parameters.getParameterListDescriptor();
        final String[]    names       = listDescriptor.getParamNames();
        final Class[]     types       = listDescriptor.getParamClasses();
        final String[]    enumerated  = listDescriptor.getEnumeratedParameterNames();
        final List<GeneralParameterDescriptor> descriptors = descriptor.descriptors();
        final GeneralParameterValue[] values = new GeneralParameterValue[descriptors.size()];
        for (int i=0; i<values.length; i++) {
            final ParameterDescriptor d = (ParameterDescriptor) descriptors.get(i);
            final ParameterValue value;
            if (compatible(d, listDescriptor, names, types, enumerated)) {
                /*
                 * Found a parameter which is a member of the JAI ParameterList, and the
                 * type matches the expected one. Uses 'parameters' as the backing store.
                 */
                value = new ImagingParameter(d, parameters);
            } else {
                /*
                 * In theory, we should use ParameterBlock sources. However, we can't because
                 * the type is not the same: JAI operations typically expect a RenderedImage
                 * source, while coverage operations typically expect a GridCoverage source.
                 * The value will be stored separatly, and the coverage framework will need
                 * to handle it itself.
                 */
                value = d.createValue();
            }
            values[i] = value;
        }
        /*
         * Checks for name clashes.
         */
        for (int j=0; j<values.length; j++) {
            final String name = values[j].getDescriptor().getName().getCode().trim();
            for (int i=0; i<values.length; i++) {
                if (i != j) {
                    final ParameterDescriptor d = (ParameterDescriptor) values[i].getDescriptor();
                    if (AbstractIdentifiedObject.nameMatches(d, name)) {
                        throw new InvalidParameterNameException(Errors.format(
                                ErrorKeys.PARAMETER_NAME_CLASH_$4,
                                d.getName().getCode(), j, name, i), name);
                    }
                }
            }
        }
        this.values = UnmodifiableArrayList.wrap(values);
        return values;
    }

    /**
     * Returns the abstract definition of this parameter.
     */
    @Override
    public ParameterDescriptorGroup getDescriptor() {
        return (ParameterDescriptorGroup) super.getDescriptor();
    }

    /**
     * Returns all values in this group as an unmodifiable list. The returned list contains all
     * parameters found in the {@linkplain #parameters underlying parameter list}. In addition, it
     * may contains sources found in the JAI's {@linkplain OperationDescriptor operation descriptor}.
     */
    public synchronized List<GeneralParameterValue> values() {
        if (values == null) {
            createElements();
        }
        assert ((ParameterDescriptorGroup) descriptor).descriptors().size() == values.size() : values;
        return values;
    }

    /**
     * Returns the value in this group for the specified identifier code. Getter and setter methods
     * will use directly the JAI's {@linkplain #parameters parameter list} as the underlying backing
     * store, when applicable.
     *
     * @param  name The case insensitive identifier code of the parameter to search for.
     * @return The parameter value for the given identifier code.
     * @throws ParameterNotFoundException if there is no parameter value for the given identifier code.
     */
    public synchronized ParameterValue parameter(String name) throws ParameterNotFoundException {
        ensureNonNull("name", name);
        name = name.trim();
        final List<GeneralParameterValue> values = values();
        final int size = values.size();
        for (int i=0; i<size; i++) {
            final ParameterValue value = (ParameterValue) values.get(i);
            if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), name)) {
                return value;
            }
        }
        throw new ParameterNotFoundException(Errors.format(
                  ErrorKeys.MISSING_PARAMETER_$1, name), name);
    }

    /**
     * Always throws an exception, since JAI's {@linkplain ParameterList parameter list}
     * don't have subgroups.
     */
    public List<ParameterValueGroup> groups(final String name) throws ParameterNotFoundException {
        throw new ParameterNotFoundException(Errors.format(
                  ErrorKeys.MISSING_PARAMETER_$1, name), name);
    }

    /**
     * Always throws an exception, since JAI's {@linkplain ParameterList parameter list}
     * don't have subgroups.
     */
    public ParameterValueGroup addGroup(final String name) throws ParameterNotFoundException {
        throw new ParameterNotFoundException(Errors.format(
                  ErrorKeys.MISSING_PARAMETER_$1, name), name);
    }

    /**
     * Compares the specified object with this parameter group for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final ImagingParameters that = (ImagingParameters) object;
            return Utilities.equals(this.parameters, that.parameters);
        }
        return false;
    }

    /**
     * Returns a hash value for this parameter group. This value doesn't need
     * to be the same in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return super.hashCode()*37 + parameters.hashCode();
    }

    /**
     * Returns a deep copy of this group of parameter values.
     */
    @Override
    public synchronized ImagingParameters clone() {
        final ImagingParameters copy = (ImagingParameters) super.clone();
        try {
            final Method cloneMethod = parameters.getClass().getMethod("clone", (Class[])null);
            final Field  paramField  = ImagingParameters.class.getField("parameters");
            paramField.setAccessible(true); // Will work only with J2SE 1.5 or above.
            paramField.set(copy, cloneMethod.invoke(parameters, (Object[]) null));
        } catch (Exception exception) {
            // TODO: localize.
            throw new UnsupportedOperationException("Clone not supported.", exception);
        }
        /*
         * Most elements in the values list are ImagingParameter instances, which are backed by the
         * ParameterList.  If the list was already created, we need to overwrite it with a new list
         * filled with ImagingParameter instances that reference the cloned ParameterList. The call
         * to createElements() do this job while preserving the parameter values since we cloned the
         * ParameterList first.
         *
         * We can not just set the list to null and wait for it to be lazily created, because not
         * all elements are ImagingParameter instances. Those that are not need to be cloned.
         */
        if (copy.values != null) {
            final GeneralParameterValue[] cloned = copy.createElements();
            assert values.size() == cloned.length : values;
            for (int i=0; i<cloned.length; i++) {
                if (!(cloned[i] instanceof ImagingParameter)) {
                    cloned[i] = ((ParameterValue) values.get(i)).clone();
                }
            }
        }
        return copy;
    }
}
