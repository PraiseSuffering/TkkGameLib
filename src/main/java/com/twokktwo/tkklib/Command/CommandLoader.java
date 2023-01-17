package com.twokktwo.tkklib.Command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandLoader {
    public CommandLoader(FMLServerStartingEvent event){
        event.registerServerCommand(new cvCommand());
        event.registerServerCommand(new debugCommand());
        event.registerServerCommand(new mirrorCommand());
        event.registerServerCommand(new moveCommand());
        event.registerServerCommand(new moveEntityCommand());
        event.registerServerCommand(new templateCommand());
        event.registerServerCommand(new GeneratingChunkCommand());
        event.registerServerCommand(new isOPCommand());
        event.registerServerCommand(new tkkJSCommand());
        event.registerServerCommand(new tkkJSRegEventCommand());
        event.registerServerCommand(new tkkJSUnregEventCommand());
        event.registerServerCommand(new tkkJSRunFileCommand());
    }
}
