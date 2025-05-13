/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.math.DecimalNumberContext;

import java.math.MathContext;
import java.util.Locale;
import java.util.Objects;

final class BasicExpressionNumberContext implements ExpressionNumberContext {

    static BasicExpressionNumberContext with(final ExpressionNumberKind expressionNumberKind,
                                             final DecimalNumberContext decimalNumberContext) {
        Objects.requireNonNull(expressionNumberKind, "expressionNumberKind");
        Objects.requireNonNull(decimalNumberContext, "decimalNumberContext");

        return new BasicExpressionNumberContext(expressionNumberKind, decimalNumberContext);
    }

    private BasicExpressionNumberContext(final ExpressionNumberKind expressionNumberKind,
                                         final DecimalNumberContext decimalNumberContext) {
        this.expressionNumberKind = expressionNumberKind;
        this.decimalNumberContext = decimalNumberContext;
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.expressionNumberKind;
    }

    private final ExpressionNumberKind expressionNumberKind;

    // DecimalNumberContext............................................................................................

    @Override
    public String currencySymbol() {
        return this.decimalNumberContext.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.decimalNumberContext.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return this.decimalNumberContext.exponentSymbol();
    }

    @Override
    public char groupSeparator() {
        return this.decimalNumberContext.groupSeparator();
    }

    @Override
    public char percentSymbol() {
        return this.decimalNumberContext.percentSymbol();
    }

    @Override
    public char negativeSign() {
        return this.decimalNumberContext.negativeSign();
    }

    @Override
    public char positiveSign() {
        return this.decimalNumberContext.positiveSign();
    }

    @Override
    public Locale locale() {
        return this.decimalNumberContext.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.decimalNumberContext.mathContext();
    }

    private final DecimalNumberContext decimalNumberContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.expressionNumberKind + " " + this.decimalNumberContext;
    }
}
