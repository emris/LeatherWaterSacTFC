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

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.bioxx.tfc.Core.Recipes;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Crafting.CraftingManagerTFC;

import cpw.mods.fml.common.registry.GameRegistry;

public class LWSRecipes
{
	public static void registerRecipes()
	{
		ItemStack lwSac = new ItemStack(LWSItems.itemLeatherWaterSac, 1, LWSItems.itemLeatherWaterSac.getMaxDamage());
		ItemStack leather = new ItemStack(LWSItems.itemWaterSacLeather, 1);
		ItemStack bladderSheep = new ItemStack(LWSItems.itemBladder, 1, 0);
		ItemStack bladderCow = new ItemStack(LWSItems.itemBladder, 1, 1);
		ItemStack bladderDeer = new ItemStack(LWSItems.itemBladder, 1, 2);
		ItemStack string_tfc = new ItemStack(TFCItems.woolYarn, 1);

		for(int j = 0; j < Recipes.knives.length; j++)
		{
			// With String
			GameRegistry.addRecipe(lwSac,new Object[]{" L ","#B#"," LK",
				Character.valueOf('#'), string_tfc,
				Character.valueOf('L'), leather,
				Character.valueOf('B'), bladderSheep,
				Character.valueOf('K'), new ItemStack(Recipes.knives[j], 1, OreDictionary.WILDCARD_VALUE)
			});
			GameRegistry.addRecipe(lwSac,new Object[]{" # ","LBL"," #K",
				Character.valueOf('#'), string_tfc,
				Character.valueOf('L'), leather,
				Character.valueOf('B'), bladderSheep,
				Character.valueOf('K'), new ItemStack(Recipes.knives[j], 1, OreDictionary.WILDCARD_VALUE)
			});

			GameRegistry.addRecipe(lwSac,new Object[]{" L ","#B#"," LK",
				Character.valueOf('#'), string_tfc,
				Character.valueOf('L'), leather,
				Character.valueOf('B'), bladderCow,
				Character.valueOf('K'), new ItemStack(Recipes.knives[j], 1, OreDictionary.WILDCARD_VALUE)
			});
			GameRegistry.addRecipe(lwSac,new Object[]{" # ","LBL"," #K",
				Character.valueOf('#'), string_tfc,
				Character.valueOf('L'), leather,
				Character.valueOf('B'), bladderCow,
				Character.valueOf('K'), new ItemStack(Recipes.knives[j], 1, OreDictionary.WILDCARD_VALUE)
			});

			GameRegistry.addRecipe(lwSac,new Object[]{" L ","#B#"," LK",
				Character.valueOf('#'), string_tfc,
				Character.valueOf('L'), leather,
				Character.valueOf('B'), bladderDeer,
				Character.valueOf('K'), new ItemStack(Recipes.knives[j], 1, OreDictionary.WILDCARD_VALUE)
			});
			GameRegistry.addRecipe(lwSac,new Object[]{" # ","LBL"," #K",
				Character.valueOf('#'), string_tfc,
				Character.valueOf('L'), leather,
				Character.valueOf('B'), bladderDeer,
				Character.valueOf('K'), new ItemStack(Recipes.knives[j], 1, OreDictionary.WILDCARD_VALUE)
			});
		}
		CraftingManagerTFC.getInstance().addRecipe(new ItemStack(LWSItems.itemWaterSacLeather, 1), new Object[] {
			"   ##",
			" # ##",
			"#####",
			"#####",
			" ### ",
			'#', TFCItems.flatLeather });
	}
}
