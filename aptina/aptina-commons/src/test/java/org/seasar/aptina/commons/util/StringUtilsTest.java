/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.util.ArrayList;

import junit.framework.TestCase;

import static java.util.Arrays.*;

import static org.seasar.aptina.commons.util.StringUtils.*;

/**
 * 
 * @author koichik
 */
public class StringUtilsTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testCapitalize() throws Exception {
        assertEquals(null, capitalize(null));
        assertEquals("", capitalize(""));
        assertEquals("A", capitalize("a"));
        assertEquals("Aa", capitalize("aa"));
        assertEquals("Foo", capitalize("foo"));
    }

    /**
     * @throws Exception
     */
    public void testDecapitalize() throws Exception {
        assertEquals(null, decapitalize(null));
        assertEquals("", decapitalize(""));
        assertEquals("a", decapitalize("a"));
        assertEquals("a", decapitalize("A"));
        assertEquals("AA", decapitalize("AA"));
        assertEquals("foo", decapitalize("Foo"));
        assertEquals("FOO", decapitalize("FOO"));
    }

    /**
     * @throws Exception
     */
    public void testJoin() throws Exception {
        assertEquals("", join(new ArrayList<String>(), ", "));
        assertEquals("aaa, bbb, ccc", join(asList("aaa", "bbb",
                "ccc"), ", "));
    }

    /**
     * @throws Exception
     */
    public void testJoin2() throws Exception {
        assertEquals("", join(new ArrayList<String>(),
                new ArrayList<String>(), ":", ", "));
        assertEquals("aaa:111, bbb:222, ccc:333", join(asList(
                "aaa", "bbb", "ccc"), asList("111", "222", "333"), ":", ", "));
        try {
            join(asList("a", "b"), asList("1"), ":", ", ");
            fail();
        } catch (final IllegalArgumentException expected) {
            assertEquals("elements1 > elements2", expected.getMessage());
        }
        try {
            join(asList("a"), asList("1", "2"), ":", ", ");
            fail();
        } catch (final IllegalArgumentException expected) {
            assertEquals("elements1 < elements2", expected.getMessage());
        }
    }

}
