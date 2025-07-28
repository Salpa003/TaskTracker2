package util;

public class ConnectionPoolAdapter {

    private ConnectionPool pool;

   public void init() {
       pool = new ConnectionPool();
   }

    public  void terminate() {
       pool.terminate();
    }
}
