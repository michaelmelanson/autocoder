package net.michaelmelanson.autocoder;

import com.shapesecurity.shift.ast.Node;

public class InvalidTargetException extends RuntimeException {
    private final Node node;

    public InvalidTargetException(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "InvalidTargetException(" + node + ")";
    }
}
