package org.geotools.data.sqlserver.reader;

import java.io.IOException;

/**
 * @author Anders Bakkevold, Bouvet
 *
 * @source $URL$
 */
public class SqlServerBinaryParseException extends IOException {

    public SqlServerBinaryParseException(String message) {
        super(message);
    }
}
