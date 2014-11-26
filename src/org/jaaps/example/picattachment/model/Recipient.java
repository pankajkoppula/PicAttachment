package org.jaaps.example.picattachment.model;

public class Recipient {

	int id;
	String recipientName;
	int mailId;
	
    // Table Names
    public static final String RECIPIENT_INFO = "recipientInfo";
    
 // Common column names
    public static final String ID = "id";
    public static final String NAME = "recipientName";
    public static final String MAIL_ID = "mailId";
    public static final String CREATED_ON = "createdOn";
    
    // Table Create Statements
    public static final String CREATE_TABLE_RECIPIENT_INFO = "CREATE TABLE "
            + RECIPIENT_INFO + "(" 
				+ ID + " INTEGER PRIMARY KEY," 
				+ NAME + " TEXT," 
				+ MAIL_ID + " INTEGER," 
				+ CREATED_ON + " DATETIME" 
			+ ")";
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public int getMailId() {
		return mailId;
	}
	public void setMailId(int mailId) {
		this.mailId = mailId;
	}
	public static String getCreatedOn() {
		return CREATED_ON;
	}
	
}
