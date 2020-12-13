package com.devacado.photoapp.api.users.data;

import com.devacado.photoapp.api.users.ui.model.AlbumResponseModel;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

    @GetMapping("users/{id}/albums")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {
    @Override
    public AlbumsServiceClient create(Throwable throwable) {
        return new AlbumServiceClientFallback(throwable);
    }
}

class AlbumServiceClientFallback implements AlbumsServiceClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Throwable throwable;

    public AlbumServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        // TODO Auto-generated method stub

        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
            logger.error("404 error took place when getAlbums was called with userId: " + id + ". Error message: "
                    + throwable.getLocalizedMessage());
        } else {
            logger.error("Other error took place: " + throwable.getLocalizedMessage());
        }

        return new ArrayList<>();
    }
}