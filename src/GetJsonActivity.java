package com.qihoo.browser.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.qihoo.browser.util.JLog;

import org.chromium.chrome.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxin on 2/7/17.
 */

public class GetJsonActivity extends Activity {
    private ListView mListView;

    private static String SERVER_URL = "http://ce.sysu.edu.cn/hope/hopedairyjson/Index.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_json_main);
        mListView = (ListView) findViewById(R.id.lv_main);

        new NewsAsyncTask().execute(SERVER_URL);

    }

    class NewsAsyncTask extends AsyncTask<String, Void, List<NewsBean>> {

        @Override
        protected List<NewsBean> doInBackground(String... params) {
            JLog.i();
//            return getJsonData(params[0]);
//            return getJsonDataByGson(params[0]);
            return getJsonDataBy_Volley_and_Gson(params[0]);

        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeanList) {
            super.onPostExecute(newsBeanList);

            //ahking
            NewsAdapter newsAdapter = new NewsAdapter(GetJsonActivity.this, newsBeanList);
            mListView.setAdapter(newsAdapter);
        }

        private List<NewsBean> getJsonData(String serverUrl) {
            List<NewsBean> newsBeanList = new ArrayList<NewsBean>();
            String jsonString;

JLog.i(serverUrl);
            try {
                HttpURLConnection conn = null;
                URL url = new URL(serverUrl);
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                if (code == 200) {
                    jsonString = readStream(conn.getInputStream());
                    NewsBean newsBean;
                    JSONObject jsonObject;
                    jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for(int i = 0; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        newsBean = new NewsBean();
                        newsBean.title = jsonObject.getString("name");
                        newsBean.date = jsonObject.getString("date");
                        newsBean.content = jsonObject.getString("content");

                        JLog.i(newsBean.title);

                        newsBeanList.add(newsBean);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return newsBeanList;

        }

        private List<NewsBean>  getJsonDataByGson (String serverUrl) {
            String jsonString;
            JSonBean jsonBean = null;
            List<NewsBean> newsBeanList = new ArrayList<NewsBean>();
JLog.i(serverUrl);
            try {
                HttpURLConnection conn = null;
                URL url = new URL(serverUrl);
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                if (code == 200) {
                    jsonString = readStream(conn.getInputStream());

                    Gson gson = new Gson();
                    jsonBean = gson.fromJson(jsonString, JSonBean.class);

                    NewsBean[] nb = jsonBean.data;
                    for(int i=0; i<nb.length;i++) {
                        newsBeanList.add(nb[i]);
                    }
               }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return newsBeanList;
        }

        private List<NewsBean>  getJsonDataBy_Volley_and_Gson (String serverUrl) {
            JLog.i(serverUrl);
            final List<NewsBean> newsBeanList = new ArrayList<NewsBean>();

            RequestQueue mQueue = Volley.newRequestQueue(GetJsonActivity.this);
            StringRequest stringRequest = new StringRequest(serverUrl,new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    JLog.i(response);
                    JSonBean jsonBean = null;
                    Gson gson = new Gson();
                    jsonBean = gson.fromJson(response, JSonBean.class);

                    NewsBean[] nb = jsonBean.data;
                    for(int i=0; i<nb.length;i++) {
                        newsBeanList.add(nb[i]);
                    }

                    NewsAdapter newsAdapter = new NewsAdapter(GetJsonActivity.this, newsBeanList);
                    mListView.setAdapter(newsAdapter);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    JLog.i("volley error : " + error);
                }
            });

            mQueue.add(stringRequest);

            return newsBeanList;//only demo, return newsBeanList 的值并没有被使用，如果调用这个方法的话.
        }

        private String readStream(InputStream is) {
            InputStreamReader isr;
            String result= "";
            try {
                String line = "";
                isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                while ((line = br.readLine()) != null) {
                    result += line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
