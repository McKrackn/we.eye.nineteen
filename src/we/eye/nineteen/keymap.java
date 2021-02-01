package we.eye.nineteen;

public class keymap {
	String N="8";
	String O="6";
	String S="2";
	String W="4";
	String attack="a";
	String map="m";
	String help="h";
	String showChar="c";
	String inventory="i";
	String use="b";
	String talk="k";
	String quit="q";
	
	String teleport="t";
	String genMon="g";
	String showMons="s";

	public void showHelp() {
		System.out.printf("\nverfügbare Befehle:\n");
		System.out.printf("-------------------\n\n");
		System.out.printf("%s - diese Hilfe anzeigen  %s - angreifen             %s - Charakter zeigen\n",this.help,this.attack,this.showChar);
		System.out.printf("%s - Item benutzen (!NA!)  %s - kommunizieren (!NA!)  %s - Inventar zeigen\n",this.use,this.talk,this.inventory);
		System.out.printf("%s - Weltarte anzeigen     %s - Spiel beenden         %s, %s, %s, %s - Steuerung\n",this.map,this.quit,this.N,this.S,this.O,this.W);
		System.out.printf("\nEntwicklerfunktionen (exceptions möglich):\n");
		System.out.printf("%s - Gegner erschaffen     %s - Gegner anzeigen       %s - teleport\n\n",this.genMon,this.showMons,this.teleport);
	}
}
