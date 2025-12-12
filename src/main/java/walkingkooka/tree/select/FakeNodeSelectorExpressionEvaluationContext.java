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

package walkingkooka.tree.select;

import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.EnvironmentValueWatcher;
import walkingkooka.naming.Name;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class FakeNodeSelectorExpressionEvaluationContext<N extends Node<N, NAME, ANAME, AVALUE>,
    NAME extends Name,
    ANAME extends Name,
    AVALUE> extends FakeExpressionEvaluationContext implements NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> {

    @Override
    public N node() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LineEnding lineEnding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> setLineEnding(final LineEnding lineEnding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> cloneEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> setEnvironmentContext(final EnvironmentContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> setEnvironmentValue(final EnvironmentValueName<T> name,
                                                                                                   final T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> removeEnvironmentValue(final EnvironmentValueName<?> name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDateTime now() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<EmailAddress> user() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> setUser(final Optional<EmailAddress> user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
        throw new UnsupportedOperationException();
    }
}
