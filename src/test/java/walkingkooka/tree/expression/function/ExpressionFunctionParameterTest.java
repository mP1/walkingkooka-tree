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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionParameterTest implements HashCodeEqualsDefinedTesting2<ExpressionFunctionParameter>, ToStringTesting<ExpressionFunctionParameter> {

    private final static ExpressionFunctionParameterName NAME = ExpressionFunctionParameterName.with("name");

    private final static Class<?> TYPE = String.class;

    @Test
    public void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionParameter.with(null, TYPE));
    }

    @Test
    public void testWithNullTypeFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionParameter.with(NAME, null));
    }

    @Test
    public void testDifferentName() {
        this.checkNotEquals(ExpressionFunctionParameter.with(ExpressionFunctionParameterName.with("different"), TYPE));
    }

    @Test
    public void testDifferentType() {
        this.checkNotEquals(ExpressionFunctionParameter.with(NAME, Integer.class));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(), "java.lang.String name");
    }

    @Override
    public ExpressionFunctionParameter createObject() {
        return ExpressionFunctionParameter.with(NAME, TYPE);
    }

    @Override
    public Class<ExpressionFunctionParameter> type() {
        return ExpressionFunctionParameter.class;
    }
}
