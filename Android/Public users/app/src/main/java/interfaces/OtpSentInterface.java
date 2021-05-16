package interfaces;

import com.google.firebase.FirebaseException;

public interface OtpSentInterface {
    public void otpSentSuccess(String token);
    public void otpSentFailure(FirebaseException e);
}
