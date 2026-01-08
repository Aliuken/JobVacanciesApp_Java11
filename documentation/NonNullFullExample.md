# Non-null example with JSpecify's @NonNull, Spring's Assert.notNull and Java's Objects.requireNonNull

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