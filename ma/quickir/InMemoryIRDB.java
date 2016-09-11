/*
 * Ma_Sys.ma Quick Information Retrieval Version 1.0.0.0
 * Coypright (c) 2016 Ma_Sys.ma.
 * For further info send an e-mail to Ma_Sys.ma@web.de.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ma.quickir;

import java.util.*;

import ma.tools2.util.NotImplementedException;
import ma.d5man.lib.j8.BiFunction;

class InMemoryIRDB<K> implements IRDB<K> {

	private static final BiFunction<Integer,String,Float> IDENTITY =
					new BiFunction<Integer,String,Float>() {
		@Override
		public Float apply(Integer x, String s) {
			return (float)x;
		}
	};

	private final IRResolver<K> resolver;
	private final Map<K, TermVector> documents;
	private final TermVector termFrequencies;

	InMemoryIRDB(IRResolver<K> resolver) {
		super();
		this.resolver = resolver;
		documents = new HashMap<K, TermVector>();
		termFrequencies = new TermVector();
	}

	@Override
	public void scan(K key) throws IRException {
		TermVector cvec = Tokenizer.toTerms(resolver.resolve(key));
		documents.put(key, cvec);
		termFrequencies.addOnce(cvec);
	}

	@Override
	public Iterable<QueryResultME<K>> query(String query, Scoring s)
							throws IRException {
		List<QueryResultME<K>> ret = new ArrayList<QueryResultME<K>>();
		TermVector queryOBJ = Tokenizer.toTerms(query);
		BiFunction<Integer,String,Float> transformer =
						makeTransformer(s, queryOBJ);
		for(Map.Entry<K, TermVector> e: documents.entrySet()) {
			float score = score(queryOBJ, e.getValue(), s,
								transformer);
			if(score > 0.0001f)
				ret.add(new QueryResultME<K>(e.getKey(),
									score));
		}
		Collections.sort(ret);
		return ret;
	}

	private BiFunction<Integer,String,Float> makeTransformer(Scoring s,
							TermVector queryOBJ) {
		switch(s) {
		case DOT_PRODUCT_SIMILARITY:
		case COSINE_SIMILARITY:
			return IDENTITY;
		case COSINE_SIMILARITY_TF_IDF:
			return new BiFunction<Integer,String,Float>() {
				@Override
				public Float apply(Integer tf, String term) {
					return (float)(
						wtd(tf) * idf(documents.size(),
							termFrequencies.
							getFrequency(term))
					);
				}
			};
		default:
			throw new NotImplementedException(s.toString());
		}
	}

	private static double wtd(int tf) {
		return (tf == 0)? 0: ((double)Math.log(tf) + 1);
	}

	/**
	 * https://janav.wordpress.com/2013/10/27/tf-idf-and-cosine-similarity/
	 * says we return 1 if documentFreqencyOfTerm is 0.
	 */
	private static double idf(int documents, int documentFreqencyOfTerm) {
		return (documentFreqencyOfTerm == 0)? 1.0:
				(1.0 + (Math.log(
				(double)documents / documentFreqencyOfTerm)));
	}

	private float score(TermVector queryOBJ, TermVector val, Scoring s,
				BiFunction<Integer,String,Float> transformer) {
		switch(s) {
		case DOT_PRODUCT_SIMILARITY:
			return val.dotProductSimilarity(queryOBJ, transformer);
		default:
			return val.cosineSimilarity(queryOBJ, transformer);
		}
	}

}
