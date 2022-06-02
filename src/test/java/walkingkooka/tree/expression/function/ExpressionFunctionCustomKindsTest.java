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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionCustomKindsTest extends ExpressionFunctionTestCase<ExpressionFunctionCustomKinds<String, ExpressionEvaluationContext>, String> {

    private final static Set<ExpressionFunctionKind> KINDS = Sets.of(
            ExpressionFunctionKind.RESOLVE_REFERENCES
    );

    @Test
    public void testWithNullFunctionFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionCustomKinds.with(null, KINDS));
    }

    @Test
    public void testWithNullKindsFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionCustomKinds.with(wrapped(), null));
    }

    @Test
    public void testApply() {
        final List<Object> parameters = Lists.of(this);
        final ExpressionEvaluationContext context = this.createContext();
        this.applyAndCheck(this.createBiFunction(), parameters, context,
                this.wrapped().apply(parameters, context));
    }

    @Test
    public void testSetKindsSame() {
        final ExpressionFunctionCustomKinds<String, ExpressionEvaluationContext> function = this.createBiFunction();
        assertSame(function, function.setKinds(KINDS));
    }

    @Test
    public void testSetKindsDifferent() {
        final ExpressionFunctionCustomKinds<String, ExpressionEvaluationContext> function = this.createBiFunction();

        final Set<ExpressionFunctionKind> different = Sets.of(
                ExpressionFunctionKind.EVALUATE_PARAMETERS
        );
        final ExpressionFunction<String, ExpressionEvaluationContext> differentFunction = function.setKinds(different);

        assertNotSame(function, differentFunction);
        assertSame(different, differentFunction.kinds());
        this.checkEquals(KINDS, function.kinds());
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                this.wrapped().toString()
        );
    }

    @Override
    public ExpressionFunctionCustomKinds<String, ExpressionEvaluationContext> createBiFunction() {
        return Cast.to(
                ExpressionFunctionCustomKinds.with(wrapped(), KINDS)
        );
    }

    private ExpressionFunction<String, ExpressionEvaluationContext> wrapped() {
        return ExpressionFunctions.typeName();
    }

    @Override
    public Class<ExpressionFunctionCustomKinds<String, ExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionCustomKinds.class);
    }
}