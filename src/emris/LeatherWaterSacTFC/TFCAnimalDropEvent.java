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

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import TFC.Entities.Mobs.EntitySheepTFC;

public class TFCAnimalDropEvent {
	@ForgeSubscribe
	public void onTFCAnimalDrop(LivingDropsEvent e) {
		if(e.entityLiving instanceof EntitySheepTFC) {
			addDrops(e, new ItemStack(LWSItems.itemSheepBladder, 1));
		}
	}
	
	void addDrops (LivingDropsEvent event, ItemStack dropStack) {
		EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
		entityitem.delayBeforeCanPickup = 10;
		event.drops.add(entityitem);
	}
}
