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

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import TFC.Core.TFC_Core;
import TFC.Food.FoodStatsTFC;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLeatherWaterSac extends Item {

	public static int itemID;

	public ItemLeatherWaterSac(int par1) {
		super(par1);

		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabMisc);
		setMaxDamage(12);
		hasSubtypes = false;
		LanguageRegistry.addName(this, "Leather Water Sac");
		itemID = 256 + par1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister registerer) {
		this.itemIcon = registerer.registerIcon("leatherwatersac:LeatherWaterSac");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack sac, World world, EntityPlayer player) {
		if (player.capabilities.isCreativeMode) {
			sac.setItemDamage(0);
		}

		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
		if (mop == null) {
			if(sac.getItemDamage() == sac.getMaxDamage()){
				if(player instanceof EntityPlayerMP) { player.sendChatToPlayer(ChatMessageComponent.createFromText("Empty!")); }
			} else {
				player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
			}
			return sac;
		} else {
			if(mop.typeOfHit == EnumMovingObjectType.TILE) {
				int x = mop.blockX;
				int y = mop.blockY;
				int z = mop.blockZ;

				if(!world.canMineBlock(player, x,  y,  z)) {
					return sac;
				}

				if(!player.canPlayerEdit(x, y, z, mop.sideHit, sac)) {
					return sac;
				}

				if(world.getBlockMaterial(x, y, z) == Material.water) {
					if(sac.getItemDamage() > 0) {
						if (!player.capabilities.isCreativeMode) {
							sac.setItemDamage(sac.getItemDamage() - 6);
							world.spawnParticle("splash", x, y+2, z, 0.0D, 0.0D, 0.0D);
							world.playSoundAtEntity(player, "random.splash", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
						}
					} else {
						player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
					}
				} else {
					if(sac.getItemDamage() == sac.getMaxDamage()){
						if(player instanceof EntityPlayerMP) { player.sendChatToPlayer(ChatMessageComponent.createFromText("Empty!")); }
					} else {
						player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
					}
				}
			}
			return sac;
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack sac) {
		return EnumAction.drink;
	}

	public int getMaxItemUseDuration(ItemStack sac) {
		return 32;
	}

	@Override
	public ItemStack onEaten(ItemStack sac, World world, EntityPlayer player) {
		if(!world.isRemote) {
			if(sac.getItemDamage() != sac.getMaxDamage() || player.capabilities.isCreativeMode) {
				if(player instanceof EntityPlayerMP) {
					EntityPlayerMP p = (EntityPlayerMP)player;
					FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(player);
					if (fs.getMaxWater(p) - 500 > fs.waterLevel) {
						float nwl = fs.getMaxWater(p);
						int rw = (int)nwl / 6;
						fs.restoreWater(p, rw);
						if (!player.capabilities.isCreativeMode) {
							sac.setItemDamage(sac.getItemDamage() + 1);
						}
					} else {
						world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
						player.sendChatToPlayer(ChatMessageComponent.createFromText("I'm full!"));
						// For testing
						//fs.restoreWater(p, -(fs.getMaxWater(p)-500));
					}
				}
			}
		}
		return sac;
	}
}
