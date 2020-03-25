package helpers.hac;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;


public class HybrisHttpResult {

    private boolean hasError;
    private String errorMessage;
    private String detailMessage;
    private List<List<String>> resultList;
    private String output;
    private String result;
    private int statusCode;


    private HybrisHttpResult() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutput() {
        return output;
    }

    public String getResult() {
        return result;
    }

    public List<List<String>> getResultList() {
        return resultList;
    }

    static public class HybrisHttpResultBuilder {
        private List<List<String>> resultList;
        private boolean hasError = false;
        private String errorMessage = EMPTY;
        private String detailMessage = EMPTY;

        private String output = EMPTY;
        private String result = EMPTY;
        private int statusCode = SC_OK;

        private HybrisHttpResultBuilder() {
        }

        public static HybrisHttpResultBuilder createResult() {
            return new HybrisHttpResultBuilder();
        }

        public HybrisHttpResultBuilder errorMessage(final String errorMessage) {
            if (isNotEmpty(errorMessage)) {
                this.errorMessage = errorMessage;
                this.hasError = true;
            }
            return this;
        }

        public HybrisHttpResultBuilder detailMessage(final String detailMessage) {
            if (isNotEmpty(detailMessage)) {
                this.detailMessage = detailMessage;
                this.hasError = true;
            }
            return this;
        }

        public HybrisHttpResultBuilder output(final String output) {
            this.output = output;
            return this;
        }

        public HybrisHttpResultBuilder result(final String result) {
            this.result = result;
            return this;
        }

        public HybrisHttpResultBuilder httpCode(final int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public HybrisHttpResultBuilder resultList(List<List<String>> resultList) {
            this.resultList = resultList;
            return this;
        }

        public HybrisHttpResult build() {
            final HybrisHttpResult httpResult = new HybrisHttpResult();
            httpResult.hasError = this.hasError;
            httpResult.errorMessage = this.errorMessage;
            httpResult.detailMessage = this.detailMessage;
            httpResult.output = this.output;
            httpResult.result = this.result;
            httpResult.statusCode = this.statusCode;
            httpResult.resultList = this.resultList;
            return httpResult;
        }

    }
}
