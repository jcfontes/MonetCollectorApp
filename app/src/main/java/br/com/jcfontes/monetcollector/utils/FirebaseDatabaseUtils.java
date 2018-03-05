package br.com.jcfontes.monetcollector.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseUtils {

    private static FirebaseDatabaseUtils instance;
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public static FirebaseDatabaseUtils getInstance() {
        return instance == null ? instance = new FirebaseDatabaseUtils() : instance;
    }

    public DatabaseReference getReference(String var) {
        return firebaseDatabase.getReference(var);
    }

    public static DatabaseReference getReference() {
        return firebaseDatabase.getReference();
    }

}
