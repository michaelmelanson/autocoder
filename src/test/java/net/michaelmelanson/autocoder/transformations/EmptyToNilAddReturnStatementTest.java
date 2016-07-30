package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.Script;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.parser.JsError;
import com.shapesecurity.shift.parser.Parser;
import net.michaelmelanson.autocoder.Transformation;
import org.junit.Assert;
import org.junit.Test;

public class EmptyToNilAddReturnStatementTest {

    @Test
    public void testWordWrapKata() throws JsError {
        Script script = Parser.parseScript("function wordWrap(s, length) {}");
        Transformation transformation = new EmptyToNilAddReturnStatement(4);

        Script transformed = transformation.applyTo(script);

        Assert.assertEquals("function wordWrap(s,length){return null}", CodeGen.codeGen(transformed));
    }
}