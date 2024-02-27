package ar.com.api.assetPlatform.exception;

public class ManageExceptionCoinGeckoServiceApi {

    public static void throwServiceException(Throwable throwable) {
        throw new
                ServiceException(
                throwable.getMessage(),
                throwable.fillInStackTrace()
        );
    }


}
