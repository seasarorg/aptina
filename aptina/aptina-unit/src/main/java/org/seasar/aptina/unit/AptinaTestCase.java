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
package org.seasar.aptina.unit;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

/**
 * {@link Processor} をテストするための抽象クラスです．
 * <p>
 * サブクラスのテストメソッドでは，コンパイルオプションやコンパイル対象などを設定して {@link #compile()} メソッドを呼び出します．
 * コンパイル後に生成されたソースの検証などを行うことができます．
 * </p>
 * <dl>
 * <dt>{@link #compile()} 前に以下のメソッドを呼び出すことができます ({@link #setUp()}から呼び出すこともできます)．
 * </dt>
 * <dd>
 * <ul>
 * <li>{@link #setLocale(Locale)}</li>
 * <li>{@link #setLocale(String)}</li>
 * <li>{@link #setCharset(Charset)}</li>
 * <li>{@link #setCharset(String)}</li>
 * <li>{@link #setOut(Writer)}</li>
 * <li>{@link #addSourcePath(File...)}</li>
 * <li>{@link #addSourcePath(String...)}</li>
 * <li>{@link #addOption(String...)}</li>
 * <li>{@link #addProcessor(Processor...)}</li>
 * <li>{@link #addCompilationUnit(Class)}</li>
 * <li>{@link #addCompilationUnit(String)}</li>
 * <li>{@link #addCompilationUnit(Class, CharSequence)}</li>
 * <li>{@link #addCompilationUnit(String, CharSequence)}</li>
 * </ul>
 * </dd>
 * <dt>{@link #compile()} 後に以下のメソッドを呼び出して情報を取得することができます．</dt>
 * <dd>
 * <ul>
 * <li>{@link #getCompiledResult()}</li>
 * <li>{@link #getDiagnostics()}</li>
 * <li>{@link #getProcessingEnvironment()}</li>
 * <li>{@link #getElementUtils()}</li>
 * <li>{@link #getTypeUtils()}</li>
 * <li>{@link #getTypeElement(Class)}</li>
 * <li>{@link #getTypeElement(String)}</li>
 * <li>{@link #getFieldElement(TypeElement, Field)}</li>
 * <li>{@link #getFieldElement(TypeElement, String)}</li>
 * <li>{@link #getConstructorElement(TypeElement)}</li>
 * <li>{@link #getConstructorElement(TypeElement, Class...)}</li>
 * <li>{@link #getConstructorElement(TypeElement, String...)}</li>
 * <li>{@link #getMethodElement(TypeElement, String)}</li>
 * <li>{@link #getMethodElement(TypeElement, String, Class...)}</li>
 * <li>{@link #getMethodElement(TypeElement, String, String...)}</li>
 * <li>{@link #getTypeMirror(Class)}</li>
 * <li>{@link #getTypeMirror(String)}</li>
 * <li>{@link #getGeneratedSource(Class)}</li>
 * <li>{@link #getGeneratedSource(String)}</li>
 * </ul>
 * </dd>
 * <dt>{@link #compile()} 後に以下のメソッドを呼び出して生成されたソースの内容を検証することができます．</dt>
 * <dd>
 * <ul>
 * <li>{@link #assertEqualsGeneratedSource(CharSequence, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSource(CharSequence, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(File, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(File, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(String, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(String, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(URL, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(URL, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(String, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(String, String)}</li>
 * </ul>
 * </dd>
 * </dl>
 * <p>
 * {@link #compile()} を呼び出した後に状態をリセットしてコンパイル前の状態に戻すには， {@link #reset()} を呼び出します．
 * </p>
 * 
 * <p>
 * 次のサンプルは， <code>src/test/java</code> フォルダにある <code>TestSource.java</code>
 * をコンパイルすると， <code>foo.bar.Baz</code> クラスのソースを生成する <code>TestProcessor</code>
 * のテストクラスです．
 * </p>
 * 
 * <pre>
 * public class TestProcessorTest extends AptinaTestCase {
 *
 *     &#x40;Override
 *     protected void setUp() throws Exception {
 *         super.setUp();
 *         // ソースパスを追加
 *         addSourcePath("src/test/java");
 *     }
 *
 *     public void test() throws Exception {
 *         // テスト対象の Annotation Processor を生成して追加
 *         TestProcessor processor = new TestProcessor();
 *         addProcessor(processor);
 *
 *         // コンパイル対象を追加
 *         addCompilationUnit(TestSource.class);
 *
 *         // コンパイル実行
 *         compile();
 *
 *         // テスト対象の Annotation Processor が生成したソースを検証
 *         assertEqualsGeneratedSource("package foo.bar; public class Baz {}",
 *                 "foo.bar.Baz");
 *     }
 * 
 * }
 * </pre>
 * 
 * @author koichik
 */
public abstract class AptinaTestCase extends TestCase {

    Locale locale;

    Charset charset;

    Writer out;

    final List<String> options = new ArrayList<String>();

    final List<File> sourcePaths = new ArrayList<File>();

    final List<Processor> processors = new ArrayList<Processor>();
    {
        processors.add(new AptinaProcessor());
    }

    final List<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();

    JavaCompiler javaCompiler;

    DiagnosticCollector<JavaFileObject> diagnostics;

    StandardJavaFileManager standardJavaFileManager;

    JavaFileManager testingJavaFileManager;

    ProcessingEnvironment processingEnvironment;

    Boolean compiledResult;

    final Map<String, TypeKind> primitiveTypes = new HashMap<String, TypeKind>();
    {
        primitiveTypes.put(void.class.getName(), TypeKind.VOID);
        primitiveTypes.put(boolean.class.getName(), TypeKind.BOOLEAN);
        primitiveTypes.put(char.class.getName(), TypeKind.CHAR);
        primitiveTypes.put(byte.class.getName(), TypeKind.BYTE);
        primitiveTypes.put(short.class.getName(), TypeKind.SHORT);
        primitiveTypes.put(int.class.getName(), TypeKind.INT);
        primitiveTypes.put(long.class.getName(), TypeKind.LONG);
        primitiveTypes.put(float.class.getName(), TypeKind.FLOAT);
        primitiveTypes.put(double.class.getName(), TypeKind.DOUBLE);
    }

    /**
     * インスタンスを構築します．
     */
    protected AptinaTestCase() {
    }

    /**
     * インスタンスを構築します．
     * 
     * @param name
     *            名前
     */
    protected AptinaTestCase(final String name) {
        super(name);
    }

    /**
     * ロケールを設定します．
     * <p>
     * 設定されなかった場合はプラットフォームデフォルトのロケールが使われます．
     * </p>
     * 
     * @param locale
     *            ロケール
     * @see Locale#getDefault()
     */
    protected void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * ロケールを設定します．
     * <p>
     * 設定されなかった場合はプラットフォームデフォルトのロケールが使われます．
     * </p>
     * 
     * @param locale
     *            ロケール
     * @see Locale#getDefault()
     */
    protected void setLocale(final String locale) {
        checkNotEmpty("locale", locale);
        setLocale(new Locale(locale));
    }

    /**
     * 文字セットを設定します．
     * <p>
     * 設定されなかった場合はプラットフォームデフォルトの文字セットが使われます．
     * </p>
     * 
     * @param charset
     *            文字セット
     * @see Charset#defaultCharset()
     */
    protected void setCharset(final Charset charset) {
        this.charset = charset;
    }

    /**
     * 文字セットを設定します．
     * <p>
     * 設定されなかった場合はプラットフォームデフォルトの文字セットが使われます．
     * </p>
     * 
     * @param charset
     *            文字セット
     * @see Charset#defaultCharset()
     */
    protected void setCharset(final String charset) {
        checkNotEmpty("charset", charset);
        setCharset(Charset.forName(charset));
    }

    /**
     * コンパイラがメッセージを出力する{@link Writer}を設定します．
     * <p>
     * 設定されなかった場合は標準エラーが使われます．
     * </p>
     * 
     * @param out
     *            コンパイラがメッセージを出力する{@link Writer}
     */
    protected void setOut(final Writer out) {
        this.out = out;
    }

    /**
     * コンパイル時に参照するソースパスを追加します．
     * 
     * @param sourcePaths
     *            コンパイル時に参照するソースパスの並び
     */
    protected void addSourcePath(final File... sourcePaths) {
        checkNotEmpty("sourcePaths", sourcePaths);
        this.sourcePaths.addAll(Arrays.asList(sourcePaths));
    }

    /**
     * コンパイル時に参照するソースパスを追加します．
     * 
     * @param sourcePaths
     *            コンパイル時に参照するソースパスの並び
     */
    protected void addSourcePath(final String... sourcePaths) {
        checkNotEmpty("sourcePaths", sourcePaths);
        for (final String path : sourcePaths) {
            this.sourcePaths.add(new File(path));
        }
    }

    /**
     * コンパイラオプションを追加します．
     * 
     * @param options
     *            形式のコンパイラオプションの並び
     */
    protected void addOption(final String... options) {
        checkNotEmpty("options", options);
        this.options.addAll(Arrays.asList(options));
    }

    /**
     * 注釈を処理する{@link Processor}を追加します．
     * 
     * @param processors
     *            注釈を処理する{@link Processor}の並び
     */
    protected void addProcessor(final Processor... processors) {
        checkNotEmpty("processors", processors);
        this.processors.addAll(Arrays.asList(processors));
    }

    /**
     * コンパイル対象のクラスを追加します．
     * <p>
     * 指定されたクラスのソースはソースパス上に存在していなければなりません．
     * </p>
     * 
     * @param clazz
     *            コンパイル対象クラス
     */
    protected void addCompilationUnit(final Class<?> clazz) {
        checkNotNull("clazz", clazz);
        addCompilationUnit(clazz.getName());
    }

    /**
     * コンパイル対象のクラスを追加します．
     * <p>
     * 指定されたクラスのソースはソースパス上に存在していなければなりません．
     * </p>
     * 
     * @param className
     *            コンパイル対象クラスの完全限定名
     */
    protected void addCompilationUnit(final String className) {
        checkNotEmpty("className", className);
        compilationUnits.add(new FileCompilationUnit(className));
    }

    /**
     * コンパイル対象のクラスをソースとともに追加します．
     * 
     * @param clazz
     *            コンパイル対象クラス
     * @param source
     *            ソース
     */
    protected void addCompilationUnit(final Class<?> clazz,
            final CharSequence source) {
        checkNotNull("clazz", clazz);
        checkNotEmpty("source", source);
        addCompilationUnit(clazz.getName(), source);
    }

    /**
     * コンパイル対象のクラスをソースとともに追加します．
     * 
     * @param className
     *            コンパイル対象クラスの完全限定名
     * @param source
     *            ソース
     */
    protected void addCompilationUnit(final String className,
            final CharSequence source) {
        checkNotEmpty("className", className);
        checkNotEmpty("source", source);
        compilationUnits.add(new InMemoryCompilationUnit(className, source
                .toString()));
    }

    /**
     * コンパイルを実行します．
     * 
     * @throws IOException
     *             入出力例外が発生した場合
     */
    protected void compile() throws IOException {
        javaCompiler = ToolProvider.getSystemJavaCompiler();
        diagnostics = new DiagnosticCollector<JavaFileObject>();
        final DiagnosticListener<JavaFileObject> listener = new LoggingDiagnosticListener(
                diagnostics);

        standardJavaFileManager = javaCompiler.getStandardFileManager(listener,
                locale, charset);
        standardJavaFileManager.setLocation(StandardLocation.SOURCE_PATH,
                sourcePaths);
        testingJavaFileManager = new TestingJavaFileManager(
                standardJavaFileManager, charset);

        final CompilationTask task = javaCompiler.getTask(out,
                testingJavaFileManager, listener, options, null,
                getCompilationUnits());
        task.setProcessors(processors);
        compiledResult = task.call();
        compilationUnits.clear();
    }

    /**
     * コンパイラの実行結果を返します．
     * 
     * @return コンパイラの実行結果
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @see CompilationTask#call()
     */
    protected Boolean getCompiledResult() throws IllegalStateException {
        checkCompiled();
        return compiledResult;
    }

    /**
     * コンパイル中に作成された {@link Diagnostic} のリストを返します．
     * 
     * @return コンパイル中に作成された {@link Diagnostic} のリスト
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics()
            throws IllegalStateException {
        checkCompiled();
        return diagnostics.getDiagnostics();
    }

    /**
     * {@link ProcessingEnvironment} を返します．
     * 
     * @return {@link ProcessingEnvironment}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ProcessingEnvironment getProcessingEnvironment()
            throws IllegalStateException {
        checkCompiled();
        return processingEnvironment;
    }

    /**
     * {@link Elements} を返します．
     * 
     * @return {@link Elements}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @see ProcessingEnvironment#getElementUtils()
     */
    protected Elements getElementUtils() throws IllegalStateException {
        checkCompiled();
        return processingEnvironment.getElementUtils();
    }

    /**
     * {@link Types} を返します．
     * 
     * @return {@link Types}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @see ProcessingEnvironment#getTypeUtils()
     */
    protected Types getTypeUtils() throws IllegalStateException {
        checkCompiled();
        return processingEnvironment.getTypeUtils();
    }

    /**
     * クラスに対応する {@link TypeElement} を返します．
     * <p>
     * このメソッドが返す {@link TypeElement} およびその {@link Element#getEnclosedElements()}
     * が返す {@link Element} から， {@link Elements#getDocComment(Element)} を使って
     * Javadoc コメントを取得することはできません．
     * </p>
     * 
     * @param clazz
     *            クラス
     * @return クラスに対応する{@link TypeElement}， 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeElement getTypeElement(final Class<?> clazz)
            throws IllegalStateException {
        checkNotNull("clazz", clazz);
        checkCompiled();
        return getElementUtils().getTypeElement(clazz.getName());
    }

    /**
     * クラスに対応する {@link TypeElement} を返します．
     * <p>
     * このメソッドが返す {@link TypeElement} およびその {@link Element#getEnclosedElements()}
     * が返す {@link Element} から， {@link Elements#getDocComment(Element)} を使って
     * Javadoc コメントを取得することはできません．
     * </p>
     * 
     * @param className
     *            クラスの完全限定名
     * @return クラスに対応する{@link TypeElement}， 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeElement getTypeElement(final String className)
            throws IllegalStateException {
        checkNotEmpty("className", className);
        checkCompiled();
        try {
            // 仕様では存在しないクラスを引数に Elements#getTypeElement(String) を呼び出すと
            // null が返されるはずだが， コンパイラの実行が終わった後だと NPE がスローされる
            return getElementUtils().getTypeElement(className);
        } catch (final NullPointerException e) {
            return null;
        }
    }

    /**
     * 型エレメントに定義されたフィールドの変数エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param field
     *            フィールド
     * @return 型エレメントに定義されたフィールドの変数エレメント． 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected VariableElement getFieldElement(final TypeElement typeElement,
            final Field field) throws IllegalStateException {
        checkNotNull("typeElement", typeElement);
        checkNotNull("field", field);
        checkCompiled();
        return getFieldElement(typeElement, field.getName());
    }

    /**
     * 型エレメントに定義されたフィールドの変数エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param fieldName
     *            フィールド名
     * @return 型エレメントに定義されたフィールドの変数エレメント． 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected VariableElement getFieldElement(final TypeElement typeElement,
            final String fieldName) throws IllegalStateException {
        checkNotNull("typeElement", typeElement);
        checkNotEmpty("fieldName", fieldName);
        checkCompiled();
        for (final VariableElement variableElement : ElementFilter
                .fieldsIn(typeElement.getEnclosedElements())) {
            if (fieldName.equals(variableElement.getSimpleName().toString())) {
                return variableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたデフォルトコンストラクタの実行可能エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @return 型エレメントに定義されたデフォルトコンストラクタの実行可能エレメント． 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getConstructorElement(
            final TypeElement typeElement) throws IllegalStateException {
        checkNotNull("typeElement", typeElement);
        checkCompiled();
        for (final ExecutableElement executableElement : ElementFilter
                .constructorsIn(typeElement.getEnclosedElements())) {
            if (executableElement.getParameters().size() == 0) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたコンストラクタの実行可能エレメントを返します．
     * <p>
     * 引数型が型引数を持つ場合は {@link #getConstructorElement(TypeElement, String...)}
     * を使用してください．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param parameterTypes
     *            引数型の並び
     * @return 型エレメントに定義されたコンストラクタの実行可能エレメント． 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getConstructorElement(
            final TypeElement typeElement, final Class<?>... parameterTypes)
            throws IllegalStateException {
        checkNotNull("typeElement", typeElement);
        checkNotNull("parameterTypes", parameterTypes);
        checkCompiled();
        for (final ExecutableElement executableElement : ElementFilter
                .constructorsIn(typeElement.getEnclosedElements())) {
            if (isMatchParameterTypes(parameterTypes, executableElement
                    .getParameters())) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたコンストラクタの実行可能エレメントを返します．
     * <p>
     * 引数がの型が配列の場合は， 要素型の名前の後に <code>[]</code> を連ねる形式と， <code>[[LString;</code>
     * のような形式のどちらでも指定することができます．
     * </p>
     * <p>
     * 引数型が型引数を持つ場合は <code>"java.util.List&lt;T&gt;"</code> のようにそのまま指定します．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param parameterTypeNames
     *            引数の型名の並び
     * @return 型エレメントに定義されたコンストラクタの実行可能エレメント． 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getConstructorElement(
            final TypeElement typeElement, final String... parameterTypeNames)
            throws IllegalStateException {
        checkNotNull("typeElement", typeElement);
        checkNotNull("parameterTypeNames", parameterTypeNames);
        checkCompiled();
        for (final ExecutableElement executableElement : ElementFilter
                .constructorsIn(typeElement.getEnclosedElements())) {
            if (isMatchParameterTypes(parameterTypeNames, executableElement
                    .getParameters())) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @return 型エレメントに定義されたメソッドの実行可能エレメント．存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getMethodElement(final TypeElement typeElement,
            final String methodName) throws IllegalStateException {
        checkNotNull("typeElement", typeElement);
        checkNotEmpty("methodName", methodName);
        checkCompiled();
        for (final ExecutableElement executableElement : ElementFilter
                .methodsIn(typeElement.getEnclosedElements())) {
            if (!methodName
                    .equals(executableElement.getSimpleName().toString())) {
                continue;
            }
            if (executableElement.getParameters().size() == 0) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * <p>
     * 引数型が型引数を持つ場合は {@link #getMethodElement(TypeElement, String, String...)}
     * を使用してください．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @param parameterTypes
     *            引数型の並び
     * @return 型エレメントに定義されたメソッドの実行可能エレメント． 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getMethodElement(final TypeElement typeElement,
            final String methodName, final Class<?>... parameterTypes)
            throws IllegalStateException {
        checkNotNull("typeElement", typeElement);
        checkNotEmpty("methodName", methodName);
        checkNotNull("parameterTypes", parameterTypes);
        checkCompiled();
        for (final ExecutableElement executableElement : ElementFilter
                .methodsIn(typeElement.getEnclosedElements())) {
            if (!methodName
                    .equals(executableElement.getSimpleName().toString())) {
                continue;
            }
            if (isMatchParameterTypes(parameterTypes, executableElement
                    .getParameters())) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * <p>
     * 引数がの型が配列の場合は， 要素型の名前の後に <code>[]</code> を連ねる形式と， <code>[[LString;</code>
     * のような形式のどちらでも指定することができます．
     * </p>
     * <p>
     * 引数型が型引数を持つ場合は <code>"java.util.List&lt;T&gt;"</code> のようにそのまま指定します．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @param parameterTypeNames
     *            引数の型名の並び
     * @return 型エレメントに定義されたメソッドの実行可能エレメント． 存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getMethodElement(final TypeElement typeElement,
            final String methodName, final String... parameterTypeNames)
            throws IllegalStateException {
        checkNotNull("typeElement", typeElement);
        checkNotEmpty("methodName", methodName);
        checkNotNull("parameterTypeNames", parameterTypeNames);
        checkCompiled();
        for (final ExecutableElement executableElement : ElementFilter
                .methodsIn(typeElement.getEnclosedElements())) {
            if (!methodName
                    .equals(executableElement.getSimpleName().toString())) {
                continue;
            }
            if (isMatchParameterTypes(parameterTypeNames, executableElement
                    .getParameters())) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * クラスに対応する {@link TypeMirror} を返します．
     * 
     * @param clazz
     *            クラス
     * @return クラスに対応する{@link TypeMirror}， クラスが存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeMirror getTypeMirror(final Class<?> clazz)
            throws IllegalStateException {
        checkNotNull("clazz", clazz);
        checkCompiled();
        if (clazz.isArray()) {
            return toArrayTypeMirror(getTypeMirror(clazz.getComponentType()));
        }
        return getTypeMirror(clazz.getName());
    }

    /**
     * クラスに対応する {@link TypeMirror} を返します．
     * <p>
     * 配列の場合は要素型の名前の後に <code>[]</code> を連ねる形式と， <code>[[LString;</code> のような
     * 形式のどちらでも指定することができます．
     * </p>
     * 
     * @param className
     *            クラスの完全限定名
     * @return クラスに対応する{@link TypeMirror}， クラスが存在しない場合は {@literal null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeMirror getTypeMirror(final String className)
            throws IllegalStateException {
        checkNotEmpty("className", className);
        checkCompiled();
        if (className.endsWith("[]")) {
            final String componentTypeName = className.substring(0, className
                    .length() - 2);
            return toArrayTypeMirror(getTypeMirror(componentTypeName));
        }
        if (className.startsWith("[") && className.endsWith(";")) {
            final int pos = className.indexOf("L");
            final String componentTypeName = className.substring(pos + 1,
                    className.length() - 1);
            TypeMirror typeMirror = getTypeMirror(componentTypeName);
            for (int i = 0; i < pos; ++i) {
                typeMirror = toArrayTypeMirror(typeMirror);
            }
            return typeMirror;
        }
        if (primitiveTypes.containsKey(className)) {
            final TypeKind typeKind = primitiveTypes.get(className);
            return getTypeUtils().getPrimitiveType(typeKind);
        }
        final TypeElement typeElement = getTypeElement(className);
        if (typeElement == null) {
            return null;
        }
        return typeElement.asType();
    }

    /**
     * {@link Processor} が生成したソースを返します．
     * 
     * @param clazz
     *            生成されたクラス
     * @return 生成されたソースの内容
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     */
    protected String getGeneratedSource(final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException {
        checkNotNull("clazz", clazz);
        checkCompiled();
        return getGeneratedSource(clazz.getName());
    }

    /**
     * {@link Processor} が生成したソースを返します．
     * 
     * @param className
     *            生成されたクラスの完全限定名
     * @return 生成されたソースの内容
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     */
    protected String getGeneratedSource(final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException {
        checkNotEmpty("className", className);
        checkCompiled();
        final JavaFileObject javaFileObject = testingJavaFileManager
                .getJavaFileForInput(StandardLocation.SOURCE_OUTPUT, className,
                        Kind.SOURCE);
        if (javaFileObject == null) {
            throw new SourceNotGeneratedException(className);
        }
        final CharSequence content = javaFileObject.getCharContent(true);
        if (content == null) {
            throw new SourceNotGeneratedException(className);
        }
        return content.toString();
    }

    /**
     * {@link Processor} が生成したソースを文字列と比較・検証します．
     * 
     * @param expected
     *            生成されたソースに期待される内容の文字列
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSource(final CharSequence expected,
            final Class<?> clazz) throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotEmpty("expected", expected);
        checkNotNull("clazz", clazz);
        checkCompiled();
        assertEqualsGeneratedSource(expected, clazz.getName());
    }

    /**
     * {@link Processor} が生成したソースを文字列と比較・検証します．
     * 
     * @param expected
     *            生成されたソースに期待される内容の文字列
     * @param className
     *            生成されたクラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSource(final CharSequence expected,
            final String className) throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotEmpty("expected", expected);
        checkNotEmpty("className", className);
        checkCompiled();
        assertEquals(className, expected.toString(),
                getGeneratedSource(className));
    }

    /**
     * {@link Processor} が生成したソースをファイルと比較・検証します．
     * 
     * @param expectedSourceFile
     *            生成されたソースに期待される内容を持つファイル
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithFile(
            final File expectedSourceFile, final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotNull("expectedSourceFile", expectedSourceFile);
        checkNotNull("clazz", clazz);
        checkCompiled();
        assertEqualsGeneratedSourceWithFile(expectedSourceFile, clazz.getName());
    }

    /**
     * {@link Processor} が生成したソースをファイルと比較・検証します．
     * 
     * @param expectedSourceFile
     *            生成されたソースに期待される内容を持つファイル
     * @param className
     *            クラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithFile(
            final File expectedSourceFile, final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotNull("expectedSourceFile", expectedSourceFile);
        checkNotEmpty("className", className);
        checkCompiled();
        assertEqualsGeneratedSource(readFromFile(expectedSourceFile), className);
    }

    /**
     * {@link Processor} が生成したソースをファイルと比較・検証します．
     * 
     * @param expectedSourceFilePath
     *            生成されたソースに期待される内容を持つファイルのパス
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithFile(
            final String expectedSourceFilePath, final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotEmpty("expectedSourceFilePath", expectedSourceFilePath);
        checkNotNull("clazz", clazz);
        checkCompiled();
        assertEqualsGeneratedSourceWithFile(expectedSourceFilePath, clazz
                .getName());
    }

    /**
     * {@link Processor} が生成したソースをファイルと比較・検証します．
     * 
     * @param expectedSourceFilePath
     *            生成されたソースに期待される内容を持つファイルのパス
     * @param className
     *            生成されたクラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithFile(
            final String expectedSourceFilePath, final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotEmpty("expectedSourceFilePath", expectedSourceFilePath);
        checkNotEmpty("className", className);
        checkCompiled();
        assertEqualsGeneratedSourceWithFile(new File(expectedSourceFilePath),
                className);
    }

    /**
     * {@link Processor} が生成したソースをクラスパス上のリソースと比較・検証します．
     * 
     * @param expectedResourceUrl
     *            生成されたソースに期待される内容を持つリソースのURL
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithResource(
            final URL expectedResourceUrl, final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotNull("expectedResourceUrl", expectedResourceUrl);
        checkNotNull("clazz", clazz);
        checkCompiled();
        assertEqualsGeneratedSourceWithResource(expectedResourceUrl, clazz
                .getName());
    }

    /**
     * {@link Processor} が生成したソースをクラスパス上のリソースと比較・検証します．
     * 
     * @param expectedResourceUrl
     *            生成されたソースに期待される内容を持つリソースのURL
     * @param className
     *            生成されたクラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithResource(
            final URL expectedResourceUrl, final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotNull("expectedResourceUrl", expectedResourceUrl);
        checkNotEmpty("className", className);
        checkCompiled();
        assertEqualsGeneratedSource(readFromResource(expectedResourceUrl),
                className);
    }

    /**
     * {@link Processor} が生成したソースをクラスパス上のリソースと比較・検証します．
     * 
     * @param expectedResource
     *            生成されたソースに期待される内容を持つリソースのパス
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithResource(
            final String expectedResource, final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotEmpty("expectedResource", expectedResource);
        checkNotNull("clazz", clazz);
        checkCompiled();
        assertEqualsGeneratedSourceWithResource(clazz.getName(),
                expectedResource);
    }

    /**
     * {@link Processor} が生成したソースをクラスパス上のリソースと比較・検証します．
     * 
     * @param expectedResource
     *            生成されたソースに期待される内容を持つリソースのパス
     * @param className
     *            生成されたクラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithResource(
            final String expectedResource, final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        checkNotEmpty("expectedResource", expectedResource);
        checkNotEmpty("className", className);
        checkCompiled();
        final URL url = Thread.currentThread().getContextClassLoader()
                .getResource(expectedResource);
        if (url == null) {
            throw new FileNotFoundException(expectedResource);
        }
        assertEqualsGeneratedSourceWithResource(url, className);
    }

    /**
     * 設定をリセットし，初期状態に戻します．
     * <p>
     * {@link #compile()} 呼び出し前に設定した内容も， {@link #compile()}
     * によって得られた状態も全てリセットされます．
     * </p>
     */
    protected void reset() {
        locale = null;
        charset = null;
        out = null;
        options.clear();
        sourcePaths.clear();
        processors.clear();
        processors.add(new AptinaProcessor());
        compilationUnits.clear();
        javaCompiler = null;
        diagnostics = null;
        standardJavaFileManager = null;
        testingJavaFileManager = null;
        processingEnvironment = null;
        compiledResult = null;
    }

    /**
     * 引数の型を要素とする配列の {@link TypeMirror} を返します．
     * 
     * @param componentTypeMirror
     *            配列の要素となる型
     * @return 引数の型を要素とする配列の {@link TypeMirror}
     * @see Types#getArrayType(TypeMirror)
     */
    TypeMirror toArrayTypeMirror(final TypeMirror componentTypeMirror) {
        if (componentTypeMirror == null) {
            return null;
        }
        return getTypeUtils().getArrayType(componentTypeMirror);
    }

    /**
     * クラスの配列を {@link TypeMirror} の配列に変換して返します．
     * 
     * @param types
     *            クラスの配列
     * @return {@link TypeMirror} の配列
     * @throws IllegalArgumentException
     *             配列の要素のクラスに対応する {@link TypeMirror} が存在しない場合
     */
    List<TypeMirror> toTypeMirrors(final Class<?>... types)
            throws IllegalArgumentException {
        final List<TypeMirror> typeMirrors = new ArrayList<TypeMirror>();
        for (final Class<?> type : types) {
            final TypeMirror typeMirror = getTypeMirror(type);
            if (typeMirror == null) {
                throw new IllegalArgumentException("unknown type : " + type);
            }
            typeMirrors.add(typeMirror);
        }
        return typeMirrors;
    }

    /**
     * クラス名の配列を {@link TypeMirror} の配列に変換して返します．
     * 
     * @param types
     *            クラス名の配列
     * @return {@link TypeMirror} の配列
     * @throws IllegalArgumentException
     *             配列の要素のクラスに対応する {@link TypeMirror} が存在しない場合
     */
    List<TypeMirror> toTypeMirrors(final String... typeNames) {
        final List<TypeMirror> typeMirrors = new ArrayList<TypeMirror>();
        for (final String typeName : typeNames) {
            final TypeMirror typeMirror = getTypeMirror(typeName);
            if (typeMirror == null) {
                throw new IllegalArgumentException("unknown type : " + typeName);
            }
            typeMirrors.add(typeMirror);
        }
        return typeMirrors;
    }

    /**
     * {@link TypeMirror}のリストと{@link VariableElement}のリストの， それぞれの要素の型がマッチすれば
     * {@link true} を返します．
     * 
     * @param typeMirros
     * @param variableElements
     * @return 二つのリストのそれぞれの要素の型がマッチすれば {@link true}
     */
    boolean isMatchParameterTypes(final Class<?>[] parameterTypes,
            final List<? extends VariableElement> variableElements) {
        final List<? extends TypeMirror> typeMirrors = toTypeMirrors(parameterTypes);
        if (typeMirrors.size() != variableElements.size()) {
            return false;
        }
        for (int i = 0; i < typeMirrors.size(); ++i) {
            if (!getTypeUtils().isSameType(typeMirrors.get(i),
                    variableElements.get(i).asType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@link TypeMirror}のリストと{@link VariableElement}のリストの， それぞれの要素の型がマッチすれば
     * {@link true} を返します．
     * 
     * @param typeMirros
     * @param variableElements
     * @return 二つのリストのそれぞれの要素の型がマッチすれば {@link true}
     */
    boolean isMatchParameterTypes(final String[] typeNames,
            final List<? extends VariableElement> variableElements) {
        if (typeNames.length != variableElements.size()) {
            return false;
        }
        for (int i = 0; i < typeNames.length; ++i) {
            if (!typeNames[i].equals(variableElements.get(i).asType()
                    .toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * パラメータが {@literal null} であれば例外をスローします．
     * 
     * @param name
     *            パラメータの名前
     * @param param
     *            パラメータ
     * @throws NullPointerException
     *             パラメータが {@literal null} の場合
     */
    void checkNotNull(final String name, final Object param)
            throws NullPointerException {
        if (param == null) {
            throw new NullPointerException(name);
        }
    }

    /**
     * 配列が空であれば例外をスローします．
     * 
     * @param name
     *            配列の名前
     * @param string
     *            文字列
     * @throws NullPointerException
     *             文字列が {@literal null} の場合
     * @throws IllegalArgumentException
     *             配列が {@literal null} の場合
     */
    void checkNotEmpty(final String name, final CharSequence string)
            throws IllegalArgumentException {
        if (string == null) {
            throw new NullPointerException(name);
        }
        if (string.length() == 0) {
            throw new IllegalArgumentException(name + " must not be empty");
        }
    }

    /**
     * 配列が {@literal null} または空であれば例外をスローします．
     * 
     * @param name
     *            配列の名前
     * @param array
     *            配列
     * @throws NullPointerException
     *             配列が {@literal null} の場合
     * @throws IllegalArgumentException
     *             配列が空の場合
     */
    void checkNotEmpty(final String name, final Object[] array)
            throws IllegalArgumentException {
        if (array == null) {
            throw new NullPointerException(name);
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(name + " must not be empty");
        }
        for (final Object element : array) {
            if (element == null) {
                throw new NullPointerException("element of " + name);
            }
        }
    }

    /**
     * {@link #compile()} が呼び出されていなければ例外をスローします．
     * 
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    void checkCompiled() throws IllegalStateException {
        if (compiledResult == null) {
            throw new IllegalStateException("not compiled");
        }
    }

    /**
     * 追加されたコンパイル対象のリストを返します．
     * 
     * @return 追加されたコンパイル対象のリスト
     * @throws IOException
     *             入出力例外が発生した場合
     */
    List<JavaFileObject> getCompilationUnits() throws IOException {
        final List<JavaFileObject> result = new ArrayList<JavaFileObject>(
                compilationUnits.size());
        for (final CompilationUnit compilationUnit : compilationUnits) {
            result.add(compilationUnit.getJavaFileObject());
        }
        return result;
    }

    /**
     * ファイルから読み込んだ内容を文字列で返します．
     * <p>
     * ファイルの内容は， {@link #charset} で指定された文字セットでエンコード (未設定時はプラットフォームデフォルトの文字セット)
     * されていなければなりません．
     * </p>
     * 
     * @param file
     *            ファイル
     * @return ファイルから読み込んだ内容の文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    String readFromFile(final File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath());
        }
        final FileInputStream is = new FileInputStream(file);
        try {
            return readFromStream(is, (int) file.length());
        } finally {
            closeSilently(is);
        }
    }

    /**
     * URL から読み込んだ内容を文字列で返します．
     * <p>
     * URLで表されるリソースの内容は， {@link #charset} で指定された文字セットでエンコード
     * (未設定時はプラットフォームデフォルトの文字セット) されていなければなりません．
     * </p>
     * 
     * @param url
     *            リソースのURL
     * @return URL から読み込んだ内容の文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    String readFromResource(final URL url) throws IOException {
        final InputStream is = url.openStream();
        try {
            if (is == null) {
                throw new FileNotFoundException(url.toExternalForm());
            }
            return readFromStream(is, is.available());
        } finally {
            closeSilently(is);
        }
    }

    /**
     * ストリームから読み込んだ内容を文字列で返します．
     * <p>
     * ファイルの内容は {@link #charset} で指定された文字セットでエンコード (未設定時はプラットフォームデフォルトの文字セット)
     * されていなければなりません．
     * </p>
     * 
     * @param is
     *            入力ストリーム
     * @param size
     *            ストリームから読み込めるバイト数
     * @return ストリームから読み込んだ内容の文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    String readFromStream(final InputStream is, final int size)
            throws IOException {
        final byte[] bytes = new byte[size];
        int readSize = 0;
        while (readSize < size) {
            readSize += is.read(bytes, readSize, size - readSize);
        }
        return new String(bytes, charset == null ? Charset.defaultCharset()
                : charset);
    }

    /**
     * クローズ可能なオブジェクトをクローズします．
     * <p>
     * 例外が発生しても無視します．
     * </p>
     * 
     * @param closeable
     *            クローズ可能なオブジェクト
     */
    void closeSilently(final Closeable closeable) {
        try {
            closeable.close();
        } catch (final IOException ignore) {
        }
    }

    /**
     * 発生した {@link Diagnostic} をコンソールに出力する {@link DiagnosticListener} です．
     * <p>
     * {@link Diagnostic} コンソールに出力した後，後続の {@link DiagnosticListener} へ通知します．
     * </p>
     * 
     * @author koichik
     */
    static class LoggingDiagnosticListener implements
            DiagnosticListener<JavaFileObject> {

        DiagnosticListener<JavaFileObject> listener;

        /**
         * インスタンスを構築します．
         * 
         * @param listener
         *            後続の {@link DiagnosticListener}
         */
        LoggingDiagnosticListener(
                final DiagnosticListener<JavaFileObject> listener) {
            this.listener = listener;
        }

        @Override
        public void report(final Diagnostic<? extends JavaFileObject> diagnostic) {
            System.out.println(diagnostic);
            listener.report(diagnostic);
        }

    }

    /**
     * コンパイル時に {@link Processor} に渡される @ ProcessingEnvironment} を取得するための
     * {@link Processor} です．
     * 
     * @author koichik
     */
    @SupportedSourceVersion(SourceVersion.RELEASE_6)
    @SupportedAnnotationTypes("*")
    class AptinaProcessor extends AbstractProcessor {

        @Override
        public synchronized void init(
                final ProcessingEnvironment processingEnvironment) {
            super.init(processingEnvironment);
            AptinaTestCase.this.processingEnvironment = processingEnvironment;
        }

        @Override
        public boolean process(final Set<? extends TypeElement> annotations,
                final RoundEnvironment roundEnv) {
            return false;
        }

    }

    /**
     * コンパイル対象を表すインタフェースです．
     * 
     * @author koichik
     */
    interface CompilationUnit {

        /**
         * このコンパイル対象に対応する {@link JavaFileObject} を返します．
         * 
         * @return このコンパイル対象に対応する {@link JavaFileObject}
         * @throws IOException
         *             入出力例外が発生した場合
         */
        JavaFileObject getJavaFileObject() throws IOException;

    }

    /**
     * ソースパス上のファイルとして存在するコンパイル対象を表すクラスです．
     * 
     * @author koichik
     */
    class FileCompilationUnit implements CompilationUnit {

        String className;

        /**
         * インスタンスを構築します．
         * 
         * @param className
         *            クラス名
         */
        public FileCompilationUnit(final String className) {
            this.className = className;
        }

        @Override
        public JavaFileObject getJavaFileObject() throws IOException {
            return standardJavaFileManager.getJavaFileForInput(
                    StandardLocation.SOURCE_PATH, className, Kind.SOURCE);
        }

    }

    /**
     * メモリ上に存在するコンパイル対象を表すクラスです．
     * 
     * @author koichik
     */
    class InMemoryCompilationUnit implements CompilationUnit {

        String className;

        String source;

        /**
         * インスタンスを構築します．
         * 
         * @param className
         *            クラス名
         * @param source
         *            ソース
         */
        public InMemoryCompilationUnit(final String className,
                final String source) {
            this.className = className;
            this.source = source;
        }

        @Override
        public JavaFileObject getJavaFileObject() throws IOException {
            final JavaFileObject javaFileObject = testingJavaFileManager
                    .getJavaFileForOutput(StandardLocation.SOURCE_OUTPUT,
                            className, Kind.SOURCE, null);
            final Writer writer = javaFileObject.openWriter();
            try {
                writer.write(source);
            } finally {
                closeSilently(writer);
            }
            return javaFileObject;
        }

    }

}
