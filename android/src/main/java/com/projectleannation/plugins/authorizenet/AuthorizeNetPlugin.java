package com.projectleannation.plugins.authorizenet;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import com.projectleannation.plugins.authorizenet.AuthorizeNetPluginHelper;

import net.authorize.acceptsdk.AcceptSDKApiClient;
import net.authorize.acceptsdk.datamodel.merchant.ClientKeyBasedMerchantAuthentication;
import net.authorize.acceptsdk.datamodel.transaction.CardData;
import net.authorize.acceptsdk.datamodel.transaction.EncryptTransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionType;

import org.json.JSONObject;

@CapacitorPlugin(name = "AuthorizeNet")
public class AuthorizeNetPlugin extends Plugin {

    @PluginMethod()
    public void getCardToken(PluginCall call) {
        String cardNumber = call.getString("number", "");
        String month = call.getString("month", "");
        String year = call.getString("year", "");
        String cardCode = call.getString("code", "");
        String fullName = call.getString("name", "");
        String clientKey = call.getString("clientKey", "");
        String apiLoginID = call.getString("loginID", "");

        /**
         * Handle missing card information
         */
        if (cardNumber == "" || month == "" || year == "" || cardCode == "" || fullName == "" || clientKey == "" || apiLoginID == "") {
            try {
                JSONObject object = new JSONObject("{\"messages\":[{\"resultCode\":\"Error\",\"message\":{\"code\":\"\",\"text\":\"Missing one or more required fields.\"}}]}");
                JSObject resp = JSObject.fromJSONObject(object);

                call.resolve(resp);

                return;
            } catch (Exception e) {
                JSObject err = new JSObject();

                err.put("error", true);
                call.resolve(err);

                return;
            }
        }

        // Initialize client
        AcceptSDKApiClient apiClient = new AcceptSDKApiClient.Builder(getActivity(), AcceptSDKApiClient.Environment.PRODUCTION).connectionTimeout(5000).build();

        // Set card data
        CardData cardData = new CardData.Builder(cardNumber,
                month, // MM
                year) // YYYY
                .cvvCode(cardCode) // Optional
                .cardHolderName(fullName)// Optional
                .build();

        // set merchant authentication
        ClientKeyBasedMerchantAuthentication merchantAuthentication = ClientKeyBasedMerchantAuthentication.
                createMerchantAuthentication(apiLoginID, clientKey);

        // Transaction request object
        EncryptTransactionObject transactionObject = TransactionObject.
                createTransactionObject(TransactionType.SDK_TRANSACTION_ENCRYPTION)// type of transaction object
                .cardData(cardData) // card data to be encrypted
                .merchantAuthentication(merchantAuthentication) //Merchant authentication
                .build();

        AuthorizeNetPluginHelper responseHelper = new AuthorizeNetPluginHelper(call);

        apiClient.getTokenWithRequest(transactionObject, responseHelper);
    }
}