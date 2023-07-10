# Gotta Go Fast

This simple mod brings bug-fixes to issues like "Player moved too quickly!" or "Vehicle of Player moved too quickly!" to a standalone mod.
It allows to modify the threshold the server side checks for if a player moves too far too quickly which then teleports them back. ***Allows clients to send fast update packets. Beware.***

The Name and logo is from the [original standalone bug-fix by Thiakil](https://www.curseforge.com/minecraft/mc-mods/gotta-go-fast) for Forge 1.12. 
This mod is server-side only and does not have to be installed on the client. 

## Note

Modrinth is now the only source of truth for this mod. The project will no longer be updated on Curseforge.

## Warning

**From v1.1.0 onwards, this mod does nothing by default. The default config values are the vanilla values and as follows:**
```
{ 
    "defaultMaxPlayerSpeed": 100.0, 
    "maxPlayerElytraSpeed": 300.0, 
    "maxPlayerVehicleSpeed": 100.0 
}
```
Try using values as low as possible, since values too high might allow players to use speedhacks. Increase the values slowly until you don't experience any lag-back anymore.

## Config

```
{ 
    "defaultMaxPlayerSpeed": 100000.0, 
    "maxPlayerElytraSpeed": 100000.0, 
    "maxPlayerVehicleSpeed": 100000.0 
}
```

Forge alternative: https://www.curseforge.com/minecraft/mc-mods/too-fast

## Special thanks

Thanks [RandomPatches](https://www.curseforge.com/minecraft/mc-mods/randompatches-fabric) for the original idea and concept.

Thanks to everybody who reports bugs and submits feedback on the github issue tracker.
