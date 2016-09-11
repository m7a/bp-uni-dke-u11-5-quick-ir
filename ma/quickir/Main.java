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
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;

import ma.tools2.util.NotImplementedException;
import ma.tools2.util.BufferUtils;

public class Main implements IRResolver<String> {

	private final QuickIR ir;
	private final String[] args;

	private Main(String[] args) {
		super();
		ir = new QuickIR();
		this.args = args;
	}

	private void run() throws IOException, IRException {
		if(args.length < 2) {
			help();
		} else {
			IRDB<String> db = ir.init(this);
			try(BufferedReader r = new BufferedReader(
					new InputStreamReader(System.in))) {
				String line;
				while((line = r.readLine()) != null)
					db.scan(line);
			}
			Iterable<QueryResultME<String>> results = db.query(
				args[1], IRDB.Scoring.COSINE_SIMILARITY_TF_IDF);
			for(QueryResultME<String> e: results)
				System.out.println(e.getKey() + " ("
							+ e.getValue() + ")");
		}
	}

	@Override
	public String resolve(String key) throws IRException {
		try {
			return BufferUtils.readfile(Files.newInputStream(
							Paths.get(key)));
		} catch(IOException ex) {
			throw new IRException(ex);
		}
	}

	public static void help() {
		System.out.println("Ma_Sys.ma Quick Information " +
						"Retrieval Version 1.0.0.0");
		System.out.println("Copyright (c) 2016 Ma_Sys.ma.");
		System.out.println("For further info send an e-mail to " +
							"Ma_Sys.ma@web.de.");
		System.out.println();
		System.out.println("USAGE $0 TEXT <query>");
	}

	public static void main(String[] args) {
		if(args.length == 0 || args[0].equals("--help")) {
			help();
		} else {
			try {
				if(args[0].equals("TEXT"))
					new Main(args).run();
				else
					throw new NotImplementedException();
			} catch(Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

}
