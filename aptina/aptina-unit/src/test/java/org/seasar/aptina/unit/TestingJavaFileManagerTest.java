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

import java.io.File;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject.Kind;

import junit.framework.TestCase;

import org.seasar.aptina.commons.util.IOUtils;

import static java.util.Arrays.*;

/**
 * 
 * @author koichik
 */
public class TestingJavaFileManagerTest extends TestCase {

    TestingJavaFileManager testingFileManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        final StandardJavaFileManager standardFileManager = compiler
            .getStandardFileManager(diagnostics, null, null);
        standardFileManager.setLocation(StandardLocation.SOURCE_PATH, asList(
            new File("aptina-unit/src/test/resources"),
            new File("src/test/resources")));
        testingFileManager = new TestingJavaFileManager(
            standardFileManager,
            null);
    }

    /**
     * @throws Exception
     */
    public void testFileObject() throws Exception {
        final FileObject fo = testingFileManager.getFileForOutput(
            StandardLocation.SOURCE_PATH,
            "hoge",
            "foo/bar",
            null);
        assertNotNull(fo);
        assertSame(fo, testingFileManager.getFileForInput(
            StandardLocation.SOURCE_PATH,
            "hoge",
            "foo/bar"));
    }

    /**
     * @throws Exception
     */
    public void testFileObjectFromClassOutput() throws Exception {
        final FileObject fo = testingFileManager.getFileForOutput(
            StandardLocation.CLASS_OUTPUT,
            "",
            "a.txt",
            null);
        assertNotNull(fo);
        assertEquals("abc\r\nあいう\r\n", IOUtils.readString(
            fo.openInputStream(),
            "UTF-8"));
    }

    /**
     * @throws Exception
     */
    public void testJavaFileObject() throws Exception {
        final JavaFileObject jfo = testingFileManager.getJavaFileForOutput(
            StandardLocation.SOURCE_OUTPUT,
            "foo.bar.Hoge",
            Kind.SOURCE,
            null);
        assertNotNull(jfo);
        assertSame(jfo, testingFileManager.getJavaFileForInput(
            StandardLocation.SOURCE_OUTPUT,
            "foo.bar.Hoge",
            Kind.SOURCE));
    }

}
