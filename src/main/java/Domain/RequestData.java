package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class RequestData {
    private String url;;
    private String userName;
    private String password;
    private String dbId;
    public RequestData(String url, String userName, String password, String dbId) {
        this.setUrl(url);
        this.setUserName(userName);
        this.setPassword(password);
        this.setDbId(dbId);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }
}
