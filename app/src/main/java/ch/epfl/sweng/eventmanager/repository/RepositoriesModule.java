package ch.epfl.sweng.eventmanager.repository;

import ch.epfl.sweng.eventmanager.repository.impl.FirebaseCloudFunction;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseEventRepository;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseFeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseMapEditionRepository;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseNewsRepository;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseUserRepository;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public abstract class RepositoriesModule {
    @Binds
    @Singleton
    abstract NewsRepository providesNewsRepository(FirebaseNewsRepository repository);

    @Binds
    @Singleton
    abstract EventRepository providesEventRepository(FirebaseEventRepository repository);

    @Binds
    @Singleton
    abstract FeedbackRepository providesFeedbackRepository(FirebaseFeedbackRepository repository);

    @Binds
    @Singleton
    abstract UserRepository providesUserRepository(FirebaseUserRepository repository);

    @Binds
    @Singleton
    abstract CloudFunction providesCloudFunction(FirebaseCloudFunction impl);

    @Binds
    @Singleton
    abstract MapEditionRepository providesMapEditionRepository(FirebaseMapEditionRepository repository);
}
