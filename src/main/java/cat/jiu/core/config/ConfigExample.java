package cat.jiu.core.config;

import cat.jiu.core.util.base.BaseConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.*;

import java.util.Arrays;
import java.util.List;

public class ConfigExample extends BaseConfig {
    public final ConfigList LISTS;
    public final ModConfigSpec.IntValue INT_TYPE;
    public final LongValue LONG_TYPE;
    public final ConfigValue<Float> FLOAT_TYPE;
    public final DoubleValue DOUBLE_TYPE;
    public final ConfigValue<String> STRING_TYPE;
    public final ConfigValue<String> STRING_TYPE_IN_LIST;
    public final BooleanValue BOOLEAN_TYPE;
    public final EnumValue<SoundSource> ENUM_TYPE;

    public ConfigExample(ModConfigSpec.Builder builder) {
        super(builder);
        builder.translation("example").push("example");
        this.LISTS = new ConfigList(builder);

        this.INT_TYPE = builder.worldRestart()
                .comment("int type config,", "min and max can be any Integer number")
                .defineInRange("int_type", 998, Integer.MIN_VALUE, Integer.MAX_VALUE);

        this.LONG_TYPE = builder.worldRestart()
                .comment("long type config,", "min and max can be any Long number")
                .defineInRange("long_type", 998, Long.MIN_VALUE, Long.MAX_VALUE);

        this.FLOAT_TYPE = builder.worldRestart()
                .comment("float type config,", "min and max can be any Float number")
                .defineInRange("float_type", 9.98F, Float.MIN_VALUE, Float.MIN_VALUE, Float.class);

        this.DOUBLE_TYPE = builder.worldRestart()
                .comment("double type config,", "min and max can be any Double number")
                .defineInRange("double_type", 9.98D, Double.MIN_VALUE, Double.MIN_VALUE);

        this.STRING_TYPE = builder.worldRestart()
                .comment("string type config,", "value can be any char.")
                .define("string_type", "this is a string.");

        this.STRING_TYPE_IN_LIST = builder.worldRestart()
                .comment("string type config,", "value can be any char.")
                .define("string_type_inlist", ()->"this is a string in list.", k->Arrays.asList("123", "s").contains(String.valueOf(k)));

        this.BOOLEAN_TYPE = builder.worldRestart()
                .comment("boolean type config,", "value can be true or false")
                .define("boolean_type", true);

        this.ENUM_TYPE = builder.worldRestart()
                .comment("this is enum type config,", "click button to change enum.")
                .defineEnum("enum_type", SoundSource.MASTER);

        builder.pop();
    }

    public static class ConfigList extends BaseConfig {
        public final ConfigValue<List<? extends Integer>> INT_TYPES;
        public final ConfigValue<List<? extends Long>> LONG_TYPES;
        public final ConfigValue<List<? extends Float>> FLOAT_TYPES;
        public final ConfigValue<List<? extends Double>> DOUBLE_TYPES;
        public final ConfigValue<List<? extends String>> STRING_TYPES;
        public final ConfigValue<List<? extends SoundSource>> ENUM_TYPES;

        public ConfigList(Builder builder) {
            super(builder);
            builder.translation("sub types").push("sub types");

            this.INT_TYPES = builder.worldRestart()
                    .comment("int type config,", "this is a Integer type config list.")
                    .defineList("int_types", Arrays.asList(1,2,3,4,5), t->true);

            this.LONG_TYPES = builder.worldRestart()
                    .comment("long type config,", "this is a Long type config list.")
                    .defineList("long_types", Arrays.asList(1L,2L,3L,4L,5L), t->true);

            this.FLOAT_TYPES = builder.worldRestart()
                    .comment("float type config,", "this is a Float type config list.")
                    .defineList("float_types", Arrays.asList(1f,2f,3f,4f,5f), t->true);

            this.DOUBLE_TYPES = builder.worldRestart()
                    .comment("double type config,", "this is a Double type config list.")
                    .defineList("double_types", Arrays.asList(1d,2d,3d,4d,5d), t->true);

            this.STRING_TYPES = builder.worldRestart()
                    .comment("string type config,", "this is a String type config list.")
                    .defineList("string_types", Arrays.asList("str1", "str2", "str3", "str4", "str5"), t->true);

            this.ENUM_TYPES = builder.worldRestart()
                    .comment("enum type config,", "this is a Enum type config list.")
                    .defineList("enum_types", Arrays.asList(SoundSource.MASTER, SoundSource.MUSIC, SoundSource.PLAYERS, SoundSource.BLOCKS, SoundSource.VOICE), t->true);
            builder.pop();
        }
    }
}
