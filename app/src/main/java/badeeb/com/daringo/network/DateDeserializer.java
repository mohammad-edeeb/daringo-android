package badeeb.com.daringo.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import badeeb.com.daringo.utils.DateUtils;

/**
 * Created by meldeeb on 12/15/17.
 */

public class DateDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date> {

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String dateString = element.getAsString();
        SimpleDateFormat format = DateUtils.API_FORMAT;
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(DateUtils.API_FORMAT.format(src));
    }
}