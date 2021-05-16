package interfaces;

import com.google.firebase.auth.FirebaseUser;

public interface OtpVerificationInterface {

    public void onOtpSuccess(FirebaseUser firebaseUser);
    public void onOtpError(Exception exception);
}
