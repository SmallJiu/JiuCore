package cat.jiu.core.game.view;

import java.io.FileNotFoundException;

import cat.jiu.core.game.service.GameResource;
import cat.jiu.core.game.service.SoundPlayer;

public class Sound {
	public static void hitEnemy() {
		play(GameResource.sound + "hit_enemy.wav", false);
	}
	
	public static void hitPlayer() {
		play(GameResource.sound + "hit_player.wav", false);
	}
	
	public static void buttonClick() {
		play(GameResource.sound + "button_click.wav", false);
	}
	
	public static void backgroud() {
		play(GameResource.sound + "bg.wav", true);
	}
	
	private static void play(String file, boolean circulate) {
        try {
            SoundPlayer player = new SoundPlayer(file, circulate);
            player.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
