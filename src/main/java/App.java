import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {

        String bestChargeMessage = "============= Order details =============\n";
        int totalPriceWithoutSales = 0;
        int promotion_1_save = 0;
        int promotion_2_save = 0;
        String promotion_food = null;
        int total = 0;
        List<String> halfPriceItemList = salesPromotionRepository.findAll().get(1).getRelatedItems();

        for (String input : inputs) {
            String[] splited = input.split("\\s+");
            String itemID = splited[0];
            String itemName = null;
            int itemPrice = 0;
            int numItemToBuy = Integer.parseInt(splited[2]);

            for (Item item : itemRepository.findAll()) {
                if (item.getId().equals(itemID)) {
                    itemName = item.getName();
                    itemPrice = (int) item.getPrice();
                    break;
                }
            }
            bestChargeMessage += itemName + " x " + numItemToBuy + " = " + numItemToBuy * itemPrice + " yuan\n";
            totalPriceWithoutSales += numItemToBuy * itemPrice;

            if (totalPriceWithoutSales >= 30) {
                promotion_1_save = 6;
            }

            if (halfPriceItemList.contains(itemID)) {
                if (promotion_food != null) {
                    promotion_food += ", " + itemName;
                } else {
                    promotion_food = itemName;
                }
                promotion_2_save += numItemToBuy * itemPrice / 2;
            }
        }

        bestChargeMessage += "-----------------------------------\n";
        if (promotion_2_save == 0 && promotion_1_save == 0) {
            total = totalPriceWithoutSales;
        } else if (promotion_2_save > promotion_1_save) {
            bestChargeMessage += "Promotion used:\n";
            bestChargeMessage += salesPromotionRepository.findAll().get(1).getDisplayName() + " (" + promotion_food + "), " + "saving " + promotion_2_save + " yuan\n-----------------------------------\n";
            total = totalPriceWithoutSales - promotion_2_save;
        } else {
            bestChargeMessage += "Promotion used:\n";
            bestChargeMessage += salesPromotionRepository.findAll().get(0).getDisplayName() + ", saving " + promotion_1_save + " yuan\n-----------------------------------\n";
            total = totalPriceWithoutSales - promotion_1_save;
        }
        bestChargeMessage += "Total: " + total + " yuan\n===================================";
        return bestChargeMessage;
    }
}