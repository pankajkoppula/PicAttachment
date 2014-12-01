package org.jaaps.example.picattachment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jaaps.example.picattachment.util.NetworkUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


@SuppressWarnings("unused")
public class MainActivity extends ActionBarActivity {

	private static final int REQUEST_FOR_IMAGE_CAPTURE = 1;
	private static final int RETURN_FROM_MAIL = 3;
	private Intent mailIntent;
	private Context context;
	
	private Button captureButton;
	private ImageView imageView;
    private static File destination;
    private static ArrayList<Uri> files = new ArrayList<Uri>();
    private String FILE_NAME_PREFIX = "image";
    static int fileNo = 0;
    static File attachmentsDirectory;
    static File outboxDirectory;
    final int BUFFER = 1024;
    
    private static String ATTACH_ROOT = null;
    private static String ATTACH_TEMP_LOCATION = null;
    private static String ATTACH_OUTBOX_LOCATION = null; 
    private static String className = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		className = MainActivity.class.getSimpleName();
		
		Log.d("ClassName", className);
		this.context = getApplicationContext();
		Log.d(className, this.context.toString());
		
		imageView = (ImageView)findViewById(R.id.image);
		
		if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }else{
        	if(createDirectories()){
        		captureImage();
        	}else{
        		Log.e(className, "Cannot create directories.");
        	}
        }
	}
	
	/**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
	
	/**
	 * Create file and capture image.
	 */
	void captureImage(){
		Log.d(className, "CaptureImage - start");
		try{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = createFile();
			Log.d(className, file.getAbsolutePath());
			if(file != null){
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent, REQUEST_FOR_IMAGE_CAPTURE);
			}else {
				Log.d(className, "File Not found");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.d(className, "CaptureImage - End");
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(className, "onActivityResult - Start");
//		Toast.makeText(context, "req-"+requestCode+"res-"+resultCode, Toast.LENGTH_SHORT).show();
		Log.d(className, "Request code : " + requestCode + ", Result code : "
				+ resultCode + ", package : " + (data != null ? data.getPackage() : "null"));
		/*super.onActivityResult(requestCode, resultCode, data);
	      Bitmap bp = (Bitmap) data.getExtras().get("data");
	      imgFavorite.setImageBitmap(bp);*/
	      
		try{
			//Image is captured and selected, confirm to attach more.
			if(requestCode == REQUEST_FOR_IMAGE_CAPTURE){
//				Toast.makeText(context, "requestcode"+requestCode,Toast.LENGTH_SHORT).show();
				if(resultCode == RESULT_OK) {
//					Toast.makeText(context, "resultCode"+resultCode,Toast.LENGTH_SHORT).show();
					attachMore();
				}
				//Back button is pressed on Camera
				//delete images from ATTACH_TEMP_LOCATION 
				else if((resultCode != Activity.RESULT_OK) || (resultCode == Activity.RESULT_CANCELED)){
					Log.d(className, "X button Pressed");
					Log.d(className, "Back Button Pressed - Delete files and show the camera.");
					deleteFile(attachmentsDirectory);
					files = new ArrayList<Uri>();
					Log.d(className, "Deleted.");
//					captureImage();
					finish();
				} 
			}else if (requestCode == RETURN_FROM_MAIL){
				Log.d(className, "Request Code : " + requestCode);
				if(resultCode == Activity.RESULT_OK){
					Log.d(className, "resultCode : result OK");
//					Toast.makeText(context, "Gmail-OK"+resultCode, Toast.LENGTH_SHORT).show();
					Log.d(className, "Gmail-OK"+resultCode);
				}else if (resultCode == Activity.RESULT_CANCELED){
					Log.d(className, "Gmail-Cancelled"+resultCode);
//					deleteFile(attachmentsDirectory);
//					files = new ArrayList<Uri>();
//					Toast.makeText(context, "Gmail-Cancelled"+resultCode, Toast.LENGTH_SHORT).show();
				}else {
//					Toast.makeText(context, "Gmail-New Code"+resultCode, Toast.LENGTH_SHORT).show();
					Log.d(className, "Gmail-New Code"+resultCode);
				}
//				deleteFile(attachmentsDirectory);
//				Log.d(className, "Deleted.");
				captureImage();
			}else {
//				Toast.makeText(context, "final else", Toast.LENGTH_SHORT).show();
//				Toast.makeText(context, "req-"+requestCode+"res-"+resultCode, Toast.LENGTH_SHORT).show();
				Log.d(className, "req-"+requestCode+"res-"+resultCode);
				Log.d(className, "final else");
				exitCamera();
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
    }
	
	void attachMore(){
		Log.d(className, "Attach More Files - Start");
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Attach More...");
        alertDialog.setMessage("Attach another ?");
//        alertDialog.setIcon(R.drawable.ic_action_cancel);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
	            captureImage();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if(NetworkUtil.isOnline(context)){
            		composeMail();
            	}else {
            		//TODO:call database.
            		Toast.makeText(context, "No Internet, Message Saved", Toast.LENGTH_SHORT).show();
            		 // Send intent to Compose View
                    Intent i = 
                    new Intent(getApplicationContext(), ComposeActivity.class);
                    startActivity(i);
            	}
            }
        });
 
        alertDialog.show();
        Log.d(className, "Attach More Files - End");
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		System.exit(0);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(className, "keyCode : " + keyCode);
		Log.d(className, "event : " + event.toString());
		return super.onKeyDown(keyCode, event);
	}
	
	public void exitCamera(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Confirm Exit...");
        alertDialog.setMessage("Are you sure you want to exit?");
//        alertDialog.setIcon(R.drawable.ic_action_cancel);
        
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
 
            // Write your code here to invoke YES event
//            Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            finish();
            }
        });
 
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // Write your code here to invoke NO event
//            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            captureImage();
            }
        });
 
        alertDialog.show();
	}
	
	void composeMail(){
		try {
           /* FileInputStream in = new FileInputStream(destination);
            
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; //shows 1/4th of the actual image size.
//                options.inSampleSize = 10; //Downsample 10x
            Bitmap userImage = BitmapFactory.decodeStream(in, null, options);
            imageView.setImageBitmap(userImage);*/
            Log.d(className, "Start - Compose Mail");
			if(files.size() > 1){
				mailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
			}else if (files.size() == 1){
				mailIntent = new Intent(android.content.Intent.ACTION_SEND);
			}else{
				Toast.makeText(MainActivity.this, "files null", Toast.LENGTH_SHORT).show();
			}
            mailIntent.setType("message/rfc822");
            mailIntent.setType("image/jpeg");
            mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"kpankaj.kumar@gmail.com"});
            mailIntent.putExtra(Intent.EXTRA_CC, new String[] {""});
            mailIntent.putExtra(Intent.EXTRA_BCC, new String[] {""});
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
            mailIntent.putExtra(Intent.EXTRA_TEXT, "Body Test");
            if(files.size() >1){
            	mailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            }else{
            	mailIntent.putExtra(Intent.EXTRA_STREAM, files.get(0));
            	if(files.get(0) != null) {
            	}else{
            		Toast.makeText(MainActivity.this, "files null", Toast.LENGTH_SHORT).show();
            	}
            }
            //TODO : should call default email client for older versions.
            mailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            startActivityForResult(mailIntent, RETURN_FROM_MAIL);
//            setResult(RESULT_OK, mailIntent);
            Log.d(className, "End - Compose Mail");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Create directories to store image files.
	 * @return boolean.
	 */
	@SuppressLint("SdCardPath")
	static boolean createDirectories(){
		boolean isCreated = false;
		try{
			Log.d(className, "Create Directory - Start");
			
			Log.d(className, android.os.Environment.getExternalStorageDirectory().toString());
			File f = new File("/mnt/sdcard/picattach/");
			f.mkdir();
			if(f.exists()){
				Log.d(className, "directory created.");
				isCreated = true;
			}else{
				Log.d(className, "directory not created.");
				isCreated = f.mkdir();
				Log.d(className, isCreated+"");
			}
			
			attachmentsDirectory = new File(f.getAbsolutePath()+"/attachmentz/");
			Log.d(className, attachmentsDirectory.getAbsolutePath());
			attachmentsDirectory.mkdir();
			outboxDirectory = new File(f.getAbsolutePath()+"/pending/");
			Log.d(className, outboxDirectory.getAbsolutePath());
			outboxDirectory.mkdir();
			
			Log.d(className, "Create Directory - End");
		}catch (Exception e) {
            e.printStackTrace();
            isCreated = false;
        }
		return isCreated;
	}
	
	/**
	 * Create a file with convention : imagexx.jpg
	 * @return file.
	 */
	File createFile(){
		boolean isCreated = false;
		File file = null;
		try{
			Log.d(className, "Create File - Start");
			file = new File(attachmentsDirectory.getAbsolutePath(), (FILE_NAME_PREFIX + fileNo + ".jpg"));
			Log.d(className, "filename : " + file.getAbsolutePath());
			fileNo++;
			files.add(Uri.fromFile(file));
			if(file != null){
				isCreated = true;
			}else{
				isCreated = false;
				Log.e(className, "File cannot be created");
			}
			Log.d(className, "Create File - End");
		}catch (Exception e) {
			Log.e(className, "Exception in Create File");
            e.printStackTrace();
            
            isCreated = false;
        }
		return file;
	}
	
	void readDirectory(){
		try{
			
		}catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private static void deleteFile(File resource) throws IOException {
		if (resource.isDirectory()) {
			File[] childFiles = resource.listFiles();
			for (File child : childFiles) {
				deleteFile(child);
				files.remove(child.toURI());
				Log.d(className, "Deleting : " + child.getAbsolutePath());
				child.delete();
			}
		} else {
			resource.delete();
			files.remove(resource.toURI());
			Log.d(className, "Deleting : " + resource.getAbsolutePath());
		}
		fileNo = 0;
		Log.d(className, "files size : " + files.size());
	}
}
