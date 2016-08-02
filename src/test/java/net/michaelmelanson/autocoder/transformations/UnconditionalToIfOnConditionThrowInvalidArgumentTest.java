package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.Script;
import com.shapesecurity.shift.ast.operators.BinaryOperator;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.parser.Parser;
import net.michaelmelanson.autocoder.Transformation;
import org.junit.Assert;
import org.junit.Test;

public class UnconditionalToIfOnConditionThrowInvalidArgumentTest {

    @Test
    public void testToMappingReducer() throws Exception {
        Script script = Parser.parseScript("function foo(s){return s}");
        Transformation transformation = new UnconditionalToIfOnConditionThrowInvalidArgument(5, "s", BinaryOperator.LessThan, 1.0);

        Script transformed = transformation.applyTo(script);

        Assert.assertEquals("function foo(s){if(s<1)throw new InvalidArgument();return s}", CodeGen.codeGen(transformed));
    }
}