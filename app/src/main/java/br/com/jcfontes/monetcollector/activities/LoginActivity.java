package br.com.jcfontes.monetcollector.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.configs.AppConfig;
import br.com.jcfontes.monetcollector.models.User;
import br.com.jcfontes.monetcollector.utils.FirebaseAuthUtils;
import br.com.jcfontes.monetcollector.utils.FirebaseDatabaseUtils;
import br.com.jcfontes.monetcollector.utils.Internet;
import br.com.jcfontes.monetcollector.utils.Notify;

public class LoginActivity extends AppCompatActivity {

    private EditText etLogin, etPassword;
    private Button btSignIn;
    private TextView tvSignUp, tvResetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPass);
        btSignIn = findViewById(R.id.btSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvResetPass = findViewById(R.id.tvResetPass);

        btSignIn.setOnClickListener(v -> {

            String email = etLogin.getText().toString();
            String password = etPassword.getText().toString();

            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etLogin.setError(getString(R.string.error_invalid_email));
                etLogin.requestFocus();

            } else if (TextUtils.isEmpty(password)) {
                etPassword.setError(getString(R.string.error_invalid_password));
                etPassword.requestFocus();

            } else if (!Internet.isNetworkAvailable(this)) {
                Notify.showNotify(this, getString(R.string.error_not_connected));

            } else {
                User user = new User(email, password);
                Intent intent = new Intent(LoginActivity.this, ValidateLoginActivity.class);
                intent.putExtra("USER", user);
                startActivityForResult(intent, AppConfig.KEY_REQUEST_CODE_VALID_LOGIN);
            }
        });

        tvSignUp.setOnClickListener(v -> {
            if (!Internet.isNetworkAvailable(this)) {
                Notify.showNotify(this, getString(R.string.error_not_connected));
            } else {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, AppConfig.KEY_REQUEST_CODE_VALID_SIGNUP);
            }
        });

        tvResetPass.setOnClickListener(v -> {
            if (!Internet.isNetworkAvailable(this)) {
                Notify.showNotify(this, getString(R.string.error_not_connected));
            } else {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppConfig.KEY_REQUEST_CODE_VALID_LOGIN:

                switch (resultCode) {
                    case RESULT_OK:
                        boolean isValid = data.getBooleanExtra(AppConfig.KEY_RESULT_LOGIN, false);
                        if (isValid) {
                            String userCode = FirebaseAuthUtils.getUUID();

                            if (userCode != null) {
                                DatabaseReference databaseReference = FirebaseDatabaseUtils.getInstance()
                                        .getReference("user/" + userCode);

                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            User loggedUser = dataSnapshot.getValue(User.class);

                                            if (loggedUser != null) {
                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                intent.putExtra("USER", loggedUser);
                                                startActivity(intent);
                                            }
                                        } catch (Exception ex) {
                                            System.out.println(ex.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("Error: " + databaseError.getDetails());
                                    }
                                });
                            }
                        }
                        break;
                }
                break;

            case AppConfig.KEY_REQUEST_CODE_VALID_SIGNUP:
                switch (resultCode) {
                    case RESULT_OK:
                        etLogin = findViewById(R.id.etLogin);
                        Bundle extras = data.getExtras();
                        etLogin.setText(extras.getString("USER_EMAIL"));
                        FirebaseAuthUtils.getInstance().signOut();
                        break;
                }
                break;
        }
    }
}