**Q: Can I use this mod in my mod pack?**

A: Yes, you can.

**Q: Are you going to backport this mod to older versions?**

A: All my mods that are currently using this multi loader template won't be backport to versions below 1.20.1.

**Q: What loaders do you support?**

A: Forge, Fabric and NeoForge.

**Q: I have blacklisted a dimension/structure and it's not working. Why?**

A: Probably you have written the dimension/structure ID incorrectly. Please double-check if you have entered the correct mod ID and dimension/structure ID. Another possibility is that you haven't saved the file.

**Q: Is it possible to blacklist damage types?**

A: Yes, it is. You can do it by adding the damage types you want to the `bypasses_invulnerability` tag using a [datapack](https://minecraft.fandom.com/wiki/Tutorials/Creating_a_data_pack). It's located at `minecraft\tags\damage_type\bypasses_invulnerability.json`.

**Q: How do I add blocks to the "totem_cant_break" tag?**

A: You need to [create a datapack](https://minecraft.fandom.com/wiki/Tutorials/Creating_a_data_pack) and use the following path: `data/better_totem_of_undying/tags/blocks` and create at this location the file `totem_cant_break.json`. Then add to the json file the blocks you want.

**Q: How do I add custom effects?**

A: Custom effects cannot be configured through the GUI. Instead, you will need to open the config file directly. In this file, you will find a configuration called `custom_effects`. Use this list to add custom effects and specify their triggers. Within this list, provide another list enclosed in curly braces. The first parameter should be the damage type, the second parameter the effect to be granted, the third parameter the duration of the effect in ticks, and the fourth parameter the effect amplifier. If you want the effect to be granted regardless of the damage type, use ‘any’. For example: [["minecraft:out_of_world", "minecraft:blindness", "800", "0"]]. In this example, if the player dies in the void, the totem will grant a level 1 blindness effect with a duration of 40 seconds.
