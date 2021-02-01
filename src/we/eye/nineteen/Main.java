package we.eye.nineteen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Main {
	static PlayerCharacter PC=new PlayerCharacter();
	static OuterMap[][] WorldMap;
	static int WorldX;
	static int WorldY;
	static int LocalX;
	static int LocalY;
	static final char defaultGround='.';
	static keymap keymap=new keymap();
	static Scanner sc=new Scanner(System.in);
	static NonPlayerCharacter[] monsters=new NonPlayerCharacter[20];
	static item[] items=new item[20]; 
	static List<weapon> weaponList=new LinkedList<weapon>();
	static List<armor> armorList=new LinkedList<armor>();


	public static void main(String[] args) {
		

		int sizeWorldMapX=20;
		int sizeWorldMapY=20;
		
		WorldMap=new OuterMap[sizeWorldMapX][sizeWorldMapY];
		WorldX=WorldMap.length/2;
		WorldY=WorldMap[0].length/2;
		
		WorldMap[WorldX][WorldY]=new OuterMap();
		WorldMap[WorldX][WorldY].distance=Math.abs(WorldX-(WorldMap.length/2))+Math.abs(WorldY-(WorldMap[0].length/2));
		WorldMap[WorldX][WorldY].MapGrid=new char[8][16];
		WorldMap[WorldX][WorldY].symbol='!';
		
		for (int i=0;i<WorldMap[WorldX][WorldY].MapGrid.length;i++) {
			Arrays.fill(WorldMap[WorldX][WorldY].MapGrid[i],defaultGround);
		}
		
		LocalX=3;
		LocalY=3;
		
		initialize();
		keymap.showHelp();
		//initialize Player Character
		PC.name="TestCharacter";
		PC.race="Human";
		PC.sex='M';
		PC.age=18;
		PC.STR=18;
		PC.DEX=18;
		PC.CON=18;
		PC.INT=18;
		PC.maxHP=PC.CON*4;
		PC.actHP=PC.maxHP;
//		PC.actWeapon=weaponList.get(0);
//		PC.actArmor=armorList.get(0);
		PC.actWeapon=items[3];
		PC.actArmor=items[7];

		
//		items[3].inPossession=1;
//		items[7].inPossession=1;
		



		System.out.printf("\n\nWillkommen in ---!\n\n");
		//System.out.printf("Testumgebung verwenden? (J/N)");		
		//String s = sc.next(); //one char read, returned as String
		//c = s.charAt(0); //extract the char from String


			
			boolean valid=false;
			boolean quit=false;


			while (quit==false) {
				//for (int i=0; i<100; i++) System.out.println();

				
			WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=PC.symbol;
            System.out.println();

			for (int i = 0; i < WorldMap[WorldX][WorldY].MapGrid.length; i++) {
	            for (int j = 0; j < WorldMap[WorldX][WorldY].MapGrid[0].length; j ++) {
	            		System.out.print(WorldMap[WorldX][WorldY].MapGrid[i][j]);
	            	}
	            System.out.println();
	            }
			valid=false;
	            
			while (valid == false) {
				//sc.useDelimiter(("\r?\n|\r")); 

				System.out.printf("Befehl eingeben: ");				
				String cmd = sc.next();
	            System.out.println();

				if (cmd.equalsIgnoreCase(keymap.N)||cmd.equalsIgnoreCase(keymap.S)||cmd.equalsIgnoreCase(keymap.O)||cmd.equalsIgnoreCase(keymap.W) ) {
						valid=movement(cmd, sc);
				} else if (cmd.equalsIgnoreCase(keymap.attack)) {
					attack(sc);
					valid=true;
				} 
				else if (cmd.equalsIgnoreCase(keymap.showChar)) {
					printStats();
					valid=true;
				}
				else if (cmd.equalsIgnoreCase(keymap.genMon)) {
					generateMon();
					valid=true;
				}
				else if (cmd.equalsIgnoreCase(keymap.showMons)) {
					printMon();
					valid=true;
				}
				else if (cmd.equalsIgnoreCase(keymap.help)||cmd.equalsIgnoreCase("help")||cmd.equalsIgnoreCase("?")){
					keymap.showHelp();
					valid=true;
				}
				else if (cmd.equalsIgnoreCase(keymap.map)) {
					showMap();
					valid=true;
				}
				else if (cmd.equalsIgnoreCase(keymap.teleport)) {
					teleport(sc);
					valid=true;
				}
				else if (cmd.equalsIgnoreCase(keymap.inventory)) {
					inventory(sc);
					valid=true;
				}
				else if (cmd.equalsIgnoreCase(keymap.quit)) {
					quit=true;
					valid=true;
				}
				else {System.out.printf("Befehl nicht erkannt (Hilfe mit %s).\n",keymap.help);}
			}
		NPCMovement();
		}
			sc.close();
	}


	private static void NPCMovement() {
		for (npcList cur = WorldMap[WorldX][WorldY].npcs; cur != null; cur = cur.next) {
			//System.out.printf("\n%s, HP: %d, X: %d, Y: %d\n\n", cur.npc.name, cur.npc.actHP, cur.npcX, cur.npcY);
			}		
	}


	private static void inventory(Scanner sc5) {
		boolean quit=false;
		while (quit==false) {
		int inventoryID=1;
		int lookup[]=new int[items.length];
		String category=null;
		System.out.printf("Gegenstände in Besitz:\n");
		System.out.printf("---------------------:\n\n");
		System.out.printf(" #  Name                   Anz.     Kategorie  Wert\n");

		for (int i=0;items[i]!=null&&i<items.length;i++) {
			if (items[i].inPossession>0) {
				if (items[i].type==0) { category="Misc.";}
				else if (items[i].type==1) { category="Benutzbares";}
				else if (items[i].type==2) { category="Waffen";}
				else if (items[i].type==3) { category="Rüstungen";}
				else if (items[i].type==4) { category="Spezielles";}
				String inventoryItem=String.format("%03d %-20s (x%3d) %13.13s %d,-", inventoryID++, items[i].name, items[i].inPossession, category, items[i].value);
				System.out.println(inventoryItem);
				lookup[inventoryID-1]=i;
			}
		}
		
		System.out.printf("\naktuelle Waffe: %s, Stärke: %d\n%s\n", PC.actWeapon.name, PC.actWeapon.DMG, PC.actWeapon.description);
		System.out.printf("\naktuelle Rüstung: %s, Stärke: %d\n%s\n\n", PC.actArmor.name, PC.actArmor.AC, PC.actArmor.description);
		
        System.out.printf("(r) Rüstung ändern        (w) Waffe ändern     (i) Detailinfos\n");
        System.out.printf("(d) Gegenstand wegwerfen  (b) benutzen         (sonstige) weiter\n");
        System.out.printf("bitte auswählen: ");
        String cmd=sc5.next();
        
        if (cmd.equalsIgnoreCase("r")) {
        	changeArmor(sc5);
        } else if (cmd.equalsIgnoreCase("w")) {
        	changeWeapon(sc5);
        } else if (cmd.equalsIgnoreCase("i")) {
        	 System.out.printf("Details für welchen Gegenstand (#)? ");
             int selItem=sc5.nextInt();
             
             for (int i=0;i<lookup.length;i++) {
     			if (selItem>lookup.length||selItem<=0||items[i]==null) {
     				System.out.printf("ungültige Eingabe\n\n");
     				break;
     				}
     			if (i==lookup[selItem-1]) {
     	            itemDetails(items[i+1]);
     				break;
     			}
     		}
             
        } else if (cmd.equalsIgnoreCase("d")) {
        	
        } else if (cmd.equalsIgnoreCase("u")) {
        	
        } else {quit=true;}
		}
	}


	private static void itemDetails(item detailed) {
		String category=null;
		System.out.printf("\nDetails für %s: \nin Besitz: %d, Wert pro Stück: %d,-\n\n", detailed.name, detailed.inPossession, detailed.value);
		if (detailed.type==0) {
			category="Misc.";
			System.out.printf("Kategorie: %s\n", category);
			System.out.printf("Beschreibung: %s\n\n", detailed.description);
			}
		else if (detailed.type==1) {
			category="Benutzbares";
			System.out.printf("Kategorie: %s, noch benutzbar: %dx\n",category ,detailed.inPossession);
			System.out.printf("%s\n\n", detailed.description);
			}
		else if (detailed.type==2) {
			category="Waffen";
			System.out.printf("Kategorie: %s, Stärke: %d\n",category ,detailed.DMG);
			if (detailed.area>0) {System.out.printf("Vorsicht, Flächenschaden! ");}
			System.out.printf("Reichweite: %d\n",detailed.range);
			System.out.printf("%s\n\n", detailed.description);
			}
		else if (detailed.type==3) {
			category="Rüstung";
			System.out.printf("Kategorie: %s, Rüstungswert: %d\n",category ,detailed.AC);
			System.out.printf("%s\n\n", detailed.description);
			}
		else if (detailed.type==4) {
			category="Spezielles";
			System.out.printf("Kategorie: %s\n",category);
			System.out.printf("%s\n\n", detailed.description);
			}

	}


	private static void changeWeapon(Scanner sc5) {
		int weaponID=1;
		int lookup[]=new int[items.length];
		System.out.printf("\nWaffen in Besitz:\n");
		System.out.printf("-----------------\n\n");
		for (int i=0;items[i]!=null&&i<items.length;i++) {
			if (items[i].inPossession>0 && items[i].type==2) {
				lookup[weaponID-1]=i;
				String weaponItem=String.format("#%3d %-20S Angriff: %2d, Reichweite %2d - %s", weaponID++, items[i].name, items[i].DMG, items[i].range, items[i].description);
				System.out.println(weaponItem);
			}
		}
		System.out.printf("\naktuelle Waffe: %s, Stärke: %d\n%s\n\n", PC.actWeapon.name, PC.actWeapon.DMG, PC.actWeapon.description);
		System.out.printf("welche Waffe anlegen (#)? ");
		int selWeapon=sc5.nextInt();
		
		for (int i=0;i<lookup.length;i++) {
			if (selWeapon>lookup.length||selWeapon<=0||items[i]==null) {
				System.out.printf("ungültige Eingabe\n\n");
				break;
				}
			if (i==lookup[selWeapon-1]) {
				if (items[i].equippable==false) {System.out.printf("ungültige Eingabe\n\n");}
				else if (PC.actWeapon==items[i]) {System.out.printf("diese Waffe ist bereits angelegt.\n\n");}
				else {PC.actWeapon=items[i];
				System.out.printf("%s angelegt.\n\n",items[i].name);}
				break;
				}
		}
	}


	private static void changeArmor(Scanner sc5) {
		int armorID=1;
		int lookup[]=new int[items.length];
		System.out.printf("\nRüstungen in Besitz:\n");
		System.out.printf("-------------------:\n\n");
		for (int i=0;items[i]!=null&&i<items.length;i++) {
			if (items[i].inPossession>0 && items[i].type==3) {
				lookup[armorID-1]=i;
				String armorItem=String.format("#%3d %-20S Rüstungswert: %2d - %s", armorID++, items[i].name, items[i].AC, items[i].description);
				System.out.println(armorItem);
			}
		}
		System.out.printf("\naktuelle Rüstung: %s, Stärke: %d\n%s\n\n", PC.actArmor.name, PC.actArmor.AC, PC.actArmor.description);
		System.out.printf("welche Rüstung anlegen (#)? ");
		int selArmor=sc5.nextInt();
		
		for (int i=0;i<lookup.length;i++) {
			if (selArmor>lookup.length||selArmor<=0||items[i]==null) {
				System.out.printf("ungültige Eingabe\n\n");
				break;
				}
			if (i==lookup[selArmor-1]) {
				if (items[i].equippable==false) {System.out.printf("ungültige Eingabe\n\n");}
				else if (PC.actArmor==items[i]) {System.out.printf("diese Rüstung ist bereits angelegt.\n\n");}
				else {PC.actArmor=items[i];
				System.out.printf("%s angelegt.\n\n",items[i].name);}
				break;
				}
		}
	}


	private static void teleport(Scanner sc4) {
        System.out.printf("derzeitige Position auf Weltkarte: X(%d), Y(%d)\n",WorldX,WorldY);
        System.out.printf("bitte neue Position eingeben, gültige Werte von \"0 0\" bis \"%d %d\": ",WorldMap.length,WorldMap[0].length);
		WorldX= sc4.nextInt();
		WorldY= sc4.nextInt();
		if (WorldMap[WorldX][WorldY] == null) {iniMap();}

		System.out.printf("derzeitige Position auf Regionalkarte: X(%d), Y(%d)\n",LocalX,LocalY);
        System.out.printf("bitte neue Position eingeben, gültige Werte von \"0 0\" bis \"%d %d\": ",WorldMap[WorldX][WorldY].MapGrid.length,WorldMap[WorldX][WorldY].MapGrid[0].length);
		LocalX= sc4.nextInt();
		LocalY= sc4.nextInt();
		
		System.out.println("Position verändert!");
	}


	private static void iniMap() {
		WorldMap[WorldX][WorldY]=new OuterMap();
		WorldMap[WorldX][WorldY].distance=Math.abs(WorldX-(WorldMap.length/2))+Math.abs(WorldY-(WorldMap[0].length/2));
		
		for (int i=0;i<WorldMap[WorldX][WorldY].MapGrid.length;i++) {
			Arrays.fill(WorldMap[WorldX][WorldY].MapGrid[i],defaultGround);
		}
		for (int i=Math.min(WorldMap[WorldX][WorldY].distance, 9);i>0;i--) {
			generateMon();
		}
	}


	private static void showMap() {
        System.out.println("Weltkarte:");
        System.out.println("----------");
		for (int i = 0; i < WorldMap.length; i++) {
            for (int j = 0; j < WorldMap[0].length; j ++) {
            	if (i==WorldX&&j==WorldY) {System.out.printf("[%c]",PC.symbol);}
            	else if (WorldMap[i][j]==null) {System.out.printf("[%c]",defaultGround);}
            	else {System.out.printf("[%d]",WorldMap[i][j].npcCount);}
            	}
            System.out.println();
            }
        System.out.println("Distanz zum Startpunkt: " + WorldMap[WorldX][WorldY].distance);
        System.out.println();
	}


	private static char directionChar(String cmd) {
		if (cmd.equalsIgnoreCase(keymap.N)) {
			return WorldMap[WorldX][WorldY].MapGrid[LocalX-1][LocalY];
		} else if (cmd.equalsIgnoreCase(keymap.S)) {
			return WorldMap[WorldX][WorldY].MapGrid[LocalX+1][LocalY];
		} else if (cmd.equalsIgnoreCase(keymap.W)) {
			return WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY-1];	
		} else if (cmd.equalsIgnoreCase(keymap.O)) {
			return WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY+1];
		}
		return PC.symbol;
	}
	
	private static int[] directionCoords(String cmd, int i) {
		if (cmd.equalsIgnoreCase(keymap.N)) {
			int targetCoords[]= {LocalX-i,LocalY};
			return targetCoords;
		} else if (cmd.equalsIgnoreCase(keymap.S)) {
			int targetCoords[]= {LocalX+i,LocalY};
			return targetCoords;
		} else if (cmd.equalsIgnoreCase(keymap.W)) {
			int targetCoords[]= {LocalX,LocalY-i};
			return targetCoords;
		} else if (cmd.equalsIgnoreCase(keymap.O)) {
			int targetCoords[]= {LocalX,LocalY+i};
			return targetCoords;
		}  
		int targetCoords[]= {LocalX,LocalY};
		return targetCoords;
	}


	private static void printMon() {
		for (npcList cur = WorldMap[WorldX][WorldY].npcs; cur != null; cur = cur.next) {
			System.out.printf("\n%s, HP: %d, X: %d, Y: %d\n\n", cur.npc.name, cur.npc.actHP, cur.npcX, cur.npcY);
			}
		}


	private static void generateMon() {
		int i;
		for (i=0;monsters[i]!=null&&i<monsters.length;i++) {
			
		}
		i=(int)((i)*Math.random());
		NonPlayerCharacter newMon = new NonPlayerCharacter();
			newMon.name=monsters[i].name;
			newMon.description=monsters[i].description;
			newMon.EXP=(int) (monsters[i].EXP*(Math.random()*30+85)/100);
			newMon.STR=monsters[i].STR;
			newMon.DEX=monsters[i].DEX;
			newMon.INT=monsters[i].INT;
			newMon.CON=monsters[i].CON;
			
			newMon.maxHP=monsters[i].maxHP;
			newMon.actHP=monsters[i].maxHP;
			
			newMon.symbol=monsters[i].symbol;
			newMon.hostile=monsters[i].hostile;
			// shallow copy:
			newMon.actArmor=monsters[i].actArmor;
			newMon.actWeapon=monsters[i].actWeapon;
			
		npcList newNPC=new npcList();
		newNPC.npc=newMon;
		
		while (newNPC.npcX==null || newNPC.npcY==null) {
			int rndX=(int)((WorldMap[WorldX][WorldY].MapGrid.length-1)*Math.random());
			int rndY=(int)((WorldMap[WorldX][WorldY].MapGrid[0].length-1)*Math.random());
			if (WorldMap[WorldX][WorldY].MapGrid[rndX][rndY]==defaultGround) {
				newNPC.npcX=rndX;
				newNPC.npcY=rndY;
			}
			
			if ( WorldMap[WorldX][WorldY].npcs.npc== null ) {
				WorldMap[WorldX][WorldY].npcs=newNPC;
				WorldMap[WorldX][WorldY].npcCount++;
				WorldMap[WorldX][WorldY].MapGrid[newNPC.npcX][newNPC.npcY]=newNPC.npc.symbol;
				System.out.printf("\n%s erscheint plötzlich\n", newNPC.npc.name);
				return;
				}

			npcList cur;
			for (cur = WorldMap[WorldX][WorldY].npcs; cur.next != null; cur = cur.next)
				;

			cur.next = newNPC;
			WorldMap[WorldX][WorldY].npcCount++;

			WorldMap[WorldX][WorldY].MapGrid[newNPC.npcX][newNPC.npcY]=newNPC.npc.symbol;
			System.out.printf("\n%s erscheint plötzlich\n", newNPC.npc.name);
		}
		return;
	}

	private static void printStats() {

		System.out.printf("\nDetails für %s, Alter %d:\n\n", PC.name, PC.age);
		System.out.printf("Gesundheit: %2d/%2d, Geld: %d\n", PC.actHP, PC.maxHP, PC.EUR);
		System.out.printf("LVL: %2d   EXP: %2d\n", PC.LVL, PC.EXP);
		System.out.printf("STR: %2d   DEX: %2d\n", PC.STR, PC.DEX);
		System.out.printf("CON: %2d   INT: %2d\n", PC.CON, PC.INT);
		System.out.printf("Angriff:    0-%d, abhängig von der Waffe sowie von Stärke bei Nahkampfwaffen oder von Geschicklichkeit bei Fernwaffen\n"
				+ "Verteidigung: %d, abhängig von Rüstung und Geschicklichkeit\n", PC.actWeapon.DMG*(int)(PC.STR/3), PC.actArmor.AC+(int)(PC.DEX/3));
		System.out.printf("\naktuelle Waffe: %s, Stärke: %d\n%s\n", PC.actWeapon.name, PC.actWeapon.DMG, PC.actWeapon.description);
		System.out.printf("\naktuelle Rüstung: %s, Stärke: %d\n%s\n", PC.actArmor.name, PC.actArmor.AC, PC.actArmor.description);
//		System.out.printf("mit <Enter> zurück zur Karte\n");
//		sc.useDelimiter(""); //set up to read single char
//		String s = sc.next(); //one char read, returned as String
//		char cmd = s.charAt(0); //extract the char from String
//        sc.close();
	}


	private static void attack(Scanner sc2) {
		System.out.printf("in welche Richtung angreifen: ");
		String cmd = sc2.next(); 
		System.out.println();
        
		npcList prev=WorldMap[WorldX][WorldY].npcs;
		if (cmd.equalsIgnoreCase(keymap.N)||cmd.equalsIgnoreCase(keymap.S)||cmd.equalsIgnoreCase(keymap.O)||cmd.equalsIgnoreCase(keymap.W) ) {
			for (int i=1;i<=PC.actWeapon.range;i++) {
			for (npcList cur = WorldMap[WorldX][WorldY].npcs; cur != null&&cur.npc != null; cur = cur.next) {
				if (cur.npcX==directionCoords(cmd,i)[0] && cur.npcY==directionCoords(cmd,i)[1]) {
					if (cur.npc.hostile==false) {
						System.out.printf("%s scheint dir keine Gefahr zu sein. trotzdem angreifen (j/n)? ", cur.npc.name);
						String confirmAttack = sc2.next();
						if (confirmAttack.equals("j")) {cur.npc.hostile=true;}
						else return;
						}
					defendNPC(cur);
					if (cur.npc.actHP<=0) {
						defeatNPC(cur);
						prev.next=cur.next;
						}
					return;
					}
				prev=cur;
				}
				}
			System.out.printf("nichts zum Angreifen dort!\n");
			}
		else System.out.printf("Richtung nicht erkannt\n");
		}


	private static void defeatNPC(npcList cur) {
		int dropEUR=(int)(cur.npc.EXP*Math.random());
		PC.EXP=PC.EXP+cur.npc.EXP;
		PC.EUR=PC.EUR+dropEUR;
		System.out.printf("%s besiegt! Du erhältst %d Erfahrungspunkte und findest %d€.\n", cur.npc.name, cur.npc.EXP, dropEUR);
		
		WorldMap[WorldX][WorldY].MapGrid[cur.npcX][cur.npcY]=defaultGround;
		WorldMap[WorldX][WorldY].npcDefeated++;
		WorldMap[WorldX][WorldY].npcCount--;
	}


	private static void defendNPC(npcList cur) {
		int attRoll=PC.actWeapon.DMG*(int)(Math.random() * (PC.STR/3));
		int defRoll=cur.npc.actArmor.AC+(int)(Math.random() * (cur.npc.DEX/3));
		int attResult=attRoll-defRoll;
		
		System.out.printf("dein Angriffswurf: %d! ",attRoll);
		if (attRoll==(int)(PC.actWeapon.DMG*(PC.STR/3)))
		{
			System.out.printf("Volltreffer, doppelter Schaden!");
			attResult=attResult*2;
		}
		System.out.printf("Verteidigungswurf von %s: %d\n", cur.npc.name, defRoll);
		
		if (attRoll<=0) {System.out.printf("Du verfehlst %s.\n",cur.npc.name);}
		else if (attResult<=0) {System.out.printf("%s wehrt deinen Angriff ab.\n",cur.npc.name);}		
		else {
			System.out.printf("Du triffst %s für %d Schaden!\n",cur.npc.name,attResult);
			cur.npc.actHP=cur.npc.actHP-attResult;
			}
	}		

	private static void initialize() {
		
		//initialize modifiers
		modifier strPlus=new modifier();
			strPlus.POW=10;
			
			
		//initialize items
			int itemID=0;
			
		item redHerring=new item();
			redHerring.name="roter Hering";
			redHerring.description="du hast wirklich absolut keine Ahnung, was du damit anfangen könntest.";
			redHerring.type=0;
			redHerring.ItemID=itemID;
			
			items[itemID]=redHerring;
			itemID++;
			
		item cookie=new item();
			cookie.name="Cookie";
			cookie.description="ein richtig fetter Cookie mit Heidelbeeren. sieht gut aus, schmeckt wahrscheinlich noch besser.";
			cookie.type=1;
			cookie.usable=true;
			cookie.POW=20;
			cookie.ItemID=itemID;

			items[itemID]=cookie;
			itemID++;
			
		item throwKnife=new item();
			throwKnife.name="Wurfmesser";
			throwKnife.description="spitz UND scharf - somit ist es fast egal, wie du triffst.";
			throwKnife.type=2;
			throwKnife.DMG=40;
			throwKnife.equippable=true;
			throwKnife.range=5;
			throwKnife.ItemID=itemID;

			items[itemID]=throwKnife;
//			weaponList.add(throwKnife);
			itemID++;

		item bareHands=new item();
			bareHands.name="Hände";
			bareHands.description="deine bloßen Hände. ob sie zum Schminken oder zum Töten verwendet werden, hängt nur von dir ab.";
			bareHands.type=2;
			bareHands.DMG=1;
			bareHands.equippable=true;
			bareHands.ItemID=itemID;
			
			items[itemID]=bareHands;
//			weaponList.add(bareHands);
			itemID++;
			
		item claws=new item();
			claws.name="Extremitäten";
			claws.description="Klauen, Scheren, Hufe, etc.";
			claws.type=2;
			claws.DMG=1;
			claws.equippable=true;
			claws.ItemID=itemID;
			
			items[itemID]=claws;
//			weaponList.add(claws);
			itemID++;

		item dragonBreath=new item();
			dragonBreath.name="Drachenfeuer";
			dragonBreath.description="lauf oder brenne";
			dragonBreath.type=2;
			dragonBreath.DMG=50;
			dragonBreath.range=5;
			dragonBreath.equippable=true;
			dragonBreath.ItemID=itemID;
			
			items[itemID]=dragonBreath;
//			weaponList.add(dragonBreath);
			itemID++;
			
		item magicSword=new item();
			magicSword.name="magisches Schwert";
			magicSword.description="sicher +1. wenn nicht mehr.";
			magicSword.type=2;
			magicSword.DMG=60;
			magicSword.equippable=true;
			magicSword.ItemID=itemID;
			
			items[itemID]=magicSword;
//			weaponList.add(magicSword);
			itemID++;

			item clothes=new item();
			clothes.name="Strassenkleidung";
			clothes.description="was du heute früh schnell im Schrank gefunden hast. nicht wirklich hilfreich jetzt.";
			clothes.type=3;
			clothes.AC=0;
			clothes.equippable=true;
			clothes.ItemID=itemID;
			
			items[itemID]=clothes;
//			armorList.add(clothes);
			itemID++;
			
		item dragonScale=new item();
			dragonScale.name="Drachenschuppen";
			dragonScale.description="die einzelnen Schuppen glänzen wunderschön in den verschiedensten Farben.";
			dragonScale.type=3;
			dragonScale.AC=10;
			dragonScale.equippable=true;
			dragonScale.ItemID=itemID;
			
			items[itemID]=dragonScale;
//			armorList.add(dragonScale);
			itemID++;
			
		item plateArmor=new item();
			plateArmor.name="Plattenrüstung";
			plateArmor.description="schwer, hart und fast undurchdringlich.";
			plateArmor.type=3;
			plateArmor.AC=8;
			plateArmor.equippable=true;
			plateArmor.ItemID=itemID;
			
			items[itemID]=plateArmor;
//			armorList.add(plateArmor);
			itemID++;
			
		item fur=new item();
			fur.name="Tiergewand";
			fur.description="was eben Tiere üblicherweise tragen, Fell, Schale, Schuppen, Federn und so.";
			fur.type=3;
			fur.AC=1;
			fur.equippable=true;
			fur.ItemID=itemID;
			
			items[itemID]=fur;
//			armorList.add(fur);
			itemID++;
			
		item brokenLaptop=new item();
			brokenLaptop.name="kaputter Laptop";
			brokenLaptop.description="man kann versuchen, damit wenigstens ein paar Schläge abzuwehren. mit Betonung auf 'versuchen'.";
			brokenLaptop.type=4;
			brokenLaptop.ItemID=itemID;
			
			items[itemID]=brokenLaptop;
			itemID++;
		
		//initialize NPCs
			int npcID=0;
			NonPlayerCharacter giantRat=new NonPlayerCharacter();
				giantRat.name="Riesenratte";
				giantRat.description="eine wirklich gewaltige Ratte";
				giantRat.EXP=100;
				giantRat.sex='W';
				giantRat.STR=6;
				giantRat.DEX=6;
				giantRat.INT=6;
				giantRat.CON=6;
				
				giantRat.maxHP=20;
				
				giantRat.symbol='R';
				giantRat.hostile=true;
				
				giantRat.actArmor=fur;
				giantRat.actWeapon=claws;
				
				monsters[npcID]=giantRat;
				npcID++;
			
				NonPlayerCharacter giantSnake=new NonPlayerCharacter();
					giantSnake.name="Flederschlange";
					giantSnake.description="eine ziemlich große Schlange - aber mit Flügeln.";
					giantSnake.EXP=150;
					giantSnake.sex='W';
					giantSnake.STR=6;
					giantSnake.DEX=6;
					giantSnake.INT=6;
					giantSnake.CON=6;
					
					giantSnake.maxHP=20;
					
					giantSnake.symbol='S';
					giantSnake.hostile=true;
					
					giantSnake.actArmor=fur;
					giantSnake.actWeapon=claws;
					
					monsters[npcID]=giantSnake;
					npcID++;
					
				NonPlayerCharacter Questor=new NonPlayerCharacter();
					Questor.name="Dummy";
					Questor.description="Er scheint dir nichts zu tun.";
					Questor.EXP=15000;
					Questor.sex='M';
					Questor.STR=16;
					Questor.DEX=16;
					Questor.INT=126;
					Questor.CON=16;
					
					Questor.maxHP=2000;
					Questor.actHP=2000;

					Questor.symbol='?';
					Questor.hostile=false;
					
					Questor.actWeapon=magicSword;
					Questor.actArmor=plateArmor;
					
					//initialize friendly NonPlayer Characters
					WorldMap[WorldX][WorldY].npcs.npc=Questor;
					WorldMap[WorldX][WorldY].npcs.npcX=0;
					WorldMap[WorldX][WorldY].npcs.npcY=5;
					WorldMap[WorldX][WorldY].npcCount++;
					WorldMap[WorldX][WorldY].MapGrid[0][5]=Questor.symbol;
	}


	private static boolean movement(String cmd, Scanner sc3) {
		if (cmd.equalsIgnoreCase(keymap.N)) {
			if (LocalX==0) {
				System.out.printf("Bereichswechsel? (j/n) ");
				String ack = sc3.next(); 
				System.out.println();
				if (ack.equalsIgnoreCase("n")) {
					System.out.println("kein Bereichswechsel");
				} else if (ack.equalsIgnoreCase("j")) {
					if (WorldX==0) {System.out.println("Hier geht es nicht mehr weiter. So weit du sehen kannst, nur unendliche Leere.");}
					else {
						WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=defaultGround;
						WorldX--;
						if (WorldMap[WorldX][WorldY] == null) {iniMap();}
						LocalX=WorldMap[WorldX][WorldY].MapGrid.length-1;
						return true;
					}
				} else {System.out.println("Eingabe nicht erkannt!");}
				return false;
				} else { if (directionChar(cmd)==defaultGround) {
					WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=defaultGround;
					LocalX--;
					return true;
				} else { System.out.printf("du kannst dort nicht hingehen!\n"); }
				}
			}
		else if (cmd.equalsIgnoreCase(keymap.S)) {
			if (LocalX==WorldMap[WorldX][WorldY].MapGrid.length-1) {
				System.out.printf("Bereichswechsel? (j/n) ");
				String ack = sc3.next(); 
				System.out.println();
				if (ack.equalsIgnoreCase("n")) {
					System.out.println("kein Bereichswechsel");
				} else if (ack.equalsIgnoreCase("j")) {
					if (WorldX==WorldMap.length-1) {System.out.println("Hier geht es nicht mehr weiter. So weit du sehen kannst, nur unendliche Leere.");}
					else {
						WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=defaultGround;
						WorldX++;
						if (WorldMap[WorldX][WorldY] == null) {iniMap();}
						LocalX=0;
						return true;
					}
				} else {System.out.println("Eingabe nicht erkannt!");}
				return false;
				} else { if (directionChar(cmd)==defaultGround) {
					WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=defaultGround;
					LocalX++;
					return true;
				} else { System.out.printf("du kannst dort nicht hingehen!\n"); }
				}
			}
		else if (cmd.equalsIgnoreCase(keymap.W)) {
			if (LocalY==0) {
				System.out.printf("Bereichswechsel? (j/n) ");
				String ack = sc3.next(); 
				System.out.println();
				if (ack.equalsIgnoreCase("n")) {
					System.out.println("kein Bereichswechsel");
				} else if (ack.equalsIgnoreCase("j")) {
					if (WorldY==WorldMap[0].length-1) {System.out.println("Hier geht es nicht mehr weiter. So weit du sehen kannst, nur unendliche Leere.");}
					else {
						WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=defaultGround;
						WorldY--;
						if (WorldMap[WorldX][WorldY] == null) {iniMap();}
						LocalY=WorldMap[WorldX][WorldY].MapGrid[0].length-1;
						return true;
					}
				} else {System.out.println("Eingabe nicht erkannt!");}
				return false;
				} else { if (directionChar(cmd)==defaultGround) {
					WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=defaultGround;
					LocalY--;
					return true;
				} else { System.out.printf("du kannst dort nicht hingehen!\n"); }
				}
			}
		else if (cmd.equalsIgnoreCase(keymap.O)) {
			if (LocalY==WorldMap[WorldX][WorldY].MapGrid[0].length-1) {
				System.out.printf("Bereichswechsel? (j/n) ");
				String ack = sc3.next(); 
				System.out.println();
				if (ack.equalsIgnoreCase("n")) {
					System.out.println("kein Bereichswechsel");
				} else if (ack.equalsIgnoreCase("j")) {
					if (WorldY==0) {System.out.println("Hier geht es nicht mehr weiter. So weit du sehen kannst, nur unendliche Leere.");}
					else {
						WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=defaultGround;
						WorldY++;
						if (WorldMap[WorldX][WorldY] == null) {iniMap();}
						LocalY=0;
						return true;
					}
				} else {System.out.println("Eingabe nicht erkannt!");}
				return false;
				} else { if (directionChar(cmd)==defaultGround) {
					WorldMap[WorldX][WorldY].MapGrid[LocalX][LocalY]=defaultGround;
					LocalY++;
					return true;
				} else { System.out.printf("du kannst dort nicht hingehen!\n"); }
				}
			}
		return false;
		}
	}
