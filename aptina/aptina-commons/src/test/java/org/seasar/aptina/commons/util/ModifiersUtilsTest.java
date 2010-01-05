/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.aptina.commons.util;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import junit.framework.TestCase;

import static org.seasar.aptina.commons.util.ModifierUtils.*;

/**
 * 
 * @author koichik
 */
public class ModifiersUtilsTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIsDefault() throws Exception {
        final Set<Modifier> modifiers = new HashSet<Modifier>();
        modifiers.add(Modifier.STATIC);
        assertTrue(isDefault(modifiers));

        modifiers.add(Modifier.PROTECTED);
        assertFalse(isDefault(modifiers));
    }

    /**
     * @throws Exception
     */
    public void testToStringOfModifiers() throws Exception {
        final Set<Modifier> modifiers = new HashSet<Modifier>();
        assertEquals("", toStringOfModifiers(modifiers));

        modifiers.add(Modifier.STATIC);
        assertEquals("static ", toStringOfModifiers(modifiers));

        modifiers.add(Modifier.FINAL);
        assertEquals("static final ", toStringOfModifiers(modifiers));

        modifiers.add(Modifier.PROTECTED);
        assertEquals("protected static final ", toStringOfModifiers(modifiers));
    }

}
