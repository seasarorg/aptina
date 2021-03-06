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

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class EnumMessageTextFormatterTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        final EnumMessageTextFormatter<TestTextMessageCode> formatter = new EnumMessageTextFormatter<TestTextMessageCode>(
            TestTextMessageCode.class,
            Locale.JAPANESE);
        assertEquals("あああ", formatter.getMessage(TestTextMessageCode.FOO));
        assertEquals("bbbのaaa", formatter.getMessage(
            TestTextMessageCode.BAR,
            "aaa",
            "bbb"));
    }

}
