/**
 *  Copyright (C) 2013  emris
 *  https://github.com/emris/LeatherWaterSacTFC
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package emris.LeatherWaterSacTFC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import TFC.Core.Recipes;
import cpw.mods.fml.common.ICraftingHandler;

public class CraftingHandler implements ICraftingHandler
{
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item,	IInventory craftMatrix)
	{
		if(craftMatrix != null)
		{
			if(item.itemID == ItemLeatherWaterSac.itemID)
			{
				if (player.capabilities.isCreativeMode)
					item.setItemDamage(0);
				
				Item[] tfcKnives = Recipes.Knives;
				for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
				{
					if(craftMatrix.getStackInSlot(i) == null)
						continue;

					for(int j = 0; j < tfcKnives.length; j++)
					{
						if(craftMatrix.getStackInSlot(i).itemID == tfcKnives[j].itemID)
						{
							ItemStack tfcKnife = craftMatrix.getStackInSlot(i).copy();
							if(tfcKnife != null) {
								tfcKnife.damageItem(1, player);
								if(tfcKnife.getItemDamage() != 0 || player.capabilities.isCreativeMode)
								{
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
	public void onSmelting(EntityPlayer player, ItemStack item)
	{
	}

}
