/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.opengeo.gsr.core.domain;

import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public class CodedValueDomain extends Domain {

    private String name;

    @XStreamImplicit
    private Set<CodedValue> codedValues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CodedValue> getCodedValues() {
        return codedValues;
    }

    public void setCodedValues(Set<CodedValue> codedValues) {
        this.codedValues = codedValues;
    }

    public CodedValueDomain(String name, Set<CodedValue> codedValues) {
        super("codedValue");
        this.name = name;
        this.codedValues = codedValues;
    }
}
