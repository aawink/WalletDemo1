package com.shiplytics.mobile.network.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Converts types to JSON or XML, depending on how they're annotated in the Retrofit method.
 * @see Json
 * @see Xml
 */
public class AnnotatedTypeConverter extends Converter.Factory {

    private final Converter.Factory jsonFactory;
    private final Converter.Factory xmlFactory;

    AnnotatedTypeConverter(Converter.Factory jsonFactory, Converter.Factory xmlFactory) {
        this.jsonFactory = jsonFactory;
        this.xmlFactory = xmlFactory;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Json) {
                return jsonFactory.responseBodyConverter(type, annotations, retrofit);
            }
            if (annotation instanceof Xml) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        for (Annotation annotation : parameterAnnotations) {
            if (annotation instanceof Json) {
                return jsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,
                        retrofit);
            }
            if (annotation instanceof Xml) {
                return xmlFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,
                        retrofit);
            }
        }
        return null;
    }
}
