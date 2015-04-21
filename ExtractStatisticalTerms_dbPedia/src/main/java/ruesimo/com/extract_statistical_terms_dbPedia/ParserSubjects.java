/**
 *@authorSimon
 */
package ruesimo.com.extract_statistical_terms_dbPedia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class ParserSubjects extends Parser {

	private final static String ARTICLE_CATEGORIES = "dbPedia sources\\article_categories_en.nt";

	public Set<SubjectsType> getSubjectsFromCategories(Set<String> categories,
			Set<SubjectsType> allSubjects) throws FileNotFoundException,
			IOException {

		NxParser parser = getNewParser(ARTICLE_CATEGORIES);

		Set<SubjectsType> results = new HashSet<SubjectsType>();
		while (parser.hasNext()) {
			Node[] triple = parser.next();

			String first = triple[0].toN3();
			String second = triple[1].toN3();
			String third = triple[2].toN3();

			// example: <http://dbpedia.org/resource/Basel_Convention>
			// <http://purl.org/dc/terms/subject>
			// <http://dbpedia.org/resource/Category:Treaties_of_Palau>

			if (first.contains("<http://dbpedia.org/resource/")
					&& second.equals("<http://purl.org/dc/terms/subject>")
					&& categories.contains(third)) {
				String firstProcessed = java.net.URLDecoder.decode(first,
						"UTF-8");
				String thirdProcessed = java.net.URLDecoder.decode(third,
						"UTF-8");
				firstProcessed = firstProcessed
						.substring(
								firstProcessed
										.indexOf("<http://dbpedia.org/resource/") + 29,
								firstProcessed.indexOf(">")).replace("_", " ");
				thirdProcessed = thirdProcessed
						.substring(
								thirdProcessed
										.indexOf("<http://dbpedia.org/resource/") + 38,
								thirdProcessed.indexOf(">")).replace("_", " ");
				SubjectsType subjectsType = new SubjectsType(thirdProcessed,
						firstProcessed);
				if (!allSubjects.contains(subjectsType)) {
					results.add(subjectsType);
				}
			}
		}
		return results;
	}
}
