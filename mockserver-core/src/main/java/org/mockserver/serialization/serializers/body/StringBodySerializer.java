package org.mockserver.serialization.serializers.body;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.mockserver.model.StringBody;

import java.io.IOException;

import static org.mockserver.model.StringBody.DEFAULT_CONTENT_TYPE;

/**
 * @author jamesdbloom
 */
public class StringBodySerializer extends StdSerializer<StringBody> {

    private final boolean serialiseDefaultValues;

    public StringBodySerializer(boolean serialiseDefaultValues) {
        super(StringBody.class);
        this.serialiseDefaultValues = serialiseDefaultValues;
    }

    @Override
    public void serialize(StringBody stringBody, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        boolean notFieldSetAndNotDefault = stringBody.getNot() != null && stringBody.getNot();
        boolean optionalFieldSetAndNotDefault = stringBody.getOptional() != null && stringBody.getOptional();
        boolean subStringFieldNotDefault = stringBody.isSubString();
        boolean contentTypeFieldSetAndNotDefault = stringBody.getContentType() != null && !stringBody.getContentType().equals(DEFAULT_CONTENT_TYPE.toString());
        if (serialiseDefaultValues || notFieldSetAndNotDefault || optionalFieldSetAndNotDefault || contentTypeFieldSetAndNotDefault || subStringFieldNotDefault) {
            jgen.writeStartObject();
            if (notFieldSetAndNotDefault) {
                jgen.writeBooleanField("not", true);
            }
            if (optionalFieldSetAndNotDefault) {
                jgen.writeBooleanField("optional", true);
            }
            jgen.writeStringField("type", stringBody.getType().name());
            jgen.writeStringField("string", stringBody.getValue());
            if (subStringFieldNotDefault) {
                jgen.writeBooleanField("subString", true);
            }
            if (contentTypeFieldSetAndNotDefault) {
                jgen.writeStringField("contentType", stringBody.getContentType());
            }
            jgen.writeEndObject();
        } else {
            jgen.writeString(stringBody.getValue());
        }
    }
}
