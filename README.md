# <img src="https://github.com/CerbonXD/BetterTotemOfUndying/blob/master/src/main/resources/logo.png" height="30"> Better Totem of Undying

A highly configurable mod that improves Minecraft Totem of Undying by adding new features to it. You can blacklist dimensions or even structures where you don't want totem to work. The mod also has compatibility with Curios API.

### New Features 

- Knockback mobs away.
- Save entity from dying in the void.
- Has option to use totem from inventory (Disabled by default).
- If Curios mod is installed you can use totem from charm slot.
- Has option to add cooldown to the totem (Disabled by default).
- If you are suffocating totem will break the blocks you are in (You can add blocks to the tag "totem_cant_break" if you want some block to not be break by the totem).
- You can blacklist dimensions where you don't want totem to work.
- You can blacklist structures where you don't want totem to work.
- Increases your food level to 8 if your food level is <= 6 (When you can't run). Note that the hunger bar ranges from 0 to 20.
- Apply Water Breathing effect.
- Apply fire resistance and water breathing effects only when needed, meaning you are going to receive the effects only if you are on fire or in water respectively.

### Config File 

The config file is divided in 4 parts:

- **Minecraft Default Totem Features:** Here is where you can configure things Minecraft does by default with totem, such as giving you regeneration and absorption effects.
- **New Totem Features:** Here is where you can configure all new totem features added by the mod.
- **Curios:** Here is where you can configure things related to curios integration.
- **Blacklists:** Here is where you can blacklist dimensions or structures.

You can download [Configured](https://www.curseforge.com/minecraft/mc-mods/configured) for easier config management in game.

<details><summary>Click here (Config)</summary>
<p>

```toml

["Minecraft Default Totem Features"]
	#This value sets the health Totem of Undying will give to the entity upon use. DEFAULT: 1
	#Range: 0 ~ 20
	"Set Health" = 1
	#When Totem of Undying is used it removes all previous effects you had. If set to false, it will keep all the effects you had before using the totem. DEFAULT: TRUE
	"Remove All Effects" = true

	["Minecraft Default Totem Features".Effects]
		#If false Totem of Undying will not give you fire resistance effect. DEFAULT: TRUE
		"Enable Fire Resistance" = true
		#Sets the duration of the fire resistance effect in ticks. DEFAULT: 800
		"Fire Resistance Duration" = 800
		#If false Totem of Undying will not give you regeneration effect. DEFAULT: TRUE
		"Enable Regeneration" = true
		#Sets the duration of the regeneration effect in ticks. DEFAULT: 900
		"Regeneration Duration" = 900
		#Sets the amplifier of the regeneration effect. DEFAULT: 1
		#Range: 0 ~ 255
		"Regeneration Amplifier" = 1
		#If false Totem of Undying will not give you absorption effect. DEFAULT: TRUE
		"Enable Absorption" = true
		#Sets the duration of the absorption effect in ticks. DEFAULT: 100
		"Absorption Duration" = 100
		#Sets the amplifier of the absorption effect. DEFAULT: 1
		#Range: 0 ~ 255
		"Absorption Amplifier" = 1

["New Totem Features"]
	#If true you will be able to use the Totem of Undying from your inventory. DEFAULT: FALSE
	"Use Totem From Inventory" = false

	["New Totem Features".Effects]
		#Apply fire resistance and water breathing effects only when needed, meaning you are going to receive the effects only if you are on fire or in water respectively. DEFAULT: TRUE
		"Apply Effects Only When Needed" = true
		#If false Totem of Undying will not give you water breathing effect. DEFAULT: TRUE
		"Enable Water Breathing" = true
		#Sets the duration of the water breathing effect in ticks. DEFAULT: 800
		"Water Breathing Duration" = 800
		#If false Totem of Undying will not give you slow falling effect. DEFAULT: TRUE
		"Enable Slow Falling" = true
		#Sets the duration of the slow falling effect in ticks. DEFAULT: 600
		"Slow Falling Duration" = 600

	["New Totem Features"."Increase Food Level"]
		#If false Totem of Undying will not increase your food level. DEFAULT: TRUE
		"Increase Food Level" = true
		#Sets the minimum food level needed to Totem of Undying increase food level. DEFAULT: <= 6
		#Range: 0 ~ 20
		"Minimum Food Level" = 6
		#Sets the food level that Totem of Undying will give upon use. DEFAULT: 8
		#Range: 0 ~ 20
		"Set Food Level" = 8

	["New Totem Features"."Destroy Blocks When Suffocating or Fully Frozen"]
		#If false Totem of Undying will not break blocks when you are suffocating. DEFAULT: TRUE
		"Destroy Blocks When Suffocating" = true
		#If false Totem of Undying will not break the powder snow when you die fully frozen. DEFAULT: TRUE
		"Destroy Powder Snow When Fully Frozen" = true

	["New Totem Features"."Knockback Mobs Away"]
		#If false Totem of Undying will not knockback mobs away. DEFAULT: TRUE
		"Knockback Mobs Away" = true
		#Sets the radius where entities needs to be for the totem knockback them. DEFAULT: 3.0
		"Knockback Radius" = 3.0
		#Sets the strength of the knockback. DEFAULT: 2.5
		"Knockback Strength" = 2.5

	["New Totem Features"."Teleport Out of Void"]
		#If false Totem of Undying will not save you from dying in the void. DEFAULT: TRUE
		"Teleport Out of Void" = true
		#If totem can't find a available position to teleport you back it will teleport you to the world's max build height plus this offset. DEFAULT:64
		#Range: 0 ~ 1024
		"Teleport Height Offset" = 64

	["New Totem Features"."Add Cooldown"]
		#If true Totem of Undying will receive a cooldown after being used and you will not be able to use it again during this period. DEFAULT: FALSE
		"Add Cooldown" = false
		#Sets the cooldown duration in ticks. DEFAULT: 200
		Cooldown = 200

[Curios]
	#If false you will not be able to use Totem of Undying from charm slot (Curios mod must be installed). DEFAULT: TRUE
	"Use Totem From Charm Slot" = true
	#If false Totem of Undying will not be displayed on the chest when worn in the curios charm slot (Curios mod must be installed). DEFAULT: TRUE
	"Display Totem on Chest" = true

[Blacklists]
	#You can put here dimensions where you don't want Totem of Undying to work. Example: "minecraft:overworld", "mod_id:dimension_id" DEFAULT: Nothing
	"Blacklisted Dimensions" = []
	#You can put here structures where you don't want Totem of Undying to work. Example: "minecraft:desert_pyramid", "mod_id:structure_id" DEFAULT: Nothing
	"Blacklisted Structures" = []
```

</p>
</details>

## Gifs

<details><summary>Click here (GIFS)</summary>
<p>

![Totem Knockback](https://github.com/CerbonXD/BetterTotemOfUndying/assets/84386993/81add95c-88e0-4c45-bd9a-ca56c2970818)

![Totem Teleport Out Of Void](https://github.com/CerbonXD/BetterTotemOfUndying/assets/84386993/cd8fc3d7-3e0b-4499-9c51-04d3aae38462)

![Destroy Blocks When Suffocating](https://github.com/CerbonXD/BetterTotemOfUndying/assets/84386993/e8a60208-5dc8-418d-89ec-082d24d39a13)

</p>
</details>


## Download

- Curseforge Link
- Modrinth Link

## Support

Please report all bugs, issues, and feature requests to the [issue tracker](https://github.com/CerbonXD/BetterTotemOfUndying/issues) or use my [discord server](https://discord.gg/2PvYZfjWDf) and report at the appropriate channel.

Please read the [FAQ](https://github.com/CerbonXD/BetterTotemOfUndying/blob/master/FAQ.md) before reporting a bug, issue or feature request.

## Affiliates

[![BisectHosting](https://www.bisecthosting.com/partners/custom-banners/1a9a55fc-f1c0-4b40-b07c-3774bc557f93.webp)](https://bisecthosting.com/Cerbon)
