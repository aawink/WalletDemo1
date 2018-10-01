package com.etrade.eo.core.json.serializers;

import com.google.common.base.Strings;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BigDecimalDeserializer implements JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonElement jsonElement,
                                  Type type,
                                  JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        if (Strings.isNullOrEmpty(jsonElement.getAsString())) {
            return null;
        }
        return jsonElement.getAsBigDecimal();
    }
}