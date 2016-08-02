package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.shift.ast.BinaryExpression;
import com.shapesecurity.shift.ast.IfStatement;
import com.shapesecurity.shift.ast.Statement;
import net.michaelmelanson.autocoder.InvalidTargetException;
import net.michaelmelanson.autocoder.MappingReducer;
import net.michaelmelanson.autocoder.Transformation;

public class UnconditionalToIfWrapInCondition implements Transformation {
    private final int target;
    private final BinaryExpression condition;

    public UnconditionalToIfWrapInCondition(int target, BinaryExpression condition) {
        this.target = target;
        this.condition = condition;
    }

    @Override
    public MappingReducer toMappingReducer() {
        return new MappingReducer(this.target, (node) -> {
            if (!Statement.class.isAssignableFrom(node.getClass())) throw new InvalidTargetException(node);
            return new IfStatement(
                    this.condition,
                    (Statement) node,
                    Maybe.<Statement>empty());
        });
    }
}
