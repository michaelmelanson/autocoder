package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.functional.data.ImmutableList;
import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.shift.ast.*;
import com.shapesecurity.shift.ast.operators.BinaryOperator;
import net.michaelmelanson.autocoder.MappingReducer;
import net.michaelmelanson.autocoder.Transformation;

public class UnconditionalToIfOnConditionThrowInvalidArgument implements Transformation {

    private final int target;
    private final String identifier;
    private final BinaryOperator operator;
    private final Double value;

    public UnconditionalToIfOnConditionThrowInvalidArgument(int target, String identifier, BinaryOperator operator, Double value) {
        this.target = target;
        this.identifier = identifier;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public MappingReducer toMappingReducer() {
        return new MappingReducer(this.target, (node) -> new FunctionBody(
                ((FunctionBody) node).getDirectives(),
                ImmutableList.cons(
                        new IfStatement(
                                new BinaryExpression(
                                        this.operator,
                                        new IdentifierExpression(this.identifier),
                                        new LiteralNumericExpression(this.value)
                                ),
                                new ThrowStatement(
                                        new NewExpression(
                                                // hack; can't figure out how to get it to add the brackets
                                                new IdentifierExpression("InvalidArgument()"),
                                                ImmutableList.empty())),
                                Maybe.empty()),
                        ((FunctionBody) node).getStatements())
        ));
    }
}
