package net.michaelmelanson.autocoder;

import com.google.common.collect.Lists;
import com.shapesecurity.functional.data.ImmutableList;
import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.shift.ast.*;
import com.shapesecurity.shift.visitor.Reducer;
import org.jetbrains.annotations.NotNull;

import java.lang.Class;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class MappingReducer implements Reducer<Node> {
    final Logger logger = Logger.getLogger(getClass().getName());

    private final int target;
    private final Function<Node, Node> mapper;
    private Stack<Node> stack = new Stack<>();
    private int index = 0;

    public MappingReducer(int target, Function<Node, Node> mapper) {
        this.target = target;
        this.mapper = mapper;
    }

    private Node visit(@NotNull Node node, Supplier<Node> factory) {
        logger.fine("Visiting node " + node);
        Node result;

        if (index++ == this.target) {
            result = this.mapper.apply(node);
            logger.fine("Replacing node " + node + " with " + result);
        } else {
            result = factory.get();
        }

        this.stack.push(result);
        logger.fine("Pushed " + result + " to the stack:" + this.stack);
        return result;
    }

    private <T> ImmutableList<T> popN(int count, Class<T> clazz) {
        int remaining = count;
        List<T> items = new ArrayList<>();
        while (remaining-- > 0 && !this.stack.empty() && clazz.isAssignableFrom(this.stack.peek().getClass())) {
            //noinspection unchecked
            final T node = (T) this.stack.pop();
            logger.fine("Popped " + node + " from the stack: " + this.stack);
            items.add(node);
        }
        return ImmutableList.from(Lists.reverse(items));
    }


    private <T> Maybe<T> maybePop(Class<T> clazz) {
        if (!this.stack.empty() && clazz.isAssignableFrom(this.stack.peek().getClass())) {
            //noinspection unchecked
            final T node = (T) this.stack.pop();
            logger.fine("Popped " + node + "  from the stack: " + this.stack);
            return Maybe.of(node);
        } else {
            logger.fine("Nope! Couldn't pop anything.");
            return Maybe.empty();
        }
    }

    @NotNull
    @Override
    public Node reduceArrayBinding(@NotNull ArrayBinding node, @NotNull ImmutableList<Maybe<Node>> elements, @NotNull Maybe<Node> restElement) {
        //noinspection unchecked
        ImmutableList<Maybe<BindingBindingWithDefault>> newElements = popN(elements.length, (Class) Maybe.class);
        Maybe<Binding> newRestElement = restElement.isNothing() ? Maybe.empty() : maybePop(Binding.class);

        return visit(node, () -> new ArrayBinding(newElements, newRestElement));
    }

    @NotNull
    @Override
    public Node reduceArrayExpression(@NotNull ArrayExpression node, @NotNull ImmutableList<Maybe<Node>> elements) {
        ImmutableList<Maybe<SpreadElementExpression>> newElements = ImmutableList.from(Maybe.of((SpreadElementExpression) this.stack.pop()));
        return visit(node, () -> new ArrayExpression(newElements));
    }

    @NotNull
    @Override
    public Node reduceArrowExpression(@NotNull ArrowExpression node, @NotNull Node params, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceAssignmentExpression(@NotNull AssignmentExpression node, @NotNull Node binding, @NotNull Node expression) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceBinaryExpression(@NotNull BinaryExpression node, @NotNull Node left, @NotNull Node right) {
        Expression newRight = maybePop(Expression.class).fromJust();
        Expression newLeft = maybePop(Expression.class).fromJust();

        return visit(node, () -> new BinaryExpression(node.getOperator(), newLeft, newRight));

    }

    @NotNull
    @Override
    public Node reduceBindingIdentifier(@NotNull BindingIdentifier node) {
        return visit(node, () -> new BindingIdentifier(node.getName()));
    }

    @NotNull
    @Override
    public Node reduceBindingPropertyIdentifier(@NotNull BindingPropertyIdentifier node, @NotNull Node binding, @NotNull Maybe<Node> init) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceBindingPropertyProperty(@NotNull BindingPropertyProperty node, @NotNull Node name, @NotNull Node binding) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceBindingWithDefault(@NotNull BindingWithDefault node, @NotNull Node binding, @NotNull Node init) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceBlock(@NotNull Block node, @NotNull ImmutableList<Node> statements) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceBlockStatement(@NotNull BlockStatement node, @NotNull Node block) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceBreakStatement(@NotNull BreakStatement node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceCallExpression(@NotNull CallExpression node, @NotNull Node callee, @NotNull ImmutableList<Node> arguments) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceCatchClause(@NotNull CatchClause node, @NotNull Node binding, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceClassDeclaration(@NotNull ClassDeclaration node, @NotNull Node name, @NotNull Maybe<Node> _super, @NotNull ImmutableList<Node> elements) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceClassElement(@NotNull ClassElement node, @NotNull Node method) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceClassExpression(@NotNull ClassExpression node, @NotNull Maybe<Node> name, @NotNull Maybe<Node> _super, @NotNull ImmutableList<Node> elements) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceCompoundAssignmentExpression(@NotNull CompoundAssignmentExpression node, @NotNull Node binding, @NotNull Node expression) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceComputedMemberExpression(@NotNull ComputedMemberExpression node, @NotNull Node object, @NotNull Node expression) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceComputedPropertyName(@NotNull ComputedPropertyName node, @NotNull Node expression) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceConditionalExpression(@NotNull ConditionalExpression node, @NotNull Node test, @NotNull Node consequent, @NotNull Node alternate) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceContinueStatement(@NotNull ContinueStatement node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceDataProperty(@NotNull DataProperty node, @NotNull Node value, @NotNull Node name) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceDebuggerStatement(@NotNull DebuggerStatement node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceDirective(@NotNull Directive node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceDoWhileStatement(@NotNull DoWhileStatement node, @NotNull Node body, @NotNull Node test) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceEmptyStatement(@NotNull EmptyStatement node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceExport(@NotNull Export node, @NotNull Node declaration) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceExportAllFrom(@NotNull ExportAllFrom node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceExportDefault(@NotNull ExportDefault node, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceExportFrom(@NotNull ExportFrom node, @NotNull ImmutableList<Node> namedExports) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceExportSpecifier(@NotNull ExportSpecifier node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceExpressionStatement(@NotNull ExpressionStatement node, @NotNull Node expression) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceForInStatement(@NotNull ForInStatement node, @NotNull Node left, @NotNull Node right, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceForOfStatement(@NotNull ForOfStatement node, @NotNull Node left, @NotNull Node right, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceForStatement(@NotNull ForStatement node, @NotNull Maybe<Node> init, @NotNull Maybe<Node> test, @NotNull Maybe<Node> update, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceFormalParameters(@NotNull FormalParameters node, @NotNull ImmutableList<Node> items, @NotNull Maybe<Node> rest) {
        ImmutableList<BindingBindingWithDefault> newItems = popN(items.length, BindingBindingWithDefault.class);
        Maybe<BindingIdentifier> newRest = rest.isNothing() ? Maybe.empty() : maybePop(BindingIdentifier.class);

        return visit(node, () -> new FormalParameters(newItems, newRest));
    }

    @NotNull
    @Override
    public Node reduceFunctionBody(@NotNull FunctionBody node, @NotNull ImmutableList<Node> directives, @NotNull ImmutableList<Node> statements) {
        ImmutableList<Statement> newStatements = popN(statements.length, Statement.class);
        ImmutableList<Directive> newDirectives = popN(directives.length, Directive.class);

        return visit(node, () -> new FunctionBody(newDirectives, newStatements));
    }

    @NotNull
    @Override
    public Node reduceFunctionDeclaration(@NotNull FunctionDeclaration node, @NotNull Node name, @NotNull Node params, @NotNull Node body) {
        Maybe<FunctionBody> newBody = maybePop(FunctionBody.class);
        Maybe<FormalParameters> newParams = maybePop(FormalParameters.class);

        return visit(node, () -> new FunctionDeclaration(node.getName(), node.getIsGenerator(), newParams.fromJust(), newBody.fromJust()));
    }

    @NotNull
    @Override
    public Node reduceFunctionExpression(@NotNull FunctionExpression node, @NotNull Maybe<Node> name, @NotNull Node params, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceGetter(@NotNull Getter node, @NotNull Node name, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceIdentifierExpression(@NotNull IdentifierExpression node) {
        return visit(node, () -> new IdentifierExpression(node.getName()));
    }

    @NotNull
    @Override
    public Node reduceIfStatement(@NotNull IfStatement node, @NotNull Node test, @NotNull Node consequent, @NotNull Maybe<Node> alternate) {
        Maybe<Statement> newAlternate = alternate.isJust() ? maybePop(Statement.class) : Maybe.empty();
        Statement newConsequent = maybePop(Statement.class).fromJust();
        Expression newTest = maybePop(Expression.class).fromJust();

        return visit(node, () -> new IfStatement(newTest, newConsequent, newAlternate));
    }

    @NotNull
    @Override
    public Node reduceImport(@NotNull Import node, @NotNull Maybe<Node> defaultBinding, @NotNull ImmutableList<Node> namedImports) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceImportNamespace(@NotNull ImportNamespace node, @NotNull Maybe<Node> defaultBinding, @NotNull Node namespaceBinding) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceImportSpecifier(@NotNull ImportSpecifier node, @NotNull Node binding) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceLabeledStatement(@NotNull LabeledStatement node, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceLiteralBooleanExpression(@NotNull LiteralBooleanExpression node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceLiteralInfinityExpression(@NotNull LiteralInfinityExpression node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceLiteralNullExpression(@NotNull LiteralNullExpression node) {
        return visit(node, LiteralNullExpression::new);
    }

    @NotNull
    @Override
    public Node reduceLiteralNumericExpression(@NotNull LiteralNumericExpression node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceLiteralRegExpExpression(@NotNull LiteralRegExpExpression node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceLiteralStringExpression(@NotNull LiteralStringExpression node) {
        return visit(node, () -> new LiteralStringExpression(node.value));
    }

    @NotNull
    @Override
    public Node reduceMethod(@NotNull Method node, @NotNull Node name, @NotNull Node params, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceModule(@NotNull Module node, @NotNull ImmutableList<Node> directives, @NotNull ImmutableList<Node> items) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceNewExpression(@NotNull NewExpression node, @NotNull Node callee, @NotNull ImmutableList<Node> arguments) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceNewTargetExpression(@NotNull NewTargetExpression node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceObjectBinding(@NotNull ObjectBinding node, @NotNull ImmutableList<Node> properties) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceObjectExpression(@NotNull ObjectExpression node, @NotNull ImmutableList<Node> properties) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceReturnStatement(@NotNull ReturnStatement node, @NotNull Maybe<Node> expression) {
        final Maybe<Expression> newExpression = expression.isNothing() ? Maybe.empty() : maybePop(Expression.class);
        return visit(node, () -> new ReturnStatement(newExpression));
    }

    @NotNull
    @Override
    public Node reduceScript(@NotNull Script node, @NotNull ImmutableList<Node> directives, @NotNull ImmutableList<Node> statements) {
        ImmutableList<Directive> newDirectives = popN(directives.length, Directive.class);
        ImmutableList<Statement> newStatements = popN(statements.length, Statement.class);
        return visit(node, () -> new Script(newDirectives, newStatements));
    }

    @NotNull
    @Override
    public Node reduceSetter(@NotNull Setter node, @NotNull Node name, @NotNull Node param, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceShorthandProperty(@NotNull ShorthandProperty node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceSpreadElement(@NotNull SpreadElement node, @NotNull Node expression) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceStaticMemberExpression(@NotNull StaticMemberExpression node, @NotNull Node object) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceStaticPropertyName(@NotNull StaticPropertyName node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceSuper(@NotNull Super node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceSwitchCase(@NotNull SwitchCase node, @NotNull Node test, @NotNull ImmutableList<Node> consequent) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceSwitchDefault(@NotNull SwitchDefault node, @NotNull ImmutableList<Node> consequent) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceSwitchStatement(@NotNull SwitchStatement node, @NotNull Node discriminant, @NotNull ImmutableList<Node> cases) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceSwitchStatementWithDefault(@NotNull SwitchStatementWithDefault node, @NotNull Node discriminant, @NotNull ImmutableList<Node> preDefaultCases, @NotNull Node defaultCase, @NotNull ImmutableList<Node> postDefaultCases) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceTemplateElement(@NotNull TemplateElement node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceTemplateExpression(@NotNull TemplateExpression node, @NotNull Maybe<Node> tag, @NotNull ImmutableList<Node> elements) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceThisExpression(@NotNull ThisExpression node) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceThrowStatement(@NotNull ThrowStatement node, @NotNull Node expression) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceTryCatchStatement(@NotNull TryCatchStatement node, @NotNull Node block, @NotNull Node catchClause) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceTryFinallyStatement(@NotNull TryFinallyStatement node, @NotNull Node block, @NotNull Maybe<Node> catchClause, @NotNull Node finalizer) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceUnaryExpression(@NotNull UnaryExpression node, @NotNull Node operand) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceUpdateExpression(@NotNull UpdateExpression node, @NotNull Node operand) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceVariableDeclaration(@NotNull VariableDeclaration node, @NotNull ImmutableList<Node> declarators) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceVariableDeclarationStatement(@NotNull VariableDeclarationStatement node, @NotNull Node declaration) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceVariableDeclarator(@NotNull VariableDeclarator node, @NotNull Node binding, @NotNull Maybe<Node> init) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceWhileStatement(@NotNull WhileStatement node, @NotNull Node test, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceWithStatement(@NotNull WithStatement node, @NotNull Node object, @NotNull Node body) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceYieldExpression(@NotNull YieldExpression node, @NotNull Maybe<Node> expression) {
        throw new RuntimeException("Not implemented.");
    }

    @NotNull
    @Override
    public Node reduceYieldGeneratorExpression(@NotNull YieldGeneratorExpression node, @NotNull Node expression) {
        throw new RuntimeException("Not implemented.");
    }
}
