package hu.remzso.tarantulaForum.converters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import hu.remzso.tarantulaForum.entities.User;

public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("firstName", user.getFirstName());
        jsonGenerator.writeStringField("lastName", user.getLastName());
        jsonGenerator.writeEndObject();
    }
}