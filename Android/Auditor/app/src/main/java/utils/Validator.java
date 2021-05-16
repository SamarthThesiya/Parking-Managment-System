package utils;


import android.text.TextUtils;

import java.util.Objects;

import interfaces.PmValidatable;
import models.Validations;

public class Validator {

    PmValidatable pmValidatable;

    public Validator(PmValidatable pmValidatable) {
        this.pmValidatable = pmValidatable;
    }

    public static final String REQUIRED   = "REQUIRED";
    public static final String MIN_LENGTH = "MIN_LENGTH";

    public Validations.Validation validate(Validations validations) {

        for (Validations.Validation validation : validations.getValidations()) {

            switch (validation.getValidation()) {

                case REQUIRED:
                    if (TextUtils.isEmpty(pmValidatable.getText())) {
                        if (TextUtils.isEmpty(validation.getMessage())) {
                            validation.setMessage("Required");
                        }
                        return validation;
                    }
                    break;

                case MIN_LENGTH:
                    int min = (int) validation.getExtra();
                    if (Objects.requireNonNull(pmValidatable.getText()).length() < min) {
                        if (TextUtils.isEmpty(validation.getMessage())) {
                            validation.setMessage("min " + min + " chars required");
                        }
                        return validation;
                    }
                    break;
            }
        }
        return null;
    }
}
