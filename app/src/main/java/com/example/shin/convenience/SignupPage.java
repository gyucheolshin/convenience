package com.example.shin.convenience;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shin.convenience.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SignupPage extends Activity {
    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextId = (EditText) findViewById(R.id.new_id);
        editTextPw = (EditText) findViewById(R.id.new_pw);
        editTextDay = (EditText) findViewById(R.id.new_day);

    }
    public void insert(View view) {
        String Id = editTextId.getText().toString();
        String Pw = editTextPw.getText().toString();
        String Day = editTextDay.getText().toString();

        insertoToDatabase(Id, Pw, Day);
    }
    private void insertoToDatabase(String Id, String Pw, String Day) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignupPage.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String Pw = (String) params[1];
                    String Day = (String) params[2];

                    String link = "http://202.31.147.55/login13_php.php";
                    String data = URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("Pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");
                    data += "&" + URLEncoder.encode("Day", "UTF-8") + "=" + URLEncoder.encode(Day, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id, Pw, Day);
    }
}