package com.esri.sde.sdk.client;

public class SeVersion {

    public static String SE_QUALIFIED_DEFAULT_VERSION_NAME = null;

    public SeVersion(SeConnection conn, String versionName) throws SeException {
    }

    public void create(boolean uniqueName, SeVersion newVersion) throws SeException{}

    public void getInfo() throws SeException{}

    public SeObjectId getStateId() {
        return null;
    }

    public void setName(String string) {
    }

    public String getName() {
        return null;
    }

    public void setParentName(String name) {
    }

    public void setDescription(String string) {
    }

    public void changeState(SeObjectId newStateId)throws SeException {
    }

    public void delete() throws SeException{}

    public void setOwner(String o) throws SeException {}
}
