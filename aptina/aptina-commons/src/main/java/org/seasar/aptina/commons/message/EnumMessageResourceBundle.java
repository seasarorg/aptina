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
package org.seasar.aptina.commons.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * メッセージコードを定義した列挙を使用するリソースバンドルです．
 * 
 * @author koichik
 * @param <T>
 *            メッセージコードを定義した列挙の型
 */
public class EnumMessageResourceBundle<T extends Enum<T> & MessageCodeEnum>
        extends ResourceBundle {

    /** メッセージコードを定義した列挙の型 */
    protected Class<T> messageCodeEnumClass;

    /** ロケール */
    protected int locale;

    /**
     * インスタンスを構築します．
     * 
     * @param messageCodeEnumClass
     *            メッセージコードを定義した列挙の型
     * @param locale
     *            ロケール
     */
    public EnumMessageResourceBundle(final Class<T> messageCodeEnumClass,
            final int locale) {
        this.messageCodeEnumClass = messageCodeEnumClass;
        this.locale = locale;
    }

    @Override
    public Enumeration<String> getKeys() {
        final Enum<T>[] codes = messageCodeEnumClass.getEnumConstants();
        final List<String> keys = new ArrayList<String>(codes.length);
        for (final Enum<T> code : codes) {
            keys.add(MessageCodeEnum.class.cast(code).getMessageFormat(locale));
        }
        return Collections.enumeration(keys);
    }

    @Override
    protected Object handleGetObject(final String key) {
        final Enum<T> code = Enum.valueOf(messageCodeEnumClass, key);
        return MessageCodeEnum.class.cast(code).getMessageFormat(locale);
    }

}
