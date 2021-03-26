package org.owpk;

import io.micronaut.configuration.picocli.PicocliRunner;
import org.owpk.commands.AuthCommand;
import org.owpk.commands.WalletsCommand;
import picocli.CommandLine.Command;

@Command(name = "hive-cli", description = "...",
        mixinStandardHelpOptions = true,
        subcommands = {WalletsCommand.class, AuthCommand.class})
public class HiveCliCommand implements Runnable {

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(HiveCliCommand.class, args);
    }

    public void run() {
        System.out.println("Hi!");
    }
}
