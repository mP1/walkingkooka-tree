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

package walkingkooka.tree;

import walkingkooka.test.Testing;
import walkingkooka.text.HasText;
import walkingkooka.text.HasTextOffset;

public interface HasTextOffsetTesting extends Testing {

    default void textOffsetAndCheck(final HasTextOffset has,
                                    final String before) {
        this.textOffsetAndCheck(has, before.length());
    }

    default void textOffsetAndCheck(final HasTextOffset has,
                                    final int offset) {
        this.checkEquals(offset,
                has.textOffset(),
                () -> (has instanceof HasText ? ((HasText) has).text() : has.toString()) + (has instanceof Node ? "\n" + ((Node<?, ?, ?, ?>) has).root() : ""));
    }
}
