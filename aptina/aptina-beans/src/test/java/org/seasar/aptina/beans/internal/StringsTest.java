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
package org.seasar.aptina.beans.internal;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class StringsTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testJoin() throws Exception {
        assertEquals("", Strings.join(new ArrayList<String>(), ", "));
        assertEquals("aaa, bbb, ccc", Strings.join(Arrays.asList("aaa", "bbb",
                "ccc"), ", "));
    }

    /**
     * @throws Exception
     */
    public void testJoin2() throws Exception {
        assertEquals("", Strings.join(new ArrayList<String>(), " ",
                new ArrayList<String>(), ", "));
        assertEquals("aaa 111, bbb 222, ccc 333", Strings.join(Arrays.asList(
                "aaa", "bbb", "ccc"), " ", Arrays.asList("111", "222", "333"),
                ", "));
    }

    /**
     * @throws Exception
     */
    public void testCapitalize() throws Exception {
        assertEquals(null, Strings.capitalize(null));
        assertEquals("", Strings.capitalize(""));
        assertEquals("A", Strings.capitalize("a"));
        assertEquals("Aa", Strings.capitalize("aa"));
        assertEquals("Foo", Strings.capitalize("foo"));
    }

    /**
     * @throws Exception
     */
    public void testDecapitalize() throws Exception {
        assertEquals(null, Strings.decapitalize(null));
        assertEquals("", Strings.decapitalize(""));
        assertEquals("a", Strings.decapitalize("a"));
        assertEquals("a", Strings.decapitalize("A"));
        assertEquals("AA", Strings.decapitalize("AA"));
        assertEquals("foo", Strings.decapitalize("Foo"));
        assertEquals("FOO", Strings.decapitalize("FOO"));
    }

}
