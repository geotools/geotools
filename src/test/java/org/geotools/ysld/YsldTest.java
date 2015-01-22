package org.geotools.ysld;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.geotools.ysld.Ysld.YsldInput;
import org.junit.Test;

public class YsldTest {
    
    @Test
    public void readerTest() throws IOException {
        InputStream inputStream = YsldTest.class.getResourceAsStream("point.yml");
        YsldInput reader = Ysld.reader(inputStream);
        reader.close();
        try {
            inputStream.read();
            fail("inputStream should be closed");
        } catch (IOException e) {
            //expect IOException reading from a closed reader
        }
        
    }

}
