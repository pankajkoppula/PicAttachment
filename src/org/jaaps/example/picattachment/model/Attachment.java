package org.jaaps.example.picattachment.model;

public class Attachment {

	int id;
	String location;
	int mailId;
	
    // Table Names
    private static final String ATTACHMENT_INFO = "attachmentInfo";
    
 // Common column names
    private static final String ID = "id";
    private static final String LOCATION = "recipientName";
    private static final String MAIL_ID = "mailId";
    private static final String CREATED_ON = "createdOn";
    
    // Table Create Statements
    public static final String CREATE_TABLE_ATTACHMENT_INFO = "CREATE TABLE "
            + ATTACHMENT_INFO + "(" 
				+ ID + " INTEGER PRIMARY KEY," 
				+ LOCATION + " TEXT," 
				+ MAIL_ID + " INTEGER," 
				+ CREATED_ON + " DATETIME" 
			+ ")";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getMailId() {
		return mailId;
	}

	public void setMailId(int mailId) {
		this.mailId = mailId;
	}
}
