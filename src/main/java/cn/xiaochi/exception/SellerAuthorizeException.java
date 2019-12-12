package cn.xiaochi.exception;

public class SellerAuthorizeException extends RuntimeException {
    public SellerAuthorizeException(String message) {
        super(message);
    }

    public SellerAuthorizeException() {
        super();
    }
}
