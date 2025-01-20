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

import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfOptionalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRangeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRepeatedParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRuleParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfTerminalParserToken;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinatorSyntaxTreeTransformer;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

final class NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer implements EbnfParserCombinatorSyntaxTreeTransformer<ParserContext> {

    private static ParserToken attribute(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::attribute);
    }

    static final EbnfIdentifierName ATTRIBUTE_IDENTIFIER = EbnfIdentifierName.with("ATTRIBUTE");

    private static ParserToken expression(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::expression);
    }

    private static ParserToken function(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::function);
    }

    private static final EbnfIdentifierName FUNCTION_IDENTIFIER = EbnfIdentifierName.with("FUNCTION");

    private static ParserToken group(final ParserToken token, final ParserContext context) {
        return NodeSelectorParserToken.group(clean(token.cast(SequenceParserToken.class)), token.text());
    }

    private static final EbnfIdentifierName GROUP_IDENTIFIER = EbnfIdentifierName.with("GROUP");

    private static List<ParserToken> clean(final SequenceParserToken token) {
        return token.flat()
            .value();
    }

    private static ParserToken negative(final ParserToken token, final ParserContext context) {
        return NodeSelectorParserToken.negative(clean(token.cast(SequenceParserToken.class)), token.text());
    }

    private static final EbnfIdentifierName NEGATIVE_IDENTIFIER = EbnfIdentifierName.with("NEGATIVE");

    private static ParserToken parent(final ParserToken token,
                                      final BiFunction<List<ParserToken>, String, ? extends NodeSelectorParentParserToken<?>> factory) {
        return factory.apply(token instanceof SequenceParserToken ?
                ((SequenceParserToken) token).value() :
                Lists.of(token),
            token.text());
    }

    // predicate .......................................................................................................

    private static ParserToken predicate(final ParserToken token, final ParserContext context) {
        return token instanceof SequenceParserToken ?
            predicate0((SequenceParserToken) token) :
            parent(token, NodeSelectorParserToken::predicate);
    }

    /**
     * Handles grouping of AND and OR sub expressions before creating the enclosing {@link NodeSelectorPredicateParserToken}.
     */
    private static ParserToken predicate0(final SequenceParserToken sequenceParserToken) {
        final List<ParserToken> all = Lists.array();
        final List<ParserToken> tokens = Lists.array();

        int mode = PREDICATE;

        for (ParserToken token : sequenceParserToken.value()) {
            final NodeSelectorParserToken selectorParserToken = token.cast(NodeSelectorParserToken.class);

            final boolean andSymbol = selectorParserToken.isAndSymbol();
            final boolean orSymbol = selectorParserToken.isOrSymbol();

            switch (mode) {
                case PREDICATE:
                    mode = andSymbol ? AND : orSymbol ? OR : PREDICATE;
                    break;
                case AND:
                    if (andSymbol || orSymbol) {
                        final NodeSelectorAndParserToken and = and(tokens);
                        tokens.clear();
                        tokens.add(and);
                    }
                    if (orSymbol) {
                        mode = OR;
                    }
                    break;
                case OR:
                    if (andSymbol || orSymbol) {
                        final NodeSelectorOrParserToken or = or(tokens);
                        tokens.clear();
                        tokens.add(or);
                    }
                    if (andSymbol) {
                        mode = AND;
                    }
                    break;
                default:
                    NeverError.unhandledCase(mode, PREDICATE, AND, OR);
                    break;
            }
            tokens.add(token);
        }

        switch (mode) {
            case PREDICATE:
                all.addAll(tokens);
                break;
            case AND:
                all.add(and(tokens));
                break;
            case OR:
                all.add(or(tokens));
                break;
            default:
                NeverError.unhandledCase(mode, PREDICATE, AND, OR);
                break;
        }

        return NodeSelectorParserToken.predicate(all, sequenceParserToken.text());
    }

    /**
     * All tokens will be immediate child tokens of the enclosing predicate.
     * The bracket-open and bracket-close symbols will always belong to the predicate and never appear in the AND or OR.
     */
    private final static int PREDICATE = 0;
    private final static int AND = PREDICATE + 1;
    private final static int OR = AND + 1;

    private static NodeSelectorAndParserToken and(final List<ParserToken> tokens) {
        return NodeSelectorParserToken.and(tokens, ParserToken.text(tokens));
    }

    private static NodeSelectorOrParserToken or(final List<ParserToken> tokens) {
        return NodeSelectorParserToken.or(tokens, ParserToken.text(tokens));
    }

    private static final EbnfIdentifierName PREDICATE_IDENTIFIER = EbnfIdentifierName.with("PREDICATE");

    static NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer create() {
        return new NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer();
    }

    private NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer() {
        super();

        final Map<EbnfIdentifierName, BiFunction<ParserToken, ParserContext, ParserToken>> identifierToTransform = Maps.sorted();
        identifierToTransform.put(ATTRIBUTE_IDENTIFIER, NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer::attribute);
        identifierToTransform.put(NodeSelectorParsers.EXPRESSION_IDENTIFIER, NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer::expression);
        identifierToTransform.put(FUNCTION_IDENTIFIER, NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer::function);
        identifierToTransform.put(GROUP_IDENTIFIER, NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer::group);
        identifierToTransform.put(NEGATIVE_IDENTIFIER, NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer::negative);
        identifierToTransform.put(PREDICATE_IDENTIFIER, NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer::predicate);

        this.identifierToTransform = identifierToTransform;
    }

    @Override
    public Parser<ParserContext> rule(final EbnfRuleParserToken token,
                                      final Parser<ParserContext> parser) {
        return this.transform(
            token.identifier(),
            parser
        );
    }

    @Override
    public Parser<ParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserContext> concatenation(final EbnfConcatenationParserToken token, Parser<ParserContext> parser) {
        return parser.transform(this::concatenation);
    }

    /**
     * Special case for binary operators and operator priorities.
     */
    private ParserToken concatenation(final ParserToken token,
                                      final ParserContext context) {
        return token.cast(SequenceParserToken.class)
            .binaryOperator(NodeSelectorEbnfParserCombinatorSyntaxTreeTransformerBinaryOperatorTransformer.INSTANCE);
    }

    @Override
    public Parser<ParserContext> exception(final EbnfExceptionParserToken token, final Parser<ParserContext> parser) {
        throw new UnsupportedOperationException(token.text()); // there are no exception tokens.
    }

    @Override
    public Parser<ParserContext> group(final EbnfGroupParserToken token, final Parser<ParserContext> parser) {
        return parser; //leave group definitions as they are.
    }

    @Override
    public Parser<ParserContext> identifier(final EbnfIdentifierParserToken token, final Parser<ParserContext> parser) {
        return this.transform(
            token,
            parser
        );
    }

    /**
     * An {@link EbnfIdentifierName} can appear in a {@link EbnfRuleParserToken} or {@link EbnfIdentifierParserToken},
     * and to avoid problems, the transformer should only be applied to the first.
     */
    private Parser<ParserContext> transform(final EbnfIdentifierParserToken identifierParserToken,
                                            final Parser<ParserContext> parser) {
        final EbnfIdentifierName name = identifierParserToken.value();

        Parser<ParserContext> result = parser;

        // REMOVE and not GET to avoid double transforming $parser
        final BiFunction<ParserToken, ParserContext, ParserToken> transformer = this.identifierToTransform.remove(name);
        if (null != transformer) {
            result = parser.transform(transformer);
        }

        return this.requiredCheck(
            name,
            result
        );
    }

    private final Map<EbnfIdentifierName, BiFunction<ParserToken, ParserContext, ParserToken>> identifierToTransform;

    private Parser<ParserContext> requiredCheck(final EbnfIdentifierName name, final Parser<ParserContext> parser) {
        return name.value().endsWith("REQUIRED") ?
            parser.orReport(ParserReporters.basic()) :
            parser; // leave as is...
    }

    @Override
    public Parser<ParserContext> optional(final EbnfOptionalParserToken token, final Parser<ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserContext> range(final EbnfRangeParserToken token,
                                       final String beginText,
                                       final String endText) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parser<ParserContext> repeated(final EbnfRepeatedParserToken token, final Parser<ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserContext> terminal(final EbnfTerminalParserToken token, final Parser<ParserContext> parser) {
        throw new UnsupportedOperationException(token.toString());
    }
}
