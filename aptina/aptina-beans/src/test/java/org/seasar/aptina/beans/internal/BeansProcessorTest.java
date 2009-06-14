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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.seasar.aptina.beans.example.BarBeanState;
import org.seasar.aptina.beans.example.BoundAndConstrainedBeanState;
import org.seasar.aptina.beans.example.BoundBeanState;
import org.seasar.aptina.beans.example.ConstrainedBeanState;
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
        assertGeneratedSource("org.seasar.aptina.beans.example.FooBean");
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
        assertGeneratedSource("org.seasar.aptina.beans.example.BarBean");
    }

    /**
     * @throws Exception
     */
    public void testBound() throws Exception {
        final BeansProcessor beansProcessor = new BeansProcessor();
        addProcessor(beansProcessor);
        addCompilationUnit(BoundBeanState.class);
        compile();
        assertTrue(getCompiledResult());
        assertGeneratedSource("org.seasar.aptina.beans.example.BoundBean");
    }

    /**
     * @throws Exception
     */
    public void testConstrained() throws Exception {
        final BeansProcessor beansProcessor = new BeansProcessor();
        addProcessor(beansProcessor);
        addCompilationUnit(ConstrainedBeanState.class);
        compile();
        assertTrue(getCompiledResult());
        assertGeneratedSource("org.seasar.aptina.beans.example.ConstrainedBean");
    }

    /**
     * @throws Exception
     */
    public void testBoundAndConstrained() throws Exception {
        final BeansProcessor beansProcessor = new BeansProcessor();
        addProcessor(beansProcessor);
        addCompilationUnit(BoundAndConstrainedBeanState.class);
        compile();
        assertTrue(getCompiledResult());
        assertGeneratedSource("org.seasar.aptina.beans.example.BoundAndConstrainedBean");
    }

    void assertGeneratedSource(final String generatedClassName)
            throws IOException {
        final String expectedResourceName = getClass().getSimpleName() + "/"
                + getName() + ".txt";
        System.out.println(getGeneratedSource(generatedClassName));
        assertEqualsGeneratedSourceWithResource(expectedResourceName,
                generatedClassName);
    }

}
