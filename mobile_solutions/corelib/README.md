# Corelib

1. [Quickstart](#quickstart)
1. [Usage](#usage)
    1. [BaseAdapterResponse](#usage_base_adapter_response)
    1. [Json](#usage_json)
    1. [CollectionUtils](#usage_collection_utils)

<a name="quickstart"></a>
## Quickstart

### Install Dagger module
```java
@Singleton
@Component(modules = {
        CoreModule.class,
        //...
})
public interface SomeComponent {
//...
```

You can now use the published objects in your Dagger component that are defined in
[CoreModule.kt](src/main/java/com/etrade/eo/core/CoreModule.kt)

<a name="usage"></a>
# Usage

<a name="usage_base_adapter_response"></a>
## BaseAdapterResponse
[BaseAdapterResponse](src/main/java/com/etrade/eo/core/domain/BaseAdapterResponse.kt) is meant to be a class that is
extended by the data transfer objects that represent HTTP responses from the Adapter backend.

This class will provide the types necessary to represent the errors, and utility functions to work with them
such as `extractErrors()` that will provide a summary of the problem present in the errors.

<a name="usage_json"></a>
## Json

Reusing the same instance of Gson will lead to performance gains due to caching in the serialization
routines. So inject the Gson variant of the JSON utility when you need to use it!

```java

public class SomeHttpIntegration {

    private final JsonUtil jsonUtil;

    @Inject
    SomeHttpIntegration(@JsonUtilGson JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }
}
```

### Registering custom type adapters

If using the Gson JsonUtility, and want to provide a custom (de)serialiation for a type, you can
`@Provides` a [GsonTypeAdapter<T>](src/main/java/com/etrade/eo/core/json/serializers/GsonTypeAdapter.java)
into the same Dagger component. See the JavaDocs for more details.

<a name="usage_collection_utils"></a>
## CollectionUtils

[CollectionUtils](src/main/java/com/etrade/eo/core/util/CollectionUtils.java) contains various static
utility functions to assist with very common boilerplate code.
```java
CollectionUtils.isNullOrEmpty(someCollection)
```

## DateFormattingUtils

This utility class is used to format different kind of date representation to strings.
Example:
```java
DateFormattingUtils.formatExpirationDateUtc(instrument.getExpirationDateUtc() - DateUtils.DAY_IN_MILLIS);
```
