package br.com.jcfontes.monetcollector.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.configs.Base64Custom;
import br.com.jcfontes.monetcollector.models.User;
import br.com.jcfontes.monetcollector.utils.FirebaseAuthUtils;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btSignUp;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_activity);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPass);
        btSignUp = findViewById(R.id.btSignUp);

        progressBar = findViewById(R.id.progressBar);
        btSignUp.setOnClickListener(v -> signUp());

        TextView tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(v -> finish());
    }

    public void signUp() {
        if (!validate()) {
            Toast.makeText(getBaseContext(), getString(R.string.error_auth_failed), Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User();
        user.setName(etName.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.setId(Base64Custom.codeBase64(user.getEmail()));

        btSignUp.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseAuthUtils.getInstance().createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, task -> {

                    progressBar.setVisibility(View.GONE);
                    btSignUp.setEnabled(true);

                    if (!task.isSuccessful()) {
//                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getBaseContext(), getString(R.string.error_auth_failed), Toast.LENGTH_LONG).show();

                    } else {
//                        Log.d(TAG, "createUserWithEmail:success");
                        user.save();
                        Intent intent = new Intent();
                        intent.putExtra("USER_EMAIL", user.getEmail());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public boolean validate() {
        boolean valid = true;

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            etName.setError(getString(R.string.error_at_least_3));
            valid = false;
        } else {
            etName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError(getString(R.string.error_between_4_and_10));
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }
}
