package cordova.plugin.egeesappel;

//Android imports
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;

// Cordova imports
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.LOG;

// JSON imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// DIEHL imports
import com.diehl.metering.izar.license.api.LicenseService;
import com.diehl.metering.izar.module.readout.text.impl.RadioInterpret;

public class EgeeSappel extends CordovaPlugin {
    private static final String TAG = "EgeeSappel";

    public static final String ACTION_GET_LICENCE = "getLicence";
    public static final String ACTION_RADIO_INTERPRET = "radioInterpret";
    public static final String ACTION_RADIO_INTERPRET_HEAD = "radioInterpretHead";

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        LOG.d(TAG, "Initializing EgeeSappel");
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (ACTION_GET_LICENCE.equals(action)) {
                this.getLicence(callbackContext);
                return true;
            } else {
                if (ACTION_RADIO_INTERPRET.equals(action)) {
                    this.radioInterpret(args, callbackContext);
                    return true;
                } else {
                    if (ACTION_RADIO_INTERPRET_HEAD.equals(action)) {
                        this.radioInterpret(args, callbackContext);
                        return true;
                    }
                }
            }
            callbackContext.error("Invalid Action");
            return false;
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
            return false;
        }

    }

    private void getLicence(CallbackContext callback) {
        try {
            String lic = "010000001000E71F200000001F454745450000000000000000000000000000000022C184F465E9D474F85F152C8899D9E4D3DE103DF2B7F6F58387978F426CF4219555EF54322B56093F38F87AB4FE95B442C8BADCD350C2D13BF7CC04374555CEF5CDA422E67E1E3CE4C9B3B2FA597962F0E0225A5B3634A72D1B2F60773A8D2CECB6117200601989C1AF84E08865A0EE693FB5D33E9441A3B7918C3621C17F8879201512C0D39F1C7059F99BE1AFB83A50CBD3B2CD6CEB4000DB2375EF943F3EB6536BA090B8164DC17FB5632E51E8FD22208C7D2F34F067EB5E8B654147E7086C5873DCECF9388EEB18B08FEB63D8D683C299410AD6714A144A90A852ED267779DE669F26E02D2B2807328EF9EB0F0653BF855A6814DB99A34F04A06B9459A5";
            boolean isValid = LicenseService.getInstance().read(lic).validate();
            callback.success("" + isValid);

        } catch (Exception ex) {
            callback.error("No license: " + ex);
        }
    }

    private void radioInterpret(JSonArray args, CallbackContext callback) {
        if (args != null) {
            try {
                String frame = args.getJSONObject(0).getString("param");
                String result = RadioInterpret.INSTANCE.interpret(frame);

                callback.success("" + (result));

            } catch (Exception ex) {
                callback.error("error: " + ex);
            }
        } else {
            callback.error("Please do not pass null value");
        }
    }

    private void radioInterpretHead(JSonArray args, CallbackContext callback) {
        if (args != null) {
            try {
                String frame = args.getJSONObject(0).getString("param");
                String result = RadioInterpret.INSTANCE.interpretHead(frame);

                callback.success("" + (result));

            } catch (Exception ex) {
                callback.error("error: " + ex);
            }
        } else {
            callback.error("Please do not pass null value");
        }
    }
}
