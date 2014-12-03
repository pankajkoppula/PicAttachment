package org.jaaps.example.picattachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ComposeActivity extends ActionBarActivity{

	Context ctx;
	String className;
	EditText editCc;
	EditText editBcc;
	EditText editSubject;
	LinearLayout composeCcLayout;
	LinearLayout composeBccLayout;
	LinearLayout composeBodyLayout;
	String attachmentDir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compose);
		className = ComposeActivity.class.getSimpleName();
		
		ctx = getApplicationContext();
		
		// Get intent data
        Intent i = getIntent();
        
		attachmentDir = i.getExtras().getString("attachmentDirectory");
		
		composeCcLayout = (LinearLayout)findViewById(R.id.layoutCc);
		composeBccLayout = (LinearLayout)findViewById(R.id.layoutBcc);
		composeBodyLayout = (LinearLayout)findViewById(R.id.layoutBody);
		
		editCc = (EditText)findViewById(R.id.inputCc);
		editBcc = (EditText)findViewById(R.id.inputBcc);
		editSubject = (EditText)findViewById(R.id.inputSubject);
		
		editSubject.setHintTextColor(Color.GRAY);
		
		composeCcLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editCc.setVisibility(View.VISIBLE);
			}
		});
		
		composeBccLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editBcc.setVisibility(View.VISIBLE);
			}
		});
		
		addViewsToBodyLayout(ctx, composeBodyLayout); 
		
	}
	
	/**
	 * Add sub views to the Content Section of the compose mail.
	 * @param ctx
	 * @param bodyLayout
	 */
	private void addViewsToBodyLayout(Context ctx, LinearLayout bodyLayout){
		
		/**
		 * Add Edit Text to Body Layout.
		 */
		EditText bodyText = new EditText(ctx);
		LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
										, LinearLayout.LayoutParams.WRAP_CONTENT);
		editTextParams.setMargins(16, 13, 0, 13);
		bodyText.setLayoutParams(editTextParams);
		bodyText.setHint("your content here..");
		bodyText.setHintTextColor(Color.GRAY);
		bodyText.setTextSize(18.0f);
		bodyText.setTextColor(Color.DKGRAY);
		bodyText.setBackgroundColor(Color.parseColor("#00000000"));
		bodyText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		bodyLayout.addView(bodyText);
		
		TableLayout table = createImageTableView(ctx);
		HorizontalScrollView hsv = new HorizontalScrollView(ctx);
		hsv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	    	     LayoutParams.WRAP_CONTENT));
		
		hsv.addView(table);
		bodyLayout.addView(hsv);
	}
	
	/**
	 * Create a Table layout containing the images.
	 * @param ctx
	 * @return table layout with images.
	 */
	private TableLayout createImageTableView(Context ctx){
		TableLayout imageTable = new TableLayout(ctx);
		imageTable.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	    	     LayoutParams.WRAP_CONTENT));
		imageTable.setBackgroundColor(Color.BLACK);
		
		//Create TableRows
	    TableRow row = new TableRow(ctx);
	    row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	    	     LayoutParams.WRAP_CONTENT));
//	    row.setBackgroundColor(Color.BLACK);
//	    row.setBackgroundResource(R.drawable.table_row_border);
	    row.setGravity(Gravity.LEFT);
	    
	    File resource = new File(attachmentDir);
	    if (resource != null && resource.isDirectory()) {
	    	try{
	    		File[] childFiles = resource.listFiles();
	    		Log.d(className, "file size - " + childFiles.length);
	    		for(File file : childFiles){
	    			ImageView image = new ImageView(ctx);
	    			BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
	    			bmpOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, bmpOptions);
	    			image.setImageBitmap(bitmap);
	    			image.setAdjustViewBounds(true);
	    			image.setScaleType(ScaleType.FIT_XY);
	    			image.setPadding(10, 10, 10, 10);
	    			row.addView(image, 350, 350);
	    		}
	    	}catch (FileNotFoundException fe){
	    		Log.e(className, "File Not Found Exception : " + fe.getMessage());
	    		fe.printStackTrace();
	    	}catch (Exception e){
	    		Log.e(className, " Exception : " + e.getMessage());
	    		e.printStackTrace();
	    	}
		}else{
			Log.d(className, attachmentDir + " is not a directory");
		}
	    imageTable.addView(row);
		return imageTable;
	}
}
