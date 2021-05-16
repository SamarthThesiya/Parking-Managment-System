package custom_view;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import interfaces.PmValidatable;
import models.Validations;
import utils.Validator;

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
    public Validations getValidations() {
        return validators;
    }

}
