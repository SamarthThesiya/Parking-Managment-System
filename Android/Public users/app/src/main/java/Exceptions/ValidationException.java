package Exceptions;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class ValidationException extends Exception{

    String message;
    public ValidationException() {
        this.message = "invalid Input";
    }
    public ValidationException(String message) {
        this.message = message;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }
}
