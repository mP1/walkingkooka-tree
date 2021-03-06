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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ExpressionNumberKindTest implements ClassTesting<ExpressionNumberKind> {

    @Test
    public void testParseBigDecimal() {
        final String text = "12.3";
        assertEquals(ExpressionNumberKind.BIG_DECIMAL.create(new BigDecimal(text)), ExpressionNumberKind.BIG_DECIMAL.parse(text));
    }

    @Test
    public void testParseDouble() {
        final String text = "12.3";
        assertEquals(ExpressionNumberKind.DOUBLE.create(new Double(text)), ExpressionNumberKind.DOUBLE.parse(text));
    }

    @Override
    public Class<ExpressionNumberKind> type() {
        return ExpressionNumberKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
