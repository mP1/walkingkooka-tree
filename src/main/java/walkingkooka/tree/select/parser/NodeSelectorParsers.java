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

package walkingkooka.tree.select.parser;

import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.DoubleQuotedParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserToken;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class NodeSelectorParsers implements PublicStaticHelper {

    static final EbnfIdentifierName EXPRESSION_IDENTIFIER = EbnfIdentifierName.with("EXPRESSION");

    /**
     * Returns a {@link Parser} that given text returns a {@link NodeSelectorParserToken}.
     */
    public static Parser<NodeSelectorParserContext> expression() {
        return EXPRESSION;
    }

    private final static Parser<NodeSelectorParserContext> EXPRESSION;

    /**
     * Returns a {@link Parser} that given text returns a {@link NodeSelectorParserToken}.
     */
    public static Parser<NodeSelectorParserContext> predicate() {
        return PREDICATE;
    }

    private final static Parser<NodeSelectorParserContext> PREDICATE;

    // AXIS .....................................................................................................

    private static void axis(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
        predefined.put(ANCESTOR_IDENTIFIER, ANCESTOR_PARSER);
        predefined.put(ANCESTOR_OR_SELF_IDENTIFIER, ANCESTOR_OR_SELF_PARSER);
        predefined.put(CHILD_IDENTIFIER, CHILD_PARSER);
        predefined.put(DESCENDANT_IDENTIFIER, DESCENDANT_PARSER);
        predefined.put(DESCENDANTORSELF_IDENTIFIER, DESCENDANTORSELF_PARSER);
        predefined.put(DESCENDANTORSELF_SLASH_SLASH_IDENTIFIER, DESCENDANTORSELF_SLASH_SLASH_PARSER);
        predefined.put(FIRST_CHILD_IDENTIFIER, FIRST_CHILD_PARSER);
        predefined.put(FOLLOWING_IDENTIFIER, FOLLOWING_PARSER);
        predefined.put(FOLLOWING_SIBLING_IDENTIFIER, FOLLOWING_SIBLING_PARSER);
        predefined.put(LAST_CHILD_IDENTIFIER, LAST_CHILD_PARSER);
        predefined.put(PARENT_IDENTIFIER, PARENT_PARSER);
        predefined.put(PARENT_DOT_DOT_IDENTIFIER, DOT_DOT_PARSER);
        predefined.put(PRECEDING_IDENTIFIER, PRECEDING_PARSER);
        predefined.put(PRECEDING_SIBLING_IDENTIFIER, PRECEDING_SIBLING_PARSER);
        predefined.put(SELF_IDENTIFIER, SELF_PARSER);
        predefined.put(SELF_DOT_IDENTIFIER, DOT_PARSER);
    }

    private static final EbnfIdentifierName ANCESTOR_IDENTIFIER = EbnfIdentifierName.with("ANCESTOR");
    private static final Parser<ParserContext> ANCESTOR_PARSER = literal("ancestor::",
        NodeSelectorParserToken::ancestor,
        NodeSelectorAncestorParserToken.class);

    private static final EbnfIdentifierName ANCESTOR_OR_SELF_IDENTIFIER = EbnfIdentifierName.with("ANCESTOR_OR_SELF");
    private static final Parser<ParserContext> ANCESTOR_OR_SELF_PARSER = literal("ancestor-or-self::",
        NodeSelectorParserToken::ancestorOrSelf,
        NodeSelectorAncestorOrSelfParserToken.class);

    private static final EbnfIdentifierName CHILD_IDENTIFIER = EbnfIdentifierName.with("CHILD");
    private static final Parser<ParserContext> CHILD_PARSER = literal("child::",
        NodeSelectorParserToken::child,
        NodeSelectorChildParserToken.class);

    private static final EbnfIdentifierName DESCENDANT_IDENTIFIER = EbnfIdentifierName.with("DESCENDANT");
    private static final Parser<ParserContext> DESCENDANT_PARSER = literal("descendant::",
        NodeSelectorParserToken::descendant,
        NodeSelectorDescendantParserToken.class);

    private static final EbnfIdentifierName DESCENDANTORSELF_IDENTIFIER = EbnfIdentifierName.with("DESCENDANT_OR_SELF");
    private static final Parser<ParserContext> DESCENDANTORSELF_PARSER = descendantOrSelf("descendant-or-self::");

    private static final EbnfIdentifierName DESCENDANTORSELF_SLASH_SLASH_IDENTIFIER = EbnfIdentifierName.with("DESCENDANTORSELF_SLASH_SLASH");
    private static final Parser<ParserContext> DESCENDANTORSELF_SLASH_SLASH_PARSER = descendantOrSelf("//");

    private static Parser<ParserContext> descendantOrSelf(final String literal) {
        return literal(literal,
            NodeSelectorParserToken::descendantOrSelf,
            NodeSelectorDescendantOrSelfParserToken.class);
    }

    private static final EbnfIdentifierName FIRST_CHILD_IDENTIFIER = EbnfIdentifierName.with("FIRST_CHILD");
    private static final Parser<ParserContext> FIRST_CHILD_PARSER = literal("first-child::",
        NodeSelectorParserToken::firstChild,
        NodeSelectorFirstChildParserToken.class);

    private static final EbnfIdentifierName FOLLOWING_IDENTIFIER = EbnfIdentifierName.with("FOLLOWING");
    private static final Parser<ParserContext> FOLLOWING_PARSER = literal("following::",
        NodeSelectorParserToken::following,
        NodeSelectorFollowingParserToken.class);

    private static final EbnfIdentifierName FOLLOWING_SIBLING_IDENTIFIER = EbnfIdentifierName.with("FOLLOWING_SIBLING");
    private static final Parser<ParserContext> FOLLOWING_SIBLING_PARSER = literal("following-sibling::",
        NodeSelectorParserToken::followingSibling,
        NodeSelectorFollowingSiblingParserToken.class);

    private static final EbnfIdentifierName LAST_CHILD_IDENTIFIER = EbnfIdentifierName.with("LAST_CHILD");
    private static final Parser<ParserContext> LAST_CHILD_PARSER = literal("last-child::",
        NodeSelectorParserToken::lastChild,
        NodeSelectorLastChildParserToken.class);

    private static final EbnfIdentifierName PARENT_IDENTIFIER = EbnfIdentifierName.with("PARENT");
    private static final Parser<ParserContext> PARENT_PARSER = literal("parent::",
        NodeSelectorParserToken::parentOf,
        NodeSelectorParentOfParserToken.class);

    private static final EbnfIdentifierName PARENT_DOT_DOT_IDENTIFIER = EbnfIdentifierName.with("PARENT_DOT_DOT");
    private static final Parser<ParserContext> DOT_DOT_PARSER = literal("..",
        NodeSelectorParserToken::parentOf,
        NodeSelectorParentOfParserToken.class);

    private static final EbnfIdentifierName PRECEDING_IDENTIFIER = EbnfIdentifierName.with("PRECEDING");
    private static final Parser<ParserContext> PRECEDING_PARSER = literal("preceding::",
        NodeSelectorParserToken::preceding,
        NodeSelectorPrecedingParserToken.class);

    private static final EbnfIdentifierName PRECEDING_SIBLING_IDENTIFIER = EbnfIdentifierName.with("PRECEDING_SIBLING");
    private static final Parser<ParserContext> PRECEDING_SIBLING_PARSER = literal("preceding-sibling::",
        NodeSelectorParserToken::precedingSibling,
        NodeSelectorPrecedingSiblingParserToken.class);

    private static final EbnfIdentifierName SELF_IDENTIFIER = EbnfIdentifierName.with("SELF");
    private static final Parser<ParserContext> SELF_PARSER = self("self::");

    private static final EbnfIdentifierName SELF_DOT_IDENTIFIER = EbnfIdentifierName.with("SELF_DOT");
    private static final Parser<ParserContext> DOT_PARSER = self(".");

    private static Parser<ParserContext> self(final String literal) {
        return literal(literal,
            NodeSelectorParserToken::self,
            NodeSelectorSelfParserToken.class);
    }

    // PREDICATE .....................................................................................................

    private static void predicate(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
        predefined.put(AND_IDENTIFIER, AND_PARSER);

        predefined.put(BRACKET_OPEN_IDENTIFIER, BRACKET_OPEN_PARSER);
        predefined.put(BRACKET_CLOSE_IDENTIFIER, BRACKET_CLOSE_PARSER);

        predefined.put(NUMBER_IDENTIFIER, NUMBER_PARSER);

        predefined.put(OR_IDENTIFIER, OR_PARSER);

        predefined.put(PARENS_OPEN_IDENTIFIER, PARENS_OPEN_PARSER);
        predefined.put(PARENS_CLOSE_IDENTIFIER, PARENS_CLOSE_PARSER);

        predefined.put(QUOTED_TEXT_IDENTIFIER, QUOTED_TEXT_PARSER);

        predefined.put(PARAMETER_SEPARATOR_IDENTIFIER, PARAMETER_SEPARATOR_PARSER);

        condition(predefined);
    }

    private static final EbnfIdentifierName AND_IDENTIFIER = EbnfIdentifierName.with("AND");
    private static final Parser<ParserContext> AND_PARSER = literal("AND",
        NodeSelectorParserToken::andSymbol,
        NodeSelectorAndSymbolParserToken.class);

    private static final EbnfIdentifierName BRACKET_OPEN_IDENTIFIER = EbnfIdentifierName.with("BRACKET_OPEN");
    private static final Parser<ParserContext> BRACKET_OPEN_PARSER = literal('[',
        NodeSelectorParserToken::bracketOpenSymbol,
        NodeSelectorBracketOpenSymbolParserToken.class);

    private static final EbnfIdentifierName BRACKET_CLOSE_IDENTIFIER = EbnfIdentifierName.with("BRACKET_CLOSE");
    private static final Parser<ParserContext> BRACKET_CLOSE_PARSER = literal(']',
        NodeSelectorParserToken::bracketCloseSymbol,
        NodeSelectorBracketCloseSymbolParserToken.class);

    private static final EbnfIdentifierName NUMBER_IDENTIFIER = EbnfIdentifierName.with("NUMBER");
    private static final Parser<ParserContext> NUMBER_PARSER = Parsers.bigDecimal()
        .transform(NodeSelectorParsers::expressionNumber)
        .setToString(NUMBER_IDENTIFIER.toString());

    private static ParserToken expressionNumber(final ParserToken token,
                                                final ParserContext context) {
        return expressionNumber0(token.cast(BigDecimalParserToken.class), (NodeSelectorParserContext) context);
    }

    private static ParserToken expressionNumber0(final BigDecimalParserToken token,
                                                 final NodeSelectorParserContext context) {
        return NodeSelectorParserToken.expressionNumber(context.expressionNumberKind().create(token.value()), token.text());
    }

    private static final EbnfIdentifierName OR_IDENTIFIER = EbnfIdentifierName.with("OR");
    private static final Parser<ParserContext> OR_PARSER = literal("OR",
        NodeSelectorParserToken::orSymbol,
        NodeSelectorOrSymbolParserToken.class);

    private static final EbnfIdentifierName PARENS_OPEN_IDENTIFIER = EbnfIdentifierName.with("PARENS_OPEN");
    private static final Parser<ParserContext> PARENS_OPEN_PARSER = literal('(',
        NodeSelectorParserToken::parenthesisOpenSymbol,
        NodeSelectorParenthesisOpenSymbolParserToken.class);

    private static final EbnfIdentifierName PARENS_CLOSE_IDENTIFIER = EbnfIdentifierName.with("PARENS_CLOSE");
    private static final Parser<ParserContext> PARENS_CLOSE_PARSER = literal(')',
        NodeSelectorParserToken::parenthesisCloseSymbol,
        NodeSelectorParenthesisCloseSymbolParserToken.class);

    private static final EbnfIdentifierName QUOTED_TEXT_IDENTIFIER = EbnfIdentifierName.with("QUOTED_TEXT");
    private static final Parser<ParserContext> QUOTED_TEXT_PARSER = Parsers.doubleQuoted()
        .transform(NodeSelectorParsers::quotedText)
        .setToString(QUOTED_TEXT_IDENTIFIER.toString());

    private static ParserToken quotedText(final ParserToken token, final ParserContext context) {
        return NodeSelectorParserToken.quotedText(((DoubleQuotedParserToken) token).value(), token.text());
    }

    private static final EbnfIdentifierName PARAMETER_SEPARATOR_IDENTIFIER = EbnfIdentifierName.with("PARAMETER_SEPARATOR");
    private static final Parser<ParserContext> PARAMETER_SEPARATOR_PARSER = literal(',',
        NodeSelectorParserToken::parameterSeparatorSymbol,
        NodeSelectorParameterSeparatorSymbolParserToken.class);

    /**
     * Registers parsers for all conditions and their symbols.
     */
    private static void condition(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
        predefined.put(PLUS_IDENTIFIER, PLUS_PARSER);
        predefined.put(MINUS_IDENTIFIER, MINUS_PARSER);
        predefined.put(MULTIPLY_IDENTIFIER, MULTIPLY_PARSER);
        predefined.put(DIVIDE_IDENTIFIER, DIVIDE_PARSER);
        predefined.put(MODULO_IDENTIFIER, MODULO_PARSER);

        predefined.put(EQUALS_IDENTIFIER, EQUALS_PARSER);
        predefined.put(GREATER_THAN_IDENTIFIER, GREATER_THAN_PARSER);
        predefined.put(GREATER_THAN_EQUALS_IDENTIFIER, GREATER_THAN_EQUALS_PARSER);
        predefined.put(LESS_THAN_IDENTIFIER, LESS_THAN_PARSER);
        predefined.put(LESS_THAN_EQUALS_IDENTIFIER, LESS_THAN_EQUALS_PARSER);
        predefined.put(NOT_EQUALS_IDENTIFIER, NOT_EQUALS_PARSER);
    }

    private static final EbnfIdentifierName PLUS_IDENTIFIER = EbnfIdentifierName.with("PLUS");
    private static final Parser<ParserContext> PLUS_PARSER = literal('+',
        NodeSelectorParserToken::plusSymbol,
        NodeSelectorPlusSymbolParserToken.class);

    private static final EbnfIdentifierName MINUS_IDENTIFIER = EbnfIdentifierName.with("MINUS");
    private static final Parser<ParserContext> MINUS_PARSER = literal('-',
        NodeSelectorParserToken::minusSymbol,
        NodeSelectorMinusSymbolParserToken.class);

    private static final EbnfIdentifierName MULTIPLY_IDENTIFIER = EbnfIdentifierName.with("MULTIPLY");
    private static final Parser<ParserContext> MULTIPLY_PARSER = literal('*',
        NodeSelectorParserToken::multiplySymbol,
        NodeSelectorMultiplySymbolParserToken.class);

    private static final EbnfIdentifierName DIVIDE_IDENTIFIER = EbnfIdentifierName.with("DIVIDE");
    private static final Parser<ParserContext> DIVIDE_PARSER = literal("div",
        NodeSelectorParserToken::divideSymbol,
        NodeSelectorDivideSymbolParserToken.class);

    private static final EbnfIdentifierName MODULO_IDENTIFIER = EbnfIdentifierName.with("MODULO");
    private static final Parser<ParserContext> MODULO_PARSER = literal("mod",
        NodeSelectorParserToken::moduloSymbol,
        NodeSelectorModuloSymbolParserToken.class);

    private static final EbnfIdentifierName EQUALS_IDENTIFIER = EbnfIdentifierName.with("EQUALS");
    private static final Parser<ParserContext> EQUALS_PARSER = literal('=',
        NodeSelectorParserToken::equalsSymbol,
        NodeSelectorEqualsSymbolParserToken.class);

    private static final EbnfIdentifierName GREATER_THAN_IDENTIFIER = EbnfIdentifierName.with("GREATER_THAN");
    private static final Parser<ParserContext> GREATER_THAN_PARSER = literal('>',
        NodeSelectorParserToken::greaterThanSymbol,
        NodeSelectorGreaterThanSymbolParserToken.class);

    private static final EbnfIdentifierName GREATER_THAN_EQUALS_IDENTIFIER = EbnfIdentifierName.with("GREATER_THAN_EQUALS");
    private static final Parser<ParserContext> GREATER_THAN_EQUALS_PARSER = literal(">=",
        NodeSelectorParserToken::greaterThanEqualsSymbol,
        NodeSelectorGreaterThanEqualsSymbolParserToken.class);

    private static final EbnfIdentifierName LESS_THAN_IDENTIFIER = EbnfIdentifierName.with("LESS_THAN");
    private static final Parser<ParserContext> LESS_THAN_PARSER = literal('<',
        NodeSelectorParserToken::lessThanSymbol,
        NodeSelectorLessThanSymbolParserToken.class);

    private static final EbnfIdentifierName LESS_THAN_EQUALS_IDENTIFIER = EbnfIdentifierName.with("LESS_THAN_EQUALS");
    private static final Parser<ParserContext> LESS_THAN_EQUALS_PARSER = literal("<=",
        NodeSelectorParserToken::lessThanEqualsSymbol,
        NodeSelectorLessThanEqualsSymbolParserToken.class);

    private static final EbnfIdentifierName NOT_EQUALS_IDENTIFIER = EbnfIdentifierName.with("NOT_EQUALS");
    private static final Parser<ParserContext> NOT_EQUALS_PARSER = literal("!=",
        NodeSelectorParserToken::notEqualsSymbol,
        NodeSelectorNotEqualsSymbolParserToken.class);

    // misc.............................................................................................................

    private static void misc(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
        predefined.put(ABSOLUTE_IDENTIFIER, ABSOLUTE_PARSER);
        predefined.put(ATSIGN_SYMBOL_IDENTIFIER, ATSIGN_SYMBOL_PARSER);
        predefined.put(ATTRIBUTE_NAME_IDENTIFIER, ATTRIBUTE_NAME_PARSER);
        predefined.put(FUNCTION_NAME_IDENTIFIER, FUNCTION_NAME_PARSER);
        predefined.put(NODE_NAME_IDENTIFIER, NODE_NAME_PARSER);
        predefined.put(SLASHSEPARATORSYMBOL_IDENTIFIER, SLASHSEPARATORSYMBOL_PARSER);
        predefined.put(WHITESPACE_IDENTIFIER, WHITESPACE_PARSER);
        predefined.put(WILDCARD_IDENTIFIER, WILDCARD_PARSER);
    }

    private static final EbnfIdentifierName ABSOLUTE_IDENTIFIER = EbnfIdentifierName.with("ABSOLUTE");
    private final static Parser<ParserContext> ABSOLUTE_PARSER = literal('/',
        NodeSelectorParserToken::absolute,
        NodeSelectorAbsoluteParserToken.class);

    private static final EbnfIdentifierName ATSIGN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("ATSIGN");
    private final static Parser<ParserContext> ATSIGN_SYMBOL_PARSER = literal('@',
        NodeSelectorParserToken::atSignSymbol,
        NodeSelectorAtSignSymbolParserToken.class);

    static final EbnfIdentifierName ATTRIBUTE_NAME_IDENTIFIER = EbnfIdentifierName.with("ATTRIBUTE_NAME");
    private final static Parser<ParserContext> ATTRIBUTE_NAME_PARSER = name(NodeSelectorAttributeName.INITIAL,
        NodeSelectorAttributeName.PART,
        NodeSelectorAttributeName::with,
        NodeSelectorAttributeNameParserToken::attributeName,
        ATTRIBUTE_NAME_IDENTIFIER);

    static final EbnfIdentifierName FUNCTION_NAME_IDENTIFIER = EbnfIdentifierName.with("FUNCTION_NAME");
    private final static Parser<ParserContext> FUNCTION_NAME_PARSER = name(NodeSelectorFunctionName.INITIAL,
        NodeSelectorFunctionName.PART,
        NodeSelectorFunctionName::with,
        NodeSelectorFunctionNameParserToken::functionName,
        FUNCTION_NAME_IDENTIFIER);

    static final EbnfIdentifierName NODE_NAME_IDENTIFIER = EbnfIdentifierName.with("NODE_NAME");
    private final static Parser<ParserContext> NODE_NAME_PARSER = name(NodeSelectorNodeName.INITIAL,
        NodeSelectorNodeName.PART,
        NodeSelectorNodeName::with,
        NodeSelectorNodeNameParserToken::nodeName,
        NODE_NAME_IDENTIFIER);

    private static <T extends NodeSelectorParserToken, N extends Name> Parser<ParserContext> name(final CharPredicate initial,
                                                                                                  final CharPredicate part,
                                                                                                  final Function<String, N> nameFactory,
                                                                                                  final BiFunction<N, String, T> parserTokenFactory,
                                                                                                  final EbnfIdentifierName name) {
        return Parsers.stringInitialAndPartCharPredicate(initial,
                CharPredicates.letterOrDigit().or(part),
                1,
                Integer.MAX_VALUE)
            .transform(((stringParserToken, parserContext) -> parserTokenFactory.apply(nameFactory.apply(((StringParserToken) stringParserToken).value()), stringParserToken.text())))
            .setToString(name.value())
            .cast();
    }

    private static final EbnfIdentifierName SLASHSEPARATORSYMBOL_IDENTIFIER = EbnfIdentifierName.with("SLASH");
    private final static Parser<ParserContext> SLASHSEPARATORSYMBOL_PARSER = literal('/',
        NodeSelectorParserToken::slashSeparatorSymbol,
        NodeSelectorSlashSeparatorSymbolParserToken.class);

    private static final EbnfIdentifierName WHITESPACE_IDENTIFIER = EbnfIdentifierName.with("WHITESPACE");
    private final static Parser<ParserContext> WHITESPACE_PARSER = Parsers.<NodeSelectorParserContext>stringCharPredicate(CharPredicates.whitespace(), 1, Integer.MAX_VALUE)
        .transform(NodeSelectorParsers::transformWhitespace)
        .setToString(NodeSelectorWhitespaceParserToken.class.getSimpleName())
        .cast();

    private static ParserToken transformWhitespace(final ParserToken token, final ParserContext context) {
        return NodeSelectorParserToken.whitespace(((StringParserToken) token).value(), token.text());
    }

    private static final EbnfIdentifierName WILDCARD_IDENTIFIER = EbnfIdentifierName.with("WILDCARD");
    private static final Parser<ParserContext> WILDCARD_PARSER = literal('*',
        NodeSelectorParserToken::wildcard,
        NodeSelectorWildcardParserToken.class);

    // NodeSelectorParsersGrammar.txt
    private final static String FILENAME = NodeSelectorParsers.class.getSimpleName() + "Grammar.txt";

    private final static EbnfIdentifierName PREDICATE_IDENTIFIER = EbnfIdentifierName.with("PREDICATE");

    // must be last so all other constants are set, avoiding NPEs.

    // Parsers the grammar and returns the selected parser.
    static {
        try {
            final Map<EbnfIdentifierName, Parser<ParserContext>> predefined = Maps.sorted();

            axis(predefined);
            predicate(predefined);
            misc(predefined);

            final Function<EbnfIdentifierName, Parser<ParserContext>> parsers = EbnfParserToken.parseFile(
                new NodeSelectorParsersGrammarProvider().text(),
                FILENAME
            ).combinatorForFile(
                (n) -> Optional.ofNullable(
                    predefined.get(n)
                ),
                NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer.create(),
                FILENAME
            );

            EXPRESSION = parsers.apply(EXPRESSION_IDENTIFIER).cast();
            PREDICATE = parsers.apply(PREDICATE_IDENTIFIER).cast();
        } catch (final NodeSelectorParserException rethrow) {
            throw rethrow;
        } catch (final Exception cause) {
            throw new NodeSelectorParserException("Unable to read grammar in file " + FILENAME + ", message: " + cause.getMessage(), cause);
        }
    }

    /**
     * Matches a token holding a single character.
     */
    private static Parser<ParserContext> literal(final char c,
                                                 final BiFunction<String, String, ParserToken> factory,
                                                 final Class<? extends NodeSelectorLeafParserToken<?>> tokenClass) {
        return Parsers.character(CaseSensitivity.SENSITIVE.charPredicate(c))
            .transform((charParserToken, context) -> factory.apply(((CharacterParserToken) charParserToken).value().toString(), charParserToken.text()))
            .setToString(tokenClass.getSimpleName())
            .cast();
    }

    private static Parser<ParserContext> literal(final String text,
                                                 final BiFunction<String, String, ParserToken> factory,
                                                 final Class<? extends NodeSelectorLeafParserToken<?>> tokenClass) {
        return text.length() == 1 ?
            literal(text.charAt(0), factory, tokenClass) :
            literal0(text, factory, tokenClass);
    }

    private static Parser<ParserContext> literal0(final String text,
                                                  final BiFunction<String, String, ParserToken> factory,
                                                  final Class<? extends NodeSelectorLeafParserToken<?>> tokenClass) {
        return Parsers.string(text, CaseSensitivity.INSENSITIVE)
            .transform((stringParserToken, context) -> factory.apply(((StringParserToken) stringParserToken).value(), stringParserToken.text()))
            .setToString(tokenClass.getSimpleName())
            .cast();
    }

    /**
     * Stop construction
     */
    private NodeSelectorParsers() {
        throw new UnsupportedOperationException();
    }
}
