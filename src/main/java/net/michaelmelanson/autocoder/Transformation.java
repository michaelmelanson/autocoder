package net.michaelmelanson.autocoder;

import com.shapesecurity.shift.ast.Script;
import com.shapesecurity.shift.visitor.Director;

public interface Transformation {

    MappingReducer toMappingReducer();

    default Script applyTo(Script script) {
        return (Script) Director.reduceScript(this.toMappingReducer(), script);
    };
}
