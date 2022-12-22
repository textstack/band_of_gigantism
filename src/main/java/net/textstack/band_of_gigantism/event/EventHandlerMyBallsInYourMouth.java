package net.textstack.band_of_gigantism.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.item.MarkUnknown;
import net.textstack.band_of_gigantism.registry.ModDamageSources;
import net.textstack.band_of_gigantism.registry.*;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.util.LoreStatHelper;

import java.util.List;
import java.util.Objects;

//i agree, it IS a good handler name!
@Mod.EventBusSubscriber(modid = BandOfGigantism.MODID)
public class EventHandlerMyBallsInYourMouth {

    final BOGConfig c = BOGConfig.INSTANCE;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingHeal(LivingHealEvent event) {

        if (event.getEntity() instanceof Player) {
            LivingEntity living = event.getEntity();

            //disables regen and reduces healing
            if (CurioHelper.hasCurio(living, ModItems.MARK_FADED.get()) || Objects.requireNonNull(living).hasEffect(ModEffects.RECOVERING.get())) {
                if (event.getAmount() <= 1.0f) {
                    event.setCanceled(true);
                    return;
                } else {
                    event.setAmount(event.getAmount() * (1 + c.mark_faded_healing.get().floatValue()));
                }
            }

            //increases regen
            if (CurioHelper.hasCurio(living, ModItems.MARK_DESCENDED.get())) {
                if (event.getAmount() <= 1.0f) {
                    event.setAmount(event.getAmount() * (1 + c.mark_descended_regeneration.get().floatValue()));
                }
            }

            //either increases or reduces healing
            if (CurioHelper.hasCurio(living, ModItems.MARK_UNKNOWN.get())) {
                int regenValue = MarkUnknown.regenValue(CurioHelper.hasCurioGet(living, ModItems.MARK_UNKNOWN.get()));
                switch (regenValue) {
                    case 5, 6, 1 -> event.setAmount(event.getAmount() * (1 - c.mark_unknown_healing.get().floatValue()));
                    case 7, 8, 2 -> event.setAmount(event.getAmount() * (1 + c.mark_unknown_healing.get().floatValue()));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        CompoundTag tag = stack.getTag();
        if (tag == null) return;
        List<Component> tooltip = event.getToolTip();

        if (tag.getBoolean("Strange")) {
            int strangeKills = tag.getInt("strangeKills");
            tooltip.add(Component.translatable(LoreStatHelper.displayStrangeName(strangeKills,LoreStatHelper.StrangeType.TOOLTIP))
                    .append(stack.getHoverName().copy().withStyle(ChatFormatting.DARK_GRAY))
                    .append(Component.translatable("tooltip.band_of_gigantism.tooltip_strange_generic", "\u00A78" + strangeKills)));
        }
    }

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            LivingEntity living = event.getEntity();
            if (CurioHelper.hasCurio(living, ModItems.FALSE_HAND.get())) {
                ItemStack stack = CurioHelper.hasCurioGet(living, ModItems.FALSE_HAND.get());
                int flipped = stack.getOrCreateTag().getInt("flipped");
                if (flipped == 0) {
                    event.setCanceled(true);
                    living.setHealth(living.getMaxHealth() / 2.0f);
                    stack.getOrCreateTag().putInt("flipped", 1);
                    living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1, false, false));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public void onReallyDeadEvent(LivingDeathEvent event) {
        if (!event.isCanceled()) {
            if (event.getSource().getEntity() instanceof Player player) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.getBoolean("Strange")) {
                    int strangeKills = tag.getInt("strangeKills");
                    if (strangeKills <= 0) {
                        tag.putInt("strangeKills", 1);
                    } else {
                        tag.putInt("strangeKills", strangeKills + 1);
                    }
                }
            }
            if (event.getSource() == ModDamageSources.BOG_MIRA) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    CurioHelper.hasCurioGet(player, ModItems.BAND_PASSION.get()).shrink(1);
                    CurioHelper.hasCurioGet(player, ModItems.BAND_APATHY.get()).shrink(1);

                    BlockPos blockpos = player.blockPosition();
                    RandomSource random = player.getRandom();
                    ServerLevel server = player.getLevel();
                    int count = 0;

                    restartMira:
                    for(int i = 0; i < 128; ++i) {
                        BlockPos blockpos1 = blockpos;
                        
                        for(int j = 0; j < i / 16; ++j) {
                            blockpos1 = blockpos1.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                            if (server.getBlockState(blockpos1).isCollisionShapeFullBlock(server, blockpos1)) {
                                continue restartMira;
                            }
                        }

                        BlockState blockstate1 = server.getBlockState(blockpos1);

                        if (blockstate1.isAir()) {
                            Holder<PlacedFeature> holder = ModPlacements.MIRA_PLACE;
                            holder.value().place(server, server.getChunkSource().getGenerator(), random, blockpos1);
                            count = count + 1;

                            if (count >= 6) {break;}
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingExperienceDrop(LivingExperienceDropEvent event) {

        if (event.getAttackingPlayer() != null) {
            int value = event.getDroppedExperience();

            if ((event.getEntity().hasEffect(ModEffects.MIRA.get()) || CurioHelper.hasCurio(event.getAttackingPlayer(), ModBlocks.MIRAPOPPY.get().asItem())) && Math.random() < c.mirapoppy_chance.get()) {
                value = value * 2;
                if (Math.random() < c.mirapoppy_chance_double.get()) {
                    value = value * 2;
                }
            }

            event.setDroppedExperience(value);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player living) {

            if (c.recovery_allhits.get()) {
                if (event.getSource() != ModDamageSources.BOG_DESCENDED &&
                        event.getSource() != ModDamageSources.BOG_FADED &&
                        event.getSource() != ModDamageSources.BOG_FORGOTTEN &&
                        event.getSource() != ModDamageSources.BOG_JUDGED &&
                        event.getSource() != ModDamageSources.BOG_OBLITERATED &&
                        event.getSource() != ModDamageSources.BOG_PURIFIED &&
                        event.getSource() != ModDamageSources.BOG_UNKNOWN)
                if ((event.getAmount() > c.recovery_minimum_damage.get()) && (Math.random() < c.recovery_chance.get())) {
                    living.addEffect(new MobEffectInstance(ModEffects.RECOVERING.get(), c.recovery_duration.get(), 0, false, c.recovery_show_particles.get()));
                }
            }

            //for the strains of ascent effect to deal damage as fast as it wants
            if (event.getSource() == ModDamageSources.BOG_DESCENDED) {
                living.invulnerableTime = 0;
            }

            if (living.hasEffect(ModEffects.MIRA.get())&&Math.random() < c.mira_gin_chance.get()&&event.getAmount()<living.getMaxHealth()*2) {
                event.setCanceled(true);
                return;
            }

            //flat damage vuln for false hand
            if (CurioHelper.hasCurio(living, ModItems.FALSE_HAND.get())) {
                int flipped = CurioHelper.hasCurioGet(living, ModItems.FALSE_HAND.get()).getOrCreateTag().getInt("flipped");
                if (flipped == 1) {
                    float damageReduce = Math.max(event.getAmount() - c.false_hand_flat_resistance.get().floatValue(), 0.0f);
                    if (damageReduce <= 0) {
                        event.setCanceled(true);
                        return;
                    } else {
                        event.setAmount(damageReduce);
                    }
                }
            }

            //flat damage resistance/vulnerability
            if (CurioHelper.hasCurio(living, ModItems.MARK_UNKNOWN.get())) {
                int regenValue = MarkUnknown.regenValue(CurioHelper.hasCurioGet(living, ModItems.MARK_UNKNOWN.get()));
                switch (regenValue) {
                    case 5, 7, 3 -> event.setAmount(event.getAmount() + c.mark_unknown_flat_resistance.get().floatValue());
                    case 6, 8, 4 -> {
                        float damageReduce = Math.max(event.getAmount() - c.mark_unknown_flat_resistance.get().floatValue(), 0.0f);
                        if (damageReduce <= 0) {
                            event.setCanceled(true);
                            return;
                        } else {
                            event.setAmount(damageReduce);
                        }
                    }
                }
            }

            //flat damage resistance
            if (CurioHelper.hasCurio(living, ModItems.MARK_FADED.get())) {
                float damageReduce = Math.max(event.getAmount() - c.mark_faded_flat_resistance.get().floatValue(), 0.0f);
                if (damageReduce <= 0) {
                    event.setCanceled(true);
                    return;
                } else {
                    event.setAmount(damageReduce);
                }
            }

            //normal (ew) resistance
            if (CurioHelper.hasCurio(living, ModItems.MARK_FORGOTTEN.get())) {
                event.setAmount(event.getAmount() * (1 + c.mark_forgotten_resistance.get().floatValue()));
                living.addEffect(new MobEffectInstance(ModEffects.FORGETFULNESS.get(), c.mark_forgotten_duration.get(), 0, false, false));
            }

            if (CurioHelper.hasCurio(living, ModItems.BAND_CRUSTACEOUS.get()) && !CurioHelper.hasCurio(living, ModItems.MARK_FADED.get()) && living.getFoodData().getFoodLevel() >= 18 && event.getAmount() > 0) {
                living.addEffect(new MobEffectInstance(ModEffects.CRABBY.get(), c.band_crustaceous_duration.get(), 0, false, false));
            }
        }
    }

    @SubscribeEvent
    public void onCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        //crit increase
        if (CurioHelper.hasCurio(player, ModItems.MARK_FORGOTTEN.get()) && event.isVanillaCritical()) {
            event.setDamageModifier(event.getDamageModifier() + c.mark_forgotten_critical_damage.get().floatValue());
        }
    }

    static final ResourceLocation FOOD_LEVEL_ELEMENT = new ResourceLocation("minecraft", "food_level");
    static final ResourceLocation PLAYER_HEALTH_ELEMENT = new ResourceLocation("minecraft", "player_health");

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onOverlayRenderPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (event.getOverlay() == GuiOverlayManager.findOverlay(FOOD_LEVEL_ELEMENT)) {

            //disable rendering of food
            if (CurioHelper.hasCurio(player, ModItems.MARK_FORGOTTEN.get())) {
                event.setCanceled(true);
            }
        } else if (event.getOverlay() == GuiOverlayManager.findOverlay(PLAYER_HEALTH_ELEMENT)) {

            //disable rendering of health
            if (Objects.requireNonNull(player).hasEffect(ModEffects.FORGETFULNESS.get())) {
                event.setCanceled(true);
                //return;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void serverChat(ServerChatEvent event) {

        if (c.marks_color_chat.get()) {
            //append §[val] to the beginning of messages for marked players
            String message = event.getRawText();
            LivingEntity living = event.getPlayer();
            message = colorMark(living, message);

            //insert colored text if required
            Component newComponent;
            if (message != null) {
                newComponent = Component.literal(message);
            } else {
                newComponent = event.getMessage();
            }

            event.setMessage(newComponent);
        }
    }

    //applies color to a message based on their mark
    private String colorMark(LivingEntity living, String message) {
        String newMessage = null;
        if (CurioHelper.hasCurio(living, ModItems.MARK_OBLITERATED.get())) { //sorted in order of (in my opinion) difficulty
            newMessage = "\u00A74" + message + "\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_FADED.get())) {
            newMessage = "\u00A78" + message + "\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_JUDGED.get())) {
            newMessage = "\u00A7c" + message + "\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_DESCENDED.get())) {
            newMessage = "\u00A79" + message + "\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_UNKNOWN.get())) {
            newMessage = "\u00A7a" + message + "\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_FORGOTTEN.get())) {
            newMessage = "\u00A76" + message + "\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_PURIFIED.get())) {
            newMessage = "\u00A77" + message + "\u00A7r";
        }
        return newMessage;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogRender(ViewportEvent.RenderFog event) {
        Player player = Objects.requireNonNull(Minecraft.getInstance().player);
        if (player.hasEffect(ModEffects.MIRA_SICKNESS.get())) {
            float distanceMod = Objects.requireNonNull(player.getEffect(ModEffects.MIRA_SICKNESS.get())).getDuration()*2+0.8f;
            RenderSystem.setShaderFogStart(0.0F);
            RenderSystem.setShaderFogEnd(distanceMod);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (Objects.requireNonNull(Minecraft.getInstance().player).hasEffect(ModEffects.MIRA_SICKNESS.get())) {
            event.setRed(200);
            event.setGreen(100);
            event.setBlue(175);
        }
    }
}
