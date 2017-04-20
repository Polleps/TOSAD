package Domain;

/**
 * Created by Polle on 19-4-2017.
 */
public class Column {

    private String name;
    private String relationType;
    private String relationTo;

    public Column(String name, String relationType, String relationTo) {
        this.name = name;
        this.relationType = relationType;
        this.relationTo = relationTo;
    }

    public String getName() {
        return name;
    }

    public String getRelationType() {
        return relationType;
    }

    public String getRelationTo() {
        return relationTo;
    }
    public String toString(){
        if(relationTo != null){
            return name + " " + relationType + " " + relationTo;
        }
        else {
            return name + " " + relationType + " ";
        }
    }
}
