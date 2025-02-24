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

package walkingkooka.tree.expression;

import org.junit.jupiter.api.Test;
import walkingkooka.ContextTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link ExpressionPurityContext}
 */
public interface ExpressionPurityContextTesting<C extends ExpressionPurityContext> extends ContextTesting<C> {

    @Test
    default void testIsPureNullNameFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().isPure(null));
    }

    default void isPureAndCheck(final ExpressionFunctionName name,
                                final boolean expected) {
        this.isPureAndCheck(this.createContext(), name, expected);
    }

    default void isPureAndCheck(final ExpressionPurityContext context,
                                final ExpressionFunctionName name,
                                final boolean expected) {
        this.checkEquals(
            expected,
            context.isPure(name),
            () -> "isPure " + name
        );
    }
}
