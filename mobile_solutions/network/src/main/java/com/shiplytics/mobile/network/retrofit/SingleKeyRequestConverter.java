package com.shiplytics.mobile.network.retrofit;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Enables the usage of the {@link SingleRootKey} annotation on Retrofit method body parameters.
 * @see SingleRootKey
 */
public class SingleKeyRequestConverter extends Converter.Factory {

    private final Converter.Factory converterFactory;
    private final Gson gson;

    SingleKeyRequestConverter(Converter.Factory converterFactory, Gson gson) {
        this.converterFactory = converterFactory;
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        boolean jsonAnnotationPresent = false;
        SingleRootKey singleRootKeyAnnotation = null;

        for (Annotation annotation : parameterAnnotations) {
            if (annotation instanceof Json) {
                jsonAnnotationPresent = true;
            } else if (annotation instanceof SingleRootKey) {
                singleRootKeyAnnotation = (SingleRootKey) annotation;
            }

            if (jsonAnnotationPresent && singleRootKeyAnnotation != null) {
                return new SingleKeyConverter<>(singleRootKeyAnnotation.value(),
                        type,
                        parameterAnnotations,
                        methodAnnotations,
                        retrofit);
            }
        }
        return converterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        return converterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    private final class SingleKeyConverter<T> implements Converter<T, RequestBody> {
        private final Converter<JsonObject, RequestBody> requestBodyConverter;
        private final String rootName;
        private final Type type;

        @SuppressWarnings("unchecked")
        private SingleKeyConverter(String rootName,
                                   Type type,
                                   Annotation[] parameterAnnotations,
                                   Annotation[] methodAnnotations,
                                   Retrofit retrofit) {
            this.requestBodyConverter = (Converter<JsonObject, RequestBody>)converterFactory.requestBodyConverter(
                    JsonObject.class, parameterAnnotations, methodAnnotations, retrofit);
            this.rootName = rootName;
            this.type = type;
        }

        @Override
        public RequestBody convert(@NonNull Object value) throws IOException {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.add(rootName, gson.toJsonTree(value, type));
            return requestBodyConverter.convert(jsonObject);
        }
    }
}
