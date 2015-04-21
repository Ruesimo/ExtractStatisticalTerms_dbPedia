/**
 * @author Simon
 */
package ruesimo.com.extract_statistical_terms_dbPedia;

public class SubjectsType {
	
	private String category;
	private String subject;
	
	public SubjectsType(String category, String subject) {
		this.category = category;
		this.subject = subject;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getSubject() {
		return subject;
	}
}
