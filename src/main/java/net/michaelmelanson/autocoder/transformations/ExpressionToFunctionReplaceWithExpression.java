package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.Expression;
import com.shapesecurity.shift.ast.LiteralStringExpression;
import net.michaelmelanson.autocoder.InvalidTargetException;
import net.michaelmelanson.autocoder.MappingReducer;
import net.michaelmelanson.autocoder.Transformation;

public class ExpressionToFunctionReplaceWithExpression implements Transformation {
    private int target;
    private Expression expression;

    public ExpressionToFunctionReplaceWithExpression(int target, Expression expression) {
        this.target = target;
        this.expression = expression;
    }

    @Override
    public MappingReducer toMappingReducer() {
        return new MappingReducer(this.target, (node) -> {
            if (!LiteralStringExpression.class.isAssignableFrom(node.getClass()))
                throw new InvalidTargetException(node);
            return this.expression;
        });
    }
}
