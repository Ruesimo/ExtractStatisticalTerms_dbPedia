/**
 * @author Simon
 */
package ruesimo.com.extract_statistical_terms_dbPedia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class ParserSubcategories extends Parser {

	private final static String SKOS_CATEGORIES = "dbPedia sources\\skos_categories_en.nt";

	public static final String[] IRRELEVANT_CATEGORIES_STRINGS = new String[] {
			/*
			 * "<http://dbpedia.org/resource/Category:Bayesian_statistics>",
			 * "<http://dbpedia.org/resource/Category:Computational_statistics>"
			 * ,
			 * "<http://dbpedia.org/resource/Category:Covariance_and_correlation>"
			 * , "<http://dbpedia.org/resource/Category:Data_analysis>",
			 * "<http://dbpedia.org/resource/Category:Data_collection>",
			 * "<http://dbpedia.org/resource/Category:Decision_theory>",
			 * "<http://dbpedia.org/resource/Category:Design_of_experiments>",
			 * "<http://dbpedia.org/resource/Category:Ethics_and_statistics>",
			 */"<http://dbpedia.org/resource/Category:Fields_of_application_of_statistics>",
			"<http://dbpedia.org/resource/Category:History_of_statistics>",
			/*
			 * "<http://dbpedia.org/resource/Category:Information,_knowledge,_and_uncertainty>"
			 * ,
			 */
			"<http://dbpedia.org/resource/Category:Laymen_and_statistics>",
			/*
			 * "<http://dbpedia.org/resource/Category:Logic_and_statistics>",
			 * "<http://dbpedia.org/resource/Category:Meta-analysis>",
			 */
			"<http://dbpedia.org/resource/Category:Misuse_of_statistics>",
			/*
			 * "<http://dbpedia.org/resource/Category:Multivariate_statistics>",
			 * "<http://dbpedia.org/resource/Category:Non-parametric_statistics>"
			 * ,
			 */
			"<http://dbpedia.org/resource/Category:Observational_study>",
			/*
			 * "<http://dbpedia.org/resource/Category:Parametric_statistics>",
			 * "<http://dbpedia.org/resource/Category:Probability_distributions>"
			 * , "<http://dbpedia.org/resource/Category:Regression_analysis>",
			 * "<http://dbpedia.org/resource/Category:Robust_statistics>",
			 * "<http://dbpedia.org/resource/Category:Sampling_(statistics)>",
			 * "<http://dbpedia.org/resource/Category:Spatial_data_analysis>",
			 */
			"<http://dbpedia.org/resource/Category:Statistical_awards>",/*
																		 * "<http://dbpedia.org/resource/Category:Statistical_charts_and_diagrams>"
																		 * ,
																		 */
			"<http://dbpedia.org/resource/Category:Statistical_data_agreements>",
			"<http://dbpedia.org/resource/Category:Statistical_data_coding>",
			"<http://dbpedia.org/resource/Category:Statistical_data_sets>",/*
																			 * "<http://dbpedia.org/resource/Category:Statistical_data_types>"
																			 * ,
																			 * "<http://dbpedia.org/resource/Category:Statistical_dependence>"
																			 * ,
																			 * "<http://dbpedia.org/resource/Category:Statistical_deviation_and_dispersion>"
																			 * ,
																			 * "<http://dbpedia.org/resource/Category:Statistical_distance_measures>"
																			 * ,
																			 * "<http://dbpedia.org/resource/Category:Statistical_inference>"
																			 * ,
																			 * "<http://dbpedia.org/resource/Category:Statistical_intervals>"
																			 * ,
																			 * "<http://dbpedia.org/resource/Category:Statistical_laws>"
																			 * ,
																			 * "<http://dbpedia.org/resource/Category:Statistical_methods>"
																			 * ,
																			 * "<http://dbpedia.org/resource/Category:Statistical_models>"
																			 * ,
																			 */
			"<http://dbpedia.org/resource/Category:Statistical_organizations>",/*
																				 * "<http://dbpedia.org/resource/Category:Statistical_principles>"
																				 * ,
																				 * "<http://dbpedia.org/resource/Category:Statistical_randomness>"
																				 * ,
																				 * "<http://dbpedia.org/resource/Category:Statistical_ratios>"
																				 * ,
																				 * "<http://dbpedia.org/resource/Category:Statistical_software>"
																				 * ,
																				 * "<http://dbpedia.org/resource/Category:Statistical_terminology>"
																				 * ,
																				 * "<http://dbpedia.org/resource/Category:Statistical_tests>"
																				 * ,
																				 * "<http://dbpedia.org/resource/Category:Statistical_theory>"
																				 * ,
																				 */
			"<http://dbpedia.org/resource/Category:Statisticians>",
			"<http://dbpedia.org/resource/Category:Statistics-related_lists>",/*
																			 * "<http://dbpedia.org/resource/Category:Statistics>"
																			 * ,
																			 */
			"<http://dbpedia.org/resource/Category:Statistics_books>",
			"<http://dbpedia.org/resource/Category:Statistics_education>",
			"<http://dbpedia.org/resource/Category:Statistics_journals>",/*
																		 * "<http://dbpedia.org/resource/Category:Stochastic_processes>"
																		 * ,
																		 * "<http://dbpedia.org/resource/Category:Summary_statistics>"
																		 * ,
																		 * "<http://dbpedia.org/resource/Category:Survey_methodology>"
																		 * ,
																		 * "<http://dbpedia.org/resource/Category:Survival_analysis>"
																		 * ,
																		 * "<http://dbpedia.org/resource/Category:Time_series_analysis>"
																		 * ,
																		 * "<http://dbpedia.org/resource/Category:Uncertainty_of_numbers>"
																		 */};
	public static final Set<String> IRRELEVANT_CATEGORIES = new HashSet<String>(
			Arrays.asList(IRRELEVANT_CATEGORIES_STRINGS));

	public Set<String> getSubcategoriesFromCategory(String category)
			throws FileNotFoundException, IOException {

		NxParser parser = getNewParser(SKOS_CATEGORIES);

		Set<String> results = new HashSet<String>();

		while (parser.hasNext()) {
			Node[] triple = parser.next();

			String first = triple[0].toN3();
			String second = triple[1].toN3();
			String third = triple[2].toN3();

			if (second.equals("<http://www.w3.org/2004/02/skos/core#broader>")
					&& third.equals(category)) {

				if (!IRRELEVANT_CATEGORIES.contains(first)) {
					results.add(first);
				}
			}
		}
		results.add(category);

		return results;
	}

	public Set<String> getSubcategoriesFromCategories(Set<String> categories,
			Set<String> allCategories) throws FileNotFoundException,
			IOException {

		NxParser parser = getNewParser(SKOS_CATEGORIES);

		Set<String> results = new HashSet<String>();

		while (parser.hasNext()) {
			Node[] triple = parser.next();

			String first = triple[0].toN3();
			String second = triple[1].toN3();
			String third = triple[2].toN3();

			if (!allCategories.contains(first) // to prevent cycles, if a
												// category was already added,
												// it will not be added again
					&& second
							.equals("<http://www.w3.org/2004/02/skos/core#broader>")
					&& categories.contains(third)) {

				if (!IRRELEVANT_CATEGORIES.contains(first)) {
					results.add(first);
				}
			}
		}
		return results;
	}
}
