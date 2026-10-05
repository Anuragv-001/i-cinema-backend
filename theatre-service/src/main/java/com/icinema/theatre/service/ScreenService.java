package com.icinema.theatre.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.icinema.theatre.client.SeatingClient;
import com.icinema.theatre.entity.Screen;
import com.icinema.theatre.repository.ScreenRepository;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final SeatingClient seatingClient;

    public ScreenService(
            ScreenRepository screenRepository,
            SeatingClient seatingClient) {
        this.screenRepository = screenRepository;
        this.seatingClient = seatingClient;
    }

    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    public Screen getScreenById(Long id) {
        return screenRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Screen not found with id: " + id));
    }

    public List<Screen> getScreensByTheatreId(Long theatreId) {
        return screenRepository.findAll()
                .stream()
                .filter(screen -> screen.getTheatreId().equals(theatreId))
                .toList();
    }

    public Screen createScreen(Screen screen) {

        Screen savedScreen = screenRepository.save(screen);

        double price = getDefaultPrice(savedScreen.getScreenType());

        seatingClient.generateSeats(
                savedScreen.getId(),
                savedScreen.getTotalSeats(),
                price
        );

        return savedScreen;
    }

    public Screen updateScreen(
            Long id,
            Screen screenDetails) {

        Screen screen = getScreenById(id);

        int oldCapacity = screen.getTotalSeats();
        int newCapacity = screenDetails.getTotalSeats();

        screen.setTheatreId(screenDetails.getTheatreId());
        screen.setScreenNumber(screenDetails.getScreenNumber());
        screen.setTotalSeats(newCapacity);
        screen.setScreenType(screenDetails.getScreenType());

        Screen updatedScreen = screenRepository.save(screen);

        double price = getDefaultPrice(updatedScreen.getScreenType());

        if (oldCapacity != newCapacity) {

            seatingClient.resizeSeats(
                    updatedScreen.getId(),
                    newCapacity,
                    price
            );

        }

        return updatedScreen;
    }

    public void deleteScreen(Long id) {

        if (!screenRepository.existsById(id)) {
            throw new RuntimeException("Screen not found");
        }

        screenRepository.deleteById(id);
    }

    private double getDefaultPrice(String screenType) {

        if ("IMAX".equalsIgnoreCase(screenType)) {
            return 250.0;
        }

        if ("PREMIUM".equalsIgnoreCase(screenType)) {
            return 220.0;
        }

        return 180.0;
    }
}