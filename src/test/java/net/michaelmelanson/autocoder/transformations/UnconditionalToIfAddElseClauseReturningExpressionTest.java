package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.BinaryExpression;
import com.shapesecurity.shift.ast.IdentifierExpression;
import com.shapesecurity.shift.ast.Script;
import com.shapesecurity.shift.ast.operators.BinaryOperator;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.parser.Parser;
import net.michaelmelanson.autocoder.Transformation;
import org.junit.Assert;
import org.junit.Test;

public class UnconditionalToIfAddElseClauseReturningExpressionTest {

    @Test
    public void testToMappingReducer() throws Exception {
        Script script = Parser.parseScript("function foo(s){if(s===0)return\"\";}");
        Transformation transformation = new UnconditionalToIfAddElseClauseReturningExpression(8, new BinaryExpression(BinaryOperator.Plus, new IdentifierExpression("a"), new IdentifierExpression("b")));

        Script transformed = transformation.applyTo(script);

        Assert.assertEquals("function foo(s){if(s===0)return\"\";else return a+b}", CodeGen.codeGen(transformed));

    }

}