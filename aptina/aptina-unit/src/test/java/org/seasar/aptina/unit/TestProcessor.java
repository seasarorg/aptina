package org.seasar.aptina.unit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

/**
 *
 * @author koichik
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.seasar.aptina.unit.Hoge")
public class TestProcessor extends AbstractProcessor {

    boolean called;

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        if (called) {
            return false;
        }
        called = true;
        processingEnv.getMessager().printMessage(Kind.OTHER, "hoge");
        try {
            final Filer filer = processingEnv.getFiler();
            final JavaFileObject file = filer.createSourceFile("foo.bar.Baz",
                    null);
            final PrintWriter writer = new PrintWriter(file.openWriter());
            writer.print("package foo.bar;");
            writer.print("public class Baz {");
            writer.print("}");
            writer.close();
        } catch (final IOException ignore) {
        }
        return false;
    }

}
