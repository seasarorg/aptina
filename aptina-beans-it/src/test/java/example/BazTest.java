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
package example;

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class BazTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        final BazBean<String> bazBean = new BazBean<String>();
        bazBean.setBbb("hoge");
        assertEquals("hoge", bazBean.getBbb());

    }
}
