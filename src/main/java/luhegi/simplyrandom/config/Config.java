package luhegi.simplyrandom.config;

import luhegi.simplyrandom.SimplyRandom;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class Config {
    private ForgeConfigSpec spec;
    public IntValue cobblestone_generator_capacity;
    public IntValue cobblestone_generator_cost;

    public Config() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("cobblestone_generator");
        cobblestone_generator_capacity = builder.comment("The amount of FE a cobblestone generator can store.",
                "Note: If this is smaller than the cost per block, it will be impossible to create cobble with this block.")
                .translation(translation("cobblestone_generator_capacity"))
                .defineInRange("cobblestone_generator_capacity", 0, 0, Integer.MAX_VALUE);
        cobblestone_generator_cost = builder.comment("The amount of FE a cobblestone generator takes to create a single cobblestone block.")
                .translation(translation("cobblestone_generator_cost"))
                .defineInRange("cobblestone_generator_cost", 0, 0, Integer.MAX_VALUE);
        builder.pop();

        spec = builder.build();
    }

    public ForgeConfigSpec getSpec() {
        return spec;
    }

    private String translation(String key) {
        return String.format("config.%s.%s", SimplyRandom.ID, key);
    }
}
