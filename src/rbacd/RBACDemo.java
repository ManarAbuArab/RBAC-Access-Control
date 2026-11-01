
package rbacd;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
 
public class RBACDemo {
 
    enum Role {
        ADMIN, MANAGER, CUSTOMER
    }
 
    enum Permission {
        CREATE, VIEW, UPDATE, DELETE
    }
 
    static class RBACSystem {
        private final Map<String, Set<Role>> userRoles = new HashMap<>();
        private final Map<Role, Set<Permission>> rolePermissions =
                new EnumMap<>(Role.class);
 
        RBACSystem() {
            for (Role role : Role.values()) {
                rolePermissions.put(role, EnumSet.noneOf(Permission.class));
            }
        }
 
        // Assign a role to a user.
        void assignRole(String username, Role role) {
            userRoles.computeIfAbsent(username,
                    key -> EnumSet.noneOf(Role.class)).add(role);
        }
 
        // Add a permission to a role.
        void grantPermission(Role role, Permission permission) {
            rolePermissions.get(role).add(permission);
        }
 
        // Check whether the user's role allows the requested action.
        boolean hasAccess(String username, Permission permission) {
            Set<Role> roles = userRoles.get(username);
 
            if (roles == null) {
                return false;
            }
 
            for (Role role : roles) {
                if (rolePermissions.get(role).contains(permission)) {
                    return true;
                }
            }
 
            return false;
        }
 
        void checkAccess(String username, Permission permission) {
            String result = hasAccess(username, permission)
                    ? "ALLOWED" : "DENIED";
            System.out.println(username + " - " + permission
                    + " account: " + result);
        }
    }
 
    public static void main(String[] args) {
        RBACSystem rbac = new RBACSystem();
 
        // Set permissions for each role.
        rbac.grantPermission(Role.ADMIN, Permission.CREATE);
        rbac.grantPermission(Role.ADMIN, Permission.VIEW);
        rbac.grantPermission(Role.ADMIN, Permission.UPDATE);
        rbac.grantPermission(Role.ADMIN, Permission.DELETE);
 
        rbac.grantPermission(Role.MANAGER, Permission.VIEW);
        rbac.grantPermission(Role.MANAGER, Permission.UPDATE);
 
        rbac.grantPermission(Role.CUSTOMER, Permission.VIEW);
 
        // Assign roles to users.
        rbac.assignRole("Ali", Role.ADMIN);
        rbac.assignRole("Sara", Role.MANAGER);
        rbac.assignRole("Omar", Role.CUSTOMER);
 
        // Test access to the Account object.
        System.out.println("RBAC - Account Access Test");
        System.out.println("---------------------------");
 
        rbac.checkAccess("Ali", Permission.CREATE);
        rbac.checkAccess("Ali", Permission.DELETE);
        rbac.checkAccess("Sara", Permission.VIEW);
        rbac.checkAccess("Sara", Permission.UPDATE);
        rbac.checkAccess("Sara", Permission.DELETE);
        rbac.checkAccess("Omar", Permission.VIEW);
        rbac.checkAccess("Omar", Permission.UPDATE);
    }
}


