package org.copia.windy.app;

import dagger.ObjectGraph;

public class ApplicationObjectGraph {

    private static ApplicationObjectGraph instance;

    private ApplicationObjectGraph() {
    }

    public static ApplicationObjectGraph getInstance() {
        if ( instance == null ) {
            instance = new ApplicationObjectGraph();
        }
        return instance;
    }

    private ObjectGraph objectGraph;

    public ObjectGraph getGraph() {
        if (objectGraph == null) {
            throw new Error( "graph is null" );
        }
        return objectGraph;
    }

    public void setObjectGraph( ObjectGraph objectGraph ) {
        this.objectGraph = objectGraph;
    }

    public void inject( Object object ) {
        getGraph().inject( object );
    }

}
