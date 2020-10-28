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
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorExpressionParserToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class Expression implements Node<Expression, FunctionExpressionName, Name, Object> {

    /**
     * An empty list that holds no children.
     */
    public final static List<Expression> NO_CHILDREN = Lists.empty();

    /**
     * Tests the value and creates the appropriate {@see Expression}.
     */
    public static Expression valueOrFail(final Object value) {
        Objects.requireNonNull(value, "value");

        return value instanceof ExpressionNumber ?
                expressionNumber((ExpressionNumber) value) :
                value instanceof Boolean ?
                        booleanExpression(Cast.to(value)) :
                        value instanceof LocalDate ?
                                localDate(Cast.to(value)) :
                                value instanceof LocalDateTime ?
                                        localDateTime(Cast.to(value)) :
                                        value instanceof LocalTime ?
                                                localTime(Cast.to(value)) :
                                                value instanceof String ?
                                                        string(Cast.to(value)) :
                                                        valueOrFailFail(value);
    }

    /**
     * Reports an unknown value type given to {@link #valueOrFail}
     */
    static Expression valueOrFailFail(final Object value) {
        throw new IllegalArgumentException("Unknown value " + CharSequences.quoteIfChars(value));
    }

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
     * {@see BooleanExpression}
     */
    public static BooleanExpression booleanExpression(final boolean value) {
        return BooleanExpression.with(value);
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
     * {@see ExpressionNumberExpression}
     */
    public static ExpressionNumberExpression expressionNumber(final ExpressionNumber value) {
        return ExpressionNumberExpression.with(value);
    }

    /**
     * {@see FunctionExpression}
     */
    public static FunctionExpression function(final FunctionExpressionName name, final List<Expression> expressions) {
        return FunctionExpression.with(name, expressions);
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
     * {@see LocalDateExpression}
     */
    public static LocalDateExpression localDate(final LocalDate value) {
        return LocalDateExpression.with(value);
    }

    /**
     * {@see LocalDateTimeExpression}
     */
    public static LocalDateTimeExpression localDateTime(final LocalDateTime value) {
        return LocalDateTimeExpression.with(value);
    }

    /**
     * {@see LocalTimeExpression}
     */
    public static LocalTimeExpression localTime(final LocalTime value) {
        return LocalTimeExpression.with(value);
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
     * {@see StringExpression}
     */
    public static StringExpression string(final String value) {
        return StringExpression.with(value);
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
     * Only {@link BooleanExpression} returns true
     */
    public final boolean isBoolean() {
        return this instanceof BooleanExpression;
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
     * Only {@link ExpressionNumberExpression} returns true
     */
    public final boolean isExpressionNumber() {
        return this instanceof ExpressionNumberExpression;
    }

    /**
     * Only {@link FunctionExpression} returns true
     */
    public final boolean isFunction() {
        return this instanceof FunctionExpression;
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
     * Only {@link LocalDateExpression} returns true
     */
    public final boolean isLocalDate() {
        return this instanceof LocalDateExpression;
    }

    /**
     * Only {@link LocalDateTimeExpression} returns true
     */
    public final boolean isLocalDateTime() {
        return this instanceof LocalDateTimeExpression;
    }

    /**
     * Only {@link LocalTimeExpression} returns true
     */
    public final boolean isLocalTime() {
        return this instanceof LocalTimeExpression;
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
     * Only {@link StringExpression} returns true
     */
    public final boolean isString() {
        return this instanceof StringExpression;
    }

    /**
     * Only {@link SubtractExpression} returns true
     */
    public final boolean isSubtract() {
        return this instanceof SubtractExpression;
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
     * Evaluates this node as a boolean
     */
    public abstract boolean toBoolean(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node as a {@link ExpressionNumber}
     */
    public abstract ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node as a {@link LocalDate}
     */
    public abstract LocalDate toLocalDate(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node as a {@link LocalDateTime}
     */
    public abstract LocalDateTime toLocalDateTime(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node as a {@link LocalTime}
     */
    public abstract LocalTime toLocalTime(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node as a {@link String}
     */
    public abstract String toString(final ExpressionEvaluationContext context);

    /**
     * Evaluates this node returning its value.
     */
    public abstract Object toValue(final ExpressionEvaluationContext context);

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
        return this.equalsAncestors(other) &&
                this.equalsDescendants0(other);
    }

    private boolean equalsAncestors(final Expression other) {
        boolean result = this.equalsIgnoringParentAndChildren(other);

        if (result) {
            final Optional<Expression> parent = this.parent();
            final Optional<Expression> otherParent = other.parent();
            final boolean hasParent = parent.isPresent();
            final boolean hasOtherParent = otherParent.isPresent();

            if (hasParent) {
                if (hasOtherParent) {
                    result = parent.get().equalsAncestors(otherParent.get());
                }
            } else {
                // result is only true if other is false
                result = !hasOtherParent;
            }
        }

        return result;
    }

    final boolean equalsDescendants(final Expression other) {
        return this.equalsIgnoringParentAndChildren(other) &&
                this.equalsDescendants0(other);
    }

    abstract boolean equalsDescendants0(final Expression other);

    /**
     * Sub classes should do equals but ignore the parent and children properties.
     */
    abstract boolean equalsIgnoringParentAndChildren(final Expression other);

    // Object .......................................................................................................

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b);
        return b.toString();
    }

    abstract void toString0(final StringBuilder b);

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
