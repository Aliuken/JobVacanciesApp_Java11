# Non-null examples with JSpecify's @NonNull, Spring's Assert.notNull and Java's Objects.requireNonNull

Given the following imports:

```java
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.springframework.util.Assert;
```

We could have some code like:

```java
@Bean
@NonNull SessionLocaleResolver localeResolver() {
    final SessionLocaleResolver localeResolver = new SessionLocaleResolver() {
        @Override
        public @NonNull Locale resolveLocale(HttpServletRequest httpServletRequest) {
            Assert.notNull(httpServletRequest, "httpServletRequest cannot be null");
            final String languageCode = httpServletRequest.getParameter("languageParam");
            if(LogicalUtils.isNullOrEmptyString(languageCode)) {
                return Locale.US;
            }

            final Locale locale = Locale.forLanguageTag(languageCode);
            Objects.requireNonNull(locale, "locale cannot be null");
            return locale;
        }
    };

    localeResolver.setDefaultLocale(Locale.US);
    return localeResolver;
}
```

Another example in a constructor:

```java
private PageEntityEnum(final String value) {
    this.value = Objects.requireNonNull(value);
}
```

Another example with varargs:

```java
private FileType(final @NonNull String @NonNull ... allowedFileExtensionsVarargs) {
    if(allowedFileExtensionsVarargs.length == 0) {
        throw new IllegalArgumentException("FileType allowedFileExtensions must not be empty");
    }

    final List<String> allowedLowerCaseFileExtensions = new ArrayList<>();
    for(final String allowedFileExtension : allowedFileExtensionsVarargs) {
        final String allowedLowerCaseFileExtension = allowedFileExtension.toLowerCase();
        allowedLowerCaseFileExtensions.add(allowedLowerCaseFileExtension);
    }

    this.allowedLowerCaseFileExtensions = allowedLowerCaseFileExtensions;
    this.allowedLowerCaseFileExtensionsString = StringUtils.getStringJoinedWithDelimiters(", ", null, null, allowedLowerCaseFileExtensions);
}
```

NOTES:
- allowedFileExtensionsVarargs cannot be null because of "@NonNull ..." or "@NonNull []"
- every allowedFileExtension in allowedFileExtensionsVarargs cannot be null because of "@NonNull String"
- To search occurrences, use the regex "\@NonNull.*\[\]" and "\@NonNull.*\.\.\."