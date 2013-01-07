package org.geotools.data.ingres;

import java.util.Map;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;
import static org.geotools.data.ingres.IngresDataStoreFactory.LOOSEBBOX;

/**
 * Ingres DataStoreFactory for connections obtained from JNDI
 * 
 *  @source $URL$
 */
public class IngresJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public IngresJNDIDataStoreFactory() {
        super(new IngresDataStoreFactory());
    }

    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
    }

}
