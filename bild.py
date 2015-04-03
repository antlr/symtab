#!/usr/bin/env python
import os
import string
import shutil

"""
This script uses my experimental build tool http://www.bildtool.org

cd symtab
./bild.py

or

./bild.py -debug

There are targets for mkjar, mkdoc, install, and clean

./bild.py -debug install

E.g.,

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

This script must be run from the main symtab root directory.
"""

# bootstrap by downloading bilder.py if not found
import urllib
import os

if not os.path.exists("bilder.py"):
    print "bootstrapping; downloading bilder.py"
    urllib.urlretrieve(
        "https://raw.githubusercontent.com/parrt/bild/master/src/python/bilder.py",
        "bilder.py")

# assumes bilder.py is in current directory
from bilder import *

VERSION = "1.0.1"


def compile():
    download("http://www.antlr.org/download/antlr-4.5-complete.jar", JARCACHE)
    cp = uniformpath("out") + os.pathsep + \
         os.path.join(JARCACHE, "antlr-4.5-complete.jar") + os.pathsep
    srcpath = ["src"]
    args = ["-Xlint", "-Xlint:-serial", "-g", "-sourcepath", string.join(srcpath, os.pathsep)]
    for sp in srcpath:
        javac(sp, "out", version="1.8", cp=cp, args=args)


def _mkjar():
    require(compile)
    # Prefix of Bundle- is OSGi cruft; it's not everything so we wrap with make_osgi_ready()
    # Declan Cox describes osgi ready jar https://github.com/antlr/antlr4/pull/689.
    manifest = \
        "Implementation-Vendor: ANTLR\n" +\
        "Implementation-Vendor-Id: org.antlr\n" +\
        "Implementation-Title: ANTLR Symbol Table Library\n" +\
        "Implementation-Version: %s\n" +\
        "Built-By: %s\n" +\
        "Build-Jdk: %s\n" +\
        "Created-By: http://www.bildtool.org\n" +\
        "Bundle-Description: ANTLR Symbol Table Library\n" +\
        "Bundle-DocURL: https://github.com/antlr/symtab\n" +\
        "Bundle-License: https://github.com/antlr/symtab/blob/master/LICENSE\n" +\
        "Bundle-Name: ANTLR Symbol Table Library\n" +\
        "Bundle-SymbolicName: org.antlr.symtab-osgi\n" +\
        "Bundle-Vendor: ANTLR\n" +\
        "Bundle-Version: %s\n" +\
        "\n"
    manifest = manifest % (VERSION, os.getlogin(), get_java_version(), VERSION)
    jarfile = "dist/symtab-" + VERSION + ".jar"
    jar(jarfile, srcdir="out", manifest=manifest)
    print "Generated " + jarfile
    osgijarfile = "dist/symtab-" + VERSION + "-osgi.jar"
    make_osgi_ready(jarfile, osgijarfile)
    os.rename(osgijarfile, jarfile) # copy back onto old jar
    print_and_log("Made jar OSGi-ready " + jarfile)


def mkjar(): # if called as root target
    clean()
    _mkjar()


def install(): # mvn installed locally in ~/.m2, java jar to /usr/local/lib if present
    require(mkjar)
    require(mksrc)
    require(mkdoc)
    jarfile = "dist/symtab-" + VERSION + ".jar"
    print_and_log("Maven installing "+jarfile+" and *-sources.jar, *-javadoc.jar")
    mvn_install(binjar=jarfile,
                srcjar="dist/symtab-" + VERSION + "-sources.jar",
                docjar="dist/symtab-" + VERSION + "-javadoc.jar",
                groupid="org.antlr",
                artifactid="symtab",
                version=VERSION)
    if os.path.exists("/usr/local/lib"):
        libjar = "/usr/local/lib/symtab-" + VERSION + ".jar"
        print_and_log("Installing "+libjar)
        shutil.copyfile(jarfile, libjar)


def mksrc():
    copytree(src="src", trg="out/src")  # messages, Java code gen, etc...
    files = allfiles("out/src", ".DS_Store")
    for f in files: rmfile(f)

    srcpath = ["out/src/org"]
    jarfile = "dist/symtab-" + VERSION + "-sources.jar"
    zip(jarfile, srcdirs=srcpath)
    print_and_log("Generated " + jarfile)


def mkdoc():
    require(mksrc)
    doc = "dist/symtab-" + VERSION + "-javadoc.jar"
    runtime_source_jarfile = "dist/symtab-" + VERSION + "-sources.jar"
    if not isstale(src=runtime_source_jarfile, trg=doc):
        return
    # JavaDoc needs antlr runtime 4.5 source code
    mkdir("out/Antlr45Runtime")
    download("http://search.maven.org/remotecontent?filepath=org/antlr/antlr4-runtime/4.5/antlr4-runtime-4.5-sources.jar", "out/Antlr45Runtime")
    unjar("out/Antlr45Runtime/antlr4-runtime-4.5-sources.jar", trgdir="out/Antlr45Runtime")
    # go!
    mkdir("doc/symtab")
    dirs = ["src"]
    exclude = [
        "org/antlr/runtime",
        "org/antlr/v4",
        "org/abego",
        "org/stringtemplate",
        "org/antlr/stringtemplate"]
    javadoc(srcdir=dirs, trgdir="doc/symtab", packages="org.antlr.symtab", exclude=exclude)
    zip(doc, "doc/symtab")


def clean():
    rmdir("dist")
    rmdir("out")
    rmdir("doc")


def all():
    clean()
    _mkjar()
    mkdoc()
    mksrc()
    install()
    clean()


processargs(globals())  # E.g., "python bild.py all"
