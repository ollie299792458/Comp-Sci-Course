#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#

# This file sets up the paths to the compiler tools. 
# These should work as-is on a standard linux distribution if you have the tools installed.
# If you are using a Microsoft O/S you might need to change your copy a bit.


# The standard ML interpreter
#   http://www.polyml.org The Poly/ML web site.
# Install with 'yum install polyml'
SMLRUN=poly


# The Java Compiler
JAVAC=javac

# The Java VM
JAVARUN=java

# Run matlab or octave
# 
OCTAVE=octave




# FSharp Compiler
FSHARP?=/usr/local/FSharp-2.0.0.0
FSC=mono $(FSHARP)/bin/fsc.exe /nowarn:75 /consolecolors- /nologo  /lib:. --nowarn:25,64

# You need packages monodevelop
# CSharp Compiler
CSC=gmcs

# The mono distro of some Windows libraries:
MLIBS ?=  /r:/usr/lib/mono/2.0/System.Windows.Forms.dll /r:/usr/lib/mono/2.0/System.Drawing.dll

# eof
