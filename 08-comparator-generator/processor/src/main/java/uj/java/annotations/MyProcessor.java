package uj.java.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({"uj.java.annotations.MyComparable"})
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            annotatedElements.forEach(this::processElement);
        }
        return true;
    }
    private void processElement(Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + e);
        TypeElement clazz = (TypeElement) e;
        String className = clazz.getQualifiedName().toString();
        try {
            JavaFileObject file = processingEnv.getFiler().createSourceFile(className + "Assistant");
            String packageName = packageName(className);
            try (PrintWriter out = new PrintWriter(file.openWriter())) {
                if (packageName != null) {
                    out.write("package " + packageName + ";\n");
                }
                out.write("class " + clazz.getSimpleName() + "Comparator {");
                out.write("public int compare(" + className + " a, " + className + " b) {");
                out.write("int result;");

                final List<? extends Element> enclosedElements = e.getEnclosedElements();
                forgeList(enclosedElements, out);

                out.write("return 0;");
                out.write("}"); //do compare
                out.write("}");//do klasy
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    static void forgeList(List<? extends Element> list, PrintWriter out){
        list.stream()
                .collect(Collectors.toMap(element -> element, MyProcessor::writePriority))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .filter((element) -> element.getKey().getKind().isField())
                .filter((element) -> element.getKey().asType().getKind().isPrimitive())
                .filter((element) -> !element.getKey().getModifiers().contains(Modifier.PRIVATE))
                .forEach(o -> writeCompares(o.getKey(), out));

    }

    static int writePriority(Element element){
        if (element.getAnnotation(ComparePriority.class) != null){
            return element.getAnnotation(ComparePriority.class).value();
        } else {
            return Integer.MAX_VALUE;
        }
    }


    private static void writeCompares(Element e, PrintWriter out) {
        out.write("result = " + findType(e) + ".compare(a." + e.getSimpleName() + ", b." + e.getSimpleName() + ");");
        out.write("if (result != 0) {return result;}");
    }

    static String findType(Element element){
        return switch (element.toString()) {
            case "charField" -> "Character";
            case "intField" -> "Integer";
            case "boolField" -> "Boolean";
            case "floatField" -> "Float";
            case "doubleField" -> "Double";
            case "byteField" -> "Byte";
            case "shortField" -> "Short";
            case "longField" -> "Long";
            default -> "";
        };
    }

    private String packageName(String className) {
        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }
        return packageName;
    }
}
