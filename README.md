# symtab

A generic symbol table for lexically/statically scoped languages. The library is slightly dependent on ANTLR parse tree types, ParserRuleContext, but you could strip these out if you want to use it without ANTLR and don't want the dependency.

Uses Java 8 (a little).

Grab the [latest jar](http://www.antlr.org/download/symtab-1.0.1.jar).

## Building / installing

It's just a bunch of Java code so you can compile it like any other pile of
code in a development environment. Or, on UNIX you can use my bild.py script:

```bash
$ cd symtab
./bild.py
```

or

```bash
./bild.py -debug
```

There are targets for mkjar, mkdoc, install, and clean

```bash
./bild.py -debug install
```

For example:

```bash
~/antlr/code/symtab $ ./bild.py install
target install
require _mkjar
require compile
build _mkjar
Generated dist/symtab-1.0.jar
Made jar OSGi-ready dist/symtab-1.0.jar
build install
require mksrc
Generated dist/symtab-1.0-sources.jar
build install
require mkdoc
require mksrc
build install
Maven installing dist/symtab-1.0.jar and *-sources.jar, *-javadoc.jar
Installing /usr/local/lib/symtab-1.0.jar
bild succeeded
```
