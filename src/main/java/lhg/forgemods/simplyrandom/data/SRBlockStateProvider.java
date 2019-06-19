package lhg.forgemods.simplyrandom.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SRBlockStateProvider implements IDataProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create();
    private static final Function<Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING = new Function<Entry<IProperty<?>, Comparable<?>>, String>()
    {
        public String apply(@Nullable Entry<IProperty<?>, Comparable<?>> prop)
        {
            if (prop == null)
            {
                return "<NULL>";
            } else
            {
                IProperty<?> key = prop.getKey();
                return key.getName() + "=" + this.getPropertyName(key, prop.getValue());
            }
        }

        private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> entry)
        {
            return property.getName((T) entry);
        }
    };

    private DataGenerator generator;

    public SRBlockStateProvider(DataGenerator generator)
    {

        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache)
    {
        for (Block block : ForgeRegistries.BLOCKS.getValues())
        {
            final ResourceLocation id = block.getRegistryName();
            if (!"simplyrandom".equals(id.getNamespace()))
            {
                continue;
            }

            Path path = makePath(id);
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            for (BlockState state : block.getStateContainer().getValidStates())
            {
                String variant = state.getValues().entrySet().stream()
                    .map(MAP_ENTRY_TO_STRING)
                    .collect(Collectors.joining(","));
                variants.add(variant, getDefaultModel(id));
            }
            blockstate.add("variants", variants);

            try
            {
                String s = GSON.toJson(blockstate);
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(path))
                {
                    writer.write(s);
                }
            } catch (IOException ex)
            {
                LOGGER.error("Couldn't save blockstate to {}", path, ex);
            }
        }
    }

    @Override
    public String getName()
    {
        return "Simply Random Blockstate Generator";
    }

    private JsonObject getDefaultModel(ResourceLocation id)
    {
        final JsonObject model = new JsonObject();
        model.addProperty("model", id.getNamespace() + ":blocks/" + id.getPath());
        return model;
    }

    private Path makePath(ResourceLocation id)
    {
        return this.generator.getOutputFolder().resolve("assets/" + id.getNamespace() + "/blockstates/" + id.getPath() + ".json");
    }
}
