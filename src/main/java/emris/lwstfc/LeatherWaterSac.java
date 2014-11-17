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

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="lwstfc", name="Leather Water Sac", version="3.4.B79", dependencies = "after:TerraFirmaCraft")
public class LeatherWaterSac
{
	@Instance("LeatherWaterSac")
	public static LeatherWaterSac instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LWSItems.Setup();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		LWSRecipes.registerRecipes();
		FMLCommonHandler.instance().bus().register(new CraftingHandler());
		MinecraftForge.EVENT_BUS.register(new TFCAnimalDropEvent());
	}
}
