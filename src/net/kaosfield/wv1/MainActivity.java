package net.kaosfield.wv1;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {

	private WebView webView = null;

	private ValueCallback<Uri> mUploadMessage;

	private final static int FILECHOOSER_RESULTCODE = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage) return;
				Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
				mUploadMessage.onReceiveValue(result);
				mUploadMessage = null;
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FILECHOOSER_RESULTCODE);
			}
		});
		webView.loadUrl("http://google.com");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		// If it wasn't the Back key or there's no web page history, bubble up to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}

}
