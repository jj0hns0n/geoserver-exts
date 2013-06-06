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
public enum FontDecorationEnum {

    LINE_THROUGH("line-through"), UNDERLINE("underline"), NONE("none");
    private final String style;

    public String getStyle() {
        return style;
    }

    private FontDecorationEnum(String style) {
        this.style = style;
    }
}
