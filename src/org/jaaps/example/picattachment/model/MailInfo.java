package org.jaaps.example.picattachment.model;

public class MailInfo {

	int id;
	String subject;
	String message;
	
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Table Names
    private static final String MAIL_INFO = "mailInfo";
    
 // Common column names
    private static final String ID = "id";
    private static final String SUBJECT = "subject";
    private static final String MESSAGE = "message";
    private static final String CREATED_ON = "createdOn";
    
    // Table Create Statements
    public static final String CREATE_TABLE_MAIL_INFO = "CREATE TABLE "
            + MAIL_INFO + "(" 
				+ ID + " INTEGER PRIMARY KEY," 
				+ SUBJECT + " TEXT," 
				+ MESSAGE + " TEXT," 
				+ CREATED_ON + " DATETIME" 
			+ ")";
    
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static String getCreatedOn() {
		return CREATED_ON;
	}
}
