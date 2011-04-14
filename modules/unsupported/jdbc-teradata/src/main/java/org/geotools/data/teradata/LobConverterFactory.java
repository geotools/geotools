package org.geotools.data.teradata;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

public class LobConverterFactory implements ConverterFactory {

    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {

        if (Blob.class.isAssignableFrom(source) && byte[].class.isAssignableFrom(target)) {
            return new Converter() {
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source instanceof Blob && byte[].class.isAssignableFrom(target)) {
                        Blob blob = (Blob) source;
                        InputStream blobIS = blob.getBinaryStream();
                        byte[] blobBA = new byte[blobIS.available()];
                        blobIS.read(blobBA);
                        blobIS.close();
                        return (T) blobBA;
                    }
                    return null;
                }
            };
        }

        if (Clob.class.isAssignableFrom(source) && String.class.isAssignableFrom(target)) {
            return new Converter() {
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source instanceof Clob && String.class.isAssignableFrom(target)) {
                        Clob clob = (Clob) source;
                        Reader clobReader = clob.getCharacterStream();
                        char[] clobChars = new char[(int) clob.length()];
                        clobReader.read(clobChars);
                        String clobString = new String(clobChars);
                        return (T) clobString;
                    }
                    return null;
                }
            };
        }

        return null;
    }
}
