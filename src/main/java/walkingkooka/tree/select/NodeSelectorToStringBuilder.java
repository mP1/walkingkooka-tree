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

package walkingkooka.tree.select;

import walkingkooka.build.Builder;
import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.expression.Expression;

import java.util.function.Predicate;

/**
 * Accepts the string representation of an axis, node and predicate node selector and builds up a {@link NodeSelector#toString()}.
 */
final class NodeSelectorToStringBuilder implements Builder<String> {

    static NodeSelectorToStringBuilder empty() {
        return new NodeSelectorToStringBuilder();
    }

    private NodeSelectorToStringBuilder() {
        super();
    }

    void absolute() {
        this.mode = this.mode.absolute(this.b);
    }

    void descendantOrSelf() {
        this.mode = this.mode.descendantOrSelf(this.b);
    }

    void self() {
        this.axisSymbol(".");
    }

    void parent() {
        this.axisSymbol("..");
    }

    void axisName(final String axis) {
        this.mode = this.mode.axisName(axis, this.b);
    }

    private void axisSymbol(final String axis) {
        this.mode = this.mode.axisSymbol(axis, this.b);
    }

    void name(final Name name) {
        this.mode = this.mode.name(name, this.b);
    }

    void predicate(final Predicate<?> predicate) {
        this.mode = this.mode.predicate(predicate, this.b);
    }

    void expression(final Expression expression) {
        this.mode = this.mode.expression(expression, this.b);
    }

    void customToString(final String custom) {
        final CharacterConstant separator = NodeSelector.SEPARATOR;
        final StringBuilder b = this.b;

        if (b.length() > 0 && !CharSequences.endsWith(b, separator.string())) {
            b.append(separator.character());
        }
        b.append(custom);
        this.mode = NodeSelectorToStringBuilderMode.CUSTOM;
    }

    void append(final String append) {
        this.b.append(append);
    }

    @Override
    public String build() {
        this.mode.finish(b);
        return this.b.toString();
    }

    private final StringBuilder b = new StringBuilder();

    private NodeSelectorToStringBuilderMode mode = NodeSelectorToStringBuilderMode.AXIS_NAME_OR_DESCENDANT_OR_SELF;

    @Override
    public String toString() {
        return b + " (" + this.mode + ')';
    }
}
