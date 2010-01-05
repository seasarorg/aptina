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
package org.seasar.aptina.beans.example;

import org.seasar.aptina.beans.BeanState;

/**
 * 
 * @author koichik
 */
@BeanState
public class BazBeanState {

    int a;

    boolean b;

    String c;

    String d;

    String e;

    String f;

    /**
     * @return
     */
    public boolean isB() {
        return b;
    }

    /**
     * @return
     */
    public String getC() {
        return c;
    }

    /**
     * @param d
     */
    public void setD(final String d) {
        this.d = d;
    }

    /**
     * @return
     */
    public String getE() {
        return e;
    }

    /**
     * @param e
     */
    public void setE(final String e) {
        this.e = e;
    }

    /**
     * @param f
     */
    public void setF(final Object f) {
        this.f = f.toString();
    }

}
