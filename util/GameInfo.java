package util;

/**
 * Classe di utilità che serve a mantenere il gioco
 */
public class GameInfo {
	
public static int MAX_MOVES = 200;
	
	public static String SERVER = "localhost";
	
	public static int SERVER_PORT = 8901;
	
	public static long TIME_OUT = 970;
	
	public static long SET_UP_TIME = 10000;
	
	/**
	 *  java -jar ai.jar <hostname> <hostport> <set_up_time> <time_for_lookup_move> <max_moves>
	 * @param args
	 */
	public static void initInfo(String [] args){
		if(args.length == 0)
			return;
		else if(args.length != 5){
			System.out.println("::Error:Param: <hostname> <hostport> <set_up_time> <time_for_lookup_move> <max_moves>");
			System.exit(-1);
		}else{
			SERVER = args[0];
			SERVER_PORT = Integer.parseInt(args[1]);
			SET_UP_TIME = Long.parseLong(args[2]);
			//per sicurezza :)
			TIME_OUT = Long.parseLong(args[3]) - 50L;
			MAX_MOVES = Integer.parseInt(args[4]);
		}
	}
}
