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

import javaemul.internal.annotations.GwtIncompatible;
import walkingkooka.convert.HasConvertError;
import walkingkooka.text.CharSequences;

// https://github.com/mP1/walkingkooka-tree/issues/307
// Emulate Class.cast
class ExpressionFunctionParameterCast extends ExpressionFunctionParameterCastGwt {

    @GwtIncompatible
    static <T> T cast(final Object value,
                      final ExpressionFunctionParameter<T> parameter) {
        final Class<T> type = parameter.type();
        if (null != value && false == type.isInstance(value)) {
            String invalidTypeMessage = null;

            if (value instanceof HasConvertError) {
                invalidTypeMessage = ((HasConvertError) value).convertErrorMessage()
                    .orElse(null);
            }

            if (null == invalidTypeMessage) {
                invalidTypeMessage = "Invalid type " +
                    value.getClass().getName() +
                    " expected " +
                    type.getName();
            }

            // Invalid parameter "name" value "Actual" expected "Expected".
            throw new ClassCastException(
                "Parameter " +
                    CharSequences.quoteAndEscape(
                        parameter.name()
                            .value()
                    ) +
                    ": " +
                    invalidTypeMessage
            );
        }
        return type.cast(value);
    }
}
