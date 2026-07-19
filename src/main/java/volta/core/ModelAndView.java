package volta.core;

import java.util.HashMap;

public class ModelAndView {
    HashMap<String,Object> attribute;
    String view ;

    public ModelAndView(){
        this.attribute = new HashMap<>();
    }
    public HashMap<String, Object> getAttribute() {
        return attribute;
    }
    public void setAttribute(HashMap<String, Object> attribute) {
        this.attribute = attribute;
    }

    public void addAttribute(String nomAttribute, Object atribute){
        attribute.put(nomAttribute, atribute);
    }
    public String getView() {
        return view;
    }
    public void setView(String view) {
        this.view = view;
    }
}
