# Bird Hat

An animated songbird that sits on your head. Client-side Fabric cosmetic for
Minecraft **1.21.11** and **26.1.2**. It bobs, looks around, flicks its tail,
hops and flutters every few seconds, flaps when you run, and ducks when you sneak.
Variants: bluebird, cardinal, canary, robin.

## Getting the jar
GitHub builds it automatically on every push: open the **Actions** tab, click the
latest "Build Bird Hat" run, and download `birdhat-1.21.11` or `birdhat-26.1.2`
under **Artifacts**. Unzip it to get the mod jar.

## Installing (Prism Launcher)
1. Create an instance for your version with the **Fabric** loader.
2. Edit instance > Mods > Download mods > add **Fabric API**.
3. Mods > Add file > pick the Bird Hat jar. Launch and press F5.

## Config
`config/birdhat.properties` in the instance folder (created on first launch):

    enabled=true
    variant=cardinal          # bluebird | cardinal | canary | robin
    show_on_everyone=false    # true = every player gets a bird
    scale=1.0
    height_offset_pixels=0    # try 1 if it sinks into helmets
