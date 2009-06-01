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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
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
 * <li>{@link #setLocale(String)}</li>
 * <li>{@link #setLocale(Locale)}</li>
 * <li>{@link #setCharset(String)}</li>
 * <li>{@link #setCharset(Charset)}</li>
 * <li>{@link #setOut(Writer)}</li>
 * <li>{@link #addSourcePath(String...)}</li>
 * <li>{@link #addSourcePath(File...)}</li>
 * <li>{@link #addOption(String...)}</li>
 * <li>{@link #addProcessor(Processor...)}</li>
 * <li>{@link #addCompilationUnit(String)}</li>
 * <li>{@link #addCompilationUnit(Class)}</li>
 * <li>{@link #addCompilationUnit(String, CharSequence)}</li>
 * <li>{@link #addCompilationUnit(Class, CharSequence)}</li>
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
 * <li>{@link #getTypeMirror(Class)}</li>
 * <li>{@link #getGeneratedSource(String)}</li>
 * <li>{@link #getGeneratedSource(Class)}</li>
 * </ul>
 * </dd>
 * <dt>{@link #compile()} 後に以下のメソッドを呼び出して検証することができます．</dt>
 * <dd>
 * <ul>
 * <li>{@link #assertEqualsGeneratedSource(CharSequence, String)}</li>
 * <li>{@link #assertEqualsGeneratedSource(CharSequence, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(String, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(String, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(File, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(File, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(String, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(String, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(URL, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(URL, Class)}</li>
 * </ul>
 * </dd>
 * </dl>
 * <p>
 * {@link #compile()} を呼び出した後に状態をリセットしてコンパイル前の状態に戻すには， {@link #reset()} を呼び出します．
 * </p>
 *
 * <p>
 * 次のサンプルは， <code>src/test/java</code> フォルダにある <code>TestSource.java</code>
 * をコンパイルし， <code>TestProcessor</code> が生成する <code>foo.bar.Baz</code>
 * クラスのソースを検証するテストクラスです．
 * </p>
 *
 * <pre>
 * public class XxxProcessorTest extends AptinaTestCase {
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
 *         final TestProcessor processor = new TestProcessor();
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
    protected void setLocale(final String locale) {
        setLocale(new Locale(locale));
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
        setCharset(Charset.forName(charset));
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
    protected void addSourcePath(final String... sourcePaths) {
        checkNotNull("sourcePaths", sourcePaths);
        checkNotEmpty("sourcePaths", sourcePaths);
        for (final String path : sourcePaths) {
            this.sourcePaths.add(new File(path));
        }
    }

    /**
     * コンパイル時に参照するソースパスを追加します．
     *
     * @param sourcePaths
     *            コンパイル時に参照するソースパスの並び
     */
    protected void addSourcePath(final File... sourcePaths) {
        checkNotNull("sourcePaths", sourcePaths);
        checkNotEmpty("sourcePaths", sourcePaths);
        this.sourcePaths.addAll(Arrays.asList(sourcePaths));
    }

    /**
     * コンパイラオプションを追加します．
     *
     * @param options
     *            形式のコンパイラオプションの並び
     */
    protected void addOption(final String... options) {
        checkNotNull("options", options);
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
        checkNotNull("processors", processors);
        checkNotEmpty("processors", processors);
        this.processors.addAll(Arrays.asList(processors));
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
        checkNotNull("className", className);
        compilationUnits.add(new FileCompilationUnit(className));
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
     * コンパイル対象のクラスをソースとともに追加します．
     *
     * @param className
     *            コンパイル対象クラスの完全限定名
     * @param source
     *            ソース
     */
    protected void addCompilationUnit(final String className,
            final CharSequence source) {
        checkNotNull("className", className);
        checkNotNull("source", source);
        compilationUnits.add(new InMemoryCompilationUnit(className, source
                .toString()));
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
        checkNotNull("source", source);
        addCompilationUnit(clazz.getName(), source);
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
     *
     * @param clazz
     *            クラス
     * @return クラスに対応する{@link TypeElement}
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
     * クラスに対応する {@link TypeMirror} を返します．
     *
     * @param clazz
     *            クラス
     * @return クラスに対応する{@link TypeMirror}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeMirror getTypeMirror(final Class<?> clazz)
            throws IllegalStateException {
        checkNotNull("clazz", clazz);
        checkCompiled();
        return getTypeElement(clazz).asType();
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
        checkNotNull("className", className);
        checkCompiled();
        final JavaFileObject javaFileObject = testingJavaFileManager
                .getJavaFileForInput(StandardLocation.SOURCE_OUTPUT, className,
                        Kind.SOURCE);
        if (javaFileObject == null) {
            throw new SourceNotGeneratedException(className);
        }
        final CharSequence content = javaFileObject.getCharContent(false);
        if (content == null) {
            throw new SourceNotGeneratedException(className);
        }
        return content.toString();
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
        checkNotNull("expected", expected);
        checkNotNull("className", className);
        checkCompiled();
        assertEquals(className, expected.toString(),
                getGeneratedSource(className));
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
        checkNotNull("expected", expected);
        checkNotNull("clazz", clazz);
        checkCompiled();
        assertEqualsGeneratedSource(expected, clazz.getName());
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
        checkNotNull("expectedSourceFilePath", expectedSourceFilePath);
        checkNotNull("className", className);
        checkCompiled();
        assertEqualsGeneratedSourceWithFile(new File(expectedSourceFilePath),
                className);
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
        checkNotNull("expectedSourceFilePath", expectedSourceFilePath);
        checkNotNull("clazz", clazz);
        checkCompiled();
        assertEqualsGeneratedSourceWithFile(expectedSourceFilePath, clazz
                .getName());
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
        checkNotNull("className", className);
        checkCompiled();
        assertEqualsGeneratedSource(readFromFile(expectedSourceFile), className);
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
        checkNotNull("expectedResource", expectedResource);
        checkNotNull("className", className);
        checkCompiled();
        final URL url = Thread.currentThread().getContextClassLoader()
                .getResource(expectedResource);
        if (url == null) {
            throw new FileNotFoundException(expectedResource);
        }
        assertEqualsGeneratedSourceWithResource(url, className);
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
        checkNotNull("expectedResource", expectedResource);
        checkNotNull("clazz", clazz);
        checkCompiled();
        assertEqualsGeneratedSourceWithResource(clazz.getName(),
                expectedResource);
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
        checkNotNull("className", className);
        checkCompiled();
        assertEqualsGeneratedSource(readFromResource(expectedResourceUrl),
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
     * @param array
     *            配列
     * @throws IllegalArgumentException
     *             配列が {@literal null} の場合
     */
    void checkNotEmpty(final String name, final Object[] array)
            throws IllegalArgumentException {
        if (array.length == 0) {
            throw new IllegalArgumentException(name + " must not be empty");
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
