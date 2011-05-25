/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import java.io.Serializable;
import java.util.Iterator;

import org.geotools.factory.Hints;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.processing.Operation;
import org.opengis.filter.identity.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.util.InternationalString;


/**
 * Provides descriptive information for a {@linkplain Coverage coverage} processing operation.
 * The descriptive information includes such information as the name of the operation, operation
 * description, and number of source grid coverages required for the operation.
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractOperation implements Operation, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -1441856042779942954L;;

    /**
     * The parameters descriptor.
     */
    protected final ParameterDescriptorGroup descriptor;

    /**
     * Constructs an operation. The operation name will be the same than the
     * parameter descriptor name.
     *
     * @param descriptor The parameters descriptor.
     */
    public AbstractOperation(final ParameterDescriptorGroup descriptor) {
        Utilities.ensureNonNull("descriptor", descriptor);
        this.descriptor = descriptor;
    }

    /**
     * Returns the name of the processing operation. The default implementation
     * returns the {@linkplain #descriptor} code name.
     *
     * @todo The return type will be changed from {@link String} to {@link Identifier}.
     */
    public String getName() {
        return descriptor.getName().getCode();
    }

    /**
     * Returns the description of the processing operation. If there is no description,
     * returns {@code null}. The default implementation returns the {@linkplain #descriptor}
     * remarks.
     *
     * @deprecated Return type need to be changed, maybe to {@link InternationalString}.
     */
    @Deprecated
    public String getDescription() {
        final InternationalString remarks = descriptor.getRemarks();
        return (remarks!=null) ? remarks.toString() : null;
    }

    /**
     * Returns the URL for documentation on the processing operation. If no online documentation
     * is available the string will be null. The default implementation returns {@code null}.
     *
     * @deprecated To be replaced by a method returning a {@link Citation}.
     */
    @Deprecated
    public String getDocURL() {
        return null;
    }

    /**
     * Returns the version number of the implementation.
     *
     * @deprecated Replacement to be determined.
     */
    @Deprecated
    public String getVersion() {
        return descriptor.getName().getVersion();
    }

    /**
     * Returns the vendor name of the processing operation implementation.
     * The default implementation returns "Geotools 2".
     *
     * @deprecated Replaced by {@code getName().getAuthority()}.
     */
    @Deprecated
    public String getVendor() {
        return "Geotools 2";
    }

    /**
     * Returns the number of source coverages required for the operation.
     */
    public int getNumSources() {
        return getNumSources(descriptor);
    }

    /**
     * Returns the number of source coverages in the specified parameter group.
     */
    private static int getNumSources(final ParameterDescriptorGroup descriptor) {
        int count = 0;
        for (final Iterator it=descriptor.descriptors().iterator(); it.hasNext();) {
            final GeneralParameterDescriptor candidate = (GeneralParameterDescriptor) it.next();
            if (candidate instanceof ParameterDescriptorGroup) {
                count += getNumSources((ParameterDescriptorGroup) candidate);
                continue;
            }
            if (candidate instanceof ParameterDescriptor) {
                final Class type = ((ParameterDescriptor) candidate).getValueClass();
                if (Coverage.class.isAssignableFrom(type)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns an initially empty set of parameters.
     */
    public ParameterValueGroup getParameters() {
        return descriptor.createValue();
    }

    /**
     * Applies a process operation to a coverage. This method is invoked by {@link DefaultProcessor}.
     *
     * @param  parameters List of name value pairs for the parameters required for the operation.
     * @param  hints A set of rendering hints, or {@code null} if none. The {@code DefaultProcessor}
     *         may provides hints for the following keys: {@link Hints#COORDINATE_OPERATION_FACTORY}
     *         and {@link Hints#JAI_INSTANCE}.
     * @return The result as a coverage.
     *
     * @throws CoverageProcessingException if the operation can't be applied.
     */
    public abstract Coverage doOperation(final ParameterValueGroup parameters, final Hints hints)
            throws CoverageProcessingException;

    /**
     * Returns a hash value for this operation. This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        // Since we should have only one operation registered for each name,
        // the descriptors hash code should be enough.
        return descriptor.hashCode() ^ (int)serialVersionUID;
    }

    /**
     * Compares the specified object with this operation for equality.
     *
     * @param object The object to compare with this operation.
     * @return {@code true} if the given object is equals to this operation.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final AbstractOperation that = (AbstractOperation) object;
            return Utilities.equals(this.descriptor, that.descriptor);
        }
        return false;
    }

    /**
     * Returns a string representation of this operation. The returned string is
     * implementation dependent. It is usually provided for debugging purposes only.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) + '[' + getName() + ']';
    }
}
