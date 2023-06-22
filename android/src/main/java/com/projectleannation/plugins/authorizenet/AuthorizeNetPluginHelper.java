package com.projectleannation.plugins.authorizenet;

import net.authorize.acceptsdk.datamodel.common.Message;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import net.authorize.acceptsdk.datamodel.common.ResponseMessages;
import net.authorize.acceptsdk.datamodel.transaction.callbacks.EncryptTransactionCallback;
import net.authorize.acceptsdk.datamodel.transaction.response.EncryptTransactionResponse;
import net.authorize.acceptsdk.datamodel.transaction.response.ErrorTransactionResponse;

import org.json.JSONObject;

public class AuthorizeNetPluginHelper implements EncryptTransactionCallback {
    private PluginCall call;

    AuthorizeNetPluginHelper(PluginCall call) {
        super();

        this.call = call;
    }

    @Override
    public void onEncryptionFinished(EncryptTransactionResponse response) {
        try {
            JSONObject object = new JSONObject("{\"opaqueData\":{\"dataDescriptor\":\""+response.getDataDescriptor()+"\",\"dataValue\":\""+response.getDataValue()+"\"},\"messages\":[{\"resultCode\":\""+response.getResultCode()+"\",\"message\":{\"code\":\""+response.getResultCode()+"\",\"text\":\"success\"}}]}");
            JSObject resp = JSObject.fromJSONObject(object);

            this.call.resolve(resp);
        } catch (Exception e) {
            JSObject err = new JSObject();

            err.put("error", true);
            this.call.resolve(err);
        }
    }

    @Override
    public void onErrorReceived(ErrorTransactionResponse errorResponse) {
        Message error = errorResponse.getFirstErrorMessage();

        try {
            JSONObject object = new JSONObject("{\"messages\":[{\"resultCode\":\"Error\",\"message\":{\"code\":\""+error.getMessageCode()+"\",\"text\":\""+error.getMessageText()+"\"}}]}");
            JSObject resp = JSObject.fromJSONObject(object);

            this.call.resolve(resp);
        } catch (Exception e) {
            JSObject err = new JSObject();

            err.put("error", true);
            this.call.resolve(err);
        }
    }
}

