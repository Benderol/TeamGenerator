package com.project.TeamGenerator.services;

import com.project.TeamGenerator.exeptions.NotEnoughPlayers;
import com.project.TeamGenerator.exeptions.PlayerExistsException;
import com.project.TeamGenerator.models.Player;
import com.project.TeamGenerator.models.dto.PlayerDTO;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface PlayerService {
    String addPlayer(PlayerDTO playerDTO, Principal principal) throws PlayerExistsException;

    List<Player> getAllPlayers(Principal principal);

    Player deletePlayerById(int id);
    String setIsPlayingToPlayer(Integer id, boolean bool, Principal principal);

    Map<Integer, List<Player>> makeTeam(Principal principal, int teams, int numberOfPlayersInOneTeam) throws NotEnoughPlayers;
}
