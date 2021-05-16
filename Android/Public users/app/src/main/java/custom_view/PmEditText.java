package custom_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import interfaces.PmValidatable;
import models.Validations;

public class PmEditText extends AppCompatEditText implements PmValidatable {

    Validations validators = null;

    public PmEditText(Context context) {
        super(context);
        validators = new Validations();
    }

    public PmEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        validators = new Validations();
    }

    public PmEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        validators = new Validations();
    }

    @Override
    public void handleValidationError(Validations.Validation errorValidation) {
        this.setError(errorValidation.getMessage());
    }

    @Override
    public void resetValidationError() {
        this.setError(null);
    }

    @Override
    public Validations getValidations() {
        return validators;
    }

}
