package org.geotools.referencing.tools;

import javax.sql.DataSource;

import org.geotools.referencing.factory.epsg.ThreadedH2EpsgFactory;
import org.h2.tools.Server;

/**
 * Starts a H2 console connected to the EPSG database so that you can explore it
 * interactively
 *
 *
 * @source $URL$
 */
public class Start {
    public static void main(String[] args) throws Exception {
        ThreadedH2EpsgFactory factory = new ThreadedH2EpsgFactory();
        DataSource source = factory.getDataSource();
        Server s = new Server();
        s.startWebServer(source.getConnection());
    }
}
