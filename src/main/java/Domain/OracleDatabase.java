package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class OracleDatabase extends Database {
    private String url;
    private int port;
    private String usrName;
    private String password;

    public OracleDatabase(String url, int port, String usrName, String password) {
        this.url = url;
        this.port = port;
        this.usrName = usrName;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }
    public int getPort() {
        return port;
    }
    public String getUsrName() {
        return usrName;
    }
    public String getPassword() {
        return password;
    }
}
