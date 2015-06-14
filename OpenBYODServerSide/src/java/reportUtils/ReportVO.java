/*
 * This class is used to leave vlues to the Performance reports bean
 * - Reports of executed dials in a month, by user
 * - Reports of sent and received emails in a mounth by user
 */

package reportUtils;

/**
 *
 * @author Castro
 */
public class ReportVO {
    
        private int qtde;

    private int qtde2;

    /**
     * Get the value of qtde2
     *
     * @return the value of qtde2
     */
    public int getQtde2() {
        return qtde2;
    }

    /**
     * Set the value of qtde2
     *
     * @param qtde2 new value of qtde2
     */
    public void setQtde2(int qtde2) {
        this.qtde2 = qtde2;
    }

    /**
     * Get the value of qtde
     *
     * @return the value of qtde
     */
    public int getQtde() {
        return qtde;
    }

    /**
     * Set the value of qtde
     *
     * @param qtde new value of qtde
     */
    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

        private String label;

    /**
     * Get the value of label
     *
     * @return the value of label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the value of label
     *
     * @param label new value of label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public ReportVO(int qtde, String label) {
        this.qtde = qtde;
        this.label = label;
    }
    
    

}
