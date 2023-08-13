package me.hsgamer.blocommands;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Permissions {
    public static final Permission ADMIN = new Permission("blocommands.admin", PermissionDefault.OP);

    private Permissions() {
        // EMPTY
    }
}
