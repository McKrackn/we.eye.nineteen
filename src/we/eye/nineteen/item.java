package we.eye.nineteen;

class item {
	String name;
	String description;
	int inPossession=1;
	int ItemID;
	int type; //0=misc,1=consumable,2=weapon,3=armor,4=special
	boolean equippable=false;
	boolean usable=false;
	boolean identified=true;
	int POW;
	int range=1;
	int area=0;
	//int modAttr; //0=EXP,1=maxHP,2=actHP,3=STR,4=DEX,5=CON,6=INT,7=actWeapon.DWM,8=actArmor.AC
	int value;
	
	int AC; //only if armor
	int DMG; //only if weapon

	modifier modifier=new modifier();
	}
