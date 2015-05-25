package org.copia.windy.app.user;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        library = true
)
public class UserModule {

    @Provides @Singleton UserService provideUserService() {
        return new UserService();
    }
}
