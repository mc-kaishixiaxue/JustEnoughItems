package mezz.jei.api.gui;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.RecipeSlotUnderMouse;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * For addons that want to draw recipe layouts somewhere themselves.
 *
 * Create an instance with {@link IRecipeManager#createRecipeLayoutDrawable(IRecipeCategory, Object, IFocusGroup)}.
 *
 * @since 3.13.2
 */
public interface IRecipeLayoutDrawable<R> {
	/**
	 * Set the position of the recipe layout in screen coordinates.
	 * To help decide on the position, you can get the width and height of this recipe from {@link #getRect()}.
	 *
	 * @since 3.13.2
	 */
	void setPosition(int posX, int posY);

	/**
	 * Draw the recipe without overlays such as item tool tips.
	 *
	 * @since 3.13.2
	 */
	void drawRecipe(GuiGraphics guiGraphics, int mouseX, int mouseY);

	/**
	 * Draw the recipe overlays such as item tool tips.
	 *
	 * @since 4.7.4
	 */
	void drawOverlays(GuiGraphics guiGraphics, int mouseX, int mouseY);

	/**
	 * Returns true if the mouse is hovering over the recipe.
	 * Hovered recipes should be drawn after other recipes to have the drawn tooltips overlap other elements properly.
	 *
	 * @since 3.13.2
	 */
	boolean isMouseOver(double mouseX, double mouseY);

	/**
	 * Returns the ItemStack currently under the mouse, if there is one.
	 *
	 * @see #getIngredientUnderMouse(int, int, IIngredientType) to get other types of ingredient.
	 *
	 * @since 11.1.1
	 */
	default Optional<ItemStack> getItemStackUnderMouse(int mouseX, int mouseY) {
		return getIngredientUnderMouse(mouseX, mouseY, VanillaTypes.ITEM_STACK);
	}

	/**
	 * Returns the ingredient currently under the mouse, if there is one.
	 * Can be an ItemStack, FluidStack, or any other ingredient type registered with JEI.
	 *
	 * @since 11.0.0
	 */
	<T> Optional<T> getIngredientUnderMouse(int mouseX, int mouseY, IIngredientType<T> ingredientType);

	/**
	 * Get the recipe slot currently under the mouse, if there is one.
	 * @since 11.5.0
	 *
	 * @deprecated use {@link #getSlotUnderMouse(double, double)}
	 */
	@Deprecated
	Optional<IRecipeSlotDrawable> getRecipeSlotUnderMouse(double mouseX, double mouseY);

	/**
	 * Get the recipe slot currently under the mouse, if there is one.
	 *
	 * @return the slot under the mouse, with an offset
	 *
	 * @since 15.9.0
	 */
	Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY);

	/**
	 * Get position and size for the recipe in absolute screen coordinates.
	 * @since 11.5.0
	 */
	Rect2i getRect();

	/**
	 * Get position and size for the recipe, including the border drawn around it, in absolute screen coordinates.
	 * @since 15.5.1
	 */
	Rect2i getRectWithBorder();

	/**
	 * Get the position of the recipe transfer button area, relative to the recipe layout drawable.
	 * @since 11.5.0
	 */
	Rect2i getRecipeTransferButtonArea();

	/**
	 * Get the position of the recipe bookmark button area, relative to the recipe layout drawable.
	 * @since 15.5.0
	 */
	Rect2i getRecipeBookmarkButtonArea();

	/**
	 * Get a view of the recipe slots for this recipe layout.
	 * @since 11.5.0
	 */
	IRecipeSlotsView getRecipeSlotsView();

	/**
	 * Get the recipe category that this recipe layout is a part of.
	 * @since 11.5.0
	 */
	IRecipeCategory<R> getRecipeCategory();

	/**
	 * Get the recipe that this recipe layout displays.
	 * @since 11.5.0
	 */
	R getRecipe();

	/**
	 * Get the input handler for this recipe layout.
	 *
	 * @since 15.9.0
	 */
	IJeiInputHandler getInputHandler();

	/**
	 * Update the recipe layout on game tick.
	 *
	 * @since 15.10.0
	 */
	void tick();
}
