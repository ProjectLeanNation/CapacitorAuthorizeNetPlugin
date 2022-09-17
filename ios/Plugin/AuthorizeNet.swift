import Foundation

@objc public class AuthorizeNet: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
