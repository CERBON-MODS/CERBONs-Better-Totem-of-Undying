# Better Totem of Undying [FORGE]

[![Status](https://img.shields.io/badge/development%20status-Active-brightgreen?style=for-the-badge)]()
[![Discord](https://img.shields.io/discord/834250417130831903?color=blue&label=Discord&logo=discord&style=for-the-badge)](https://discord.gg/2PvYZfjWDf)
[![CurseForge](https://img.shields.io/static/v1?label=Curseforge&message=Forge&color=orange&labelColor=grey&style=for-the-badge&logo=curseforge&logoColor=orange)](https://www.curseforge.com/minecraft/mc-mods/better-totem-of-undying)
[![Modrinth](https://img.shields.io/static/v1?label=Modrinth&message=Forge&color=dark_green&labelColor=grey&style=for-the-badge&logo=Modrinth&logoColor=dark_green)]()

A highly configurable mod that improves Minecraft Totem of Undying by adding new features to it. You can blacklist dimensions or even structures where you don't want totem to work. The mod also has compatibility with Curios API.

## Features 

- Totem will knockback mobs when they are too close.
- Totem will save you when falling into the void, teleporting you safely to the surface.
- Totem will destroy blocks if you are suffocating inside them. (If there is a block you don't want to be destroyed, you can add it to the "totem_cant_break" tag).
- Totem will increase your hunger bar to 8 if it's <= 6 (When you can't run). Giving you enough energy to run from danger and/or find food.
- Totem will also apply Water Breathing effect. However, both fire resistance and water breathing effects will only be applied when needed. This means you are going to receive the effects only if you are on fire or in water, respectively.
- You can use the totem from charm slot if Curios mod is installed.
- You can use the totem from your inventory (Disabled by default).
- You can add a cooldown to the totem (Disabled by default).
- You can blacklist dimensions and structures where you don't want totem to work.

## Gifs

<details><summary>Click here (GIFS)</summary>
<p>

![Totem Knockback](https://github.com/CerbonXD/BetterTotemOfUndying/blob/master/.github/Totem%20Knockback.gif)

![Totem Teleport Out Of Void](https://github.com/CerbonXD/BetterTotemOfUndying/blob/master/.github/Totem%20Teleport%20Out%20Of%20Void.gif)

![Destroy Blocks When Suffocating](https://github.com/CerbonXD/BetterTotemOfUndying/blob/master/.github/Destroy%20Blocks%20When%20Suffocating.gif)

</p>
</details>

## Config File 

The config file is partitioned in four sections:

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

## Support

Please report all bugs, issues, and feature requests to the [issue tracker](https://github.com/CerbonXD/BetterTotemOfUndying/issues) or use my [discord server](https://discord.gg/2PvYZfjWDf) and report at the appropriate channel.

Please read the [FAQ](https://github.com/CerbonXD/BetterTotemOfUndying/blob/master/FAQ.md) before reporting a bug, issue or feature request. 

## Donations 

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/E1E8MI65L)

## Affiliates

[![BisectHosting](https://www.bisecthosting.com/partners/custom-banners/1a9a55fc-f1c0-4b40-b07c-3774bc557f93.webp)](https://bisecthosting.com/Cerbon)
