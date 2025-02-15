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

import walkingkooka.test.Testing;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.TreePrintable;

/**
 * A mixin interface with tests and helpers to assist in testing a {@link ExpressionPurity}
 */
public interface ExpressionPurityTesting extends Testing {

    default void isPureAndCheck(final ExpressionPurity purity,
                                final ExpressionPurityContext context,
                                final boolean expected) {
        this.checkEquals(
            expected,
            purity.isPure(context),
            () -> purity instanceof TreePrintable ?
                ((TreePrintable) purity).treeToString(Indentation.SPACES2, LineEnding.NL) :
                purity.toString()
        );
    }
}