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
package org.seasar.aptina.commons.message;

import java.util.Locale;

import javax.tools.Diagnostic.Kind;

/**
 * 
 * @author koichik
 */
public enum TestTextMessageCode implements EnumMessageCode {
    /** */
    FOO(Kind.WARNING, "aaa", "あああ"),
    /** */
    BAR(Kind.ERROR, "{0} of {1}", "{1}の{0}");

    /** サポートするロケールの配列 */
    public static final Locale[] SUPPORTED_LOCALES = new Locale[] {
            Locale.ROOT, Locale.JAPANESE };

    /** 診断の種類 */
    private final Kind kind;

    /** メッセージフォーマット */
    private final String[] messageFormats;

    /**
     * インスタンスを構築します．
     * <p>
     * 配列の要素はロケールごとのメッセージフォーマットです．
     * </p>
     * 
     * @param messageFormats
     *            メッセージフォーマットの配列
     */
    private TestTextMessageCode(final Kind kind, final String... messageFormats) {
        this.kind = kind;
        this.messageFormats = messageFormats;
    }

    /**
     * 診断の種類を返します．
     * 
     * @return 診断の種類
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * 指定されたロケールのメッセージフォーマットを返します．
     * 
     * @param locale
     *            ロケール
     * @return 指定されたロケールのメッセージフォーマット
     */
    public String getMessageFormat(final int locale) {
        return messageFormats[locale];
    }

}
