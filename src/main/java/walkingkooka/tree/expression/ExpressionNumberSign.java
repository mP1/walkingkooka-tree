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

import java.math.BigDecimal;

/**
 * Represents the sign of a given {@link ExpressionNumber}.
 */
public enum ExpressionNumberSign {

    NEGATIVE(0),

    ZERO(1),

    POSITIVE(2);

    ExpressionNumberSign(int index) {
        this.index = index;
    }

    private final int index;

    ExpressionNumber expressionNumberBigDecimal() {
        return EXPRESSION_NUMBERS[this.index + 0];
    }

    ExpressionNumber expressionNumberDouble() {
        return EXPRESSION_NUMBERS[this.index + 3];
    }

    private final static ExpressionNumber[] EXPRESSION_NUMBERS = new ExpressionNumber[]{
        ExpressionNumberKind.BIG_DECIMAL.create(-1),
        ExpressionNumberKind.BIG_DECIMAL.zero(),
        ExpressionNumberKind.BIG_DECIMAL.create(+1),
        ExpressionNumberKind.DOUBLE.create(-1),
        ExpressionNumberKind.DOUBLE.zero(),
        ExpressionNumberKind.DOUBLE.create(+1)
    };

    static ExpressionNumberSign pick(final BigDecimal bigDecimal) {
        return pick0(bigDecimal.signum());
    }

    static ExpressionNumberSign pick(final double d) {
        return pick0((int) Math.signum(d));
    }

    private static ExpressionNumberSign pick0(final int sign) {
        return sign == 0 ? ZERO :
            sign < 0 ?
                NEGATIVE :
                POSITIVE;
    }
}
