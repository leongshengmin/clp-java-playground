package clp.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.yscope.clp.compressorfrontend.BuiltInVariableHandlingRuleVersions;
import com.yscope.clp.compressorfrontend.ByteSegment;
import com.yscope.clp.compressorfrontend.ByteSegments;
import com.yscope.clp.compressorfrontend.EightByteClpEncodedSubquery;
import com.yscope.clp.compressorfrontend.EightByteClpWildcardQueryEncoder;
import com.yscope.clp.compressorfrontend.EncodedMessage;
import com.yscope.clp.compressorfrontend.MessageEncoder;


class CLPEncoder {
    private static final Logger logger = Logger.getLogger(CLPEncoder.class.getName());

    /**
     * Encodes a wildcard query {@code wildcardQuery} into its CLP encoded equivalent {@code CLPEncodedMessage} to simulate a search query.
     * 
     * @param wildcardQuery the wildcard query to be encoded
     * @return a list of {@link CLPEncodedMessage} objects representing the encoded subqueries
     */
    public List<CLPEncodedMessage> encodeWildcardQuery(String wildcardQuery) {
        /**
         * Encodes a wildcard query {@code wildcardQuery} into its CLP encoded equivalent {@code CLPEncodedMessage} to simulate a search query.
         */
        EightByteClpWildcardQueryEncoder queryEncoder = new EightByteClpWildcardQueryEncoder(BuiltInVariableHandlingRuleVersions.VariablesSchemaV2, BuiltInVariableHandlingRuleVersions.VariableEncodingMethodsV1);
        
        // generate list of encoded subqueries from the wildcard query
        EightByteClpEncodedSubquery[] subqueries = queryEncoder.encode(wildcardQuery);
        
        List<CLPEncodedMessage> clpEncodedSubqueries;
        clpEncodedSubqueries = Arrays.stream(subqueries).map((EightByteClpEncodedSubquery subquery) -> {
            String logtype = subquery.getLogtypeQueryAsString();
            long[] encodedVars = subquery.getEncodedVars();
            ByteSegments dictVars = subquery.getDictVars();
            List<String> dictVarsAsStrings = new ArrayList<>();
            for (ByteSegment byteSegment : dictVars) {
                String byteSegmentAsString = byteSegment.toString();
                dictVarsAsStrings.add(byteSegmentAsString);
                logger.log(Level.FINE, "encodeWildcardQuery | byteSegmentAsString={0}", new Object[]{byteSegmentAsString});
            }
            CLPEncodedMessage clpEncodedSubquery = new CLPEncodedMessage(logtype, encodedVars, dictVarsAsStrings.toArray(new String[dictVarsAsStrings.size()]));
            return clpEncodedSubquery;
        }).toList();

        logger.log(Level.FINE, "encodeWildcardQuery | wildcardQuery={0},clpEncodedSubqueries={1}", new Object[]{wildcardQuery, clpEncodedSubqueries});
        return clpEncodedSubqueries;
    }

    /**
     * Encodes unstructured text {@code valueToEncode} into its CLP encoded equivalent {@code CLPEncodedMessage} to simulate storing a message.
     *
     * @param valueToEncode the unstructured text to be encoded
     * @return an {@link Optional} containing the {@link CLPEncodedMessage} if the encoding was successful, or an empty {@link Optional} if an exception occurred during encoding
     */
    public Optional<CLPEncodedMessage> encode(String valueToEncode) {
        /**
         * Encodes unstructured text {@code valueToEncode} into its CLP encoded equivalent {@code CLPEncodedMessage} to simulate storing a message.
         */
        MessageEncoder _clpMessageEncoder = new MessageEncoder(BuiltInVariableHandlingRuleVersions.VariablesSchemaV2, BuiltInVariableHandlingRuleVersions.VariableEncodingMethodsV1);
        EncodedMessage _clpEncodedMessage = new EncodedMessage();

        try {
            logger.log(Level.FINE, "encode | valueToEncode={0}, _clpEncodedMessage={1}", new Object[]{valueToEncode, _clpEncodedMessage});
            _clpMessageEncoder.encodeMessage(valueToEncode, _clpEncodedMessage);

            String logtype = _clpEncodedMessage.getLogTypeAsString();
            long[] encodedVars = _clpEncodedMessage.getEncodedVars();
            String[] dictVars = _clpEncodedMessage.getDictionaryVarsAsStrings();

            return Optional.of(new CLPEncodedMessage(logtype, encodedVars, dictVars));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "encode | Failed to encode valueToEncode={0}, e={1}", new Object[]{valueToEncode, e.getMessage()});
        }
        return Optional.empty();     
    }
}
