import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class Decode3003 {

    public static void main(String[] args) throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem decode = CRS.decode("EPSG:3003");
        System.out.println(CRS.lookupIdentifier(decode, true));
    }
}
