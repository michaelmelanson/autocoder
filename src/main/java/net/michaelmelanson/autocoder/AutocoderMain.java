package net.michaelmelanson.autocoder;

import com.google.common.collect.ImmutableList;
import com.shapesecurity.shift.ast.*;
import com.shapesecurity.shift.ast.operators.BinaryOperator;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.codegen.PrettyCodeGen;
import com.shapesecurity.shift.parser.JsError;
import com.shapesecurity.shift.parser.Parser;
import net.michaelmelanson.autocoder.transformations.*;

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
                new NilToConstantReplaceWithStringLiteral(4, ""),
                new ConstantToScalarReplaceWithIdentifier(4, "s"),
                new UnconditionalToIfOnConditionReturnLiteral(6, "s", ""),
                new UnconditionalToIfOnConditionThrowInvalidArgument(12, "length", BinaryOperator.LessThan, 1.0),
                new UnconditionalToIfWrapInCondition(18, new BinaryExpression(
                        BinaryOperator.LessThanEqual,
                        new CallExpression(new StaticMemberExpression("length", new IdentifierExpression("s")), com.shapesecurity.functional.data.ImmutableList.empty()),
                        new IdentifierExpression("length"))),
                new UnconditionalToIfAddElseClauseReturningExpression(24, new LiteralStringExpression("long\nword")),
                new ExpressionToFunctionReplaceWithExpression(24, new BinaryExpression(
                        BinaryOperator.Plus,
                        new BinaryExpression(
                                BinaryOperator.Plus,
                                new CallExpression(new StaticMemberExpression("substring", new IdentifierExpression("s")), com.shapesecurity.functional.data.ImmutableList.of(new LiteralNumericExpression(0.0), new IdentifierExpression("length"))),
                                new LiteralStringExpression("\n")
                        ),
                        new CallExpression(new StaticMemberExpression("substring", new IdentifierExpression("s")), com.shapesecurity.functional.data.ImmutableList.of(new IdentifierExpression("length"))))

                )
        );

        final Script[] output = {script};
        final int[] i = {0};

        transformations.stream().forEachOrdered((transformation) -> {
            System.out.println();
            System.out.println("Step " + ++i[0] + ": Applying " + transformation + "...");
            output[0] = transformation.applyTo(output[0]);

            System.out.println("Result: " + CodeGen.codeGen(output[0]));
        });


        final String transformedSource = PrettyCodeGen.codeGen(output[0]);

        System.out.println("Transformed code: " + transformedSource);

        try {
            engine.eval(transformedSource);
        } catch (final ScriptException ex) {
            ex.printStackTrace();
        }
    }

}
