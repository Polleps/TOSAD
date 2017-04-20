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
    private String ruleName;
    private String ruleTable;
    private String primaryKey;
    private String foreignKey;
    private String ruleTypeId;

    /*public RequestData(String url, String userName, String password, String dbId, String serviceName) {
        this.setUrl(url);
        this.setUserName(userName);
        this.setPassword(password);
        this.setDbId(dbId);
        this.setServiceName(serviceName);
    }*/
    public RequestData(String url, String userName, String password, String dbId, String serviceName, String ruleName, String ruleTable, String primaryKey, String foreignKey, String ruleTypeId) {
        this.setUrl(url);
        this.setUserName(userName);
        this.setPassword(password);
        this.setDbId(dbId);
        this.setServiceName(serviceName);
        this.setRuleName(ruleName);
        this.setRuleTable(ruleTable);
        this.setPrimaryKey(primaryKey);
        this.setForeignKey(foreignKey);
        this.setRuleTypeId(ruleTypeId);
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
        return "<span style='color:grey;'>DATA: URL: " + getUrl() + " USR: " + userName + " PSW: " + password + " DB: " + dbId + " RULE: " + ruleName + " TABLE: " + ruleTable + "</span>";
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getRuleTable() {
        return ruleTable;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setRuleTable(String ruleTable) {
        this.ruleTable = ruleTable;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getRuleTypeId() {
        return ruleTypeId;
    }

    public void setRuleTypeId(String ruleTypeId) {
        this.ruleTypeId = ruleTypeId;
    }
}
