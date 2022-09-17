import Foundation
import Capacitor
import AuthorizeNetAccept

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(AuthorizeNetPlugin)
public class AuthorizeNetPlugin: CAPPlugin {
    private let implementation = AuthorizeNet()

    @objc func getCardToken(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        
        let cardNumber = call.getString("number") ?? ""
        let month = call.getString("month") ?? ""
        let year = call.getString("year") ?? ""
        let cardCode = call.getString("code") ?? ""
        let fullName = call.getString("name") ?? ""
        let clientKey = call.getString("clientKey") ?? ""
        let apiLoginID = call.getString("loginID") ?? ""
        
        
        /* Return if missing required fields */
        if (cardNumber == "" || month == "" || year == "" || cardCode == "" || fullName == "" || clientKey == "" || apiLoginID == "") {
            return call.resolve([
                
                "messages": [
                    "resultCode": "Error",
                    "message": Array([
                        "code": "",
                        "text": "Missing one or more required fields."
                    ])
                ]
            ])
        }
        
        let handler = AcceptSDKHandler(environment: AcceptSDKEnvironment.ENV_LIVE)
                
                let request = AcceptSDKRequest()
                request.merchantAuthentication.name = apiLoginID
                request.merchantAuthentication.clientKey = clientKey
                
                request.securePaymentContainerRequest.webCheckOutDataType.token.cardNumber = cardNumber
        request.securePaymentContainerRequest.webCheckOutDataType.token.fullName = fullName
                request.securePaymentContainerRequest.webCheckOutDataType.token.expirationMonth = month
                request.securePaymentContainerRequest.webCheckOutDataType.token.expirationYear = year
                request.securePaymentContainerRequest.webCheckOutDataType.token.cardCode = cardCode
        
        handler!.getTokenWithRequest(request, successHandler: { (inResponse:AcceptSDKTokenResponse) -> () in
            var messageArray = Array<Any>();
            
            messageArray.append([
                "code": inResponse.getMessages().getMessages()[0].getCode(),
                "text": inResponse.getMessages().getMessages()[0].getText()
            ])
            
                let response = [
                    "opaqueData": [
                     "dataDescriptor": inResponse.getOpaqueData().getDataDescriptor(),
                     "dataValue": inResponse.getOpaqueData().getDataValue()
                    ],
                    "messages": [
                        "resultCode": inResponse.getMessages().getResultCode(),
                        "message": messageArray
                    ]
                ];
            
            print(response);
            
            return call.resolve(response)
               }) { (inError:AcceptSDKErrorResponse) -> () in
                   var messageArray = Array<Any>();
                   
                   messageArray.append([
                    "code": inError.getMessages().getMessages()[0].getCode(),
                    "text": inError.getMessages().getMessages()[0].getText()
                   ]);
                   
                   let response = [
                    
                    "messages": [
                        "resultCode": inError.getMessages().getResultCode(),
                        "message": messageArray
                    ]
                   ];
                   
                   print(response);
                   
                   return call.resolve(response)
               }
        
        
    }
}
