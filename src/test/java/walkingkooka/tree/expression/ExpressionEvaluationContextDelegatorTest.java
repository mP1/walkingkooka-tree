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

import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContextDelegatorTest.TestExpressionEvaluationContextDelegator;

import java.math.MathContext;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class ExpressionEvaluationContextDelegatorTest implements ExpressionEvaluationContextTesting<TestExpressionEvaluationContextDelegator>,
    DecimalNumberContextDelegator {

    @Override
    public void testEvaluateExpressionUnknownFunctionNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEnterScopeGivesDifferentInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestExpressionEvaluationContextDelegator createContext() {
        return new TestExpressionEvaluationContextDelegator();
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public MathContext mathContext() {
        return new TestExpressionEvaluationContextDelegator()
            .mathContext();
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return new TestExpressionEvaluationContextDelegator();
    }

    // class............................................................................................................

    @Override
    public Class<TestExpressionEvaluationContextDelegator> type() {
        return TestExpressionEvaluationContextDelegator.class;
    }

    static final class TestExpressionEvaluationContextDelegator implements ExpressionEvaluationContextDelegator {

        @Override
        public Optional<Optional<Object>> reference(final ExpressionReference reference) {
            return this.expressionEvaluationContext().reference(reference);
        }

        @Override
        public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
            Objects.requireNonNull(scoped, "scoped");

            throw new UnsupportedOperationException();
        }

        @Override
        public ExpressionEvaluationContext expressionEvaluationContext() {
            final Locale locale = Locale.ENGLISH;

            return ExpressionEvaluationContexts.basic(
                ExpressionNumberKind.BIG_DECIMAL,
                (fn) -> {
                    Objects.requireNonNull(fn, "fn");
                    throw new UnsupportedOperationException();
                },
                (rr) -> {
                    Objects.requireNonNull(rr, "rr");
                    throw rr;
                },
                (ref) -> {
                    Objects.requireNonNull(ref, "ref");
                    throw new UnsupportedOperationException();
                },
                ExpressionEvaluationContexts.referenceNotFound(),
                CaseSensitivity.SENSITIVE,
                ConverterContexts.basic(
                    0,
                    Converters.fake(),
                    DateTimeContexts.basic(
                        DateTimeSymbols.fromDateFormatSymbols(
                            new DateFormatSymbols(locale)
                        ),
                        locale,
                        1900,
                        50,
                        LocalDateTime::now
                    ),
                    DecimalNumberContexts.american(MathContext.DECIMAL32)
                ),
                LocaleContexts.jre(locale)
            );
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
