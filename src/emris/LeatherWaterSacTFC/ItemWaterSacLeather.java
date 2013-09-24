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

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWaterSacLeather extends Item
{
	public ItemWaterSacLeather(int par1) {
		super(par1);
		maxStackSize = 12;
		setCreativeTab(CreativeTabs.tabMisc);
		hasSubtypes = false;
		setUnlocalizedName("WaterSacLeather");
		LanguageRegistry.addName(this, "Water Sac Leather");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister registerer) {
		this.itemIcon = registerer.registerIcon("leatherwatersac:WaterSacLeather");
	}
}
