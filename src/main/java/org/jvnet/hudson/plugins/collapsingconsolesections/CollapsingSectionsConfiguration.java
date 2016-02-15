/*
 * The MIT License
 *
 * Copyright 2013 Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jvnet.hudson.plugins.collapsingconsolesections;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Provides a serializable instance of collapsing sections global configs. 
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.4.1
 */
public class CollapsingSectionsConfiguration implements Serializable {
    private final CollapsingSectionNote[] sections;
    private final boolean numberingEnabled;
   
    public CollapsingSectionsConfiguration(CollapsingSectionNote[] sections, boolean numberingEnabled) {
        this.sections = sections != null ? sections : new CollapsingSectionNote[]{} ;
        this.numberingEnabled = numberingEnabled;
    }

    public boolean isNumberingEnabled() {
        return numberingEnabled;
    }

    public CollapsingSectionNote[] getSections() {
        return sections;
    }
    
    public SectionDefinition[] getSectionDefinitions() {
        CollapsingSectionNote[] configs = getSections();
        ArrayList<SectionDefinition> defs = new ArrayList<SectionDefinition>();

        for (CollapsingSectionNote config : configs) {
            defs.add(config.getDefinition());
        }

        return defs.toArray((SectionDefinition[]) Array.newInstance(SectionDefinition.class, 0));
    }
}
