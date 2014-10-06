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

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class LWSItems
{
	public static Item itemLeatherWaterSac;
	public static Item itemWaterSacLeather;
	public static Item itemSheepBladder;

	public static void Setup()
	{
		itemLeatherWaterSac = new ItemLeatherWaterSac();
		itemWaterSacLeather = new ItemWaterSacLeather();
		itemSheepBladder = new ItemSheepBladder();

		GameRegistry.registerItem(itemLeatherWaterSac, itemLeatherWaterSac.getUnlocalizedName());
		GameRegistry.registerItem(itemWaterSacLeather, itemWaterSacLeather.getUnlocalizedName());
		GameRegistry.registerItem(itemSheepBladder, itemSheepBladder.getUnlocalizedName());
	}
}
