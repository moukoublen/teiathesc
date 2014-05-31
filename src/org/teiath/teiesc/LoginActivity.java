package org.teiath.teiesc;


import java.io.IOException;

import org.json.JSONException;
import org.teiath.teiesc.options.SharedSettingsTransaction;
import org.teiath.teiesc.ws.WsTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity
{
    private UserLoginTask mAsyncLoginTask = null;
    
    private String mUserName = "";
    private String mPassword = "";
    
    private EditText mUserNameView;
    private EditText mPasswordView;
    private View     mLoginFormView;
    private View     mLoginStatusView;
    private TextView mLoginStatusMessageView;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setListeners();

        String tmp = getIntent().getStringExtra(MainActivity.USERNAME);
        mUserName = null != tmp ? tmp : "";

        if (SharedSettingsTransaction.hasUsernameAndPassword(this)
                || !ConnectionChecker.isConnected(this))
        {
            goToMain(false);
        }
        else
        {
            mUserNameView.setText(mUserName);
            mPasswordView.setText(mPassword);
            if (getIntent().hasExtra(MainActivity.USERNAME)
                    && !mUserName.equals(""))
            {
                mPasswordView
                        .setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
            else
            {
                mUserNameView.requestFocus();
            }

        }
    }

    private void storeSettings()
    {
        SharedSettingsTransaction.setUsernameAndPassword(this, mUserName, mPassword);
    }
    
    private void findViews()
    {
        mUserNameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
    }

    private void setListeners()
    {
        mPasswordView
                .setOnEditorActionListener(new TextView.OnEditorActionListener()
                {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                            KeyEvent keyEvent)
                    {
                        if (id == R.id.login || id == EditorInfo.IME_NULL)
                        {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });

        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        attemptLogin();
                    }
                });
    }
    private void goToMain(boolean justLogedin)
    {
        Intent goToMain = new Intent(this, MainActivity.class);
        goToMain.putExtra(MainActivity.SETTING_JUST_LOGED_IN, justLogedin);
        startActivity(goToMain);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    public View getErrorValidate()
    {
        View focusView = null;
        
        // Έλεγχος για έγκυρο password.
        if (TextUtils.isEmpty(mPassword))
        {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
        }
        else if (mPassword.length() < 4)
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
        }

        // Έλεγχος για έγκυρο username
        if (TextUtils.isEmpty(mUserName))
        {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
        }
        if (mUserName.length() < 4)
        {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
        }
        
        return focusView;
    }
    public void attemptLogin()
    {
        if (mAsyncLoginTask != null)
        {
            return;
        }
        
        if (!ConnectionChecker.isConnected(this))
        {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
        
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        
        mUserName = mUserNameView.getText().toString();
        mPassword = mPasswordView.getText().toString();
        
        View focusView = getErrorValidate();
        
        if (focusView != null)
        {
            focusView.requestFocus();
        }
        else
        {
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mAsyncLoginTask = new UserLoginTask();
            
            LoginData data = new LoginData();
            data.userName  = mUserName;
            data.password  = mPassword;
            data.url       = getString(R.string.ws_login);
            data.errorMsg  = WsTransaction.ERROR_MSG;
            
            mAsyncLoginTask.execute(data);
        }
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            mLoginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        }
        else
        {
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    
    public class LoginData
    {
        public String userName;
        public String password;
        public String url;
        public String errorMsg;
    }
    
    private class ResponseStatus
    {
        public String ticket       = null;
        public Exception exception = null;
        
        public boolean hasErrors()
        {
            return null != exception;
        }
    }
    
    public class UserLoginTask extends AsyncTask <LoginData, Void, ResponseStatus>
    {
        @Override
        protected ResponseStatus doInBackground(LoginData... params)
        {
            ResponseStatus result = new ResponseStatus();
            try
            {
                LoginData data = params[0];
                result.ticket = data.errorMsg;
                result.ticket = WsTransaction.login(data.url, data.userName, data.password);
            }
            catch (IOException e)
            { 
                result.exception = e;
            }
            catch (JSONException e)
            { 
                result.exception = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(final ResponseStatus response)
        {
            mAsyncLoginTask = null;
            showProgress(false);
            
            if(response.hasErrors())
            {
                mPasswordView
                    .setError(getString(R.string.error_connection));
                mPasswordView.requestFocus();
                return ;
            }

            if (WsTransaction.isTicketCorrect(response.ticket))
            {
                SharedSettingsTransaction.TICKET = response.ticket;
                storeSettings();
                goToMain(true);
            }
            else
            {
                mPasswordView
                        .setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled()
        {
            mAsyncLoginTask = null;
            showProgress(false);
        }
    }
}
