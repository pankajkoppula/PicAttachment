package org.jaaps.example.picattachment.util;

import org.jaaps.example.picattachment.model.Attachment;
import org.jaaps.example.picattachment.model.MailInfo;
import org.jaaps.example.picattachment.model.Recipient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUtil extends SQLiteOpenHelper {

	// Database Name
    private static final String DATABASE_NAME = "mailManager";
    
 // Database Version
    private static final int DATABASE_VERSION = 1;
    
 // Logcat tag
    private static final String LOG = "DatabaseUtil";
	
	public DatabaseUtil(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		 db.execSQL(MailInfo.CREATE_TABLE_MAIL_INFO);
		 db.execSQL(Recipient.CREATE_TABLE_RECIPIENT_INFO);
		 db.execSQL(Attachment.CREATE_TABLE_ATTACHMENT_INFO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + MailInfo.CREATE_TABLE_MAIL_INFO);
		 db.execSQL("DROP TABLE IF EXISTS " + Recipient.CREATE_TABLE_RECIPIENT_INFO);
		 db.execSQL("DROP TABLE IF EXISTS " + Attachment.CREATE_TABLE_ATTACHMENT_INFO);
		 
		// create new tables
        onCreate(db);
	}
}
