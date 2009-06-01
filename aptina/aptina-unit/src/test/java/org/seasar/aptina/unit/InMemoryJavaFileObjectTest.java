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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;

import javax.tools.JavaFileObject.Kind;

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class InMemoryJavaFileObjectTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        final InMemoryJavaFileObject fileObject = new InMemoryJavaFileObject(
                new URI("/foo"), Kind.SOURCE, null);
        final OutputStream os = fileObject.openOutputStream();
        final PrintWriter pw = new PrintWriter(os);
        pw.print("hoge hoge");
        pw.close();

        final InputStream is = fileObject.openInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                is));
        assertEquals("hoge hoge", reader.readLine());
    }

}
