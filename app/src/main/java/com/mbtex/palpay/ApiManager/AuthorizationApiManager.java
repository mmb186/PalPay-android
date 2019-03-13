package com.mbtex.palpay.ApiManager;

import android.content.Context;
import android.graphics.Bitmap;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationApiManager extends ApiManager {
    private static AuthorizationApiManager _authorizationManager;
    private RequestQueue _queue;
    private ImageLoader _imageLoader;
    private static Context _ctx;

    private AuthorizationApiManager(Context context) {
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

    public static synchronized AuthorizationApiManager getAuthorizationManager(Context ctx) {
        if (_authorizationManager == null) {
            _authorizationManager = new AuthorizationApiManager(ctx);
        }
        return _authorizationManager;
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


    public void loginUser(String username, String password) {}

    public void registerUser(JSONObject formData)
    {
        String register_user_route = _baseURL + "/create_user/";
        JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, register_user_route, formData,

        new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {
                String res = "";
                Toast.makeText(_ctx, "Response: " + response.toString(), Toast.LENGTH_SHORT).show();
                // TODO: Change intent to new Dashboard Screen.
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
//                headers.put("Authorization", "" + ""); //put your token here
                return headers;
            }
        };
        _queue.add(registerUserRequest);
    }

    public Boolean logoutUser (String username)
    {
        return false;
    }

    public String refereshToken()
    {
        return "";
    }

}
