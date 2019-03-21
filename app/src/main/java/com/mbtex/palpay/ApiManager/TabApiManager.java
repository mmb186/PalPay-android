package com.mbtex.palpay.ApiManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mbtex.palpay.Dashboard;
import com.mbtex.palpay.Tabs.Tab;
import com.mbtex.palpay.User.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabApiManager extends ApiManager{
    private static TabApiManager _tabApiManager;
    private RequestQueue _queue;
    private ImageLoader _imageLoader;
    private static Context _ctx;

    private TabApiManager(Context context) {
        _ctx = context;
        _queue = getRequestQueue();


        _imageLoader = new ImageLoader(_queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap> (20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized TabApiManager getTabApiManager(Context ctx) {
        if (_tabApiManager == null) {
            _tabApiManager = new TabApiManager(ctx);
        }
        return _tabApiManager;
    }

    private RequestQueue getRequestQueue() {
        if (_queue == null) {
            _queue = Volley.newRequestQueue(_ctx);
        }
        return _queue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader get_imageLoader() {
        return _imageLoader;
    }


    /*
     * Api create a new Tab.
     * */
    public void create_new_tab(JSONObject tabData, final User current_user, final AppCompatActivity callingActivity) {
        String create_tab_route = _baseURL + "/create_new_tab/";

        JsonObjectRequest createTabRequest = new JsonObjectRequest(Request.Method.POST, create_tab_route, tabData,
                new Response.Listener<JSONObject> () {
                    @Override
                    public void onResponse(JSONObject response) {
                        String res = "";
                        callingActivity.startActivity(current_user.createIntentAndAddSelf(callingActivity, Dashboard.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String e = error.getMessage();
                        if (e.equals("DNE")) {
                            // user does not exist
                        } else if (e.equals("Can't add yourself")) {
                            // can't add yourself
                        } else
                            Toast.makeText(_ctx, "Response: " + e, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", current_user.getAuthToken()); //put your token here
                return headers;
            }
        };

        _queue.add(createTabRequest);
    }

    public void get_user_tabs(final User current_user, final Dashboard actingActivity, final ArrayList<Tab> tab, final double balance)
    {
        String get_user_tab_route = _baseURL + "/get_all_user_tabs";
        ArrayList<Tab> userTabs;

        JsonObjectRequest createTabRequest = new JsonObjectRequest(Request.Method.GET, get_user_tab_route, null,
                new Response.Listener<JSONObject> () {
                    AppCompatActivity temp = actingActivity;
                    @Override
                    public void onResponse(JSONObject response) {
                        String res = "";

//                        try {
//                            balance = (double)response.get("balance") .get("balance");
//
//                            for (int i  = 0; i < response.get("data").get("tabs").values.size(); i++)
//                            {
//                                JSONObject temp = response.get("data").get("tabs").values.get(i);
//                                Tab t = new Tab(
//                                        temp.get("name"),
//                                        temp.get("Status"),
//                                        temp.get("balance"),
//                                        i,
//                                        temp.get("user_tab_status")
//                                        );
//                                tab.add(t);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String e = error.getMessage();
                        Toast.makeText(_ctx, "Response: " + e, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", current_user.getAuthToken()); //put your token here
                return headers;
            }
        };
        _queue.add(createTabRequest);
    }
}
