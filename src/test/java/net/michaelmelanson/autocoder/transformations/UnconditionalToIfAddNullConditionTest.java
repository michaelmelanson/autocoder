package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.Script;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.parser.Parser;
import net.michaelmelanson.autocoder.Transformation;
import org.junit.Assert;
import org.junit.Test;

public class UnconditionalToIfAddNullConditionTest {

    @Test
    public void testToMappingReducer() throws Exception {
        Script script = Parser.parseScript("function foo(s){return s}");
        Transformation transformation = new UnconditionalToIfAddNullCondition(5, "s", "foo");

        Script transformed = transformation.applyTo(script);

        Assert.assertEquals("function foo(s){if(s===null)return\"foo\";return s}", CodeGen.codeGen(transformed));

    }
}