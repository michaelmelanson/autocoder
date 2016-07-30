package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.shift.ast.IdentifierExpression;
import net.michaelmelanson.autocoder.MappingReducer;
import net.michaelmelanson.autocoder.Transformation;

public class ConstantToScalarReplaceWithIdentifier implements Transformation {
    private final int target;
    private final String identifier;

    public ConstantToScalarReplaceWithIdentifier(int target, String identifier) {
        this.target = target;
        this.identifier = identifier;
    }

    @Override
    public MappingReducer toMappingReducer() {
        return new MappingReducer(this.target, (node) -> new IdentifierExpression(this.identifier));
    }
}
