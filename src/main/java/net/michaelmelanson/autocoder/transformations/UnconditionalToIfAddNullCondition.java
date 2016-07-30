package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.functional.data.ImmutableList;
import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.shift.ast.*;
import com.shapesecurity.shift.ast.operators.BinaryOperator;
import net.michaelmelanson.autocoder.MappingReducer;
import net.michaelmelanson.autocoder.Transformation;

public class UnconditionalToIfAddNullCondition implements Transformation {
    private final int target;
    private final String parameter;
    private final String returnLiteral;

    public UnconditionalToIfAddNullCondition(int target, String parameter, String returnLiteral) {
        this.target = target;
        this.parameter = parameter;
        this.returnLiteral = returnLiteral;
    }

    @Override
    public MappingReducer toMappingReducer() {
        return new MappingReducer(this.target, (node) -> {
            FunctionBody body = (FunctionBody) node;

            return new FunctionBody(
                    body.getDirectives(),
                    ImmutableList.cons(
                            new IfStatement(
                                    new BinaryExpression(
                                            BinaryOperator.StrictEqual,
                                            new IdentifierExpression(this.parameter),
                                            new LiteralNullExpression()
                                    ),
                                    new ReturnStatement(
                                            Maybe.of(new LiteralStringExpression(this.returnLiteral))),
                                    Maybe.empty()),
                            body.getStatements())
            );
        });
    }

}
