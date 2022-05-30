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

package walkingkooka.tree.select.parser;

import walkingkooka.ToStringBuilder;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@link NodeSelectorParserContext} without any functionality.
 */
final class BasicNodeSelectorParserContext implements NodeSelectorParserContext {

    /**
     * Creates a new {@link }
     */
    static BasicNodeSelectorParserContext with(final ExpressionNumberKind kind,
                                               final MathContext mathContext) {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(mathContext, "mathContext");

        return new BasicNodeSelectorParserContext(
                kind,
                mathContext
        );
    }

    private BasicNodeSelectorParserContext(final ExpressionNumberKind kind,
                                           final MathContext mathContext) {
        super();
        this.kind = kind;
        this.mathContext = mathContext;
    }

    @Override
    public List<String> ampms() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String currencySymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char decimalSeparator() {
        return '.';
    }

    @Override
    public int defaultYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String exponentSymbol() {
        return "E";
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.kind;
    }

    private final ExpressionNumberKind kind;

    @Override
    public char groupingSeparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MathContext mathContext() {
        return this.mathContext;
    }

    private final MathContext mathContext;

    @Override
    public char negativeSign() {
        return '-';
    }

    @Override
    public List<String> monthNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> monthNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDateTime now() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char percentageSymbol() {
        return '%';
    }

    @Override
    public char positiveSign() {
        return '+';
    }

    @Override
    public int twoDigitYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> weekDayNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> weekDayNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("decimalSeparator").value(this.decimalSeparator())
                .label("exponentSymbol").value(this.exponentSymbol())
                .label("negativeSign").value(this.negativeSign())
                .label("percentageSymbol").value(this.percentageSymbol())
                .label("positiveSign").value(this.positiveSign())
                .build();
    }
}
