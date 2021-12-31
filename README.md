[![Build Status](https://github.com/mP1/walkingkooka-tree/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-tree/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-tree?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-tree.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-tree.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)



## Background

Abstractions and utilities for tree graphs supporting functional and immutable interfaces which are almost identical to several well known APIS:

- [json-patch](http://jsonpatch.com) only works with JSON and not XML.
- [XPATH](https://en.wikipedia.org/wiki/XPath) only works with XML and not JSON.

The above technologies are useful but unfortunately only work with XML and JSON respectively, this project using the `Node` interface brings the same
utility to many other implementations.

- [Expression](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression) Abstraction over an expression AST
- [File](https://github.com/mP1/walkingkooka-tree-file/tree/master/src/main/java/walkingkooka/tree/file) Abstraction over a file system
- [Json](https://github.com/mP1/walkingkooka-tree-json/tree/master/src/main/java/walkingkooka/tree/json) Json objects
- [Patch](https://github.com/mP1/walkingkooka-tree-patch/tree/master/src/main/java/walkingkooka/tree/patch) JSON Patch for all Node(s)
- [Pojo](https://github.com/mP1/walkingkooka-tree-pojo/tree/master/src/main/java/walkingkooka/tree/pojo) Java POJOs
- [Search](https://github.com/mP1/walkingkooka-tree-search/tree/master/src/main/java/walkingkooka/tree/search) Text with highlight support
- [Xml](https://github.com/mP1/walkingkooka-tree-xml/tree/master/src/main/java/walkingkooka/tree/xml) Xml documents

With these APIs it is possible to combine these technologies to do x-path selectors over a directory and then `java.util.stream.Stream` and more,
combining the expressiveness, power into solutions handling graphs.



## Navigation
All `Node` include a reference to their parent and children, along with helpers to find previous and following siblings.
There are a variety of other navigation aides each best suited to different use cases with a brief summary below. A familiar
navigation method for XML includes methods nextSibling are now brought to all other implementations such as files. JSON.



## [NodePointer](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/pointer/NodePointer.java)
A `NodePointer` provides an equivalent functionality for `Nodes` as what `Json pointer` provides for a Json object graph,
namely a path mechanism to locate a single `Node` with a tree. The `Node.pointer()` may be used to fetch the pointer for
an individual node relative to its root and may be used to locate another Node upon another graph.

[JsonPointer RFC6901](https://tools.ietf.org/html/rfc6901)



## [NodeSelector](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/select/NodeSelector.java) 
A `NodeSelector` provides all xpath selectors to all `Nodes`. Predicate expressions and many xpath like functions are
implemented, with support for custom functions. Many xpath like functions (eg: `starts-with()`) are already implemented
with tickets for others outstanding.

Selectors may be built by parsing a String holding the expression or by using the would be builder methods that
`NodeSelector` provides. The sample in the stream section below shows a selector being built and the helper
eventually creates a stream and verifies the result of a collect and other stream operations against the provided expectations.

### [NodeSelector mapping](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/select/NodeSelectorContexts.java#L41) 
It is possible to provide a `evaluate` which applies a mapper evaluate over selected or matched `Node`, with the new
tree root returned.

An example of this utility may be to load a HTML document, assemble a selector to match only certain elements and map or
modify just those, with the result being the new updated tree.



## Streaming

### [java.util.stream.Stream support](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/select/NodeSelector.java#L446)
It is also possible to leverage the power of all the intermediate operations like: skip, limits, filtering, mapping
along with familiar terminal operations such as collect, count, findAny and so on. Operations are efficient and navigation
and selections stop once a operator such as limit is satisfied.

The fragment below is a sample taken from a test.

```java
@Test
public void testDescendantOrSelfNamedStream() {
    final TestNode root = TestNode.with("root",
            TestNode.with("branch1", TestNode.with("leaf")),
            TestNode.with("branch2",
                    TestNode.with("skip"),
                    TestNode.with("leaf")));

    this.collectAndCheck(() -> this.stream(TestNode.absoluteNodeSelector()
                    .descendantOrSelf()
                    .named(Names.string("leaf")),
            root),
            root.child(0).child(0),
            root.child(1).child(1));
}
```



## Visitor

All `Node` implementations include a type safe `Visitor` making for easy walking of a tree with navigation control of
children and descendants.

eg:

- [JsonNodeVisitor](https://github.com/mP1/walkingkooka-tree-json/blob/master/src/main/java/walkingkooka/tree/json/JsonNode.java)

## Functions

Only a few functions are available in this project and may be registered and invoked within expressions.

- fake // useful for testing.
- node()
- nodeName(Node)
- typeName(Object) // equivalent to Object.getClass().getName()

More functions are available in other projects including:

- [boolean](https://github.com/mP1/walkingkooka-tree-expression-function-boolean)
- [datetime](https://github.com/mP1/walkingkooka-tree-expression-function-datetime)
- [number](https://github.com/mP1/walkingkooka-tree-expression-function-number)
- [number-trigonometry](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry)
- [string](https://github.com/mP1/walkingkooka-tree-expression-function-string)

