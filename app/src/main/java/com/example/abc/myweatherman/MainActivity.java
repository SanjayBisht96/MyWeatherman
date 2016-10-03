package com.example.abc.myweatherman;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText place;
   String message;
    TextView weather;

    public void weatherfind(View view){
try {
    String encodedplace = URLEncoder.encode(place.getText().toString(), "UTF-8");
  DownloadTask task=new DownloadTask();
    task.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedplace+"&APPID=b986f10a1c9ad8735fc0a7b64188fe45");
}
catch (Exception e){
    e.printStackTrace();
    Toast.makeText(getApplicationContext(),"Unable to find weather api", Toast.LENGTH_SHORT).show();
}

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         place=(EditText)findViewById(R.id.editText);
        weather=(TextView) findViewById(R.id.textView);

    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
               URL url;
            HttpURLConnection connection=null;
            try{
                String result="";
                 url=new URL(urls[0]);

                connection=(HttpURLConnection) url.openConnection();
              //  connection.connect();
                InputStream in = connection.getInputStream() ;
                InputStreamReader reader= new InputStreamReader(in);
                int data=reader.read();
                 while (data!=-1){
                     char current=(char)data;
                     result+=current;
                     data=reader.read();
                 }
                return result;
            }
            catch (Exception e)
            {
              e.printStackTrace();
                Toast.makeText(getApplicationContext(),"could not find weather url",Toast.LENGTH_LONG).show();
            }
            return null;
        }
        @Override

        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
           Log.i("Website result",result);
            try {
                JSONObject json=new JSONObject(result);
               String weatherinfo=json.getString("weather");

               JSONArray arr=new JSONArray(weatherinfo);
               for (int i=0;i<arr.length();i++)
                {
                    JSONObject jsonpart=arr.getJSONObject(i);
                    String description="";

                    description=jsonpart.getString("description");
                    if(description!="")
                    {
                        message+=description;
                    }
                }
            /*    String tempinfo=json.getString("main");

                JSONArray arr2=new JSONArray(weatherinfo);
                for (int i=0;i<arr2.length();i++)
                {
                    JSONObject jsonpart2=arr2.getJSONObject(i);
                    String temp=jsonpart2.keys();
                }*/
                if(message!="") {
                    weather.setText(message);

                }
                else{
                    Toast.makeText(getApplicationContext(),"could not find weather messege null",Toast.LENGTH_LONG).show();
                }
            }
              catch (Exception e){
                  e.printStackTrace();
                  Toast.makeText(getApplicationContext(),"could not find weather json not working",Toast.LENGTH_LONG).show();
              }
        }
    }



}
