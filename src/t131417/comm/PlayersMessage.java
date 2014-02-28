package t131417.comm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import teams.ucmTeam.Message;
import teams.ucmTeam.UCMPlayer;

public final class PlayersMessage extends Message {

    private final List<UCMPlayer> players;

    public PlayersMessage (UCMPlayer... players) {
        this.players = Collections.unmodifiableList(new ArrayList<UCMPlayer>(Arrays.asList(players)));
    }

    public List<UCMPlayer> getPlayers () {
        return players;
    }
}
