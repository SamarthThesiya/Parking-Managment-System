package custom_view;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import interfaces.PmValidatable;
import models.Validations;
import utils.Validator;

public class PmImageView extends AppCompatImageView implements PmValidatable {

    String   text;
    TextView tv_viewMsg;
    Validations validators = null;

    public PmImageView(@NonNull Context context) {
        super(context);
        validators = new Validations();
    }

    public PmImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        validators = new Validations();
    }

    public PmImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        validators = new Validations();
    }

    public void isRequired(boolean b) {

        if (b) {
            validators.add(Validator.REQUIRED, null, null);
        } else {
            validators.remove(Validator.REQUIRED);
        }
    }

    public void setMsgTextView(TextView tv_viewMsg) {
        this.tv_viewMsg = tv_viewMsg;
    }

    @Override
    public void handleValidationError(Validations.Validation errorValidation) {
        tv_viewMsg.setError(errorValidation.getMessage());
    }

    @Override
    public Editable getText() {
        return new SpannableStringBuilder(text);
    }

    @Override
    public Validations getValidations() {
        return validators;
    }

    public void setText(String text) {
        this.text = text;
    }
}
