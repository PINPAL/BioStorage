package io.github.pinpal;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class BiostorageJEI implements IModPlugin {

	private static final List<ResourceLocation> ITEMS_TO_KEEP = new ArrayList<>();

	static {
		// Add resource locations of items to keep
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "quartz_enriched_iron"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "cable"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "grid"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "crafting_grid"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "fluid_grid"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "controller"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "creative_controller"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "external_storage"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "interface"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "fluid_interface"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "processor_binding"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "basic_processor"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "improved_processor"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "advanced_processor"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "silicon"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "creative_storage_block"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "importer"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "exporter"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "upgrade"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "speed_upgrade"));
		ITEMS_TO_KEEP.add(new ResourceLocation("refinedstorage", "stack_upgrade"));

	}

	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registration) {
		List<ItemStack> itemStacksToRemove = new ArrayList<>();

		// Iterate through all items registered with the minecraft mod ID
		for (ResourceLocation location : ForgeRegistries.ITEMS.getKeys()) {
			if (location.getNamespace().equals("refinedstorage") && !ITEMS_TO_KEEP.contains(location)) {
				Item item = ForgeRegistries.ITEMS.getValue(location);
				if (item != null) {
					ItemStack stack = new ItemStack(item);
					if (!stack.isEmpty()) {
						itemStacksToRemove.add(stack);
					} else {
						System.out.println("Empty ItemStack for item: " + location.toString());
					}
				}
			}
		}

		// Debug output to check which items are being removed
		System.out.println("Items to remove: " + itemStacksToRemove);

		// Remove all identified items from JEI
		registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemStacksToRemove);
	}

	@Override
	@Nonnull
	public ResourceLocation getPluginUid() {
		return new ResourceLocation("biostorage", "biostorage_jei_plugin");
	}
}
