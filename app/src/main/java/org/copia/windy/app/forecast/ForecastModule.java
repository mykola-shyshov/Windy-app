package org.copia.windy.app.forecast;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import org.copia.windy.app.ApplicationObjectGraph;
import org.copia.windy.app.MainActivity;
import org.copia.windy.app.utils.CacheModule;
import org.copia.windy.app.utils.InmemoryCache;

import javax.inject.Singleton;

@Module(
        injects = { ForecastApiClient.class },
        includes = { CacheModule.class },
        library = true
)
public class ForecastModule {

    @Provides @Singleton
    ForecastClient provideForecastClient() {
        ForecastApiClient client = new ForecastApiClient();
        ApplicationObjectGraph.getInstance().inject( client );
        return client;
    }

}
