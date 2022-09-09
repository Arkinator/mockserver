package org.mockserver.serialization.serializers.body;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.mockserver.serialization.model.StringBodyDTO;

import java.io.IOException;

import static org.mockserver.model.StringBody.DEFAULT_CONTENT_TYPE;

/**
 * @author jamesdbloom
 */
public class StringBodyDTOSerializer extends StdSerializer<StringBodyDTO> {

    private final boolean serialiseDefaultValues;

    public StringBodyDTOSerializer(boolean serialiseDefaultValues) {
        super(StringBodyDTO.class);
        this.serialiseDefaultValues = serialiseDefaultValues;
    }

    @Override
    public void serialize(StringBodyDTO stringBodyDTO, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        boolean notFieldSetAndNotDefault = stringBodyDTO.getNot() != null && stringBodyDTO.getNot();
        boolean optionalFieldSetAndNotDefault = stringBodyDTO.getOptional() != null && stringBodyDTO.getOptional();
        boolean subStringFieldNotDefault = stringBodyDTO.isSubString();
        boolean contentTypeFieldSetAndNotDefault = stringBodyDTO.getContentType() != null && !stringBodyDTO.getContentType().equals(DEFAULT_CONTENT_TYPE.toString());
        if (serialiseDefaultValues || notFieldSetAndNotDefault || optionalFieldSetAndNotDefault || contentTypeFieldSetAndNotDefault || subStringFieldNotDefault) {
            jgen.writeStartObject();
            if (notFieldSetAndNotDefault) {
                jgen.writeBooleanField("not", true);
            }
            if (optionalFieldSetAndNotDefault) {
                jgen.writeBooleanField("optional", true);
            }
            jgen.writeStringField("type", stringBodyDTO.getType().name());
            jgen.writeStringField("string", stringBodyDTO.getString());
            if (stringBodyDTO.getRawBytes() != null) {
                jgen.writeObjectField("rawBytes", stringBodyDTO.getRawBytes());
            }
            if (subStringFieldNotDefault) {
                jgen.writeBooleanField("subString", true);
            }
            if (contentTypeFieldSetAndNotDefault) {
                jgen.writeStringField("contentType", stringBodyDTO.getContentType());
            }
            jgen.writeEndObject();
        } else {
            jgen.writeString(stringBodyDTO.getString());
        }
    }
}
