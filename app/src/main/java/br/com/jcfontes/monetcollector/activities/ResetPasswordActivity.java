package br.com.jcfontes.monetcollector.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.jcfontes.monetcollector.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset, tvBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_reset_password_activity);

        inputEmail = findViewById(R.id.etEmail);
        btnReset = findViewById(R.id.btResetPass);
        tvBack = findViewById(R.id.tvBack);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        tvBack.setOnClickListener(v -> finish());
        btnReset.setOnClickListener(v -> {

            String email = inputEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(),  getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, getString(R.string.msg_reset_password), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, getString(R.string.error_reset_password), Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    });
        });
    }

}
