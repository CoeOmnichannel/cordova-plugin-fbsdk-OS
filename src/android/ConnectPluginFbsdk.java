package org.apache.cordova.facebook;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

public class ConnectPluginFbsdk extends CordovaPlugin {

    private final String TAG = "EventsConnectPluginFbsdk";

    @Override
    protected void pluginInitialize() {

        FacebookSdk.setApplicationId(Objects.requireNonNull(getStringByIdName("events_fb_app_id")));
        FacebookSdk.setClientToken(getStringByIdName("events_fb_client_token"));
        FacebookSdk.setApplicationName(getStringByIdName("events_fb_app_name"));

        if(getStringByIdName("fb_app_id") == null || Objects.equals(getStringByIdName("fb_app_id"), "")) {
            FacebookSdk.sdkInitialize(cordova.getActivity().getApplicationContext());
        }

        // augment web view to enable hybrid app events
        FacebookSdk.setAutoLogAppEventsEnabled(getStringByIdName("events_fb_auto_log_app_events_enabled").equals("true"));
        FacebookSdk.setAdvertiserIDCollectionEnabled(getStringByIdName("events_fb_advertiser_id_collection_enabled").equals("true"));
        enableHybridAppEvents();
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        // Developers can observe how frequently users activate their app by logging an app activation event.
        AppEventsLogger.activateApp(cordova.getActivity().getApplication());
    }


    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        //force variables 
        FacebookSdk.setApplicationId(getStringByIdName("events_fb_app_id"));
        FacebookSdk.setClientToken(getStringByIdName("events_fb_client_token"));
        FacebookSdk.setApplicationName(getStringByIdName("events_fb_app_name"));

        switch (action) {
            case "getApplicationId":
                callbackContext.success(FacebookSdk.getApplicationId());
                return true;

            case "setApplicationId":
                FacebookSdk.setApplicationId(getStringByIdName("events_fb_app_id"));
                callbackContext.success();
                return true;

            case "getClientToken":
                callbackContext.success(FacebookSdk.getClientToken());
                return true;

            case "setClientToken":
                FacebookSdk.setClientToken(getStringByIdName("events_fb_client_token"));
                callbackContext.success();
                return true;

            case "getApplicationName":
                callbackContext.success(FacebookSdk.getApplicationName());
                return true;

            case "setApplicationName":
                FacebookSdk.setApplicationName(getStringByIdName("events_fb_app_name"));
                callbackContext.success();
                return true;


            case "getAccessToken":
                if (hasAccessToken()) {
                    callbackContext.success(Objects.requireNonNull(AccessToken.getCurrentAccessToken()).getToken());
                } else {
                    // Session not open
                    callbackContext.error("Session not open.");
                }
                return true;

            case "logEvent":
                executeLogEvent(args, callbackContext);
                return true;

            case "activateApp":
                cordova.getThreadPool().execute(() -> {
                    AppEventsLogger.activateApp(cordova.getActivity().getApplication());
                    callbackContext.success();
                });

                return true;
        }
        return false;
    }

    private void executeLogEvent(JSONArray args, CallbackContext callbackContext) throws JSONException {

        AppEventsLogger logger = AppEventsLogger.newLogger(cordova.getActivity().getApplicationContext());

        if (args.length() == 0) {
            // Not enough parameters
            callbackContext.error("Invalid arguments");
            return;
        }

        String eventName = args.getString(0);
        if (args.length() == 1) {
            logger.logEvent(eventName);
            callbackContext.success();
            return;
        }

        // Arguments is greater than 1
        JSONObject params = args.getJSONObject(1);
        Bundle parameters = new Bundle();
        Iterator<String> iter = params.keys();

        while (iter.hasNext()) {
            String key = iter.next();
            try {
                // Try get a String
                String value = params.getString(key);
                parameters.putString(key, value);
            } catch (JSONException e) {
                // Maybe it was an int
                Log.w(TAG, "Type in AppEvent parameters was not String for key: " + key);
                try {
                    int value = params.getInt(key);
                    parameters.putInt(key, value);
                } catch (JSONException e2) {
                    // Nope
                    Log.e(TAG, "Unsupported type in AppEvent parameters for key: " + key);
                }
            }
        }

        if (args.length() == 2) {
            logger.logEvent(eventName, parameters);
            callbackContext.success();
        }

        if (args.length() == 3) {
            double value = args.getDouble(2);
            logger.logEvent(eventName, value, parameters);
            callbackContext.success();
        }
    }

    private void enableHybridAppEvents() {
        try {
            Context appContext = cordova.getActivity().getApplicationContext();
            Resources res = appContext.getResources();
            int enableHybridAppEventsId = res.getIdentifier("events_fb_hybrid_app_events", "bool", appContext.getPackageName());
            boolean enableHybridAppEvents = enableHybridAppEventsId != 0 && res.getBoolean(enableHybridAppEventsId);
            if (enableHybridAppEvents) {
                AppEventsLogger.augmentWebView((WebView) this.webView.getView(), appContext);
                Log.d(TAG, "FB Hybrid app events are enabled");
            } else {
                Log.d(TAG, "FB Hybrid app events are not enabled");
            }
        } catch (Exception e) {
            Log.d(TAG, "FB Hybrid app events cannot be enabled");
        }
    }

    // Simple active session check
    private boolean hasAccessToken() {
        AccessToken token = AccessToken.getCurrentAccessToken();

        if (token == null)
            return false;

        return !token.isExpired();
    }


    private String getStringByIdName(String idName) {
        try{
            Resources res = cordova.getActivity().getApplicationContext().getResources();
            return res.getString(res.getIdentifier(idName, "string", cordova.getActivity().getApplicationContext().getPackageName()));
        } catch (Exception e) {
            Log.d(TAG, "Resource " + idName + "not found");
        }
        return null;
    }
}
