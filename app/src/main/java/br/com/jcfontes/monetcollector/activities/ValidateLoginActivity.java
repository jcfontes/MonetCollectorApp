package br.com.jcfontes.monetcollector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.configs.AppConfig;
import br.com.jcfontes.monetcollector.models.User;
import br.com.jcfontes.monetcollector.utils.FirebaseAuthUtils;
import br.com.jcfontes.monetcollector.utils.FirebaseDatabaseUtils;
import br.com.jcfontes.monetcollector.utils.Notify;

public class ValidateLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userCode = FirebaseAuthUtils.getUUID();

        if (userCode == null) {
            if (getIntent() != null) {
                User user = getIntent().getParcelableExtra("USER");

                if (user != null) {
                    setContentView(R.layout.login_validate_activity);
                    this.authenticate(user);

                } else {
                    startActivity(new Intent(ValidateLoginActivity.this, LoginActivity.class));
                    finish();
                }
            }

        } else {
            setContentView(R.layout.login_validate_activity);

            DatabaseReference databaseReference = FirebaseDatabaseUtils.getInstance()
                    .getReference("user/" + userCode);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        User loggedUser = dataSnapshot.getValue(User.class);

                        if (loggedUser == null) {
                            startActivity(new Intent(ValidateLoginActivity.this, LoginActivity.class));
                            finish();

                        } else {
                            Intent isLoginValid = new Intent(ValidateLoginActivity.this, HomeActivity.class);
                            isLoginValid.putExtra("USER", loggedUser);
                            startActivity(isLoginValid);
                            finish();
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

    private void authenticate(final User user) {

        Intent result = new Intent();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())

                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(ValidateLoginActivity.this, getString(R.string.error_auth_failed), Toast.LENGTH_LONG).show();
                        result.putExtra(AppConfig.KEY_RESULT_LOGIN, false);

                    } else {
                        result.putExtra(AppConfig.KEY_RESULT_LOGIN, true);
                    }
                    setResult(RESULT_OK, result);
                    finish();
                });
    }
}
