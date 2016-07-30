package net.michaelmelanson.autocoder;

import com.google.common.collect.ImmutableList;
import com.shapesecurity.shift.ast.Script;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.parser.JsError;
import com.shapesecurity.shift.parser.Parser;
import net.michaelmelanson.autocoder.transformations.EmptyToNilAddReturnStatement;
import net.michaelmelanson.autocoder.transformations.NilToConstantReplaceWithStringLiteral;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class AutocoderMain {
    public static void main(String[] args) throws JsError {
        ScriptEngineManager factory = new ScriptEngineManager();

        ScriptEngine engine = factory.getEngineByName("nashorn");

        Script script = Parser.parseScript("function wordWrap(s, length) {}");


        System.out.println("Initial code:");
        System.out.println(CodeGen.codeGen(script));

        List<Transformation> transformations = ImmutableList.of(
                new EmptyToNilAddReturnStatement(4),
                new NilToConstantReplaceWithStringLiteral(4, "")
        );

        final Script[] output = {script};
        final int[] i = {0};

        transformations.stream().forEachOrdered((transformation) -> {
            output[0] = transformation.applyTo(output[0]);

            System.out.println("After step " + ++i[0] + ": ");
            System.out.println(CodeGen.codeGen(output[0]));
        });


        final String transformedSource = CodeGen.codeGen(output[0]);

        System.out.println("Transformed code: ");
        System.out.println(transformedSource);

        try {
            engine.eval(transformedSource);
        } catch (final ScriptException ex) {
            ex.printStackTrace();
        }
    }

}
