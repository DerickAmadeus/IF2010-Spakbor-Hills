package Items;

import main.GamePanel;

public interface Buyable {

    public void buy(GamePanel gp, Item item, int amount);
}