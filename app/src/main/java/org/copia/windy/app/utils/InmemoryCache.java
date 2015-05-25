package org.copia.windy.app.utils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class InmemoryCache implements Cache {

    /**
     * In ms
     */
    private long expirationTime;

    private HashMap<Object, InmemoryCacheItem> cache = new HashMap<Object, InmemoryCacheItem>();

    public InmemoryCache( long expirationTime ) {
        this.expirationTime = expirationTime;
    }

    public Object get( Object key ) {
        InmemoryCacheItem data = cache.get( key );
        if (data == null) {
            return null;
        }
        if ( data.getUpdatedDate().getTime() + expirationTime > new Date().getTime() ) {
            return data.getValue();
        } else {
            cache.remove( key );
            return null;
        }
    }

    public Object get( String namespace, Object key ) {
        return get( Arrays.asList( namespace, key ) );
    }

    public void put( Object key, Object value ) {
        cache.put( key, new InmemoryCacheItem( value ) );
    }

    public void put( String namespace, Object key, Object value ) {
        put( Arrays.asList( namespace, key ), value );
    }
}
