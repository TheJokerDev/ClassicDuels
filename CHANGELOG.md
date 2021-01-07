### 1.0.3 Release (07.01.2021)
* Added multi-world support.
* Added new lore messages to cuboid selector item.
* Added new value to scoreboard contents which shows opponent's direction with arrows.
* Removed default false booleans.
* Fixed Hex color codes for debugger in Minecraft 1.16+.
* Fixed default join permission.
* Changed default leave item to red bed from white bed.
* Changed PAPI's plugin version with Bukkit's.
* Changed some debugger keys.
* Now list command replaces values faster.
* Disabled locales in debug mode.
* Updated license header.

** Also contains more news from 1.0.2's hotfix releases.**

### 1.0.2 Release (14.11.2020 - 18.11.2020)
* Added license header.
* Added area selection to clear arena after game.
* Added layout editor for players to save their kits.
* Added online player completion to tab complete for stats command.
* Added new option to disable animated countdown for player's level bar.
* Added warning message if you place a game sign another world from default.
* Added chat prefix to some join messages (not in-game).
* Fixed `Error creating shaded jar: null` error in Java 11 - (Developer Alert)
* Fixed attack cooldown is not resetting on pure reload.
* Fixed can not registering events in Minecraft 1.9.x.
* Fixed getting NPE when player die from fire.
* Fixed timer message issues.
* Fixed some debug prefixes.
* Fixed some JavaDoc issues.
* Fixed rewards getting NPE.
* Fixed player being invisible after game.
* Fixed end location saving.
* Fixed countdown starting from 4 while it should be start from 5.
* Fixed NPE during giving rewards to players.
* Fixed ClassCastException when a skeleton shoot a arrow to a player.
* Removed default false values for config options.
* Replaced some attribute methods with API methods.
* Moved project into Java 11.
* Moved script engine to switch-case.
* Some changes on default kit design.
* Some changes on kit system to add custom ones however it's not implemented yet.
* Changed default gameplay time to 540 seconds from 900.
* Changed package of a class - (Developer Alert)
   * me.despical.classicduels.commands.admin.arena.CreateCommand.java
* Now fireworks will stop spawning on restarting stage.
* Now sending arena creation message centered.
* Performance improvements.

### 1.0.1 Release (02.11.2020)
* Added new language system.
* Added compatibility to Essentials and Multiverse-core.
* Fixed player being invisible after game finishes.
* Fixed NPE when signs aren't located in default world.
* Optimized events.
