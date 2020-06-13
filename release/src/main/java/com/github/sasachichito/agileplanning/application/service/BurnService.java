package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.application.command.burn.BurnUpdateCmd;
import com.github.sasachichito.agileplanning.domain.model.burn.*;
import com.github.sasachichito.agileplanning.domain.model.story.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BurnService {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final BurnRepository burnRepository;
    private final StoryRepository storyRepository;

    public Set<Burn> getAll() {
        return this.burnRepository.getAll();
    }

    public Burn get(BurnId burnId) {
        return this.burnRepository.get(burnId);
    }

    public Burn createBurn(int storyId) {
        Burn burn = new Burn(
                this.burnRepository.nextId(),
                LocalDate.now(),
                new StoryId(storyId),
                new BurnSpec(this.burnRepository, this.storyRepository));

        this.burnRepository.add(burn);
        return burn;
    }

    public Burn updateOrPut(BurnUpdateCmd burnUpdateCmd) {
        BurnId burnId = new BurnId(burnUpdateCmd.burnId());

        if (this.burnRepository.exist(burnId)) {
            return this.update(
                    this.burnRepository.get(burnId),
                    burnUpdateCmd);
        }
        return this.put(burnUpdateCmd);
    }

    private Burn update(Burn burn, BurnUpdateCmd burnUpdateCmd) {
        burn.change(
                LocalDate.parse(burnUpdateCmd.date(), dtf),
                new StoryId(burnUpdateCmd.storyId()),
                new BurnSpec(this.burnRepository, this.storyRepository)
        );

        return burn;
    }

    private Burn put(BurnUpdateCmd burnUpdateCmd) {
        Burn burn = new Burn(
                new BurnId(burnUpdateCmd.burnId()),
                LocalDate.parse(burnUpdateCmd.date(), dtf),
                new StoryId(burnUpdateCmd.storyId()),
                new BurnSpec(this.burnRepository, this.storyRepository));

        this.burnRepository.put(burn);
        return burn;
    }

    public void delete(BurnId burnId) {
        Burn burn = this.burnRepository.get(burnId);
        burn.remove();

        this.burnRepository.put(burn);
    }
}
