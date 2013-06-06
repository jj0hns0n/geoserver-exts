package org.opengeo.gsr.core.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeometryArrayTest {

    
    /*
     * A.3.7: geometry/array - Verify that every array object meets the requirements
     */
    @Test
    public void isValidGeometryTypesTest() {
        double[] coord1 = { -77.1, 40.07 };
        double[] coord2 = { -79.1, 38.07 };
        double[] coord3 = { -105.1, -29.08 };
        SpatialReference spatialRef = new SpatialReferenceWKID(4326);
        Point point1 = new Point(coord1[0], coord1[1], spatialRef);
        Point point2 = new Point(coord2[0], coord2[1], spatialRef);
        double[][] coords = { coord1, coord2, coord3, coord1 };
        double[][][] rings = { coords };
        Polygon polygon = new Polygon(rings, spatialRef);
        Geometry[] geometries1 = { point1, point2 };
        Geometry[] geometries2 = { point1, point2, polygon };
        GeometryArray geometryArray1 = new GeometryArray(GeometryTypeEnum.POINT, geometries1,
                spatialRef);
        GeometryArray geometryArray2 = new GeometryArray(GeometryTypeEnum.POINT, geometries2,
                spatialRef);

        assertEquals(true, geometryArray1.isValidGeometryTypes());
        assertEquals(false, geometryArray2.isValidGeometryTypes());
    }
}
