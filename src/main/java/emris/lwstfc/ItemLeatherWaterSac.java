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

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.Core.Player.FoodStatsTFC;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCFluids;
import com.bioxx.tfc.api.Enums.EnumFoodGroup;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.ISize;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLeatherWaterSac extends Item implements ISize, IFluidContainerItem
{
	private int capacity;
	private int drinkAmount;

	public ItemLeatherWaterSac()
	{
		super();
		this.maxStackSize = 1;
		this.capacity = 600;
		this.drinkAmount = 50;
		this.setCreativeTab(TFCTabs.TFC_MISC);
		this.setMaxDamage(capacity);
		this.hasSubtypes = false;
		this.setUnlocalizedName("LeatherWaterSac");
		this.canStack();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		list.add(new ItemStack(this, 1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister registerer)
	{
		this.itemIcon = registerer.registerIcon("lwstfc:LeatherWaterSac");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack sac, World world, EntityPlayer player)
	{
		// Do not use in creative game mode
		if(player.capabilities.isCreativeMode)
			return sac;

		// Fix item damage if sac is empty and damage is not maxDamage for some reason
		if(this.getFluid(sac) == null && sac.getItemDamage() != sac.getMaxDamage())
			sac.setItemDamage(sac.getMaxDamage());

		// Right click + Sneak will empty the sac
		if(player.isSneaking())
		{
			this.drain(sac, capacity, true);
			return sac;
		}

		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
		if (mop == null)
		{
			if(sac.getItemDamage() == sac.getMaxDamage())
			{
				if(player instanceof EntityPlayerMP)
					player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("sac.empty")));
			}
			else
			{
				player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
			}
			return sac;
		}
		else
		{
			if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				WPos wp = findWater(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);

				if(!world.canMineBlock(player, wp.getX(), wp.getY(), wp.getZ()))
					return sac;

				if(!player.canPlayerEdit(wp.getX(), wp.getY(), wp.getZ(), mop.sideHit, sac))
					return sac;

				if(wp.isWater())
				{
					if(sac.getItemDamage() > 0)
					{
						fillSac(world, sac, wp.getX(), wp.getY(), wp.getZ(), 200);

						double xHit = mop.hitVec.xCoord;
						double yHit = mop.hitVec.yCoord;
						if(world.getBlockMetadata(wp.getX(), wp.getY(), wp.getZ()) > 0 && mop.sideHit == 1) yHit += 1;
						double zHit = mop.hitVec.zCoord;
						for(int l = 0; l < 5; l++)
						{
							world.spawnParticle("splash", xHit, yHit, zHit, (-0.5F + world.rand.nextFloat()), (-0.5F + world.rand.nextFloat()), (-0.5F + world.rand.nextFloat()));
						}
						world.playSoundAtEntity(player, "random.splash", 0.2F, world.rand.nextFloat() * 0.1F + 0.9F);
					}
					else
					{
						player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
					}
				}
				else
				{
					if(sac.getItemDamage() == sac.getMaxDamage())
					{
						if(player instanceof EntityPlayerMP)
							player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("sac.empty")));
					}
					else
					{
						player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
					}
				}
			}
			return sac;
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack sac)
	{
		return EnumAction.drink;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack sac)
	{
		return 32;
	}

	@Override
	public ItemStack onEaten(ItemStack sac, World world, EntityPlayer player)
	{
		if(!world.isRemote && !player.capabilities.isCreativeMode && player instanceof EntityPlayerMP)
		{
			FluidStack sacFS = this.getFluid(sac);
			if(sacFS != null)
			{
				EntityPlayerMP p = (EntityPlayerMP)player;
				FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(p);
				float nwl = fs.getMaxWater(p);
				int rw = (int)nwl / 6;

				if (sacFS.getFluid() == TFCFluids.FRESHWATER)
				{
					if (fs.needDrink())
					{
						fs.restoreWater(p, rw);
						this.drain(sac, drinkAmount, true);
					}
				}
				else if (sacFS.getFluid() == TFCFluids.SALTWATER && fs.needDrink())
				{
					fs.restoreWater(p, -rw);
					this.drain(sac, drinkAmount, true);
					p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("drink.salt")));
					//Do some kind of spit particle animation
					p.getServerForPlayer().getEntityTracker().func_151248_b(p, new S0BPacketAnimation(p, 4));
				}
				else if (isAlcohol(sacFS))
				{
					if (fs.needDrink())
					{
						fs.restoreWater(p, 800);
						this.drain(sac, drinkAmount, true);

						int time = world.rand.nextInt(1000) + 400;
						fs.consumeAlcohol();

						if(world.rand.nextInt(100) == 0) p.addPotionEffect(new PotionEffect(8, time, 4));
						if(world.rand.nextInt(100) == 0) p.addPotionEffect(new PotionEffect(5, time, 4));
						if(world.rand.nextInt(100) == 0) p.addPotionEffect(new PotionEffect(8, time, 4));
						if(world.rand.nextInt(200) == 0) p.addPotionEffect(new PotionEffect(10, time, 4));
						if(world.rand.nextInt(150) == 0) p.addPotionEffect(new PotionEffect(12, time, 4));
						if(world.rand.nextInt(180) == 0) p.addPotionEffect(new PotionEffect(13, time, 4));

						int levelMod = 250 * p.experienceLevel;
						if(fs.soberTime > TFC_Time.getTotalTicks() + 3000 + levelMod)
						{
							if(world.rand.nextInt(4) == 0)
							{
								//player.addPotionEffect(new PotionEffect(9,time,4));
							}

							if(fs.soberTime > TFC_Time.getTotalTicks() + 5000 + levelMod)
							{
								if(world.rand.nextInt(4) == 0)
									p.addPotionEffect(new PotionEffect(18, time, 4));

								if(fs.soberTime > TFC_Time.getTotalTicks() + 7000 + levelMod)
								{
									if(world.rand.nextInt(2) == 0)
										p.addPotionEffect(new PotionEffect(15, time, 4));

									if(fs.soberTime > TFC_Time.getTotalTicks() + 8000 + levelMod)
									{
										if(world.rand.nextInt(10) == 0)
											p.addPotionEffect(new PotionEffect(20, time, 4));
									}

									if(fs.soberTime > TFC_Time.getTotalTicks() + 10000 + levelMod)
									{
										fs.soberTime = 0;
										player.attackEntityFrom((new DamageSource("alcohol")).setDamageBypassesArmor().setDamageIsAbsolute(), player.getMaxHealth());
									}
								}
							}
						}
						TFC_Core.setPlayerFoodStats(player, fs);
					}
				}
				else if(sacFS.getFluid() == TFCFluids.MILK && fs.needFood())
				{
					if (fs.needDrink())
					{
						fs.restoreWater(p, rw);
						this.drain(sac, drinkAmount, true);
						fs.addNutrition(EnumFoodGroup.Dairy, 20);
						TFC_Core.setPlayerFoodStats(player, fs);
					}
				}

				if(!fs.needDrink())
				{
					world.playSoundAtEntity(p, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
					p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("drink.full")));
				}
			}
		}
		return sac;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		ItemTerra.addSizeInformation(is, arraylist);
		FluidStack fs = this.getFluid(is);
		if(fs != null)
		{
			arraylist.add(EnumChatFormatting.DARK_AQUA + fs.getLocalizedName());
			//arraylist.add(EnumChatFormatting.DARK_GRAY + "" + fs.amount + ":" + is.getItemDamage());
		}
	}

	@Override
	public EnumSize getSize(ItemStack is)
	{
		return EnumSize.SMALL;
	}

	@Override
	public EnumWeight getWeight(ItemStack is)
	{
		return EnumWeight.LIGHT;
	}

	@Override
	public boolean canStack()
	{
		return false;
	}

	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return EnumItemReach.SHORT;
	}

	////////////////////////////////////////////////////
	/*
	 * Private Methods
	 */
	////////////////////////////////////////////////////
	private void fillSac(World world, ItemStack sac, int x, int y, int z, int amount)
	{
		Block b = world.getBlock(x, y, z);
		if (isFreshWater(b) || isHotWater(b))
		{
			FluidStack fs = FluidRegistry.getFluidStack(TFCFluids.FRESHWATER.getName(), amount);
			this.fill(sac, fs, true);
		}

		if (isSaltWater(b))
		{
			FluidStack fs = FluidRegistry.getFluidStack(TFCFluids.SALTWATER.getName(), amount);
			this.fill(sac, fs, true);
		}
	}

	private boolean isValidFluid(FluidStack fs)
	{
		return fs.getFluid() == TFCFluids.BEER
				|| fs.getFluid() == TFCFluids.CIDER
				|| fs.getFluid() == TFCFluids.CORNWHISKEY
				|| fs.getFluid() == TFCFluids.FRESHWATER
				|| fs.getFluid() == TFCFluids.HOTWATER
				|| fs.getFluid() == TFCFluids.MILK
				|| fs.getFluid() == TFCFluids.RUM
				|| fs.getFluid() == TFCFluids.RYEWHISKEY
				|| fs.getFluid() == TFCFluids.SAKE
				|| fs.getFluid() == TFCFluids.SALTWATER
				|| fs.getFluid() == TFCFluids.VODKA
				|| fs.getFluid() == TFCFluids.WHISKEY;
	}

	private boolean isAlcohol(FluidStack fs)
	{
		return fs.getFluid() == TFCFluids.BEER
				|| fs.getFluid() == TFCFluids.CIDER
				|| fs.getFluid() == TFCFluids.CORNWHISKEY
				|| fs.getFluid() == TFCFluids.RUM
				|| fs.getFluid() == TFCFluids.RYEWHISKEY
				|| fs.getFluid() == TFCFluids.SAKE
				|| fs.getFluid() == TFCFluids.VODKA
				|| fs.getFluid() == TFCFluids.WHISKEY;
	}

	private boolean isFreshWater(Block block)
	{
		return block == TFCBlocks.freshWater || block == TFCBlocks.freshWaterStationary;
	}

	private boolean isHotWater(Block block)
	{
		return block == TFCBlocks.hotWater || block == TFCBlocks.hotWaterStationary;
	}

	private boolean isSaltWater(Block block)
	{
		return block == TFCBlocks.saltWater || block == TFCBlocks.saltWaterStationary;
	}

	private WPos findWater(World world, int x, int y, int z, int side)
	{
		WPos wp = new WPos();
		wp.setX(x);
		wp.setY(y);
		wp.setZ(z);
		wp.setWater(false);

		Block block = world.getBlock(wp.getX(), wp.getY(), wp.getZ());
		if(isFreshWater(block) || isHotWater(block) || isSaltWater(block))
		{
			wp.setWater(true);
			return wp;
		}
		else
		{
			switch (side)
			{
				case 1:
					wp.setY(wp.getY() + 1);
					break;
				case 2:
					wp.setZ(wp.getZ() - 1);
					break;
				case 3:
					wp.setZ(wp.getZ() + 1);
					break;
				case 4:
					wp.setX(wp.getX() - 1);
					break;
				case 5:
					wp.setX(wp.getX() + 1);
					break;
			}
			block = world.getBlock(wp.getX(), wp.getY(), wp.getZ());
			if(isFreshWater(block) || isHotWater(block) || isSaltWater(block))
			{
				wp.setWater(true);
				return wp;
			}
		}

		return wp;
	}

	////////////////////////////////////////////////////
	/*
	 * Fluid stuff
	 */
	////////////////////////////////////////////////////
	@Override
	public FluidStack getFluid(ItemStack container)
	{
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
			return null;

		return FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		return this.capacity;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		if (resource == null || !isValidFluid(resource))
			return 0;

		if (!doFill)
		{
			if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
				return Math.min(capacity, resource.amount);

			FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));

			if (stack == null)
				return Math.min(capacity, resource.amount);

			if (!stack.isFluidEqual(resource))
				return 0;

			return Math.min(capacity - stack.amount, resource.amount);
		}

		if (container.stackTagCompound == null)
			container.stackTagCompound = new NBTTagCompound();

		if (!container.stackTagCompound.hasKey("Fluid"))
		{
			NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

			if (capacity < resource.amount)
			{
				fluidTag.setInteger("Amount", capacity);
				container.stackTagCompound.setTag("Fluid", fluidTag);
				container.setItemDamage(0);
				return capacity;
			}

			container.stackTagCompound.setTag("Fluid", fluidTag);
			container.setItemDamage(capacity - resource.amount);
			return resource.amount;
		}

		NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
		FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

		if (!stack.isFluidEqual(resource))
			return 0;

		int filled = capacity - stack.amount;
		if (resource.amount < filled)
		{
			stack.amount += resource.amount;
			filled = resource.amount;
		}
		else
		{
			stack.amount = capacity;
		}

		container.stackTagCompound.setTag("Fluid", stack.writeToNBT(fluidTag));
		container.setItemDamage(capacity - stack.amount);
		return filled;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
			return null;

		FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
		if (stack == null)
			return null;

		int currentAmount = stack.amount;
		stack.amount = Math.min(stack.amount, maxDrain);
		if (doDrain)
		{
			if (currentAmount == stack.amount)
			{
				container.stackTagCompound.removeTag("Fluid");
				if (container.stackTagCompound.hasNoTags())
					container.stackTagCompound = null;
				container.setItemDamage(container.getMaxDamage());
				return stack;
			}

			NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
			fluidTag.setInteger("Amount", currentAmount - stack.amount);
			container.stackTagCompound.setTag("Fluid", fluidTag);
			container.setItemDamage(capacity - (currentAmount - stack.amount));
		}
		return stack;
	}
}
