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

import java.util.Map;
import java.util.HashMap;

import ma.d5man.lib.j8.BiFunction;

class TermVector {

	private final Map<String, Integer> termFrequency;

	TermVector() {
		super();
		termFrequency = new HashMap<String, Integer>();
	}

	TermVector(TermVector c) {
		super();
		termFrequency = new HashMap<String, Integer>(c.termFrequency);
	}

	// Writing

	void recordTerm(String term) {
		recordTerm(term, 1);
	}

	/** internal option, currently freq is never != 1 */
	private void recordTerm(String term, int freq) {
		if(termFrequency.containsKey(term))
			termFrequency.put(term, termFrequency.get(term) + freq);
		else
			termFrequency.put(term, freq);

	}

	/** accumulating add (to count documents in which this term occurs) */
	void addOnce(TermVector v) {
		for(Map.Entry<String,Integer> e: v.termFrequency.entrySet())
			recordTerm(e.getKey());
	}

	int getFrequency(String term) {
		return termFrequency.containsKey(term)?
						termFrequency.get(term): 0;
	}

	// Reading and Comparing

	float dotProductSimilarity(TermVector to,
				BiFunction<Integer,String,Float> weightener) {
		float sim = 0;
		for(Map.Entry<String, Integer> e: termFrequency.entrySet())
			sim += weightener.apply(to.getFrequency(e.getKey()),
					e.getKey()) *
				weightener.apply(e.getValue(), e.getKey());
		return sim;
	}

	float cosineSimilarity(TermVector to,
				BiFunction<Integer,String,Float> weightener) {
		return dotProductSimilarity(to, weightener) /
						(euclideanLength(weightener) *
						to.euclideanLength(weightener));
	}

	float euclideanLength(BiFunction<Integer,String,Float> weightener) {
		float sqSum = 0;
		for(Map.Entry<String, Integer> e: termFrequency.entrySet()) {
			float val = weightener.apply(e.getValue(), e.getKey());
			sqSum += val * val;
		}
		return (float)Math.sqrt(sqSum);
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder("TV[");
		for(Map.Entry<String, Integer> e: termFrequency.entrySet()) {
			ret.append(e.getKey());
			ret.append('=');
			ret.append(e.getValue());
			ret.append(',');
		}
		ret.append(']');
		return ret.toString();
	}

}
