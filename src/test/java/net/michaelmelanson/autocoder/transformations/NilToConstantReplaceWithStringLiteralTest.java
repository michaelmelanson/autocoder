package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.Script;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.parser.Parser;
import net.michaelmelanson.autocoder.Transformation;
import org.junit.Assert;
import org.junit.Test;

public class NilToConstantReplaceWithStringLiteralTest {

    @Test
    public void testToMappingReducer() throws Exception {
        Script script = Parser.parseScript("function foo(){return null}");
        Transformation transformation = new NilToConstantReplaceWithStringLiteral(2, "test");

        Script transformed = transformation.applyTo(script);

        Assert.assertEquals("function foo(){return\"test\"}", CodeGen.codeGen(transformed));
    }
}