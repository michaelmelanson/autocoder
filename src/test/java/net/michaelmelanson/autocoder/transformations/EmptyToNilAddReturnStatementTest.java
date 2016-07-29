package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.functional.data.ImmutableList;
import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.functional.data.NonEmptyImmutableList;
import com.shapesecurity.shift.ast.*;
import com.shapesecurity.shift.codegen.CodeGen;
import com.shapesecurity.shift.visitor.Director;
import net.michaelmelanson.autocoder.Transformation;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class EmptyToNilAddReturnStatementTest {

    @Test
    public void testWordWrapKata() {
        Script script = wordWrapKata();

        Transformation transformation = new EmptyToNilAddReturnStatement(4);

        Script transformed = transformation.applyTo(script);

        Assert.assertEquals("function wordWrap(s, length){return null}", CodeGen.codeGen((Script) transformed));
    }

    @NotNull
    private static Script wordWrapKata() {
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

        return new Script(ImmutableList.empty(), program);
    }
}