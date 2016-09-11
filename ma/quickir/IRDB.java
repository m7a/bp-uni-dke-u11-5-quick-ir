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

public interface IRDB<K> {

	public enum Scoring {
		DOT_PRODUCT_SIMILARITY,
		COSINE_SIMILARITY,
		COSINE_SIMILARITY_TF_IDF,
	}

	void scan(K key) throws IRException;

	Iterable<QueryResultME<K>> query(String query, Scoring s)
							throws IRException;

}
