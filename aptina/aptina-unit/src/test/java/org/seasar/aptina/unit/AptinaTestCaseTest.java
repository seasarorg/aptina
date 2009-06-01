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

import javax.lang.model.element.TypeElement;

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
        assertNull(getTypeElement("Hoge"));
        assertNotNull(getTypeMirror(TestSource.class));
        assertNull(getTypeMirror("Hoge"));
        assertTrue(processor.called);
        assertEqualsGeneratedSource("package foo.bar;public class Baz {}",
                "foo.bar.Baz");
    }

    /**
     * @throws Exception
     */
    public void testGetFieldElement() throws Exception {
        addCompilationUnit(TestSource.class);

        compile();

        final TypeElement typeElement = getTypeElement(TestSource.class);
        assertNotNull(typeElement);
        assertNotNull(getFieldElement(typeElement, "aaa"));
        assertNotNull(getFieldElement(typeElement, "bbb"));
        assertNull(getFieldElement(typeElement, "zzz"));
    }

    /**
     * @throws Exception
     */
    public void testGetConstructorElement() throws Exception {
        addCompilationUnit(TestSource.class);

        compile();

        final TypeElement typeElement = getTypeElement(TestSource.class);
        assertNotNull(typeElement);

        assertNotNull(getConstructorElement(typeElement));

        assertNotNull(getConstructorElement(typeElement, int.class));
        assertNotNull(getConstructorElement(typeElement, String[].class));
        assertNull(getConstructorElement(typeElement, TestSource.class));

        assertNotNull(getConstructorElement(typeElement, "int"));
        assertNotNull(getConstructorElement(typeElement, "java.lang.String[]"));
        assertNull(getConstructorElement(typeElement, "java.lang.Object"));
    }

    /**
     * @throws Exception
     */
    public void testGetMethodElement() throws Exception {
        addCompilationUnit(TestSource.class);

        compile();

        final TypeElement typeElement = getTypeElement(TestSource.class);
        assertNotNull(typeElement);

        assertNotNull(getMethodElement(typeElement, "hoge"));
        assertNull(getMethodElement(typeElement, "unknown"));

        assertNotNull(getMethodElement(typeElement, "setAaa", int.class));
        assertNotNull(getMethodElement(typeElement, "setBbb", String[].class));
        assertNull(getMethodElement(typeElement, "unknown", TestSource.class));

        assertNotNull(getMethodElement(typeElement, "setAaa", "int"));
        assertNotNull(getMethodElement(typeElement, "setBbb",
                "java.lang.String[]"));
        assertNull(getMethodElement(typeElement, "unknown", "java.lang.Object"));
    }

    /**
     * @throws Exception
     */
    public void testGetTypeMirror() throws Exception {
        final TestProcessor processor = new TestProcessor();
        addProcessor(processor);
        addCompilationUnit(TestSource.class);

        compile();
        assertNotNull(getTypeMirror(boolean.class));
        assertNotNull(getTypeMirror(int[].class));
        assertNotNull(getTypeMirror(String[][].class));

        assertNotNull(getTypeMirror("boolean"));
        assertNotNull(getTypeMirror("java.lang.String[]"));
        assertNotNull(getTypeMirror(String[][].class.getName()));
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
