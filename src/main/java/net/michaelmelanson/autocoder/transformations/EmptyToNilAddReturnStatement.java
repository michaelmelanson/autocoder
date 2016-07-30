package net.michaelmelanson.autocoder.transformations;

import com.shapesecurity.functional.data.ImmutableList;
import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.shift.ast.FunctionBody;
import com.shapesecurity.shift.ast.LiteralNullExpression;
import com.shapesecurity.shift.ast.ReturnStatement;
import net.michaelmelanson.autocoder.MappingReducer;
import net.michaelmelanson.autocoder.Transformation;

public class EmptyToNilAddReturnStatement implements Transformation {
    private final int position;

    public EmptyToNilAddReturnStatement(int position) {
        this.position = position;
    }

    @Override
    public MappingReducer toMappingReducer() {
        return new MappingReducer(this.position, (node) -> {
            FunctionBody body = (FunctionBody) node;

            return new FunctionBody(body.getDirectives(), ImmutableList.of(
                    new ReturnStatement(Maybe.of(new LiteralNullExpression()))));
        });

    }
}
