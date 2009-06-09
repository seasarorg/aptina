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

import javax.tools.Diagnostic.Kind;

/**
 * メッセージコードを定義した列挙のインタフェースです．
 * <p>
 * メッセージコードは {@link Kind} とロケールごとのメッセージを持ちます． メッセージのロケールは
 * {@link #SUPORTED_LOCALE} と同じ並びで順序づけられます． この順序は {@link #getMessageFormat(int)}
 * の引数として使われます．
 * </p>
 * 
 * @author koichik
 */
public interface EnumMessageCode {

    /**
     * サポートするロケールの配列．
     * <p>
     * 配列の並びは次の通りです．
     * </p>
     * <ol>
     * <li>{@link Locale#ROOT}</li>
     * <li>{@link Locale#JAPANESE}</li>
     * </ol>
     * <p>
     * この並びに対応したロケールのインデックスが {@link #getMessageFormat(int)} の引数として渡されます．
     * </p>
     */
    Locale[] SUPORTED_LOCALE = new Locale[] { Locale.ROOT, Locale.JAPANESE };

    /**
     * 診断の種類を返します．
     * 
     * @return 診断の種類
     */
    Kind getKind();

    /**
     * 指定されたロケールのメッセージを返します．
     * 
     * @param locale
     *            ロケールのインデックス
     * @return 指定されたロケールのメッセージ
     */
    String getMessageFormat(final int locale);

}
