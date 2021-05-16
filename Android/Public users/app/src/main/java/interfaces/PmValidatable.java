package interfaces;

import android.text.Editable;

import models.Validations;

public interface PmValidatable {


    Validations getValidations();

    void handleValidationError(Validations.Validation errorValidation);

    Editable getText();

    void resetValidationError();

}
