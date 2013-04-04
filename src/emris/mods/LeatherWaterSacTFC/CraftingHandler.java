package emris.mods.LeatherWaterSacTFC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import TFC.Core.Recipes;
import cpw.mods.fml.common.ICraftingHandler;

public class CraftingHandler implements ICraftingHandler {

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item,	IInventory craftMatrix) {
		if(craftMatrix != null) {
			if(item.itemID == ItemLeatherWaterSac.itemID) {
				if (player.capabilities.isCreativeMode) {
					item.setItemDamage(0);
				}
				
				Item[] tfcKnives = Recipes.Knives;
				for(int i = 0; i < craftMatrix.getSizeInventory(); i++) {
					if(craftMatrix.getStackInSlot(i) == null) {
						continue;
					}
					for(int j = 0; j < tfcKnives.length; j++) {
						if(craftMatrix.getStackInSlot(i).itemID == tfcKnives[j].itemID) {
							ItemStack tfcKnife = craftMatrix.getStackInSlot(i).copy();
							if(tfcKnife != null) {
								tfcKnife.damageItem(1, player);
								if(tfcKnife.getItemDamage() != 0 || player.capabilities.isCreativeMode) {
									craftMatrix.setInventorySlotContents(i, tfcKnife);
									craftMatrix.getStackInSlot(i).stackSize = 2;
								}
							}
						}
					}
				}
			}
		}
		
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {
		// TODO Auto-generated method stub
	}

}
