package models.responseModels;

import org.json.JSONObject;

import models.BaseModel;

public class ErrorResponse implements BaseModel {

    String  message;
    String  code;
    Integer status_code;
    String  action;
    JSONObject fieldValidation;

    public String getAction() {
        return action;
    }

    public void setAction(String methodName) {
        this.action = methodName;
    }

    public void setFieldValidation(JSONObject fieldValidation) {
        this.fieldValidation = fieldValidation;
    }

    public JSONObject getFieldValidation() {
        return fieldValidation;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }


    public String getError_message() {
        return message;
    }

    public String getError_code() {
        return code;
    }

    public Integer getStatus_code() {
        return status_code;
    }


}
