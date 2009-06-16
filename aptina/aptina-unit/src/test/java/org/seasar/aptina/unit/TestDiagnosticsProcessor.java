package org.seasar.aptina.unit;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

/**
 * 
 * @author koichik
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.seasar.aptina.unit.Hoge")
public class TestDiagnosticsProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
        final Messager messager = processingEnv.getMessager();
        messager.printMessage(Kind.NOTE, "hoge");
        for (final TypeElement annotation : annotations) {
            for (final TypeElement typeElement : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(annotation))) {
                messager.printMessage(Kind.ERROR, "foo", typeElement);
                messager.printMessage(Kind.WARNING, "bar", typeElement);
            }
        }
        return false;
    }

}
