package tests;

import java.security.Permission;

/**
 * For disabling System.exit()
 * 
 * @author GBH
 *
 */
public class ExitSystemDisable {

//-----------------------
  public static void main(String[] args) {
  // Usage: 
    ExitTrappedException.forbidSystemExitCall();
    try {
    // Call the "exiting" code here...
    } catch (ExitTrappedException e) {
    } finally {
      ExitTrappedException.enableSystemExitCall();
    }
  }

  private static class ExitTrappedException extends SecurityException {

    private static void forbidSystemExitCall() {
      final SecurityManager securityManager = new SecurityManager() {

        public void checkPermission(Permission permission) {
          if ("exitVM".equals(permission.getName())) {
            throw new ExitTrappedException();
          }
        }
      };
      System.setSecurityManager(securityManager);
    }

    private static void enableSystemExitCall() {
      System.setSecurityManager(null);
    }
  }
}
