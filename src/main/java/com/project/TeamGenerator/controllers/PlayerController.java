package com.project.TeamGenerator.controllers;

import com.project.TeamGenerator.exeptions.EmptyPlayerValue;
import com.project.TeamGenerator.exeptions.NotEnoughPlayers;
import com.project.TeamGenerator.exeptions.PlayerExistsException;
import com.project.TeamGenerator.models.Player;
import com.project.TeamGenerator.models.dto.PlayerDTO;
import com.project.TeamGenerator.models.dto.TeamNumberDTO;
import com.project.TeamGenerator.models.enums.Tier;
import com.project.TeamGenerator.services.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/team-generator/player")
@AllArgsConstructor
@Slf4j
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("tiers", Tier.values());
        return "player-add";
    }

    @PostMapping("/add")
    public String addPlayer(PlayerDTO player, Principal principal, Model model){
        System.out.println(player.getId());
        try {
            log.info(playerService.addPlayer(player, principal));
        }catch (PlayerExistsException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tiers", Tier.values());
            return "player-add";
        }

        return "redirect:/team-generator/player/pick-players";
    }

    @GetMapping("/pick-players")
    public String pick(Model model, Principal principal){
        model.addAttribute("playingPlayer", playerService.getAllPlayers(principal));

        return "pick-players";
    }

    @PostMapping("/pick/{id}/{bool}")
    public String pickOne(@PathVariable Integer id, @PathVariable boolean bool, Principal principal){
        log.info(playerService.setIsPlayingToPlayer(id,bool,principal));
        return "redirect:/team-generator/player/pick-players";
    }

    @PostMapping("/make-team")
    public String make(Principal principal, Model model, TeamNumberDTO teamNumber) {
        try {
            model.addAttribute("teams",teamNumber.getTeams());
            model.addAttribute("teamsMap", playerService.makeTeam(principal,teamNumber.getTeams(), teamNumber.getNumberOfPlayers()));
        } catch (NotEnoughPlayers e) {
            model.addAttribute("error",e.getMessage());
            model.addAttribute("playingPlayer", playerService.getAllPlayers(principal));

            return "pick-players";
        }

        return "get-teams";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable int id, Model model){
        model.addAttribute("player", playerService.deletePlayerById(id));
        model.addAttribute("tiers", Tier.values());

        return "player-add";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id){
         log.info(playerService.deletePlayerById(id).getFirstName());

        return "redirect:/team-generator/player/pick-players";
    }
}
