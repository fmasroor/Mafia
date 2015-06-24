import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
	static String blue = "\u001B[34;1m", red = "\u001B[31;1m", green = "\u001B[32m", end = "\u001B[0m";
	public static int consensusReached(ArrayList<Integer> votes, int numPlayers) {
		Collections.sort(votes);
		if (modeFrequency(votes) > (numPlayers / 2))
			return mode(votes);
		else
			return 0;
	}

	public static void main(String args[]) {
		Player p1 = new TestPlayer(), p2 = new TestPlayer(), p3 = new TestPlayer();
		Player p4 = new TestPlayer(), p5 = new TestPlayer(), p6 = new TestPlayer(), p7 = new TestPlayer();
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		players.add(p5);
		players.add(p6);
		players.add(p7);
		Collections.shuffle(players, new Random(System.nanoTime())); // shuffle  around players

		ArrayList<Integer> roles = new ArrayList<Integer>();
		roles.add(1);
		roles.add(2);
		roles.add(3);
		roles.add(4);
		roles.add(5);
		roles.add(6);
		roles.add(7);
		Collections.shuffle(roles, new Random(System.nanoTime())); // shuffle around roles

		Player M1 = null, M2 = null, C = null, D = null;
		ArrayList<Identity> Ids = new ArrayList<Identity>();

		for (int i = 0; i < 7; i++) { //assign roles to all players
			players.get(i).setHat(i + 1);
			players.get(i).setRole(roles.get(i));
			if (roles.get(i) == 1)
				M1 = players.get(i);
			if (roles.get(i) == 2)
				M2 = players.get(i);
			if (roles.get(i) == 3)
				C = players.get(i);
			if (roles.get(i) == 4)
				D = players.get(i);
			Ids.add(players.get(i).pub);
			System.out.println("hat #" + (i + 1) + " got role "
					+ players.get(i).role + " " + players.get(i).Role);
		}
		Player.Identities = Ids;

		M1.otherMafia = M2;
		M2.otherMafia = M1;
		int nightno = 1;
		while ((Player.numTown > Player.numMafia) && (Player.numMafia > 0) && (nightno < 10)) {
			//this loop runs the game
			System.out.println("-----Night " + nightno + "-----");
			int m = 0, n = 0;
			int mafia = 0;
			for (int i = 0; i < 25; i++) { //asks the two mafia for their choice

				m = M1.night(i, n);
				n = M2.night(i, m);

				if (m == n) {
					mafia = m;
					M1.addVisit("" + mafia);
					M2.addVisit("" + mafia);
					break;
				}

			}
			System.out.println(red + "Mafia chose " + mafia + end);

			int doctor = D.night(0, 0); //ask doctor for choice
			if(doctor != 0)
			{
				D.addVisit("" + doctor);
				System.out.println(blue + "Doctor chose " + doctor + end);
			}

			int cop = C.night(0, 0); //ask cop for choice
			String s;
			if(cop != 0)
			{
				int p = players.get(cop - 1).priv.role;
				if ((p == 1) || (p == 2))
					s = "G";
				else
					s = "I";
				s = cop + s;
				C.addVisit(s); //give cop the report
				System.out.println(blue + "Cop chose " + cop + end);
				System.out.println("Cop got:" + s);
			}

			System.out.println("    -----Day " + nightno + "-----");

			if (doctor != mafia) //decide if someone dies
				players.get(mafia - 1).die();
			else
				System.out.println(green + "    No deaths last night!" + end);

			int k = 0;
			for (int i = 0; i < 20; i++) {
				ArrayList<Integer> votes = new ArrayList<Integer>();
				for (int j = 0; j < 7; j++) {
					int t = players.get(j).day(i);
					if (t != 0)
					{
						votes.add(t);
						System.out.println("    Player " + (j+1) + " has voted to lynch " + t);
					}
				}
				k = consensusReached(votes, Player.numTown + Player.numMafia);
				if (k != 0)
				{
					System.out.print("    Consensus reached! ");
					i = 60;
				}
			}
			if (k == -1)
				System.out.println("Town voted no lynch");
			else if (k == 0) System.out.println("Town did not come to a consensus.");
			else {
				System.out.println("Town lynches " + k);
				players.get(k - 1).die();
			}
			System.out.println(blue + Player.numTown + end + " " + red + Player.numMafia + end);
			nightno++;
		}
		if(Player.numMafia >= Player.numTown)System.out.println(red + "Mafia has won!");
		else System.out.println(blue + "Town has won!");
	}
	public static int mode(ArrayList<Integer> votes) {
		int mode = votes.get(0), count = 0, maxCount = 0;

		for (int element : votes) {
			count = 0;
			for (int j = 0; j < votes.size(); j++) {
				if (votes.get(j) == element)
					count++;
				if (count > maxCount) {
					maxCount = count;
					mode = element;
				}
			}
		}
		return mode;
	}
 
	public static int modeFrequency(ArrayList<Integer> votes) {
		int count = 0, maxCount = 0;

		for (int element : votes) {
			count = 0;
			for (int j = 0; j < votes.size(); j++) {
				if (votes.get(j) == element)
					count++;
				if (count > maxCount)
					maxCount = count;
			}
		}
		return maxCount;
	}
}
