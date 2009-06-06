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

import java.nio.charset.Charset;
import java.util.Locale;

import org.seasar.aptina.beans.example.BarBeanState;
import org.seasar.aptina.beans.example.FooBeanState;
import org.seasar.aptina.unit.AptinaTestCase;

/**
 * 
 * @author koichik
 */
public class BeansProcessorTest extends AptinaTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setLocale(Locale.JAPANESE);
        setCharset(Charset.forName("UTF-8"));
        addSourcePath("aptina-beans/src/test/java", "src/test/java");
    }

    /**
     * @throws Exception
     */
    public void testFoo() throws Exception {
        final BeansProcessor beansProcessor = new BeansProcessor();
        addProcessor(beansProcessor);
        addCompilationUnit(FooBeanState.class);
        compile();
        assertTrue(getCompiledResult());
        System.out
                .println(getGeneratedSource("org.seasar.aptina.beans.example.FooBean"));
        assertEqualsGeneratedSourceWithResource("FooBean.txt",
                "org.seasar.aptina.beans.example.FooBean");
    }

    /**
     * @throws Exception
     */
    public void testBar() throws Exception {
        final BeansProcessor beansProcessor = new BeansProcessor();
        addProcessor(beansProcessor);
        addCompilationUnit(BarBeanState.class);
        compile();
        assertTrue(getCompiledResult());
        System.out
                .println(getGeneratedSource("org.seasar.aptina.beans.example.BarBean"));
        assertEqualsGeneratedSourceWithResource("BarBean.txt",
                "org.seasar.aptina.beans.example.BarBean");
    }

}
