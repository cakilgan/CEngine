package io.github.cakilgan.engine.system.ecs.util;

public class CEObjectID implements ID{
    String stringID;
    int integerID;
    public CEObjectID(String ID){
        this.stringID = ID;
    }
    public CEObjectID(int ID){
        this.integerID = ID;
    }
    @Override
    public String ID() {
        if (integerID>=0){
        return stringID.isEmpty()?integerID+"":stringID+":"+integerID;
        }else{
            return stringID.isEmpty()?"emptyID":stringID;
        }
    }
    @Override
    public boolean equalTo(ID obj) {
        return obj.ID().equals(this.ID());
    }
}
