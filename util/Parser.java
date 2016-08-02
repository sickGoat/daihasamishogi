package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import board.implementation.Move;
import board.implementation.MoveFactory;
import board.implementation.Player;

/**
 * Classe utilizzata per parserizzare i
 * messaggi dal server
 * @author antonio
 *
 */
public class Parser {
	
	private final Pattern pattern = Pattern.compile("[^\\s]\\w*$");
	
	private Matcher matcher = null;
	
	public Parser(){}
	
	/**
	 * Riceve la mossa fornita dal server
	 * e restituisce un oggetto mossa
	 * @param moveMessage
	 * @return
	 */
	public Move parseMove(String moveMessage){
		System.out.println("Server Message:" +moveMessage);
		matcher = pattern.matcher(moveMessage);
		if(matcher.find()){
			String cells = matcher.group();
			return MoveFactory.getInstance().create(cells);
		}else{
			throw new RuntimeException("Qualcosa è andato storto nel parser");
		}
	}
	
	/**
	 * Riceve il player fornito dal server
	 * e restituisce l'enum corrispondente
	 * @param playerMessage
	 * @return
	 */
	public Player parsePlayer(String playerMessage){
		matcher = pattern.matcher(playerMessage);
		if(matcher.find()){
			String player = matcher.group();
			if( player.equalsIgnoreCase(Player.WHITE.toString())){
				return Player.WHITE;
			}else{
				return Player.BLACK;
			}
		}else{
			throw new RuntimeException("Qualcosa è andato storto nel parser");
		}
	}
	
	
}
