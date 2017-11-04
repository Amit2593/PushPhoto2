package com.example.lattoo.pushphoto;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import java.util.jar.Attributes;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private EditText NameField;
    private EditText PasswordField;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        NameField = (EditText) findViewById(R.id.editName);
        PasswordField = (EditText) findViewById(R.id.editPassword);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Signing In");
    }


    public void signIn(View view) {

        String email = NameField.getText().toString();
        String password = PasswordField.getText().toString();

        Log.d(TAG, "signIn:" + email);

        if (email.length() == 0) {
            Toast.makeText(SignInActivity.this, "Please Enter UserName....", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() == 0) {
            Toast.makeText(SignInActivity.this, "Please Enter Password....", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgress.show();


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            mProgress.dismiss();
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(SignInActivity.this, "Authentication Success!",
                                    Toast.LENGTH_SHORT).show();
                            Intent _result = new Intent();
                            setResult(RESULT_OK, _result);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            mProgress.dismiss();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(SignInActivity.this)
                .setMessage("Do you want to Exit App?")
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                })
                .show();
    }


}
