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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.tree.expression.ExpressionEvaluationReferenceException;
import walkingkooka.tree.expression.ExpressionReference;

import java.lang.reflect.Method;

public class ExpressionFunctionContextsTest implements PublicStaticHelperTesting<ExpressionFunctionContexts> {

    @Test
    public void testReferenceNotFound() {
        final ExpressionReference reference = new ExpressionReference() {
            @Override
            public String toString() {
                return "Reference123";
            }
        };

        final ExpressionEvaluationReferenceException thrown = (ExpressionEvaluationReferenceException) ExpressionFunctionContexts.referenceNotFound()
                .apply(reference);

        this.checkEquals(
                reference,
                thrown.expressionReference(),
                () -> reference.toString()
        );


        this.checkEquals(
                "Reference not found: Reference123",
                thrown.getMessage(),
                () -> reference.toString()
        );
    }

    @Override
    public Class<ExpressionFunctionContexts> type() {
        return ExpressionFunctionContexts.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
