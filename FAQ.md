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
