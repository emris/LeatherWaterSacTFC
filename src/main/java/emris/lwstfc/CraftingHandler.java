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
package emris.lwstfc;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.Core.Recipes;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class CraftingHandler
{
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent e)
	{
		Item item = e.crafting.getItem();
		IInventory craftMatrix = e.craftMatrix;

		if(craftMatrix != null)
		{
			if(item == LWSItems.itemLeatherWaterSac)
			{
				if (e.player.capabilities.isCreativeMode)
					e.crafting.setItemDamage(0);
				
				for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
				{
					if(craftMatrix.getStackInSlot(i) == null)
						continue;

					for(int j = 0; j < Recipes.Knives.length; j++)
					{
						if(craftMatrix.getStackInSlot(i).getItem() == Recipes.Knives[j])
						{
							ItemStack tfcKnife = craftMatrix.getStackInSlot(i).copy();
							if(tfcKnife != null)
							{
								tfcKnife.damageItem(1, e.player);
								if(tfcKnife.getItemDamage() != 0 || e.player.capabilities.isCreativeMode)
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

}
