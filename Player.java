import java.util.ArrayList;

public abstract class Player {
	static String blue = "\u001B[34;1m", red = "\u001B[31;1m",
			green = "\u001B[32m", end = "\u001B[0m";
	static ArrayList<Identity> Identities = new ArrayList<Identity>();
	public static int numTown, numMafia;
	public int hat;
	Player otherMafia;
	Identity pub, priv;
	public int role;
	String Role;

	public Player() {
		pub = new Identity();
		priv = new Identity();
		otherMafia = null;
		numTown = 5;
		numMafia = 2;
	}

	public void addVisit(String s) {
		priv.visits.add(s);
	}

	public static String roleToString(int n) {
		if (n == 1)
			return red + "Mafia" + end;
		else if (n == 2)
			return red + "Mafia" + end;
		else if (n == 3)
			return blue + "Cop" + end;
		else if (n == 4)
			return blue + "Doctor" + end;
		else
			return blue + "Town" + end;
	}

	public void claim(int n) {
		if (pub.role != n)
			System.out.println("    " + green + hat + " has claimed " + n + "!"
					+ end);
		pub.role = n;
	}

	public final int day(int i) {
		if (pub.life) {
			if ((role == 1) || (role == 2))
				return dayMafia();
			if (role == 3)
				return dayCop();
			if (role == 4)
				return dayDoctor();
			else
				return dayTown();
		} else
			return 0;
	}

	public abstract int dayCop();

	public abstract int dayDoctor();

	public abstract int dayMafia();

	public abstract int dayTown();

	public void die() {
		pub.life = false;
		priv.life = false;
		pub.role = role;
		pub.Role = Role;
		System.out.println("    " + hat + " has died! Role was " + Role + " (" + role
				+ ")");
		if ((role == 1) || (role == 2))
			numMafia--;
		else
			numTown--;
	}

	public boolean isPlayerAlive(int n) {
		Identity p = Identities.get(n - 1);
		return p.life;
	}

	public boolean isPlayerDead(int n) {
		Identity p = Identities.get(n - 1);
		return !p.life;
	}

	public final int night(int i, int mafiaChoice) {
		if (pub.life) {
			if ((role == 1) || (role == 2))
					return nightMafia(i, mafiaChoice);
			if (role == 3)
				return nightCop();
			if (role == 4)
				return nightDoctor();
			else
				return 0;
		} else if ((role == 1) || (role == 2))
			return mafiaChoice;
		else return 0;
	}

	public abstract int nightCop();

	public abstract int nightDoctor();

	public abstract int nightMafia(int i, int mafiaChoice);

	public void revealVisits() {
		for (String s : priv.visits)
			pub.visits.add(s);
	}

	public void setHat(int n) {
		pub.hat = n;
		priv.hat = n;
		hat = n;
	}

	public void setRole(int n) {
		priv.role = n;
		role = priv.role;
		Role = roleToString(role);
		priv.Role = Role;
		pub.Role = "Unknown";
	
	}

	@Override
	public String toString() {

		return "My hat is " + hat + " and my role is " + priv.role;
	}

}
