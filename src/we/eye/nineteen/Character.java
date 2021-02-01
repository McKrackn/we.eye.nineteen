package we.eye.nineteen;

public abstract class Character {

	String name;
	String description;
	protected char sex;
	protected int EXP = 0;
	protected int maxHP;
	protected int actHP;
	protected int STR = 0;
	protected int DEX = 0;
	protected int CON = 0;
	protected int INT = 0;
	protected item actWeapon;
	protected item actArmor;
	protected item actSpecial;
	protected char symbol = 182;

	public Character() {
		super();
	}

}