package io.github.pinpal;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "biostorage", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RecipeRemover {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String NAMESPACE_TO_REMOVE = "refinedstorage";

	@SubscribeEvent
	public static void onServerStarting(ServerStartingEvent event) {
		// Get the recipe manager
		MinecraftServer server = event.getServer();
		RecipeManager recipeManager = server.getRecipeManager();

		// Use reflection to access the private fields of RecipeManager
		// Debug code to list all fields of RecipeManager to find the correct field name
		// This code is not needed for the actual recipe removal
		// Example Output:
		//Field: f_44005_, Type: class com.google.gson.Gson
		//Field: f_44006_, Type: interface org.slf4j.Logger
		//Field: f_44007_, Type: interface java.util.Map
		//Field: f_199900_, Type: interface java.util.Map
		//Field: f_44008_, Type: boolean
		//Field: context, Type: interface net.minecraftforge.common.crafting.conditions.ICondition$IContext

		// The field we are looking for is the 'recipes' field
		// We are looking for a field of type:
		// Map<RecipeType<?>, Map<ResourceLocation, ?>>
		// In this case, the field name is 'f_44007_'

//		Field[] fields = RecipeManager.class.getDeclaredFields();
//		for (Field field : fields) {
//			field.setAccessible(true);
//			LOGGER.info("Field: {}, Type: {}", field.getName(), field.getType());
//		}


		// Remove recipes from the specified namespace
		removeRecipesFromNamespace(recipeManager, NAMESPACE_TO_REMOVE);
	}

	public static void removeRecipesFromNamespace(RecipeManager recipeManager, String namespace) {
		try {
			// Use reflection to access the private 'recipes' field
			// "f_44007_" is the field name for the 'recipes' field in RecipeManager
			// Obtained for debug code above due to private access
			// Can vary depending on the mappings and obfuscation
			// TODO: All of this is very jank and should be refactored
			Field recipesField = RecipeManager.class.getDeclaredField("f_44007_");

			recipesField.setAccessible(true);

			// Get the value of the 'recipes' field
			Map<RecipeType<?>, Map<ResourceLocation, ?>> immutableRecipes =
					(Map<RecipeType<?>, Map<ResourceLocation, ?>>) recipesField.get(recipeManager);

			// Create a new modifiable map to replace the immutable one
			Map<RecipeType<?>, Map<ResourceLocation, ?>> modifiableRecipes = new HashMap<>();

			for (Map.Entry<RecipeType<?>, Map<ResourceLocation, ?>> entry : immutableRecipes.entrySet()) {
				// Create a modifiable copy of each recipe map
				Map<ResourceLocation, ?> originalMap = entry.getValue();
				Map<ResourceLocation, Object> modifiableMap = new HashMap<>(originalMap);
				modifiableRecipes.put(entry.getKey(), modifiableMap);
			}

			// Remove recipes from the specified namespace
			for (Map<ResourceLocation, ?> map : modifiableRecipes.values()) {
				map.entrySet().removeIf(entry -> namespace.equals(entry.getKey().getNamespace()));
			}

			// Set the modified recipes back to the RecipeManager using reflection
			recipesField.set(recipeManager, modifiableRecipes);

		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.error("Failed to remove recipes", e);
		}
	}
}
