package custom_view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import interfaces.PmValidatable;
import models.Validations;
import utils.Validator;

public class PmButton extends AppCompatButton implements View.OnClickListener {

    ArrayList<PmValidatable> dependentValidatable;
    private OnClickListener onClickListener;

    public PmButton(Context context) {
        super(context);
        init();
    }

    public PmButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PmButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dependentValidatable = new ArrayList<>();
        super.setOnClickListener(this);
    }

    public void setDependent(PmValidatable... dependentEditTexts) {
        this.dependentValidatable = new ArrayList<>(Arrays.asList(dependentEditTexts));
    }

    public void removeAllDependent() {
        this.dependentValidatable = new ArrayList<>();
    }

    private boolean validateDependent() {

        for (PmValidatable validatable : dependentValidatable) {
            Validator              validator       = new Validator(validatable);
            Validations.Validation errorValidation = validator.validate(validatable.getValidations());

            if (errorValidation != null) {
                validatable.handleValidationError(errorValidation);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        boolean is_validate = validateDependent();

        if (is_validate && null!=onClickListener) {
            Objects.requireNonNull(onClickListener).onClick(this);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
