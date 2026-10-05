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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.math.DecimalNumberContextTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberContextBasicTest implements ClassTesting<ExpressionNumberContextBasic>,
    ToStringTesting<ExpressionNumberContextBasic>,
    HasExpressionNumberKindTesting,
    DecimalNumberContextTesting {

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionNumberContextBasic.with(null, DECIMAL_NUMBER_CONTEXT)
        );
    }

    @Test
    public void testWithNullDecimalNumberContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionNumberContextBasic.with(EXPRESSION_NUMBER_KIND, null)
        );
    }

    // String..........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            ExpressionNumberContextBasic.with(EXPRESSION_NUMBER_KIND, DECIMAL_NUMBER_CONTEXT),
            "expressionNumberKind=BIG_DECIMAL decimalNumberContext=decimalNumberDigitNumberCount=9 symbols=negativeSign='-' positiveSign='+' zeroDigit='0' currencySymbol=\"$\" decimalSeparator='.' exponentSymbol=\"e\" groupSeparator=',' infinitySymbol=\"∞\" monetaryDecimalSeparator='.' nanSymbol=\"NaN\" percentSymbol='%' permillSymbol='‰' locale=en_AU mathContext=precision=7 roundingMode=HALF_EVEN"
        );
    }

    @Override
    public Class<ExpressionNumberContextBasic> type() {
        return ExpressionNumberContextBasic.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
