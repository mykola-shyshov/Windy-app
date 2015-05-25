package org.copia.windy.app.utils;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
    library = true
)
public class CacheModule {

    private final static long EXPIRATION_CACHE_TIME = 3 * 60 * 1000;

    @Provides @Singleton InmemoryCache provideInmemoryCache() {
        return new InmemoryCache( EXPIRATION_CACHE_TIME );
    }

}
