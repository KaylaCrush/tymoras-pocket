import core.*;
import java.util.HashMap;
import java.util.Map;

public class TymorasPocket {
    private static TymorasPocket instance; // Singleton instance
    private DiceBag diceBag;
    //private PersistenceManager persistenceManager;
    //private Map<String, User> users; // Future user management

    private TymorasPocket() {
        this.diceBag = new DiceBag("Main Bag");
        //this.persistenceManager = new PersistenceManager();
        //this.users = new HashMap<>();
    }

    public static TymorasPocket getInstance() {
        if (instance == null) {
            instance = new TymorasPocket();
        }
        return instance;
    }

    public DiceBag getDiceBag() {
        return diceBag;
    }

//    public PersistenceManager getPersistenceManager() {
//        return persistenceManager;
//    }
//
//    public void addUser(String username) {
//        users.put(username, new User(username));
//    }
//
//    public User getUser(String username) {
//        return users.get(username);
//    }
//
//    public void saveState(String filename) {
//        persistenceManager.save(this, filename);
//    }
//
//    public static TymorasPocket loadState(String filename) {
//        instance = PersistenceManager.load(filename);
//        return instance;
//    }
}
