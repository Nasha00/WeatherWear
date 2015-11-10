package org.reyan.weatherwear.domain;

/**
 * Created by reyan on 11/4/15.
 */
public class Dressing {

    private String hat = "";
    private String glasses = "";
    private String neck = "";
    private String upper = "";
    private String lower = "";
    private String dress = "";
    private String shoes = "";
    private String bags = "";
    private String umbrella = "";
    private String glove = "";

    public void clear() {
        hat = "";
        glasses = "";
        neck = "";
        upper = "";
        lower = "";
        dress = "";
        shoes = "";
        bags = "";
        umbrella = "";
        glove = "";
    }

    public String getHat() { return hat; }
    public void setHat(String hat) { this.hat = hat; }

    public String getGlasses() { return glasses; }
    public void setGlasses(String glasses) { this.glasses = glasses; }

    public String getNeck() { return neck; }
    public void setNeck(String neck) { this.neck = neck; }

    public String getUpper() { return upper; }
    public void setUpper(String upper) { this.upper = upper; }

    public String getLower() { return lower; }
    public void setLower(String lower) { this.lower = lower; }

    public String getDress() { return dress; }
    public void setDress(String dress) { this.dress = dress; }

    public String getShoes() { return shoes; }
    public void setShoes(String shoes) { this.shoes = shoes; }

    public String getBags() { return bags; }
    public void setBags(String bags) { this.bags = bags; }

    public String getUmbrella() { return umbrella; }
    public void setUmbrella(String umbrella) { this.umbrella = umbrella; }

    public String getGlove() { return glove; }
    public void setGlove(String glove) { this.glove = glove; }

    @Override
    public String toString() {
        String suggestion = new String();
        suggestion += "The recommendation is:\n";
        if(!"".equals(hat)) {
            suggestion += this.hat;
            suggestion += "\n";
        }
        if (!"".equals(glasses)) {
            suggestion += this.glasses;
            suggestion += "\n";
        }
        if (!"".equals(neck)) {
            suggestion += this.neck;
            suggestion += "\n";
        }
        if (!"".equals(upper)) {
            suggestion += this.upper;
            suggestion += "\n";
        }
        if (!"".equals(lower)) {
            suggestion += this.lower;
            suggestion += "\n";
        }
        if (!"".equals(dress)) {
            suggestion += this.dress;
            suggestion += "\n";
        }
        if (!"".equals(shoes)) {
            suggestion += this.shoes;
            suggestion += "\n";
        }
        if (!"".equals(bags)) {
            suggestion += this.bags;
            suggestion += "\n";
        }
        if (!"".equals(umbrella)) {
            suggestion += this.umbrella;
            suggestion += "\n";
        }
        if (!"".equals(glove)) {
            suggestion += this.glove;
        }
        return suggestion;
    }

}
