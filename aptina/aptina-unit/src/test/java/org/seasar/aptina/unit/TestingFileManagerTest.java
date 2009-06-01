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

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject.Kind;

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class TestingFileManagerTest extends TestCase {

    TestingJavaFileManager testingFileManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        final StandardJavaFileManager standardFileManager = compiler
                .getStandardFileManager(diagnostics, null, null);
        testingFileManager = new TestingJavaFileManager(standardFileManager,
                null);
    }

    /**
     * @throws Exception
     */
    public void testFileObject() throws Exception {
        final FileObject fo = testingFileManager.getFileForOutput(
                StandardLocation.CLASS_OUTPUT, "hoge", "foo/bar", null);
        assertNotNull(fo);
        assertSame(fo, testingFileManager.getFileForInput(
                StandardLocation.CLASS_OUTPUT, "hoge", "foo/bar"));
    }

    /**
     * @throws Exception
     */
    public void testJavaFileObject() throws Exception {
        final JavaFileObject jfo = testingFileManager.getJavaFileForOutput(
                StandardLocation.SOURCE_OUTPUT, "foo.bar.Hoge", Kind.SOURCE,
                null);
        assertNotNull(jfo);
        assertSame(jfo, testingFileManager.getJavaFileForInput(
                StandardLocation.SOURCE_OUTPUT, "foo.bar.Hoge", Kind.SOURCE));
    }

}
