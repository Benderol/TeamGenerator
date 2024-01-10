package com.project.TeamGenerator.services.impl;

import com.project.TeamGenerator.exeptions.EmptyPlayerValue;
import com.project.TeamGenerator.exeptions.NotEnoughPlayers;
import com.project.TeamGenerator.exeptions.PlayerExistsException;
import com.project.TeamGenerator.models.Player;
import com.project.TeamGenerator.models.User;
import com.project.TeamGenerator.models.dto.PlayerDTO;
import com.project.TeamGenerator.models.enums.Tier;
import com.project.TeamGenerator.repositories.PlayerRepository;
import com.project.TeamGenerator.repositories.UserRepository;
import com.project.TeamGenerator.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;
    private UserRepository userRepository;


    @Override
    public String addPlayer(PlayerDTO playerDTO, Principal principal) throws PlayerExistsException{
        User user = findUserByPrincipal(principal);

        checkExceptions(playerDTO, user);

        Player player = Player.builder()
                .firstName(playerDTO.getFirstName())
                .lastName(playerDTO.getLastName())
                .tier(playerDTO.getTier())
                .user(user)
                .build();


        addPlayerAndSave(user,player);

        return "Player was added";
    }

    private void checkExceptions(PlayerDTO playerDTO, User user) throws PlayerExistsException {
        if(playerDTO.getFirstName().isEmpty() || playerDTO.getLastName().isEmpty())
            throw new EmptyPlayerValue();

        if(playerRepository
                .getByFirstNameAndLastNameAndUser(playerDTO.getFirstName(), playerDTO.getLastName(), user).isPresent())
            throw new PlayerExistsException();
    }


    @Override
    public List<Player> getAllPlayers(Principal principal) {
        return findUserByPrincipal(principal).getPlayers();
    }

    @Override
    public Player deletePlayerById(int id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new RuntimeException("Cant find player"));
        player.getUser().getPlayers().remove(player);
        playerRepository.deleteById(id);
        return player;
    }

    @Override
    public String setIsPlayingToPlayer(Integer id, boolean bool, Principal principal) {
        User user = findUserByPrincipal(principal);
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player with id: "+ id + " was not found"));

        player.setPlaying(bool);
        addPlayerAndSave(user,player);

        return bool + " was set into player with id: " + id;
    }

    @Override
    public Map<Integer, List<Player>> makeTeam(Principal principal, int teams, int numberOfPlayersInOneTeam) throws NotEnoughPlayers {

        List<Player> players = getAllByPlaying(principal, true);
         if(players.isEmpty() || players.size() % teams != 0)
             throw new NotEnoughPlayers();

         int tierValues = (getFullTierValues(players) / teams);

         Map<Integer, List<Player>> resultMap = new HashMap<>();
         Map<Tier, List<Player>> mapWithTiers = getMapWithPlayersByTier(players);
         for(int i=1; i<=teams; i++) {
             try {
                 resultMap.put(i, makeTeams(mapWithTiers, tierValues, numberOfPlayersInOneTeam));
             }catch (Exception e){
                 i=0;
                 tierValues+=5;
                 mapWithTiers = getMapWithPlayersByTier(players);
                 resultMap.clear();
             }
         }

        return resultMap;
    }

    private User findUserByPrincipal(Principal principal){
        return userRepository.findUserByUsername(principal.getName())
                .orElseThrow( () -> new RuntimeException("User with username: "+ principal.getName() + " not found"));
    }

    private void addPlayerAndSave(User user, Player player){
        user.getPlayers()
                .removeIf(play -> play.getFirstName().equals(player.getFirstName()) && play.getLastName().equals(player.getLastName()));
        user.getPlayers().add(player);
        playerRepository.save(player);
    }

    private List<Player> getAllByPlaying(Principal principal, boolean isPLaying) {
        return getAllPlayers(principal)
                .stream().filter( player -> player.isPlaying() == isPLaying).toList();
    }

    private Map<Tier,List<Player>> getMapWithPlayersByTier(List<Player> allPLayers){
        Map<Tier,List<Player>> map = new HashMap<>();
        for(Player player : allPLayers){
            if(!map.containsKey(player.getTier())){
                map.put(player.getTier(), new ArrayList<>());
            }
            map.get(player.getTier()).add(player);
        }

        return map;
    }

    private int getFullTierValues(List<Player> players){
        return players.stream().mapToInt(player -> player.getTier().getValue()).sum();
    }

    private List<Player> makeTeams(Map<Tier,List<Player>> map, int tierValueForOneTeam, int numberOfPlayersForOneTeam) throws Exception{
        List<Player> players = new ArrayList<>();
        Player player;
        int presentValueForTeam = 0, presentTierValue = Tier.getMaxValue(), presentNumberOfPlayers = 0;
        while (presentNumberOfPlayers < numberOfPlayersForOneTeam) {
            Tier presentTier = Tier.getTierByValue(presentTierValue);
            if(checkMap(map, presentTier) && presentValueForTeam + presentTierValue <= tierValueForOneTeam){
                player =  map.get(presentTier)
                      .get(getRandomIndex(map,presentTier));

                players.add(player);

                presentValueForTeam += presentTierValue;

                map.get(presentTier).remove(player);
                presentNumberOfPlayers++;
            }else{
                throwExceptionIfNeeded(presentTierValue);
                presentTierValue /= 2;
            }
        }


        return players;
    }

    private boolean checkMap(Map<Tier,List<Player>> map, Tier presentTier){
        return map.get(presentTier) != null && !map.get(presentTier).isEmpty();
    }

    //For more random
    private int getRandomIndex(Map<Tier,List<Player>> map, Tier presentTier){
        return (int) (Math.random()*map.get(presentTier).size());
    }
    private void throwExceptionIfNeeded(int presentTierValue) throws Exception{
        if(presentTierValue == 0)
            throw new Exception("Can't make team");
    }
}
