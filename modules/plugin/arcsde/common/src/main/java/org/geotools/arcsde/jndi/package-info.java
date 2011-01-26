/**
 * This package contains relevant classes to support configuring a GeoTools ArcSDE
 * <code>ISessionPool</code> (a pool of connections to an ArcSDE server) inside a Java Naming and
 * Directory Interface (JNDI) context.
 * <p>
 * Since ArcSDE uses a legacy protocol to communicate between the ArcSDE service instance and the
 * ArcSDE Java API, and it's not possible to connect to an ArcSDE service through the java JDBC API,
 * we can't use the standard <code>javax.sql.DataSource</code> object factories. Hence, we provide a
 * custom <code>javax.naming.ObjectFactory</code> specialized in creating ArcSDE connection pools
 * for the GeoTools library.
 * </p>
 * <h3>Configuration inside a J2EE Container</h3>
 * <p>
 * The JNDI specification acts only at the API level, so how to configure a given resource to be accessible
 * through JNDI is J2EE Container specific. Some of them provide a means to configure an
 * ObjectFactory to provide a specific kind of resource (whether it is a JDBC DataSource, a standard
 * Java Bean or any other object type). For instance, the Apache Tomcat container provides such a
 * mechanism, but the JBoss Application Server does not, even though it uses Apache Tomcat
 * internally.
 * </p>
 * 
 * <h4>Classpath configuration for container-wide shared connection pools</h4>
 * <p>
 * Whether an ArcSDE connection pool is going to be shared among different web applications
 * on the same J2EE container is a matter of how you lay out the ArcSDE Jars on the classpath.
 * That is, if you want to achieve a container instance wide shared session pool (for example
 * to have various GeoServer instances sharing the same connection pool), you need to drop
 * certain jar files in the container's shared libraries folder instead of inside 
 * {@code <geoserver war>/WEB-INF/lib}. For instance, the following jars shall be put on such
 * a shared library directory: 
 * <ul>
 * <li>{@code jsde_sdk.jar, jpe_sdk.jar and icu4j.jar} from your ArcSDE Java SDK installation.
 * <li>{@code gt-arcsde-common-<version>.jar}
 * <li>{@code commons-pool-1.3.jar} (or whatever commons-pool version the gt-arcsde-common depends on)
 * </ul>
 * gt-arcsde-<GT Version>.jar, which provides the actual DataStore implementation may be placed
 * inside your app's {@code WEB-INF/lib} directory though.
 * </p>
 * <p>
 * The following are configuration examples to set up <a href="http://geosever.org">GeoServer</a>
 * for Tomcat and JBoss with ArcSDE JNDI support, but apply to any other web application where you 
 * want to use GeoTools with ArcSDE and JNDI.
 * </p>
 * <p>
 * First off, you need to add an entry on the web application's WEB-INF/web.xml file to indicate the
 * JNDI resource that's going to be available for the application. Open the WEB-INF/web.xml file
 * and add an entry as the following as the last element before {@code </web-app>}:
 * 
 * <pre>
 * <code>
 * &lt;resource-ref&gt;
 *   &lt;description&gt;JNDI arcsde resource configuration&lt;/description&gt;
 *   &lt;res-ref-name&gt;geotools/arcsde&lt;/res-ref-name&gt;
 *   &lt;res-type&gt;org.geotools.arcsde.session.ISessionPool&lt;/res-type&gt;
 *   &lt;res-auth&gt;Container&lt;/res-auth&gt;
 * &lt;/resource-ref&gt;
 * </code>
 * </pre>
 * Where the values for description and res-ref-name are of your choice, res-ref-name being the path under
 * which the arcsde connection pool is configured on your JNDI container (see bellow for instructions on how
 * to configure such a resource on Tomcat and JBoss).
 * </p>
 * <h4>Configuration in Tomcat</h4>
 * <p>
 * <ul>
 * <li>Copy the {@code jsde_sdk.jar, jpe_sdk.jar, icu4j.jar, commons-pool-1.3.jar and gt-arcsde-common-<version>.jar}
 * files to the Tomcat shared libs folder (either <catalina home>/lib or <catalina home>/common/lib folder,
 * depending on your Tomcat version).
 * <li>Expand geoserver.war into <catalina home>/webapps
 * <li>copy {@code gt-arcsde-<version>.jar} to <catalina home>/webapps/geoserver/WEB-INF/lib
 * </ul>
 * Finally, configure the {@link ArcSDEConnectionFactory ArcSDE Connection Factory} as explained in the
 * <i>Adding Custom Resource Factories</i> section of the Tomcat's
 * <a href="http://tomcat.apache.org/tomcat-6.0-doc/jndi-resources-howto.html">JNDI Resources HOW-TO</a>. That is,
 * add an entry as the following to the file {@code <catalina home>/conf/context.xml} as a child of the root
 * {@code <Context>} element:
 * <pre>
 * <code>
 *   &lt;Resource name="geotools/arcsde" auth="Container" type="org.geotools.arcsde.session.ISessionPool"
 *             factory="org.geotools.arcsde.jndi.ArcSDEConnectionFactory" 
 *             server="&lt;arcsde server>" 
 *             user="&lt;arcsde user name>"
 *             password="&lt;arcsde user password>" 
 *             instance="&lt;arcsde database name>" 
 *             port="&lt;arcsde instance port number&gt;"
 *   /&gt;
 * </code>
 * </pre>
 * 
 * And that's it. Now you're able to select the <i>ArcSDE JNDI</i> DataStore factory and use the {@code "geotools/arcsde"}
 * (or whatever entry name of your choice) as the DataStore's JNDI resource path name.
 * </p>
 *  
 * <h4>Configuration in JBoss Application Server (4.0.3+)</h4>
 * <p>
 * JBoss provides an extensible mechanism to configure any kind of object as a JNDI resource
 * starting with JBoss version <b>4.0.3</b>. Prior versions do not provide this mechanism and hence
 * it is not possible to configure a GeoTools ArcSDE connection pool for versions lower than 4.0.3.
 * Disregard what the JBoss documentation says on its <a href="http://www.jboss.org/file-access/default/members/jbossweb/freezone/docs/latest/jndi-resources-howto.html"
 * >JNDI Resources HOW-TO</a> , that seems to be a verbatim copy of the Tomcat documentation but is
 * just not supported.
 * </p>
 * <p>
 * To configure a JNDI ArcSDE session pool on JBoss, you need to create an xml file containing the managed bean definition
 * that configures the ArcSDE connection pool. You can call this file as you want, for example {@code geoserver-service.xml},
 * and place it on the deploy folder for the JBoss configuration that you're going to run. 
 * For example, {@code <jboss installation dir>/server/default/deploy/geoserver-service.xml} 
 * </p>
 * <p>
 * The contents of the {@code geoserver-service.xml} file shall be as following. For more information about this kind of
 * configuration consult the JBoss' <a href="http://www.jboss.org/community/wiki/JNDIBindingServiceMgr">JNDIBindingServiceMgr</a>
 * documentation.
 * <pre>
 * <code>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?>
 * &lt;!DOCTYPE server PUBLIC &quot;-//JBoss//DTD MBean Service 4.0//EN&quot;
 *           &quot;http://www.jboss.org/j2ee/dtd/jboss-service_4_0.dtd&quot;>
 * &lt;server>
 *    &lt;mbean code=&quot;org.jboss.naming.JNDIBindingServiceMgr&quot;
 *         name=&quot;jboss.tests:service=JNDIBindingServiceMgr&quot;>
 *       &lt;attribute name=&quot;BindingsConfig&quot; serialDataType=&quot;jbxb&quot;>
 *          &lt;jndi:bindings
 *         xmlns:xs=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;
 *         xmlns:jndi=&quot;urn:jboss:jndi-binding-service:1.0&quot;
 *         xs:schemaLocation=&quot;urn:jboss:jndi-binding-service:1.0 resource:jndi-binding-service_1_0.xsd&quot;>
 * 
 *             &lt;jndi:binding name=&quot;geotools/arcsdeGlobal&quot;>
 *                &lt;java:properties xmlns:java=&quot;urn:jboss:java-properties&quot;
 *                         xmlns:xs=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;
 *                         xs:schemaLocation=&quot;urn:jboss:java-properties resource:java-properties_1_0.xsd&quot;>
 *                   &lt;java:property>
 *                      &lt;java:key>server&lt;/java:key>
 *                      &lt;java:value>172.16.241.128&lt;/java:value>
 *                   &lt;/java:property>
 *                   &lt;java:property>
 *                      &lt;java:key>port&lt;/java:key>
 *                      &lt;java:value>5151&lt;/java:value>
 *                   &lt;/java:property>
 *                   &lt;java:property>
 *                      &lt;java:key>instance&lt;/java:key>
 *                      &lt;java:value>sde&lt;/java:value>
 *                   &lt;/java:property>
 *                   &lt;java:property>
 *                      &lt;java:key>user&lt;/java:key>
 *                      &lt;java:value>sde&lt;/java:value>
 *                   &lt;/java:property>
 *                   &lt;java:property>
 *                      &lt;java:key>password&lt;/java:key>
 *                      &lt;java:value>sde&lt;/java:value>
 *                   &lt;/java:property>
 *                   &lt;java:property>
 *                      &lt;java:key>pool.minConnections&lt;/java:key>
 *                      &lt;java:value>2&lt;/java:value>
 *                   &lt;/java:property>
 *                   &lt;java:property>
 *                      &lt;java:key>pool.maxConnections&lt;/java:key>
 *                      &lt;java:value>10&lt;/java:value>
 *                   &lt;/java:property>
 *                   &lt;java:property>
 *                      &lt;java:key>pool.timeOut&lt;/java:key>
 *                      &lt;java:value>1000&lt;/java:value>
 *                   &lt;/java:property>
 *                &lt;/java:properties>
 *             &lt;/jndi:binding>
 * 
 *          &lt;/jndi:bindings>
 *       &lt;/attribute>
 *       &lt;depends>jboss:service=Naming&lt;/depends>
 *    &lt;/mbean>
 * &lt;/server&gt;
 * </code>
 * </pre>
 * </p>
 * <p>
 * What we're doing here is to create a {@code java.util.Properties} object containing the connection parameters, and storing
 * it on the JNDI container at the {@code geotools/arcsdeGlobal} path. This is going to be a globally accessible resource that
 * the {@link ArcSDEConnectionFactory} will use to create the appropriate {@link org.geotools.arcsde.session.ISessionPool connection pool}.
 * </p>
 * <p>
 * But to get this resource visible to your web application, there's one more step missing. In addition to the resource-ref
 * entry in {@code WEB-INF/web.xml} mentioned above, JBoss requires an extra config file to manage the indirection between
 * your web application and the globally configured JNDI resource.
 * You'll need to create a file called {@code jboss-web.xml} as a sibling of {@code WEB-INF/web.xml} with the following content:
 * <pre>
 * <code>
 * <?xml version="1.0" encoding="UTF-8"?>
 * <jboss-web>
 *     <resource-ref>
 *         <res-ref-name>geotools/arcsde</res-ref-name>
 *         <res-type>org.geotools.arcsde.session.ISessionPool</res-type>
 *         <jndi-name>geotools/arcsdeGlobal</jndi-name>
 *     </resource-ref>
 * </jboss-web>
 * </code>
 * </pre>
 * Where {@code geotools/arcsdeGlobal} is the name of the globally configured resource, and {@code geotools/arcsde}
 * is the JNDI name by which the resource will actually be accessible to your web application, and shall match
 * the name used for the resource-ref entry in {@code WEB-INF/web.xml}.
 * </p>
 * <p>
 * Now you're ready to select the <i>ArcSDE JNDI</i> DataStore factory and use the {@code "geotools/arcsde"}
 * (or whatever entry name of your choice you have configured) as the DataStore's JNDI resource path name.
 * </p>
 */
package org.geotools.arcsde.jndi;

