package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.Script;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.parser.JsError;
import com.shapesecurity.shift.parser.Parser;
import net.michaelmelanson.autocoder.Transformation;
import org.junit.Assert;
import org.junit.Test;

public class ConstantToScalarReplaceWithIdentifierTest {

    @Test
    public void testWorksProperly() throws JsError {
        Script script = Parser.parseScript("function foo(a){return null}");
        Transformation transformation = new ConstantToScalarReplaceWithIdentifier(3, "a");

        Script transformed = transformation.applyTo(script);

        Assert.assertEquals("function foo(a){return a}", CodeGen.codeGen(transformed));

    }
}