package toolbox.common.recipes;

import java.util.ArrayList;
import java.util.List;

import api.materials.AdornmentMaterial;
import api.materials.HaftMaterial;
import api.materials.HandleMaterial;
import api.materials.HeadMaterial;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import toolbox.common.items.ModItems;
import toolbox.common.items.tools.IAdornedTool;
import toolbox.common.items.tools.IHaftTool;
import toolbox.common.items.tools.IHandleTool;
import toolbox.common.items.tools.IHeadTool;
import toolbox.common.materials.ModMaterials;

public class AxeRecipe extends ToolRecipe {

	private HeadMaterial headMat = null;
	private HaftMaterial haftMat = null;
	private HandleMaterial handleMat = null;
	private AdornmentMaterial adornmentMat = null;

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		headMat = null;
		haftMat = null;
		handleMat = null;
		adornmentMat = null;
		int haftSlot = -1;
		List<ItemStack> items = new ArrayList<>();
		boolean[] slots = new boolean[inv.getSizeInventory()];
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack temp = inv.getStackInSlot(i).copy();
			if (!temp.isEmpty()) {
				if (!slots[i] && headMat == null && temp.getItem() == ModItems.axe_head) {
					for (ItemStack test : ModRecipes.head_map.keySet()) {
						if (headMat == null && ItemStack.areItemsEqual(test, temp)
								&& ItemStack.areItemStackTagsEqual(test, temp)) {
							headMat = ModRecipes.head_map.get(test);
							slots[i] = true;
						}
					}
				}
				if (!slots[i] && haftMat == null) {
					for (ItemStack test : ModRecipes.haft_map.keySet()) {
						if (haftMat == null && ItemStack.areItemsEqual(test, temp)
								&& ItemStack.areItemStackTagsEqual(test, temp)) {
							haftMat = ModRecipes.haft_map.get(test);
							slots[i] = true;
							haftSlot = i;
						}
					}
				}
				if (!slots[i] && handleMat == null) {
					for (ItemStack test : ModRecipes.handle_map.keySet()) {
						if (handleMat == null && ItemStack.areItemsEqual(test, temp)
								&& ItemStack.areItemStackTagsEqual(test, temp)) {
							handleMat = ModRecipes.handle_map.get(test);
							slots[i] = true;
						}
					}
				}
				if (!slots[i] && handleMat == null && haftMat != null && haftSlot > -1) {
					ItemStack haft = inv.getStackInSlot(haftSlot).copy();
					for (ItemStack test : ModRecipes.handle_map.keySet()) {
						if (handleMat == null && ItemStack.areItemsEqual(test, haft)
								&& ItemStack.areItemStackTagsEqual(test, haft)) {
							HandleMaterial tempHandleMat = ModRecipes.handle_map.get(test);
							for (ItemStack test2 : ModRecipes.haft_map.keySet()) {
								if (ItemStack.areItemsEqual(test2, temp)
										&& ItemStack.areItemStackTagsEqual(test2, temp)) {
									haftMat = ModRecipes.haft_map.get(test2);
									handleMat = tempHandleMat;
									slots[i] = true;
									haftSlot = i;
									break;
								}
							}
						}
					}
				}
				if (!slots[i] && adornmentMat == null) {
					for (ItemStack test : ModRecipes.adornment_map.keySet()) {
						if (adornmentMat == null && ItemStack.areItemsEqual(test, temp)
								&& ItemStack.areItemStackTagsEqual(test, temp)) {
							adornmentMat = ModRecipes.adornment_map.get(test);
							slots[i] = true;
						}
					}
				}
				items.add(temp);
			}
		}
		if (items.size() > getRecipeSize()) {
			return false;
		}

		if (headMat == null || haftMat == null || handleMat == null) {
			return false;
		}

		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack out = new ItemStack(ModItems.axe);

		if (adornmentMat == null) {
			adornmentMat = ModMaterials.ADORNMENT_NULL;
		}

		if (headMat == null || haftMat == null || handleMat == null || adornmentMat == null) {
			return ItemStack.EMPTY;
		}

		NBTTagCompound tag = new NBTTagCompound();
		tag.setString(IHeadTool.HEAD_TAG, headMat.getName());
		tag.setString(IHaftTool.HAFT_TAG, haftMat.getName());
		tag.setString(IHandleTool.HANDLE_TAG, handleMat.getName());
		tag.setString(IAdornedTool.ADORNMENT_TAG, adornmentMat.getName());
		out.setTagCompound(tag);

		return out;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= getRecipeSize();
	}

	public int getRecipeSize() {
		if (adornmentMat == null) {
			return 3;
		}
		return 4;
	}

}
