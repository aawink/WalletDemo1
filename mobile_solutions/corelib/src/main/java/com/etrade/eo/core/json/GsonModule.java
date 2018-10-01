package com.etrade.eo.core.json;

import android.support.annotation.NonNull;

import com.etrade.eo.core.json.serializers.BigDecimalDeserializer;
import com.etrade.eo.core.json.serializers.GsonTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class GsonModule {

    @Provides
    @IntoSet
    static GsonTypeAdapter<?> provideBigDecimalGsonTypeAdapter() {
        return new GsonTypeAdapter<BigDecimal>() {
            @Override
            public Class<BigDecimal> getType() {
                return BigDecimal.class;
            }

            @Override
            public Object getTypeAdapter() {
                return new BigDecimalDeserializer();
            }
        };
    }


    @Provides
    static Gson provideGson(@NonNull Set<GsonTypeAdapter<?>> typeAdapters) {
        final GsonBuilder gsonBuilder = new GsonBuilder();

        for (final GsonTypeAdapter typeAdapter : typeAdapters) {
            gsonBuilder.registerTypeAdapter(typeAdapter.getType(), typeAdapter.getTypeAdapter());
        }

        gsonBuilder.serializeNulls();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        return gsonBuilder.create();
    }
}
