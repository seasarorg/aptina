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
package org.seasar.aptina.commons.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import junit.framework.TestCase;

import static org.seasar.aptina.commons.util.IOUtils.*;

/**
 * 
 * @author koichik
 */
public class IOUtilsTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testCloseSilently() throws Exception {
        final StringReader reader = new StringReader("");
        closeSilently(reader);
        try {
            reader.read();
            fail();
        } catch (final IOException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testReadString() throws Exception {
        assertEquals("あいうえおかきくけこさしすせそたちつてとなにぬねの", readString(getTestFile(),
                Charset.forName("UTF-8")));
    }

    private File getTestFile() {
        return new File(getTestResourcesDir(), getClass().getSimpleName() + "_"
                + getName() + ".txt");
    }

    private File getTestResourcesDir() {
        File dir = new File("aptina-commons/src/test/resources");
        if (!dir.exists()) {
            dir = new File("src/test/resources");
        }
        return dir;
    }

}
