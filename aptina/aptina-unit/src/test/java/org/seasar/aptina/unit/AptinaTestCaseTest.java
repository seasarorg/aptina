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
package org.seasar.aptina.unit;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

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
        assertEqualsGeneratedSource(
            "package foo.bar;public class Baz {}",
            "foo.bar.Baz");
    }

    /**
     * @throws Exception
     */
    public void testInMemory() throws Exception {
        final TestProcessor processor = new TestProcessor();
        addProcessor(processor);
        addCompilationUnit(
            "Foo",
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
        assertEqualsGeneratedSource(
            "package foo.bar;public class Baz {}",
            "foo.bar.Baz");
    }

    /**
     * @throws Exception
     */
    public void testGetDiagnostics() throws Exception {
        addProcessor(new TestDiagnosticsProcessor());
        addCompilationUnit(TestSource.class);

        compile();

        List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
        assertNotNull(diagnostics);
        assertEquals(3, diagnostics.size());
        assertTrue(diagnostics.get(0).getMessage(null).endsWith("hoge"));
        assertTrue(diagnostics.get(1).getMessage(null).endsWith("foo"));
        assertTrue(diagnostics.get(2).getMessage(null).endsWith("bar"));

        diagnostics = getDiagnostics(TestSource.class);
        assertNotNull(diagnostics);
        assertEquals(2, diagnostics.size());
        assertTrue(diagnostics.get(0).getMessage(null).endsWith("foo"));
        assertTrue(diagnostics.get(1).getMessage(null).endsWith("bar"));

        diagnostics = getDiagnostics(Kind.NOTE);
        assertNotNull(diagnostics);
        assertEquals(1, diagnostics.size());
        assertTrue(diagnostics.get(0).getMessage(null).endsWith("hoge"));

        diagnostics = getDiagnostics(TestSource.class, Kind.ERROR);
        assertNotNull(diagnostics);
        assertEquals(1, diagnostics.size());
        assertTrue(diagnostics.get(0).getMessage(null).endsWith("foo"));
    }

    /**
     * @throws Exception
     */
    public void testGetFieldElement() throws Exception {
        addCompilationUnit(TestSource.class);

        compile();

        final TypeElement typeElement = getTypeElement(TestSource.class);
        assertNotNull(typeElement);
        assertEquals("aaa", getFieldElement(typeElement, "aaa")
            .getSimpleName()
            .toString());
        assertEquals("bbb", getFieldElement(typeElement, "bbb")
            .getSimpleName()
            .toString());
        assertEquals("ccc", getFieldElement(typeElement, "ccc")
            .getSimpleName()
            .toString());
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
        assertNotNull(getConstructorElement(typeElement, "java.util.List<T>"));
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

        assertEquals("hoge", getMethodElement(typeElement, "hoge")
            .getSimpleName()
            .toString());
        assertNull(getMethodElement(typeElement, "unknown"));

        assertEquals("setAaa", getMethodElement(
            typeElement,
            "setAaa",
            int.class).getSimpleName().toString());
        assertEquals("setBbb", getMethodElement(
            typeElement,
            "setBbb",
            String[].class).getSimpleName().toString());
        assertNull(getMethodElement(typeElement, "unknown", TestSource.class));

        assertEquals("setAaa", getMethodElement(typeElement, "setAaa", "int")
            .getSimpleName()
            .toString());
        assertEquals("setBbb", getMethodElement(
            typeElement,
            "setBbb",
            "java.lang.String[]").getSimpleName().toString());
        assertEquals("setCcc", getMethodElement(
            typeElement,
            "setCcc",
            "java.util.List<T>").getSimpleName().toString());
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
        assertEquals("boolean", getTypeMirror(boolean.class).toString());
        assertEquals("int[]", getTypeMirror(int[].class).toString());
        assertEquals("java.lang.String[][]", getTypeMirror(String[][].class)
            .toString());

        assertEquals("boolean", getTypeMirror("boolean").toString());
        assertEquals("java.lang.String[]", getTypeMirror("java.lang.String[]")
            .toString());
        assertEquals("java.lang.String[][][]", getTypeMirror(
            String[][][].class.getName()).toString());
    }

    /**
     * @throws Exception
     */
    public void testReadFromResource() throws Exception {
        final BufferedReader reader = new BufferedReader(new StringReader(
            readFromResource(Thread
                .currentThread()
                .getContextClassLoader()
                .getResource("a.txt"))));
        assertEquals("abc", reader.readLine());
        assertEquals("あいう", reader.readLine());
    }

}
