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
import walkingkooka.collect.list.Lists;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ExpressionNumberFunction2TestCase<F extends ExpressionNumberFunction2> extends ExpressionFunctionTestCase<F, Number> {

    ExpressionNumberFunction2TestCase() {
        super();
    }

    @Test
    public final void testZeroArgumentsFails() {
        this.applyAndFail();
    }

    @Test
    public final void testTwoArgumentsFails() {
        this.applyAndFail(1, 2);
    }

    private void applyAndFail(final Object... parameters) {
        assertThrows(IllegalArgumentException.class, () -> this.apply2(parameters));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createBiFunction(), this.functionToString());
    }

    abstract String functionToString();

    final void applyAndCheck3(final Number number) {
        this.applyAndCheck3(number, number);
    }

    final void applyAndCheck3(final Number number, final Number expected) {
        this.applyAndCheck2(Lists.of(number), expected);
    }
}
