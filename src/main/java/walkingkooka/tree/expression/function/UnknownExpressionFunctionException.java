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

import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionException;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.Objects;

public class UnknownExpressionFunctionException extends ExpressionException {

    private static final long serialVersionUID = 1L;

    public UnknownExpressionFunctionException(final ExpressionFunctionName name) {
        super();
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String getMessage() {
        return "Unknown function " + CharSequences.quote(this.name().value());
    }

    public final ExpressionFunctionName name() {
        return this.name;
    }

    private final ExpressionFunctionName name;
}
