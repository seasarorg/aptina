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
package org.seasar.aptina.unit;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 *
 * @author koichik
 */
public class AptinaTestCaseTest extends AptinaTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setLocale(Locale.JAPANESE);
        setCharset(Charset.forName("UTF-8"));
        addSourcePath("aptina-unit/src/test/java", "src/test/java");
    }

    /**
     * @throws Exception
     */
    public void testFile() throws Exception {
        final TestProcessor processor = new TestProcessor();
        addProcessor(processor);
        addCompilationUnit(TestSource.class);

        compile();

        assertTrue(getCompiledResult());
        assertFalse(getDiagnostics().isEmpty());
        assertNotNull(getProcessingEnvironment());
        assertNotNull(getTypeElement(TestSource.class));
        assertNotNull(getTypeMirror(TestSource.class));
        assertTrue(processor.called);
        assertEqualsGeneratedSource("package foo.bar;public class Baz {}",
                "foo.bar.Baz");
    }

    /**
     * @throws Exception
     */
    public void testInMemory() throws Exception {
        final TestProcessor processor = new TestProcessor();
        addProcessor(processor);
        addCompilationUnit("Foo",
                "@org.seasar.aptina.unit.Hoge public class Foo {}");

        compile();

        assertTrue(getCompiledResult());
        assertFalse(getDiagnostics().isEmpty());
        assertNotNull(getProcessingEnvironment());
        assertNotNull(getTypeElement(TestSource.class));
        assertNotNull(getTypeMirror(TestSource.class));
        assertTrue(processor.called);
        assertEqualsGeneratedSource("package foo.bar;public class Baz {}",
                "foo.bar.Baz");
    }

    /**
     * @throws Exception
     */
    public void testReadFromFile() throws Exception {
        File f = new File("aptina-unit/src/test/resources/a.txt");
        if (!f.exists()) {
            f = new File("src/test/resources/a.txt");
        }
        assertEquals("abc", readFromFile(f));

    }

    /**
     * @throws Exception
     */
    public void testReadFromResource() throws Exception {
        assertEquals("abc", readFromResource(Thread.currentThread()
                .getContextClassLoader().getResource("a.txt")));
    }

}
