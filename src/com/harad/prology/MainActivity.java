package com.harad.prology;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TextView;
import android.os.*;
import java.io.*;
import java.util.regex.*;
import android.text.format.*;
import android.view.*;
import android.text.*;
import android.view.ContextMenu.*;
import android.widget.*;

public class MainActivity extends Activity
{
    private static final int GL_REQUEST_CODE = 1001;

	final int MENU_COPY = 1;
	final int MENU_SHARE = 2;
	final int MENU_SAVE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Remove title bar
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		startTestGLForGPUInfo();
		Toast.makeText(this, "Press long on text to manage your data", Toast.LENGTH_LONG).show();
	}
	
// GPUinfo
	
	private void startTestGLForGPUInfo()
	{
		Intent i = new Intent(MainActivity.this, TestGL.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(i, GL_REQUEST_CODE);
		overridePendingTransition(0,0);
	}
	
// CPUinfo
	
	public String readFile(){
        BufferedReader rdr;
		String proc = "";
		String line;
		int lineNumber = 0;
        try {
            rdr = new BufferedReader(new FileReader("/proc/cpuinfo"));

			while ((line = rdr.readLine()) != null) {
				lineNumber++;

				Matcher matcher = Pattern.compile("Processor\\s*: (.*)").matcher(line);
				if (matcher.find()) {
					proc = matcher.group(1);
				}
			}

        } catch (Exception e) {
            e.printStackTrace();
        }
        return proc;
		}
		
// CORES count
	
	public String Cores() {

        String file_contents = readFileC();
        int count = 0;

		String patternString = "processor";

		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(file_contents);

		while (matcher.find()) {
		 	count++;
			}
		if (count < 1){
			count = count + 1;
			return Integer.toString(count);
		}
		else
		return Integer.toString(count);
		}
	
	public String readFileC(){
        char buf[] = new char[512];
        FileReader rdr;
        String contents = "";
        try {
            rdr = new FileReader("/proc/cpuinfo");
            int s = rdr.read(buf);
            for(int k = 0; k < s; k++){
                contents+=buf[k];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }
	
// MAX frequency
	
	public int freq(){
		String number = readFileF();
		int f = Integer.parseInt(number.trim())/1000;
    	return f;
		
	}
	
	public String readFileF(){
        char buf[] = new char[512];
        FileReader rdr;
        String max = "";
        try {
            rdr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            int s = rdr.read(buf);
            for(int k = 0; k < s; k++){
                max+=buf[k];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max;
    }

// MEMORY	
	
	public String sd_card_free(){
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long availBlocks = stat.getBlockCount();
		long blockSize = stat.getBlockSize();
		return Formatter.formatFileSize(this, availBlocks * blockSize);
	}

	public String data(){
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getBlockCount();
		return Formatter.formatFileSize(this, availableBlocks * blockSize);
	}
	
// RAM
	
	public String readFileM(){
        BufferedReader rdr;
		String mmm = "";
		String line;
		int lineNumber = 0;
        try {
            rdr = new BufferedReader(new FileReader("/proc/meminfo"));

			while ((line = rdr.readLine()) != null) {
				lineNumber++;

				Matcher matcher = Pattern.compile("MemTotal:\\s*\\s*\\s* (.*)\\s*kB").matcher(line);
				if (matcher.find()) {
					mmm = matcher.group(1);
				}
			}

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mmm;
	}
	
	public int rmem(){
		String number = readFileM().trim();
		int f = Integer.parseInt(number)/1024;
    	return f;
	}
	
// Screen
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public String res(){
		int screenWidth = 0;
		int screenHeight = 0;
		
		if (Build.VERSION.SDK_INT >= 11) {
	        Point size = new Point();
	        try {
	            this.getWindowManager().getDefaultDisplay().getRealSize(size);
	            screenWidth = size.x;
	            screenHeight = size.y;
	        } catch (NoSuchMethodError e) {
	            Log.i("error", "it can't work");
	        }
	    } else {
	        DisplayMetrics metrics = new DisplayMetrics();
	        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        screenWidth = metrics.widthPixels;
	        screenHeight = metrics.heightPixels;
	    }
		String result = Integer.toString(screenWidth) + "x" + Integer.toString(screenHeight);
		return result;
	}
	
//Diagonal
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public float diagonal(){
		Point size = new Point();

		this.getWindowManager().getDefaultDisplay().getRealSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;
		
    	DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
		float height=screenWidth/(metrics.xdpi*1);
        float width=screenHeight/(metrics.ydpi*1);
        float result = FloatMath.sqrt((height*height)+(width*width));

		return result;
    }
	
//PPI

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public Float ppi(){
		int screenWidth = 0;
		int screenHeight = 0;

		if (Build.VERSION.SDK_INT >= 11) {
	        Point size = new Point();
	        try {
	            this.getWindowManager().getDefaultDisplay().getRealSize(size);
	            screenWidth = size.x;
	            screenHeight = size.y;
	        } catch (NoSuchMethodError e) {
	            Log.i("error", "it can't work");
	        }
	    } else {
	        DisplayMetrics metrics = new DisplayMetrics();
	        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        screenWidth = metrics.widthPixels;
	        screenHeight = metrics.heightPixels;
	    }
		
		float Width = screenWidth;
		float Height = screenHeight;

		Point size = new Point();

		this.getWindowManager().getDefaultDisplay().getRealSize(size);
		int screenWidthd = size.x;
		int screenHeightd = size.y;

    	DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

		float height=screenWidthd/(metrics.xdpi*1);
        float width=screenHeightd/(metrics.ydpi*1);
        float dig = FloatMath.sqrt((height*height)+(width*width));

		float result= FloatMath.sqrt((Height*Height)+(Width*Width))/dig;
		
		return result;
    }
	
// Result
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == GL_REQUEST_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				// GPU 
				String glInfo = intent.getStringExtra("GLInfo");
				// Processor
				String file_proc = readFile();
				// Cores
				String count = Cores();
				// max frequency
				int max = freq();
				// Memory SD
				String mem = sd_card_free();
				// Memory system
				String dat = data();
				// Memory RAM
				int ramm = rmem();
				// Resolution
				String ress = res();
				//Diagonal
				double d = Math.round(diagonal()*10)/10.0;
				String dig = Double.toString(d);
				//ppi
				int p = Math.round(ppi()*1)/1;
				String pppi = Integer.toString(p);
				
				TextView tvInfo = (TextView)findViewById(R.id.tvInfo);
				tvInfo.setText(
						"Model: " + Build.MODEL
						+ "\n" + "Brand: " + Build.BRAND
						+ "\n" + "Board: " + Build.BOARD
						
						+ "\n" + "CPU Chip: " + Build.HARDWARE
						+ "\n" + "Processor: " + file_proc
						+ "\n" + "Max CPU frequency: " + max + "Mhz"
						+ "\n" + "Number of cores: " + count
						
						+ "\n" + glInfo
						
						+ "\n" + "RAM: " + ramm + "MB"	
						+ "\n" + "System storage: " + dat						
						+ "\n" + "Internal storage: " + mem
						
						+ "\n" + "Android version: " + Build.VERSION.RELEASE
						+ "\n" + "SDK version: " + Build.VERSION.SDK
						
						+ "\n" + "Screen resolution: " + ress
						+ "\n" + "Screen diagonal: " + dig
				    	+ "\n" + "Screen ppi: " + pppi
						);
						
				registerForContextMenu(tvInfo);
						
			}
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		 menu.add(0, MENU_COPY, 0, "Copy to the clipboard");
	     menu.add(0, MENU_SHARE, 0, "Send via Email");
	     menu.add(0, MENU_SAVE, 0, "Save to SDcard");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		TextView tvInfo = (TextView)findViewById(R.id.tvInfo);
		switch (item.getItemId()) {
				
			case MENU_COPY:
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
				clipboard.setText(tvInfo.getText());
				Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
				break;
	    	case MENU_SHARE:
	    		Intent email = new Intent(Intent.ACTION_SEND);
	    		email.putExtra(Intent.EXTRA_EMAIL, new String[]{""});		  
	    		email.putExtra(Intent.EXTRA_SUBJECT, "");
	    		email.putExtra(Intent.EXTRA_TEXT, tvInfo.getText());
	    		email.setType("message/rfc822");
	    		startActivity(Intent.createChooser(email, "Choose an Email client :"));
				break;
	    	case MENU_SAVE:
	    		try {
	    			File myFile = new File(Environment.getExternalStorageDirectory() + "/properties.txt");
	    			myFile.createNewFile();
	    			FileOutputStream fOut = new FileOutputStream(myFile);
	    			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	    			myOutWriter.append(tvInfo.getText());
	    			myOutWriter.close();
	    			fOut.close();
	    			Toast.makeText(getBaseContext(), "Saved to properties.txt", Toast.LENGTH_SHORT).show();
	    		} catch (Exception e) {
	    			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), Environment.getExternalStorageDirectory() + "/properties.txt", Toast.LENGTH_SHORT).show();
	    		}
	    		break;
		}
		return super.onContextItemSelected(item);
	}
	
	
}
