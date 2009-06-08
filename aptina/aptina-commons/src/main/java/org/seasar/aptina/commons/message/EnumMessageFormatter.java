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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * メッセージコードを定義した列挙が持つフォーマット文字列を使用してメッセージを組み立てるクラスです．
 * <p>
 * メッセージコードを定義した列挙は {@link EnumMessageCode} を実装していなければなりません．
 * </p>
 * 
 * @author koichik
 * @param <T>
 *            メッセージコードを定義した列挙の型
 */
public class EnumMessageFormatter<T extends Enum<T> & EnumMessageCode> {

    /** リソースバンドル */
    protected final ResourceBundle bundle;

    /**
     * デフォルトロケールでインスタンスを構築します．
     * 
     * @param enumClass
     *            メッセージコードを定義した列挙の型
     */
    public EnumMessageFormatter(final Class<T> enumClass) {
        bundle = EnumMessageResourceBundle.getBundle(enumClass);
    }

    /**
     * ロケールを指定してインスタンスを構築します．
     * 
     * @param enumClass
     *            メッセージコードを定義した列挙の型
     * @param locale
     *            ロケール
     */
    public EnumMessageFormatter(final Class<T> enumClass, final Locale locale) {
        bundle = EnumMessageResourceBundle.getBundle(enumClass, locale);
    }

    /**
     * メッセージを作成して返します．
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数
     * @return メッセージ
     */
    public String getMessage(final T messageCode, final Object... args) {
        final String pattern = bundle.getString(messageCode.name());
        return String.format(pattern, args);
    }

}
