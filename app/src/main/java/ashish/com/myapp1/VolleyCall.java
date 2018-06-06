package ashish.com.myapp1;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyCall {
    private static VolleyCall volleyCall;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleyCall(Context con){
        context = con;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized VolleyCall getInstance(Context con){
        if(volleyCall==null)
            volleyCall = new VolleyCall(con);
        return volleyCall;
    }

    public<T>  void addToRequestQueue(Request<T> req){
        requestQueue.add(req);
    }
}
