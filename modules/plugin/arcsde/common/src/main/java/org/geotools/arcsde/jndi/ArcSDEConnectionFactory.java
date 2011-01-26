/*
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.jndi;

import static org.geotools.arcsde.session.ArcSDEConnectionConfig.CONNECTION_TIMEOUT_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.INSTANCE_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.MAX_CONNECTIONS_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.MIN_CONNECTIONS_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.PASSWORD_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.PORT_NUMBER_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.SERVER_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.USER_NAME_PARAM_NAME;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.geotools.arcsde.logging.Loggers;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.ISessionPoolFactory;
import org.geotools.arcsde.session.SessionPoolFactory;

/**
 * A {@link ObjectFactory} to create an ArcSDE {@link ISessionPool connection pool} to be JNDI
 * managed.
 * <p>
 * This factory creates an {@link ISessionPool} out of the following mandatory parameters:
 * <ul>
 * <li> {@link ArcSDEConnectionConfig#SERVER_NAME_PARAM_NAME server} (String) the arcsde server name
 * or IP address
 * <li> {@link ArcSDEConnectionConfig#PORT_NUMBER_PARAM_NAME port} (Integer) the TCP/IP port number
 * where ArcSDE is listening for connection requests
 * <li> {@link ArcSDEConnectionConfig#INSTANCE_NAME_PARAM_NAME instance} (String) the name of the
 * arcsde database
 * <li> {@link ArcSDEConnectionConfig#USER_NAME_PARAM_NAME user} (String) the database user name to
 * connect as
 * <li> {@link ArcSDEConnectionConfig#PASSWORD_PARAM_NAME password} (String) the database user
 * password
 * </ul>
 * And the following optional parameters:
 * <ul>
 * <li> {@link ArcSDEConnectionConfig#MIN_CONNECTIONS_PARAM_NAME pool.minConnections} (Integer) how
 * many connections the connection pool shall be populated with at creation time
 * <li> {@link ArcSDEConnectionConfig#MAX_CONNECTIONS_PARAM_NAME pool.maxConnections} (Integer) the
 * maximum number of connections allowed to be held on the pool at any time
 * <li> {@link ArcSDEConnectionConfig#CONNECTION_TIMEOUT_PARAM_NAME pool.timeOut} (Integer) how long
 * to wait for an available connection before {@link ISessionPool#getSession()} fails, in
 * milliseconds.
 * </ul>
 * </p>
 * <p>
 * See the package documentation for further information on how to configure JNDI resources for
 * ArcSDE on GeoTools.
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * 
 * @source $URL$
 * @version $Id$
 * @since 2.5.7
 */
public class ArcSDEConnectionFactory implements ObjectFactory {

    private static final Logger LOGGER = Loggers.getLogger("org.geotools.arcsde.jndi");

    private ISessionPoolFactory closablePoolFactory = SessionPoolFactory.getInstance();

    /**
     * @return an {@link ISessionPool} ready to be shared (ie, per connection option singleton).
     *         Whether shared or not is a matter of external JNDI configuration.
     * @see ObjectFactory#getObjectInstance(Object, Name, Context, Hashtable)
     */
    public Object getObjectInstance(final Object obj, final Name name, final Context nameCtx,
            final Hashtable<?, ?> environment) throws Exception {

        final Reference ref = (Reference) obj;

        LOGGER.info("ArcSDEConnectionFactory: ref is " + ref);

        final String className = ref.getClassName();

        LOGGER.info("ArcSDEConnectionFactory: className is " + className);

        // may an alternate SessionPoolFactory being set?
        checkAlternateSessionPoolFactory(ref);

        Object dereferencedObject = null;
        if (ISessionPool.class.getName().equals(className)) {
            ArcSDEConnectionConfig config = createConfig(ref);
            LOGGER.info("ArcSDEConnectionFactory: config is " + config);

            ISessionPool sharedPool = getSharedPool(config);
            LOGGER.info("ArcSDEConnectionFactory: shared pool is " + sharedPool);

            dereferencedObject = sharedPool;
        } else {
            LOGGER.info("ArcSDEConnectionFactory: not a config");
        }

        return dereferencedObject;
    }

    public ISessionPool getInstance(Map<String, Serializable> properties) throws IOException {
        ArcSDEConnectionConfig config = ArcSDEConnectionConfig.fromMap(properties);
        return getInstance(config);
    }

    public ISessionPool getInstance(ArcSDEConnectionConfig config) throws IOException {
        validate(config);
        ISessionPool sharedPool = getSharedPool(config);
        return sharedPool;
    }

    private void checkAlternateSessionPoolFactory(final Reference ref) {
        String poolFactoryClassName = getProperty(ref, "sessionPoolFactory", null);
        if (poolFactoryClassName == null) {
            return;
        }

        LOGGER.info("Using alternate session pool factory " + poolFactoryClassName);
        final ISessionPoolFactory newFactory;
        try {
            Class<?> factoryClass = Class.forName(poolFactoryClassName);
            newFactory = (ISessionPoolFactory) factoryClass.newInstance();

        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Alternate SessionPoolFactory class not found: "
                    + poolFactoryClassName);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        setClosableSessionPoolFactory(newFactory);
    }

    public void setClosableSessionPoolFactory(final ISessionPoolFactory newFactory) {
        this.closablePoolFactory = newFactory;
    }

    private ISessionPool getSharedPool(final ArcSDEConnectionConfig config) throws IOException {
        ISessionPool sharedPool = SharedSessionPool.getInstance(config, closablePoolFactory);
        return sharedPool;
    }

    private ArcSDEConnectionConfig createConfig(final Reference ref) {
        LOGGER.info("ArcSDEConnectionFactory: creating config");

        String server = getProperty(ref, SERVER_NAME_PARAM_NAME, null);
        String port = getProperty(ref, PORT_NUMBER_PARAM_NAME, null);
        String user = getProperty(ref, USER_NAME_PARAM_NAME, null);
        String password = getProperty(ref, PASSWORD_PARAM_NAME, null);

        String instance = getProperty(ref, INSTANCE_NAME_PARAM_NAME, null);
        String minConnections = getProperty(ref, MIN_CONNECTIONS_PARAM_NAME, "1");
        String maxConnections = getProperty(ref, MAX_CONNECTIONS_PARAM_NAME, "6");
        String connTimeout = getProperty(ref, CONNECTION_TIMEOUT_PARAM_NAME, "500");

        ArcSDEConnectionConfig config = new ArcSDEConnectionConfig();
        config.setServerName(server);
        config.setPortNumber(port);
        config.setDatabaseName(instance);
        config.setUserName(user);
        config.setPassword(password);
        config.setMinConnections(Integer.parseInt(minConnections));
        config.setMaxConnections(Integer.parseInt(maxConnections));
        config.setConnTimeOut(Integer.parseInt(connTimeout));

        validate(config);

        return config;
    }

    private void validate(ArcSDEConnectionConfig config) {
        if (config.getServerName() == null) {
            throw new IllegalArgumentException("Missing param: " + SERVER_NAME_PARAM_NAME);
        }
        if (config.getPortNumber() == null) {
            throw new IllegalArgumentException("Missing param: " + PORT_NUMBER_PARAM_NAME);
        }

        if (config.getUserName() == null) {
            throw new IllegalArgumentException("Missing param: " + USER_NAME_PARAM_NAME);
        }

        if (config.getPassword() == null) {
            throw new IllegalArgumentException("Missing param: " + PASSWORD_PARAM_NAME);
        }
    }

    protected String getProperty(final Reference ref, final String propName, final String defValue) {
        final RefAddr addr = ref.get(propName);
        if (addr == null) {
            return defValue;
        }
        return (String) addr.getContent();
    }

}
