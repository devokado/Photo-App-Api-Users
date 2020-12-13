package com.devacado.photoapp.api.users.service;

import com.devacado.photoapp.api.users.data.AlbumsServiceClient;
import com.devacado.photoapp.api.users.data.UserEntity;
import com.devacado.photoapp.api.users.data.UsersRepository;
import com.devacado.photoapp.api.users.shared.UserDTO;
import com.devacado.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    UsersRepository usersRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    //    RestTemplate restTemplate;
    Environment environment;
    AlbumsServiceClient albumsServiceClient;
    Logger logger = LoggerFactory.getLogger(this.getClass())

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder,
                            AlbumsServiceClient albumsServiceClient,
                            Environment environment) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.albumsServiceClient = albumsServiceClient;
        this.environment = environment;
    }

    @Override
    public UserDTO createUser(UserDTO userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        usersRepository.save(userEntity);
        UserDTO returnValue = modelMapper.map(userEntity, UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if (userEntity == null) throw new UsernameNotFoundException(username);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(userEntity, UserDTO.class);
    }

    @Override
    public UserDTO getUserByUserId(String userId) {

        UserEntity userEntity = usersRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("User not found");

        UserDTO userDto = new ModelMapper().map(userEntity, UserDTO.class);

//        String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
//
//        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//        });
//        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();

        logger.info("Before calling albums Microservice");
        List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
        logger.info("After calling albums Microservice");
//
        userDto.setAlbums(albumsList);

        return userDto;
    }
}
