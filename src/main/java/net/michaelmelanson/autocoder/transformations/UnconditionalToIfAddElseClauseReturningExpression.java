package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.shift.ast.Expression;
import com.shapesecurity.shift.ast.IfStatement;
import com.shapesecurity.shift.ast.ReturnStatement;
import net.michaelmelanson.autocoder.InvalidTargetException;
import net.michaelmelanson.autocoder.MappingReducer;
import net.michaelmelanson.autocoder.Transformation;

public class UnconditionalToIfAddElseClauseReturningExpression implements Transformation {
    private int target;
    private Expression returnExpression;

    public UnconditionalToIfAddElseClauseReturningExpression(int target, Expression returnExpression) {
        this.target = target;
        this.returnExpression = returnExpression;
    }

    @Override
    public MappingReducer toMappingReducer() {
        return new MappingReducer(this.target, (node) -> {
            if (!IfStatement.class.isAssignableFrom(node.getClass())) throw new InvalidTargetException(node);

            return new IfStatement(
                    ((IfStatement) node).test,
                    ((IfStatement) node).consequent,
                    Maybe.of(new ReturnStatement(Maybe.of(this.returnExpression)))
            );
        });
    }
}
