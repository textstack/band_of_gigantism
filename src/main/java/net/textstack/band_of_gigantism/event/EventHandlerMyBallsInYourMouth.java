package net.textstack.band_of_gigantism.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.item.MarkUnknown;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import net.textstack.band_of_gigantism.util.CurioHelper;

import java.util.Objects;

//i agree, it IS a good handler name!
@Mod.EventBusSubscriber(modid = BandOfGigantism.MODID)
public class EventHandlerMyBallsInYourMouth {

    BOGConfig c = BOGConfig.INSTANCE;

    /*private static final float mark_descended_regeneration = c.mark_descended_regeneration.get().floatValue();
    private static final float mark_faded_healing = c.mark_faded_healing.get().floatValue();
    private static final float mark_faded_flat_resistance = c.mark_faded_flat_resistance.get().floatValue();
    private static final int mark_forgotten_duration = c.mark_forgotten_duration.get();
    private static final float mark_forgotten_critical_damage = c.mark_forgotten_critical_damage.get().floatValue();
    private static final float mark_forgotten_resistance = c.mark_forgotten_resistance.get().floatValue();
    private static final float mark_unknown_healing = c.mark_unknown_healing.get().floatValue();
    private static final float mark_unknown_flat_resistance = c.mark_unknown_flat_resistance.get().floatValue();
    private static final boolean marks_color_chat = c.marks_color_chat.get();*/

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingHeal(LivingHealEvent event) {

        if (event.getEntityLiving() instanceof PlayerEntity) {
            LivingEntity living = event.getEntityLiving();

            //disables regen and reduces healing
            if (CurioHelper.hasCurio(living, ModItems.MARK_FADED.get())||Objects.requireNonNull(living).isPotionActive(ModEffects.RECOVERING.get())) {
                if (event.getAmount() <= 1.0f) {
                    event.setCanceled(true);
                    return;
                } else {
                    event.setAmount(event.getAmount() * (1+c.mark_faded_healing.get().floatValue()));
                }
            }

            //increases regen
            if (CurioHelper.hasCurio(living, ModItems.MARK_DESCENDED.get())) {
                if (event.getAmount() <= 1.0f) {
                    event.setAmount(event.getAmount() * (1+c.mark_descended_regeneration.get().floatValue()));
                }
            }

            //either increases or reduces healing
            if (CurioHelper.hasCurio(living, ModItems.MARK_UNKNOWN.get())) {
                int regenValue = MarkUnknown.regenValue(CurioHelper.hasCurioGet(living, ModItems.MARK_UNKNOWN.get()));
                switch (regenValue) {
                    case 5:
                    case 6:
                    case 1: event.setAmount(event.getAmount() * (1-c.mark_unknown_healing.get().floatValue()));
                            break;
                    case 7:
                    case 8:
                    case 2: event.setAmount(event.getAmount() * (1+c.mark_unknown_healing.get().floatValue()));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            LivingEntity living = event.getEntityLiving();

            //for the strains of ascent effect to deal damage as fast as it wants
            if (event.getSource() == MarkDamageSource.BOG_DESCENDED) {
                living.hurtResistantTime = 0;
            }

            //flat damage resistance/vulnerability
            if (CurioHelper.hasCurio(living, ModItems.MARK_UNKNOWN.get())) {
                int regenValue = MarkUnknown.regenValue(CurioHelper.hasCurioGet(living, ModItems.MARK_UNKNOWN.get()));
                switch (regenValue) {
                    case 5:
                    case 7:
                    case 3: event.setAmount(event.getAmount()+c.mark_unknown_flat_resistance.get().floatValue());
                            break;
                    case 6:
                    case 8:
                    case 4:
                        float damageReduce = Math.max(event.getAmount() - c.mark_unknown_flat_resistance.get().floatValue(),0.0f);
                        if (damageReduce<=0) {
                            event.setCanceled(true);
                            return;
                        } else {
                            event.setAmount(damageReduce);
                        }
                }
            }

            //flat damage resistance
            if (CurioHelper.hasCurio(living, ModItems.MARK_FADED.get())) {
                float damageReduce = Math.max(event.getAmount() - c.mark_faded_flat_resistance.get().floatValue(),0.0f);
                if (damageReduce<=0) {
                    event.setCanceled(true);
                    return;
                } else {
                    event.setAmount(damageReduce);
                }
            }

            //normal (ew) resistance
            if (CurioHelper.hasCurio(living, ModItems.MARK_FORGOTTEN.get())) {
                event.setAmount(event.getAmount() * (1+c.mark_forgotten_resistance.get().floatValue()));
                living.addPotionEffect(new EffectInstance(ModEffects.FORGETFULNESS.get(),c.mark_forgotten_duration.get(),0,false,false));
            }
        }
    }

    //i think these do stuff?
    @SubscribeEvent
    public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            if (Float.isNaN(player.getHealth())) {
                player.setHealth(0F);
            }

            if (Float.isNaN(player.getAbsorptionAmount())) {
                player.setAbsorptionAmount(0F);
            }
        }
    }

    @SubscribeEvent
    public void onCriticalHit(CriticalHitEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            //crit increase
            if (CurioHelper.hasCurio(player, ModItems.MARK_FORGOTTEN.get())&&event.isVanillaCritical()) {
                event.setDamageModifier(event.getDamageModifier()+c.mark_forgotten_critical_damage.get().floatValue());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onOverlayRenderPre(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {

            //disable rendering of food
            if (CurioHelper.hasCurio(player, ModItems.MARK_FORGOTTEN.get())) {
                event.setCanceled(true);
            }
        } else if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {

            //disable rendering of health
            if (Objects.requireNonNull(player).isPotionActive(ModEffects.FORGETFULNESS.get())) {
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
            ITextComponent newComponent;
            if (message != null) {
                newComponent = new TranslationTextComponent("chat.type.text", event.getUsername(), message);
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
            newMessage = "\u00A74"+message+"\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_FADED.get())) {
            newMessage = "\u00A78"+message+"\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_JUDGED.get())) {
            newMessage = "\u00A7c"+message+"\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_DESCENDED.get())) {
            newMessage = "\u00A79"+message+"\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_UNKNOWN.get())) {
            newMessage = "\u00A7a"+message+"\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_FORGOTTEN.get())) {
            newMessage = "\u00A76"+message+"\u00A7r";
        } else if (CurioHelper.hasCurio(living, ModItems.MARK_PURIFIED.get())) {
            newMessage = "\u00A77"+message+"\u00A7r";
        }
        return newMessage;
    }
}