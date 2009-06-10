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
/**
 * プロパティファイルの代わりに列挙に記述されたメッセージを使用する 
 * {@link java.util.ResourceBundle} を提供します．
 * <p>
 * メッセージを記述した列挙は，
 * {@link org.seasar.aptina.commons.message.EnumMessageCode} を実装し，
 * {@link org.seasar.aptina.commons.message.EnumMessageCode#SUPORTED_LOCALE} に
 * 定義されたロケールの配列と同じ数・並びでロケールごとのメッセージをサポートしなくてはなりません．
 * また， 診断レベルとして {@link javax.tools.Diagnostic.Kind} を持たなくてはなりません．
 * メッセージを定義した列挙は次のようになります．
 * </p>
 * <pre>
 * public enum XxxMessageCode implements EnumMessageCode {
 *     WARN(Kind.WARNING, "warning", "警告"),
 *     ERROR(Kind.ERROR, "error", "エラー"),
 *     ...
 * 
 *     private final Kind kind;
 *     private final String[] messageFormats;
 * 
 *     private XxxMessageCode(Kind kind, String... messageFormats) {
 *         this.kind = kind;
 *         this.messageFormats = messageFormats;
 *     }
 * 
 *     public Kind getKind() {
 *         return kind;
 *     }
 * 
 *     public String getMessageFormat(final int locale) {
 *         return messageFormats[locale];
 *     }
 * }
 * </pre>
 * <p>
 * 列挙を指定して {@link java.util.ResourceBundle} を取得するには，
 * {@link org.seasar.aptina.commons.message.EnumMessageResourceBundle} の
 * {@literal static} メソッドを使用します．
 * </p>
 * <pre>
 * ResourceBundle bundle = EnumMessageResourceBundle.getBundle(XxxMessageCode.class);
 * </pre>
 * <p>
 * 列挙の文字列表現をキーとしてメッセージを取得することができます．
 * </p>
 * <pre>
 * String message = bundle.getString(WARN.name());
 * </pre>
 * <p>
 * 列挙のメッセージとして {@link java.util.Formatter} のパターンを定義した場合は
 * {@link org.seasar.aptina.commons.message.EnumMessageFormatter} を使用することができます．
 * </p>
 * <pre>
 * public enum YyyMessageCode implements EnumMessageCode {
 *     E0000(Kind.ERROR, "Exception occurred : %1$s%n", "例外が発生しました : %1$s%n"),
 *     ...
 * </pre>
 * <pre>
 * EnumMessageFormatter&lt;YyyMessageCode&gt; formatter = 
 *     new EnumMessageFormatter&lt;TestMessageCode&gt;(YyyMessageCode.class);
 * String message = formatter.getMessage(E0000, e);
 * </pre>
 * <p>
 * または， 
 * {@link org.seasar.aptina.commons.message.EnumMessageFormatter}
 * の出力先を指定してインスタンス化して使うこともできます．
 * </p>
 * <pre>
 * StringBuilder builder = new StringBuilder();
 * EnumMessageFormatter&lt;YyyMessageCode&gt; formatter = 
 *     new EnumMessageFormatter&lt;TestMessageCode&gt;(YyyMessageCode.class, builder);
 * formatter.format(E0000, e); // builder にフォーマットされた文字列が追加される
 * </pre>
 */
package org.seasar.aptina.commons.message;

