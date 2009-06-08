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

import junit.framework.TestCase;
import static org.seasar.aptina.commons.util.ClassUtils.*;

/**
 * 
 * @author koichik
 */
public class ClassUtilsTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetQualifiedNameOfParam2() throws Exception {
        assertEquals(getClass().getName(), getQualifiedName(
                "org.seasar.aptina.commons.util", getClass().getSimpleName()));
    }

    /**
     * @throws Exception
     */
    public void testGetPackageName() throws Exception {
        assertEquals("org.seasar.aptina.commons.util",
                getPackageName(getClass().getName()));
    }

    /**
     * @throws Exception
     */
    public void testGetQualifiedName() throws Exception {
        assertNull(getQualifiedName(null));
        assertEquals("java.lang.String", getQualifiedName(String.class));
        assertEquals("java.lang.String[]", getQualifiedName(String[].class));
        assertEquals("int", getQualifiedName(int.class));
        assertEquals("int[]", getQualifiedName(int[].class));
    }

    /**
     * @throws Exception
     */
    public void testGetQualifiedNameArray() throws Exception {
        final String[] result = getQualifiedNameArray(String.class,
                String[].class, int.class, int[].class);
        assertEquals(4, result.length);
        assertEquals("java.lang.String", result[0]);
        assertEquals("java.lang.String[]", result[1]);
        assertEquals("int", result[2]);
        assertEquals("int[]", result[3]);
    }

    /**
     * @throws Exception
     */
    public void testGetSimpleName() throws Exception {
        assertNull(getSimpleName(null));
        assertEquals("String", getSimpleName(String.class));
        assertEquals("String[]", getSimpleName(String[].class));
        assertEquals("int", getSimpleName(int.class));
        assertEquals("int[]", getSimpleName(int[].class));
    }

    /**
     * @throws Exception
     */
    public void testGetSimpleNameArray() throws Exception {
        final String[] result = getSimpleNameArray(String.class,
                String[].class, int.class, int[].class);
        assertEquals(4, result.length);
        assertEquals("String", result[0]);
        assertEquals("String[]", result[1]);
        assertEquals("int", result[2]);
        assertEquals("int[]", result[3]);
    }

}
