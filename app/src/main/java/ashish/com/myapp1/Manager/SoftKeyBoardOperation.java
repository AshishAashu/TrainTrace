package ashish.com.myapp1.Manager;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyBoardOperation {
    public static void hideSoftKeyboard(View view, Activity activity) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(activity.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
