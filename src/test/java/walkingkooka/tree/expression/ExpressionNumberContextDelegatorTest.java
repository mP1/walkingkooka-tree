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
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.tree.expression.ExpressionNumberContextDelegatorTest.TestExpressionNumberContextDelegator;

import java.math.MathContext;

public class ExpressionNumberContextDelegatorTest implements ExpressionNumberContextTesting<TestExpressionNumberContextDelegator>,
    DecimalNumberContextDelegator {

    private final static ExpressionNumberContext EXPRESSION_NUMBER_CONTEXT = ExpressionNumberContexts.basic(
        ExpressionNumberKind.BIG_DECIMAL,
        DecimalNumberContexts.american(MathContext.DECIMAL32)
    );

    @Override
    public TestExpressionNumberContextDelegator createContext() {
        return new TestExpressionNumberContextDelegator();
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return EXPRESSION_NUMBER_CONTEXT;
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
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
