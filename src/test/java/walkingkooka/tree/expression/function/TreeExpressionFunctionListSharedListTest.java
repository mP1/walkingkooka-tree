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
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TreeExpressionFunctionListSharedListTest extends TreeExpressionFunctionListSharedTestCase<TreeExpressionFunctionListSharedList<FakeExpressionEvaluationContext>,
    FakeExpressionEvaluationContext> {

    // apply............................................................................................................

    @Test
    public void testApplyEmpty() {
        final List<Object> list = Lists.empty();

        this.applyAndCheck(
            list,
            list
        );
    }

    @Test
    public void testApplyIncludesNullElement() {
        final List<Object> list = Lists.of(
            10,
            null,
            20,
            null
        );

        this.applyAndCheck(
            list,
            list
        );
    }

    @Test
    public void testApply() {
        final List<Object> list = Lists.of(10, 20);

        this.applyAndCheck(
            list,
            list
        );
    }

    // helpers..........................................................................................................

    @Override
    public TreeExpressionFunctionListSharedList<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeExpressionFunctionListSharedList.instance();
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {

            @Override
            public FakeExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
                return new FakeExpressionEvaluationContext() {

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        checkEquals(ExpressionNumber.class, target, "target");

                        return this.successfulConversion(
                            ExpressionNumberKind.DOUBLE.create((Number) value),
                            target
                        );
                    }

                    @Override
                    public boolean isText(final Object value) {
                        return value instanceof String;
                    }

                    @Override
                    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                        return scoped.apply(reference);
                    }
                };
            }
        };
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "list"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionListSharedList<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionListSharedList.class);
    }
}
