package services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import interfaces.OtpSentInterface;
import interfaces.OtpVerificationInterface;
import utils.FireBaseAuthUtils;

import static android.content.ContentValues.TAG;

public class AuthService {

    Context                  context;
    FireBaseAuthUtils        fireBaseAuthUtils;
    OtpSentInterface         otpSentInterface;
    OtpVerificationInterface otpVerificationInterface;

    OnCompleteListener<AuthResult> onOtpVerify = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                otpVerificationInterface.onOtpSuccess(user);
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                otpVerificationInterface.onOtpError(task.getException());

//                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                    task.getResult();
//                }
            }
        }
    };

    PhoneAuthProvider.OnVerificationStateChangedCallbacks sentOtpCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            fireBaseAuthUtils.signInWithPhoneAuthCredential(phoneAuthCredential, onOtpVerify);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            otpSentInterface.otpSentFailure(e);
        }

        @Override
        public void onCodeSent(String token, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(token, forceResendingToken);
            otpSentInterface.otpSentSuccess(token);
        }
    };

    public AuthService(Context context) {
        this.context      = context;
        fireBaseAuthUtils = new FireBaseAuthUtils(context);
    }

    public void sendOtp(String phone, OtpSentInterface otpSentInterface) {
        this.otpSentInterface = otpSentInterface;
        fireBaseAuthUtils.sendOtp(phone, sentOtpCallbacks);
    }

    public void verify(String otp, String token, OtpVerificationInterface otpVerificationInterface) {
        this.otpVerificationInterface = otpVerificationInterface;
        PhoneAuthCredential phoneAuthCredential = fireBaseAuthUtils.generateCredential(token, otp);
        fireBaseAuthUtils.signInWithPhoneAuthCredential(phoneAuthCredential, onOtpVerify);
    }

    public void signOut() {
        fireBaseAuthUtils.signOut();
    }

    public FirebaseUser getSignedInUser() {
        return fireBaseAuthUtils.getCurrentUser();
    }

    public void updateDisplayName(String display_name, OnCompleteListener onCompleteListener) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(display_name)
                .build();

        fireBaseAuthUtils.updateProfile(profileUpdates, onCompleteListener);

    }
}
