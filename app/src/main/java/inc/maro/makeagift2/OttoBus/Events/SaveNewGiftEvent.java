package inc.maro.makeagift2.OttoBus.Events;

public class SaveNewGiftEvent extends GiftEventSave {

   public SaveNewGiftEvent(String description, String target, String date){
       setDescription(description);
       setDate(date);
       setTarget(target);
   }
}