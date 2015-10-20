/**
 * Created by manoharprabhu on 10/15/2015.
 */
public class Dialogue {
    private int dialogueNumber;
    private long startTime;
    private long endTime;
    private String dialogueText;

    public Dialogue(int dialogueNumber, long startTime, long endTime, String dialogueText){
        this.dialogueNumber = dialogueNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dialogueText = dialogueText;
    }

    public int getDialogueNumber() {
        return dialogueNumber;
    }

    public void setDialogueNumber(int dialogueNumber) {
        this.dialogueNumber = dialogueNumber;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getDialogueText() {
        return dialogueText;
    }

    public void setDialogueText(String dialogueText) {
        this.dialogueText = dialogueText;
    }

    @Override
    public String toString(){
        return this.startTime + " -> " + this.endTime + " = " + this.dialogueText;
    }
}
