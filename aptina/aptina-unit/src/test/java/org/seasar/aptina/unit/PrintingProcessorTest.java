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

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * 
 * @author koichik
 */
public class PrintingProcessorTest extends AptinaTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setLocale(Locale.JAPANESE);
        setCharset(Charset.forName("UTF-8"));
        addSourcePath("aptina-unit/src/main/java", "src/main/java");
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        addProcessor(new PrintingProcessor());
        addCompilationUnit(PrintingProcessor.class);
        compile();
    }

}
