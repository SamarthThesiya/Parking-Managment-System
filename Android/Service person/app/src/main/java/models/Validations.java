package models;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Validations implements BaseModel {

    ArrayList<Validation> validations;

    public Validations() {
        validations = new ArrayList<>();
    }

    public void add(@NotNull String validation, @Nullable Object extra, @Nullable String message) {
        remove(validation); // Remove old validation if already exist
        Validation validation_obj = new Validation();
        validation_obj.setValidation(validation);
        validation_obj.setExtra(extra);
        validation_obj.setMessage(message);

        validations.add(validation_obj);
    }

    public void add(@NotNull String validation, @Nullable Object extra) {
        add(validation, extra, null);
    }

    public void add(@NotNull String validation) {
        add(validation, null, null);
    }

    public boolean remove(@NotNull String validation) {

        for (Validation validation_obj : validations) {

            if (validation_obj.getValidation().equalsIgnoreCase(validation)) {
                return validations.remove(validation_obj);
            }
        }

        return false;
    }

    public ArrayList<Validation> getValidations() {
        return validations;
    }

    public static class Validation {
        private String validation;
        private Object extra;
        private String message;

        public String getValidation() {
            return validation;
        }

        public void setValidation(String validation) {
            this.validation = validation;
        }

        public Object getExtra() {
            return extra;
        }

        public void setExtra(Object extra) {
            this.extra = extra;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
}
