package net.michaelmelanson.autocoder;

import com.shapesecurity.functional.data.ImmutableList;
import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.functional.data.NonEmptyImmutableList;
import com.shapesecurity.shift.ast.*;
import com.shapesecurity.shift.codegen.CodeGen;
import net.michaelmelanson.autocoder.transformations.EmptyToNilAddReturnStatement;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class AutocoderMain {
    public static void main(String[] args) {
        ScriptEngineManager factory = new ScriptEngineManager();

        ScriptEngine engine = factory.getEngineByName("nashorn");

        final NonEmptyImmutableList<Statement> program = ImmutableList.of(
                new FunctionDeclaration(
                        new BindingIdentifier("wordWrap"),
                        false,
                        new FormalParameters(ImmutableList.of(
                                new BindingIdentifier("s"),
                                new BindingIdentifier("length")
                        ), Maybe.empty()),
                        new FunctionBody(ImmutableList.empty(), ImmutableList.empty())
                ));

        Script script = new Script(ImmutableList.empty(), program);


        System.out.println("Code generated: ");
        System.out.println(CodeGen.codeGen(script));

        Transformation transformation = new EmptyToNilAddReturnStatement(4);

        Script transformed = transformation.applyTo(script);

        final String transformedSource = CodeGen.codeGen(transformed);

        System.out.println("Transformed code: ");
        System.out.println(transformedSource);

        try {
            engine.eval(transformedSource);
        } catch (final ScriptException ex) {
            ex.printStackTrace();
        }
    }

}
