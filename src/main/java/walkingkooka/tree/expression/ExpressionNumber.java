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

import walkingkooka.reflect.PublicStaticHelper;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Collection of utilities to assist with converting number values to one of the preferred expression number types of
 * {@link BigDecimal}, {@link BigInteger}, {@link Double} and {@link Long}.
 */
public final class ExpressionNumber implements PublicStaticHelper {

    public static boolean isByteShortIntegerLong(final Object value) {
        return value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long;
    }

    public static boolean isFloatDouble(final Object value) {
        return value instanceof Float || value instanceof Double;
    }

    /**
     * Returns true if the value type is one of the supported or upcastable types.
     */
    public static boolean is(final Object value) {
        return isByteShortIntegerLong(value) || isFloatDouble(value) || value instanceof BigDecimal || value instanceof BigInteger;
    }

    public static Class<? extends Number> wider(final Object value) {
        return isByteShortIntegerLong(value) ?
                Long.class :
                isFloatDouble(value) ?
                        Double.class :
                        value instanceof BigInteger ?
                                BigInteger.class :
                                value instanceof BigDecimal ?
                                        BigDecimal.class :
                                        failUp(value);
    }

    private static Class<? extends Number> failUp(final Object value) {
        throw new IllegalArgumentException("value is not a ExpressionNumber");
    }

    /**
     * Stop creation
     */
    private ExpressionNumber() {
        throw new UnsupportedOperationException();
    }
}
