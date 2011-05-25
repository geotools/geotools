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

import java.text.MessageFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.Locale;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.image.RenderedImage;

import javax.media.jai.util.Range;
import javax.media.jai.EnumeratedParameter;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.ParameterListDescriptor;
import javax.media.jai.RegistryElementDescriptor;
import javax.media.jai.registry.RenderedRegistryMode;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.OnLineFunction;
import org.opengis.metadata.citation.Role;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.InternationalString;
import org.opengis.util.GenericName;

import org.geotools.util.Utilities;
import org.geotools.util.NameFactory;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.opengis.parameter.InvalidParameterNameException;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.citation.ContactImpl;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.XArray;


/**
 * Wraps a JAI's {@link ParameterListDescriptor}. This adaptor is provided for interoperability
 * with <A HREF="http://java.sun.com/products/java-media/jai/">Java Advanced Imaging</A>. A JAI
 * parameter list descriptor is part of an {@linkplain OperationDescriptor operation descriptor}.
 * This adaptor make it easier to access parameters for a JAI operation through the general GeoAPI
 * parameters framework.
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ImagingParameterDescriptors extends DefaultParameterDescriptorGroup {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 2127050865911951239L;

    /**
     * Mapping between values of the "Vendor" resource (in OperationDescriptor)
     * and the citation for know authorities.
     */
    private static final Object[] AUTHORITIES = {
            "com.sun.media.jai", Citations.JAI,
            "org.geotools",      Citations.GEOTOOLS,
            "jaitools.media.jai",Citations.JAI
    };

    /**
     * The default <cite>source type map</cite> as a (<code>{@linkplain RenderedImage}.class</code>,
     * <code>{@linkplain GridCoverage}.class</code>) key-value pair. This is the default argument
     * for wrapping a JAI operation in the
     * {@value javax.media.jai.registry.RenderedRegistryMode#MODE_NAME} registry mode.
     */
    @SuppressWarnings("unchecked")
    public static final Map<Class<?>,Class<?>> DEFAULT_SOURCE_TYPE_MAP = (Map)
            Collections.singletonMap(RenderedImage.class, GridCoverage.class);

    /**
     * The registry mode, usually {@value javax.media.jai.registry.RenderedRegistryMode#MODE_NAME}.
     * This field is {@code null} if {@link #operation} is null.
     */
    protected final String registryMode;

    /**
     * The JAI's operation descriptor, or {@code null} if none. This is usually an
     * instance of {@link OperationDescriptor}, but this is not strictly required.
     */
    protected final RegistryElementDescriptor operation;

    /**
     * The Java Advanced Imaging parameter descriptor. If {@link #operation} is non-null, then
     * this attribute is defined by {@link RegistryElementDescriptor#getParameterListDescriptor}.
     */
    protected final ParameterListDescriptor descriptor;

    /**
     * Constructs a parameter descriptor wrapping the specified JAI operation, including sources.
     * The {@linkplain #getName name for this parameter group} will be inferred from the
     * {@linkplain RegistryElementDescriptor#getName name of the supplied registry element}
     * using the {@link #properties properties} method.
     *
     * The <cite>source type map</cite> default to a (<code>{@linkplain RenderedImage}.class</code>,
     * <code>{@linkplain GridCoverage}.class</code>) key-value pair and the <cite>registry
     * mode</cite> default to {@value javax.media.jai.registry.RenderedRegistryMode#MODE_NAME}.
     *
     * @param operation The JAI's operation descriptor, usually as an instance of
     *        {@link OperationDescriptor}.
     */
    public ImagingParameterDescriptors(final RegistryElementDescriptor operation) {
        this(properties(operation), operation, DEFAULT_SOURCE_TYPE_MAP,
                RenderedRegistryMode.MODE_NAME);
    }

    /**
     * Constructs a parameter descriptor wrapping the specified JAI operation, including sources.
     * The {@linkplain #getName name for this parameter group} will be inferred from the
     * {@linkplain RegistryElementDescriptor#getName name of the supplied registry element}
     * using the {@link #properties properties} method.
     *
     * The <cite>source type map</cite> default to a (<code>{@linkplain RenderedImage}.class</code>,
     * <code>{@linkplain GridCoverage}.class</code>) key-value pair and the <cite>registry
     * mode</cite> default to {@value javax.media.jai.registry.RenderedRegistryMode#MODE_NAME}.
     *
     * @param operation The JAI's operation descriptor, usually as an instance of
     *        {@link OperationDescriptor}.
     * @param extension Additional parameters to put in this descriptor, or {@code null} if none.
     *        If a parameter has the same name than an {@code operation} parameter, then the
     *        extension overrides the later.
     *
     * @since 2.4
     */
    public ImagingParameterDescriptors(final RegistryElementDescriptor operation,
                                       final Collection<ParameterDescriptor> extension)
    {
        this(properties(operation), operation, RenderedRegistryMode.MODE_NAME,
                DEFAULT_SOURCE_TYPE_MAP, extension);
    }

    /**
     * Constructs a parameter descriptor wrapping the specified JAI operation, including sources.
     * The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param operation The JAI's operation descriptor, usually as an instance of
     *        {@link OperationDescriptor}.
     * @param sourceTypeMap Mapping from JAI source type to this group source type. Typically a
     *        singleton with the (<code>{@linkplain RenderedImage}.class</code>,
     *        <code>{@linkplain GridCoverage}.class</code>) key-value pair.
     * @param registryMode The JAI's registry mode (usually
     *        {@value javax.media.jai.registry.RenderedRegistryMode#MODE_NAME}).
     *
     * @deprecated Replaced by {@link #ImagingParameterDescriptors(Map,
     *             RegistryElementDescriptor,String,Map,Collection}.
     */
    public ImagingParameterDescriptors(final Map<String,?> properties,
                                       final RegistryElementDescriptor operation,
                                       final Map<Class<?>,Class<?>> sourceTypeMap,
                                       final String registryMode)
    {
        this(properties,
             operation.getParameterListDescriptor(registryMode),
             operation, registryMode, sourceTypeMap, null);
    }

    /**
     * Constructs a parameter descriptor wrapping the specified JAI operation, including sources.
     * The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param operation The JAI's operation descriptor, usually as an instance of
     *        {@link OperationDescriptor}.
     * @param registryMode The JAI's registry mode (usually
     *        {@value javax.media.jai.registry.RenderedRegistryMode#MODE_NAME}).
     * @param sourceTypeMap Mapping from JAI source type to this group source type. Typically a
     *        singleton with the (<code>{@linkplain RenderedImage}.class</code>,
     *        <code>{@linkplain GridCoverage}.class</code>) key-value pair.
     * @param extension Additional parameters to put in this descriptor, or {@code null} if none.
     *        If a parameter has the same name than an {@code operation} parameter, then the
     *        extension overrides the later.
     *
     * @since 2.4
     */
    public ImagingParameterDescriptors(final Map<String,?> properties,
                                       final RegistryElementDescriptor operation,
                                       final String registryMode,
                                       final Map<Class<?>,Class<?>> sourceTypeMap,
                                       final Collection<ParameterDescriptor> extension)
    {
        this(properties,
             operation.getParameterListDescriptor(registryMode),
             operation, registryMode, sourceTypeMap, extension);
    }

    /**
     * Constructs a parameter descriptor wrapping the specified JAI parameter list descriptor.
     * The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param descriptor The JAI descriptor.
     */
    public ImagingParameterDescriptors(final Map<String,?> properties,
                                       final ParameterListDescriptor descriptor)
    {
        this(properties, descriptor, null, null, null, null);
    }

    /**
     * Constructs a parameter descriptor wrapping the specified JAI descriptor.
     * If {@code operation} is non-null, then {@code descriptor} should be derived from it.
     * This constructor is private in order to ensure this condition.
     */
    private ImagingParameterDescriptors(final Map<String,?> properties,
                                        final ParameterListDescriptor descriptor,
                                        final RegistryElementDescriptor operation,
                                        final String registryMode,
                                        final Map<Class<?>,Class<?>> sourceTypeMap,
                                        final Collection<ParameterDescriptor> extension)
    {
        super(properties, 1, 1, asDescriptors(descriptor, operation, registryMode, sourceTypeMap, extension));
        this.descriptor   = descriptor;
        this.operation    = operation;
        this.registryMode = registryMode;
    }

    /**
     * Infers from the specified JAI operation a set of properties that can be given to the
     * {@linkplain #ImagingParameterDescriptors(Map,RegistryElementDescriptor,Map,String)
     * constructor}. The returned map includes values (when available) for the following keys:
     * <p>
     * <table border="1">
     *  <tr>
     *   <th nowrap>Key</th>
     *   <th nowrap>Inferred from</th>
     *  </tr>
     *  <tr>
     *   <td>{@link #NAME_KEY NAME_KEY}</td>
     *   <td>{@linkplain RegistryElementDescriptor#getName descriptor name}</td>
     *  </tr>
     *  <tr>
     *   <td>{@link #ALIAS_KEY ALIAS_KEY}</td>
     *   <td>{@code "Vendor"} (for the {@linkplain GenericName#getScope scope}) and
     *       {@code "LocalName"} {@linkplain OperationDescriptor#getResources resources}</td>
     *  </tr>
     *  <tr>
     *   <td>{@link Identifier#AUTHORITY_KEY AUTHORITY_KEY}</td>
     *   <td>{@linkplain Citations#JAI JAI} or {@linkplain Citations#GEOTOOLS Geotools}
     *       inferred from the vendor, extented with {@code "DocURL"}
     *       {@linkplain OperationDescriptor#getResources resources} as
     *       {@linkplain ResponsibleParty#getContactInfo contact information}.</td></td>
     *  </tr>
     *  <tr>
     *   <td>{@link ReferenceIdentifier#VERSION_KEY VERSION_KEY}</td>
     *   <td>{@code "Version"} {@linkplain OperationDescriptor#getResources resources}</td>
     *  </tr>
     *  <tr>
     *   <td>{@link #REMARKS_KEY REMARKS_KEY}</td>
     *   <td>{@code "Description"} {@linkplain OperationDescriptor#getResources resources}</td>
     *  </tr>
     * </table>
     * <p>
     * For JAI image operation (for example {@code "Add"}, the end result is fully-qualified name
     * like {@code "JAI:Add"} and one alias like {@code "com.sun.media.jai.Add"}.
     * <p>
     * This method returns a modifiable map. Users can safely changes its content in order to
     * select for example a different name.
     */
    public static Map<String,Object> properties(final RegistryElementDescriptor operation) {
        String name = operation.getName();
        final Map<String,Object> properties = new HashMap<String,Object>();
        if (operation instanceof OperationDescriptor) {
            /*
             * Gets the vendor name (if available) using US locale in order to get something as
             * close as possible to a kind of "locale-independent" string.  This string will be
             * used in order to remove the prefix (if any) from the global name, for example in
             * "org.geotools.Combine" operation name.  We can remove the prefix because it will
             * appears in the GenericName's scope below (as an alias).
             */
            final OperationDescriptor op = (OperationDescriptor) operation;
            final ResourceBundle bundle = op.getResourceBundle(Locale.getDefault());
            String vendor = op.getResourceBundle(Locale.US).getString("Vendor");
            Citation authority = null;
            if (vendor != null) {
                vendor = vendor.trim();
                name = ImagingParameterDescription.trimPrefix(name, vendor);
                for (int i=0; i<AUTHORITIES.length; i+=2) {
                    if (vendor.equalsIgnoreCase((String) AUTHORITIES[i])) {
                        authority = (Citation) AUTHORITIES[i+1];
                        break;
                    }
                }
            }
            if (authority == null) {
                /*
                 * jump off, since it will break the creation of CitationImpl
                 * with a missleading "null source" message.
                 */
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.NO_SUCH_AUTHORITY_CODE_$2, "AUTHORITIES", vendor));
            }
            /*
             * If we are able to construct an URI, replaces the contact info for the first (and only
             * the first) responsible party. Exactly one responsible party should be presents, since
             * the authority is one of the hard-coded AUTHORITIES list above.  We replace completely
             * the contact info;  for example we do not retain any telephone number because it would
             * be a mismatch with the new URI purpose (this new URI do not links to information that
             * can be used to contact the individual or organisation - it is information about an
             * image operation, and I'm not sure that anyone wants to phone to an image operation).
             */
            final InternationalString description;
            description = new ImagingParameterDescription(op, "Description", null);
            try {
                final URI uri = new URI(bundle.getString("DocURL"));
                final OnLineResourceImpl resource = new OnLineResourceImpl(uri);
                resource.setFunction(OnLineFunction.INFORMATION);
                resource.setDescription(description);
                final CitationImpl citation = new CitationImpl(authority);
                final Collection<ResponsibleParty> parties = citation.getCitedResponsibleParties();
                final ResponsibleParty oldParty;
                if (true) {
                    final Iterator<ResponsibleParty> it = parties.iterator();
                    if (it.hasNext()) {
                        oldParty = it.next();
                        it.remove(); // This party will be re-injected with a new URI below.
                    }
                    else {
                        oldParty = null;
                    }
                }
                final ResponsiblePartyImpl party = new ResponsiblePartyImpl(oldParty);
                party.setRole(Role.RESOURCE_PROVIDER);
                party.setContactInfo(new ContactImpl(resource));
                parties.add(party);
                authority = (Citation) citation.unmodifiable();
            } catch (URISyntaxException exception) {
                // Invalid URI syntax. Ignore, since this property
                // was really just for information purpose.
            }
            /*
             * At this point, all properties have been created. Stores them in the map.
             * The name should be stored as a String (not as an Identifier), otherwise
             * the version and the authority would be ignored. For JAI image operation,
             * the end result is fully-qualified name like "JAI:Add" and one alias like
             * "com.sun.media.jai.Add".
             */
            final GenericName alias = NameFactory.create(new InternationalString[] {
                new ImagingParameterDescription(op, "Vendor"   , null),     // Scope name
                new ImagingParameterDescription(op, "LocalName", "Vendor")  // Local name
            }, '.');
            properties.put(ALIAS_KEY,   alias);
            properties.put(REMARKS_KEY, description);
            properties.put(ReferenceIdentifier.VERSION_KEY, bundle.getString("Version"));
            properties.put(Identifier.AUTHORITY_KEY, authority);
        }
        properties.put(NAME_KEY, name);
        return properties;
    }

    /**
     * Returns the JAI's parameters as {@link ParameterDescriptor} objects.
     * This method is a work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private static ParameterDescriptor[] asDescriptors(
            final ParameterListDescriptor descriptor,
            final RegistryElementDescriptor operation, final String registryMode,
            final Map<Class<?>,Class<?>> sourceTypeMap,
            final Collection<ParameterDescriptor> extension)
    {
        /*
         * Creates the list of JAI descriptor to be replaced by user-supplied parameters.
         * Note that this map will be modified again in the remaining of this method.
         */
        ensureNonNull("descriptor", descriptor);
        final Map<String,ParameterDescriptor> replacements =
                new LinkedHashMap<String,ParameterDescriptor>();
        if (extension != null) {
            for (final ParameterDescriptor d : extension) {
                final String name = d.getName().getCode().trim().toLowerCase();
                if (replacements.put(name, d) != null) {
                    throw new InvalidParameterNameException(Errors.format(
                            ErrorKeys.DUPLICATED_VALUES_$1, name), name);
                }
            }
        }
        /*
         * JAI considers sources as a special kind of parameters, while GridCoverageProcessor makes
         * no distinction. If the registry element "operation" is really a JAI's OperationDescriptor
         * (which should occurs most of the time), prepend the JAI's sources before all ordinary
         * parameters. In addition, transforms the source type if needed.
         */
        final int numSources;
        final int numParameters = descriptor.getNumParameters();
        final Map<String,CharSequence> properties = new HashMap<String,CharSequence>();
        ParameterDescriptor[] desc;
        if (operation instanceof OperationDescriptor) {
            final OperationDescriptor op = (OperationDescriptor) operation;
            final String[]   names = op.getSourceNames();
            final Class<?>[] types = op.getSourceClasses(registryMode);
            numSources = op.getNumSources();
            desc = new ParameterDescriptor[numParameters + numSources];
            for (int i=0; i<numSources; i++) {
                Class<?> type = (Class<?>) sourceTypeMap.get(types[i]);
                if (type == null) {
                    type = types[i];
                }
                String name = names[i];
                properties.clear();
                if (numSources == 1) {
                    /*
                     * If there is only one source argument, rename for example "Source0"
                     * as "Source" for better compliance with OpenGIS usage. However, we
                     * will keep the original name as an alias.
                     */
                    final int length = name.length();
                    if (length != 0) {
                        final char c = name.charAt(length-1);
                        if (c=='0' || c=='1') {
                            properties.put(ALIAS_KEY, name);
                            name = name.substring(0, length-1);
                        }
                    }
                }
                properties.put(NAME_KEY, name);
                desc[i] = new DefaultParameterDescriptor(properties, type,
                            null,   // validValues
                            null,   // defaultValue
                            null,   // minimum
                            null,   // maximum
                            null,   // unit
                            true);  // required
            }
        } else {
            numSources = 0;
            desc = new ParameterDescriptor[numParameters];
        }
        /*
         * Source parameters completed. Now get the ordinary parameters. We check for
         * replacement of JAI parameters by user-supplied parameters in this process.
         */
        final String[]     names = descriptor.getParamNames();
        final Class<?>[] classes = descriptor.getParamClasses();
        final Object[]  defaults = descriptor.getParamDefaults();
        for (int i=0; i<numParameters; i++) {
            final String name = names[i];
            final ParameterDescriptor replacement = replacements.remove(name.trim().toLowerCase());
            if (replacement != null) {
                desc[i + numSources] = replacement;
                continue;
            }
            final Class<?> type = classes[i];
            final Range range = descriptor.getParamValueRange(name);
            final Comparable<?> min, max;
            if (range != null) {
                min = range.getMinValue();
                max = range.getMaxValue();
            } else {
                min = null;
                max = null;
            }
            EnumeratedParameter[] validValues;
            if (EnumeratedParameter.class.isAssignableFrom(type)) try {
                validValues = descriptor.getEnumeratedParameterValues(name);
            } catch (UnsupportedOperationException exception) {
                validValues = null;
            } else {
                validValues = null;
            }
            Object defaultValue = defaults[i];
            if (defaultValue == ParameterListDescriptor.NO_PARAMETER_DEFAULT) {
                defaultValue = null;
            }
            properties.clear();
            properties.put(NAME_KEY, name);
            if (operation instanceof OperationDescriptor) {
                final ImagingParameterDescription remark =
                        new ImagingParameterDescription((OperationDescriptor) operation, i);
                if (remark.exists()) {
                    properties.put(REMARKS_KEY, remark);
                }
            }
            desc[i + numSources] = new DefaultParameterDescriptor(properties,
                                    type, validValues, defaultValue, min, max, null, true);
        }
        /*
         * Appends the remaining extra descriptors. Note that some descriptor may
         * have been removed from the 'replacements' map before we reach this point.
         */
        int i = desc.length;
        desc = XArray.resize(desc, i + replacements.size());
        for (final ParameterDescriptor d : replacements.values()) {
            desc[i++] = d;
        }
        return desc;
    }

    /**
     * Creates a new instance of parameter value group. A JAI {@link javax.media.jai.ParameterList}
     * is created for holding parameter values, and wrapped into an {@link ImagingParameters}
     * instance.
     *
     * @return The new value initialized to the default value.
     */
    @Override
    public ParameterValueGroup createValue() {
        return new ImagingParameters(this);
    }

    /**
     * Compares the specified object with this parameter group for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object, compareMetadata)) {
            final ImagingParameterDescriptors that = (ImagingParameterDescriptors) object;
            return Utilities.equals(this.operation,  that.operation) &&
                   Utilities.equals(this.descriptor, that.descriptor);
        }
        return false;
    }

    /**
     * Returns a hash value for this parameter. This value doesn't need
     * to be the same in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return super.hashCode() ^ descriptor.hashCode();
    }
}
