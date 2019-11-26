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


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A {@link LocalDateTime} number value.
 */
public final class LocalDateTimeExpression extends ValueExpression<LocalDateTime> {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(LocalDateTimeExpression.class);

    static LocalDateTimeExpression with(final LocalDateTime value) {
        Objects.requireNonNull(value, "value");
        return new LocalDateTimeExpression(NO_INDEX, value);
    }

    private LocalDateTimeExpression(final int index, final LocalDateTime value) {
        super(index, value);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public LocalDateTimeExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    LocalDateTimeExpression replace1(final int index, final LocalDateTime value) {
        return new LocalDateTimeExpression(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LocalDateTimeExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
