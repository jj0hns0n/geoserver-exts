/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.opengeo.gsr.core.font;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public enum FontStyleEnum {

    ITALIC("italic"), NORMAL("normal"), OBLIQUE("oblique");
    private final String style;

    public String getStyle() {
        return style;
    }

    private FontStyleEnum(String style) {
        this.style = style;
    }

}
