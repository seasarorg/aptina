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
 * JavaBeans を生成するためのフィールドを定義した状態クラスに付与する注釈型や列挙型を提供します．
 * <p>
 * Aptina Beans の提供する Annotation Processor 
 * ({@link org.seasar.aptina.beans.internal.BeansProcessor}) は，
 * {@link org.seasar.aptina.beans.BeanState} で注釈されたクラス (以下「状態クラス」) からサブクラス 
 * (以下「Bean クラス」) を生成します．
 * Bean クラスは，状態クラスに定義されたフィールドに対する getter/setter メソッドを持つ JavaBeans です．
 * </p>
 * <h3>状態クラス</h3>
 * <p>
 * {@link org.seasar.aptina.beans.BeanState} で注釈された状態クラスは次のようなクラスでなければなりません．
 * </p>
 * <ul>
 * <li>通常のクラスであること (インタフェースやアノテーション，列挙は状態クラスにできません)．</li>
 * <li>トップレベルのクラスであること (ネストしたクラスは状態クラスにできません)．</li>
 * <li>{@literal public} なクラスであること．</li>
 * <li>{@literal final} クラスではないこと．</li>
 * </ul>
 * <h3>Bean クラス</h3>
 * <p>
 * 状態クラスから生成される Bean クラスは {@link org.seasar.aptina.beans.JavaBean} で注釈され， 
 * 状態クラスと同じパッケージに生成されます．
 * Bean クラスの名前は次のようになります．
 * </p>
 * <dl>
 * <dt>状態クラスの名前が <code>Abstract</code> で始まっている場合</dt>
 * <dd>状態クラスの名前の先頭から <code>Abstract</code> を除去した名前になります．</dd>
 * <dt>状態クラスの名前が <code>State</code> で終わっている場合</dt>
 * <dd>状態クラスの名前の末尾から <code>State</code> を除去した名前になります．</dd>
 * <dt>状態クラスの名前が <code>Bean</code> で終わっている場合</dt>
 * <dd>状態クラスの名前の末尾に <code>Impl</code> を付加した名前になります．</dd>
 * <dt>その他の場合</dt>
 * <dd>状態クラスの名前の末尾に <code>Bean</code> を付加した名前になります．</dd>
 * </dl>
 * <p>
 * 例
 * </p>
 * <table border="1">
 * <tr>
 * <th>状態クラスの名前</th>
 * <th>生成される Bean クラスの名前</th>
 * </tr>
 * <tr>
 * <td><code>AbstractHogeBean</code></td>
 * <td><code>HogeBean</code></td>
 * </tr>
 * <tr>
 * <td><code>HogeBeanState</code></td>
 * <td><code>HogeBean</code></td>
 * </tr>
 * <tr>
 * <td><code>HogeBean</code></td>
 * <td><code>HogeBeanImpl</code></td>
 * </tr>
 * <tr>
 * <td><code>Hoge</code></td>
 * <td><code>HogeBean</code></td>
 * </tr>
 * </table>
 * <h3>プロパティ</h3>
 * <p>
 * 状態クラスの次の条件を満たすフィールドが Bean クラスのプロパティとなります．
 * </p>
 * <ul>
 * <li>{@literal static}，{@literal private}，{@literal public} の修飾子が付けられていないこと．</li>
 * <li>{@link org.seasar.aptina.beans.Property} アノテーションで 
 * {@link org.seasar.aptina.beans.AccessType#NONE} が指定されていないこと．</li>
 * </ul>
 * <p>
 * 状態クラスのプロパティが配列型の場合， Bean クラスにはインデックス付きの getter/setter メソッドが生成されます．
 * </p>
 * <p>
 * 状態クラスのフィールドに
 * {@link org.seasar.aptina.beans.Property}
 * アノテーションを付与し，
 * {@link org.seasar.aptina.beans.Property#access}
 * 要素で getter/setter をどのように生成するか指定することができます．
 * {@link org.seasar.aptina.beans.Property#access}
 * 要素の型は
 * {@link org.seasar.aptina.beans.AccessType}
 * です．
 * </p>
 * <table border="1">
 * <tr>
 * <th>
 * {@link org.seasar.aptina.beans.AccessType}
 * の値
 * </th>
 * <th>説明</th>
 * </tr>
 * <tr>
 * <td>{@link org.seasar.aptina.beans.AccessType#NONE}</td>
 * <td>プロパティとしてアクセスしません (getter/setter とも生成されません)．</td>
 * </tr>
 * <tr>
 * <td>{@link org.seasar.aptina.beans.AccessType#READ_ONLY}</td>
 * <td>参照のみ可能なプロパティです (getter のみ生成されます)．</td>
 * </tr>
 * <tr>
 * <td>{@link org.seasar.aptina.beans.AccessType#WRITE_ONLY}</td>
 * <td>
 * 変更のみ可能なプロパティです (setter のみ生成されます)．
 * フィールドが {@literal final} の場合はエラーになります．
 * </td>
 * </tr>
 * <tr>
 * <td>{@link org.seasar.aptina.beans.AccessType#READ_WRITE}<br />(デフォルト)</td>
 * <td>
 * 参照・変更とも可能なプロパティです (getter/setter とも生成されます)．
 * フィールドが {@literal final} の場合， setter は生成されません．
 * </td>
 * </tr>
 * </table>
 * <h3>コンストラクタ</h3>
 * <p>
 * Bean クラスは状態クラスの非 {@literal private} コンストラクタを引き継ぎます．
 * 引き継ぐことのできるコンストラクタが一つもない場合はエラーとなります．
 * </p>
 * <h3>例</h3>
 * <p>
 * 次に状態クラスの例を示します．
 * </p>
 * 
 * <pre>
 * &#x40;BeanState
 * public abstract class FooBeanState {
 *     protected String aaa; // getter/setter とも生成されます
 * 
 *     final String bbb;     // final なので getter のみ生成されます
 * 
 *     &#x40;Property(access=AccessType.NONE)
 *     public String ccc;    // AccessType.NONE なので getter/setter とも生成されません
 * 
 *     &#x40;Property(access=AccessType.READ_ONLY)
 *     public String ddd;    // AccessType.READ_ONLY なので getter のみ生成されます
 * 
 *     &#x40;Property(access=AccessType.WRITE_ONLY)
 *     public String eee;    // AccessType.WRITE_ONLY なので setter のみ生成されます
 * 
 *     &#x40;Property(access=AccessType.READ_WRITE)
 *     public String fff;    // AccessType.READ_WRITE なので getter/setter とも生成されます
 * 
 *     private String ggg;   // private なので getter/setter とも生成されません
 * 
 *     public String hhh;    // public なので getter/setter とも生成されません
 * 
 *     static String iii;    // static なので getter/setter とも生成されません
 * }
 * </pre>
 * <p>
 * {@link org.seasar.aptina.beans.internal.BeansProcessor} は， 
 * 上記の状態クラスから次の Bean クラスを生成します．
 * </p>
 * 
 * <pre>
 * &#x40;JavaBean
 * public class FooBean extends FooBeanState {
 *     public FooBean() {
 *         super();
 *     }
 * 
 *     public String getAaa() {
 *         return aaa;
 *     }
 *     public void setAaa(String aaa) {
 *         this.aaa = aaa;
 *     }
 * 
 *     public String getBbb() {
 *         return bbb;
 *     }
 * 
 *     public String getDdd() {
 *         return ddd;
 *     }
 * 
 *     public void setEee(String eee) {
 *         this.eee = eee;
 *     }
 * 
 *     public String getFff() {
 *         return fff;
 *     }
 *     public void setFff(String fff) {
 *         this.fff = fff;
 *     }
 * }
 * </pre>
 */
package org.seasar.aptina.beans;

