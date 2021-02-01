package we.eye.nineteen;

class OuterMap {
	char[][] MapGrid=new char[(int)(Math.max(Main.LocalX+1, Math.random()*10+5))][Math.max(Main.LocalY+1, (int)(Math.random()*15+15))];
	npcList npcs=new npcList();
	int npcCount=0;
	int npcDefeated=0;
	int distance;
	char symbol=we.eye.nineteen.Main.defaultGround;
}
