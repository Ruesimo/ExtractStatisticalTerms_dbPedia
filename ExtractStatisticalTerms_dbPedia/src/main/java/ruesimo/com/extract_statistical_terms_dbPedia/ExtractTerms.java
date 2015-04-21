/**
 * @author Simon
 */
package ruesimo.com.extract_statistical_terms_dbPedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ExtractTerms {

	// last level to be included
	private final static int LEVEL = 2;

	private final static String MAIN_CATEGORY = "<http://dbpedia.org/resource/Category:Statistics>";

	public static String DATABASE;
	public static String PASSWORD;

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		String filenameCategories = "results\\categories_";
		String filenameSubjects = "results\\subjects_";

		DATABASE = args[0];
		PASSWORD = args[1];

		Set<String> subcategories = new HashSet<String>();
		Set<String> allSubcategories = new HashSet<String>();
		Set<SubjectsType> subjects = new HashSet<SubjectsType>();
		Set<SubjectsType> allSubjects = new HashSet<SubjectsType>();

		int currentLevel = 0;

		System.out.println("parsing for categories... (level " + currentLevel
				+ ")");
		subcategories = new ParserSubcategories()
				.getSubcategoriesFromCategory(MAIN_CATEGORY);
		allSubcategories.addAll(subcategories);

		while (currentLevel <= LEVEL) {

			System.out.println("parsing for subjects... (level " + currentLevel
					+ ")");

			subjects = new ParserSubjects()
					.getSubjectsFromCategories(subcategories, allSubjects);
			allSubjects.addAll(subjects);

			writeString(subcategories, filenameCategories, currentLevel);
			writeSubjectType(subjects, filenameSubjects, currentLevel);
			save(subjects, currentLevel);
			System.out.println("level " + currentLevel + " completed");
			System.out.println("parsing for categories... (level "
					+ (++currentLevel) + ")");
			subcategories = new ParserSubcategories()
					.getSubcategoriesFromCategories(subcategories,
							allSubcategories);
			allSubcategories.addAll(subcategories);
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.println("End: " + sdf.format(date));
	}

	private static void writeString(Set<String> strings, String filename,
			int currentLevel) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File(filename + currentLevel));
		for (String string : strings) {
			out.write(string + "\n");
		}
		out.close();
	}

	private static void writeSubjectType(Set<SubjectsType> subjectTypes, String filename,
			int currentLevel) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File(filename + currentLevel));
		for (SubjectsType subjectType : subjectTypes) {
			out.write(subjectType.getCategory() + "\t"
					+ subjectType.getSubject() + "\n");
		}
		out.close();
	}

	private static void save(Set<SubjectsType> subjects, int level)
			throws FileNotFoundException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		double currentNumber = 0.0;
		int totalNumber = subjects.size();
		double percent = 0.0;
		double currentPercent = -1.0;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/"
					+ DATABASE, "root", PASSWORD);

			for (SubjectsType subject : subjects) {

				preparedStatement = connection
						.prepareStatement("INSERT INTO subjects_" + level
								+ " VALUES(?, ?)");

				preparedStatement.setString(1, subject.getCategory());
				preparedStatement.setString(2, subject.getSubject());

				percent = (++currentNumber) / totalNumber * 100;
				if (percent > currentPercent + 1) {
					currentPercent = percent;
					System.out.println(Math.round(percent) + "% saved");
				}

				try {
					preparedStatement.executeUpdate();
				} catch (SQLException e1) {
					System.out.println("save(" + subject + "): " + e1);
				}
			}

			connection.close();
		} catch (SQLException e2) {
			System.out.println("save(): " + e2);
		}
	}
}