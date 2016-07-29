package com.example.jsonparsing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ListView listViewComments;
	Button buttonLoad;
	ArrayList<String> listComments = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		buttonLoad = (Button) findViewById(R.id.button1);
		listViewComments = (ListView) findViewById(R.id.listView1);
		adapter = new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_list_item_1, listComments);
		listViewComments.setAdapter(adapter);

		// load JSON Comments
		buttonLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String commentUrl = "http://jsonplaceholder.typicode.com/comments";
				LoadCommentTask task = new LoadCommentTask();
				task.execute(commentUrl);
			}
		});
	}// eof oncreate

	class LoadCommentTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String Url = params[0];
			HttpGet getRequest = new HttpGet(Url);
			String result = "";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(getRequest);
				InputStream is = response.getEntity().getContent();

				// convert i/p stream in String
				InputStreamReader reader = new InputStreamReader(is);
				BufferedReader bufferedReader = new BufferedReader(reader);
				while (true) {
					String line = bufferedReader.readLine();
					if (line == null)
						break;
					else {
						result += line;
					}
				}// end of while
				bufferedReader.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return result;
		}// end of do IN background

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MainActivity.this, result, 5).show();
			Log.e("JSON", result);

			// JSON Parsing
			try {
				JSONArray jarray = new JSONArray(result);
				for (int i = 0; i < jarray.length(); i++) {

					JSONObject jobj = jarray.getJSONObject(i);
					int id = jobj.getInt("id");
					String name = jobj.getString("name");
					listComments.add(id+","+name);
				}
				//refresh
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
