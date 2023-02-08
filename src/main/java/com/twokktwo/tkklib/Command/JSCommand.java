package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.js.JsContainer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class JSCommand implements ICommand {
    public JsContainer jsContainer;

    public JSCommand(JsContainer js){
        jsContainer=js;
    }

    @Override
    public String getName() {
        return (String) jsContainer.run("getName");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return (String) jsContainer.run("getUsage");
    }

    @Override
    public List<String> getAliases() {
        return (List<String>) jsContainer.run("getAliases");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        jsContainer.run("execute",server,sender,args);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return (List<String>) jsContainer.run("getTabCompletions",server,sender,args,targetPos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return (boolean) jsContainer.run("isUsernameIndex",args,index);
    }

    @Override
    public int compareTo(ICommand o) {
        return this.getName().compareTo(o.getName());
    }
    public int getRequiredPermissionLevel()
    {
        return (int)jsContainer.run("getRequiredPermissionLevel");
    }
}
