package com.appmyday.testoauth;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson.JacksonFactory;
import com.wuman.android.auth.AuthorizationFlow;
import com.wuman.android.auth.AuthorizationUIController;
import com.wuman.android.auth.DialogFragmentController;
import com.wuman.android.auth.OAuthManager;
import com.wuman.android.auth.oauth2.store.SharedPreferencesCredentialStore;

import java.io.IOException;


public class MyActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        AuthorizationFlow flow = getAuthorizationFlow(getApplicationContext());


        AuthorizationUIController controller = getAuthorizationUIController();

        OAuthManager oauth = new OAuthManager(flow, controller);
        oauth.authorizeImplicitly("usertest", new OAuthManager.OAuthCallback<Credential>() {
            @Override
            public void run(OAuthManager.OAuthFuture<Credential> future) {

                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
            }
        }, h);
    }

    private AuthorizationUIController getAuthorizationUIController() {
        return new DialogFragmentController(getFragmentManager()) {

            @Override
            public String getRedirectUri() throws IOException {
                return "http://localhost/Callback";
            }

            @Override
            public boolean isJavascriptEnabledForWebView() {
                return true;
            }

        };
    }

    private AuthorizationFlow getAuthorizationFlow(Context context) {
        SharedPreferencesCredentialStore credentialStore =
                new SharedPreferencesCredentialStore(context,
                        "preferenceFileName", new JacksonFactory());

        GenericUrl genericUrl = new GenericUrl(SERVER_PATH + "/oauth");
        ClientParametersAuthentication cpa = new ClientParametersAuthentication(CLIENT_ID, "");

        AuthorizationFlow.Builder builder = new AuthorizationFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                // new GenericUrl("https://socialservice.com/oauth2/access_token"),
                genericUrl,
                cpa,
                CLIENT_ID,
                SERVER_PATH + "/oauth");
//                "https://socialservice.com/oauth2/authorize");
        builder.setCredentialStore(credentialStore);
        return builder.build();
    }
    final static String SERVER_PATH = "http://192.168.56.1:8080";
    final static String CLIENT_ID = "testclient2";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
