[![Build Status](https://github.com/mP1/walkingkooka-tree/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-tree/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-tree?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-tree.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-tree.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

## Background

Trees are everywhere in computing, representing more complex data structures than a list or sequence.

Before expanding on their utility consider the following

- [Expression](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression) along
  with numerous helpers may be used to represent a runtime or execute numerous different language systems, which will be
  discussed below.
- [Node](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/Node.java) A node
  represents a building block for a tree system, such as JSON, XML and more.
- [Select](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/select) A general
  purpose XPATH like execution system that works over many tree systems, unlike XPATH which only works with XML.

## [Node](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/Node.java)

This helps define a node within a tree or graph with emphasis for navigation in any direction, including branches,
children and leaves.

Currently the following implementations are available which means selecting nodes using xpath like queries is possible.

- [Expression](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression)
  Abstraction over an expression AST
- [File](https://github.com/mP1/walkingkooka-tree-file/tree/master/src/main/java/walkingkooka/tree/file) Abstraction
  over a file system
- [Json](https://github.com/mP1/walkingkooka-tree-json/tree/master/src/main/java/walkingkooka/tree/json) Json objects
- [Patch](https://github.com/mP1/walkingkooka-tree-patch/tree/master/src/main/java/walkingkooka/tree/patch) JSON Patch
  for all Node(s)
- [Pojo](https://github.com/mP1/walkingkooka-tree-pojo/tree/master/src/main/java/walkingkooka/tree/pojo) Java POJOs
- [Search](https://github.com/mP1/walkingkooka-tree-search/tree/master/src/main/java/walkingkooka/tree/search) Text with
  highlight support
- [Xml](https://github.com/mP1/walkingkooka-tree-xml/tree/master/src/main/java/walkingkooka/tree/xml) Xml documents

If this interface is implement any new tree or graph can then be also be searched by the query systems more below.

Some interesting and different navigation systems can be found in java, javascript and the internet, and implementations
that work with `Node` are available, and other

- [JSON POINTER](https://datatracker.ietf.org/doc/html/rfc6901) Another navigation system that may be used to identify a
  value within a JSON document.
- [json-patch](http://jsonpatch.com) only works with JSON and not XML.
- [XPATH](https://en.wikipedia.org/wiki/XPath) only works with XML and not JSON.

## [NodePointer](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/pointer/NodePointer.java)

`NodePointer` provides an equivalent functionality for `Nodes` mentioned above, as what `Json pointer` provides for a
Json object graph, namely a path mechanism to locate a single `Node` with a tree. The `Node.pointer()` may be used to
fetch the pointer for an individual node relative to its root and may be used to locate another Node upon another graph.

[JsonPointer RFC6901](https://tools.ietf.org/html/rfc6901)

### [NodeSelector](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/select/NodeSelector.java)

A `NodeSelector` provides all xpath selectors to all `Nodes`. Predicate expressions and many xpath like functions are
implemented using `Expression` from above, with support for custom functions, which can easily be registered. Many xpath
like functions (eg: `starts-with()`) are already implemented with tickets for others outstanding.

The `Selector` AST may be built by parsing a String holding the expression or by using the available assemblers/builders
present. See the many unit tests for examples.

Some interesting examples of a `select query` can include:

- Executing a query against a Node, like XPATH over XML.
- Executing the same query against a file system to find files, where files and directories are nodes, file attributes
  such as file size are node attributes.
- Queries can also be executed over JSON.

## [Expression](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression)

Expressions is an AST that can be executed, with complete control over the type system and whether types may or may not
be coerced or converted from one type to another, meaning it is possible to do the convenient or horrible depending on
your point of view of type systems such as Javascript and its concept of truthiness, where objects can be converted to
other types depending on context.

Expressions themselves do not enforce any grammar the language in text form, parsing and the definition is a separate
task, but visitors are available to convert between a Expression graph to another AST.

Two interesting examples of the Expression system are

- The mini expression language used
  by [select](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/select). The Node
  selector parser includes a visitor which converts its parser tokens into equivalent Expression.
- The [spreadsheet project](https://github.com/mP1/walkingkooka-spreadsheet) also uses Expressions to represent and
  execute a spreadsheet formula.
- An `Expression` is also a `Node` which means queries can also be executed over an expression.

### [functions](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java)

This interface makes it easy to insert a function located by name into an `Expression` AST.

Numerous projects are available focusing on grouping functions that perform a similar or related task:

More functions are available in other projects including:

- [boolean](https://github.com/mP1/walkingkooka-tree-expression-function-boolean)
- [datetime](https://github.com/mP1/walkingkooka-tree-expression-function-datetime)
- [engineering](https://github.com/mP1/walkingkooka-tree-expression-function-engineering)
- [number](https://github.com/mP1/walkingkooka-tree-expression-function-number)
- [number-trigonometry](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry)
- [spreadsheet](https://github.com/mP1/walkingkooka-spreadsheet-expression-function)
- [string](https://github.com/mP1/walkingkooka-tree-expression-function-string)

Many of these will be used to as the excel functions in
the [spreadsheet project](https://github.com/mP1/walkingkooka-spreadsheet).

### Visitors

Lists have powerful APIs such as `java.util.stream.Stream` and besides the navigation technologies briefly mentioned
above it is often necessary to find and change an AST in some way or perhaps convert from/to another AST.

- Visitors are provided for `Expression` and all `Node` implementations used in numerous interesting ways.
- The [spreadsheet project](https://github.com/mP1/walkingkooka-spreadsheet) uses visitors in many use cases, including
  rewrite formulas when a column/row is moved.
- An `ExpressionVisitor` can be used to transform an Expression back to the `ParserToken` representation, allowing a
  roundtrip from parsing text into an expression and back.
