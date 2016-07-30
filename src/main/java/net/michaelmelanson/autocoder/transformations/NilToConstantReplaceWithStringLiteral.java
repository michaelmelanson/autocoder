package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.LiteralStringExpression;
import net.michaelmelanson.autocoder.MappingReducer;
import net.michaelmelanson.autocoder.Transformation;

public class NilToConstantReplaceWithStringLiteral implements Transformation {
    private final int target;
    private final String literal;

    public NilToConstantReplaceWithStringLiteral(int target, String literal) {
        this.target = target;
        this.literal = literal;
    }

    @Override
    public MappingReducer toMappingReducer() {
        return new MappingReducer(this.target, node -> new LiteralStringExpression(this.literal));
    }
}
