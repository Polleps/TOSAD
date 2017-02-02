package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class RequestData {
    private String url;;
    private String userName;
    private String password;
    private String dbId;
    private String serviceName;
    public RequestData(String url, String userName, String password, String dbId, String serviceName) {
        this.setUrl(url);
        this.setUserName(userName);
        this.setPassword(password);
        this.setDbId(dbId);
        this.setServiceName(serviceName);
    }

    public String getUrl() {
        return url + "/" + serviceName;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String toString(){
        return "DATA: URL: " + getUrl() + " USR: " + userName + " PSW: " + password + " DB: " + dbId;
    }
}
