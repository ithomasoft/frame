package com.thomas.frame.data;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.thomas.frame.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import retrofit2.Retrofit;

/**
 * 统一管理数据业务层
 *
 * @author <a href="mailto:tom_flying@163.com">thomas</a>
 */
@Singleton
public class DataRepository implements IDataRepository {
    @Inject
    Lazy<Retrofit> mRetrofit;

    @Inject
    Application mApplication;

    /**
     * 缓存 Retrofit Service
     */
    private LruCache<String, Object> mRetrofitServiceCache;
    /**
     * 缓存 RoomDatabase
     */
    private LruCache<String, Object> mRoomDatabaseCache;

    @Inject
    public DataRepository() {
    }

    /**
     * 提供上下文{@link Context}
     *
     * @return {@link #mApplication}
     */
    @Override
    public Context getContext() {
        return mApplication;
    }

    /**
     * 传入Class 通过{@link retrofit2.Retrofit#create(Class)} 获得对应的Class
     *
     * @param service
     * @param <T>
     * @return {@link retrofit2.Retrofit#create(Class)}
     */
    @Override
    public <T> T getRetrofitService(@NonNull Class<T> service) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = new LruCache<>(Constants.DEFAULT_RETROFIT_SERVICE_MAX_SIZE);
        }
        checkNotNull(mRetrofitServiceCache);

        T retrofitService = (T) mRetrofitServiceCache.get(service.getCanonicalName());
        if (retrofitService == null) {
            synchronized (mRetrofitServiceCache) {
                if (retrofitService == null) {
                    retrofitService = mRetrofit.get().create(service);
                    //缓存
                    mRetrofitServiceCache.put(service.getCanonicalName(), retrofitService);
                }

            }
        }

        return retrofitService;
    }

    /**
     * 传入Class 通过{@link Room#databaseBuilder},{@link RoomDatabase.Builder<T>#build()}获得对应的Class
     *
     * @param database
     * @param dbName   为{@code null}时，默认为{@link Constants#DEFAULT_DATABASE_NAME}
     * @param <T>
     * @return {@link RoomDatabase.Builder<T>#build()}
     */
    @Override
    public <T extends RoomDatabase> T getRoomDatabase(@NonNull Class<T> database, @Nullable String dbName) {
        if (mRoomDatabaseCache == null) {
            mRoomDatabaseCache = new LruCache<>(Constants.DEFAULT_ROOM_DATABASE_MAX_SIZE);
        }
        checkNotNull(mRoomDatabaseCache);

        T roomDatabase = (T) mRoomDatabaseCache.get(database.getCanonicalName());
        if (roomDatabase == null) {
            synchronized (mRoomDatabaseCache) {
                if (roomDatabase == null) {
                    RoomDatabase.Builder<T> builder = Room.databaseBuilder(getContext(), database, TextUtils.isEmpty(dbName) ? Constants.DEFAULT_DATABASE_NAME : dbName);
                    roomDatabase = builder.build();
                    //缓存
                    mRoomDatabaseCache.put(database.getCanonicalName(), roomDatabase);
                }
            }
        }
        return roomDatabase;
    }


    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */

    private <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
