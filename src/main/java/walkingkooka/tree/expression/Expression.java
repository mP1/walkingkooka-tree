/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.expression;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorExpressionParserToken;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * There are many sub classes of {@link Expression} representing different concepts typically found with an expression
 * language, from values, to arithmetic or functions.
 * <br>
 * The provided <code>toXXX</code> methods along with the required {@link ExpressionEvaluationContext} support a
 * non turing complete language. A sub class {@link ExpressionVisitor} will need to be
 * authored, to enable typical language features such as <code>if</code> and other similar conditional statements or
 * <code>return</code>.
 * <br>
 * The {@link Expression} system is used to provide the runtime expression evaluation for the xpath like implementation
 * and spreadsheet formula evaluation.
 */
public abstract class Expression implements Node<Expression, FunctionExpressionName, Name, Object>,
        ExpressionPurity,
        HasText,
        TreePrintable {

    /**
     * An empty list that holds no children.
     */
    public final static List<Expression> NO_CHILDREN = Lists.empty();

    // ........................................................................................

    /**
     * {@see AddExpression}
     */
    public static AddExpression add(final Expression left, final Expression right) {
        return AddExpression.with(left, right);
    }

    /**
     * {@see AndExpression}
     */
    public static AndExpression and(final Expression left, final Expression right) {
        return AndExpression.with(left, right);
    }

    /**
     * {@see CallExpression}
     */
    public static CallExpression call(final Expression callable,
                                      final List<Expression> parameters) {
        return CallExpression.with(
                callable,
                parameters
        );
    }

    /**
     * {@see DivideExpression}
     */
    public static DivideExpression divide(final Expression left, final Expression right) {
        return DivideExpression.with(left, right);
    }

    /**
     * {@see EqualsExpression}
     */
    public static EqualsExpression equalsExpression(final Expression left, final Expression right) {
        return EqualsExpression.with(left, right);
    }

    /**
     * {@see GreaterThanExpression}
     */
    public static GreaterThanExpression greaterThan(final Expression left, final Expression right) {
        return GreaterThanExpression.with(left, right);
    }

    /**
     * {@see GreaterThanEqualsExpression}
     */
    public static GreaterThanEqualsExpression greaterThanEquals(final Expression left, final Expression right) {
        return GreaterThanEqualsExpression.with(left, right);
    }

    /**
     * {@see LambdaFunctionExpression}
     */
    public static LambdaFunctionExpression lambdaFunction(final List<ExpressionFunctionParameter<?>> parameters,
                                                          final Expression value) {
        return LambdaFunctionExpression.with(
                parameters,
                value
        );
    }

    /**
     * {@see LessThanExpression}
     */
    public static LessThanExpression lessThan(final Expression left, final Expression right) {
        return LessThanExpression.with(left, right);
    }

    /**
     * {@see LessThanEqualsExpression}
     */
    public static LessThanEqualsExpression lessThanEquals(final Expression left, final Expression right) {
        return LessThanEqualsExpression.with(left, right);
    }

    /**
     * {@see ModuloExpression}
     */
    public static ModuloExpression modulo(final Expression left, final Expression right) {
        return ModuloExpression.with(left, right);
    }

    /**
     * {@see MultiplyExpression}
     */
    public static MultiplyExpression multiply(final Expression left, final Expression right) {
        return MultiplyExpression.with(left, right);
    }

    /**
     * {@see NamedFunctionExpression}
     */
    public static NamedFunctionExpression namedFunction(final FunctionExpressionName name) {
        return NamedFunctionExpression.with(
                name
        );
    }

    /**
     * {@see NegativeExpression}
     */
    public static NegativeExpression negative(final Expression expression) {
        return NegativeExpression.with(expression);
    }

    /**
     * {@see NotExpression}
     */
    public static NotExpression not(final Expression expression) {
        return NotExpression.with(expression);
    }

    /**
     * {@see NotEqualsExpression}
     */
    public static NotEqualsExpression notEquals(final Expression left, final Expression right) {
        return NotEqualsExpression.with(left, right);
    }

    /**
     * {@see ExpressionOrNode}
     */
    public static OrExpression or(final Expression left, final Expression right) {
        return OrExpression.with(left, right);
    }

    /**
     * {@see PowerExpression}
     */
    public static PowerExpression power(final Expression left, final Expression right) {
        return PowerExpression.with(left, right);
    }

    /**
     * {@see ReferenceExpression}
     */
    public static ReferenceExpression reference(final ExpressionReference reference) {
        return ReferenceExpression.with(reference);
    }

    /**
     * {@see SubtractExpression}
     */
    public static SubtractExpression subtract(final Expression left, final Expression right) {
        return SubtractExpression.with(left, right);
    }

    /**
     * {@see ValueExpression}
     */
    public static <V> ValueExpression<V> value(final V value) {
        return ValueExpression.with(value);
    }

    /**
     * {@see XorExpression}
     */
    public static XorExpression xor(final Expression left, final Expression right) {
        return XorExpression.with(left, right);
    }

    /**
     * {@see ListExpression}
     */
    public static ListExpression list(final List<Expression> expressions) {
        return ListExpression.with(expressions);
    }

    private final static Optional<Expression> NO_PARENT = Optional.empty();

    /**
     * Package private ctor to limit sub classing.
     */
    Expression(final int index) {
        this.parent = NO_PARENT;
        this.index = index;
    }

    // parent ..................................................................................................

    @Override
    public final Optional<Expression> parent() {
        return this.parent;
    }

    /**
     * This setter is used to recreate the entire graph including parents of parents receiving new children.
     * It is only ever called by a parent node and is used to adopt new children.
     */
    final Expression setParent(final Optional<Expression> parent, final int index) {
        final Expression copy = this.replace(index);
        copy.parent = parent;
        return copy;
    }

    private Optional<Expression> parent;

    /**
     * Sub classes should call this and cast.
     */
    final Expression removeParent0() {
        return this.isRoot() ?
                this :
                this.replace(NO_INDEX);
    }

    /**
     * Sub classes must create a new copy of the parent and replace the identified child using its index or similar,
     * and also sets its parent after creation, returning the equivalent child at the same index.
     */
    abstract Expression setChild(final Expression newChild);

    /**
     * Only ever called after during the completion of a setChildren, basically used to recreate the parent graph
     * containing this child.
     */
    final Expression replaceChild(final Optional<Expression> previousParent) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .setChild(this)
                        .children()
                        .get(this.index()) :
                this;
    }

    // index........................................................................................................

    @Override
    public final int index() {
        return this.index;
    }

    final int index;

    abstract Expression replace(final int index);

    // attributes.......................................................................................................

    @Override
    public final Map<Name, Object> attributes() {
        return Maps.empty();
    }

    @Override
    public final Expression setAttributes(final Map<Name, Object> attributes) {
        throw new UnsupportedOperationException();
    }

    // is...............................................................................................................

    /**
     * Only {@link AddExpression} returns true
     */
    public final boolean isAdd() {
        return this instanceof AddExpression;
    }

    /**
     * Only {@link AndExpression} returns true
     */
    public final boolean isAnd() {
        return this instanceof AndExpression;
    }

    /**
     * Only {@link CallExpression} returns true
     */
    public final boolean isCall() {
        return this instanceof CallExpression;
    }

    /**
     * Only {@link DivideExpression} returns true
     */
    public final boolean isDivide() {
        return this instanceof DivideExpression;
    }

    /**
     * Only {@link EqualsExpression} returns true
     */
    public final boolean isEquals() {
        return this instanceof EqualsExpression;
    }

    /**
     * Only {@link GreaterThanExpression} returns true
     */
    public final boolean isGreaterThan() {
        return this instanceof GreaterThanExpression;
    }

    /**
     * Only {@link GreaterThanEqualsExpression} returns true
     */
    public final boolean isGreaterThanEquals() {
        return this instanceof GreaterThanEqualsExpression;
    }

    /**
     * Only {@link LambdaFunctionExpression} returns true
     */
    public final boolean isLambdaFunction() {
        return this instanceof LambdaFunctionExpression;
    }

    /**
     * Only {@link LessThanExpression} returns true
     */
    public final boolean isLessThan() {
        return this instanceof LessThanExpression;
    }

    /**
     * Only {@link LessThanEqualsExpression} returns true
     */
    public final boolean isLessThanEquals() {
        return this instanceof LessThanEqualsExpression;
    }

    /**
     * Only {@link ListExpression} returns true
     */
    public final boolean isList() {
        return this instanceof ListExpression;
    }

    /**
     * Only {@link ModuloExpression} returns true
     */
    public final boolean isModulo() {
        return this instanceof ModuloExpression;
    }

    /**
     * Only {@link MultiplyExpression} returns true
     */
    public final boolean isMultiply() {
        return this instanceof MultiplyExpression;
    }

    /**
     * Only {@link NamedFunctionExpression} returns true
     */
    public final boolean isNamedFunction() {
        return this instanceof NamedFunctionExpression;
    }

    /**
     * Only {@link NegativeExpression} returns true
     */
    public final boolean isNegative() {
        return this instanceof NegativeExpression;
    }

    /**
     * Only {@link NotExpression} returns true
     */
    public final boolean isNot() {
        return this instanceof NotExpression;
    }

    /**
     * Only {@link NotEqualsExpression} returns true
     */
    public final boolean isNotEquals() {
        return this instanceof NotEqualsExpression;
    }

    /**
     * Only {@link OrExpression} returns true
     */
    public final boolean isOr() {
        return this instanceof OrExpression;
    }

    /**
     * Only {@link PowerExpression} returns true
     */
    public final boolean isPower() {
        return this instanceof PowerExpression;
    }

    /**
     * Only {@link ReferenceExpression} returns true
     */
    public final boolean isReference() {
        return this instanceof ReferenceExpression;
    }

    /**
     * Only {@link SubtractExpression} returns true
     */
    public final boolean isSubtract() {
        return this instanceof SubtractExpression;
    }

    /**
     * Only {@link ValueExpression} returns true
     */
    public final boolean isValue() {
        return this instanceof ValueExpression;
    }

    /**
     * Only {@link XorExpression} returns true
     */
    public final boolean isXor() {
        return this instanceof XorExpression;
    }

    // helper............................................................................................................

    final <T extends Expression> T cast() {
        return Cast.to(this);
    }

    // Visitor............................................................................................................

    abstract void accept(final ExpressionVisitor visitor);

    // Eval................................................................................................................

    /**
     * Prepares this {@link Expression} as a {@link ExpressionFunction} ready to be executed.
     */
    abstract ExpressionFunction<?, ExpressionEvaluationContext> function(final ExpressionEvaluationContext context);

    /**
     * Factory that returns a {@link ExpressionFunction} that expects no parameters and calls {@link Expression#toValue(ExpressionEvaluationContext)}.
     */
    final ExpressionFunction<?, ExpressionEvaluationContext> toValueFunction() {
        return ExpressionExpressionFunction.with(this);
    }

    /**
     * Evaluates this node as a boolean
     */
    public abstract boolean toBoolean(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node as a {@link ExpressionNumber}
     */
    public abstract ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node as a {@link String}
     */
    public abstract String toString(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node returning its {@link ExpressionReference} or value. For all sub classes except {@link ReferenceExpression}
     * will be identical to {@link #toValue(ExpressionEvaluationContext)}.
     */
    public abstract Object toReferenceOrValue(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node returning its value.
     */
    public abstract Object toValue(final ExpressionEvaluationContext context);

    // TreePrintable...................................................................................................

    @Override
    public abstract void printTree(final IndentingPrinter printer); // seemed to solve j2cl complaining about inconsistent hierarchy.

    /**
     * Returns the type name helper that will appear in {@link TreePrintable} output.
     */
    final String typeName() {
        return this.getClass().getSimpleName();
    }

    // Object .......................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final Expression other) {
        return this.equalsIgnoringChildren(other) &&
                this.equalsChildren(other);
    }

    /**
     * This method is only implemented by {@link LeafExpression} which returns true and {@link ParentExpression}
     * which iterates over its children.
     */
    abstract boolean equalsChildren(final Expression other);

    /**
     * Sub classes should check other properties ignoring any children.
     */
    abstract boolean equalsIgnoringChildren(final Expression other);

    // Object .......................................................................................................

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b);
        return b.toString();
    }

    abstract void toString0(final StringBuilder b);

    // HasText.. .......................................................................................................

    @Override
    public String text() {
        return this.toString();
    }

    // NodeSelector .......................................................................................................

    /**
     * {@see NodeSelector#absolute}
     */
    public static NodeSelector<Expression, FunctionExpressionName, Name, Object> absoluteNodeSelector() {
        return NodeSelector.absolute();
    }

    /**
     * {@see NodeSelector#relative}
     */
    public static NodeSelector<Expression, FunctionExpressionName, Name, Object> relativeNodeSelector() {
        return NodeSelector.relative();
    }

    /**
     * Creates a {@link NodeSelector} for {@link Expression} from a {@link NodeSelectorExpressionParserToken}.
     */
    public static NodeSelector<Expression, FunctionExpressionName, Name, Object> nodeSelectorExpressionParserToken(final NodeSelectorExpressionParserToken token,
                                                                                                                   final Predicate<FunctionExpressionName> functions) {
        return NodeSelector.parserToken(token,
                n -> FunctionExpressionName.with(n.value()),
                functions,
                Expression.class);
    }
}
