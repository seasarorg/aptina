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
package org.seasar.aptina.commons.message;

import java.util.Locale;
import java.util.ResourceBundle;

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class EnumMessageResourceBundleTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetBundle() throws Exception {
        final ResourceBundle bundle = EnumMessageResourceBundle.getBundle(
            TestMessageCode.class,
            Locale.JAPANESE);
        assertNotNull(bundle);
        assertEquals(EnumMessageResourceBundle.class, bundle.getClass());
        assertEquals(1, EnumMessageResourceBundle.class.cast(bundle).locale);
    }

    /**
     * @throws Exception
     */
    public void testGetBundleNoFallback() throws Exception {
        final ResourceBundle bundle = EnumMessageResourceBundle
            .getBundleNoFallback(TestMessageCode.class, Locale.ENGLISH);
        assertNotNull(bundle);
        assertEquals(EnumMessageResourceBundle.class, bundle.getClass());
        assertEquals(0, EnumMessageResourceBundle.class.cast(bundle).locale);
    }

}
