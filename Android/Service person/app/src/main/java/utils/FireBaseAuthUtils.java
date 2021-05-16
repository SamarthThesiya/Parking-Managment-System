package utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

public class FireBaseAuthUtils {

    private FirebaseAuth mAuth;
    private Context      context;

    public FireBaseAuthUtils(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();
        this.context = context;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void sendOtp(String phone, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) context)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential, OnCompleteListener<AuthResult> mCallback) {
        mAuth.signInWithCredential(credential).addOnCompleteListener((Activity) context, mCallback);
    }

    public PhoneAuthCredential generateCredential(String verificationId, String code) {
        return PhoneAuthProvider.getCredential(verificationId, code);
    }

    public void signOut() {
        mAuth.signOut();
    }

    public void updateProfile(UserProfileChangeRequest userProfileChangeRequest, OnCompleteListener onCompleteListener) {

        FirebaseUser user = getCurrentUser();
        user.updateProfile(userProfileChangeRequest).addOnCompleteListener(onCompleteListener);
    }
}
