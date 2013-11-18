package tests.file;

import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;

import name.pachler.nio.file.*;

/**
 * Watch for file changes in a directory...
 *
 * @author GBH
 */
public class PathWatcherTest {

   WatchKey watchingKey;
   WatchService watchService;

   public void simple() {
      String path = "/Temp";
      watchService = FileSystems.getDefault().newWatchService();
      Path watchedPath = Paths.get(path);
      WatchKey key = null;
      try {
         key = watchedPath.register(watchService, StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_DELETE);
      } catch (UnsupportedOperationException uox) {
         System.err.println("file watching not supported!");
         // handle this error here
      } catch (IOException iox) {
         System.err.println("I/O errors");
         // handle this error here
      }
      for (;;) {
         // take() will block until a file has been created/deleted
         try {
            key = watchService.take();
         } catch (InterruptedException ix) {
            // we'll ignore being interrupted
            continue;
         } catch (ClosedWatchServiceException cwse) {
            // other thread closed watch service
            System.out.println("watch service closed, terminating.");
            break;
         }

         // get list of events from key
         List<WatchEvent<?>> list = key.pollEvents();

         // VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
         // key to be reported again by the watch service
         key.reset();

         // we'll simply print what has happened; real applications
         // will do something more sensible here
         for (WatchEvent e : list) {
            String message = "";
            if (e.kind() == StandardWatchEventKind.ENTRY_CREATE) {
               Path context = (Path) e.context();
               message = context.toString() + " created";
            } else if (e.kind() == StandardWatchEventKind.ENTRY_DELETE) {
               Path context = (Path) e.context();
               message = context.toString() + " deleted";
            } else if (e.kind() == StandardWatchEventKind.OVERFLOW) {
               message = "OVERFLOW: more changes happened than we could retreive";
            }
            System.out.println(message);
         }
      }

   }

   public void setWatchedPath(String path) {
//      if (watchingKey != null) { // deregister
//         watchingKey.cancel();
//      }
      Path watchedPath = Paths.get(path);
//      WatchKey key = null;
      try {
         watchingKey = watchedPath.register(watchService,
                 StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_DELETE);
      } catch (UnsupportedOperationException uox) {
         System.err.println("file watching not supported!");
         // handle this error here
      } catch (IOException iox) {
         System.err.println("I/O errors");
         // handle this error here
      }
      //watchingKey = key;
   }
   WatchingThread watchingThread;

   public void startWatching(String path) {
      stopWatching();
      if (watchService == null) {
         watchService = FileSystems.getDefault().newWatchService();
      }
      setWatchedPath(path);
      new WatchingThread().start();
      //System.out.println("started WatchingThread");
   }

   class WatchingThread extends Thread {

      public void run() {
         while (watchService != null) {
            // take() will block until a file has been created/deleted
            WatchKey signalledKey;
            try {
               signalledKey = watchService.take();
            } catch (InterruptedException ix) {
               // we'll ignore being interrupted
               continue;
            } catch (ClosedWatchServiceException cwse) {
               // other thread closed watch service
               //System.out.println("watch service closed, terminating.");
               break;
            }
            // get list of events from key
            List<WatchEvent<?>> list = signalledKey.pollEvents();
            // VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
            // key to be reported again by the watch service
            signalledKey.reset();
            // we'll simply print what has happened; real applications
            // will do something more sensible here
            for (WatchEvent e : list) {
               String message = "";
               if (e.kind() == StandardWatchEventKind.ENTRY_CREATE) {
                  Path context = (Path) e.context();
                  message = context.toString() + " created";
               } else if (e.kind() == StandardWatchEventKind.ENTRY_DELETE) {
                  Path context = (Path) e.context();
                  message = context.toString() + " deleted";
               } else if (e.kind() == StandardWatchEventKind.OVERFLOW) {
                  message = "OVERFLOW: more changes happened than we could retreive";
               }
               System.out.println(message);
               System.out.flush();
            }
         }
         System.out.println("Watching stopped.");
      }
   }

   public void stopWatching() {
      if (watchService != null) {
         try {
            watchService.close();
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }
      watchService = null;
   }

   public static void main(String[] args) {
      PathWatcherTest w = new PathWatcherTest();
      //w.simple();
      System.out.println("watching /SciSoft...");
      w.startWatching("/SciSoft");
      try {
         Thread.sleep(10000);
      } catch (InterruptedException ex) {
      }
      System.out.println("watching /Temp...");
      //w.stopWatching();
      w.startWatching("/Temp");
      try {
         Thread.sleep(10000);
      } catch (InterruptedException ex) {
      }
      w.stopWatching();
      System.out.println("end");
      

   }
}
