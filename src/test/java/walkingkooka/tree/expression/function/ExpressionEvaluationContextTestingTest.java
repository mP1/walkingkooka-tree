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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Either;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContextDelegator;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextDelegator;
import walkingkooka.environment.EnvironmentContexts;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContextTesting;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FakeExpressionReference;
import walkingkooka.tree.expression.function.ExpressionEvaluationContextTestingTest.TestExpressionEvaluationContext;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ExpressionEvaluationContextTestingTest implements ExpressionEvaluationContextTesting<TestExpressionEvaluationContext>,
    DecimalNumberContextDelegator {

    private final static String UNKNOWN_REFERENCE_MESSAGE = "Unknown reference 123";

    @Test
    public void testReferenceFails() {
        this.referenceFails(
            new TestExpressionEvaluationContext(),
            new FakeExpressionReference(),
            new IllegalArgumentException(UNKNOWN_REFERENCE_MESSAGE)
        );
    }

    @Override
    public void testEnterScopeGivesDifferentInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEvaluateExpressionUnknownFunctionNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRemoveEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetIndentationWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetTimeOffsetWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestExpressionEvaluationContext createContext() {
        return new TestExpressionEvaluationContext();
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DECIMAL_NUMBER_CONTEXT;
    }

    @Override
    public int decimalNumberDigitCount() {
        return DECIMAL_NUMBER_CONTEXT.decimalNumberDigitCount();
    }

    @Override
    public MathContext mathContext() {
        return DECIMAL_NUMBER_CONTEXT.mathContext();
    }

    private final static DecimalNumberContext DECIMAL_NUMBER_CONTEXT = DecimalNumberContexts.american(MathContext.DECIMAL32);

    @Override
    public Class<TestExpressionEvaluationContext> type() {
        return TestExpressionEvaluationContext.class;
    }

    final static class TestExpressionEvaluationContext implements ExpressionEvaluationContext,
        DateTimeContextDelegator,
        DecimalNumberContextDelegator,
        EnvironmentContextDelegator,
        LocaleContextDelegator {

        @Override
        public boolean canNumbersHaveGroupSeparator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long dateOffset() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Indentation indentation() {
            return Indentation.SPACES2;
        }

        @Override
        public char valueSeparator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean canConvert(final Object value,
                                  final Class<?> type) {
            return false;
        }

        @Override
        public <T> Either<T, String> convert(final Object value,
                                             final Class<T> tye) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
            Objects.requireNonNull(scoped, "scoped");

            throw new UnsupportedOperationException();
        }

        @Override
        public Object evaluate(final String expression) {
            Objects.requireNonNull(expression, "expression");
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                      final Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object handleException(final RuntimeException exception) {
            return null;
        }

        @Override
        public Optional<Optional<Object>> reference(final ExpressionReference reference) {
            Objects.requireNonNull(reference, "reference");

            throw new IllegalArgumentException(UNKNOWN_REFERENCE_MESSAGE);
        }

        @Override
        public boolean isText(final Object value) {
            return false;
        }

        @Override
        public CaseSensitivity stringEqualsCaseSensitivity() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isPure(ExpressionFunctionName name) {
            Objects.requireNonNull(name, "name");

            throw new UnsupportedOperationException();
        }

        @Override
        public ExpressionNumberKind expressionNumberKind() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
            Objects.requireNonNull(name, "name");

            throw new UnsupportedOperationException();
        }

        @Override
        public DateTimeContext dateTimeContext() {
            final LocaleContext localeContext = this.localeContext();
            final Locale locale = localeContext.locale();

            return DateTimeContexts.basic(
                localeContext.dateTimeSymbolsForLocale(locale)
                    .get(),
                locale,
                1950, // defaultYear
                50, // twoDigitYear
                () -> LocalDateTime.MIN
            );
        }

        @Override
        public DecimalNumberContext decimalNumberContext() {
            return DECIMAL_NUMBER_CONTEXT;
        }

        @Override
        public MathContext mathContext() {
            return DECIMAL_NUMBER_CONTEXT.mathContext();
        }

        @Override
        public LocaleContext localeContext() {
            return LocaleContexts.jre(Locale.FRANCE);
        }

        @Override
        public Locale locale() {
            return this.environmentContext()
                .locale();
        }

        // EnvironmentContextDelegator..................................................................................

        @Override
        public ExpressionEvaluationContext cloneEnvironment() {
            return new TestExpressionEvaluationContext();
        }

        @Override
        public void removeEnvironmentValue(final EnvironmentValueName<?> environmentValueName) {
            Objects.requireNonNull(environmentValueName, "environmentValueName");
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> void setEnvironmentValue(final EnvironmentValueName<T> environmentValueName,
                                            final T reference) {
            Objects.requireNonNull(environmentValueName, "environmentValueName");
            Objects.requireNonNull(reference, "reference");

            throw new UnsupportedOperationException();
        }

        @Override
        public TestExpressionEvaluationContext setEnvironmentContext(final EnvironmentContext environmentContext) {
            Objects.requireNonNull(environmentContext, "environmentContext");
            return new TestExpressionEvaluationContext();
        }

        @Override
        public void setIndentation(final Indentation indentation) {
            this.environmentContext.setIndentation(indentation);
        }
        
        @Override
        public void setLineEnding(final LineEnding lineEnding) {
            this.environmentContext.setLineEnding(lineEnding);
        }

        @Override
        public void setLocale(final Locale locale) {
            this.environmentContext.setLocale(locale);
        }

        @Override
        public void setUser(final Optional<EmailAddress> user) {
            this.environmentContext.setUser(user);
        }

        @Override
        public LocalDateTime now() {
            return this.environmentContext.now();
        }

        @Override
        public EnvironmentContext environmentContext() {
            return this.environmentContext;
        }

        private final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                Indentation.SPACES2,
                LineEnding.NL,
                Locale.ENGLISH,
                () -> LocalDateTime.MIN,
                EnvironmentContext.ANONYMOUS
            )
        );

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
