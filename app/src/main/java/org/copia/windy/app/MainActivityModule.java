package org.copia.windy.app;

import dagger.Module;
import org.copia.windy.app.forecast.ForecastModule;
import org.copia.windy.app.user.UserModule;
import org.copia.windy.app.utils.CacheModule;

@Module(
    injects = { MainActivity.class },
    includes = { CacheModule.class, ForecastModule.class, UserModule.class }
)
public class MainActivityModule {
}
