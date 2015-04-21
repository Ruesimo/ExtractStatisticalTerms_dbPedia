/**
 * @author Simon
 */
package ruesimo.com.extract_statistical_terms_dbPedia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.semanticweb.yars.nx.parser.NxParser;

public class Parser {

	public NxParser getNewParser(String source) throws FileNotFoundException, IOException {
		return new NxParser(new FileInputStream(source), false);
	}
}