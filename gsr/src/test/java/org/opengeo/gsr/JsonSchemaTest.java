package org.opengeo.gsr;

import java.io.File;

import org.opengeo.gsr.core.format.GeoServicesJsonFormat;
import org.opengeo.gsr.validation.JSONValidator;

import com.thoughtworks.xstream.XStream;

public abstract class JsonSchemaTest {

    final protected static XStream xstream = new GeoServicesJsonFormat().getXStream();

    public JsonSchemaTest() {
    }

    public static String getJson(Object obj) {
        return xstream.toXML(obj);
    }

    public static boolean validateJSON(String json, String schemaPath) {
        File schemaFile = new java.io.File(System.getProperty("user.dir") + "/src/test/resources/schemas/" + schemaPath);
        return JSONValidator.isValidSchema(json, schemaFile);
    }
}
