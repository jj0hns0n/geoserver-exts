/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.opengeo.gsr.core.feature;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public enum FieldTypeEnum {

    SHORT_INTEGER("esriFieldTypeSmallInteger"),
    INTEGER("esriFieldTypeInteger"),
    SINGLE("esriFieldTypeSingle"),
    DOUBLE("esriFieldTypeDouble"),
    STRING("esriFieldTypeString"),
    DATE("esriFieldTypeDate"),
    OID("esriFieldTypeOID"),
    GEOMETRY("esriFieldTypeGeometry"),
    GUID("esriFieldTypeGUID");

    private final String fieldType;

    public String getFieldType() {
        return fieldType;
    }

    private FieldTypeEnum(String fieldType) {
        this.fieldType = fieldType;
    }

    public static FieldTypeEnum forClass(Class<?> binding) {
        if (String.class.equals(binding)) {
            return STRING;
        } else if (Float.class.equals(binding)) {
            return SINGLE;
        } else if (Double.class.equals(binding) || BigDecimal.class.equals(binding)) {
            return DOUBLE;
        } else if (Byte.class.equals(binding) || Short.class.equals(binding)) {
            return SHORT_INTEGER;
        } else if (Integer.class.equals(binding) || Long.class.equals(binding) || BigInteger.class.equals(binding)) {
            return INTEGER;
        } else if (Date.class.equals(binding)) {
            return DATE;
        } else if (Geometry.class.isAssignableFrom(binding)) {
            return GEOMETRY;
        } else {
            throw new RuntimeException("No FieldType equivalent known for " + binding);
        }
    }
}
