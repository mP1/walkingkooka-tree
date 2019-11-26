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


import java.time.LocalDate;
import java.util.Objects;

/**
 * A {@link LocalDate} value.
 */
public final class LocalDateExpression extends ValueExpression<LocalDate> {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(LocalDateExpression.class);

    static LocalDateExpression with(final LocalDate value) {
        Objects.requireNonNull(value, "value");
        return new LocalDateExpression(NO_INDEX, value);
    }

    private LocalDateExpression(final int index, final LocalDate value) {
        super(index, value);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public LocalDateExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    LocalDateExpression replace1(final int index, final LocalDate value) {
        return new LocalDateExpression(index, value);
    }

    // Visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LocalDateExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
