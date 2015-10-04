package fi.ptm.parsejsonexample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author PTM
 */
public class MainActivity extends Activity {
    private JSONArray highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FetchDataTask task = new FetchDataTask();
        task.execute("http://ptm.fi/android/highscore.json");
    }

    class FetchDataTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            JSONObject json = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                json = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {
            StringBuffer text = new StringBuffer("");
            try {
                highscores = json.getJSONArray("highscores");
                for (int i=0;i < highscores.length();i++) {
                    JSONObject hs = highscores.getJSONObject(i);
                    text.append(hs.getString("name") + ":" + hs.getString("score")+"\n");
                }
            } catch (JSONException e) {
                Log.e("JSON", "Error getting data.");
            }

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(text);
        }
    }

}
