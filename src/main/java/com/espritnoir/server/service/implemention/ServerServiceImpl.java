package com.espritnoir.server.service.implemention;

import com.espritnoir.server.enumeration.Status;
import com.espritnoir.server.model.Server;
import com.espritnoir.server.repository.ServerRepository;
import com.espritnoir.server.service.ServerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static java.lang.Boolean.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {
    private final ServerRepository serverRepository;
    @Override
    public Server create(Server server) {
        log.info("Saving new server {}", server.getName());
        server.setImageUrl(SetServerImageUrl());
        return serverRepository.save(server);
    }


    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by ID: {}", id);
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server by ID: {}", id);
        serverRepository.deleteById(id);
        return TRUE;
    }
//***************************************************************Others Methods***************************************************************//
private String SetServerImageUrl() {
        String[] imageNames = {"serve1.png","serve2.png","serve3.png","serve4.png","serve5.png","serve6.png"};
    return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/images/" + imageNames[new Random().nextInt(6)]).toUriString();
}
}
