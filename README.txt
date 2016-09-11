Ma_Sys.ma Quick Information Retrieval Version 1.0.0.0
Coypright (c) 2016 Ma_Sys.ma.
For further info send an e-mail to Ma_Sys.ma@web.de.

--------------------------------------------------------------[ Introduction ]--

This ``Quick Information Retrieval''-System (short: QuickIR) is a program to
query a set of text documents using the common TF-IDF and cosine similarity.
This allows a keyword-based search which takes into account that certain words
which appear in a lot of documents are less relevant.

Not to raise any wrong expectations: It is called ``Quick'' because it was
written quickly, _not_ because it runs quickly! The code has been written to be
very changeable, that is: there is large freedom of choosing an algorithm for
simple ranking, e.g. cosine similarity, dot product similarity etc. and on the
other hand, there are a lot of function calls which could be optimized away¹
(or ``precomputed'') if this feature was not needed.

This was designed as a solution to an exercise presented at the TU Darmstadt's
DKE course in summer 2016.

---------------------------------------------------------------[ Compilation ]--

Compile this as follows (using any Java 7 compiler):

	javac -encoding UTF-8 -cp minitools ma/quickir/*.java

Notes for MDVL users
 * If you are on a MDVL system, you may use `$ make` or `$ make jar` as well.
 * Also, if you have a working D5Man-installation, it is recommended to use
   `-cp .../libd5manexport.jar` with `...` being the path to your
   `libd5manexport.jar` (edit the `LIBD5MANEXPORT` variable from the `Makefile`
   accordingly).

---------------------------------------------------------------------[ Usage ]--

Invoke the program as follows

	java -cp minitools:. ma.quickir.Main <arguments>

where arguments is a specfic commandline.

Currently, only `<arguments>` of the format `TEXT <query>` are supported.

Also, in order to tell the application which files to consult for ``search
indexing'' (will be wholly in memory and re-generated for every request) you
have to give a list of file names via STDIN.

Example:

	find /usr/share/common-licenses -type f | \
		java -cp minitools:. ma.quickir.Main TEXT "Apache"

will output (on my Debian Jessie system):

	/usr/share/common-licenses/Apache-2.0 (0.10426607)

Thus, this query yielded only one result. Let's try to confuse the search
engine with adding a few trivial words:

	find /usr/share/common-licenses -type f | \
		java -cp minitools:. ma.quickir.Main TEXT "Apache and to that"

will output (formatted in a tabular manner for enhanced readability in the
documentation):

	Apache-2.0 (0.13838476)
	LGPL-3     (0.05926972)
	GPL-1      (0.057670876)
	Artistic   (0.053396318)
	GPL-2      (0.052671414)
	LGPL-2     (0.047885068)
	LGPL-2.1   (0.04646213)
	GFDL-1.2   (0.041788474)
	GFDL-1.3   (0.039808597)
	BSD        (0.03747116)
	GPL-3      (0.03579121)

Now we have more results but sill the Apache license is found first, although it
is not that it contains many of the words ``and'', ``to'' and ``that'' compared
with the other licenses which we can find out by removing ``Apache'' from the
list of words to be searched for:

	find /usr/share/common-licenses -type f | \
		java -cp minitools:. ma.quickir.Main TEXT "and to that"

will output (also formatted for enhanced readability in the documentation):

	LGPL-3     (0.13050869)
	GPL-1      (0.12698813)
	Artistic   (0.11757579)
	GPL-2      (0.11597958)
	LGPL-2     (0.10544031)
	LGPL-2.1   (0.10230709)
	Apache-2.0 (0.100169055)
	GFDL-1.2   (0.09201595)
	GFDL-1.3   (0.08765637)
	BSD        (0.08250946)
	GPL-3      (0.078810304)

Of course, you are invited to try the program on other interesting texts :)

--------------------------------------------------------------------[ Issues ]--

As this was created for learning purposes, it's features are only rudimentary.
Especially, this search is

 - fully case-sensitive
 - unaware of any punctuation
 - insufficient at determining word boundaries
   (a ``new word'' is whenever a character not in [A-Za-z_] occurs)
 - incomplete in that it does not support querying D5Man documents which
   means it has a currently unnecessary dependency on ``libd5manexport''
   (or the ``minitools'' supplied with this program).
 - slow (see ``Introduction'' for details)
 - single-threaded (This problem really calls for parallelization but for now,
   this is not implemented).

¹) Using better design, it may be possible to combine both aspecs but that is
not currently implemented.

-------------------------------------------------------------------[ License ]--

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

This program is licensed under GPLv3. The full text can be found under
`minitools/ma/tools2/license.txt`.
