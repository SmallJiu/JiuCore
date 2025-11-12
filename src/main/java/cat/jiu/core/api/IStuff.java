package cat.jiu.core.api;

import cat.jiu.core.util.registry.DynamicLanguageProvider;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import java.util.List;

@SuppressWarnings("unchecked")
public interface IStuff {
    default <T extends IStuff> T self() {
        return (T) this;
    }
    AbstractRegistrate<?> getRegistrate();

    List<Language> getLanguages();

    default <T extends IStuff> T addLanguage(String languageCode, String key, String name) {
        return this.addLanguage(languageCode, key::toString, name);
    }
    default <T extends IStuff> T addLanguage(String languageCode, NonNullSupplier<String> key, String name) {
        this.getLanguages().add(new Language(languageCode, name, key));
        return this.self();
    }

    default <T extends IStuff> T registerLanguage(DynamicLanguageProvider provider) {
        if (this.getLanguages() != null) {
            this.getLanguages().forEach(language ->
                provider.add(language.languageCode, language.key.get(), language.name)
            );
        }
        return this.self();
    }

    class Language {
        public final String languageCode, name;
        public final NonNullSupplier<String> key;
        public Language(String languageCode, String name, NonNullSupplier<String> key) {
            this.languageCode = languageCode;
            this.name = name;
            this.key = key;
        }
    }
}
