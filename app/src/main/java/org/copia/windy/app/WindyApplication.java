package org.copia.windy.app;

import android.app.Application;
import dagger.ObjectGraph;

import java.util.Arrays;
import java.util.List;

public class WindyApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        ApplicationObjectGraph.getInstance().setObjectGraph(
                ObjectGraph.create( getModules().toArray() )
        );
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                (Object) new MainActivityModule()
        );
    }

    public void inject(Object object) {
        ApplicationObjectGraph.getInstance().getGraph().inject( object );
    }
}
