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
package org.geotools.xsd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import javax.xml.namespace.QName;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.util.Utilities;
import org.geotools.xs.XSConfiguration;
import org.geotools.xsd.impl.PicoMap;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.DuplicateComponentKeyRegistrationException;

/**
 * Responsible for configuring a parser runtime environment.
 *
 * <p>Implementations have the following responsibilities:
 *
 * <ul>
 *   <li>Configuration of bindings.
 *   <li>Configuration of context used by bindings.
 *   <li>Supplying specialized handlers for looking up schemas.
 *   <li>Supplying specialized handlers for parsing schemas.
 *   <li>Declaring dependencies on other configurations
 * </ul>
 *
 * <h3>Dependencies</h3>
 *
 * <p>Configurations have dependencies on one another, that results from the fact that one schema
 * imports another. Configuration dependencies are transitive. Each configuration should declare all
 * dependencies in the constructor using the {@link #addDependency(Configuration)} method. <code>
 *         <pre>
 *         class MyConfiguration extends Configuration {
 *     public MyConfiguration() {
 *       super();
 *
 *       addDependency( new FooConfiguration() );
 *       addDependency( new BarConfiguration() );
 *     }
 *     ...
 *  }
 *         </pre>
 * </code>
 *
 * <h3>Binding Configuration</h3>
 *
 * <p>To enable a particular binding to be found during a parse, the configuration must first
 * populate a container with said binding. This can be done by returning the appropriate instance of
 * {@link org.geotools.xml.BindingConfiguration} in {@link #getBindingConfiguration()}:
 *
 * <pre>
 *          <code>
 *  BindingConfiguration getBindingConfiguration() {
 *      return new MyBindingConfiguration();
 *  }
 *          </code>
 *  </pre>
 *
 * Instances of type {@link org.geotools.xml.BindingConfiguration} are used to populate a container
 * with all the bindings from a particular schema.
 *
 * <h3>Context Configuration</h3>
 *
 * <p>Many bindings have dependencies on other types of objects. The pattern used to satisfy these
 * dependencies is known as <b>Constructor Injection</b>. Which means that any dependencies a
 * binding has is passed to it in its constructor. For instance, the following binding has a
 * dependency on java.util.List.
 *
 * <pre>
 *         <code>
 * class MyBinding implements SimpleBinding {
 *
 *                List list;
 *
 *                 public MyBinding(List list) {
 *                         this.list = list;
 *                 }
 * }
 *         </code>
 * </pre>
 *
 * Before a binding can be created, the container in which it is housed in must be able to satisfy
 * all of its dependencies. It is the responsibility of the configuration to satisfy this criteria.
 * This is known as configuring the binding context. The following is a suitable configuration for
 * the above binding.
 *
 * <pre>
 *         <code>
 * class MyConfiguration extends Configuration {
 *        ....
 *                void configureContext(MutablePicoContainer container) {
 *                        container.registerComponentImplementation(ArrayList.class);
 *                }
 * }
 *         </code>
 * </pre>
 *
 * <h3>Schema Resolution</h3>
 *
 * <p>XML instance documents often contain schema uri references that are invalid with respect to
 * the parser, or non-existent. A configuration can supply specialized look up classes to prevent
 * the parser from following an invalid uri and prevent any errors that may occur as a result.
 *
 * <p>An instance of {@link org.eclipse.xsd.util.XSDSchemaLocationResolver} can be used to override
 * a schemaLocation referencing another schema. This can be useful when the entity parsing an
 * instance document stores schemas in a location unknown to the entity providing the instance
 * document.
 *
 * <p>An instance of {@link org.eclipse.xsd.util.XSDSchemaLocator} can be used to provide an
 * pre-parsed schema and prevent the parser from parsing a schemaLocation manually. This can be
 * useful when an instance document does not supply a schemaLocation for the targetNamespace of the
 * document.
 *
 * <pre>
 *         <code>
 * class MyConfiguration implements Configuration {
 *
 *                XSDSchemaLocationResolver getSchemaLocationResolver() {
 *                  return new MySchemaLocationResolver();
 *                }
 *
 *                XSDSchemaLocator getSchemaLocator() {
 *                  return new MySchemaLocator();
 *                }
 * }
 *         </code>
 * </pre>
 *
 * <p>The XSDSchemaLocator and XSDSchemaLocationResolver implementations are used in a couple of
 * scenarios. The first is when the <b>schemaLocation</b> attribute of the root element of the
 * instance document is being parsed. The schemaLocation attribute has the form:
 *
 * <pre>
 * <code>
 *         schemaLocation="namespace location namespace location ..."
 * </code>
 * </pre>
 *
 * In which (namespace,location) tuples are listed. For each each namespace encountered when parsing
 * the schemaLocation attribute, an appropriate resolver / locator is looked up. If an override is
 * not available, the framework attempts to resolve the location part of the tuple into a schema.
 *
 * <p>The second scenario occurs when the parsing of a schema encounters an <b>import</b> or an
 * <b>include</b> element. These elements have the form:
 *
 * <pre>
 *  <code>
 *      &lt;import namespace="" schemaLocation=""/&gt;
 *        </code>
 *  </pre>
 *
 * and:
 *
 * <pre>
 *  <code>
 *      &lt;include schemaLocation=""&gt;
 *  </code>
 *        </pre>
 *
 * respectively. Similar to above, the schemaLocation (and namespace in the case of an import) are
 * used to find an override. If not found they are resolved directly.
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 * @see org.geotools.xml.BindingConfiguration
 */
public abstract class Configuration {
    /** XSD instance */
    private final XSD xsd;

    /** List of configurations depended on. */
    private final List<Configuration> dependencies;

    /** List of parser properties. */
    private final Set<QName> properties;

    /** Internal context */
    private final MutablePicoContainer context;

    /**
     * Creates a new configuration.
     *
     * <p>Any dependent schemas should be added in subclass constructor. The xml schema dependency
     * does not have to be added.
     */
    public Configuration(XSD xsd) {
        this.xsd = xsd;
        dependencies = Collections.synchronizedList(new ArrayList<>());

        // bootstrap check
        if (!(this instanceof XSConfiguration)) {
            dependencies.add(new XSConfiguration());
        }

        properties = Collections.synchronizedSet(new HashSet<>());
        context = new DefaultPicoContainer();
    }

    /** The XSD instance representing the schema for which the schema works against. */
    public XSD getXSD() {
        return xsd;
    }

    /** @return a list of direct dependencies of the configuration. */
    public final List<Configuration> getDependencies() {
        return dependencies;
    }

    /**
     * Returns a list of parser properties to set.
     *
     * <p>To set a parser property:
     *
     * <pre>
     * Configuration configuration = ...
     * configuration.getProperties().add( Parser.Properties.... );
     * </pre>
     *
     * <p>Beware this class is not thread safe so take the needed precautions when using the list
     * returned by this method.
     *
     * @return A list of hte set parser properties.
     */
    public final Set<QName> getProperties() {
        return properties;
    }

    /** Searches the configuration and all dependent configuration for the specified property. */
    public final boolean hasProperty(QName property) {
        for (Configuration configuration : allDependencies()) {
            if (configuration.getProperties().contains(property)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns all dependencies in the configuration dependency tree.
     *
     * <p>The return list contains no duplicates.
     *
     * @return All dependencies in the configuration dependency tree.
     */
    public final List<Configuration> allDependencies() {
        LinkedList<Configuration> unpacked = new LinkedList<>();

        Stack<Configuration> stack = new Stack<>();
        stack.push(this);

        while (!stack.isEmpty()) {
            Configuration c = stack.pop();

            if (!unpacked.contains(c)) {
                unpacked.addFirst(c);
                stack.addAll(c.getDependencies());
            }
        }

        if (unpacked.size() < 2) {
            return unpacked;
        }

        // create a graph of the dependencies
        DepGraph g = new DepGraph();
        for (Configuration c : unpacked) {
            for (Configuration d : c.getDependencies()) {
                g.addEdge(c, d);
            }
        }

        PriorityQueue<DepNode> q =
                new PriorityQueue<>(
                        g.nodes.size(),
                        (o1, o2) ->
                                Integer.valueOf(o1.outgoing().size())
                                        .compareTo(o2.outgoing().size()));
        for (DepNode n : g.nodes.values()) {
            q.add(n);
        }

        unpacked = new LinkedList<>();
        while (!q.isEmpty()) {
            DepNode n = q.remove();
            if (n.outgoing().size() != 0) {
                throw new IllegalStateException();
            }

            unpacked.add(n.config);
            for (DepNode i : n.incoming()) {
                g.removeEdge(i.config, n.config);
                /*
                 * PriorityQueue.remove(Object) is broken in Java 5
                 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6207984
                 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6268068
                 */
                q.removeAll(Collections.singletonList(i));
                q.add(i);
            }
        }

        return unpacked;
    }

    /**
     * Returns the first dependency of this configuration of the specified type.
     *
     * @since 8.0
     */
    public <C extends Configuration> C getDependency(Class<C> clazz) {
        List dependencies = allDependencies();
        @SuppressWarnings("unchecked")
        C cast =
                (C)
                        dependencies.stream()
                                .filter(dep -> clazz.isInstance(dep))
                                .findFirst()
                                .orElse(null);
        return cast;
    }

    /**
     * Adds a dependent configuration.
     *
     * <p>This method should only be called from the constructor.
     */
    protected void addDependency(Configuration dependency) {
        if (dependencies.contains(dependency)) {
            return;
        }

        dependencies.add(dependency);
    }

    /** @return The namespace of the configuration schema. */
    public final String getNamespaceURI() {
        return getXSD().getNamespaceURI();
    }

    /**
     * Returns an internal context which is copied into the runtime context while parsing.
     *
     * <p>This context is provided to allow for placing values in the parsing context without having
     * to subclass.
     *
     * @return The context.
     */
    public final MutablePicoContainer getContext() {
        return context;
    }

    /**
     * Creates the map of QName to Binding which is used during parsing to attach bindings to an
     * element,attribute, or type.
     *
     * @return A map of Qname,[Class|Object]
     */
    public final Map<QName, Object> setupBindings() {
        Map<QName, Object> bindings = new HashMap<>();

        // wrap the binding map up in a pico container for backwards compatibility
        // with old api which registered bindings in a pico container
        PicoMap container = new PicoMap(bindings);

        // configure bindings of all dependencies
        for (Configuration dependency : allDependencies()) {
            dependency.registerBindings(bindings);

            // call old api
            dependency.registerBindings((MutablePicoContainer) container);
        }
        for (Configuration dependency : allDependencies()) {
            dependency.configureBindings(bindings);

            // call old api
            dependency.configureBindings((MutablePicoContainer) container);
        }

        return bindings;
    }

    /**
     * Prepares a parser instance for use with this Configuration instance and all of its
     * dependencies.
     *
     * @since 2.7
     */
    public final void setupParser(Parser parser) {
        for (Configuration dep : allDependencies()) {
            dep.configureParser(parser);
        }
    }

    /**
     * Prepares a encoder instance for use with this Configuration instance and all of its
     * dependencies.
     *
     * @since 2.7
     */
    public final void setupEncoder(Encoder encoder) {
        for (Configuration dep : allDependencies()) {
            dep.configureEncoder(encoder);
        }
    }

    /**
     * Registers the bindings for the configuration.
     *
     * <p>This method is intended to provide the default bindings for a configuration and is
     * intended to be subclassed by client code. Client code should use {@link
     * #configureBindings(MutablePicoContainer)} . Subclasses should mark this method as final after
     * implementing.
     *
     * @param container Container containing all bindings, keyed by {@link QName}.
     */
    protected void registerBindings(MutablePicoContainer container) {
        // do nothing, in the case where the subclass has overridden the config
        // will recognize and adapt this method to #registerBindings(Map)
        // accordingly (see #setupBindings()}
    }

    /**
     * Registers the bindings for the configuration.
     *
     * <p>This method is intended to provide the "default" bindings for a configuration and is not
     * intended to be subclassed by client code. Client code should use {@link
     * #configureBindings(MutablePicoContainer)} to override/remove/add new bindings on the fly.
     *
     * <p>The key of the <tt>bindings</tt> map is of type {@link QName}. The value can be class, or
     * an instance. In the case of a class, the binding will be instantiated by the parser at
     * runtime. In the instance case the binding will be used as is.
     */
    protected void registerBindings(Map<QName, Object> bindings) {}

    /**
     * Template method allowing subclass to override any bindings.
     *
     * @param container Container containing all bindings, keyed by {@link QName}.
     */
    protected void configureBindings(MutablePicoContainer container) {
        // do nothing
    }

    /**
     * Template method allowing subclass to override any bindings.
     *
     * @param bindings Map containing all bindings, keyed by {@link QName}.
     */
    protected void configureBindings(Map<QName, Object> bindings) {
        // do nothing
    }

    /**
     * Configures the root context to be used when parsing elements.
     *
     * @param container The container representing the context.
     */
    public final MutablePicoContainer setupContext(MutablePicoContainer container) {
        // configure bindings of all dependencies
        List dependencies = allDependencies();

        for (Object value : dependencies) {
            Configuration dependency = (Configuration) value;

            // throw locator and location resolver into context
            XSDSchemaLocationResolver resolver = new SchemaLocationResolver(dependency.getXSD());

            if (resolver != null) {
                QName key = new QName(dependency.getNamespaceURI(), "schemaLocationResolver");
                container.registerComponentInstance(key, resolver);
            }

            XSDSchemaLocator locator = new SchemaLocator(dependency.getXSD());

            if (locator != null) {
                QName key = new QName(dependency.getNamespaceURI(), "schemaLocator");
                container.registerComponentInstance(key, locator);
            }

            // set any parser properties
            synchronized (dependency.getProperties()) {
                for (QName property : dependency.getProperties()) {
                    try {
                        container.registerComponentInstance(property, property);
                    } catch (DuplicateComponentKeyRegistrationException e) {
                        // ok, ignore
                    }
                }
            }

            // add any additional configuration, factories and such
            // create a new container to allow configurations to override factories in dependant
            // configurations
            container = container.makeChildContainer();
            dependency.configureContext(container);
        }

        // copy the internal context over
        if (!context.getComponentAdapters().isEmpty()) {
            container = container.makeChildContainer();

            for (Object o : context.getComponentAdapters()) {
                ComponentAdapter adapter = (ComponentAdapter) o;
                container.registerComponent(adapter);
            }
        }

        return container;
    }

    /**
     * Configures the root context to be used when parsing elements.
     *
     * <p>The context satisfies any dependencies needed by a binding. This is often a factory used
     * to create something.
     *
     * <p>This method should be overridden. The default implementation does nothing.
     *
     * @param container The container representing the context.
     */
    protected void configureContext(MutablePicoContainer container) {}

    /**
     * Configures the parser to be used with this configuration.
     *
     * <p>This method provides a callback for Configuration instances to configure the parser with
     * whatever options they require.
     */
    protected void configureParser(Parser parser) {}

    /**
     * Configures the encoder to be used with this configuration.
     *
     * <p>This method provides a callback for Configuration instances to configure the encoder with
     * whatever options they require.
     */
    protected void configureEncoder(Encoder encoder) {}

    /** Equals override, equality is based solely on {@link #getNamespaceURI()}. */
    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Configuration) {
            Configuration other = (Configuration) obj;

            return Utilities.equals(getNamespaceURI(), other.getNamespaceURI());
        }

        return false;
    }

    @Override
    public final int hashCode() {
        if (getNamespaceURI() != null) {
            return getNamespaceURI().hashCode();
        }

        return 0;
    }

    static class DepGraph {
        Map<Configuration, DepNode> nodes = new HashMap<>();

        public void addEdge(Configuration from, Configuration to) {
            DepNode src = addNode(from);
            DepNode dst = addNode(to);

            DepEdge dep = src.getEdge(dst);
            if (dep != null) {
                return;
            }

            // ensure two configurations not dependent on each other
            if (dst.getEdge(src) != null) {
                throw new IllegalArgumentException("Cycle between " + from + ", " + to);
            }

            dep = new DepEdge(src, dst);
            src.edges.add(dep);
            dst.edges.add(dep);
        }

        public void removeEdge(Configuration from, Configuration to) {
            DepNode src = addNode(from);
            DepNode dst = addNode(to);

            DepEdge dep = src.getEdge(dst);
            if (dep == null) {
                throw new IllegalStateException("No such edge: " + from + "," + to);
            }

            src.edges.remove(dep);
            dst.edges.remove(dep);
        }

        DepNode addNode(Configuration config) {
            DepNode node = nodes.get(config);
            if (node == null) {
                node = new DepNode(config);
                nodes.put(config, node);
            }
            return node;
        }
    }

    static class DepNode {
        Configuration config;
        List<DepEdge> edges = new ArrayList<>();

        DepNode(Configuration config) {
            this.config = config;
        }

        DepEdge getEdge(DepNode node) {
            for (DepEdge edge : edges) {
                if (edge.src == this && edge.dst == node) {
                    return edge;
                }
            }

            return null;
        }

        public List<DepNode> incoming() {
            List<DepNode> incoming = new ArrayList<>();
            for (DepEdge edge : edges) {
                if (edge.dst == this) {
                    incoming.add(edge.src);
                }
            }
            return incoming;
        }

        public List<DepNode> outgoing() {
            List<DepNode> outgoing = new ArrayList<>();
            for (DepEdge edge : edges) {
                if (edge.src == this) {
                    outgoing.add(edge.dst);
                }
            }
            return outgoing;
        }

        @Override
        public String toString() {
            return config.toString();
        }
    }

    static class DepEdge {
        DepNode src, dst;

        DepEdge(DepNode src, DepNode dst) {
            this.src = src;
            this.dst = dst;
        }

        @Override
        public String toString() {
            return "[" + src.toString() + ", " + dst.toString() + "]";
        }
    }
}
