package net.textstack.band_of_gigantism.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.item.MarkUnknown;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import net.textstack.band_of_gigantism.registry.ModBlocks;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;

import java.util.Objects;

//i agree, it IS a good handler name!
@Mod.EventBusSubscriber(modid = BandOfGigantism.MODID)
public class EventHandlerMyBallsInYourMouth {

    final BOGConfig c = BOGConfig.INSTANCE;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingHeal(LivingHealEvent event) {

        if (event.getEntityLiving() instanceof Player) {
            LivingEntity living = event.getEntityLiving();

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

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            LivingEntity living = event.getEntityLiving();
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

    @SubscribeEvent
    public void onLivingExperienceDrop(LivingExperienceDropEvent event) {

        if (event.getAttackingPlayer() != null) {
            int value = event.getDroppedExperience();

            if ((event.getEntityLiving().hasEffect(ModEffects.MIRA.get()) || CurioHelper.hasCurio(event.getAttackingPlayer(), ModBlocks.MIRAPOPPY.get().asItem())) && Math.random() < c.mirapoppy_chance.get()) {
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
        if (event.getEntityLiving() instanceof Player player) {
            LivingEntity living = event.getEntityLiving();

            //for the strains of ascent effect to deal damage as fast as it wants
            if (event.getSource() == MarkDamageSource.BOG_DESCENDED) {
                living.invulnerableTime = 0;
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

            if (CurioHelper.hasCurio(living, ModItems.BAND_CRUSTACEOUS.get()) && !CurioHelper.hasCurio(living, ModItems.MARK_FADED.get()) && player.getFoodData().getFoodLevel() >= 18 && event.getAmount() > 0) {
                living.addEffect(new MobEffectInstance(ModEffects.CRABBY.get(), c.band_crustaceous_duration.get(), 0, false, false));
            }
        }
    }

    @SubscribeEvent
    public void onCriticalHit(CriticalHitEvent event) {
        if (event.getEntityLiving() instanceof Player player) {

            //crit increase
            if (CurioHelper.hasCurio(player, ModItems.MARK_FORGOTTEN.get()) && event.isVanillaCritical()) {
                event.setDamageModifier(event.getDamageModifier() + c.mark_forgotten_critical_damage.get().floatValue());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onOverlayRenderPre(RenderGameOverlayEvent.PreLayer event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (event.getOverlay() == ForgeIngameGui.FOOD_LEVEL_ELEMENT) {

            //disable rendering of food
            if (CurioHelper.hasCurio(player, ModItems.MARK_FORGOTTEN.get())) {
                event.setCanceled(true);
            }
        } else if (event.getOverlay() == ForgeIngameGui.PLAYER_HEALTH_ELEMENT) {

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
            String message = event.getMessage();
            LivingEntity living = event.getPlayer();
            message = colorMark(living, message);

            //insert colored text if required
            Component newComponent;
            if (message != null) {
                newComponent = new TranslatableComponent("chat.type.text", event.getUsername(), message);
            } else {
                newComponent = event.getComponent();
            }

            event.setComponent(newComponent);
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
}
