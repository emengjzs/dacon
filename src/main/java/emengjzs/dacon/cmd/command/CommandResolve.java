package emengjzs.dacon.cmd.command;

import org.apache.commons.cli.Options;

public interface CommandResolve {

    CommandMapper.MethodMapper getMappedMethod(String name);

    Options getCommands();
}
