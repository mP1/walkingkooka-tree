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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionNumberContextTest implements ClassTesting<BasicExpressionNumberContext>, ToStringTesting<BasicExpressionNumberContext> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;
    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL32;

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionNumberContext.with(null, MATH_CONTEXT));
    }

    @Test
    public void testWithNullMathContextFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionNumberContext.with(KIND, null));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(BasicExpressionNumberContext.with(KIND, MATH_CONTEXT), KIND + " " + MATH_CONTEXT);
    }

    @Override
    public Class<BasicExpressionNumberContext> type() {
        return BasicExpressionNumberContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
