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

import java.io.File;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import TFC.TerraFirmaCraft;

public class LWSItems
{
	static int itemLeatherWaterSacID;
	static int itemWaterSacLeatherID;
	static int itemSheepBladderID;
	static Item itemLeatherWaterSac;
	static Item itemWaterSacLeather;
	static Item itemSheepBladder;

	public static void Setup()
	{
		loadIDs();
		itemLeatherWaterSac = new ItemLeatherWaterSac(itemLeatherWaterSacID);
		itemWaterSacLeather = new ItemWaterSacLeather(itemWaterSacLeatherID);
		itemSheepBladder = new ItemSheepBladder(itemSheepBladderID);
	}

	private static void loadIDs()
	{
		Configuration config;
		try
		{
			config = new Configuration(new File(TerraFirmaCraft.proxy.getMinecraftDir(), "/config/LWSOptions.cfg"));
			config.load();
		}
		catch (Exception e)
		{
			System.out.println(new StringBuilder().append("[LWS] Error while trying to access settings configuration!").toString());
			config = null;
		}
		System.out.println(new StringBuilder().append("[LWS] Loading Settings").toString());

		itemLeatherWaterSacID = getIntFor(config, "General", "LeatherWaterSacID", 5252);
		itemWaterSacLeatherID = getIntFor(config, "General", "WaterSacLeatherID", 5253);
		itemSheepBladderID = getIntFor(config, "General", "SheepBladderID", 5254);

		if (config != null)
			config.save();
	}
	
	private static int getIntFor(Configuration config, String heading, String item, int value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getInt(value);
		}
		catch (Exception e)
		{
			System.out.println(new StringBuilder().append("[LWS] Error while trying to add Integer, config wasn't loaded properly!").toString());
		}
		return value;
	}
}
