package dev.forward.casino.modules;

import dev.forward.casino.util.FastAccess;

public abstract class Module implements FastAccess {
    public abstract void load();
    public abstract void unload();
}
