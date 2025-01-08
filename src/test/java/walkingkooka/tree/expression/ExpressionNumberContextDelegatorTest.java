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

import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.tree.expression.ExpressionNumberContextDelegatorTest.TestExpressionNumberContextDelegator;

import java.math.MathContext;

public class ExpressionNumberContextDelegatorTest implements ExpressionNumberContextTesting<TestExpressionNumberContextDelegator> {

    private final static ExpressionNumberContext EXPRESSION_NUMBER_CONTEXT = ExpressionNumberContexts.basic(
        ExpressionNumberKind.BIG_DECIMAL,
        DecimalNumberContexts.american(MathContext.DECIMAL32)
    );

    @Override
    public String currencySymbol() {
        return EXPRESSION_NUMBER_CONTEXT.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return EXPRESSION_NUMBER_CONTEXT.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return EXPRESSION_NUMBER_CONTEXT.exponentSymbol();
    }

    @Override
    public char groupSeparator() {
        return EXPRESSION_NUMBER_CONTEXT.groupSeparator();
    }

    @Override
    public MathContext mathContext() {
        return EXPRESSION_NUMBER_CONTEXT.mathContext();
    }

    @Override
    public char negativeSign() {
        return EXPRESSION_NUMBER_CONTEXT.negativeSign();
    }

    @Override
    public char percentageSymbol() {
        return EXPRESSION_NUMBER_CONTEXT.percentageSymbol();
    }

    @Override
    public char positiveSign() {
        return EXPRESSION_NUMBER_CONTEXT.positiveSign();
    }

    @Override
    public TestExpressionNumberContextDelegator createContext() {
        return new TestExpressionNumberContextDelegator();
    }

    static final class TestExpressionNumberContextDelegator implements ExpressionNumberContextDelegator {
        @Override
        public ExpressionNumberContext expressionNumberContext() {
            return EXPRESSION_NUMBER_CONTEXT;
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

    // class............................................................................................................

    @Override
    public Class<TestExpressionNumberContextDelegator> type() {
        return TestExpressionNumberContextDelegator.class;
    }
}
