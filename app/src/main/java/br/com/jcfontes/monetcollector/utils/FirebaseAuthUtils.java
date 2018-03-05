package br.com.jcfontes.monetcollector.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.jcfontes.monetcollector.configs.Base64Custom;

public class FirebaseAuthUtils {

    public static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static String getUUID() {
        FirebaseUser user = getCurrentFirebaseUser();

        return user != null ? Base64Custom.codeBase64(user.getEmail()) : null;
    }

    private static FirebaseUser getCurrentFirebaseUser() {
        return auth.getCurrentUser();
    }

    public static FirebaseAuth getInstance() {
        return auth;
    }

}
