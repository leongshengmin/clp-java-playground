package clp.java;

import java.util.Arrays;


public class CLPEncodedMessage {
    String logtype;
    long[] encodedVars;
    String[] dictVars;

    @Override
    public String toString() {
        return "CLPEncodedMessage [logtype=" + logtype + ", encodedVars=" + Arrays.toString(encodedVars) + ", dictVars=" + Arrays.toString(dictVars) + "]";
    }

    public CLPEncodedMessage(String logtype, long[] encodedVars, String[] dictVars) {
        this.logtype = logtype;
        this.encodedVars = encodedVars;
        this.dictVars = dictVars;
    }
}
